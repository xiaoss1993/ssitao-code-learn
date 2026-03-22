package com.ssitao.code.thread.code20.utils;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓冲区类，用于生产者和消费者之间的数据传递
 * 使用ReentrantLock和Condition实现线程同步
 */
public class Buffer {
    /**
     * 缓冲区存储结构，使用链表实现队列（FIFO）
     */
    private LinkedList<String> buffer;

    /**
     * 缓冲区的最大容量
     */
    private int maxSize;

    /**
     * 可重入锁，用于保护缓冲区的访问
     */
    private ReentrantLock lock;

    /**
     * 缓冲区有数据的条件变量
     * 消费者在此等待，当生产者插入数据时唤醒
     */
    private Condition lines;

    /**
     * 缓冲区有空间的条件变量
     * 生产者在此等待，当消费者取出数据时唤醒
     */
    private Condition space;

    /**
     * 标记是否还有更多行需要处理
     * 当所有数据都被生产完成后设为false
     */
    private boolean pendingLines;

    /**
     * 构造函数，初始化缓冲区属性
     *
     * @param maxSize 缓冲区的最大容量
     */
    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        this.buffer = new LinkedList<>();
        this.lock = new ReentrantLock();
        // 从锁创建两个条件变量，分别用于等待数据和等待空间
        this.lines = lock.newCondition();
        this.space = lock.newCondition();
        this.pendingLines = true; // 初始时假设有数据待处理
    }

    /**
     * 向缓冲区中插入一行数据
     * 如果缓冲区满，则等待消费者取出数据腾出空间
     *
     * @param line 要插入的一行数据
     */
    public void insert(String line) {
        lock.lock();
        try {
            // 当缓冲区满时，生产者等待有空间可用
            while (this.buffer.size() == this.maxSize) {
                this.space.await();
            }

            // 将数据插入缓冲区尾部
            this.buffer.offer(line);
            System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread().getName(), this.buffer.size());
            // 唤醒所有等待的消费者（有数据可取了）
            this.lines.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从缓冲区获取一行数据
     * 如果缓冲区空且还有待处理数据，则等待生产者插入
     *
     * @return 取出的数据，如果无数据可取则返回null
     */
    public String get() {
        String line = null;
        lock.lock();
        try {
            // 当缓冲区为空且还有待处理数据时，消费者等待
            while (this.buffer.size() == 0 && hasPendingLines()) {
                this.lines.await();
            }

            // 再次检查是否有数据可取
            if (hasPendingLines()) {
                // 从缓冲区头部取出数据
                line = this.buffer.poll();
                System.out.printf("%s: Line Readed: %d\n", Thread.currentThread().getName(), this.buffer.size());
                // 唤醒所有等待的生产者（有空间可用了）
                this.space.signalAll();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }

        return line;
    }

    /**
     * 设置是否还有更多行需要追加到缓冲区
     * 生产者完成所有数据生产后调用此方法
     *
     * @param pendingLines true表示还有数据要追加，false表示已无数据追加
     */
    public void setPendingLines(boolean pendingLines) {
        this.pendingLines = pendingLines;
    }

    /**
     * 判断是否还有数据可以进行处理
     * 当缓冲区有数据，或者还有数据要追加时返回true
     *
     * @return true表示有数据可进行处理，false表示无数据可处理
     */
    public boolean hasPendingLines() {
        return this.pendingLines || this.buffer.size() > 0;
    }
}