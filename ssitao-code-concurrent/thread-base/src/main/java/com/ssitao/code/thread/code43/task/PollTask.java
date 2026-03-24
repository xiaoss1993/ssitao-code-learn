package com.ssitao.code.thread.code43.task;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 删除任务类：从并发队列两端删除元素
 *
 * 每个 PollTask 实例会从队列删除5000对元素（共10000个）
 * 使用 pollFirst() 和 pollLast() 从两端同时删除
 *
 * pollFirst() / pollLast() 特点：
 * - 返回并移除队列头部/尾部的元素
 * - 如果队列为空，返回 null（不会抛出异常）
 * - 线程安全，使用 CAS 实现
 *
 * 使用两端删除的好处：
 * - 减少头部/尾部的竞争
 * - 提高并发效率
 */
public class PollTask implements Runnable {

    /**
     * 源队列引用
     */
    private ConcurrentLinkedDeque<String> list;

    /**
     * 构造函数
     *
     * @param list 源并发队列
     */
    public PollTask(ConcurrentLinkedDeque<String> list) {
        this.list = list;
    }

    /**
     * 核心方法：从队列两端删除5000对元素
     *
     * 每次循环：
     * - pollFirst(): 移除并返回队列头部元素
     * - pollLast(): 移除并返回队列尾部元素
     *
     * 5000次循环 x 2个元素 = 10000个元素
     *
     * 注意：如果队列为空，pollFirst()/pollLast() 会返回 null
     * 这可能导致实际删除的元素少于预期
     */
    @Override
    public void run() {
        for (int i = 0; i < 5000; i++) {
            // 从队列头部删除元素
            // 如果队列为空，返回 null
            list.pollFirst();

            // 从队列尾部删除元素
            // 如果队列为空，返回 null
            list.pollLast();
        }
    }
}
