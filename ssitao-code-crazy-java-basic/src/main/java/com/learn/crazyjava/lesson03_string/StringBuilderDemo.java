package com.learn.crazyjava.lesson03_string;

/**
 * StringBuilder高效拼接演示
 */
public class StringBuilderDemo {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        // 使用StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append("test");
        }
        String result = sb.toString();

        long end = System.currentTimeMillis();
        System.out.println("StringBuilder耗时：" + (end - start) + "ms");
    }
}
