package com.ssitao.code.thread.code48.task;

import java.util.concurrent.atomic.AtomicLong;


/**
 * 账户类 - 使用 AtomicLong 实现线程安全
 *
 * 核心设计：
 * 1. 使用 AtomicLong 替代 long 类型存储余额
 *    - 保证所有操作的原子性
 *    - 无需 synchronized 关键字即可实现线程安全
 *
 * 2. AtomicLong 常用方法：
 *    - get(): 获取当前值
 *    - set(newValue): 设置新值
 *    - getAndAdd(delta): 先获取再增加，返回旧值
 *    - addAndGet(delta): 先增加再返回，返回新值
 *    - compareAndSet(expected, update): CAS 操作
 *
 * 3. 优势对比：
 *    - vs synchronized: 更轻量，无线程阻塞
 *    - vs volatile: 保证原子性，不只是可见性
 *    - vs long (基本类型): 保证操作的原子性
 */
public class Account {

    /**
     * 账户余额，使用 AtomicLong 保证线程安全
     * 原子变量可以确保在多线程环境下的读写一致性
     */
    private AtomicLong balance;

    /**
     * 默认构造函数，初始化原子变量
     */
    public Account() {
        balance = new AtomicLong();
    }

    /**
     * 获取账户余额
     *
     * @return 当前余额
     */
    public long getBalance() {
        return balance.get();
    }

    /**
     * 设置账户余额
     *
     * @param balance 新余额
     */
    public void setBalance(long balance) {
        this.balance.set(balance);
    }

    /**
     * 增加余额（存款操作）
     *
     * @param amount 增加的金额（正值）
     *
     * getAndAdd() 是原子操作：
     * 1. 读取当前值
     * 2. 增加指定金额
     * 3. 返回旧值
     * 整个过程是原子的，不会被其他线程中断
     */
    public void addAmount(long amount) {
        this.balance.getAndAdd(amount);
    }

    /**
     * 减少余额（取款操作）
     *
     * @param amount 减少的金额（正值）
     *
     * 注意：传入正值，但内部使用负数实现
     * getAndAdd(-amount) 同样是原子操作
     *
     * 潜在问题（此处未处理）：
     * - 如果余额不足，不应该允许取款
     * - 完整的实现需要检查余额是否足够
     * - 可以使用 compareAndSet 实现 "检查后执行" 的原子性
     */
    public void subtractAmount(long amount) {
        this.balance.getAndAdd(-amount);
    }

}
