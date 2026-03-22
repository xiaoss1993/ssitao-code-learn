package com.ssitao.code.thread.code20.task;


import com.ssitao.code.thread.code20.utils.Buffer;

import java.util.Random;

/**
 * 消费者任务类
 * 从缓冲区读取数据并进行处理（模拟打印）
 */
public class Consumer implements Runnable {

    /**
     * 缓冲区，用于获取生产者放入的数据
     */
    private Buffer buffer;

    /**
     * 构造函数
     *
     * @param buffer 缓冲对象
     */
    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    /**
     * 核心方法：持续从缓冲区获取数据并处理
     * 只要缓冲区还有数据待处理，就继续循环
     */
    @Override
    public void run() {
        while (buffer.hasPendingLines()) {
            String line = buffer.get();
            processLine(line);
        }
    }

    /**
     * 模拟处理一行数据，休眠随机时间[0,100)毫秒
     *
     * @param line 一行数据
     */
    private void processLine(String line) {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
