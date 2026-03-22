package com.ssitao.code.thread.code14.task;

/**
 * 公司类，模拟向账户存款的并发操作
 *
 * 该类实现了Runnable接口，可以作为线程任务运行。
 * 职责：向共享的Account对象中反复存入一定金额。
 *
 * 注意：由于Account类的addAccount()方法使用了synchronized修饰，
 *       多个Company线程并发执行时能保证数据一致性。
 */
public class Company implements Runnable {

    /**
     * 一个帐户
     * 多个线程共享同一个账户对象
     */
    private Account account;

    /**
     * 构造函数
     * @param account 帐户对象
     */
    public Company(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        // 循环100次，每次向账户存入1000元
        // 总共将存入 100 * 1000 = 100000 元
        for (int i = 0; i < 100; i++) {
            // 调用账户的存款方法
            // 该方法使用synchronized修饰，保证线程安全
            this.account.addAccount(1000);
        }
    }
}
