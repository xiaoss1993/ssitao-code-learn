package com.concurrency.chapter2.task;

import java.util.concurrent.TimeUnit;

/**
 * synchronized同步方法示例
 */
public class SynchronizedMethodDemo {

    public static void demo() {
        Account account = new Account(1000);

        // 创建多个线程同时操作同一个账户
        Thread t1 = new Thread(() -> account.deposit(500), "Deposit-Thread");
        Thread t2 = new Thread(() -> account.withdraw(300), "Withdraw-Thread");
        Thread t3 = new Thread(() -> account.deposit(200), "Deposit2-Thread");

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("最终余额: " + account.getBalance());
    }

    /**
     * 线程不安全的账户类
     */
    static class UnsafeAccount {
        private double balance;

        public UnsafeAccount(double balance) {
            this.balance = balance;
        }

        public void deposit(double amount) {
            System.out.println(Thread.currentThread().getName() + " - 存款前余额: " + balance);
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " - 存款 " + amount + ", 最终余额: " + balance);
        }

        public void withdraw(double amount) {
            System.out.println(Thread.currentThread().getName() + " - 取款前余额: " + balance);
            if (balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " - 取款 " + amount + ", 最终余额: " + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + " - 余额不足!");
            }
        }

        public double getBalance() {
            return balance;
        }
    }

    /**
     * 线程安全的账户类 (使用synchronized方法)
     */
    static class Account {
        private double balance;

        public Account(double balance) {
            this.balance = balance;
        }

        // synchronized保证原子性
        public synchronized void deposit(double amount) {
            System.out.println(Thread.currentThread().getName() + " - 存款前余额: " + balance);
            balance += amount;
            System.out.println(Thread.currentThread().getName() + " - 存款 " + amount + ", 最终余额: " + balance);
        }

        public synchronized void withdraw(double amount) {
            System.out.println(Thread.currentThread().getName() + " - 取款前余额: " + balance);
            if (balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " - 取款 " + amount + ", 最终余额: " + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + " - 余额不足!");
            }
        }

        public synchronized double getBalance() {
            return balance;
        }
    }
}
