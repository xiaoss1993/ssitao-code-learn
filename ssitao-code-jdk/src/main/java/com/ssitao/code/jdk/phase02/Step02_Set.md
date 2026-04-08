# 步骤2：Set集合 - 无序、去重的集合

---

## 2.1 Set接口概述

### 2.1.1 Set的特点

- **无序**: 不保证元素的顺序（HashSet、LinkedHashSet、TreeSet各有不同）
- **去重**: 不允许存储重复元素
- **只存储一个null**: HashSet允许一个null，TreeSet不允许

### 2.1.2 Set的实现类

```
Set
├── HashSet         - 基于哈希表，无序
├── LinkedHashSet   - 基于哈希表+链表，保持插入顺序
└── TreeSet         - 基于红黑树，有序（自然顺序或自定义）
```

### 2.1.3 Set接口定义

```java
public interface Set<E> extends Collection<E> {
    // 无新增方法，只是语义不同（去重、无序）
    // 继承自Collection的方法都适用
}
```

---

## 2.2 HashSet

### 2.2.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | HashMap（实际存储） |
| 元素顺序 | 不保证顺序，可能变化 |
| 线程安全 | 否 |
| 允许null | 是（仅一个） |
| 性能 | 添加/删除/查找 O(1) |

### 2.2.2 源码分析

```java
public class HashSet<E> extends AbstractSet<E>
        implements Set<E>, Cloneable, java.io.Serializable {

    // 实际存储数据的HashMap
    private transient HashMap<E, Object> map;

    // HashMap的value用一个固定的PRESENT对象
    private static final Object PRESENT = new Object();

    // 构造方法
    public HashSet() {
        map = new HashMap<>();
    }

    public HashSet(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    public HashSet(Collection<? extends E> c) {
        map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
        addAll(c);
    }

    // 添加元素 - 实际是存入HashMap，key是元素，value是PRESENT
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    // HashMap.put(key, value) 返回null表示新增成功
    // 返回非null表示key已存在（更新操作），HashSet视为成功添加

    // 删除元素
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    // 查找（contains）
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    // 迭代
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
}
```

### 2.2.3 HashSet的时间复杂度

| 操作 | 时间复杂度 | 说明 |
|------|------------|------|
| add(E e) | O(1) average | 哈希查找 |
| remove(Object o) | O(1) average | 哈希查找 |
| contains(Object o) | O(1) average | 哈希查找 |
| iterator() | O(1) | 获取迭代器 |

### 2.2.4 HashSet使用示例

```java
Set<String> set = new HashSet<>();

// 添加元素（重复元素会被忽略）
set.add("apple");
set.add("banana");
set.add("apple");  // 被忽略，不报错

// 常用操作
set.size();                    // 元素数量（2）
set.contains("apple");         // true
set.remove("banana");
set.isEmpty();

// 批量操作
set.addAll(Arrays.asList("c", "d"));
set.containsAll(Arrays.asList("a", "b"));
set.removeAll(Arrays.asList("c", "d"));
set.retainAll(Arrays.asList("a", "b"));  // 保留交集

// 集合运算
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
Set<Integer> set2 = new HashSet<>(Arrays.asList(4, 5, 6, 7, 8));

// 交集
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2);  // [4, 5]

// 并集
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2);             // [1, 2, 3, 4, 5, 6, 7, 8]

// 差集 (set1 - set2)
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2);     // [1, 2, 3]
```

---

## 2.3 LinkedHashSet

### 2.3.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | LinkedHashMap |
| 元素顺序 | 保持插入顺序 |
| 线程安全 | 否 |
| 允许null | 是（仅一个） |
| 性能 | 添加/删除/查找 O(1)，迭代 O(n) |

### 2.3.2 与HashSet的区别

```java
// HashSet 不保证顺序
Set<String> hashSet = new HashSet<>();
hashSet.add("c");
hashSet.add("a");
hashSet.add("b");
// 迭代顺序可能是: c, a, b 或其他顺序，每次运行可能不同

// LinkedHashSet 保持插入顺序
Set<String> linkedHashSet = new LinkedHashSet<>();
linkedHashSet.add("c");
linkedHashSet.add("a");
linkedHashSet.add("b");
// 迭代顺序: c, a, b（固定顺序）
```

### 2.3.3 源码分析

