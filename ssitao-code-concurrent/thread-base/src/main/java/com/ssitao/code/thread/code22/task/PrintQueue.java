package com.ssitao.code.thread.code22.task;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 打印队列类
 *
 * 该类模拟了一个打印队列系统，管理3台打印机的访问。
 *
 * 核心设计：
 * 1. 信号量(Semaphore) - 控制对打印机的总体访问数量
 *    - 初始化为3，表示有3台可用的打印机
 *    - 当线程调用acquire()时，如果还有空闲打印机则获取成功，否则阻塞
 *
 * 2. boolean[] freePrinters - 跟踪每台打印机的空闲状态
 *    - true表示空闲，false表示占用
 *
 * 3. ReentrantLock - 保护freePrinters数组的并发访问
 *    - 确保在查找和分配打印机时不会出现竞争条件
 *
 * 工作流程：
 * 1. 线程调用printJob()请求打印
 * 2. 通过semaphore.acquire()获取一个"打印机许可证"
 * 3. 通过getPrinter()找到第一台空闲的打印机并标记为占用
 * 4. 模拟打印过程（随机1-10秒）
 * 5. 打印完成后释放打印机（标记为true）
 * 6. 调用semaphore.release()释放许可证，让其他线程可以使用
 */
public class PrintQueue {
    /**
     * 资源信号量，控制打印机的访问
     */
    private Semaphore semaphore;
    /**
     * 标记打印机是否空闲的数组
     */
    private boolean[] freePrinters;
    /**
     * 锁，控制打印机是否空闲的数组的访问
     */
    private Lock lockPrinters;

    /**
     * 构造函数，初始化变量
     */
    public PrintQueue() {
        semaphore = new Semaphore(3); // 资源信号量的个数为3，说明有3个打印机
        freePrinters = new boolean[3];
        for (int i = 0; i < freePrinters.length; i++) {
            freePrinters[i] = true;
        }

        lockPrinters = new ReentrantLock();
    }

    /**
     * 模拟文档打印的方法
     *
     * 这是打印队列的主方法，线程通过调用此方法提交打印任务。
     * 方法内部使用try-finally确保信号量一定会被释放，即使发生异常。
     *
     * @param document 需要打印的对象（本例中未实际使用，仅作演示）
     */
    public void printJob(Object document) {
        try {
            // ========== 步骤1：请求信号量 ==========
            // acquire()方法会尝试获取一个许可证
            // 如果已有名线程占用3台打印机，此线程会阻塞等待
            // 这是控制并发访问的核心机制
            semaphore.acquire();

            // ========== 步骤2：分配打印机 ==========
            // 获取分配的打印机编号（0、1或2）
            int assignedPrinter = getPrinter();

            // ========== 步骤3：执行打印 ==========
            // 模拟打印耗时（1-10秒随机）
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s: PrintQueue: Printing a Job in Printer %d during %d seconds\n",
                    Thread.currentThread().getName(), assignedPrinter, duration);
            TimeUnit.SECONDS.sleep(duration);

            // ========== 步骤4：释放打印机 ==========
            // 打印完成后，标记该打印机为空闲状态
            freePrinters[assignedPrinter] = true;

        } catch (InterruptedException e) {
            // 处理线程被中断的情况
            e.printStackTrace();
        } finally {
            // ========== 步骤5：释放信号量 ==========
            // 无论打印是否成功，都必须释放信号量
            // 否则其他等待的线程将永远无法获得打印机
            semaphore.release();
        }
    }

    /**
     * 获取分配的打印机编号
     *
     * 此方法在信号量acquire()之后调用，负责从空闲打印机列表中分配一台。
     * 使用ReentrantLock保证在查找和分配过程中不会被其他线程干扰。
     *
     * 注意：由于semaphore已经限制了同时访问的线程数为3，
     *       这里使用锁主要是为了保护freePrinters数组的状态一致性
     *
     * @return 分配的打印机编号（0、1、2），如果所有打印机都忙则返回-1（理论上不会发生）
     */
    private int getPrinter() {
        int ret = -1;
        try {
            // 获取锁，保护打印机状态数组的并发访问
            lockPrinters.lock();
            // 线性查找第一台空闲的打印机
            for (int i = 0; i < freePrinters.length; i++) {
                if (freePrinters[i]) {
                    ret = i;              // 记录打印机编号
                    freePrinters[i] = false; // 标记为占用状态
                    break;                // 找到第一台空闲打印机就退出
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 无论如何都要释放锁，确保其他线程可以继续访问
            lockPrinters.unlock();
        }
        return ret;
    }
}
