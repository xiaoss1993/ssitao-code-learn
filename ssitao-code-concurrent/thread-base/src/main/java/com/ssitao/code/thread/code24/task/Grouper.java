package com.ssitao.code.thread.code24.task;


import com.ssitao.code.thread.code24.utils.Results;

/**
 * 结果汇总类
 *
 * 实现了 Runnable 接口，当作 CyclicBarrier 的 Runnable 参数传入。
 * 当所有 Searcher 线程都到达屏障点后，CyclicBarrier 会自动执行此 Runnable。
 *
 * 作用：
 * - 汇总所有 Searcher 线程的搜索结果
 * - 计算目标数字在矩阵中出现的总次数
 */
public class Grouper implements Runnable {
    /**
     * 结果对象引用
     * 存储了所有搜索线程的部分结果
     */
    private Results results;

    /**
     * 构造函数
     *
     * @param results 结果对象
     */
    public Grouper(Results results) {
        this.results = results;
    }

    /**
     * 核心方法：汇总所有搜索结果
     *
     * 执行流程：
     * 1. 从 Results 对象获取所有行的搜索结果数组
     * 2. 遍历数组，累加每行的计数
     * 3. 打印最终的总计数
     *
     * 注意：此方法在线程被唤醒后自动执行，不需要手动调用
     */
    @Override
    public void run() {
        int finalResult = 0;

        System.out.printf("Grouper: Processing results...\n");

        // 获取所有行的搜索结果
        int data[] = results.getData();

        // 累加所有行的计数，得到目标数字在矩阵中的总出现次数
        for (int number : data) {
            finalResult += number;
        }

        System.out.printf("Grouper: Total result: %d.\n", finalResult);
    }
}