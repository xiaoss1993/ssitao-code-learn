# 步骤3：Map集合 - 键值对映射

---

## 3.1 Map接口概述

### 3.1.1 Map的特点

- **键值对**: 存储 key-value 映射
- **键唯一**: 同一个Map中key不能重复
- **值可重复**: value可以重复
- **高效查找**: 根据key快速查找value

### 3.1.2 Map的实现类

```
Map
├── HashMap        - 基于哈希表，无序
├── LinkedHashMap  - 保持插入顺序
├── TreeMap        - 基于红黑树，有序
├── Hashtable      - 线程安全（古老）
└── ConcurrentHashMap - 高并发哈希表
```

### 3.1.3 Map接口定义

```java
public interface Map<K, V> {
    // 基本操作
    V get(Object key);
    V put(K key, V value);
    V remove(Object key);
    boolean containsKey(Object key);
    boolean containsValue(Object value);

    // 批量操作
    void putAll(Map<? extends K, ? extends V> m);
    void clear();

    // 视图
    Set<K> keySet();
    Collection<V> values();
    Set<Map.Entry<K, V>> entrySet();

    // 其他
    int size();
    boolean isEmpty();

    // JDK 8+ 默认方法
    default V getOrDefault(Object key, V defaultValue);
    default V putIfAbsent(K key, V value);
    default boolean remove(Object key, Object value);
    default V replace(K key, V value);
    default boolean replace(K key, V oldValue, V newValue);
}
```

---

## 3.2 HashMap

### 3.2.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 数组 + 链表 + 红黑树（JDK 8+） |
| 初始容量 | 16 |
| 负载因子 | 0.75 |
| 扩容机制 | 容量翻倍 |
| 线程安全 | 否 |
| 允许null键 | 是（仅一个） |
| 允许null值 | 是（多个） |
| 性能 | 查找/插入 O(1) average |

### 3.2.2 源码分析（JDK 8）

```java
public class HashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    // 节点数组
    transient Node<K, V>[] table;

    // 节点数量
    transient int size;

    // 容量（数组长度）
    int threshold;

    // 负载因子
    final float loadFactor;

    // 链表转红黑树的阈值
    static final int TREEIFY_THRESHOLD = 8;

    // 树转链表的阈值
    static final int UNTREEIFY_THRESHOLD = 6;

    // 最小树化容量
    static final int MIN_TREEIFY_CAPACITY = 64;

    // 节点结构
    static class Node<K, V> {
        final int hash;        // key的hash
        final K key;           // 不可变
        V value;
        Node<K, V> next;       // 链表next
    }

    // 构造方法
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;  // 0.75
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, float loadFactor) {
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    // 计算数组大小（必须是2的幂）
    static final int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    // 哈希计算
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    // 获取节点
    final Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab; Node<K, V> first, e; int n; K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
            (first = tab[(n - 1) & hash]) != null) {
            // 检查第一个节点
            if (first.hash == hash &&
                ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            // 检查链表或红黑树
            if ((e = first.next) != null) {
                if (first instanceof TreeNode)
                    return ((TreeNode<K, V>)first).getTreeNode(hash, key);
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    // 插入节点
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K, V>[] tab; Node<K, V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        // 计算桶位置并插入
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            Node<K, V> e; K k;
            if (p.hash == hash &&
                ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof TreeNode)
                e = ((TreeNode<K, V>)p).putTreeVal(this, tab, hash, key, value);
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        // 链表过长转红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1)
                            treeifyBin(tab, hash);
                        break;
                    }
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            // key已存在，更新value
            if (e != null) {
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        return null;
    }

    // 扩容
    final Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            newCap = oldCap << 1;
            newThr = oldThr << 1;
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;  // 16
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY); // 12
        }
        threshold = newThr;
        Node<K, V>[] newTab = new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K, V>)e).split(this, newTab, j, oldCap);
                    else {
                        // 链表拆分
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null) loHead = e;
                                else loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null) hiHead = e;
                                else hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }
}
```

### 3.2.3 HashMap的时间复杂度

