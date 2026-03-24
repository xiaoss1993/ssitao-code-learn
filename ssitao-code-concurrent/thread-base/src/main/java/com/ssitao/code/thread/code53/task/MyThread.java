package com.ssitao.code.thread.code53.task;

import java.util.Date;

/**
 * 自定义线程 - 扩展 Thread 类添加监控功能
 *
 * 核心设计：
 * 1. 继承 Thread 类，扩展监控能力
 * 2. 记录线程的创建时间、开始时间、结束时间
 * 3. 可以计算任务执行时长
 * 4. 重写 toString() 提供详细的线程信息
 *
 * 监控时间点：
 * - creationDate: 线程对象创建时记录
 * - startDate: 线程实际开始执行时记录
 * - finishDate: 线程执行完成时记录
 *
 * 执行时间计算：
 * - getExecutionTime() = finishDate - startDate
 * - 不包括线程创建到开始执行的时间
 *
 * 使用场景：
 * - 需要追踪任务执行时长的场景
 * - 性能监控和调试
 * - 日志记录和审计
 */
public class MyThread extends Thread {

    /**
     * 线程创建日期（构造函数被调用时）
     */
    private Date creationDate;

    /**
     * 线程开始执行日期（run() 方法开始时）
     */
    private Date startDate;

    /**
     * 线程执行完成日期（run() 方法结束时）
     */
    private Date finishDate;


    /**
     * 构造函数
     *
     * @param target 要执行的任务（Runnable）
     * @param name   线程名称
     */
    public MyThread(Runnable target, String name) {
        super(target, name);
        // 在构造函数中记录线程创建时间
        setCreationDate();
    }

    /**
     * 重写 run() 方法，在任务执行前后记录时间戳
     *
     * 执行流程：
     * 1. 记录开始时间
     * 2. 执行父类的 run()（即执行传入的 Runnable 任务）
     * 3. 记录结束时间
     */
    @Override
    public void run() {
        // 任务开始执行时记录时间
        setStartDate();
        // 调用父类的 run() 执行实际任务
        super.run();
        // 任务执行完成时记录时间
        setFinishDate();
    }

    /**
     * 设置线程创建日期
     */
    public void setCreationDate() {
        creationDate = new Date();
    }

    /**
     * 设置线程开始执行日期
     */
    public void setStartDate() {
        startDate = new Date();
    }

    /**
     * 设置线程执行完成日期
     */
    public void setFinishDate() {
        finishDate = new Date();
    }

    /**
     * 获取任务执行时长
     *
     * @return 执行时长（毫秒）
     *
     * 注意：这是实际任务执行的时间，不包括等待时间
     */
    public long getExecutionTime() {
        return finishDate.getTime() - startDate.getTime();
    }

    /**
     * 返回线程的详细字符串描述
     *
     * 包含：线程名称、创建日期、执行时长
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getName());
        buffer.append(": ");
        buffer.append(" Creation Date: ");
        buffer.append(creationDate);
        buffer.append(" : Running time: ");
        buffer.append(getExecutionTime());
        buffer.append(" Milliseconds.");
        return buffer.toString();
    }
}
