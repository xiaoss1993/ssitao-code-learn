# 步骤1：List集合 - 有序、可重复的序列

---

## 1.1 List接口概述

### 1.1.1 List的特点

- **有序**: 元素按照插入顺序存储
- **可重复**: 允许存储相同的元素
- **索引访问**: 通过整数索引访问元素
- **动态大小**: 容量自动增长

### 1.1.2 List接口定义

```java
public interface List<E> extends Collection<E> {
    // 位置访问
    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    E remove(int index);

    // 查找
    int indexOf(Object o);
    int lastIndexOf(Object o);

    // 列表操作
    List<E> subList(int fromIndex, int toIndex);
}
```

### 1.1.3 List的实现类

```
List
├── ArrayList    - 基于动态数组
├── LinkedList   - 基于双向链表
└── Vector       - 线程安全的动态数组
    └── Stack    - 栈结构（继承Vector）
```

---

## 1.2 ArrayList

### 1.2.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | Object[] elementData |
| 初始容量 | 10（第一次添加时扩容） |
| 扩容机制 | `newCapacity = oldCapacity * 1.5 + 1` |
| 线程安全 | 否 |
| 适用场景 | 随机访问、顺序遍历 |

### 1.2.2 源码分析

```java
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    // 存储元素的数组
    transient Object[] elementData;

    // 元素数量
    private int size;

    // 默认容量
    private static final int DEFAULT_CAPACITY = 10;

    // 扩容方法
    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);  // 1.5倍
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    // 添加元素
    public boolean add(E e) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    // 指定位置添加
    public void add(int index, E e) {
        rangeCheck(index);
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = e;
        size++;
    }

    // 获取元素
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    // 删除元素
    public E remove(int index) {
        rangeCheck(index);
        modCount++;
        E oldValue = elementData(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        elementData[--size] = null;  // 便于GC
        return oldValue;
    }
}
```

### 1.2.3 ArrayList的时间复杂度

| 操作 | 时间复杂度 | 说明 |
|------|------------|------|
| get(int index) | O(1) | 直接通过索引访问 |
| add(E e) | O(1) amortized | 均摊常数时间 |
| add(int index, E e) | O(n) | 需要移动元素 |
| remove(int index) | O(n) | 需要移动元素 |
| contains(Object o) | O(n) | 需要遍历 |

### 1.2.4 ArrayList使用示例

```java
List<String> list = new ArrayList<>();

// 添加元素
list.add("apple");
list.add("banana");
list.add("orange");
list.add(1, "grape");  // 指定位置插入

// 访问元素
String first = list.get(0);      // "apple"
int size = list.size();           // 4
int index = list.indexOf("banana"); // 1

// 修改元素
list.set(2, "peach");

// 删除元素
list.remove(0);        // 按索引删除
list.remove("banana"); // 按对象删除

// 子列表
List<String> sub = list.subList(1, 3);

// 遍历方式
for (int i = 0; i < list.size(); i++) {
    System.out.println(list.get(i));
}

for (String s : list) {
    System.out.println(s);
}

// JDK 8+ Lambda
list.forEach(s -> System.out.println(s));

// Stream
list.stream().filter(s -> s.length() > 5).forEach(System.out::println);
```

---

## 1.3 LinkedList

### 1.3.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 双向链表（Node<E>） |
| 线程安全 | 否 |
| 适用场景 | 频繁增删操作 |

### 1.3.2 源码分析

```java
public class LinkedList<E> extends AbstractSequentialList<E>
        implements List<E>, Deque<E>, Cloneable, java.io.Serializable {

    // 头尾节点
    transient Node<E> first;
    transient Node<E> last;

    // 节点结构
    private static class Node<E> {
        E item;
        Node<E> next;  // 后继
        Node<E> prev;  // 前驱
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    // 添加到尾部
    public boolean add(E e) {
        linkLast(e);
        return true;
    }

    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
    }

    // 添加到头部
    public void addFirst(E e) {
        linkFirst(e);
    }

    private void linkFirst(E e) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<>(null, e, f);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.prev = newNode;
        size++;
    }

    // 获取元素
    public E get(int index) {
        return node(index).item;
    }

    // 返回index位置的节点
    Node<E> node(int index) {
        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    // 删除节点
    public E remove(int index) {
        return unlink(node(index));
    }

    E unlink(Node<E> x) {
        final E element = x.item;
        final Node<E> next = x.next;
        final Node<E> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.item = null;
        size--;
        return element;
    }
}
```

### 1.3.3 LinkedList的时间复杂度

| 操作 | 时间复杂度 | 说明 |
|------|------------|------|
| get(int index) | O(n) | 需要遍历链表 |
| add(E e) | O(1) | 尾部添加 |
| addFirst(E e) | O(1) | 头部添加 |
| add(int index, E e) | O(n) | 需先找到位置 |
| remove() | O(1) | 头部删除 |
| remove(int index) | O(n) | 需先找到位置 |

