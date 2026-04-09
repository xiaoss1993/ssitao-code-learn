package com.concurrency.chapter1.task;

/**
 * 简单的Runnable任务
 */
public class RunnableTask implements Runnable {

    private String name;

    public RunnableTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Task [" + name + "] running in thread: " + Thread.currentThread().getName());
        for (int i = 0; i < 3; i++) {
            System.out.println("  [" + name + "] iteration " + i + " - Thread: " + Thread.currentThread().getName());
        }
        System.out.println("Task [" + name + "] completed");
    }
}
