package com.learn.crazyjava.lesson11_thread;

import java.util.concurrent.*;

/**
 * 第11课：多线程 - 线程创建
 */
public class ThreadCreateDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 方式1：继承Thread
        Thread t1 = new Thread(() -> {
            System.out.println("方式1：Lambda创建线程");
        });
        t1.start();

        // 方式2：实现Runnable
        Runnable task = () -> System.out.println("方式2：Runnable线程");
        new Thread(task).start();

        // 方式3：Callable + Future
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum += i;
            }
            return sum;
        });
        System.out.println("计算结果：" + future.get());
        executor.shutdown();
    }
}
