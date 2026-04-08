# 步骤5：迭代器 - 统一集合遍历

---

## 5.1 Iterator接口概述

### 5.1.1 为什么需要迭代器

```java
// 传统遍历方式的缺点
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
list.add("c");

// 方式1：索引遍历（只适合List）
for (int i = 0; i < list.size(); i++) {
    String s = list.get(i);  // LinkedList的get(i)是O(n)
}

// 方式2：for-each（内部也是Iterator）
for (String s : list) {
    // ...
}

// 问题：如果在遍历过程中删除元素，会出现ConcurrentModificationException
for (String s : list) {
    list.remove(s);  // 报错！
}
```

### 5.1.2 迭代器的优势

```java
// 使用Iterator安全删除
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("b")) {
        it.remove();  // 安全删除，不会ConcurrentModificationException
    }
}

// 统一遍历方式：所有Collection都可用Iterator
List<String> list = new ArrayList<>();
Set<String> set = new HashSet<>();
Map<String, Integer> map = new HashMap<>();

// 遍历List
for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
    String s = it.next();
}

// 遍历Set
for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
    String s = it.next();
}

// 遍历Map - 遍历keySet
for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
    String key = it.next();
}
```

---

## 5.2 Iterator接口定义

```java
public interface Iterator<E> {
    // 是否还有下一个元素
    boolean hasNext();

    // 返回下一个元素并前进
    E next();

    // 删除最后一个返回的元素（JDK 8+可选操作）
    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    // 对每个剩余元素执行操作（JDK 8+）
    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }
}
```

---

## 5.3 Iterable接口

### 5.3.1 Iterable接口定义

```java
public interface Iterable<T> {
    // 返回迭代器
    Iterator<T> iterator();

    // JDK 8+ forEach方法
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }

    // JDK 8+ spliterator
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}

// 所有集合都实现了Iterable接口，所以都能用for-each
public interface Collection<E> extends Iterable<E> { ... }
```

### 5.3.2 for-each的实现原理

```java
// for-each语法糖
for (String s : list) {
    System.out.println(s);
}

// 编译后等价于
for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
    String s = it.next();
    System.out.println(s);
}
```

---

## 5.4 ListIterator接口

### 5.4.1 ListIterator特点

- 只能在List中使用
- 可以双向遍历
- 可以添加、修改元素
- 可以获取当前位置

### 5.4.2 ListIterator接口定义

```java
public interface ListIterator<E> extends Iterator<E> {
    // 双向移动
    boolean hasNext();      // 向前
    E next();
    boolean hasPrevious();  // 向后
    E previous();

    // 位置
    int nextIndex();
    int previousIndex();

    // 修改
    void set(E e);          // 修改最后一个返回的元素
    void add(E e);          // 添加元素
    void remove();          // 删除最后一个返回的元素
}
```

### 5.4.3 ListIterator使用示例

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
ListIterator<String> it = list.listIterator();

// 正向遍历
while (it.hasNext()) {
    int idx = it.nextIndex();
    String s = it.next();
    System.out.println(idx + ": " + s);
}

// 反向遍历
while (it.hasPrevious()) {
    int idx = it.previousIndex();
    String s = it.previous();
    System.out.println(idx + ": " + s);
}

// 在指定位置添加
ListIterator<String> it2 = list.listIterator(1);  // 从索引1开始
it2.add("x");  // 在位置1添加x

// 修改元素
ListIterator<String> it3 = list.listIterator();
while (it3.hasNext()) {
    String s = it3.next();
    if (s.equals("b")) {
        it3.set("bb");  // 将b改为bb
    }
}

// 先add再set
ListIterator<String> it4 = list.listIterator();
while (it4.hasNext()) {
    String s = it4.next();
    if (s.equals("x")) {
        it4.set("y");  // set是修改刚返回的元素
    }
}
```

---

## 5.5 源码解析

### 5.5.1 ArrayList的Iterator实现

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, Serializable {

    // 内部类 - Itr实现Iterator
    private class Itr implements Iterator<E> {
        int cursor;       // 下一个返回元素的索引
        int lastRet = -1; // 上一个返回元素的索引
        int expectedModCount = modCount;  // 期望的修改次数

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();  // 检查并发修改
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();
            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;  // 更新期望值
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        // 检查并发修改
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // 内部类 - ListItr实现ListIterator
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = ArrayList.this.elementData;
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();
            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();
            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    // iterator()返回Itr
    public Iterator<E> iterator() {
        return new Itr();
    }

    // listIterator()返回ListItr
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public ListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }
}
```

