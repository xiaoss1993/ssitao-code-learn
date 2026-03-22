package com.ssitao.code.thread.code14.core;

import com.ssitao.code.thread.code14.task.Account;
import com.ssitao.code.thread.code14.task.Bank;
import com.ssitao.code.thread.code14.task.Company;

/**
 * Code14: 使用synchronized解决竞态条件问题
 *
 * 本示例演示如何通过synchronized关键字解决code13中的线程安全问题。
 * Company线程向账户存款，Bank线程从账户取款，两者并发执行时，
 * 由于使用了synchronized修饰关键方法，确保了数据一致性。
 *
 * 预期结果：初始余额1000 + 存款100000 - 取款100000 = 1000
 * 解决后：无论运行多少次，最终余额都应该是1000
 */
public class Main {
    public static void main(String[] args) {
        // 创建一个帐户对象
        Account account = new Account();
        // 帐户初始值为1000
        account.setBalance(1000);

        // 创建一个公司对象，并且让公司对象在一个线程中运行
        Company company = new Company(account);
        Thread companyThread = new Thread(company);

        // 创建一个银行对象，并且让银行对象在一个线程中运行
        Bank bank = new Bank(account);
        Thread bankThread = new Thread(bank);

        // 输出初始的余额
        System.out.printf("Account : Initial Balance: %f\n", account.getBalance());

        // 启动公司和银行两个线程
        companyThread.start();
        bankThread.start();

        try {
            // 等待两个线程的完成
            companyThread.join();
            bankThread.join();
            // 输出最后的余额
            System.out.printf("Account : Final Balance: %f\n", account.getBalance());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}