| 操作 | 时间复杂度 | 说明 |
|------|------------|------|
| get(Object key) | O(1) average | 哈希查找 |
| put(K key, V value) | O(1) average | 哈希查找+插入 |
| remove(Object key) | O(1) average | 哈希查找+删除 |
| containsKey(Object key) | O(1) average | 哈希查找 |
| containsValue(Object value) | O(n) | 需遍历 |

### 3.2.4 HashMap使用示例

```java
Map<String, Integer> map = new HashMap<>();

// 添加
map.put("apple", 1);
map.put("banana", 2);
map.put("orange", 3);
map.put("apple", 10);  // 更新apple的值

// 获取
map.get("apple");           // 10
map.getOrDefault("grape", 0); // 0（key不存在返回默认值）

// 删除
map.remove("banana");
map.remove("apple", 1);     // 仅当value为1时才删除

// 判断
map.containsKey("apple");   // true/false
map.containsValue(10);     // true/false
map.isEmpty();
map.size();

// 遍历方式1：遍历keySet
for (String key : map.keySet()) {
    System.out.println(key + " -> " + map.get(key));
}

// 遍历方式2：遍历entrySet（推荐，性能更好）
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}

// 遍历方式3：遍历values
for (Integer value : map.values()) {
    System.out.println(value);
}

// JDK 8+ forEach
map.forEach((k, v) -> System.out.println(k + " -> " + v));

// JDK 8+ compute方法
map.compute("apple", (k, v) -> v == null ? 1 : v + 10);  // apple的value +10
map.computeIfAbsent("new", k -> k.length());  // key不存在才计算
map.computeIfPresent("apple", (k, v) -> v + 1); // key存在才计算
```

---

## 3.3 LinkedHashMap

### 3.3.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | HashMap + 双向链表 |
| 元素顺序 | 保持插入顺序（或访问顺序） |
| 线程安全 | 否 |
| 允许null键 | 是（仅一个） |
| 允许null值 | 是（多个） |
| 性能 | 查找/插入 O(1)，迭代 O(n) |

### 3.3.2 源码分析

```java
public class LinkedHashMap<K, V> extends HashMap<K, V> {

    // 双向链表的头尾
    transient LinkedHashMap.Entry<K, V> head;
    transient LinkedHashMap.Entry<K, V> tail;

    // 是否按访问顺序（true=LRU，false=插入顺序）
    final boolean accessOrder;

    // 节点结构（继承自HashMap.Node）
    static class Entry<K, V> extends HashMap.Node<K, V> {
        Entry<K, V> before, after;
    }

    // 构造方法
    public LinkedHashMap() {
        super();
        this.accessOrder = false;  // 默认插入顺序
    }

    // 按访问顺序的构造方法
    public LinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor);
        this.accessOrder = accessOrder;  // true=LRU
    }

    // 重写afterNodeInsertion（HashMap内部调用的钩子方法）
    void afterNodeInsertion(boolean evict) {
        LinkedHashMap.Entry<K, V> first;
        if (evict && (first = head) != null && eldest != null) {
            int sz = size();
            if (removeEldestEntry(first) && sz > 0) {
                removeNode(first.hash, first.key, null, false, true);
            }
        }
    }

    // 重写afterNodeAccess（访问后移动到尾部）
    void afterNodeAccess(Node<K, V> e) {
        LinkedHashMap.Entry<K, V> last;
        if (accessOrder && (last = tail) != e) {
            LinkedHashMap.Entry<K, V> p =
                (LinkedHashMap.Entry<K, V>)e, b = p.before, a = p.after;
            p.after = null;
            if (b == null) head = a;
            else b.after = a;
            if (a != null) a.before = b;
            else last = b;
            if (last == null) head = p;
            else { p.before = last; last.after = p; }
            tail = p;
            ++modCount;
        }
    }
}
```

### 3.3.3 LinkedHashMap使用场景

```java
// 场景1：保持插入顺序
Map<String, Integer> map = new LinkedHashMap<>();
map.put("c", 3);
map.put("a", 1);
map.put("b", 2);
// 迭代顺序: c -> 3, a -> 1, b -> 2

// 场景2：实现LRU缓存
Map<Integer, String> lru = new LinkedHashMap<>(16, .75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
        return size() > 3;  // 超过3个时移除最老的
    }
};
lru.put(1, "one");
lru.put(2, "two");
lru.put(3, "three");
lru.get(1);  // 访问1，会将1移到尾部
lru.put(4, "four");  // 移除最老的(原来是2)
// 当前顺序: 3, 1, 4
```

