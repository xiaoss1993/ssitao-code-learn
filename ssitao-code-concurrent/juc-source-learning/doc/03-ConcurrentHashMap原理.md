# ConcurrentHashMap 原理详解 (JDK8)

## 1. 概述

ConcurrentHashMap 是线程安全的 HashMap，JDK8 做了重大重构，取消了分段锁，采用了新的实现方式。

### 版本对比

| 版本 | 实现方式 |
|------|----------|
| JDK7 | Segment数组 + HashEntry + ReentrantLock |
| JDK8 | Node数组 + CAS + synchronized + 红黑树 |

---

## 2. 核心数据结构

### JDK8 数据结构

```java
public class ConcurrentHashMap<K, V> {
    // Node数组，核心数据容器
    transient volatile Node<K, V>[] table;

    // 扩容时的新数组
    private transient volatile Node<K, V>[] nextTable;

    // 基本参数
    private static final int DEFAULT_CAPACITY = 16;
    private static final int TREE_THRESHOLD = 8;    // 链表转红黑树
    private static final int UNTREEIFY_THRESHOLD = 6; // 红黑树转链表
}
```

### Node 节点

```java
static class Node<K, V> implements Map.Entry<K, V> {
    final int hash;
    final K key;
    volatile V value;        // value用volatile
    volatile Node<K, V> next; // 链表指针

    Node(int hash, K key, V value, Node<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
}
```

### TreeNode 红黑树节点

```java
static final class TreeNode<K, V> extends Node<K, V> {
    TreeNode<K, V> parent;
    TreeNode<K, V> left;
    TreeNode<K, V> right;
    boolean red;
    // ...
}
```

---

## 3. 核心操作

### put 流程

```java
public V put(K key, V value) {
    int hash = spread(key.hashCode());
    Node<K, V>[] tab = table;

    while (true) {
        Node<K, V> f = tabAt(tab, i);  // 获取桶首节点

        if (f == null) {
            // 桶为空，CAS插入
            if (casTabAt(tab, i, null, new Node<>(hash, key, value, null))) {
                break;
            }
        } else if ((fh = f.hash) == MOVED) {
            // 正在扩容，帮助扩容
            tab = helpTransfer(tab, f);
        } else {
            // 桶不为空，使用synchronized加锁
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        // 链表插入
                        for (Node<K, V> e = f; ; ) {
                            if (e.hash == hash &&
                                key.equals(e.key)) {
                                e.value = value;
                                break;
                            }
                            Node<K, V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<>(hash, key, value, null);
                                break;
                            }
                        }
                    } else if (f instanceof TreeBin) {
                        // 红黑树插入
                        TreeBin<K, V> t = (TreeBin<K, V>) f;
                        TreeNode<K, V> r = t.putTreeVal(hash, key, value);
                    }
                }
            }
        }
    }
    addCount(1L, binCount);  // 更新计数
    return null;
}
```

### get 流程 (无锁)

```java
public V get(Object key) {
    Node<K, V>[] tab;
    Node<K, V> e, p;
    int n, eh;
    K ek;

    int h = spread(key.hashCode());

    if ((tab = table) != null &&
        (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || key.equals(ek))
                return e.value;
        } else if (eh < 0) {
            // 树节点
            return (e = p.find(h, key)) != null ? e.value : null;
        }
        // 链表遍历
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || key.equals(ek)))
                return e.value;
        }
    }
    return null;
}
```

---

## 4. 并发机制

### CAS 操作

```java
// 获取数组指定位置的元素 (volatile读)
static final <K, V> Node<K, V> tabAt(Node<K, V>[] tab, int i) {
    return (Node<K, V>) UNSAFE.getObjectVolatile(tab,
        ((long) i << ASHIFT) + ABASE);
}

// CAS设置数组指定位置
static final <K, V> boolean casTabAt(Node<K, V>[] tab, int i,
                                     Node<K, V> c, Node<K, V> v) {
    return UNSAFE.compareAndSwapObject(tab,
        ((long) i << ASHIFT) + ABASE, c, v);
}
```

### synchronized 桶锁

JDK8 对每个桶使用 synchronized 加锁，只锁住当前桶，不影响其他桶操作。

```java
synchronized (f) {
    // 处理当前桶的链表或红黑树
}
```

---

## 5. 扩容机制

### 多线程并发扩容

```java
private final void transfer(Node<K, V>[] tab, Node<K, V>[] nextTab) {
    int n = tab.length;
    int stride = (n >>> 3) / NCPU;  // 每线程处理桶数
    if (stride < MIN_TRANSFER_STRIDE)
        stride = MIN_TRANSFER_STRIDE;

    // 每个线程处理一段桶
    for (int i = 0, bound = 0; ; ) {
        while (i >= bound || i >= n)
            if ((b = bound) <= i && i < n &&
                (nextTab == null ? 64 : n) - 1 >= i)
                    bound = i + stride;
            else if (--b >= 0)
                ; // 自旋等待
            else if (nextTab == null)
                nextTab = (Node<K,V>[])new Node<?,?>[n << 1];
            else
                return;  // 扩容完成

        if (i >= 0 && i < n && i + n < nextn) {
            Node<K, V> f = tabAt(tab, i);
            if (f != null) {
                // 迁移单个桶
                synchronized (f) {
                    // 移动节点到nextTab
                }
            }
        }
    }
}
```

---

## 6. 计数机制

### baseCount + CounterCell

```java
// 基础计数
private transient volatile long baseCount;

// 计数单元
static final class CounterCell {
    volatile long value;
    CounterCell(long x) { value = x; }
}

// 添加计数
private final void addCount(long x, int check) {
    CounterCell[] cs;
    long b, s;

    if ((cs = counterCells) != null ||
        !casBaseCount(b = baseCount, s = b + x)) {
        // CAS失败，使用CounterCell
    }
}
```

---

## 7. 关键常量

```java
// 容量相关
static final int DEFAULT_CAPACITY = 16;
static final int MAX_CAPACITY = 1 << 30;

// 树化阈值
static final int TREEIFY_THRESHOLD = 8;      // 链表 > 8 转红黑树
static final int UNTREEIFY_THRESHOLD = 6;     // 红黑树 < 6 转链表
static final int MIN_TREEIFY_CAPACITY = 64;   // 最小树化容量

// 扩容相关
static final int MIN_TRANSFER_STRIDE = 16;     // 最小迁移步长
static final int MOVED     = -1; // hash for forwarding nodes
static final int TREEBIN   = -2; // hash for roots of trees
static final int RESERVED   = -3; // hash for transient reservations
```

---

## 8. JDK7 vs JDK8

| 特性 | JDK7 | JDK8 |
|------|------|------|
| 数据结构 | Segment + HashEntry | Node + TreeNode |
| 锁机制 | 分段锁 (ReentrantLock) | synchronized + CAS |
| 并发度 | 16个Segment | 数组长度 (可扩容) |
| 扩容 | 只能单线程 | 多线程并发 |
| 查询 | 无锁 | 无锁 |
| 树化 | 不支持 | 支持红黑树 |
