package com.ssitao.code.thread.code13.core;

import com.ssitao.code.thread.code13.task.Account;
import com.ssitao.code.thread.code13.task.Bank;
import com.ssitao.code.thread.code13.task.Company;

/**
 * 使用synchronized实现同步方法-问题
 *
 * 本类演示了多线程并发访问共享资源时的竞态条件问题。
 * 场景：Company（公司）向账户存款，Bank（银行）从账户取款，
 *       由于没有同步控制，最终余额可能不正确。
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个帐户对象
        Account account = new Account();
        // 帐户初始值为1000
        account.setBalance(1000);

        // 创建一个公司对象，并且让公司对象在一个线程中运行
        // 公司任务：向账户添加1000元，执行100次，总共增加100000元
        Company company = new Company(account);
        Thread companyThread = new Thread(company);

        // 创建一个银行对象，并且让银行对象在一个线程中运行
        // 银行任务：从账户扣除1000元，执行100次，总共减少100000元
        Bank bank = new Bank(account);
        Thread bankThread = new Thread(bank);

        // 输出初始的余额
        System.out.printf("Account : Initial Balance: %f\n", account.getBalance());

        // 启动公司和银行两个线程
        companyThread.start();
        bankThread.start();

        try {
            // 等待两个线程的完成
            // join()方法会阻塞主线程，直到对应的线程执行完毕
            companyThread.join();
            bankThread.join();
            // 输出最后的余额
            // 理论上：1000 + 100000 - 100000 = 1000
            // 但由于竞态条件，实际结果通常不等于1000
            System.out.printf("Account : Final Balance: %f\n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