---

## 3.4 TreeMap

### 3.4.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 红黑树 |
| 元素顺序 | 有序（自然顺序或Comparator） |
| 线程安全 | 否 |
| 允许null键 | 否（除第一个null外） |
| 允许null值 | 是 |
| 性能 | 查找/插入/删除 O(log n) |

### 3.4.2 源码分析

```java
public class TreeMap<K, V> extends AbstractMap<K, V>
        implements NavigableMap<K, V>, Cloneable, java.io.Serializable {

    // 红黑树根节点
    private transient Entry<K, V> root;

    // 元素数量
    private transient int size = 0;

    // 比较器（null表示使用自然顺序）
    private final Comparator<? super K> comparator;

    // 节点结构
    static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;
        boolean color = BLACK;  // 红黑树颜色
    }

    // 构造方法
    public TreeMap() {
        comparator = null;  // 使用自然顺序
    }

    public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    // 插入
    public V put(K key, V value) {
        Entry<K, V> t = root;
        if (t == null) {
            root = new Entry<>(key, value, null);
            size = 1;
            return null;
        }
        int cmp;
        Entry<K, V> parent;
        Comparator<? super K> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0) t = t.left;
                else if (cmp > 0) t = t.right;
                else return t.setValue(value);
            } while (t != null);
        } else {
            Objects.requireNonNull(key);
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            do {
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0) t = t.left;
                else if (cmp > 0) t = t.right;
                else return t.setValue(value);
            } while (t != null);
        }
        Entry<K, V> e = new Entry<>(key, value, parent);
        if (cmp < 0) parent.left = e;
        else parent.right = e;
        fixAfterInsertion(e);
        size++;
        return null;
    }

    // 查找
    public V get(Object key) {
        Entry<K, V> p = getEntry(key);
        return p != null ? p.value : null;
    }

    final Entry<K, V> getEntry(Object key) {
        // 与put类似，二分查找
    }
}
```

### 3.4.3 TreeMap有序操作

```java
TreeMap<Integer, String> map = new TreeMap<>();

map.put(5, "five");
map.put(2, "two");
map.put(8, "eight");
map.put(1, "one");
map.put(9, "nine");
// 按key排序: {1=one, 2=two, 5=five, 8=eight, 9=nine}

// 导航操作
map.lowerKey(5);      // < 5 的最大key: 2
map.floorKey(5);      // <= 5 的最大key: 5
map.higherKey(5);     // > 5 的最小key: 8
map.ceilingKey(5);    // >= 5 的最小key: 5

// 子Map
map.subMap(2, 5);      // [2, 5) -> {2=two, ...}
map.headMap(5);        // < 5
map.tailMap(5);        // >= 5

// 第一个/最后一个
map.firstKey();  // 1
map.lastKey();   // 9

// 删除
map.pollFirstEntry();  // 返回并删除第一个: 1=one
map.pollLastEntry();   // 返回并删除最后一个: 9=nine
```

---

## 3.5 Hashtable

### 3.5.1 核心特性

| 特性 | 说明 |
|------|------|
| 底层结构 | 数组 + 链表 |
| 初始容量 | 11 |
| 扩容机制 | `capacity * 2 + 1` |
| 线程安全 | 是（所有方法synchronized） |
| 允许null | 不允许null键和null值 |

### 3.5.2 HashMap vs Hashtable

```java
// Hashtable - 线程安全，所有方法同步
Hashtable<String, Integer> ht = new Hashtable<>();
ht.put("a", 1);
ht.get("a");  // 线程安全，但性能差

// HashMap - 线程不安全
HashMap<String, Integer> hm = new HashMap<>();
hm.put("a", 1);
hm.get("a");  // 性能好，但需外部同步

// ConcurrentHashMap - 高并发推荐
ConcurrentHashMap<String, Integer> chm = new ConcurrentHashMap<>();
chm.put("a", 1);
chm.get("a");  // 线程安全且高效
```

---

## 3.6 ConcurrentHashMap

### 3.6.1 JDK 7 vs JDK 8

