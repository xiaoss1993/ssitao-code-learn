package com.ssitao.code.thread.code28.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 服务器类 - 模拟一个使用线程池处理任务的服务器
 *
 * 该类封装了线程池的创建和管理，提供任务提交和服务器关闭功能
 * 使用ThreadPoolExecutor作为底层线程池实现，支持高并发任务处理
 */
public class Server {

    /**
     * 线程池执行器 - 负责管理线程的生命周期和任务调度
     *
     * ThreadPoolExecutor是Java并发包提供的核心类：
     * - 管理线程创建和销毁
     * - 维护任务队列
     * - 控制并发线程数量
     * - 提供线程状态监控
     */
    private ThreadPoolExecutor executor;

    /**
     * 构造函数 - 初始化线程池
     *
     * 创建一个CachedThreadPool类型的线程池：
     * - 核心线程数: 0
     * - 最大线程数: Integer.MAX_VALUE (几乎无限制)
     * - 空闲线程超时: 60秒后被回收
     * - 特点: 按需创建新线程，复用空闲线程，适合短生命周期异步任务
     */
    public Server() {
        // 使用Executors工厂类创建缓存线程池，并强转为ThreadPoolExecutor以使用其特有方法
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
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
        executor.execute(task);

        // ========== 步骤3: 输出线程池状态（用于监控） ==========

        // getPoolSize(): 返回当前线程池中的线程数量
        // 包括正在执行任务的线程和等待任务的空闲线程
        System.out.printf("Server: Pool Size: %d\n", executor.getPoolSize());

        // getActiveCount(): 返回正在执行任务的线程数量
        // 这个值是瞬时的，可能在获取后立即变化
        System.out.printf("Server: Active Count: %d\n", executor.getActiveCount());

        // getCompletedTaskCount(): 返回已完成任务的总计数
        // 注意：这是累积值，包括所有已完成的任务
        System.out.printf("Server: Completed Tasks: %d\n", executor.getCompletedTaskCount());
    }

    /**
     * 关闭服务器方法 - 平滑关闭线程池
     *
     * 调用shutdown()后：
     * - 不再接受新任务提交
     * - 会完成已提交到队列中的所有任务
     * - 正在执行的任务会继续执行直到完成
     * - 不会阻塞等待所有任务完成
     */
    public void endServer() {
        // 使用shutdown()方法平滑关闭线程池
        // 如果需要等待所有任务完成，可使用shutdownNow()
        executor.shutdown();
    }
}
