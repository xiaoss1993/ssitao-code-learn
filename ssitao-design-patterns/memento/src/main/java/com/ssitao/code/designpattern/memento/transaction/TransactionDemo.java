package com.ssitao.code.designpattern.memento.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 事务回滚示例
 *
 * 应用场景：
 * 1. 数据库事务 - 提交/回滚
 * 2. 金融交易 - 交易撤销
 * 3. 订单处理 - 订单状态回滚
 */
public class TransactionDemo {

    public static void main(String[] args) {
        System.out.println("=== 事务回滚示例 ===\n");

        // 创建账户
        Account account = new Account("123456", 10000);

        System.out.println("初始余额: " + account.getBalance());

        // 创建事务管理器
        TransactionManager manager = new TransactionManager();

        // 开始事务
        System.out.println("\n--- 开始转账事务 ---");
        manager.beginTransaction();

        try {
            // 转账操作1: 转出
            System.out.println("转出 3000 元");
            account.withdraw(3000);

            // 保存事务点
            manager.setSavepoint();

            // 转账操作2: 转入（模拟可能失败）
            System.out.println("转入 2000 元");
            account.deposit(2000);

            // 转账操作3: 扣手续费（模拟可能失败）
            System.out.println("扣除手续费 100 元");
            account.withdraw(100);

            // 提交事务
            System.out.println("\n--- 提交事务 ---");
            manager.commit();
            System.out.println("最终余额: " + account.getBalance());

        } catch (Exception e) {
            System.out.println("\n--- 事务失败，回滚到保存点 ---");
            System.out.println("错误: " + e.getMessage());
            manager.rollbackToSavepoint();
            System.out.println("回滚后余额: " + account.getBalance());
        }

        // 再次尝试（这次会成功）
        System.out.println("\n--- 再次尝试转账 ---");
        manager.beginTransaction();

        try {
            System.out.println("转出 1000 元");
            account.withdraw(1000);
            System.out.println("扣除手续费 50 元");
            account.withdraw(50);
            manager.commit();
            System.out.println("最终余额: " + account.getBalance());
        } catch (Exception e) {
            System.out.println("事务失败: " + e.getMessage());
            manager.rollback();
            System.out.println("回滚后余额: " + account.getBalance());
        }
    }
}

/**
 * 账户 - 原发器
 */
class Account {
    private String accountId;
    private double balance;

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    // 存入
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("存款金额必须大于0");
        }
        balance += amount;
    }

    // 取出
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("取款金额必须大于0");
        }
        if (amount > balance) {
            throw new IllegalStateException("余额不足");
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    // 创建备忘录
    public AccountMemento createMemento() {
        return new AccountMemento(accountId, balance);
    }

    // 恢复备忘录
    public void restoreMemento(AccountMemento memento) {
        this.balance = memento.getBalance();
    }
}

/**
 * 账户备忘录
 */
class AccountMemento {
    private String accountId;
    private double balance;
    private long timestamp;

    public AccountMemento(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        this.timestamp = System.currentTimeMillis();
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * 事务管理器 - 管理者
 */
class TransactionManager {
    private List<AccountMemento> undoStack = new ArrayList<>();
    private List<AccountMemento> redoStack = new ArrayList<>();
    private AccountMemento savepoint;

    // 开始事务
    public void beginTransaction() {
        undoStack.clear();
        redoStack.clear();
        System.out.println("事务开始");
    }

    // 设置保存点
    public void setSavepoint() {
        // 这里简化处理，实际应该保存当前账户状态
        System.out.println("设置保存点");
    }

    // 提交事务
    public void commit() {
        undoStack.clear();
        System.out.println("事务已提交");
    }

    // 回滚到保存点
    public void rollbackToSavepoint() {
        if (!undoStack.isEmpty()) {
            // 回滚到上一个状态
            AccountMemento lastState = undoStack.get(undoStack.size() - 1);
            redoStack.add(lastState);
            System.out.println("已回滚到上一个状态");
        }
    }

    // 完全回滚
    public void rollback() {
        while (!undoStack.isEmpty()) {
            AccountMemento state = undoStack.remove(undoStack.size() - 1);
            redoStack.add(state);
        }
        System.out.println("事务已完全回滚");
    }
}
