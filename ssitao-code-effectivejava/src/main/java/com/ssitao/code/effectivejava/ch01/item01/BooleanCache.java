package com.ssitao.code.effectivejava.ch01.item01;

/**
 * Item 1: Consider static factory methods instead of constructors
 *
 * Demonstrates Boolean.valueOf() caching mechanism
 */
public class BooleanCache {
    public static void main(String[] args) {
        System.out.println("=== Static Factory Method Demo ===\n");

        // Boolean.valueOf() reuses cached instances
        Boolean b1 = Boolean.valueOf(true);
        Boolean b2 = Boolean.valueOf(true);
        Boolean b3 = Boolean.valueOf(false);
        Boolean b4 = Boolean.valueOf(false);

        System.out.println("Boolean.valueOf(true) == Boolean.valueOf(true): " + (b1 == b2));
        System.out.println("Boolean.valueOf(false) == Boolean.valueOf(false): " + (b3 == b4));

        System.out.println("\nConclusion: Static factory methods can reuse objects");
    }
}
