package com.ssitao.code.datastruct.stack;


import com.ssitao.code.datastruct.linkedlist.LinkedList;

/**
 * 基于单链表的栈实现
 *
 * 栈操作复杂度：
 * - push:  O(1)（链表头部插入）
 * - pop:   O(1)（链表头部删除）
 * - peek:  O(1)
 */
public class LinkedListStack<E> implements Stack<E> {

    private LinkedList<E> list;

    public LinkedListStack() {
        list = new LinkedList<>();
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
    public void push(E e) {
        list.addFirst(e);  // 头部插入作为栈顶
    }

    @Override
    public E pop() {
        return list.removeFirst();  // 头部删除
    }

    @Override
    public E peek() {
        return list.get(0);  // 查看头部元素
    }

    @Override
    public void clear() {
        list = new LinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack: [");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] <- top");
        return sb.toString();
    }
}