package com.ssitao.code.thread.code36.core;


import com.ssitao.code.thread.code36.task.ExecutableTask;
import com.ssitao.code.thread.code36.task.ResultTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 代码36：使用 FutureTask 的 done() 钩子方法监控任务状态
 *
 * 【核心概念】
 * FutureTask 是 Future 的实现类
 * 它有一个重要的钩子方法 done()，在任务完成（正常/取消/异常）时自动调用
 *
 * 【done() 钩子方法】
 * - protected void done() {}
 * - 这是一个模板方法，允许子类在任务完成后执行特定操作
 * - 默认实现为空，需要子类重写
 *
 * 【本例设计】
 * ResultTask extends FutureTask<String>
 * 重写 done() 方法，在任务完成时打印日志（是被取消还是正常完成）
 *
 * 【应用场景】
 * - 任务完成时的回调处理
 * - 资源清理
 * - 进度更新
 * - 日志记录
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建缓存线程池 ==========
        ExecutorService executor = (ExecutorService) Executors.newCachedThreadPool();

        // ========== 2. 创建并提交5个 ResultTask ==========
        // ResultTask 是 FutureTask 的子类，管理 ExecutableTask 的执行
        // 设计模式：装饰器模式 - ResultTask 包装了 ExecutableTask
        //
        // 执行流程：
        // Main线程                 线程池线程
        //    │                         │
        // 创建ResultTask ──────────>│
        //    │                         │
        // submit(resultTask) ───────>│
        //    │                         │
        // 返回Future ──────┐          │
        //    │             │          │
        //    │         执行ExecutableTask
        //    │             │          │
        //    │         call()完成
        //    │             │          │
        //    │         done()被调用
        //    │             │          │
        ResultTask resultTasks[] = new ResultTask[5];
        for (int i = 0; i < 5; i++) {
            // 创建实际要执行的任务
            ExecutableTask executableTask = new ExecutableTask("Task " + i);
            // 包装成 ResultTask（FutureTask 的子类）
            resultTasks[i] = new ResultTask(executableTask);
            // 提交到线程池
            executor.submit(resultTasks[i]);
        }

        // ========== 3. 主线程休眠5秒 ==========
        // 在这5秒内，5个任务会并发执行
        // 每个任务耗时0-10秒（随机）
        // 有些任务会在5秒内完成，有些不会
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        // ========== 4. 取消所有任务 ==========
        // cancel(true) 的效果：
        // - 对于已完成的任务：无效
        // - 对于正在执行的任务：调用 interrupt() 中断
        // - 对于未开始的任务：不会再执行
        //
        // 【关键点】
        // 5秒后，有些任务已完成（取消无效）
        // 有些任务还在执行（会被中断）
        for (ResultTask resultTask : resultTasks) {
            resultTask.cancel(true);
        }

        // ========== 5. 只输出未被取消的任务结果 ==========
        // isCancelled() 判断任务是否被取消
        // 只有未被取消且已完成的任务才能获取结果
        for (ResultTask resultTask : resultTasks) {
            try {
                if (!resultTask.isCancelled()) {
                    // get() 获取任务返回值
                    // 只有正常完成且未被取消的任务才能获取结果
                    System.out.printf("%s\n", resultTask.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // ========== 6. 关闭线程池 ==========
        executor.shutdown();
    }
}
