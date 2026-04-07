package com.ssitao.code.effectivejava.ch07.item41;

/**
 * 条目41：使用异常链保留根本原因
 *
 * 当捕获并重新抛出异常时，使用异常链保留原始原因
 *
 * 异常链的好处：
 * 1. 保留原始异常信息，便于调试
 * 2. 调用者可以看到完整的异常堆栈
 * 3. 不丢失重要的诊断信息
 */
public class ExceptionChaining {
    public static void main(String[] args) {
        System.out.println("=== 异常链 ===\n");

        // 错误：丢失原始原因
        try {
            processWithLostCause();
        } catch (Exception e) {
            System.out.println("丢失的原因: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n--- 正确做法如下 ---\n");

        // 正确：使用异常链保留原因
        try {
            processWithChaining();
        } catch (Exception e) {
            System.out.println("链接的原因: " + e.getCause());
            System.out.println("原始异常: " + e.getMessage());
        }
    }

    static void processWithLostCause() {
        try {
            // 模拟底层异常
            throw new RuntimeException("底层错误");
        } catch (RuntimeException e) {
            // 错误：原始原因丢失了！
            throw new RuntimeException("高层错误");
        }
    }

    static void processWithChaining() throws HigherLevelException {
        try {
            throw new RuntimeException("底层错误");
        } catch (RuntimeException e) {
            // 正确：链接异常，保留原因
            throw new HigherLevelException("高层错误", e);
        }
    }

    /**
     * 支持异常链的异常类
     */
    static class HigherLevelException extends Exception {
        public HigherLevelException(String message, Throwable cause) {
            super(message, cause);  // 将原因传递给父类
        }
    }

    // 替代方式：使用 initCause() 和 getCause()
    static void alternativeChaining() {
        try {
            throw new RuntimeException("底层错误");
        } catch (RuntimeException e) {
            RuntimeException highLevel = new RuntimeException("高层错误");
            highLevel.initCause(e);  // 手动链接
            throw highLevel;
        }
    }
}
