package com.ssitao.code.thread.code47.task;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 任务类 - 演示 ThreadLocalRandom 线程本地随机数生成
 *
 * 每个线程运行此任务时，会生成10个 [0, 10) 范围内的随机整数
 *
 * 设计特点：
 * 1. 构造函数中调用 ThreadLocalRandom.current()
 *    - 初始化当前线程的随机数生成器
 *    - 每个线程第一次调用时会创建独立的生成器
 *
 * 2. run() 方法中多次调用 ThreadLocalRandom.current()
 *    - 虽然也可以保存引用，但 current() 调用开销很小
 *    - 每次调用返回当前线程的生成器
 *
 * 3. nextInt(10) 生成 [0, 10) 即 0-9 的随机整数
 *
 * 注意事项：
 * - ThreadLocalRandom.current() 不能跨线程使用
 * - 每个线程必须自己调用 current() 获取自己的生成器
 * - 如果在主线程创建 Random 对象传给子线程，反而会更慢
 */
public class TaskLocalRandom implements Runnable {

    /**
     * 构造函数 - 初始化当前线程的随机数生成器
     *
     * 注意：这里调用 current() 纯粹是为了初始化
     * 实际上在 run() 中调用也是一样的效果
     * 因为 current() 会在第一次调用时懒加载初始化
     */
    public TaskLocalRandom() {
        ThreadLocalRandom.current();
    }

    /**
     * 任务执行入口
     *
     * 核心逻辑：
     * 1. 获取当前线程名称，用于标识输出
     * 2. 循环10次，每次生成一个 [0, 10) 的随机整数并打印
     *
     * 输出示例：
     * Thread-0: 3
     * Thread-0: 7
     * Thread-0: 1
     * ...
     * Thread-1: 5
     * Thread-1: 2
     * ...
     */
    @Override
    public void run() {
        // 获取当前线程名称
        String name = Thread.currentThread().getName();

        // 循环10次，生成并打印随机数
        for (int i = 0; i < 10; i++) {
            // ThreadLocalRandom.current() 返回当前线程的随机数生成器
            // nextInt(10) 生成 [0, 10) 即 0, 1, 2, ..., 9 中的任意整数
            System.out.printf("%s: %d\n", name, ThreadLocalRandom.current().nextInt(10));
        }
    }
}
