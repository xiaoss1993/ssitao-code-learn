package com.ssitao.code.thread.code43.task;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 添加任务类：向并发队列中添加元素
 *
 * 每个 AddTask 实例会向队列添加10000个元素
 * 由于 ConcurrentLinkedDeque 是线程安全的，
 * 多个线程可以同时调用 add() 方法而无需额外同步
 *
 * ConcurrentLinkedDeque.add() 特点：
 * - 无返回值（返回 boolean，只是表示成功）
 * - 内部使用 CAS 实现无锁添加
 * - 不会抛出异常，会一直重试直到成功
 */
public class AddTask implements Runnable {

    /**
     * 目标队列引用
     */
    private ConcurrentLinkedDeque<String> list;

    /**
     * 构造函数
     *
     * @param list 目标并发队列
     */
    public AddTask(ConcurrentLinkedDeque<String> list) {
        this.list = list;
    }

    /**
     * 核心方法：向队列添加10000个元素
     *
     * 每个元素格式：线程名 + ": Element " + 序号
     * 例如："Thread-0: Element 0"
     *
     * 使用线程名可以方便地追踪是哪个线程添加的元素
     */
    @Override
    public void run() {
        // 获取当前执行线程的名称
        String name = Thread.currentThread().getName();

        // 循环添加10000个元素
        for (int i = 0; i < 10000; i++) {
            // add() 方法将元素添加到队列尾部
            // 由于 ConcurrentLinkedDeque 的线程安全性，
            // 这个操作在多线程环境下是安全的
            list.add(name + ": Element " + i);
        }
    }
}
