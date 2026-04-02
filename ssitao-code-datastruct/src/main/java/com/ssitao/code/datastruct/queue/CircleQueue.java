package com.ssitao.code.datastruct.queue;

/**
 * 循环队列实现
 *
 * 循环队列原理：
 * - 用数组模拟环，front和rear指针在环上移动
 * - 当指针到达数组末尾时，下一个位置回到数组开头
 * - 通过取模运算实现：index = index % capacity
 *
 * 判断队列状态：
 * - 空队列：front == rear
 * - 满队列：(rear + 1) % capacity == front
 * - 牺牲一个空间来区分空和满的状态
 *
 * 优点：充分利用数组空间，没有"假溢出"问题
 */
public class CircleQueue<E> implements Queue<E> {

    private E[] data;
    private int front;  // 队首索引
    private int rear;   // 队尾索引（指向下一个可插入位置）
    private int size;   // 当前元素个数

    private static final int DEFAULT_CAPACITY = 10;

    public CircleQueue() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public CircleQueue(int capacity) {
        // 循环队列需要额外一个空间来区分空和满
        data = (E[]) new Object[capacity + 1];
        front = 0;
        rear = 0;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(E e) {
        if ((rear + 1) % data.length == front) {
            // 队列满，需要扩容
            resize(data.length - 1);
        }

        data[rear] = e;
        rear = (rear + 1) % data.length;
        size++;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalArgumentException("队列为空");
        }

        E result = data[front];
        data[front] = null;  // 释放引用
        front = (front + 1) % data.length;
        size--;

        return result;
    }

    @Override
    public E getFront() {
        if (isEmpty()) {
            throw new IllegalArgumentException("队列为空");
        }
        return data[front];
    }

    @Override
    public void clear() {
        front = 0;
        rear = 0;
        size = 0;
        data = (E[]) new Object[data.length];
    }

    public int getCapacity() {
        return data.length - 1;  // 牺牲一个空间
    }

    // 动态扩容
    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity + 1];
        for (int i = 0; i < size; i++) {
            newData[i] = data[(front + i) % data.length];
        }
        data = newData;
        front = 0;
        rear = size;
        System.out.println("循环队列容量变化: " + (data.length - 1) + " -> " + newCapacity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CircleQueue: [");

        for (int i = 0; i < size; i++) {
            int index = (front + i) % data.length;
            sb.append(data[index]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }

        sb.append("] <- front, capacity=").append(getCapacity());
        return sb.toString();
    }
}