```java
public class LinkedHashSet<E> extends HashSet<E>
        implements Set<E>, Cloneable, java.io.Serializable {

    public LinkedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);
        // 调用HashSet的三个参数的构造，内部创建LinkedHashMap
    }

    public LinkedHashSet(int initialCapacity) {
        super(initialCapacity, .75f, true);
    }

    public LinkedHashSet() {
        super(16, .75f, true);
    }

    public LinkedHashSet(Collection<? extends E> c) {
        super(Math.max(c.size()*2, 11), .75f, true);
        addAll(c);
    }
}

// 关键点：LinkedHashMap在HashMap的基础上增加了双向链表
// 用于维护插入顺序（accessOrder=false时）或访问顺序（accessOrder=true时）
```

### 2.3.4 LinkedHashSet使用场景

```java
// 场景1：去除重复同时保持插入顺序
List<String> listWithDups = Arrays.asList("a", "b", "c", "a", "d", "b");
Set<String> uniqueOrdered = new LinkedHashSet<>(listWithDups);
// 结果: [a, b, c, d]

// 场景2：实现LRU缓存（利用访问顺序）
Set<String> lru = new LinkedHashSet<>(16, .75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > 3;  // 超过3个元素时移除最老的
    }
};
```

---

## 2.4 TreeSet

### 2.4.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | TreeMap（红黑树） |
| 元素顺序 | 有序（自然顺序或Comparator） |
| 线程安全 | 否 |
| 允许null | 否（除第一个添加的null外） |
| 性能 | 添加/删除/查找 O(log n) |

### 2.4.2 源码分析

```java
public class TreeSet<E> extends AbstractSet<E>
        implements NavigableSet<E>, Cloneable, java.io.Serializable {

    // 实际存储数据的TreeMap
    private transient NavigableMap<E, Object> map;

    // 使用PRESENT作为value
    private static final Object PRESENT = new Object();

    // 无参构造 - 使用自然顺序（元素必须实现Comparable）
    public TreeSet() {
        this(new TreeMap<>());
    }

    // 指定比较器
    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
    }

    // 从Collection创建 - 使用自然顺序
    public TreeSet(Collection<? extends E> c) {
        this();
        addAll(c);
    }

    // 添加元素
    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    // 查找
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    // 删除
    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }
}
```

### 2.4.3 有序操作示例

```java
TreeSet<Integer> set = new TreeSet<>();

// 添加元素（自动排序）
set.add(5);
set.add(2);
set.add(8);
set.add(1);
set.add(9);
// 迭代顺序: [1, 2, 5, 8, 9]

// 范围操作
set.lower(5);      // < 5 的最大元素: 2
set.floor(5);       // <= 5 的最大元素: 5
set.higher(5);     // > 5 的最小元素: 8
set.ceiling(5);    // >= 5 的最小元素: 5

// 子集
set.subSet(2, 8);      // [2, 8) -> [2, 5]
set.subSet(2, true, 8, true); // [2, 8] -> [2, 5, 8]
set.headSet(5);        // < 5 -> [1, 2]
set.headSet(5, true);  // <= 5 -> [1, 2, 5]
set.tailSet(5);        // >= 5 -> [5, 8, 9]
set.tailSet(5, false); // > 5 -> [8, 9]

// 获取第一个/最后一个
set.first();  // 1
set.last();   // 9

// 删除
set.pollFirst();  // 返回并删除第一个: 1
set.pollLast();   // 返回并删除最后一个: 9
```

### 2.4.4 自定义排序的TreeSet

```java
// 使用Comparator指定排序规则
TreeSet<String> set = new TreeSet<>(Comparator.comparingInt(String::length));

set.add("apple");
set.add("banana");
set.add("pie");
// 迭代顺序: [pie, apple, banana]（按长度排序）

// 降序排列
TreeSet<Integer> descSet = new TreeSet<>(Comparator.reverseOrder());
descSet.addAll(Arrays.asList(5, 2, 8, 1, 9));
// 迭代顺序: [9, 8, 5, 2, 1]

// 自定义类使用TreeSet
class Person {
    String name;
    int age;
}

TreeSet<Person> personSet = new TreeSet<>(Comparator.comparingInt(p -> p.age));
```

---

## 2.5 三种Set对比

### 2.5.1 核心对比

