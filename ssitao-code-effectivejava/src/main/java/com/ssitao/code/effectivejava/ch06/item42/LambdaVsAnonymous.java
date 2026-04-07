package com.ssitao.code.effectivejava.ch06.item42;

import java.util.*;
import java.util.function.*;

/**
 * 条目42：优先使用Lambda而非匿名类
 *
 * Lambda的优点：
 * 1. 更简洁：不需要完整的类声明
 * 2. 更易读：代码意图更清晰
 * 3. 更符合函数式编程风格
 *
 * 何时仍用匿名类：
 * - 需要实现多个抽象方法的接口
 * - 需要特殊处理 'this' 引用
 */
public class LambdaVsAnonymous {
    public static void main(String[] args) {
        System.out.println("=== 匿名类（Java 8之前） ===\n");

        // 旧方式：匿名类
        Comparator<String> anonymousComparator = new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return Integer.compare(a.length(), b.length());
            }
        };

        System.out.println("匿名类 compare('abc', 'xy'): " +
            anonymousComparator.compare("abc", "xy"));

        System.out.println("\n=== Lambda（Java 8+） ===\n");

        // 新方式：Lambda
        Comparator<String> lambdaComparator = (a, b) -> Integer.compare(a.length(), b.length());

        System.out.println("Lambda compare('abc', 'xy'): " +
            lambdaComparator.compare("abc", "xy"));

        System.out.println("\n=== 更简洁：方法引用 ===\n");

        // 最佳：Comparator.comparing + 方法引用
        Comparator<String> methodRefComparator = Comparator.comparingInt(String::length);

        System.out.println("方法引用 compare('abc', 'xy'): " +
            methodRefComparator.compare("abc", "xy"));

        System.out.println("\n=== 何时仍用匿名类 ===\n");

        // Lambda适用于任何函数式接口（单一抽象方法）
        Runnable task = () -> System.out.println("Lambda runnable");
        task.run();

        // 何时用匿名类：
        // - 非函数式接口（多个抽象方法）
        // - 需要特殊处理 'this' 引用

        System.out.println("\n=== Lambda 'this' vs 匿名类 'this' ===\n");

        class LambdaOuter {
            private String value = "outer";

            public void demonstrate() {
                // Lambda：'this' 指向外围实例
                Runnable lambda = () -> System.out.println("Lambda this: " + this);
                // 匿名类：'this' 指向匿名类实例
                Runnable anonymous = new Runnable() {
                    @Override
                    public void run() {
                        // System.out.println("Anonymous this: " + this); // 需要 OuterClass.this
                        System.out.println("Anonymous外围 this: " + LambdaOuter.this);
                    }
                };

                lambda.run();
                anonymous.run();
            }

            @Override
            public String toString() {
                return "LambdaOuter{" + "value='" + value + '\'' + '}';
            }
        }

        new LambdaOuter().demonstrate();

        System.out.println("\n=== 最佳实践 ===\n");

        // 1. Lambda用于函数式接口
        Function<String, Integer> length = String::length;

        // 2. 尽可能使用方法引用
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        names.sort(String::compareToIgnoreCase);

        // 3. Lambda保持简短 - 太长则提取为方法
        // 4. 不要滥用流（条目40）
    }
}
