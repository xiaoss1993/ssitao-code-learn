package com.ssitao.code.effectivejava.ch01.item06;

import java.util.regex.Pattern;

/**
 * 条目6：避免创建不必要的对象
 *
 * 避免创建不必要对象的建议：
 * 1. 优先使用原始类型而非装箱类型，避免自动装箱
 * 2. 复用不可变对象（如String常量池）
 * 3. 缓存昂贵对象（如编译后的正则表达式）
 * 4. 注意：不要为了复用而牺牲清晰的代码
 */
public class AvoidUnnecessaryObjects {

    /**
     * 错误：自动装箱创建大量对象
     * Long是装箱类型，每次+=都会创建新的Long对象
     */
    static void badAutoBoxing() {
        Long sum = 0L;  // 注意是Long，不是long！
        long start = System.nanoTime();
        for (long i = 0; i < 1_000_000; i++) {
            sum += i;  // 每次运算都会创建新的Long对象！
        }
        long end = System.nanoTime();
        System.out.println("错误方式(Long): " + (end - start) / 1_000_000 + "ms");
    }

    /**
     * 正确：使用原始类型
     * long是原始类型，直接进行数值运算，无对象创建
     */
    static void goodAutoBoxing() {
        long sum = 0L;  // 使用原始类型long
        long start = System.nanoTime();
        for (long i = 0; i < 1_000_000; i++) {
            sum += i;  // 直接数值运算，无装箱
        }
        long end = System.nanoTime();
        System.out.println("正确方式(long): " + (end - start) / 1_000_000 + "ms");
    }

    // 正确：缓存编译后的Pattern
    // Pattern.compile()是一个昂贵操作，应只执行一次
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{3}-\\d{4}");

    static boolean isValidGood(String input) {
        return PHONE_PATTERN.matcher(input).matches();
    }

    public static void main(String[] args) {
        System.out.println("=== 避免创建不必要对象示例 ===\n");

        System.out.println("--- 自动装箱对比 ---");
        badAutoBoxing();   // 慢：每次创建新Long对象
        goodAutoBoxing();  // 快：直接数值运算

        System.out.println("\n--- String常量池 ---");
        String s1 = new String("hello");  // 错误：创建新对象
        String s2 = "hello";               // 正确：从常量池获取
        System.out.println("s1 == s2: " + (s1 == s2) + " (s1用new创建，s2从常量池)");
        // 结果为false，因为s1和s2引用不同对象

        System.out.println("\n--- Pattern缓存 ---");
        String phone = "123-4567";
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            isValidGood(phone);  // 复用编译后的Pattern
        }
        long end = System.nanoTime();
        System.out.println("使用缓存的Pattern: " + (end - start) / 1_000_000 + "ms");
    }
}
