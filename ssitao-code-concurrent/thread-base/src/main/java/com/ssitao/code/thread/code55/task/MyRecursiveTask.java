package com.ssitao.code.thread.code55.task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * 递归任务类 - 使用 Fork/Join 框架计算数组总和
 *
 * 核心设计：
 * 1. 继承 RecursiveTask<Integer>，支持返回值的递归任务
 * 2. 使用分治策略：将大数组拆分为小数组，并行计算后合并结果
 * 3. 阈值设定：当数组范围 <= 100 时，直接计算；否则拆分
 *
 * Fork/Join 核心方法：
 * - fork(): 异步提交任务到线程池
 * - join(): 等待任务完成并获取结果
 * - invokeAll(task1, task2): 同时提交两个任务并等待完成
 *
 * 分治策略：
 * - 任务范围 > 100: 拆分为两个子任务，递归处理
 * - 任务范围 <= 100: 直接遍历计算
 *
 * 示例：100000个元素
 * - 第一层: 1个任务 → 2个子任务（各50000）
 * - 第二层: 2个任务 → 4个子任务（各25000）
 * - ...
 * - 直到每个任务 <= 100个元素
 * - 然后逐层合并结果
 */
public class MyRecursiveTask extends RecursiveTask<Integer> {

    /**
     * 要计算的数组
     */
    private int array[];

    /**
     * 计算范围的起始索引（包含）
     */
    private int start;

    /**
     * 计算范围的结束索引（不包含）
     */
    private int end;

    /**
     * 阈值：当范围大小 <= 此值时，直接计算而不拆分
     */
    private static final int THRESHOLD = 100;

    /**
     * 构造函数
     *
     * @param array 要计算的数组
     * @param start 计算范围的起始索引（包含）
     * @param end   计算范围的结束索引（不包含）
     */
    public MyRecursiveTask(int array[], int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    /**
     * 任务执行方法（核心）
     *
     * 分治逻辑：
     * 1. 如果范围大于阈值，拆分为两个子任务
     * 2. 使用 invokeAll 提交两个子任务
     * 3. 等待子任务完成后，合并结果
     * 4. 如果范围小于等于阈值，直接遍历计算
     *
     * @return 计算结果（数组元素的和）
     */
    @Override
    protected Integer compute() {
        Integer ret;

        // 获取当前工作线程，添加任务计数
        MyWorkerThread thread = (MyWorkerThread) Thread.currentThread();
        thread.addTask();

        // 计算范围大小
        int range = end - start;

        // 如果范围大于阈值，拆分任务
        if (range > THRESHOLD) {
            // 计算中间位置
            int mid = (start + end) / 2;

            // 创建两个子任务
            MyRecursiveTask task1 = new MyRecursiveTask(array, start, mid);
            MyRecursiveTask task2 = new MyRecursiveTask(array, mid, end);

            // 提交两个任务并等待完成
            // fork() 异步提交，invokeAll() 等待两个任务都完成
            invokeAll(task1, task2);

            // 合并两个子任务的结果
            ret = addResults(task1, task2);
        } else {
            // 范围较小，直接计算
            int add = 0;
            for (int i = start; i < end; i++) {
                add += array[i];
            }
            ret = new Integer(add);
        }

        // 模拟任务处理时间（10毫秒）
        // 实际应用中这行可以移除，这里只是为了演示任务执行
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * 合并两个子任务的结果
     *
     * @param task1 第一个子任务
     * @param task2 第二个子任务
     * @return 两个子任务结果之和
     */
    private Integer addResults(MyRecursiveTask task1, MyRecursiveTask task2) {
        int value;
        try {
            // get() 阻塞等待任务完成并获取结果
            value = task1.get().intValue() + task2.get().intValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
            value = 0;
        } catch (ExecutionException e) {
            e.printStackTrace();
            value = 0;
        }
        return new Integer(value);
    }
}
