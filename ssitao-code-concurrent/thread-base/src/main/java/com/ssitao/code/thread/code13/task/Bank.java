package com.ssitao.code.thread.code13.task;

/**
 * 银行类，模拟从账户取款的并发操作
 *
 * 该类实现了Runnable接口，可以作为线程任务运行。
 * 职责：从共享的Account对象中反复扣除一定金额。
 *
 * 注意：由于Account类的subtractAmount()方法存在线程安全问题，
 *       多个Bank线程并发执行时可能导致数据不一致。
 */
public class Bank implements Runnable {
    /**
     * 一个帐户
     * 多个线程共享同一个账户对象，这是产生竞态条件的必要条件之一
     */
    private Account account;

    /**
     * 构造函数
     *
     * @param account 银行帐户
     */
    public Bank(Account account) {
        this.account = account;
    }


    @Override
    public void run() {
        // 循环100次，每次从账户扣除1000元
        // 总共将扣除 100 * 1000 = 100000 元
        for (int i = 0; i < 100; i++) {
            // 调用账户的扣除方法
            // 这里没有同步控制，多个线程可能同时调用该方法
            this.account.subtractAmount(1000);
        }
    }
}