### 1.3.4 LinkedList使用示例

```java
LinkedList<String> list = new LinkedList<>();

// 作为List使用
list.add("apple");
list.add("banana");
list.get(0);

// 作为Deque使用
list.addFirst("orange");    // 头部添加
list.addLast("grape");       // 尾部添加
list.removeFirst();         // 头部删除
list.removeLast();          // 尾部删除

// 栈操作（LIFO）
list.push("item");           // 压栈
String item = list.pop();    // 弹栈

// 队列操作（FIFO）
list.offer("item");          // 入队
String s = list.poll();      // 出队

// 查看但不移除
String head = list.peek();   // 查看头部
String tail = list.peekLast(); // 查看尾部
```

### 1.3.5 ArrayList vs LinkedList

| 操作 | ArrayList | LinkedList |
|------|-----------|------------|
| 随机访问 get(i) | O(1) | O(n) |
| 头部插入/删除 | O(n) | O(1) |
| 尾部插入/删除 | O(1) amortized | O(1) |
| 中间插入/删除 | O(n) | O(1) 查找+O(1)修改 |
| 内存占用 | 连续，节省指针 | 每节点需额外指针 |

**选择建议**:
- 随机访问多 → ArrayList
- 频繁增删头尾 → LinkedList
- 一般情况下默认使用 ArrayList

---

## 1.4 Vector

### 1.4.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | Object[] elementData |
| 初始容量 | 10 |
| 扩容机制 | `newCapacity = oldCapacity * 2` |
| 线程安全 | 是（synchronized） |
| 替代方案 | ArrayList（外部同步）或 Collections.synchronizedList |

### 1.4.2 Vector vs ArrayList

```java
// Vector的方法都使用了synchronized
public synchronized E get(int index) {
    if (index >= elementCount)
        throw new ArrayIndexOutOfBoundsException(index);
    return elementData(index);
}

// 推荐：使用ArrayList + Collections.synchronizedList
List<String> list = new ArrayList<>();
List<String> syncList = Collections.synchronizedList(list);
```

### 1.4.3 Stack类

```java
// Stack继承自Vector
public class Stack<E> extends Vector<E> {
    public E push(E item);      // 压栈
    public synchronized E pop(); // 弹栈
    public synchronized E peek(); // 查看栈顶
    public boolean empty();      // 是否为空
}

// JDK 5+ 推荐使用Deque代替Stack
Deque<Integer> stack = new ArrayDeque<>();
stack.push(1);
stack.pop();
```

---

## 1.5 常用操作汇总

### 1.5.1 创建与初始化

```java
// 创建空List
List<String> list = new ArrayList<>();

// 创建带初始容量
List<String> list2 = new ArrayList<>(100);

// 从数组创建
String[] array = {"a", "b", "c"};
List<String> list3 = new ArrayList<>(Arrays.asList(array));

// JDK 8+ 简化
List<String> list4 = Arrays.asList("a", "b", "c");

// JDK 9+ 不可变List
List<String> immutable = List.of("a", "b", "c");

// 初始化块（JDK 9+）
List<Integer> list5 = new ArrayList<>(List.of(1, 2, 3));
```

### 1.5.2 常用方法

```java
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

// 添加
list.add(6);                    // 尾部添加
list.add(0, 0);                 // 指定位置添加
list.addAll(Arrays.asList(7, 8)); // 批量添加

// 删除
list.remove(0);                 // 按索引删除
list.remove(Integer.valueOf(3)); // 按对象删除（注意包装类）
list.removeIf(n -> n % 2 == 0); // 条件删除（JDK 9+）

// 修改
list.set(0, 10);                // 修改指定位置

// 查询
list.get(0);                     // 获取元素
list.contains(3);               // 是否包含
list.indexOf(3);                // 首次出现位置
list.lastIndexOf(3);            // 最后出现位置

// 判断
list.isEmpty();
list.size();
list.clear();

// 批量操作
list.containsAll(Arrays.asList(1, 2)); // 是否包含所有
list.retainAll(Arrays.asList(1, 2, 3)); // 保留交集
list.removeAll(Arrays.asList(4, 5));   // 删除交集

// 排序
Collections.sort(list);                    // 升序
Collections.sort(list, Comparator.reverseOrder()); // 降序
list.sort(Comparator.naturalOrder());     // JDK 8+
list.sort(Comparator.comparingInt(n -> n)); // 自定义

// 替换（JDK 9+）
Collections.replaceAll(list, 1, 10);      // 替换所有
list.replaceAll(n -> n * 2);              // JDK 8+

// 不可变副本
List<Integer> copy = List.copyOf(list);   // JDK 9+
```

