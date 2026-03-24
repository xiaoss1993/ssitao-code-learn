package com.ssitao.code.thread.code58.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义优先级传输队列
 *
 * 核心设计：
 * 1. 继承 PriorityBlockingQueue（优先级队列）
 * 2. 实现 TransferQueue 接口（传输队列）
 * 3. 结合两者实现带优先级的同步传输机制
 *
 * 数据结构：
 * - counter: 等待中的消费者数量（原子变量）
 * - transfered: 等待被传递的元素队列（FIFO）
 * - lock: 重入锁，保护共享状态
 *
 * TransferQueue 核心语义：
 * - transfer() 确保元素被消费者接收后才返回
 * - 如果没有消费者，元素会等待直到被消费
 * - 这实现了生产者和消费者的同步
 *
 * take() 特殊处理：
 * - 优先从 transfered 队列取（等待传递的元素）
 * - 如果 transfered 为空，从 PriorityBlockingQueue 取
 * - 取到元素后唤醒等待中的生产者线程
 */
public class MyPriorityTransferQueue<E> extends
        PriorityBlockingQueue<E> implements TransferQueue<E> {

    /**
     * 等待中的消费者计数器
     * 使用原子变量保证线程安全
     */
    private AtomicInteger counter;

    /**
     * 待传输的元素队列（FIFO）
     * 存储等待被传递的元素
     */
    private LinkedBlockingQueue<E> transfered;

    /**
     * 重入锁，保护共享状态
     */
    private ReentrantLock lock;

    /**
     * 构造函数
     */
    public MyPriorityTransferQueue() {
        counter = new AtomicInteger(0);
        lock = new ReentrantLock();
        transfered = new LinkedBlockingQueue<>();
    }

    /**
     * 非阻塞尝试传输
     *
     * @param e 要传输的元素
     * @return true 如果有消费者等待，false 如果没有消费者
     */
    @Override
    public boolean tryTransfer(E e) {
        lock.lock();
        boolean value;
        if (counter.get() == 0) {
            // 没有消费者，返回 false
            value = false;
        } else {
            // 有消费者，添加到队列（被消费者取走）
            put(e);
            value = true;
        }
        lock.unlock();
        return value;
    }

    /**
     * 传输元素（阻塞）
     *
     * 如果有等待中的消费者，直接交给消费者
     * 如果没有消费者，等待直到被消费
     *
     * @param e 要传输的元素
     * @throws InterruptedException 如果等待过程中被中断
     */
    @Override
    public void transfer(E e) throws InterruptedException {
        lock.lock();
        if (counter.get() != 0) {
            // 有等待中的消费者，直接放入队列
            put(e);
            lock.unlock();
        } else {
            // 没有消费者，加入传输队列并等待
            transfered.add(e);
            lock.unlock();

            // 在元素上等待，直到被消费者唤醒
            synchronized (e) {
                e.wait();
            }
        }
    }

    /**
     * 限时尝试传输
     *
     * @param e 要传输的元素
     * @param timeout 最大等待时间
     * @param unit 时间单位
     * @return true 如果元素被消费，false 如果超时
     * @throws InterruptedException 如果等待过程中被中断
     */
    @Override
    public boolean tryTransfer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {
        lock.lock();
        if (counter.get() != 0) {
            // 有消费者，直接放入队列
            put(e);
            lock.unlock();
            return true;
        } else {
            // 没有消费者，加入传输队列
            transfered.add(e);
            long newTimeout = TimeUnit.MILLISECONDS.convert(timeout, unit);
            lock.unlock();

            // 等待指定时间
            e.wait(newTimeout);

            lock.lock();
            // 检查元素是否还在传输队列中
            if (transfered.contains(e)) {
                // 超时，元素未被消费，移除
                transfered.remove(e);
                lock.unlock();
                return false;
            } else {
                // 元素已被消费
                lock.unlock();
                return true;
            }
        }
    }

    /**
     * 检查是否有等待中的消费者
     *
     * @return true 如果有消费者在等待
     */
    @Override
    public boolean hasWaitingConsumer() {
        return (counter.get() != 0);
    }

    /**
     * 获取等待中的消费者数量
     *
     * @return 等待中的消费者数量
     */
    @Override
    public int getWaitingConsumerCount() {
        return counter.get();
    }

    /**
     * 获取元素（阻塞）
     *
     * 重写 take() 方法以支持传输机制：
     * 1. 增加消费者计数器
     * 2. 优先从 transfered 队列获取（等待传递的元素）
     * 3. 如果 transfered 为空，从 PriorityBlockingQueue 获取
     * 4. 获取后减少消费者计数器
     * 5. 唤醒等待中的生产者线程
     *
     * @return 获取的元素
     * @throws InterruptedException 如果等待过程中被中断
     */
    @Override
    public E take() throws InterruptedException {
        lock.lock();

        // 有一个消费者在等待
        counter.incrementAndGet();

        // 优先从传输队列获取
        E value = transfered.poll();

        if (value == null) {
            // 传输队列为空，从优先级队列获取
            lock.unlock();
            value = super.take();
            lock.lock();
        } else {
            // 从传输队列获取到元素，唤醒等待的生产者
            synchronized (value) {
                value.notify();
            }
        }

        // 消费者不再等待
        counter.decrementAndGet();
        lock.unlock();

        return value;
    }
}
