package com.ssitao.code.thread.code32.task;

/**
 * 结果类，存储并发任务产生的结果
 *
 * 【设计说明】
 * Result 是一个数据传输对象(Data Transfer Object, DTO)
 * 用于在线程池和主线程之间传递任务执行结果
 *
 * 【为什么需要这个类？】
 * - Task.call() 返回 Result，而不是简单类型
 * - Result 包含任务的标识(name)和计算结果(value)
 * - 实际场景中可能包含更多字段（如状态码、错误信息等）
 *
 * 【线程安全说明】
 * 每个任务返回自己独立的 Result 对象
 * 主线程通过 Future.get() 逐个获取，不存在并发访问同一对象的情况
 * 因此 Result 类本身不需要考虑线程安全
 */
public class Result {
    /**
     * 产生结果的任务名称
     * 用于标识是哪个任务产生了这个结果
     */
    private String name;

    /**
     * 产生的结果值
     * 本例中是5个随机数的和
     */
    private int value;

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置任务名称
     *
     * @param name 任务名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取计算结果
     *
     * @return 计算结果值
     */
    public int getValue() {
        return value;
    }

    /**
     * 设置计算结果
     *
     * @param value 计算结果值
     */
    public void setValue(int value) {
        this.value = value;
    }
}
