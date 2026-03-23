package com.ssitao.code.thread.code32.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 任务类，实现Callable接口，参数化为Result类型
 *
 * 【泛型说明】
 * Callable<Result> 表示：
 * - call() 方法的返回类型是 Result
 * - 可以被线程池submit()或invokeAll()执行
 *
 * 【模拟场景】
 * 这个任务模拟一个耗时的计算操作：
 * 1. 随机等待0-10秒（模拟计算/IO耗时）
 * 2. 计算5个随机数的和
 * 3. 返回包含任务名和结果的Result对象
 *
 * 【与 Runnable 的区别】
 * - Runnable: run() 无返回值，不能抛检查异常
 * - Callable: call() 有返回值，可以抛检查异常
 * - 本例需要返回计算结果，所以用 Callable
 */
public class Task implements Callable<Result> {
    /**
     * 任务的名称
     */
    private String name;

    /**
     * 构造函数
     *
     * @param name 初始化任务的名称
     */
    public Task(String name) {
        this.name = name;
    }

    /**
     * 核心方法：模拟一个耗时的计算任务
     *
     * 【执行流程】
     * 1. 输出开始日志
     * 2. 随机等待0-10秒（模拟真实的计算/IO耗时）
     * 3. 计算5个随机数的和
     * 4. 创建Result对象并返回
     *
     * 【为什么返回 Result 而不是简单类型？】
     * invokeAll() 返回 List<Future<Result>>
     * Result 可以携带多个数据（任务名+计算结果）
     * 实际场景中可能返回更复杂的数据结构
     *
     * @return 包含任务名称和计算结果的Result对象
     * @throws Exception 如果任务执行失败
     */
    @Override
    public Result call() throws Exception {
        // ========== 1. 任务开始 ==========
        System.out.printf("%s: Staring\n", this.name);

        // ========== 2. 模拟耗时操作（随机0-10秒） ==========
        // 真实场景：网络请求、数据库查询、复杂计算等
        try {
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s: Waiting %d seconds for results.\n", this.name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            // 如果线程被中断，说明线程池正在关闭
            // 任务应该清理资源并退出
            e.printStackTrace();
        }

        // ========== 3. 执行计算：求5个随机数的和 ==========
        int value = 0;
        for (int i = 0; i < 5; i++) {
            // Math.random() * 100 生成 0-100 之间的随机数
            value += (int) (Math.random() * 100);
        }

        // ========== 4. 创建并返回结果对象 ==========
        Result result = new Result();
        result.setName(this.name);
        result.setValue(value);
        System.out.printf("%s: Ends\n", this.name);

        return result;
    }
}
