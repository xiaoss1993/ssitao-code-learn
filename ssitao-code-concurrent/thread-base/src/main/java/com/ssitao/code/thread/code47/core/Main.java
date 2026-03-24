package com.ssitao.code.thread.code47.core;


import com.ssitao.code.thread.code47.task.TaskLocalRandom;

/**
 * 主程序 - ThreadLocalRandom 并发随机数示例
 *
 * 演示使用 ThreadLocalRandom 在多线程环境中生成随机数
 *
 * 核心概念：
 * 1. ThreadLocalRandom - 线程本地的随机数生成器
 *    - 每个线程拥有独立的随机数生成器
 *    - 避免多线程竞争导致的性能问题
 *    - 无需同步，非常适合并发场景
 *
 * 2. 与 Random 的区别：
 *    - Random 是全局的，所有线程共享一个随机数生成器
 *    - 高并发时，多个线程竞争同一个锁，导致性能瓶颈
 *    - ThreadLocalRandom 每个线程独立，避免竞争
 *
 * 3. 常见错误：
 *    - 在主线程调用 ThreadLocalRandom.current() 然后传给子线程
 *    - 正确做法：每个线程自己调用 ThreadLocalRandom.current()
 *
 * 典型应用场景：
 * - 多线程并发生成随机ID
 * - 并行计算中的随机采样
 * - 游戏中的多线程随机事件
 */
public class Main {
    public static void main(String[] args) {

        // 创建长度为3的线程数组
        Thread threads[] = new Thread[3];

        // 启动3个线程，每个线程运行一个 TaskLocalRandom 任务
        // 每个任务内部会自己调用 ThreadLocalRandom.current()
        // 确保每个线程获得独立的随机数生成器
        for (int i = 0; i < threads.length; i++) {
            TaskLocalRandom task = new TaskLocalRandom();
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // 注意：这里没有使用 join() 等待线程完成
        // 因为主线程不需要等待子线程，主线程执行完毕后程序会退出
        // 如果需要等待所有线程完成，应该添加 join() 调用
    }
}
