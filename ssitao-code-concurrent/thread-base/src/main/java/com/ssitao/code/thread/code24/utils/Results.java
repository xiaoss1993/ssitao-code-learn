package com.ssitao.code.thread.code24.utils;

/**
 * 结果存储类
 *
 * 存储矩阵搜索的结果。
 * 数组的每个元素对应矩阵的一行，表示该行中找到目标数字的次数。
 *
 * 设计说明：
 * - 每个搜索线程（Searcher）负责处理矩阵的一部分行
 * - 每个线程将其搜索结果写入 results 数组对应行索引的位置
 * - 由于每个线程写入的是不同的行索引位置，所以不需要额外的同步机制
 *
 * 示例：
 * - results.setData(5, 10) 表示第5行找到了目标数字10次
 */
public class Results {
    /**
     * 结果数组
     * 每个元素对应矩阵的一行，记录该行中找到目标数字的次数
     */
    private int[] data;

    /**
     * 构造函数
     *
     * @param size 数组长度，通常与矩阵的行数相同
     */
    public Results(int size) {
        this.data = new int[size];
    }

    /**
     * 设置指定位置的搜索结果
     *
     * @param position 位置（对应矩阵的行索引）
     * @param value    该行中找到目标数字的次数
     */
    public void setData(int position, int value) {
        data[position] = value;
    }

    /**
     * 获取所有搜索结果
     *
     * @return 结果数组
     */
    public int[] getData() {
        return data;
    }
}