### 5.5.2 HashMap的Iterator实现

```java
public class HashMap<K, V> extends AbstractMap<K, V> {

    // HashMap的迭代器基类
    abstract class HashIterator {
        Node<K, V> next;        // 下一个节点
        Node<K, V> current;     // 当前节点
        int expectedModCount;   // 期望修改次数
        int index;              // 当前桶索引

        HashIterator() {
            expectedModCount = modCount;
            Node<K, V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) {
                // 找到第一个非空桶
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<K, V> nextNode() {
            Node<K, V>[] t;
            Node<K, V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            current = e;
            next = (current.next != null) ? current.next :
                   (t = table) != null ? nextNode(t) : null;
            return e;
        }

        private Node<K, V> nextNode(Node<K, V>[] t) {
            do {
            } while (index < t.length && (next = t[index++]) == null);
            return null;
        }

        public final void remove() {
            Node<K, V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            HashMap.this.removeNode(p.key, null, false, false);
            expectedModCount = modCount;
        }
    }

    // keySet的迭代器
    final class KeyIterator extends HashIterator implements Iterator<K> {
        public final K next() { return nextNode().key; }
    }

    // values的迭代器
    final class ValueIterator extends HashIterator implements Iterator<V> {
        public final V next() { return nextNode().value; }
    }

    // entrySet的迭代器
    final class EntryIterator extends HashIterator implements Iterator<Map.Entry<K, V>> {
        public final Map.Entry<K, V> next() { return nextNode(); }
    }
}
```

---

## 5.6 fail-fast机制

### 5.6.1 什么是fail-fast

```java
// fail-fast：快速失败机制
// 当检测到并发修改时，立即抛出ConcurrentModificationException

List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

// 错误示例
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);  // 抛出ConcurrentModificationException
    }
}

// 正确示例1：使用Iterator删除
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("b")) {
        it.remove();  // 安全删除
    }
}

// 正确示例2：JDK 8+ removeIf
list.removeIf(s -> s.equals("b"));
```

### 5.6.2 fail-safe机制

```java
// fail-safe：fail-fast的替代方案
// 复制一份数据遍历，不受原集合修改影响

// CopyOnWriteArrayList
CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<>();
cowList.add("a");
cowList.add("b");
cowList.add("c");

for (String s : cowList) {
    cowList.remove("b");  // 不会抛出ConcurrentModificationException
}

// ConcurrentHashMap
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("a", 1);
map.put("b", 2);
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    // 安全遍历
}
```

### 5.6.3 modCount与expectedModCount

```java
// AbstractList中的modCount
protected transient int modCount = 0;

// 每次修改集合（add/remove/clear）modCount++

// ArrayList的迭代器保存初始的modCount
int expectedModCount = modCount;

// 每次迭代器操作前检查
if (modCount != expectedModCount)
    throw new ConcurrentModificationException();
```

---

## 5.7 Enumeration接口

### 5.7.1 Enumeration vs Iterator

```java
// Enumeration是古老的迭代器接口（JDK 1.0）
// Vector、Hashtable等遗留类使用

// 方法只有两个
public interface Enumeration<E> {
    boolean hasMoreElements();
    E nextElement();
}

// 转换为Iterator
Collections.list(vector.elements()).iterator();

// 转换为Iterable（需要包装）
Iterators.forEnumeration(Collections.enumeration(list));
```

---

## 5.8 遍历方式总结

### 5.8.1 List遍历方式

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

// 方式1：普通for循环
for (int i = 0; i < list.size(); i++) {
    String s = list.get(i);
}

// 方式2：for-each（JDK 5+）
for (String s : list) {
    // 内部使用Iterator
}

// 方式3：Iterator（JDK 1.2+）
for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
    String s = it.next();
}

// 方式4：ListIterator（JDK 1.2+）
ListIterator<String> it = list.listIterator();

// 方式5：forEach（JDK 8+）
list.forEach(s -> System.out.println(s));

// 方式6：Stream（JDK 8+）
list.stream().forEach(System.out::println);

// 方式7：parallelStream（JDK 8+）多线程
list.parallelStream().forEach(System.out::println);

// 方式8：forEachRemaining（JDK 8+）
Iterator<String> it = list.iterator();
it.forEachRemaining(System.out::println);
```

### 5.8.2 Set遍历方式

```java
Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c"));

// 方式1：for-each
for (String s : set) {
    // 内部使用Iterator
}

// 方式2：Iterator
Iterator<String> it = set.iterator();
while (it.hasNext()) {
    String s = it.next();
}

