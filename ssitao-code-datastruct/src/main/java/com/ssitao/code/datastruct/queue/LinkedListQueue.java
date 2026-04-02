package com.ssitao.code.datastruct.queue;


import com.ssitao.code.datastruct.linkedlist.DoubleLinkedList;

/**
 * 基于双链表的队列实现
 *
 * 队列操作复杂度：
 * - enqueue: O(1) - 链表尾部插入
 * - dequeue:  O(1) - 链表头部删除
 * - getFront: O(1)
 *
 * 使用双链表的原因：
 * - 需要在头部删除（dequeue）和尾部插入（enqueue）
 * - 双链表的头部和尾部操作都是 O(1)
 * - 单链表需要特殊处理尾部，或维护尾指针
 */
public class LinkedListQueue<E> implements Queue<E> {

    private DoubleLinkedList<E> list;

    public LinkedListQueue() {
        list = new DoubleLinkedList<>();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        list.addLast(e);  // 尾部插入
    }

    @Override
    public E dequeue() {
        return list.removeFirst();  // 头部删除
    }

    @Override
    public E getFront() {
        return list.get(0);
    }

    @Override
    public void clear() {
        list = new DoubleLinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LinkedListQueue: [");

        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("] <- front");
        return sb.toString();
    }
}