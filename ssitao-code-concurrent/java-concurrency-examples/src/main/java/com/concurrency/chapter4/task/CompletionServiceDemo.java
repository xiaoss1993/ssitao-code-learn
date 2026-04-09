package com.concurrency.chapter4.task;

import java.util.concurrent.*;

/**
 * CompletionService示例 - 分离任务启动与结果处理
 */
public class CompletionServiceDemo {

    public static void demo() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        System.out.println("提交5个搜索任务...\n");

        // 提交任务
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            completionService.submit(() -> {
                int sleepTime = (int) (Math.random() * 2000);
                Thread.sleep(sleepTime);
                return "搜索结果" + taskId + " (耗时" + sleepTime + "ms)";
            });
        }

        // 获取结果（按完成顺序）
        for (int i = 0; i < 5; i++) {
            Future<String> future = completionService.take(); // 阻塞直到有结果
            try {
                String result = future.get();
                System.out.println("收到结果: " + result);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }
}