// 方式3：forEach（JDK 8+）
set.forEach(s -> System.out.println(s));

// 方式4：Stream
set.stream().forEach(System.out::println);
```

### 5.8.3 Map遍历方式

```java
Map<String, Integer> map = new HashMap<>();
map.put("a", 1);
map.put("b", 2);
map.put("c", 3);

// 方式1：遍历keySet
for (String key : map.keySet()) {
    Integer value = map.get(key);
}

// 方式2：遍历entrySet（推荐，性能更好）
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    String key = entry.getKey();
    Integer value = entry.getValue();
}

// 方式3：Iterator遍历entrySet
Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
while (it.hasNext()) {
    Map.Entry<String, Integer> entry = it.next();
}

// 方式4：forEach（JDK 8+）
map.forEach((k, v) -> System.out.println(k + " -> " + v));

// 方式5：遍历values
for (Integer value : map.values()) {
    System.out.println(value);
}

// 方式6：Stream遍历entrySet
map.entrySet().stream()
    .filter(e -> e.getValue() > 1)
    .forEach(e -> System.out.println(e.getKey()));
```

---

## 5.9 迭代器性能对比

### 5.9.1 遍历方式性能

| 遍历方式 | ArrayList | LinkedList | HashSet | TreeSet |
|----------|-----------|------------|---------|---------|
| fori | O(1) per | O(n) per | 不支持 | 不支持 |
| for-each | O(1) per | O(1) per | O(1) per | O(log n) per |
| Iterator | O(1) per | O(1) per | O(1) per | O(log n) per |
| stream | O(1) per | O(1) per | O(1) per | O(log n) per |

### 5.9.2 选择建议

```java
// List遍历
// ArrayList: fori 或 for-each 或 stream 性能一样好
// LinkedList: 避免fori（O(n²)），使用 for-each 或 stream

// Set遍历
// for-each 和 stream 性能差不多

// Map遍历
// 推荐 entrySet.forEach 或 for-each entrySet
// 避免 多次调用get() 根据key获取value
```

---

## 5.10 练习题

```java
// 1. 实现一个方法，移除List中的所有偶数（使用Iterator）

// 2. 实现一个方法，反向遍历LinkedList（不使用 Collections.reverse）

// 3. 说明以下代码的输出结果
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
Iterator<String> it = list.iterator();
list.add("d");
while (it.hasNext()) {
    System.out.println(it.next());
}

// 4. 实现一个方法，将两个List合并并去重

// 5. 使用Iterator实现一个简单的无限迭代器（循环遍历List）
```

---

## 5.11 参考答案

```java
// 1. 移除所有偶数
public static void removeEvens(List<Integer> list) {
    Iterator<Integer> it = list.iterator();
    while (it.hasNext()) {
        if (it.next() % 2 == 0) {
            it.remove();
        }
    }
}

// 2. 反向遍历LinkedList
public static <E> void reverseTraverse(LinkedList<E> list) {
    ListIterator<E> it = list.listIterator(list.size());
    while (it.hasPrevious()) {
        System.out.println(it.previous());
    }
}

// 3. 输出：抛出ConcurrentModificationException
// 因为在获取Iterator后修改了List

// 4. 合并去重
public static <T> List<T> mergeUnique(List<T> list1, List<T> list2) {
    Set<T> set = new LinkedHashSet<>(list1);
    set.addAll(list2);
    return new ArrayList<>(set);
}

// 5. 无限迭代器
class CyclingIterator<T> implements Iterator<T> {
    private final List<T> list;
    private Iterator<T> it;

    public CyclingIterator(List<T> list) {
        this.list = list;
        this.it = list.iterator();
    }

    public boolean hasNext() {
        return !list.isEmpty();
    }

    public T next() {
        if (!it.hasNext()) {
            it = list.iterator();
        }
        return it.next();
    }

    public void remove() {
        it.remove();
    }
}
```

---

[返回目录](./README.md)

## 第二阶段总结

### 核心知识点

| 步骤 | 主题 | 核心概念 |
|------|------|----------|
| 1 | List | ArrayList, LinkedList, Vector, 扩容机制 |
| 2 | Set | HashSet, LinkedHashSet, TreeSet, 去重原理 |
| 3 | Map | HashMap, LinkedHashMap, TreeMap, 哈希查找 |
| 4 | Queue | LinkedList, ArrayDeque, PriorityQueue |
| 5 | Iterator | fail-fast, ListIterator, 遍历方式 |
