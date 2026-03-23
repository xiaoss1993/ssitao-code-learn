package com.ssitao.code.thread.code32.core;

import com.ssitao.code.thread.code32.task.Result;
import com.ssitao.code.thread.code32.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 代码32：使用 invokeAll() 等待所有任务完成
 *
 * 【核心概念】
 * invokeAll() 方法：提交一组任务给线程池，等待所有任务完成
 * - 返回所有任务的 Future 对象列表
 * - 任务按提交顺序执行（不一定同时执行）
 * - 所有任务都会完成，不会中途取消
 *
 * 【与 invokeAny() 的区别】
 * - invokeAny(): 只要任意一个成功，立即返回（第一个成功者获胜）
 * - invokeAll(): 等待所有任务完成（全部成功才算成功）
 *
 * 【应用场景】
 * - 需要收集所有任务结果的场景
 * - 各任务之间相互独立，必须全部完成才有用
 * - 例如：批量处理数据、并行计算后汇总结果
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 创建缓存线程池 ==========
        // newCachedThreadPool() 特点：
        // - 会重用空闲线程
        // - 可以按需创建新线程（最多 Integer.MAX_VALUE）
        ExecutorService executor = Executors.newCachedThreadPool();

        // ========== 2. 创建任务列表 ==========
        // 注意：invokeAll() 接收 List<Callable<T>>，返回 List<Future<T>>
        List<Task> taskList = new ArrayList<Task>();

        // ========== 3. 添加3个任务到列表 ==========
        // 模拟场景：3个独立任务，每个任务耗时不同（随机0-10秒）
        for(int i=0;i<3;i++){
            Task task = new Task("Task-"+i);
            taskList.add(task);
        }

        // ========== 4. 使用 invokeAll() 执行所有任务 ==========
        // 执行逻辑：
        // 1. 将所有任务提交给线程池
        // 2. 阻塞等待，直到所有任务完成（正常返回或抛异常）
        // 3. 返回 List<Future<Result>>，按任务提交顺序排列
        //
        // 【重要】invokeAll() 会阻塞直到所有任务完成
        // 这意味着主线程会等待直到所有子任务都结束
        List<Future<Result>> resultList = null;
        try {
            resultList = executor.invokeAll(taskList);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        // ========== 5. 关闭线程池 ==========
        // 关闭后不再接受新任务，但会完成已提交的任务
        executor.shutdown();

        // ========== 6. 遍历 Future 列表，获取每个任务的结果 ==========
        // 注意：future.get() 会阻塞直到该任务完成
        // 但因为 invokeAll() 已等待所有任务完成，所以这里不会阻塞
        System.out.printf("Core: Printing the results\n");
        for (Future<Result> future : resultList) {
            try {
                // 【关键】从 Future 中获取任务返回值
                // Result 包含任务名称(name)和计算结果(value)
                Result result = future.get();
                System.out.printf("%s: %s\n", result.getName(), result.getValue());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