```java
// JDK 7: Segment数组 + HashEntry数组 + 链表
// 每个Segment类似一个小的HashMap，锁分段

// JDK 8: 数组 + 链表 + 红黑树 + CAS + synchronized
// 锁细化到单个桶，不再使用Segment
```

### 3.6.2 常用方法

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// 基本操作
map.put("a", 1);
map.get("a");  // 读无锁

// 原子操作
map.putIfAbsent("a", 10);  // key不存在才插入
map.remove("a", 1);        // key=value才删除
map.replace("a", 1, 10);   // key=oldValue才替换

// JDK 8+ 聚合操作
map.compute("a", (k, v) -> v == null ? 1 : v + 10);
map.merge("a", 1, Integer::sum);

// 批量操作
map.putAll(Map.of("b", 2, "c", 3));

// 统计
map.keys();        // 枚举key
map.elements();    // 枚举value
map.entrySet();    // 枚举entry

// 计算
map.getOrDefault("a", 0);
map.containsValue(1);

// 原子更新
map.putIfAbsent("key", new AtomicInteger(0));
map.computeIfPresent("key", (k, v) -> v.incrementAndGet());
```

---

## 3.7 Map对比总结

### 3.7.1 核心对比

| 特性 | HashMap | LinkedHashMap | TreeMap | Hashtable | ConcurrentHashMap |
|------|---------|---------------|---------|-----------|-------------------|
| 底层结构 | 数组+链表+红黑树 | HashMap+链表 | 红黑树 | 数组+链表 | CAS+ synchronized |
| 顺序 | 无序 | 插入顺序/LRU | 排序顺序 | 无序 | 无序 |
| null键 | 允许1个 | 允许1个 | 不允许 | 不允许 | 不允许 |
| null值 | 允许多个 | 允许多个 | 允许 | 不允许 | 允许 |
| 线程安全 | 否 | 否 | 否 | 是 | 是 |
| get/put性能 | O(1) avg | O(1) avg | O(log n) | O(1) | O(1) avg |
| 适用场景 | 通用场景 | 保持顺序/LRU | 需要排序 | 需要线程安全(旧) | 高并发 |

### 3.7.2 选择建议

```java
// 通用场景，无顺序要求 -> HashMap
Map<String, Integer> map = new HashMap<>();

// 需要保持插入顺序 -> LinkedHashMap
Map<String, Integer> ordered = new LinkedHashMap<>();

// 需要按键排序 -> TreeMap
Map<Integer, String> sorted = new TreeMap<>();

// 高并发场景 -> ConcurrentHashMap
ConcurrentHashMap<String, Integer> concurrent = new ConcurrentHashMap<>();
```

---

## 3.8 练习题

```java
// 1. 统计字符串中每个字符出现的次数

// 2. 实现一个简单的WordCount（统计单词出现次数）

// 3. 按value排序HashMap

// 4. HashMap<String, Integer> map = new HashMap<>();
//    map.put(null, 1);
//    map.put("a", null);
//    System.out.println(map.size());
//    打印结果是什么？

// 5. 实现一个LRU缓存（使用LinkedHashMap）
```

---

## 3.9 参考答案

```java
// 1. 统计字符出现次数
public static Map<Character, Integer> countChars(String str) {
    Map<Character, Integer> map = new HashMap<>();
    for (char c : str.toCharArray()) {
        map.merge(c, 1, Integer::sum);  // 或 map.put(c, map.getOrDefault(c, 0) + 1);
    }
    return map;
}

// 2. 单词统计
public static Map<String, Integer> wordCount(String text) {
    Map<String, Integer> map = new HashMap<>();
    for (String word : text.split("\\s+")) {
        map.merge(word, 1, Integer::sum);
    }
    return map;
}

// 3. 按value排序
public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
        Map<K, V> map) {
    return map.entrySet().stream()
        .sorted(Map.Entry.<K, V>comparingByValue())
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            Map.Entry::getValue,
            (e1, e2) -> e1,
            LinkedHashMap::new
        ));
}

// 4. 打印结果: 2
// HashMap允许一个null键和多个null值

// 5. LRU缓存实现
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);  // accessOrder=true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}
```

---

[返回目录](./README.md) | [下一步：Queue集合](./Step04_Queue.md)
