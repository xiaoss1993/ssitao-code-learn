package com.ssitao.code.effectivejava.ch07.item39;

/**
 * 条目39：只在异常情况下使用异常
 *
 * 异常应该只用于异常情况，不应该用于普通控制流
 *
 * 异常层次结构：
 * - Error: JVM错误，不应该捕获
 * - RuntimeException: 非受查异常，通常是编程错误
 * - IOException: 受查异常，必须处理
 *
 * 异常的性能问题：
 * - 创建异常对象开销大（需要收集栈追踪信息）
 * - 不要用异常代替正常的控制流
 */
public class ExceptionHierarchy {
    public static void main(String[] args) {
        System.out.println("=== 异常层次结构 ===\n");

        System.out.println("Throwable");
        System.out.println("├── Error (非受查) - JVM错误，不要捕获！");
        System.out.println("│   ├── OutOfMemoryError");
        System.out.println("│   └── StackOverflowError");
        System.out.println("└── Exception");
        System.out.println("    ├── RuntimeException (非受查)");
        System.out.println("    │   ├── NullPointerException");
        System.out.println("    │   ├── IllegalArgumentException");
        System.out.println("    │   └── IndexOutOfBoundsException");
        System.out.println("    └── IOException (受查) - 必须处理");

        System.out.println("\n=== 错误：用异常控制流程 ===\n");

        // 错误！异常用于异常情况
        int[] array = {1, 2, 3};
        try {
            int i = 0;
            while (true) {
                int value = array[i++];  // 当 i >= length 时抛出异常
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // 找到末尾了 - 这是错误的使用方式！
        }

        // 正确：使用适当的循环
        for (int value : array) {
            System.out.println("值: " + value);
        }

        System.out.println("\n=== 错误：过度使用异常 ===\n");

        // 错误！不要用try-catch处理简单逻辑
        // 这很慢，因为创建异常开销很大
        String s = "123";
        try {
            int parsed = Integer.parseInt(s);
            System.out.println("解析结果: " + parsed);
        } catch (NumberFormatException e) {
            System.out.println("无效数字");
        }

        // 正确：先检查
        if (s != null && s.matches("\\d+")) {
            System.out.println("正确方式：先检查再解析");
        }

        System.out.println("\n=== 异常性能 ===\n");
        System.out.println("创建异常比简单检查慢很多");
        System.out.println("- 栈追踪创建开销大");
        System.out.println("- 只在异常情况下使用异常");
        System.out.println("- 控制流使用if-else或循环");
    }
}
