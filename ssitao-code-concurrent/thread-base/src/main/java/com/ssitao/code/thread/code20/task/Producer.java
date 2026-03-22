package com.ssitao.code.thread.code20.task;

import com.ssitao.code.thread.code20.utils.Buffer;
import com.ssitao.code.thread.code20.utils.FileMock;

/**
 * 生产者任务类
 * 从文件模拟对象读取数据并插入到缓冲区
 */
public class Producer implements Runnable {

    /**
     * 文件模拟对象，用于读取文件数据
     */
    private FileMock mock;

    /**
     * 缓冲区，用于存储生产者读取的数据
     */
    private Buffer buffer;

    /**
     * 构造函数
     *
     * @param mock   文件模拟对象
     * @param buffer 缓冲对象
     */
    public Producer(FileMock mock, Buffer buffer) {
        this.mock = mock;
        this.buffer = buffer;
    }

    /**
     * 核心方法：读取文件中的数据，并且将读取到的数据插入到缓冲区
     * 流程：
     * 1. 标记开始有数据待处理
     * 2. 循环读取文件直到读完所有行
     * 3. 每读取一行就插入缓冲区
     * 4. 完成后标记已无数据可追加
     */
    @Override
    public void run() {
        // 标记开始有数据待处理，唤醒等待中的消费者
        this.buffer.setPendingLines(true);
        // 循环读取文件中的所有行
        while (this.mock.hasMoreLines()) {
            String line = this.mock.getLine();
            this.buffer.insert(line);
        }
        // 标记已无数据可追加
        this.buffer.setPendingLines(false);
    }
}
