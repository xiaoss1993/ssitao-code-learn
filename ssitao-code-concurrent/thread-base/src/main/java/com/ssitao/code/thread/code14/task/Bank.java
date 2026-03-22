package com.ssitao.code.thread.code14.task;

/**
 * 银行类，模拟从账户取款的并发操作
 *
 * 该类实现了Runnable接口，可以作为线程任务运行。
 * 职责：向共享的Account对象中反复取出一定金额。
 *
 * 注意：由于Account类的subtractAmount()方法使用了synchronized修饰，
 *       多个Bank线程并发执行时能保证数据一致性。
 */
public class Bank implements Runnable {
    /**
     * 一个帐户
     * 多个线程共享同一个账户对象
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
        // 循环100次，每次从账户取出1000元
        // 总共将取出 100 * 1000 = 100000 元
        for (int i = 0; i < 100; i++) {
            // 调用账户的取款方法
            // 该方法使用synchronized修饰，保证线程安全
            this.account.subtractAmount(1000);
        }
    }
}
