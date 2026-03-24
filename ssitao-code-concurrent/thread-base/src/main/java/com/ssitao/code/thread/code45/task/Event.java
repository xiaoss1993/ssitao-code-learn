package com.ssitao.code.thread.code45.task;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 事件类，实现 Delayed 接口
 *
 * Delayed 接口是 Java 并发包中的关键接口，用于支持延迟操作
 * 典型应用场景：
 * 1. 缓存过期：缓存元素在指定时间后自动失效
 * 2. 任务调度：延迟执行的任务
 * 3. 订单超时：超时未支付则自动取消订单
 *
 * 接口要求实现两个方法：
 * 1. getDelay(TimeUnit unit) - 返回距离激活还剩余的时间
 * 2. compareTo(Delayed o) - 与其他 Delayed 对象比较顺序
 */
public class Event implements Delayed {
    /**
     * 事件的激活时间（到期时间）
     * 在这个时间之前，事件不能从 DelayQueue 中取出
     */
    private Date startDate;

    /**
     * 构造函数
     *
     * @param startDate 事件的激活时间（到期时间）
     */
    public Event(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 比较两个事件的优先级
     *
     * DelayQueue 内部使用优先级队列（堆结构）存储元素
     * 需要按照到期时间排序：到期时间越早，优先级越高
     *
     * @param o 要比较的另一个 Delayed 对象
     * @return 负数表示当前对象先到期，正数表示后到期，0表示同时到期
     */
    @Override
    public int compareTo(Delayed o) {
        // 计算两个事件剩余延迟的差值（以纳秒为单位）
        long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        if (result < 0) {
            return -1;  // 当前事件先到期
        } else if (result > 0) {
            return 1;   // 当前事件后到期
        }
        return 0;       // 两个事件同时到期
    }

    /**
     * 获取距离激活还剩余的延迟时间
     *
     * 这是 DelayQueue 判断元素是否到期的方法
     * 返回值 <= 0 表示元素已到期，可以被取出
     *
     * @param unit 希望返回的时间单位
     * @return 剩余延迟时间，如果已到期则返回 0 或负数
     */
    @Override
    public long getDelay(TimeUnit unit) {
        Date now = new Date();
        // 计算激活时间与当前时间之差
        long diff = startDate.getTime() - now.getTime();
        // 转换为请求的时间单位返回
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }
}
