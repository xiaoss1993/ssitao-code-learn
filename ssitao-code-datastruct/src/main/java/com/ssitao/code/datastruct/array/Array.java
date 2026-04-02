package com.ssitao.code.datastruct.array;

/**
 * 动态数组实现
 *
 * 数组特点：
 * 1. 连续的内存空间，访问效率高 O(1)
 * 2. 插入、删除效率低，需要移动元素 O(n)
 * 3. 固定大小（这里实现动态扩容）
 */
public class Array<E> {

    private E[] data;
    private int size;  // 当前元素个数

    // 默认容量
    private static final int DEFAULT_CAPACITY = 10;

    // 无参构造，默认容量10
    public Array() {
        this(DEFAULT_CAPACITY);
    }

    // 指定容量构造
    public Array(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("容量不能小于等于0");
        }
        data = (E[]) new Object[capacity];
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

    // 获取容量
    public int getCapacity() {
        return data.length;
    }

    // 添加元素到末尾
    public void addLast(E e) {
        add(size, e);
    }

    // 添加元素到开头
    public void addFirst(E e) {
        add(0, e);
    }

    // 在指定位置插入元素
    public void add(int index, E e) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("索引越界，有效范围: [0, " + size + "]");
        }

        // 扩容检查
        if (size == data.length) {
            resize(data.length * 2);
        }

        // 从后往前移动元素，空出index位置
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = e;
        size++;
    }

    // 获取index位置的元素
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界，有效范围: [0, " + (size - 1) + "]");
        }
        return data[index];
    }

    // 设置index位置的元素
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界");
        }
        data[index] = e;
    }

    // 查找元素是否存在
    public boolean contains(E e) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(e)) {
                return true;
            }
        }
        return false;
    }

    // 查找元素索引，不存在返回-1
    public int find(E e) {
        for (int i = 0; i < size; i++) {
            if (data[i].equals(e)) {
                return i;
            }
        }
        return -1;
    }

    // 删除index位置的元素，返回被删除的元素
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("索引越界");
        }

        E result = data[index];

        // 从index开始，后面的元素往前移动
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        size--;
        data[size] = null;  // 释放引用

        // 缩容检查（防止复杂度震荡）
        if (size == data.length / 4 && data.length > DEFAULT_CAPACITY) {
            resize(data.length / 2);
        }

        return result;
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
        int index = find(e);
        if (index != -1) {
            remove(index);
        }
    }

    // 动态扩容/缩容
    private void resize(int newCapacity) {
        E[] newData = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
        System.out.println("数组容量变化: " + data.length + " -> " + newCapacity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Array: [");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append("], size=").append(size).append(", capacity=").append(data.length);
        return sb.toString();
    }
}