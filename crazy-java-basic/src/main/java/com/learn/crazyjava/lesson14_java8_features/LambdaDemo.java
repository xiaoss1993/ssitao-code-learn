package com.learn.crazyjava.lesson14_java8_features;

import java.util.*;
import java.util.function.*;

/**
 * 第14课：Java 8新特性 - Lambda表达式
 */
public class LambdaDemo {
    public static void main(String[] args) {
        // Lambda基本语法
        Runnable r1 = () -> System.out.println("无参数Lambda");
        r1.run();

        Consumer<String> c1 = (s) -> System.out.println("消费：" + s);
        c1.accept("hello");

        Function<Integer, Integer> f1 = (x) -> x * 2;
        System.out.println("Function: " + f1.apply(5));

        Supplier<String> s1 = () -> "Supplier";
        System.out.println("Supplier: " + s1.get());

        Predicate<Integer> p1 = (x) -> x > 0;
        System.out.println("Predicate: " + p1.test(10));

        // 方法引用
        List<String> names = Arrays.asList("Tom", "Jerry", "Mike");
        names.forEach(System.out::println);

        Function<String, Integer> parser = Integer::parseInt;
        System.out.println("方法引用：" + parser.apply("123"));

        // 类型推断
        Comparator<String> comp = (s1, s2) -> s1.length() - s2.length();
        names.sort(comp);
        System.out.println("排序后：" + names);
    }
}