### 1.5.3 转换

```java
List<String> list = Arrays.asList("a", "b", "c");

// List转数组
String[] array1 = list.toArray(new String[0]);
String[] array2 = list.toArray(String[]::new);

// List转Set
Set<String> set = new HashSet<>(list);

// 数组转List
List<String> list2 = Arrays.asList(array);
List<String> list3 = new ArrayList<>(Arrays.asList(array));

// JDK 9+ 不可变转换
List<String> immutable = List.of("a", "b", "c");
String[] arr = immutable.toArray(String[]::new);
```

---

## 1.6 线程安全List

### 1.6.1 Vector（不推荐）

```java
List<String> v = new Vector<>();  // 性能差，不推荐
```

### 1.6.2 Collections.synchronizedList

```java
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
// 所有方法都同步，但迭代时需手动同步
synchronized (syncList) {
    for (String s : syncList) {
        // 操作
    }
}
```

### 1.6.3 CopyOnWriteArrayList（推荐读多写少）

```java
List<String> cowList = new CopyOnWriteArrayList<>();
// 读操作无锁，写操作复制整个数组
// 适用于读多写少场景
```

### 1.6.4 并发场景推荐

| 场景 | 推荐 |
|------|------|
| 高并发读多写少 | CopyOnWriteArrayList |
| 需要随机访问 | Collections.synchronizedList + 外部同步 |
| 复合操作需原子性 | java.util.concurrent包的其他类 |

---

## 1.7 底层原理面试要点

### 1.7.1 ArrayList扩容机制

```
初始容量: 10
扩容公式: newCapacity = oldCapacity + (oldCapacity >> 1)
即每次扩容为原来的1.5倍

容量变化示例:
10 -> 15 -> 22 -> 33 -> 49 -> 73 -> 109 ...
```

### 1.7.2 ArrayList和LinkedList如何选择

```
高频面试题：
"ArrayList增删慢" - 错误！头部增删慢，中间增删慢，尾部增删快
"LinkedList增删快" - 正确！但需先找到位置（O(n)）

正确理解：
- ArrayList尾部增删：O(1) amortized
- LinkedList头部/尾部增删：O(1)
- LinkedList中间增删：O(n)查找 + O(1)修改
```

### 1.7.3 fail-fast机制

```java
// ArrayList等使用modCount实现fail-fast
// 检测到并发修改时抛出ConcurrentModificationException

List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
for (String s : list) {
    list.remove(s);  // 抛出ConcurrentModificationException
}

// 安全遍历方式
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    String s = it.next();
    if (s.equals("b")) {
        it.remove();  // 使用迭代器删除
    }
}
```

---

## 1.8 练习题

```java
// 1. 合并两个有序List -> 合并后仍然有序

// 2. 找出List中的重复元素（保持原有顺序）

// 3. 反转List（不能创建新List）

// 4. 将List中的null元素移除

// 5. ArrayList<String> list = new ArrayList<>();
//    list.add("a"); list.add("b"); list.add("c");
//    for (String s : list) { list.remove(s); }
//    运行结果是什么？为什么？
```

---

## 1.9 参考答案

```java
// 1. 合并两个有序List
public static <T extends Comparable<? super T>> List<T> mergeLists(
        List<T> l1, List<T> l2) {
    List<T> result = new ArrayList<>(l1.size() + l2.size());
    int i = 0, j = 0;
    while (i < l1.size() && j < l2.size()) {
        if (l1.get(i).compareTo(l2.get(j)) <= 0) {
            result.add(l1.get(i++));
        } else {
            result.add(l2.get(j++));
        }
    }
    while (i < l1.size()) result.add(l1.get(i++));
    while (j < l2.size()) result.add(l2.get(j++));
    return result;
}

// 2. 找出重复元素（保持顺序）
public static <T> List<T> findDuplicates(List<T> list) {
    List<T> duplicates = new ArrayList<>();
    Set<T> seen = new HashSet<>();
    for (T item : list) {
        if (!seen.add(item) && !duplicates.contains(item)) {
            duplicates.add(item);
        }
    }
    return duplicates;
}

// 3. 反转List（原地反转）
public static <T> void reverse(List<T> list) {
    int left = 0, right = list.size() - 1;
    while (left < right) {
        T temp = list.get(left);
        list.set(left++, list.get(right));
        list.set(right--, temp);
    }
}

// 4. 移除null元素
list.removeIf(Objects::isNull);  // JDK 9+

// 5. 运行结果：抛出ConcurrentModificationException
// 因为for-each使用迭代器遍历，迭代过程中不允许直接修改集合
```

---

[返回目录](./README.md) | [下一步：Set集合](./Step02_Set.md)