| 特性 | HashSet | LinkedHashSet | TreeSet |
|------|---------|---------------|---------|
| 底层结构 | HashMap | LinkedHashMap | TreeMap |
| 顺序 | 无序 | 插入顺序 | 排序顺序 |
| null支持 | 是 | 是 | 否 |
| 添加性能 | O(1) | O(1) | O(log n) |
| 查找性能 | O(1) | O(1) | O(log n) |
| 删除性能 | O(1) | O(1) | O(log n) |
| 迭代性能 | O(n) | O(n) | O(log n) + n |

### 2.5.2 选择建议

```java
// 需要去重 + 无顺序要求 -> HashSet（默认选择）
Set<String> hashSet = new HashSet<>();

// 需要去重 + 保持插入顺序 -> LinkedHashSet
Set<String> linkedHashSet = new LinkedHashSet<>();

// 需要去重 + 排序 -> TreeSet
Set<Integer> treeSet = new TreeSet<>();
```

---

## 2.6 底层原理

### 2.6.1 HashSet的哈希查找

```
HashSet.add("apple") 执行流程:

1. 计算"apple"的hashCode()
2. 根据hashCode计算桶位置（index = hash & (capacity - 1)）
3. 如果桶为空，直接插入
4. 如果桶不为空，遍历桶中的链表/红黑树
5. 使用equals()比较是否相同
6. 相同则忽略，不同则插入
```

### 2.6.2 HashSet的负载因子

```java
// HashSet默认负载因子0.75
// 当填充率达到75%时自动扩容

// 构造函数可以指定初始容量
Set<String> set = new HashSet<>(100);  // 初始容量100

// 预估容量设置公式: initialCapacity = (int)(expectedSize / 0.75) + 1
Set<String> set2 = new HashSet<>(133);  // 预计存100个元素
```

### 2.6.3 equals和hashCode的重要性

```java
// 自定义类放入HashSet必须重写equals和hashCode
class Person {
    String name;
    int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
```

---

## 2.7 线程安全Set

### 2.7.1 Collections.synchronizedSet

```java
Set<String> syncSet = Collections.synchronizedSet(new HashSet<>());
// 所有方法同步，迭代需手动同步
```

### 2.7.2 CopyOnWriteArraySet

```java
Set<String> cowSet = new CopyOnWriteArraySet<>();
// 读无锁，写复制
// 适用于读多写少场景
```

### 2.7.3 ConcurrentHashMap.newKeySet()

```java
// JDK 8+ 高并发Set
Set<String> concurrentSet = ConcurrentHashMap.newKeySet();
// 底层是ConcurrentHashMap的keySet
```

---

## 2.8 练习题

```java
// 1. 去除List中的重复元素（使用Set）

// 2. 找出两个数组的共同元素

// 3. 判断一个字符串中所有字符是否都不重复

// 4. 实现统计字符串中每个字符出现次数（使用Map）

// 5. TreeSet<Integer> set = new TreeSet<>();
//    set.add(1); set.add(2); set.add(3);
//    System.out.println(set.lower(2));  // 输出什么？
```

---

## 2.9 参考答案

```java
// 1. 去除重复（保持顺序）
public static <T> List<T> removeDuplicates(List<T> list) {
    return new ArrayList<>(new LinkedHashSet<>(list));
}

// 2. 共同元素
public static <T> Set<T> commonElements(T[] a, T[] b) {
    Set<T> setA = new HashSet<>(Arrays.asList(a));
    Set<T> setB = new HashSet<>(Arrays.asList(b));
    setA.retainAll(setB);
    return setA;
}

// 3. 判断字符是否重复
public static boolean allUnique(String str) {
    Set<Character> set = new HashSet<>();
    for (char c : str.toCharArray()) {
        if (!set.add(c)) {
            return false;  // 添加失败说明已存在
        }
    }
    return true;
}

// 4. 字符出现次数
public static Map<Character, Integer> charCount(String str) {
    Map<Character, Integer> map = new HashMap<>();
    for (char c : str.toCharArray()) {
        map.merge(c, 1, Integer::sum);  // JDK 9+
    }
    return map;
}

// 5. 输出: 1
// lower(2)返回小于2的最大元素，即1
```

---

[返回目录](./README.md) | [下一步：Map集合](./Step03_Map.md)
