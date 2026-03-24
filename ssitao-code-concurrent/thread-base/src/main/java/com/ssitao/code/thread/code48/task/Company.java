package com.ssitao.code.thread.code48.task;

/**
 * 公司类 - 存款操作（生产者）
 *
 * 模拟公司向账户存款的操作
 * 在独立线程中运行，执行10次存款，每次存1000
 *
 * 设计目的：
 * - 演示 AtomicLong 的线程安全性
 * - 与 Bank 形成"一存一取"的并发操作
 *
 * 存款序列（理论上）：
 * 1000, 1000, 1000, ..., 1000 （共10次）
 * 总计存入: 10 × 1000 = 10000
 *
 * 注意：存款操作和取款操作是交错的，顺序不确定
 * 但由于 Account 使用 AtomicLong，所有操作都是原子的
 */
public class Company implements Runnable {

    /**
     * 共享的账户对象
     * 多个线程会同时访问此账户
     */
    private Account account;

    /**
     * 构造函数，初始化账户
     *
     * @param account 共享的账户对象
     */
    public Company(Account account) {
        this.account = account;
    }

    /**
     * 存款任务执行入口
     *
     * 核心逻辑：
     * 循环10次，每次向账户增加1000
     * 使用 account.addAmount(1000) 是原子操作
     */
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            // 每次存款 1000
            account.addAmount(1000);
        }
    }

}
