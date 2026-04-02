package com.ssitao.code.datastruct.hashtable;

/**
 * 哈希表实现（分离链接法）
 *
 * 哈希函数：
 * - 使用键的hashCode()计算哈希值
 * - 再通过 (hash & 0x7FFFFFFF) % capacity 得到实际索引
 *
 * 冲突解决 - 分离链接法（Separate Chaining）：
 * - 每个桶（bucket）存储一个链表
 * - 冲突的元素挂在同一个桶的链表上
 * - 当链表过长时，查找会退化为O(n)
 *
 * 扩容机制：
 * - 当元素数量 / 桶数量 > 负载因子时扩容
 * - 扩容将链表重新散列，降低链表长度
 */
public class HashMap<K, V> implements Map<K, V> {

    // 键值对节点
    private class Node {
        K key;
        V value;
        Node next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Object[] table;  // 哈希表数组，使用Object[]避免泛型数组问题
    private int size;        // 键值对数量
    private int capacity;    // 桶的数量

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;  // 负载因子

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    public HashMap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.table = new Object[capacity];
    }

    // 从table中获取Node并强制转换
    @SuppressWarnings("unchecked")
    private Node getNode(int index) {
        return (Node) table[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 获取键的哈希值
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        // 处理负数哈希码，并取模得到正数索引
        return (key.hashCode() & 0x7FFFFFFF) % capacity;
    }

    @Override
    public void put(K key, V value) {
        int index = hash(key);
        Node head = getNode(index);

        // 遍历链表，查找是否已存在该键
        Node cur = head;
        while (cur != null) {
            if ((cur.key == null && key == null) ||
                (cur.key != null && cur.key.equals(key))) {
                // 键已存在，更新值
                cur.value = value;
                return;
            }
            cur = cur.next;
        }

        // 键不存在，头插法插入新节点
        Node newNode = new Node(key, value);
        newNode.next = head;
        table[index] = newNode;
        size++;

        // 检查是否需要扩容
        if (size > capacity * LOAD_FACTOR) {
            resize(capacity * 2);
        }
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        Node cur = getNode(index);

        while (cur != null) {
            if ((cur.key == null && key == null) ||
                (cur.key != null && cur.key.equals(key))) {
                return cur.value;
            }
            cur = cur.next;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int index = hash(key);
        Node head = getNode(index);

        // 头节点处理
        if (head == null) {
            return null;
        }

        if ((head.key == null && key == null) ||
            (head.key != null && head.key.equals(key))) {
            V result = head.value;
            table[index] = head.next;
            size--;
            return result;
        }

        // 遍历链表查找前驱节点
        Node prev = head;
        Node cur = head.next;
        while (cur != null) {
            if ((cur.key == null && key == null) ||
                (cur.key != null && cur.key.equals(key))) {
                V result = cur.value;
                prev.next = cur.next;
                cur.next = null;
                size--;
                return result;
            }
            prev = cur;
            cur = cur.next;
        }

        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        for (int i = 0; i < capacity; i++) {
            Node cur = getNode(i);
            while (cur != null) {
                if ((cur.value == null && value == null) ||
                    (cur.value != null && cur.value.equals(value))) {
                    return true;
                }
                cur = cur.next;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
        }
        size = 0;
    }

    // 扩容并重新散列
    private void resize(int newCapacity) {
        Object[] oldTable = table;
        capacity = newCapacity;
        table = new Object[capacity];
        size = 0;

        // 重新插入所有元素
        for (int i = 0; i < oldTable.length; i++) {
            Node cur = (Node) oldTable[i];
            while (cur != null) {
                put(cur.key, cur.value);
                cur = cur.next;
            }
        }
        System.out.println("哈希表扩容: " + oldTable.length + " -> " + newCapacity);
    }

    // 获取哈希表信息
    public int getCapacity() {
        return capacity;
    }

    public double getLoadFactor() {
        return (double) size / capacity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashMap {");
        boolean first = true;
        for (int i = 0; i < capacity; i++) {
            Node cur = getNode(i);
            while (cur != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(cur.key).append("=").append(cur.value);
                first = false;
                cur = cur.next;
            }
        }
        sb.append("}, size=").append(size).append(", capacity=").append(capacity);
        return sb.toString();
    }
}