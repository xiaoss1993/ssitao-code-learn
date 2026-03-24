package com.ssitao.code.thread.code50.core;

import com.ssitao.code.thread.code50.executor.MyExecutor;
import com.ssitao.code.thread.code50.task.SleepTwoSecondsTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 主程序 - ThreadPoolExecutor 自定义执行器与 Future 异步编程示例
 *
 * 演示内容：
 * 1. 自定义线程执行器（扩展 ThreadPoolExecutor）
 * 2. 使用 Future 实现异步任务提交与结果获取
 * 3. 执行器的生命周期管理（shutdown、awaitTermination）
 *
 * 核心概念：
 * 1. ThreadPoolExecutor 执行器框架：
 *    - 核心线程数: 2（池中始终保持的最小线程数）
 *    - 最大线程数: 4（池中允许的最大线程数）
 *    - 存活时间: 1000ms（空闲线程等待新任务的超时时间）
 *    - 任务队列: LinkedBlockingDeque（无界队列）
 *
 * 2. Future 异步编程：
 *    - submit() 立即返回 Future 对象，不阻塞
 *    - get() 阻塞等待任务完成并获取结果
 *    - 支持超时获取，避免无限等待
 *
 * 3. 执行器生命周期：
 *    - shutdown(): 平缓关闭，不再接受新任务，但会完成已提交的任务
 *    - awaitTermination(): 阻塞等待执行器完全终止
 *
 * 执行流程：
 * 1. 提交10个任务（每个任务执行2秒）
 * 2. 由于核心线程2个，最大4个，队列无界
 * 3. 前2个任务立即被核心线程执行
 * 4. 后续8个任务进入队列等待
 * 5. 核心线程空闲时从队列取任务执行
 *
 * 注意：get() 方法在任务完成前会阻塞调用线程
 */
public class Main {
    public static void main(String[] args) {

        // 创建自定义执行器
        // 参数说明：
        // - 核心线程数: 2（最小保持的线程）
        // - 最大线程数: 4（最多创建到4个线程）
        // - 存活时间: 1000ms（空闲线程存活时间）
        // - 时间单位: 毫秒
        // - 任务队列: LinkedBlockingDeque（无界队列）
        MyExecutor myExecutor = new MyExecutor(
            2,                          // corePoolSize: 2
            4,                          // maximumPoolSize: 4
            1000,                       // keepAliveTime: 1000ms
            TimeUnit.MILLISECONDS,       // unit: 毫秒
            new LinkedBlockingDeque<>()  // workQueue: 无界队列
        );

        // 存储异步任务结果的 Future 列表
        List<Future<String>> results = new ArrayList<>();

        // 提交10个任务到执行器
        // submit() 立即返回 Future，不阻塞
        // 任务会被调度到线程池执行
        for (int i = 0; i < 10; i++) {
            SleepTwoSecondsTask task = new SleepTwoSecondsTask();
            Future<String> result = myExecutor.submit(task);
            results.add(result);
        }

        // 获取前5个任务的执行结果
        // get() 方法会阻塞，直到对应任务完成
        for (int i = 0; i < 5; i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: Result for Task %d : %s\n", i, result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 关闭执行器（平缓关闭）
        // 不再接受新任务，但会完成已提交的任务
        // 关闭后提交的任务会被拒绝
        myExecutor.shutdown();

        // 获取后5个任务的执行结果
        // 注意：此时执行器已关闭，但已提交的任务会继续执行
        for (int i = 5; i < 10; i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: Result for Task %d : %s\n", i, result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 等待执行器完全终止
        // 参数：最大等待时间 1 天
        // 返回：true 表示已终止，false 表示超时
        try {
            myExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 程序结束
        System.out.printf("Main: End of the program.\n");
    }
}
