package com.ssitao.code.datastruct.queue;


import com.ssitao.code.datastruct.array.Array;

/**
 * 基于动态数组的队列实现
 *
 * 问题：存在"假溢出"问题
 * - 随着dequeue，front指针后移，有效元素前面的空间浪费
 * - 即使数组有空间，front到达尾部后也无法继续入队
 *
 * 解决方案：使用循环队列
 */
public class ArrayQueue<E> implements Queue<E> {

    private Array<E> array;
    private int front;  // 队首索引
    private int rear;   // 队尾索引

    public ArrayQueue() {
        this(10);
    }

    public ArrayQueue(int capacity) {
        array = new Array<>(capacity);
        front = 0;
        rear = 0;
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
    public void enqueue(E e) {
        array.addLast(e);
        rear++;
    }

    @Override
    public E dequeue() {
        E result = array.remove(0);
        front++;
        return result;
    }

    @Override
    public E getFront() {
        return array.get(0);
    }

    @Override
    public void clear() {
        front = 0;
        rear = 0;
        array = new Array<>();
    }

    public int getCapacity() {
        return array.getCapacity();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Queue: [");
        for (int i = 0; i < array.size(); i++) {
            sb.append(array.get(i));
            if (i != array.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("] <- front");
        return sb.toString();
    }
}