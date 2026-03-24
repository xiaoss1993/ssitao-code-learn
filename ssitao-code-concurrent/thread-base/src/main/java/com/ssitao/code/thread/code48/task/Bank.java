package com.ssitao.code.thread.code48.task;

/**
 * 银行类 - 取款操作（消费者）
 *
 * 模拟银行从账户取款的操作
 * 在独立线程中运行，执行10次取款，每次取1000
 *
 * 设计目的：
 * - 演示 AtomicLong 的线程安全性
 * - 与 Company 形成"一存一取"的并发操作
 *
 * 取款序列（理论上）：
 * 1000, 1000, 1000, ..., 1000 （共10次）
 * 总计取出: 10 × 1000 = 10000
 *
 * 注意：取款操作和存款操作是交错的，顺序不确定
 * 但由于 Account 使用 AtomicLong，所有操作都是原子的
 */
public class Bank implements Runnable {

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
    public Bank(Account account) {
        this.account = account;
    }


    /**
     * 取款任务执行入口
     *
     * 核心逻辑：
     * 循环10次，每次从账户减少1000
     * 使用 account.subtractAmount(1000) 是原子操作
     *
     * 注意：此实现不检查余额是否足够
     * 如果余额可能为负数，应在 Account 类中添加检查逻辑
     */
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            // 每次取款 1000
            account.subtractAmount(1000);
        }
    }

}
