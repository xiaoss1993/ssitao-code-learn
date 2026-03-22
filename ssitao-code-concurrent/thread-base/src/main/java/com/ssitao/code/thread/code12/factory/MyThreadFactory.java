package com.ssitao.code.thread.code12.factory;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;


/**
 * 自定义线程工厂
 *
 * 实现 ThreadFactory 接口，自定义线程创建行为
 *
 * 使用场景：
 * 1. 在线程池中统一设置线程属性（名称、优先级、守护线程等）
 * 2. 记录线程创建日志，便于问题排查
 * 3. 对线程进行统一包装，增加额外功能
 */
public class MyThreadFactory implements ThreadFactory {

    /** counter: 线程计数器，每创建一个线程+1 */
    private int counter;

    /** name: 线程名称的前缀部分，如 "MyThreadFactory" */
    private String name;

    /** stats: 记录所有创建的线程信息，用于统计 */
    private List<String> stats;

    /**
     * 构造函数
     *
     * @param name 线程名称的前缀，将生成如 "MyThreadFactory-Thread_0" 的线程名
     */
    public MyThreadFactory(String name) {
        this.name = name;
        this.counter = 0;                      // 初始化计数器为0
        this.stats = new ArrayList<String>(); // 初始化统计列表
    }

    /**
     * 创建线程的核心方法
     *
     * 每当调用 newThread() 时：
     * 1. 创建一个新的 Thread，命名规则：name + "-Thread_" + counter
     * 2. counter 自增
     * 3. 记录线程创建信息到 stats 列表
     *
     * @param r 要在线程中执行的任务（Runnable）
     * @return 已创建但未启动的 Thread 对象
     */
    @Override
    public Thread newThread(Runnable r) {
        // 构建线程名称：前缀-Thread_序号，如 "MyThreadFactory-Thread_0"
        Thread t = new Thread(r, this.name + "-Thread_" + this.counter);

        // 计数器+1，为下一个线程准备
        this.counter++;

        // 记录线程创建信息：线程ID、线程名、创建时间
        this.stats.add(String.format(
            "Created thread %d with name %s on %s\n",
            t.getId(),
            t.getName(),
            new Date()
        ));

        return t;
    }

    /**
     * 获取线程工厂的统计信息
     *
     * 返回所有已创建线程的详细信息列表
     *
     * @return 包含所有线程创建信息的字符串
     */
    public String getStats() {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> it = stats.iterator();

        // 遍历统计列表，拼接所有记录
        while (it.hasNext()) {
            buffer.append(it.next());
        }

        return buffer.toString();
    }
}
