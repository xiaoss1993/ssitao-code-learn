package com.concurrency.chapter5.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * SumTask示例 - Fork/Join分治求和
 */
public class SumTaskDemo {

    public static void demo() throws Exception {
        long[] array = new long[100];
        for (int i = 0; i < 100; i++) {
            array[i] = i + 1;
        }

        ForkJoinPool pool = new ForkJoinPool();
        SumTask task = new SumTask(array, 0, array.length);

        long startTime = System.currentTimeMillis();
        Long result = pool.invoke(task);
        long endTime = System.currentTimeMillis();

        System.out.println("数组长度: " + array.length);
        System.out.println("计算结果: " + result);
        System.out.println("期望结果: " + (100 * 101 / 2)); // 5050
        System.out.println("耗时: " + (endTime - startTime) + "ms");

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);
    }
}
