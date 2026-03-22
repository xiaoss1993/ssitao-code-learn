package com.ssitao.code.thread.code24.core;


import com.ssitao.code.thread.code24.task.Grouper;
import com.ssitao.code.thread.code24.task.Searcher;
import com.ssitao.code.thread.code24.utils.MatrixMock;
import com.ssitao.code.thread.code24.utils.Results;

import java.util.concurrent.CyclicBarrier;

/**
 * 主程序入口类
 * 演示使用 CyclicBarrier（循环屏障） 实现多线程分阶段同步
 *
 * CyclicBarrier 核心概念：
 * - 一种同步辅助类，让一组线程相互等待
 * - 所有线程都到达屏障点后，屏障打开，线程继续执行
 * - 可以指定一个 Runnable，在所有线程到达后执行（类似于本例的 Grouper）
 * - 特点：可以重置并重复使用（"Cyclic"含义）
 *
 * 本例场景：矩阵数字并行搜索
 * - 10000行 x 1000列的矩阵，随机填充数字
 * - 5个线程并行搜索数字"5"在矩阵中出现的次数
 * - 每个线程负责2000行（10000/5）
 * - 所有搜索线程完成后，Grouper 汇总统计最终结果
 */
public class Main {
    public static void main(String[] args) {

        // ========== 配置参数 ==========
        final int ROWS = 10000;           // 矩阵的行数
        final int NUMBERS = 1000;         // 每行的列数
        final int SEARCH = 5;             // 要查找的目标数字
        final int PARTICIPANTS = 5;       // 搜索线程的数量
        final int LINES_PARTICIPANT = 2000; // 每个线程处理的行数

        // ========== 步骤1：创建模拟数据 ==========
        // 创建一个 10000x1000 的矩阵，随机填充0-9的数字
        // 构造函数会统计目标数字出现的总次数并打印
        MatrixMock mock = new MatrixMock(ROWS, NUMBERS, SEARCH);

        // ========== 步骤2：创建结果存储 ==========
        // Results 用于存储每个搜索线程的结果
        // 每个元素对应矩阵的一行，记录该行中找到目标数字的次数
        Results results = new Results(ROWS);

        // ========== 步骤3：创建结果汇总任务 ==========
        // Grouper 实现了 Runnable，将在所有搜索线程完成后执行
        // 用于汇总所有搜索结果并计算总出现次数
        Grouper grouper = new Grouper(results);

        // ========== 步骤4：创建循环屏障 ==========
        // CyclicBarrier 初始化为5个参与者
        // 当5个线程都调用 await() 后，屏障打开
        // 然后会执行 Grouper（第二个参数），完成结果汇总
        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS, grouper);

        // ========== 步骤5：创建并启动搜索线程 ==========
        Searcher[] searchers = new Searcher[PARTICIPANTS];
        for (int i = 0; i < searchers.length; i++) {
            // 每个 Searcher 负责搜索矩阵的特定行范围
            // 例如：Searcher 0 处理 0-1999 行，Searcher 1 处理 2000-3999 行，以此类推
            searchers[i] = new Searcher(
                    barrier,
                    i * LINES_PARTICIPANT,
                    (i * LINES_PARTICIPANT) + LINES_PARTICIPANT,
                    mock,
                    results,
                    PARTICIPANTS);

            Thread thread = new Thread(searchers[i]);
            thread.start();
        }

        // 主线程打印完成消息后退出
        // 注意：主线程退出后，搜索线程仍在继续运行
        System.out.printf("Main: The main thread has finished.\n");
    }
}
