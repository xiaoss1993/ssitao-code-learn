package com.ssitao.code.effectivejava.ch06.item39;

import java.util.*;
import java.util.function.*;

/**
 * 条目39：优先使用标准函数式接口
 *
 * java.util.function 提供了43个函数式接口
 * 只有当需要唯一签名或特定语义时才创建自定义接口
 *
 * 常用标准接口：
 * - Function<T, R>: T -> R 转换
 * - Predicate<T>: T -> boolean 判断
 * - Consumer<T>: T -> void 消费
 * - Supplier<T>: () -> T 生产
 */
public class FunctionalInterfaces {
    public static void main(String[] args) {
        System.out.println("=== 标准函数式接口 ===\n");

        // 基本 Function<T, R>
        Function<String, Integer> strLen = String::length;
        System.out.println("Function<String, Integer>: 'Hello'的长度 = " + strLen.apply("Hello"));

        // UnaryOperator<T> (T -> T)
        UnaryOperator<String> upper = String::toUpperCase;
        System.out.println("UnaryOperator: 'hello' -> " + upper.apply("hello"));

        // BinaryOperator<T> (T, T -> T)
        BinaryOperator<Integer> sum = Integer::sum;
        System.out.println("BinaryOperator: sum(3, 4) = " + sum.apply(3, 4));

        // Predicate<T> (T -> boolean)
        Predicate<String> isEmpty = String::isEmpty;
        System.out.println("Predicate: isEmpty('') = " + isEmpty.test(""));

        // Supplier<T> (() -> T)
        Supplier<Random> rng = Random::new;
        System.out.println("Supplier: 随机整数 = " + rng.get().nextInt(100));

        // Consumer<T> (T -> void)
        Consumer<String> printer = System.out::println;
        printer.accept("Consumer: Hello from Consumer!");

        System.out.println("\n=== 原始类型特化 ===\n");

        // 避免装箱
        IntFunction<double[]> arrayCreator = double[]::new;
        System.out.println("IntFunction<double[]>: 创建长度为5的数组");
        double[] arr = arrayCreator.apply(5);
        System.out.println("数组长度: " + arr.length);

        // ToIntFunction, IntToDoubleFunction 等
        IntPredicate isEven = (i) -> i % 2 == 0;
        System.out.println("IntPredicate: isEven(4) = " + isEven.test(4));

        System.out.println("\n=== 何时需要自定义接口 ===\n");

        // 需要标准库没有的签名时
        // 例如：Java 8之前没有三参数函数

        // 示例：Comparator<T> 是特殊的（有语义，用于排序）
        Comparator<String> byLength = Comparator.comparingInt(String::length);
        System.out.println("Comparator (特殊): 比较 'ab' vs 'abc' = " +
            byLength.compare("ab", "abc"));

        System.out.println("\n=== 需要特定签名的接口 ===\n");
        System.out.println("- ToIntBiFunction<T, U> 当需要 (T, U) -> int");
        System.out.println("- IntBinaryOperator 当两个参数都是int");
        System.out.println("- ObjIntConsumer<T> 当需要 (T, int) -> void");
    }
}
