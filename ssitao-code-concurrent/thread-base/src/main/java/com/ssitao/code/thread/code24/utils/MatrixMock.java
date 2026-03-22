package com.ssitao.code.thread.code24.utils;

import java.util.Random;

/**
 * 矩阵模拟类
 *
 * 用于生成随机数据的二维矩阵，模拟搜索任务的输入数据。
 * 矩阵中填充0-9的随机整数。
 */
public class MatrixMock {
    /**
     * 二维矩阵数据
     * 第一维表示行，第二维表示列
     */
    private int[][] data;

    /**
     * 构造函数
     *
     * 创建一个指定大小的矩阵，随机填充0-9的数字。
     * 同时统计目标数字在矩阵中出现的总次数。
     *
     * @param size   矩阵的行数
     * @param length 每行的列数
     * @param number 要搜索的目标数字
     */
    public MatrixMock(int size, int length, int number) {
        int counter = 0;
        // 创建矩阵
        data = new int[size][length];
        Random random = new Random();

        // 填充随机数据并统计目标数字出现次数
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < length; j++) {
                // 生成0-9的随机数
                data[i][j] = random.nextInt(10);
                // 如果是目标数字，增加计数器
                if (data[i][j] == number) {
                    counter++;
                }
            }
        }

        // 打印生成的数据统计信息
        System.out.printf("Mock: There are %d ocurrences of number in generated data.\n", counter, number);
    }

    /**
     * 获取指定行的数据
     *
     * @param row 行索引
     * @return 该行的数据数组，如果行号无效则返回 null
     */
    public int[] getRow(int row) {
        if (row >= 0 && row < data.length) {
            return data[row];
        }
        return null;
    }
}