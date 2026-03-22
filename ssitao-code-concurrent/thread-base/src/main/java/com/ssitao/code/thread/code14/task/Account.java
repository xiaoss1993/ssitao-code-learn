package com.ssitao.code.thread.code14.task;

/**
 * 帐户类，模拟一个银行帐户
 *
 * 使用synchronized关键字实现线程同步，保证并发访问时的数据一致性。
 *
 * 【synchronized锁机制说明】
 * 1. 锁的对象：synchronized修饰的方法锁的是this，即当前Account对象实例本身
 * 2. 锁的作用：同一时刻只能有一个线程执行该对象的synchronized方法
 * 3. addAccount()和subtractAmount()是同步的：它们锁的是同一个对象，调用时互斥
 *
 * 【工作原理示意】
 *   线程A(Company)调用addAccount()    线程B(Bank)调用subtractAmount()
 *          │                                │
 *          ├─ 获取account对象的锁            ├─ 尝试获取锁，被阻塞等待
 *          │  进入方法执行                   │  (无法进入方法)
 *          │                                │
 *          ├─ 执行完毕，释放锁               ├─ 获得锁，进入方法执行
 *          │                                │
 *
 * 【可重入性】synchronized是可重入的，同一线程可多次获取同一把锁
 */
public class Account {
    /**
     * 账号余额
     */
    private double balance;

    /**
     * 获取帐户余额
     * @return 余额
     */
    public double getBalance() {
        return balance;
    }

    /**
     * 设置帐户余额
     * @param balance 余额
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * 增加账号余额（同步方法）
     *
     * 使用synchronized修饰，保证多线程并发访问时的线程安全。
     * 同一时刻只有一个线程能执行此方法，防止竞态条件导致的更新丢失。
     *
     * 【方法执行流程】
     * 1. 获取当前账户对象的锁
     * 2. 读取当前余额到临时变量tmp
     * 3. 模拟业务处理延迟(Thread.sleep)
     * 4. 计算tmp + amount
     * 5. 将结果写回balance
     * 6. 释放锁
     *
     * @param amount 增加的金额
     */
    public synchronized void addAccount(double amount) {
        double tmp = balance;
        try {
            Thread.sleep(10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        tmp += amount;
        balance = tmp;
    }


    /**
     * 减少帐户余额（同步方法）
     *
     * 使用synchronized修饰，与addAccount()方法互斥执行。
     * 同一时刻只能有一个线程执行addAccount()或subtractAmount()之一。
     *
     * 【方法执行流程】
     * 1. 获取当前账户对象的锁
     * 2. 读取当前余额到临时变量tmp
     * 3. 模拟业务处理延迟(Thread.sleep)
     * 4. 计算tmp - amount
     * 5. 将结果写回balance
     * 6. 释放锁
     *
     * @param amount 减少的余额
     */
    public synchronized void subtractAmount(double amount) {
        double tmp = balance;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tmp -= amount;
        balance = tmp;
    }
}
