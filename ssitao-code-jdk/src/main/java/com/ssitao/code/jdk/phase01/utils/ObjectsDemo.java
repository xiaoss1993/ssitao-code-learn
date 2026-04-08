package com.ssitao.code.jdk.phase01.utils;

import java.util.*;

/**
 * 第一阶段步骤4: 常用工具类 - Objects和System演示
 */
public class ObjectsDemo {

    public static void main(String[] args) {
        System.out.println("=== Objects工具类 ===\n");

        // 1. 空值检查
        System.out.println("--- 1. 空值检查 ---");
        String nullStr = null;
        String notNullStr = "hello";

        System.out.println("Objects.isNull(nullStr): " + Objects.isNull(nullStr));
        System.out.println("Objects.nonNull(notNullStr): " + Objects.nonNull(notNullStr));

        // requireNonNull
        try {
            Objects.requireNonNull(nullStr, "不能为null");
        } catch (NullPointerException e) {
            System.out.println("requireNonNull(null): " + e.getMessage());
        }
        System.out.println();

        // 2. 对象比较
        System.out.println("--- 2. 对象比较 ---");
        System.out.println("Objects.equals(\"a\", \"a\"): " + Objects.equals("a", "a"));
        System.out.println("Objects.equals(null, null): " + Objects.equals(null, null));
        System.out.println("Objects.equals(null, \"a\"): " + Objects.equals(null, "a"));

        // hashCode
        System.out.println("Objects.hashCode(null): " + Objects.hashCode(null));
        System.out.println("Objects.hashCode(\"abc\"): " + Objects.hashCode("abc"));
        System.out.println("Objects.hash(1, 2, 3): " + Objects.hash(1, 2, 3));
        System.out.println();

        // 3. toString
        System.out.println("--- 3. toString ---");
        System.out.println("Objects.toString(null): " + Objects.toString(null));
        System.out.println("Objects.toString(null, \"default\"): " + Objects.toString(null, "default"));
        System.out.println("Objects.toString(\"hello\"): " + Objects.toString("hello"));
        System.out.println();

        // 4. deepEquals
        System.out.println("--- 4. deepEquals ---");
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3, 4}};
        System.out.println("deepEquals(arr1, arr2): " + Objects.deepEquals(arr1, arr2));
        System.out.println();

        System.out.println("=== System类 ===\n");

        // 5. 数组复制
        System.out.println("--- 5. 数组复制 ---");
        int[] src = {1, 2, 3, 4, 5};
        int[] dest = new int[5];
        System.arraycopy(src, 0, dest, 0, 5);
        System.out.println("System.arraycopy: " + Arrays.toString(dest));
        System.out.println();

        // 6. 时间测量
        System.out.println("--- 6. 时间测量 ---");
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < 1000000; i++) {
            sum += i;
        }
        long end = System.nanoTime();
        System.out.println("计算耗时: " + (end - start) / 1000000.0 + " ms");
        System.out.println();

        // 7. 系统属性
        System.out.println("--- 7. 系统属性 ---");
        System.out.println("java.version: " + System.getProperty("java.version"));
        System.out.println("os.name: " + System.getProperty("os.name"));
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        System.out.println();

        // 8. identityHashCode
        System.out.println("--- 8. identityHashCode ---");
        String str = "hello";
        System.out.println("str.hashCode(): " + str.hashCode());
        System.out.println("System.identityHashCode(str): " + System.identityHashCode(str));
    }
}
