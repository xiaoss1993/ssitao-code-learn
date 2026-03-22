package com.ssitao.code.thread.code19.task;


/**
 * 打印任务类
 * 实现了Runnable接口，代表一个可执行的打印任务
 */
public class Job implements Runnable {
    /**
     * 打印队列引用，用于提交打印任务
     */
    private PrintQueue printQueue;

    /**
     * 构造函数
     *
     * @param printQueue 打印文档的队列
     */
    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    /**
     * 任务执行方法
     * 当线程启动时会调用此方法
     */
    @Override
    public void run() {
        // 输出当前线程即将打印文档
        System.out.printf("%s: Going to print a document\n", Thread.currentThread().getName());
        // 调用打印队列打印文档
        printQueue.printJob(new Object());
        // 打印完成后输出提示信息
        System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
    }
}
