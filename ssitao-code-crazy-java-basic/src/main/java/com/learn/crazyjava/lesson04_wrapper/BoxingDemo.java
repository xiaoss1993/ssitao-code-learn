package com.learn.crazyjava.lesson04_wrapper;

/**
 * 第4课：包装类 - 装箱与拆箱
 */
public class BoxingDemo {
    public static void main(String[] args) {
        // 自动装箱
        Integer i1 = 100;
        Integer i2 = 100;
        System.out.println("100 == 100: " + (i1 == i2));  // true，命中缓存

        Integer i3 = 200;
        Integer i4 = 200;
        System.out.println("200 == 200: " + (i3 == i4));  // false，超出缓存范围

        // 自动拆箱
        int n = i1;  // i1自动拆箱为int
        System.out.println("拆箱后：" + n);

        // 运算时自动拆箱
        System.out.println("相加：" + (i1 + i2));  // 200，自动拆箱后运算

        // 字符串转换
        int a = Integer.parseInt("123");
        double b = Double.parseDouble("3.14");
        boolean c = Boolean.parseBoolean("true");

        System.out.println("parseInt: " + a);
        System.out.println("parseDouble: " + b);
        System.out.println("parseBoolean: " + c);

        // 进制转换
        System.out.println("二进制：" + Integer.toBinaryString(10));   // 1010
        System.out.println("八进制：" + Integer.toOctalString(10));   // 12
        System.out.println("十六进制：" + Integer.toHexString(10));  // a
    }
}
