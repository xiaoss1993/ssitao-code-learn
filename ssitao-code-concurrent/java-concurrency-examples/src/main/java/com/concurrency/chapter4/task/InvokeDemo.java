package com.concurrency.chapter4.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * invokeAny和invokeAll示例
 */
public class InvokeDemo {

    public static void demo() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Callable<String>> tasks = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            tasks.add(() -> {
                int sleepTime = (int) (Math.random() * 1000);
                Thread.sleep(sleepTime);
                return "任务" + taskId + " 完成 (耗时" + sleepTime + "ms)";
            });
        }

        System.out.println("=== invokeAny: 获取第一个完成的结果 ===");
        String firstResult = executor.invokeAny(tasks);
        System.out.println("最先完成的结果: " + firstResult);

        System.out.println("\n=== invokeAll: 获取所有结果 ===");
        List<Callable<String>> tasks2 = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            tasks2.add(() -> {
                Thread.sleep(500);
                return "任务" + taskId;
            });
        }

        List<Future<String>> futures = executor.invokeAll(tasks2);
        for (Future<String> f : futures) {
            System.out.println("invokeAll结果: " + f.get());
        }

        executor.shutdown();
    }
}
