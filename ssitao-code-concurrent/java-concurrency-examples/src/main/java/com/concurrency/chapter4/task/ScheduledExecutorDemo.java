package com.concurrency.chapter4.task;

import java.util.concurrent.*;

/**
 * ScheduledExecutorService定时执行示例
 */
public class ScheduledExecutorDemo {

    public static void demo() throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        System.out.println("提交定时任务...\n");

        // 延迟1秒后执行一次
        ScheduledFuture<?> future1 = scheduler.schedule(() -> {
            System.out.println("延迟1秒后执行的任务");
        }, 1, TimeUnit.SECONDS);

        // 延迟2秒后开始执行，之后每1秒执行一次
        ScheduledFuture<?> future2 = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("定时任务执行 - " + System.currentTimeMillis());
        }, 2, 1, TimeUnit.SECONDS);

        // 等待一段时间后取消任务
        Thread.sleep(5000);

        future2.cancel(false);
        scheduler.shutdown();

        if (scheduler.awaitTermination(3, TimeUnit.SECONDS)) {
            System.out.println("\n定时执行器已关闭");
        }
    }
}
