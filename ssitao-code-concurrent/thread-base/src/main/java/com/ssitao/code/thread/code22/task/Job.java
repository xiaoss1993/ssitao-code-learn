package com.ssitao.code.thread.code22.task;

public class Job implements Runnable {
    /**
     * 打印队列对象
     */
    private PrintQueue printQueue;

    /**
     * 构造函数，初始化打印队列对象
     *
     * @param printQueue 打印队列对象
     */
    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    /**
     * 核心方法，向打印队列中发送打印任务，并且等待它完成
     *
     * 执行流程：
     * 1. 打印开始消息，告知用户该线程即将提交打印任务
     * 2. 调用printQueue.printJob()提交打印请求
     *    - 此时该线程可能会被阻塞（如果所有打印机都在使用中）
     *    - 直到获得一台打印机并完成打印后才会返回
     * 3. 打印完成消息
     */
    @Override
    public void run() {
        System.out.printf("%s: Going to print a job\n", Thread.currentThread().getName());
        // 调用打印队列的printJob方法
        // 注意：这里传入的Object对象实际上并未使用，仅作为示例参数
        printQueue.printJob(new Object());
        System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
    }
}
