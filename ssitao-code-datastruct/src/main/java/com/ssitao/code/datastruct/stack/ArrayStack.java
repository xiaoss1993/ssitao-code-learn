package com.ssitao.code.datastruct.stack;


import com.ssitao.code.datastruct.array.Array;

/**
 * 基于动态数组的栈实现
 *
 * 栈操作复杂度：
 * - push:  O(1) amortized（数组尾部插入）
 * - pop:   O(1)（数组尾部删除）
 * - peek:  O(1)
 */
public class ArrayStack<E> implements Stack<E> {

    private Array<E> array;

    // 默认容量
    public ArrayStack() {
        this(10);
    }

    // 指定容量
    public ArrayStack(int capacity) {
        array = new Array<>(capacity);
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public void push(E e) {
        array.addLast(e);
    }

    @Override
    public E pop() {
        return array.removeLast();
    }

    @Override
    public E peek() {
        return array.get(array.size() - 1);
    }

    @Override
    public void clear() {
        // 重新创建空数组
        array = new Array<>();
    }

    public int getCapacity() {
        return array.getCapacity();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stack: [");
        for (int i = 0; i < array.size(); i++) {
            sb.append(array.get(i));
            if (i != array.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] <- top");
        return sb.toString();
    }
}