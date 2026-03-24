package com.ssitao.code.thread.code51.task;

import java.util.concurrent.TimeUnit;

/**
 * 优先级任务类 - 实现 Comparable 接口支持优先级排序
 *
 * 核心设计：
 * 1. 实现 Runnable 接口，可作为线程池的任务
 * 2. 实现 Comparable 接口，定义任务间的优先级比较规则
 * 3. 数字越小优先级越高（与自然序相反）
 *
 * Comparable 实现说明：
 * - compareTo 返回负数表示当前对象优先级更高
 * - compareTo 返回正数表示当前对象优先级更低
 * - compareTo 返回 0 表示优先级相同
 *
 * 在 PriorityBlockingQueue 中的作用：
 * - 队列使用堆结构维护元素的优先级顺序
 * - 每当插入新任务时，会重新调整堆以保持优先级顺序
 * - 取任务时，总是返回优先级最高的元素
 *
 * 优先级规则：
 * - priority 值越小，优先级越高
 * - Task 0 优先级最高，Task 7 优先级最低
 * - 相同优先级的任务，按自然顺序（提交顺序）执行
 */
public class MyPriorityTask implements Runnable, Comparable<MyPriorityTask> {

    /**
     * 任务优先级
     * 值越小优先级越高
     */
    private int priority;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 构造函数，初始化任务属性
     *
     * @param name     任务名称
     * @param priority 任务优先级（值越小优先级越高）
     */
    public MyPriorityTask(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    /**
     * 获取任务优先级
     *
     * @return 优先级数值
     */
    public int getPriority() {
        return priority;
    }

    /**
     * 比较两个任务的优先级
     *
     * PriorityBlockingQueue 使用此方法确定元素顺序
     * 返回负数：当前对象优先级更高（应该先执行）
     * 返回正数：当前对象优先级更低（应该后执行）
     * 返回零：优先级相同
     *
     * @param o 要比较的任务
     * @return 比较结果
     */
    @Override
    public int compareTo(MyPriorityTask o) {
        // 当前任务优先级更低（数值更大），返回 1
        if (this.getPriority() < o.getPriority()) {
            return 1;
        }

        // 当前任务优先级更高（数值更小），返回 -1
        if (this.getPriority() > o.getPriority()) {
            return -1;
        }

        // 优先级相同，返回 0（自然顺序）
        return 0;
    }

    /**
     * 任务执行方法
     *
     * 打印任务信息，然后睡眠2秒模拟耗时操作
     * 每个任务执行需要2秒
     */
    @Override
    public void run() {
        // 打印任务开始信息
        System.out.printf("MyPriorityTask: %s Priority : %d\n", name, priority);
        try {
            // 睡眠2秒，模拟耗时操作
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}