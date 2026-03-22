package com.ssitao.code.thread.code24.task;


import com.ssitao.code.thread.code24.utils.MatrixMock;
import com.ssitao.code.thread.code24.utils.Results;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 搜索者类
 *
 * 负责在矩阵的指定行范围内搜索目标数字的出现次数。
 * 每个 Searcher 运行在自己的线程中，处理矩阵的一部分行。
 */
public class Searcher implements Runnable {
    /**
     * 开始查找的行数（inclusive，包含）
     */
    private int firstRow;

    /**
     * 最后查找的行数（exclusive，不包含）
     */
    private int lastRow;

    /**
     * 矩阵模拟对象引用
     */
    private MatrixMock mock;

    /**
     * 结果对象引用，用于存储搜索结果
     */
    private Results results;

    /**
     * 要查找的目标数字
     */
    private int number;

    /**
     * 循环屏障引用
     * 用于与其他搜索线程同步
     */
    private final CyclicBarrier barrier;

    /**
     * 构造函数
     *
     * @param barrier  循环屏障，用于线程同步
     * @param firstRow 开始查找的行数（包含）
     * @param lastRow  最后查找的行数（不包含）
     * @param mock     矩阵模拟对象
     * @param results  结果对象
     * @param number   要查找的数字
     */
    public Searcher(CyclicBarrier barrier, int firstRow, int lastRow, MatrixMock mock, Results results, int number) {
        this.barrier = barrier;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.mock = mock;
        this.results = results;
        this.number = number;
    }

    /**
     * 核心方法：搜索指定行范围内的目标数字
     *
     * 执行流程：
     * 1. 打印开始信息，显示处理的行范围
     * 2. 遍历 firstRow 到 lastRow-1 的每一行
     * 3. 对每行的每个元素进行搜索，统计目标数字出现的次数
     * 4. 将每行的统计结果存入 results 数组对应位置
     * 5. 调用 barrier.await() 等待所有搜索线程完成
     */
    @Override
    public void run() {
        int counter;

        // ========== 步骤1：打印开始信息 ==========
        System.out.printf("%s: Processing lines from %d to %d.\n",
                Thread.currentThread().getName(), firstRow, lastRow);

        // ========== 步骤2：搜索矩阵 ==========
        for (int i = firstRow; i < lastRow; i++) {
            // 获取当前行的数据
            int row[] = mock.getRow(i);
            counter = 0;

            // 遍历当前行的每个元素
            for (int j = 0; j < row.length; j++) {
                // 如果找到目标数字，计数器加1
                if (row[j] == number) {
                    counter++;
                }
            }

            // 将该行的搜索结果存入 results 数组
            results.setData(i, counter);
        }

        // ========== 步骤3：打印完成信息 ==========
        System.out.printf("%s: Lines processed.\n", Thread.currentThread().getName());

        // ========== 步骤4：等待其他线程 ==========
        // await() 方法会使当前线程等待，直到所有参与线程都到达屏障点
        // 当所有线程都调用 await() 后，屏障打开，线程继续执行
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            // 如果屏障被破坏（例如有线程超时或被中断），会抛出此异常
            e.printStackTrace();
        }
    }
}
