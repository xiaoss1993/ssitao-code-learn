package com.ssitao.code.designpattern.threadpool.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池示例
 *
 * 线程池核心组件：
 * 1. BlockingQueue - 任务队列
 * 2. Worker Thread - 工作线程
 * 3. ThreadFactory - 线程工厂
 * 4. RejectedExecutionHandler - 拒绝策略
 */
public class CustomThreadPoolDemo {

    public static void main(String[] args) {
        System.out.println("=== 自定义线程池示例 ===\n");

        // 创建自定义线程池
        CustomThreadPool pool = new CustomThreadPool(3, 5);

        // 提交任务
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("任务" + taskId + " 开始，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep((long) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务" + taskId + " 完成");
            });
        }

        // 关闭线程池
        pool.shutdown();

        // 等待所有任务完成
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 自定义线程池
 */
class CustomThreadPool {
    // 核心线程数
    private final int corePoolSize;
    // 最大线程数
    private final int maxPoolSize;
    // 任务队列
    private final BlockingQueue<Runnable> workQueue;
    // 工作线程列表
    private final List<Worker> workers = new ArrayList<>();
    // 线程是否停止
    private volatile boolean isShutdown = false;
    // 任务计数器
    private final AtomicInteger taskCount = new AtomicInteger(0);

    public CustomThreadPool(int corePoolSize, int maxPoolSize) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.workQueue = new LinkedBlockingQueue<>();
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("线程池已关闭");
        }

        taskCount.incrementAndGet();

        // 如果当前线程数小于核心线程数，创建新线程
        if (workers.size() < corePoolSize) {
            Worker worker = new Worker(task);
            workers.add(worker);
            worker.start();
        } else {
            // 否则加入队列
            try {
                workQueue.put(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        isShutdown = true;
        for (Worker worker : workers) {
            worker.stopWorker();
        }
    }

    /**
     * 工作线程
     */
    class Worker extends Thread {
        private Runnable firstTask;
        private volatile boolean running = true;

        public Worker(Runnable firstTask) {
            super("Worker-" + workers.size());
            this.firstTask = firstTask;
        }

        public void run() {
            Runnable task = firstTask;

            // 循环获取任务执行
            while (running) {
                try {
                    if (task == null) {
                        // 从队列获取任务，超时等待
                        task = workQueue.poll(1, TimeUnit.SECONDS);
                    }

                    if (task != null) {
                        task.run();
                        task = null;
                    } else if (workers.size() > corePoolSize) {
                        // 如果线程数大于核心数，可以退出
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        public void stopWorker() {
            running = false;
        }
    }
}

/**
 * 拒绝策略示例
 */
class RejectedExecutionHandlerDemo {

    public static void main(String[] args) {
        System.out.println("=== 拒绝策略示例 ===\n");

        // 模拟任务队列满的情况
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(3);

        // 1. AbortPolicy - 抛出异常
        System.out.println("1. AbortPolicy（默认）:");
        try {
            for (int i = 0; i < 5; i++) {
                final int taskId = i;
                queue.put(() -> System.out.println("任务" + taskId));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2. CallerRunsPolicy - 由调用线程执行
        System.out.println("\n2. CallerRunsPolicy:");

        // 3. DiscardPolicy - 丢弃任务
        System.out.println("\n3. DiscardPolicy:");

        // 4. DiscardOldestPolicy - 丢弃最老的任务
        System.out.println("\n4. DiscardOldestPolicy:");
    }
}
