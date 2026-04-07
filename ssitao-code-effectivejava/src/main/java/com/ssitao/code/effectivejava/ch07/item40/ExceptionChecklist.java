package com.ssitao.code.effectivejava.ch07.item40;

import java.io.IOException;

/**
 * 条目40：遵循受查异常的约定
 *
 * 受查异常：调用者必须恢复或传播（try-catch或throws）
 * 非受查异常：程序员错误（代码bug）
 * 错误：JVM错误（通常不捕获）
 */
public class ExceptionChecklist {
    public static void main(String[] args) {
        System.out.println("=== 异常类型 ===\n");

        System.out.println("1. 受查异常 (继承Exception，不是RuntimeException)");
        System.out.println("   - 必须捕获或在方法签名中声明");
        System.out.println("   - 用于：可恢复的条件");
        System.out.println("   - 示例：IOException, SQLException");

        System.out.println("\n2. 非受查异常 (继承RuntimeException)");
        System.out.println("   - 不需要捕获或声明");
        System.out.println("   - 用于：编程错误（bug）");
        System.out.println("   - 示例：NullPointerException, IllegalArgumentException");

        System.out.println("\n3. 错误 (继承Error)");
        System.out.println("   - JVM错误，不要尝试捕获");
        System.out.println("   - 示例：OutOfMemoryError, StackOverflowError");

        System.out.println("\n=== 异常设计检查清单 ===\n");

        // ✓ 做：受查异常用于可恢复条件
        // ✓ 做：提供帮助调用者恢复的方法
        // ✓ 做：用@throws文档化异常
        // ✓ 做：包含失败捕获信息
        // ✗ 不做：用异常控制流程
        // ✗ 不做：过度使用受查异常（负担重）
        // ✗ 不做：捕获通用的Exception或Throwable

        demonstrateCorrectUsage();
    }

    /**
     * 好：受查异常 + 恢复方法
     */
    public static class Account {
        private double balance;

        public void deposit(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("金额必须为正");
            }
            balance += amount;
        }

        public void withdraw(double amount) throws InsufficientFundsException {
            if (amount > balance) {
                throw new InsufficientFundsException(
                    "余额: " + balance + ", 请求金额: " + amount
                );
            }
            balance -= amount;
        }

        // 恢复方法帮助调用者避免异常
        public boolean tryWithdraw(double amount) {
            if (amount <= balance) {
                balance -= amount;
                return true;
            }
            return false;
        }
    }

    public static class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    private static void demonstrateCorrectUsage() {
        System.out.println("=== 正确的异常使用 ===\n");

        Account account = new Account();
        account.deposit(100);

        // 选项1：先检查后操作（避免异常）
        if (account.tryWithdraw(50)) {
            System.out.println("取款50，成功");
        }

        // 选项2：捕获异常处理
        try {
            account.withdraw(1000);  // 会失败
        } catch (InsufficientFundsException e) {
            System.out.println("捕获异常: " + e.getMessage());
        }

        System.out.println("\n✓ 当调用者可能失败时使用 tryWithdraw()");
        System.out.println("✓ 当调用者必须处理失败时使用 withdraw()");
    }
}
