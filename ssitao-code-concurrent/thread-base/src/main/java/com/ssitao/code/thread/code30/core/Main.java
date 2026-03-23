package com.ssitao.code.thread.code30.core;


import com.ssitao.code.thread.code30.task.FactorialCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Main类：演示使用ThreadPoolExecutor线程池执行器执行多个 Callable 任务
 *
 * 【核心概念讲解】
 *
 * 1. ThreadPoolExecutor（线程池执行器）
 *    - 线程池是一种多线程处理形式，处理过程中将任务添加到队列
 *    - 线程池在系统启动时即创建大量空闲线程，我们将任务提交给线程池执行
 *    - 优点：重用已有线程，减少线程创建/销毁开销，控制并发数量
 *
 * 2. Callable 与 Future
 *    - Callable<T> 是带返回值的任务接口，类似于 Runnable 但能返回结果
 *    - Future<T> 代表异步计算的结果，可以查询任务是否完成、获取返回值
 *    - Callable通过ExecutorService.submit()提交，返回Future对象
 *
 * 3. FixedThreadPool
 *    - 固定大小的线程池，本例创建了2个线程
 *    - 最多同时执行2个任务，其他任务在队列中等待
 *
 * 4. 工作流程
 *    创建任务 → 提交到线程池 → 线程池分配线程执行 → 主线程通过Future获取结果
 */
public class Main {
    public static void main(String[] args) {
        // ========== 第1步：创建线程池 ==========
        // newFixedThreadPool(2) 创建固定2个线程的线程池
        // 使用ThreadPoolExecutor类型引用，以便访问其特有方法（如getCompletedTaskCount）
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        // ========== 第2步：准备存储结果的容器 ==========
        // Future<Integer> 用于存储任务的执行结果
        // 注意：Future对象在任务提交时创建，此时任务可能还未执行完
        List<Future<Integer>> resultList = new ArrayList<>();

        // Random用于生成0-9的随机数作为阶乘计算的输入
        Random random = new Random();

        // ========== 第3步：提交10个任务到线程池 ==========
        // 循环创建10个阶乘计算任务并提交
        for (int i = 0; i < 10; i++) {
            // 生成[0, 10)之间的随机整数
            Integer number = random.nextInt(10);

            // 创建阶乘计算任务（实现Callable接口）
            FactorialCalculator calculator = new FactorialCalculator(number);

            // 【关键】submit()方法：
            //   - 将任务提交给线程池执行
            //   - 立即返回Future对象，不会阻塞主线程
            //   - 任务会在线程池的某个空闲线程中异步执行
            Future<Integer> result = executor.submit(calculator);

            // 保存Future对象，后续用于获取结果
            resultList.add(result);
        }

        // ========== 第4步：监控任务执行进度 ==========
        // 使用do-while循环轮询任务完成状态
        // 注意：此时任务正在后台异步执行，主线程可以同时做其他事
        do {
            // getCompletedTaskCount() 获取已完成的任务总数（累计）
            System.out.printf("Main: Number of Completed Tasks: %d\n",
                executor.getCompletedTaskCount());

            // 遍历所有Future对象，检查每个任务是否完成
            // isDone() 返回true表示该任务已完成（无论成功或异常）
            for (int i = 0; i < resultList.size(); i++) {
                Future<Integer> result = resultList.get(i);
                System.out.printf("Main: Task %d: %s\n", i, result.isDone());
            }

            // 主线程休眠50毫秒，避免频繁轮询消耗CPU资源
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 条件：已完成任务数 < 总任务数，则继续等待
        } while (executor.getCompletedTaskCount() < resultList.size());

        // ========== 第5步：获取任务执行结果 ==========
        // 所有任务完成后，通过Future.get()获取每个任务的返回值
        System.out.printf("Main: Results\n");

        for (int i = 0; i < resultList.size(); i++) {
            Future<Integer> result = resultList.get(i);
            Integer number = null;
            try {
                // 【重要】get()方法的行为：
                //   - 如果任务已完成，立即返回结果
                //   - 如果任务未完成，阻塞调用线程直到任务完成
                //   - 如果任务执行过程抛出异常，会包装为ExecutionException
                number = result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.printf("Core: Task %d: %d\n", i, number);
        }

        // ========== 第6步：关闭线程池 ==========
        // shutdown()：优雅关闭，不再接受新任务，但会执行已提交的任务
        // shutdownNow()：强制关闭，尝试中断正在执行的任务
        executor.shutdown();
    }
}
