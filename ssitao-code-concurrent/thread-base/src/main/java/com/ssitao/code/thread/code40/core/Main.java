package com.ssitao.code.thread.code40.core;

import com.ssitao.code.thread.code40.task.FolderProcessor;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Fork/Join 框架演示：并行文件搜索
 *
 * 本示例展示如何使用 ForkJoinPool 和 RecursiveTask 实现高效的并行文件搜索。
 * Fork/Join 框架采用"分而治之"策略，将大任务递归拆分成小任务并行处理，最后合并结果。
 *
 * 核心概念：
 * - ForkJoinPool: 专门用于执行 Fork/Join 任务的线程池
 * - Fork: 将大任务拆分成多个小任务，异步执行
 * - Join: 等待子任务完成并合并结果
 */
public class Main {

    public static void main(String[] args) {
        // ========== 1. 创建 ForkJoinPool ==========
        // ForkJoinPool 是专门为 Fork/Join 任务设计的线程池
        // 它使用 work-stealing 算法：空闲线程会从其他忙碌线程的任务队列中"偷取"任务
        ForkJoinPool pool = new ForkJoinPool();

        // ========== 2. 创建多个文件处理任务 ==========
        // 为三个不同的文件夹创建处理器，每个任务会递归搜索子文件夹
        FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
        FolderProcessor apps = new FolderProcessor("C:\\Program Files", "log");
        FolderProcessor documents = new FolderProcessor("C:\\Documents And Settings", "log");

        // ========== 3. 异步执行任务 ==========
        // execute() 方法用于异步执行任务，不会阻塞主线程
        pool.execute(system);
        pool.execute(apps);
        pool.execute(documents);

        // ========== 4. 打印执行统计信息 ==========
        // 循环打印线程池状态，直到所有任务完成
        do {
            System.out.printf("******************************************\n");
            // getParallelism(): 返回池中的并行级别（即同时执行的任务数）
            System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
            // getActiveThreadCount(): 当前正在执行任务的线程数
            System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
            // getQueuedTaskCount(): 等待执行的任务数
            System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
            // getStealCount(): 线程从其他线程偷取任务的总次数（衡量负载均衡程度）
            System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
            System.out.printf("******************************************\n");
            try {
                // 每秒打印一次状态
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while ((!system.isDone()) || (!apps.isDone()) || (!documents.isDone()));
        // isDone(): 判断任务是否完成（完成、取消或异常）

        // ========== 5. 关闭线程池 ==========
        pool.shutdown();

        // ========== 6. 获取并打印结果 ==========
        // join() 方法会阻塞直到任务完成并返回结果
        // 类似于 Future.get()，但专门用于 Fork/Join 任务
        List<String> results;

        results = system.join();
        System.out.printf("System: %d files found.\n", results.size());

        results = apps.join();
        System.out.printf("Apps: %d files found.\n", results.size());

        results = documents.join();
        System.out.printf("Documents: %d files found.\n", results.size());
    }
}
