package com.ssitao.code.thread.code29.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 服务器类 - 模拟使用线程池处理任务的服务器
 *
 * 该类封装了线程池的创建和管理，提供任务提交和服务器关闭功能
 * 使用ThreadPoolExecutor作为底层实现，支持高并发任务处理
 */
public class Server {

    /**
     * 线程池执行器 - 负责管理线程的生命周期和任务调度
     *
     * ThreadPoolExecutor是Java并发包提供的核心类：
     * - 管理线程创建和销毁
     * - 维护任务阻塞队列
     * - 控制并发线程数量
     * - 提供线程池状态监控
     */
    private ThreadPoolExecutor executor;

    /**
     * 构造函数 - 初始化固定大小线程池
     *
     * 创建一个FixedThreadPool(5)类型的线程池：
     * - 核心线程数: 5（始终保持5个工作线程）
     * - 最大线程数: 5（不会超过5个线程）
     * - 空闲线程超时: 无超时回收（核心线程不会回收）
     * - 特点: 线程数量固定，适合长期运行的任务，保持系统资源稳定
     *
     * 与CachedThreadPool对比：
     * - FixedThreadPool: 资源可控，线程复用，适合任务量稳定但较长的任务
     * - CachedThreadPool: 弹性扩展，线程按需创建，适合短时异步任务
     */
    public Server() {
        // 使用Executors工厂类创建固定大小线程池，并强转为ThreadPoolExecutor以使用其特有方法
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    /**
     * 任务执行方法 - 接收Task对象并提交到线程池执行
     *
     * 该方法是服务器处理请求的入口，负责：
     * 1. 打印新任务到达信息
     * 2. 将任务提交到线程池
     * 3. 输出线程池当前状态（用于监控）
     *
     * @param task 待执行的Task对象，不能为空
     */
    public void executeTask(Task task) {
        // ========== 步骤1: 记录新任务到达 ==========
        System.out.printf("Server: A new task has arrived\n");

        // ========== 步骤2: 提交任务到线程池 ==========
        // execute()方法是非阻塞的，会立即返回
        // 任务会被放入阻塞队列，由线程池中的工作线程异步执行
        // 如果所有5个线程都在忙碌，任务会在队列中等待
        executor.execute(task);

        // ========== 步骤3: 输出线程池状态（用于监控） ==========

        // getPoolSize(): 返回当前线程池中的线程数量
        // FixedThreadPool的线程数固定为5，不会变化
        System.out.printf("Server: Pool Size: %d\n", executor.getPoolSize());

        // getActiveCount(): 返回正在执行任务的线程数量
        // 范围是0到5，表示当前有多少个工作线程在执行任务
        System.out.printf("Server: Active Count: %d\n", executor.getActiveCount());

        // getTaskCount(): 返回已提交的任务总数（包括已完成、正在执行、等待执行）
        // 注意：这是累积值，从线程池创建开始计算
        System.out.printf("Server: Task Count: %d\n", executor.getTaskCount());

        // getCompletedTaskCount(): 返回已完成任务的总计数
        // 注意：这是累积值，包括所有已完成的任务
        System.out.printf("Server: Completed Tasks: %d\n", executor.getCompletedTaskCount());
    }

    /**
     * 关闭服务器方法 - 平滑关闭线程池
     *
     * 调用shutdown()后：
     * - 不再接受新任务提交
     * - 会完成已提交到队列中的所有任务（包括95个排队任务）
     * - 正在执行的5个任务会继续执行直到完成
     * - 不会阻塞等待所有任务完成（非阻塞调用）
     *
     * 注意：如果需要等待所有任务完成，可使用awaitTermination()
     *       如果需要强制停止，可使用shutdownNow()
     */
    public void endServer() {
        // 使用shutdown()方法平滑关闭线程池
        executor.shutdown();
    }
}
