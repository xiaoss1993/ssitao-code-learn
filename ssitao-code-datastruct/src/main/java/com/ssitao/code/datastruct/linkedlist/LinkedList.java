package com.ssitao.code.datastruct.linkedlist;

/**
 * 单链表实现
 *
 * 链表特点：
 * 1. 内存不连续，通过指针连接
 * 2. 插入、删除效率高 O(1)，但需要先找到位置 O(n)
 * 3. 访问效率低，需要从头遍历 O(n)
 */
public class LinkedList<E> {

    // 节点内部类
    private class Node {
        E e;
        Node next;

        Node(E e) {
            this.e = e;
            this.next = null;
        }

        Node() {
            this.e = null;
            this.next = null;
        }

        @Override
        public String toString() {
            return e.toString();
        }
    }

    private Node head;  // 头节点（虚拟节点，不存储数据）
    private int size;  // 元素个数

    public LinkedList() {
        head = new Node();  // 虚拟头节点
        size = 0;
    }

    // 获取元素个数
    public int size() {
        return size;
    }

    // 判断是否为空
    public boolean isEmpty() {
        return size == 0;
    }

    // 在链表末尾添加元素
    public void addLast(E e) {
        add(size, e);
    }

    // 在链表开头添加元素
    public void addFirst(E e) {
        add(0, e);
    }

    // 在指定位置插入元素
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("索引越界，有效范围: [0, " + size + "]");
        }

        // 找到index位置的前一个节点
        Node prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        Node node = new Node(e);
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    // 获取index位置的元素
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界");
        }

        Node cur = head.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur.e;
    }

    // 设置index位置的元素
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界");
        }

        Node cur = head.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        cur.e = e;
    }

    // 查找元素是否存在
    public boolean contains(E e) {
        Node cur = head.next;
        while (cur != null) {
            if (cur.e.equals(e)) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    // 查找元素索引，不存在返回-1
    public int find(E e) {
        Node cur = head.next;
        int index = 0;
        while (cur != null) {
            if (cur.e.equals(e)) {
                return index;
            }
            cur = cur.next;
            index++;
        }
        return -1;
    }

    // 删除index位置的元素，返回被删除的元素
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界");
        }

        // 找到index位置的前一个节点
        Node prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        Node nodeToRemove = prev.next;
        prev.next = nodeToRemove.next;
        nodeToRemove.next = null;
        size--;

        return nodeToRemove.e;
    }

    // 删除第一个元素
    public E removeFirst() {
        return remove(0);
    }

    // 删除最后一个元素
    public E removeLast() {
        return remove(size - 1);
    }

    // 删除指定元素（只删除第一个找到的）
    public void removeElement(E e) {
        Node prev = head;
        while (prev.next != null) {
            if (prev.next.e.equals(e)) {
                Node nodeToRemove = prev.next;
                prev.next = nodeToRemove.next;
                nodeToRemove.next = null;
                size--;
                return;
            }
            prev = prev.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LinkedList: [");

        Node cur = head.next;
        while (cur != null) {
            sb.append(cur.e);
            if (cur.next != null) {
                sb.append(" -> ");
            }
            cur = cur.next;
        }

        sb.append("], size=").append(size);
        return sb.toString();
    }
}