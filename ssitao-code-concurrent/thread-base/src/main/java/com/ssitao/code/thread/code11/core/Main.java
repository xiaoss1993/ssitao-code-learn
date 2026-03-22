package com.ssitao.code.thread.code11.core;


import com.ssitao.code.thread.code11.group.MyThreadGroup;
import com.ssitao.code.thread.code11.task.Task;

/**
 * 线程组中不可控制异常的处理
 */
public class Main {

    public static void main(String[] args) {
        // 创建一个自定义的线程组
        MyThreadGroup threadGroup = new MyThreadGroup("MyThreadGroup");
        // 创建一个任务
        Task task = new Task();
        // 创建两个线程，将其放入同一个线程组中，并且执行同一个任务
        for (int i = 0; i < 2; i++) {
            Thread t = new Thread(threadGroup, task);
            t.start();
        }
    }
}
