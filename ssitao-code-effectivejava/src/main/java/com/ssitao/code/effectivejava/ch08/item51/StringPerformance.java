package com.ssitao.code.effectivejava.ch08.item51;

import java.util.*;

/**
 * 条目51：注意字符串拼接的性能
 *
 * 在循环中用+拼接字符串会创建大量中间String对象
 * String是不可变的，每次拼接都会创建新对象
 *
 * 解决方案：
 * - 循环中使用StringBuilder
 * - 少量已知字符串用+即可
 * - Java 8+用String.join()连接字符串列表
 */
public class StringPerformance {
    public static void main(String[] args) {
        System.out.println("=== 字符串拼接性能 ===\n");

        // 错误：循环中用+拼接
        System.out.println("循环中使用+:");
        long start = System.nanoTime();
        String bad = "";
        for (int i = 0; i < 1000; i++) {
            bad += "x";  // 每次迭代创建新String对象
        }
        long end = System.nanoTime();
        System.out.println("时间: " + (end - start) / 1000 + " 微秒");

        // 正确：使用StringBuilder
        System.out.println("\n使用StringBuilder:");
        start = System.nanoTime();
        StringBuilder good = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            good.append("x");
        }
        end = System.nanoTime();
        System.out.println("时间: " + (end - start) / 1000 + " 微秒");

        System.out.println("\n=== 何时用+没问题 ===\n");

        // 单次拼接没问题
        String name = "Hello" + " " + "World";  // 编译器会优化

        // 少量已知字符串
        String msg = "The value is " + 42;  // OK

        System.out.println("单次拼接OK: " + name);
        System.out.println("简单拼接OK: " + msg);

        System.out.println("\n=== StringBuilder最佳实践 ===\n");

        // 1. 已知容量时指定
        StringBuilder sb = new StringBuilder(100);  // 避免扩容

        // 2. 方法链
        String result = new StringBuilder()
            .append("Start")
            .append("-")
            .append("Middle")
            .append("-")
            .append("End")
            .toString();
        System.out.println("链式调用: " + result);

        // 3. StringBuilder非线程安全（需要线程安全时用StringBuffer）
        // StringBuffer sb2 = new StringBuffer();  // 线程安全，但较慢

        System.out.println("\n=== 构建大字符串 ===\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve");

        // 错误：多次拼接
        String badJoin = "";
        for (String name2 : names) {
            badJoin += name2 + ", ";  // 循环中很糟糕
        }
        System.out.println("错误拼接: " + badJoin);

        // 正确：StringBuilder
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            if (i > 0) sb2.append(", ");
            sb2.append(names.get(i));
        }
        System.out.println("正确拼接: " + sb2);

        // 最佳：使用join (Java 8+)
        System.out.println("最佳join: " + String.join(", ", names));
    }
}
