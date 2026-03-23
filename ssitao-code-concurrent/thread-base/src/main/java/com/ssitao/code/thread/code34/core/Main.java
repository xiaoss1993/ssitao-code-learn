package com.ssitao.code.thread.code34.core;


import com.ssitao.code.thread.code34.task.Task;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 代码34：使用 scheduleAtFixedRate() 执行周期性定时任务
 *
 * 【核心概念】
 * scheduleAtFixedRate() 是 ScheduledExecutorService 的方法
 * 用于以固定频率重复执行任务
 *
 * 【方法签名】
 * scheduleAtFixedRate(Runnable, initialDelay, period, TimeUnit)
 * - initialDelay: 首次执行的延迟时间
 * - period: 两次执行之间的间隔周期
 *
 * 【scheduleAtFixedRate vs scheduleWithFixedDelay】
 *
 * 1. scheduleAtFixedRate() - 固定频率
 *    - 任务开始时间间隔固定
 *    - 如果某次任务执行时间超过周期，下一次任务会立即执行（不会等待）
 *    - 适合：按固定时间间隔执行的任务（如心跳检测）
 *
 * 2. scheduleWithFixedDelay() - 固定延迟
 *    - 上次任务结束后，等待固定延迟再执行下一次
 *    - 任务执行时间不影响下一次开始时间
 *    - 适合：任务耗时不确定的场景（如数据同步）
 *
 * 【应用场景】
 * - 定期数据备份
 * - 心跳检测
 * - 定期刷新缓存
 * - 定时报表生成
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建定时线程池 ==========
        // newScheduledThreadPool(1) 创建核心线程数为1的定时线程池
        // 参数1表示池中保持的最小线程数（即使空闲也不会回收）
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        System.out.printf("Main: Starting at: %s\n", new Date());

        // ========== 2. 提交周期性任务 ==========
        // scheduleAtFixedRate() 参数说明：
        // - task: 要执行的任务
        // - initialDelay: 首次执行延迟 = 1秒（启动1秒后开始第一次）
        // - period: 周期 = 2秒（每2秒执行一次）
        // - TimeUnit.SECONDS: 时间单位为秒
        //
        // 【执行时间线】（假设任务执行时间可忽略）
        // 1秒后：第1次执行
        // 3秒后：第2次执行
        // 5秒后：第3次执行
        // ...以此类推
        Task task = new Task("Task");
        ScheduledFuture<?> result = executor.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);

        // ========== 3. 监控任务的下次执行延迟 ==========
        // getDelay(TimeUnit) 返回距离下次执行还剩多少时间（毫秒）
        // 这是一个非阻塞查询，用于了解任务的调度状态
        //
        // 【执行流程】
        // 循环10次，每次间隔500毫秒
        // 期间不断打印下次任务执行的剩余延迟时间
        for (int i = 0; i < 10; i++) {
            // 获取距离下次执行的时间（毫秒）
            System.out.printf("Main: Delay: %d\n", result.getDelay(TimeUnit.MILLISECONDS));
            try {
                // 休眠500毫秒后再次检查
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // ========== 4. 关闭定时线程池 ==========
        // shutdown() 行为：
        // - 停止接收新任务
        // - 已提交的周期性任务会继续执行（直到所有任务完成）
        // - 与 shutdownNow() 不同，shutdown() 不会中断正在执行的任务
        executor.shutdown();
        System.out.printf("Main: No more tasks at: %s\n", new Date());

        // ========== 5. 等待5秒验证关闭效果 ==========
        // 验证在执行器关闭后，周期性任务确实停止了
        // 如果关闭成功，这5秒内不应该有任何 Task 输出
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ========== 6. 输出最终完成时间 ==========
        System.out.printf("Main: Finished at: %s\n", new Date());
    }
}
