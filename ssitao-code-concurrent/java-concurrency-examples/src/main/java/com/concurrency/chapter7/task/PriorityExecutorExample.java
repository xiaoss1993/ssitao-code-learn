package com.concurrency.chapter7.task;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于优先级的Executor示例
 */
public class PriorityExecutorExample {

    public static void demo() throws InterruptedException {
        ExecutorService executor = new PriorityThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS);

        System.out.println("提交优先级任务...\n");

        // 提交不同优先级的任务
        executor.submit(new PriorityTask(3, "低优先级任务"));
        executor.submit(new PriorityTask(1, "高优先级任务"));
        executor.submit(new PriorityTask(2, "中优先级任务"));
        executor.submit(new PriorityTask(1, "紧急任务"));
        executor.submit(new PriorityTask(3, "普通任务"));

        Thread.sleep(1000);

        executor.shutdown();
    }

    static class PriorityTask implements Runnable, Comparable<PriorityTask> {
        private final int priority;
        private final String name;

        PriorityTask(int priority, String name) {
            this.priority = priority;
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println("执行: " + name + " (优先级: " + priority + ")");
        }

        @Override
        public int compareTo(PriorityTask o) {
            return Integer.compare(this.priority, o.priority);
        }
    }

    static class PriorityThreadPoolExecutor extends ThreadPoolExecutor {
        public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                          long keepAliveTime, TimeUnit unit) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                    new PriorityBlockingQueue<>());
        }

        @Override
        public <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
            if (runnable instanceof PriorityTask) {
                return new PriorityFutureTask<>((PriorityTask) runnable, value);
            }
            return super.newTaskFor(runnable, value);
        }
    }

    static class PriorityFutureTask<T> extends FutureTask<T> implements Comparable<PriorityFutureTask<T>> {
        private final PriorityTask task;

        public PriorityFutureTask(PriorityTask task, T result) {
            super(task, result);
            this.task = task;
        }

        @Override
        public int compareTo(PriorityFutureTask<T> o) {
            return task.compareTo(o.task);
        }
    }
}
