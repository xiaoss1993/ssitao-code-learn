package com.ssitao.code.thread.code13.task;

/**
 * 帐户类，模拟一个银行帐户
 *
 * 【问题演示】
 * 该类的addAccount()和subtractAmount()方法存在线程安全问题。
 * 在多线程环境下，多个线程可能同时读取、计算、写入同一个账户的余额，
 * 导致更新丢失（Lost Update）问题。
 *
 * 典型问题场景：
 * 1. 线程A读取balance=1000到临时变量tmp
 * 2. 线程B也读取balance=1000到临时变量tmp
 * 3. 线程A计算tmp=1000+1000=2000，写入balance=2000
 * 4. 线程B计算tmp=1000-1000=0，写入balance=0
 * 结果：线程A的更新被线程B覆盖，丢失了+1000的操作
 *
 * 解决方案：使用synchronized关键字对方法或代码块进行同步
 */
public class Account {
    /**
     * 帐户余额
     * 使用double类型，用于存储货币金额
     */
    private double balance;

    /**
     * 获取帐户余额
     *
     * @return 帐户余额
     */
    public double getBalance() {
        return balance;
    }

    /**
     * 设置帐户余额
     *
     * @param balance 帐户余额
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * 增加帐户余额
     *
     * 【问题代码】
     * 该方法存在竞态条件：
     * 1. 先读取当前余额到临时变量
     * 2. 休眠10毫秒（模拟业务处理延迟）
     * 3. 计算新余额
     * 4. 写回余额
     *
     * 在步骤2休眠期间，其他线程可能也读取了相同的旧值，
     * 导致后续的写操作覆盖其他线程的更新。
     *
     * @param amount 增加的余额
     */
    public void addAccount(double amount) {
        // 步骤1：读取当前余额到临时变量
        double tmp = balance;
        try {
            // 步骤2：休眠10毫秒，模拟业务处理延迟
            // 这会放大竞态条件问题，让其他线程有机会在此期间读取相同的旧值
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 步骤3：计算新余额（基于可能已过时的tmp值）
        tmp += amount;
        // 步骤4：将计算结果写回余额
        // 如果其他线程在此期间修改了balance，这里的赋值会覆盖其修改
        balance = tmp;
    }

    /**
     * 减少帐户余额
     *
     * 【问题代码】
     * 与addAccount()方法存在相同的竞态条件问题。
     * 详细分析请参见addAccount()方法的注释。
     *
     * @param amount 减少的余额
     */
    public void subtractAmount(double amount) {
        // 步骤1：读取当前余额到临时变量
        double tmp = balance;
        try {
            // 步骤2：休眠10毫秒，模拟业务处理延迟
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 步骤3：计算新余额
        tmp -= amount;
        // 步骤4：写回余额
        balance = tmp;
    }
}
