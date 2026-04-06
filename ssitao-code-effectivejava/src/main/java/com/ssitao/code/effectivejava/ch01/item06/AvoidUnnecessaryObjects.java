package com.ssitao.code.effectivejava.ch01.item06;

import java.util.regex.Pattern;

/**
 * Item 6: Avoid creating unnecessary objects
 */
public class AvoidUnnecessaryObjects {

    // BAD: Auto boxing creates many objects
    static void badAutoBoxing() {
        Long sum = 0L;  // Long, not long!
        long start = System.nanoTime();
        for (long i = 0; i < 1_000_000; i++) {
            sum += i;  // Creates new Long each iteration!
        }
        long end = System.nanoTime();
        System.out.println("Bad (Long): " + (end - start) / 1_000_000 + "ms");
    }

    // GOOD: Use primitive type
    static void goodAutoBoxing() {
        long sum = 0L;  // primitive!
        long start = System.nanoTime();
        for (long i = 0; i < 1_000_000; i++) {
            sum += i;
        }
        long end = System.nanoTime();
        System.out.println("Good (long): " + (end - start) / 1_000_000 + "ms");
    }

    // GOOD: Cache compiled Pattern
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{3}-\\d{4}");

    static boolean isValidGood(String input) {
        return PHONE_PATTERN.matcher(input).matches();
    }

    public static void main(String[] args) {
        System.out.println("=== Avoid Unnecessary Objects Demo ===\n");

        System.out.println("--- Auto Boxing Comparison ---");
        badAutoBoxing();
        goodAutoBoxing();

        System.out.println("\n--- String Pool ---");
        String s1 = new String("hello");  // BAD: creates new object
        String s2 = "hello";               // GOOD: from pool
        System.out.println("s1 == s2: " + (s1 == s2) + " (s1 uses new, s2 from pool)");

        System.out.println("\n--- Pattern Caching ---");
        String phone = "123-4567";
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            isValidGood(phone);
        }
        long end = System.nanoTime();
        System.out.println("With cached Pattern: " + (end - start) / 1_000_000 + "ms");
    }
}
