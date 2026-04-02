package com.ssitao.code.datastruct.linkedlist;

/**
 * 双链表实现
 *
 * 双链表特点：
 * 1. 每个节点有前驱和后继指针
 * 2. 可以双向遍历
 * 3. 删除操作更高效，不需要查找前驱节点
 */
public class DoubleLinkedList<E> {

    // 双链表节点内部类
    private class Node {
        E e;
        Node prev;   // 前驱指针
        Node next;   // 后继指针

        Node(E e) {
            this.e = e;
            this.prev = null;
            this.next = null;
        }

        Node() {
            this.e = null;
            this.prev = null;
            this.next = null;
        }

        @Override
        public String toString() {
            return e.toString();
        }
    }

    private Node head;  // 头节点（虚拟节点）
    private Node tail;  // 尾节点（虚拟节点）
    private int size;   // 元素个数

    public DoubleLinkedList() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
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
        Node node = new Node(e);
        Node prev = tail.prev;

        node.next = tail;
        node.prev = prev;
        prev.next = node;
        tail.prev = node;

        size++;
    }

    // 在链表开头添加元素
    public void addFirst(E e) {
        Node node = new Node(e);
        Node next = head.next;

        node.prev = head;
        node.next = next;
        head.next = node;
        next.prev = node;

        size++;
    }

    // 在指定位置插入元素
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("索引越界，有效范围: [0, " + size + "]");
        }

        if (index == size) {
            addLast(e);
            return;
        }

        Node node = new Node(e);
        Node cur = getNode(index);

        node.prev = cur.prev;
        node.next = cur;
        cur.prev.next = node;
        cur.prev = node;

        size++;
    }

    // 获取index位置的节点
    private Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界");
        }

        Node cur;
        if (index < size / 2) {
            // 从头往后找
            cur = head.next;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
        } else {
            // 从尾往前找
            cur = tail.prev;
            for (int i = size - 1; i > index; i--) {
                cur = cur.prev;
            }
        }
        return cur;
    }

    // 获取index位置的元素
    public E get(int index) {
        return getNode(index).e;
    }

    // 设置index位置的元素
    public void set(int index, E e) {
        getNode(index).e = e;
    }

    // 查找元素是否存在
    public boolean contains(E e) {
        return find(e) != -1;
    }

    // 查找元素索引，不存在返回-1
    public int find(E e) {
        Node cur = head.next;
        int index = 0;
        while (cur != tail) {
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

        Node nodeToRemove = getNode(index);
        nodeToRemove.prev.next = nodeToRemove.next;
        nodeToRemove.next.prev = nodeToRemove.prev;

        E result = nodeToRemove.e;
        nodeToRemove.prev = null;
        nodeToRemove.next = null;
        size--;

        return result;
    }

    // 删除第一个元素
    public E removeFirst() {
        if (isEmpty()) {
            throw new IllegalArgumentException("链表为空");
        }
        return remove(0);
    }

    // 删除最后一个元素
    public E removeLast() {
        if (isEmpty()) {
            throw new IllegalArgumentException("链表为空");
        }
        return remove(size - 1);
    }

    // 删除指定元素（只删除第一个找到的）
    public void removeElement(E e) {
        Node cur = head.next;
        while (cur != tail) {
            if (cur.e.equals(e)) {
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                cur.prev = null;
                cur.next = null;
                size--;
                return;
            }
            cur = cur.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DoubleLinkedList: [");

        Node cur = head.next;
        while (cur != tail) {
            sb.append(cur.e);
            if (cur.next != tail) {
                sb.append(" <-> ");
            }
            cur = cur.next;
        }

        sb.append("], size=").append(size);
        return sb.toString();
    }
}