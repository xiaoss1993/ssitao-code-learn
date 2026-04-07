package com.ssitao.code.effectivejava.ch06.item38;

import java.util.*;
import java.util.function.*;

/**
 * 条目38：优先使用方法引用而非Lambda
 *
 * 方法引用通常比Lambda更简洁、更易读
 *
 * 方法引用的四种类型：
 * 1. 静态方法引用：Class::staticMethod
 * 2. 绑定实例方法引用：object::instanceMethod
 * 3. 未绑定实例方法引用：Class::instanceMethod
 * 4. 构造器引用：Class::new
 */
public class MethodReferences {
    public static void main(String[] args) {
        System.out.println("=== 方法引用 vs Lambda ===\n");

        List<String> names = Arrays.asList("Bob", "Alice", "Charlie", "David");

        // Lambda（冗长）
        System.out.println("Lambda:");
        names.forEach((s) -> System.out.println(s));

        // 方法引用（简洁）
        System.out.println("\n方法引用:");
        names.forEach(System.out::println);

        System.out.println("\n=== 方法引用类型 ===\n");

        // 1. 静态方法引用：Class::staticMethod
        Function<String, Integer> parser1 = (s) -> Integer.parseInt(s);
        Function<String, Integer> parser2 = Integer::parseInt;
        System.out.println("静态方法: Integer::parseInt -> " + parser1.apply("42"));

        // 2. 绑定实例方法引用：object::instanceMethod
        String prefix = "Hello, ";
        Function<String, String> concat1 = (s) -> prefix.concat(s);
        Function<String, String> concat2 = prefix::concat;
        System.out.println("绑定实例: prefix::concat -> " + concat2.apply("World"));

        // 3. 未绑定实例方法引用：Class::instanceMethod
        Comparator<String> comp1 = (a, b) -> a.compareToIgnoreCase(b);
        Comparator<String> comp2 = String::compareToIgnoreCase;
        System.out.println("未绑定实例: String::compareToIgnoreCase -> " +
            comp2.compare("Apple", "apple"));

        // 4. 构造器引用：Class::new
        Supplier<ArrayList<String>> list1 = () -> new ArrayList<>();
        Supplier<ArrayList<String>> list2 = ArrayList::new;
        System.out.println("构造器: ArrayList::new -> " + list2.get());

        System.out.println("\n=== 何时保留Lambda ===\n");
        // 有时Lambda更清晰
        Comparator<String> naturalOrder = String::compareTo;        // OK
        Comparator<String> revNaturalOrder = (a, b) -> b.compareTo(a); // OK - Lambda更清晰

        // 规则：如果Lambda太长，提取为方法并使用方法引用
        System.out.println("规则：除非Lambda更清晰，否则优先使用方法引用");
    }
}
