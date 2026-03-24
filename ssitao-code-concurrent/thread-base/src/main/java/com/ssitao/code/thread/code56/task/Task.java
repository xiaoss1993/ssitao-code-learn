package com.ssitao.code.thread.code56.task;

/**
 * 具体任务类 - 继承 MyWorkerTask 实现数组元素递增
 *
 * 核心设计：
 * 1. 继承 MyWorkerTask，使用 ForkJoinTask 底层 API
 * 2. 实现分治策略：范围 > 100 时拆分，否则直接处理
 * 3. 每个任务将数组指定范围内的元素 +1
 *
 * 分治策略：
 * - 范围 > 100: 拆分为两个子任务，递归处理
 * - 范围 <= 100: 遍历并递增元素值，模拟耗时操作
 *
 * 与 RecursiveAction 的区别：
 * - RecursiveTask<Void> 继承自 ForkJoinTask<Void>
 * - 直接继承 ForkJoinTask<Void> 绕过便捷抽象类
 * - 更底层，控制更精细
 *
 * 示例：10000个元素
 * - 初始: [0, 10000)
 * - 逐层拆分直到每段 <= 100
 * - 每段内所有元素 +1
 * - 最终所有元素从 0 变为 1
 */
public class Task extends MyWorkerTask {

    /**
     * 序列化版本ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 要处理的数组
     */
    private int array[];

    /**
     * 处理范围的起始索引（包含）
     */
    private int start;

    /**
     * 处理范围的结束索引（不包含）
     */
    private int end;

    /**
     * 阈值：当范围大小 <= 此值时，直接处理而不拆分
     */
    private static final int THRESHOLD = 100;

    /**
     * 构造函数
     *
     * @param name  任务名称
     * @param array 要处理的数组
     * @param start 处理范围的起始索引（包含）
     * @param end   处理范围的结束索引（不包含）
     */
    public Task(String name, int array[], int start, int end) {
        super(name);
        this.array = array;
        this.start = start;
        this.end = end;
    }

    /**
     * 具体计算逻辑
     *
     * 分治策略：
     * 1. 如果范围大于阈值，拆分为两个子任务
     * 2. 使用 invokeAll 同时提交两个子任务
     * 3. 等待子任务完成后结束
     * 4. 如果范围小于等于阈值，直接遍历递增元素
     */
    @Override
    protected void compute() {
        // 计算范围大小
        int range = end - start;

        // 如果范围大于阈值，拆分任务
        if (range > THRESHOLD) {
            // 计算中间位置
            int mid = (end + start) / 2;

            // 创建两个子任务
            Task task1 = new Task(this.getName() + "1", array, start, mid);
            Task task2 = new Task(this.getName() + "2", array, mid, end);

            // 提交两个任务并等待完成
            // fork() 异步提交，invokeAll() 等待两个任务都完成
            invokeAll(task1, task2);

        } else {
            // 范围较小，直接处理
            // 将范围内的每个元素 +1
            for (int i = start; i < end; i++) {
                array[i]++;
            }

            // 模拟耗时操作
            // 实际应用中这行可以移除
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
