package com.ssitao.code.designpattern.threadpool.biz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 业务场景中的线程池应用
 *
 * 典型应用：
 * 1. 订单处理 - 异步处理订单
 * 2. 报表生成 - 并行生成多张报表
 * 3. 数据同步 - 多线程数据抓取
 * 4. 消息推送 - 批量推送消息
 * 5. 文件处理 - 并行处理多个文件
 */
public class BizThreadPoolDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景线程池 ===\n");

        // 1. 订单处理
        System.out.println("1. 订单处理");
        orderProcessDemo();

        // 2. 报表生成
        System.out.println("\n2. 报表生成");
        reportGenerateDemo();

        // 3. 数据抓取
        System.out.println("\n3. 数据抓取");
        dataFetchDemo();
    }

    /**
     * 订单处理示例
     */
    private static void orderProcessDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 模拟处理10个订单
        for (int i = 1; i <= 10; i++) {
            final int orderId = i;
            executor.submit(() -> {
                try {
                    System.out.println("处理订单: ORD-" + orderId);
                    Thread.sleep(200); // 模拟处理时间

                    // 模拟90%成功率
                    if (Math.random() > 0.1) {
                        successCount.incrementAndGet();
                        System.out.println("订单 ORD-" + orderId + " 处理成功");
                    } else {
                        failCount.incrementAndGet();
                        System.out.println("订单 ORD-" + orderId + " 处理失败");
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("处理完成: 成功=" + successCount.get() + ", 失败=" + failCount.get());
    }

    /**
     * 报表生成示例
     */
    private static void reportGenerateDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 报表类型
        List<String> reportTypes = new ArrayList<>();
        reportTypes.add("销售报表");
        reportTypes.add("库存报表");
        reportTypes.add("用户报表");

        // 并行生成多张报表
        for (String reportType : reportTypes) {
            executor.submit(() -> {
                System.out.println("开始生成: " + reportType);
                try {
                    Thread.sleep(1000 + new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("完成生成: " + reportType);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据抓取示例
     */
    private static void dataFetchDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 模拟多个数据源
        String[] dataSources = {"数据源A", "数据源B", "数据源C", "数据源D"};

        for (String source : dataSources) {
            executor.submit(() -> {
                System.out.println("开始抓取: " + source);
                try {
                    Thread.sleep(500 + new Random().nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("抓取完成: " + source);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 线程池监控示例
 */
class ThreadPoolMonitor {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 监控线程池状态
        Thread monitor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }
}

/**
 * 线程池参数调优建议
 *
 * 1. 线程数计算
 * - CPU密集型: N_cpu + 1
 * - IO密集型: N_cpu * 2
 * - 混合型: N_cpu * (1 + W/C)
 *
 * 2. 队列选择
 * - 无界队列: 适合任务量可控
 * - 有界队列: 防止内存溢出
 * - 优先级队列: 重要任务优先
 *
 * 3. 拒绝策略
 * - AbortPolicy: 抛异常（默认）
 * - CallerRunsPolicy: 调用方执行
 * - DiscardPolicy: 丢弃
 * - DiscardOldestPolicy: 丢弃最老
 *
 * 4. 常用配置
 * - 核心线程数: CPU核数
 * - 最大线程数: CPU核数 * 2
 * - 队列大小: 100-1000
 * - 空闲存活时间: 60秒
 */
