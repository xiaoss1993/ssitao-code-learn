package com.ssitao.code.thread.code17.task;

/**
 * 打印任务类 - 实现了Runnable接口，代表一个可执行的打印任务
 *
 * 演示ReentrantLock的"可重入"特性：
 * - 同一个线程可以多次获取同一把锁（lock()可以嵌套调用）
 * - 锁的持有计数会递增，释放时计数递减
 */
public class Job implements Runnable {
    /**
     * 打印队列引用（共享资源）
     */
    private PrintQueue printQueue;

    /**
     * 构造函数
     *
     * @param printQueue 打印队列实例
     */
    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        // 输出线程即将打印的提示信息
        System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
        // 调用打印队列打印文档（内部会获取锁）
        printQueue.printJob(new Object());
        // 打印完成后的提示信息
        System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
    }
}
