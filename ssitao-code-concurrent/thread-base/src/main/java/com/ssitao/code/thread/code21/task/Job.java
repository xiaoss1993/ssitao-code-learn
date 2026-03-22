package com.ssitao.code.thread.code21.task;

/**
 * 打印任务类
 * 实现了Runnable接口，代表一个可执行的打印任务
 */
public class Job implements Runnable {
    /**
     * 打印队列对象（共享资源）
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
     * 线程执行方法
     * 当线程获得CPU时间片时会执行此方法
     * 向打印队列发送打印任务，并等待打印完成
     */
    @Override
    public void run() {
        // 打印线程即将打印
        System.out.printf("%s: Going to print a job\n", Thread.currentThread().getName());
        // 调用打印队列的printJob方法，这里会尝试获取信号量
        // 如果信号量已被占用，当前线程会被阻塞，等待信号量释放
        printQueue.printJob(new Object());
        // 打印完成后打印此消息
        System.out.printf("%s: The document has been printed\n", Thread.currentThread().getName());
    }
}
