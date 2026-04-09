package com.concurrency.chapter5.task;

import java.util.concurrent.RecursiveTask;

/**
 * 求和任务 - 使用RecursiveTask
 */
public class SumTask extends RecursiveTask<Long> {

    private static final int THRESHOLD = 10;
    private final long[] array;
    private final int start;
    private final int end;

    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            // 小于阈值，直接计算
            return sumDirectly();
        }

        // 分成两半，递归计算
        int middle = start + length / 2;
        SumTask leftTask = new SumTask(array, start, middle);
        SumTask rightTask = new SumTask(array, middle, end);

        // 分叉执行子任务
        leftTask.fork();
        rightTask.fork();

        // 合并结果
        return leftTask.join() + rightTask.join();
    }

    private long sumDirectly() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }
}
