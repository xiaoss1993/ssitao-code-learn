package com.ssitao.code.thread.code42.utils;

import java.util.Random;

/**
 * 数组生成工具类
 *
 * 用于生成指定长度的整型数组，数组元素值为 [0, 10) 范围内的随机整数
 */
public class ArrayGenerator {

    /**
     * 生成指定长度的整型数组
     *
     * 生成的数组元素值范围：0 ~ 9（包含）
     * 使用 Random 类生成伪随机数
     *
     * @param size 数组长度
     * @return 生成的整型数组
     */
    public int[] generateArray(int size) {
        int array[] = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            // nextInt(10) 返回 [0, 10) 范围内的随机整数
            array[i] = random.nextInt(10);
        }
        return array;
    }
}
