package com.ssitao.code.thread.code48.core;

import com.ssitao.code.thread.code48.task.Account;
import com.ssitao.code.thread.code48.task.Bank;
import com.ssitao.code.thread.code48.task.Company;

/**
 * 主程序 - AtomicLong 原子变量并发示例
 *
 * 演示使用 AtomicLong 实现线程安全的银行账户操作
 * Company（公司）向账户存钱，Bank（银行）从账户取钱
 *
 * 核心概念：
 * 1. AtomicLong - 原子 long 类型变量
 *    - 保证 long 类型操作的原子性
 *    - 底层使用 CAS（Compare-And-Swap）算法实现
 *    - 比 synchronized 更轻量级，无锁设计
 *
 * 2. 线程安全的并发操作：
 *    - Company.addAmount(): 存款操作
 *    - Bank.subtractAmount(): 取款操作
 *    - 两者并发执行，不会产生数据竞争
 *
 * 3. 预期结果：
 *    - 初始余额: 1000
 *    - Company 存 10 次，每次 1000 → 共存入 10000
 *    - Bank 取 10 次，每次 1000 → 共取出 10000
 *    - 最终余额: 1000（与初始值相同）
 *
 * 注意事项：
 * - 虽然操作是原子的，但多个操作的组合（如检查-执行）仍需额外同步
 * - 这里的 addAmount 和 subtractAmount 都是单一原子操作，所以是安全的
 */
public class Main {

    public static void main(String[] args) {

        // 创建一个账户对象
        Account account = new Account();
        // 初始化账户余额为 1000
        account.setBalance(1000);

        // 创建一个公司线程（存款方）
        // Company 每秒存 1000，共存 10 次 = 10000
        Company company = new Company(account);
        Thread companyThread = new Thread(company);

        // 创建一个银行线程（取款方）
        // Bank 每秒取 1000，共取 10 次 = 10000
        Bank bank = new Bank(account);
        Thread bankThread = new Thread(bank);

        // 输出账户初始余额
        System.out.printf("Account : Initial Balance: %d\n", account.getBalance());

        // 启动两个并发线程
        companyThread.start();
        bankThread.start();

        try {
            // 等待两个线程执行完成
            // join() 会阻塞主线程，直到对应线程结束
            companyThread.join();
            bankThread.join();

            // 两个线程都完成后，输出最终余额
            // 理论上应该是 1000（1000 + 10000 - 10000）
            System.out.printf("Account : Final Balance: %d\n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
