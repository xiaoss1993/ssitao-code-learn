package com.ssitao.code.jdk.phase01.wrapper;

import java.util.Arrays;

/**
 * 第一阶段步骤3: 包装类 - Math工具类演示
 */
public class MathDemo {

    public static void main(String[] args) {
        System.out.println("=== Math工具类 ===\n");

        // 1. 常用常量
        System.out.println("--- 1. 常用常量 ---");
        System.out.println("Math.PI = " + Math.PI);
        System.out.println("Math.E = " + Math.E);
        System.out.println();

        // 2. 取整函数
        System.out.println("--- 2. 取整函数 ---");
        double num = 3.7;
        System.out.println("num = " + num);
        System.out.println("Math.floor(num) = " + Math.floor(num) + " (向下取整)");
        System.out.println("Math.ceil(num) = " + Math.ceil(num) + " (向上取整)");
        System.out.println("Math.round(num) = " + Math.round(num) + " (四舍五入)");
        System.out.println("Math.rint(3.5) = " + Math.rint(3.5) + " (返回最接近的double)");
        System.out.println();

        // 3. 绝对值
        System.out.println("--- 3. 绝对值 ---");
        System.out.println("Math.abs(-10) = " + Math.abs(-10));
        System.out.println("Math.absExact(Integer.MIN_VALUE) = " + Math.absExact(Integer.MIN_VALUE) + " (溢出时抛异常)");
        try {
            Math.absExact(Integer.MIN_VALUE);
        } catch (ArithmeticException e) {
            System.out.println("Integer.MIN_VALUE绝对值溢出,抛ArithmeticException!");
        }
        System.out.println();

        // 4. 最大最小值
        System.out.println("--- 4. 最大最小值 ---");
        System.out.println("Math.max(10, 20) = " + Math.max(10, 20));
        System.out.println("Math.min(10, 20) = " + Math.min(10, 20));
        System.out.println();

        // 5. 幂和开方
        System.out.println("--- 5. 幂和开方 ---");
        System.out.println("Math.pow(2, 3) = " + Math.pow(2, 3) + " (2的3次方)");
        System.out.println("Math.sqrt(16) = " + Math.sqrt(16) + " (平方根)");
        System.out.println("Math.cbrt(27) = " + Math.cbrt(27) + " (立方根)");
        System.out.println();

        // 6. 对数
        System.out.println("--- 6. 对数 ---");
        System.out.println("Math.log(Math.E) = " + Math.log(Math.E) + " (自然对数)");
        System.out.println("Math.log10(100) = " + Math.log10(100) + " (以10为底)");
        System.out.println("Math.log1p(Math.E - 1) = " + Math.log1p(Math.E - 1) + " (ln(1+x),高精度)");
        System.out.println();

        // 7. 三角函数
        System.out.println("--- 7. 三角函数 ---");
        System.out.println("Math.sin(Math.PI / 2) = " + Math.sin(Math.PI / 2));
        System.out.println("Math.cos(Math.PI) = " + Math.cos(Math.PI));
        System.out.println("Math.tan(Math.PI / 4) = " + Math.tan(Math.PI / 4));
        System.out.println("Math.toDegrees(Math.PI) = " + Math.toDegrees(Math.PI) + " (弧度转角度)");
        System.out.println("Math.toRadians(180) = " + Math.toRadians(180) + " (角度转弧度)");
        System.out.println();

        // 8. 随机数
        System.out.println("--- 8. 随机数 ---");
        System.out.println("Math.random() = " + Math.random() + " [0,1)");
        System.out.println("生成[1,100]随机整数: " + ((int)(Math.random() * 100) + 1));
        System.out.println();

        // 9. 位运算辅助方法
        System.out.println("--- 9. 位运算辅助方法 ---");
        System.out.println("Math.abs(-16) 二进制中1的个数: " + Integer.bitCount(-16));
        System.out.println("Integer.highestOneBit(156) = " + Integer.highestOneBit(156) + " (最高位1)");
        System.out.println("Integer.lowestOneBit(156) = " + Integer.lowestOneBit(156) + " (最低位1)");
        System.out.println("Integer.numberOfLeadingZeros(16) = " + Integer.numberOfLeadingZeros(16) + " (前导零)");
        System.out.println("Integer.numberOfTrailingZeros(16) = " + Integer.numberOfTrailingZeros(16) + " (尾随零)");
    }
}
