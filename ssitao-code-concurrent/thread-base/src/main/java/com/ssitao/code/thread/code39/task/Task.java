package com.ssitao.code.thread.code39.task;

import com.ssitao.code.thread.code39.utils.Product;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * Fork/Join任务类 - 继承RecursiveAction
 *
 * RecursiveAction是Fork/Join框架中用于无返回值任务的抽象类。
 * 当任务需要返回结果时，应使用RecursiveTask<T>。
 *
 * 本任务实现分治算法：
 * 1. 如果要处理的产品数量少于10个，直接执行价格更新
 * 2. 否则，将任务拆分成两个子任务，递归处理
 *
 * @param <T> 产品列表类型
 */
public class Task extends RecursiveAction {

    /**
     * 产品集合对象 - 所有产品共享同一个列表引用
     */
    private List<Product> products;
    /**
     * 处理的第一个产品位置（起始索引，包含）
     */
    private int first;
    /**
     * 处理的最后一个产品位置（结束索引，不包含）
     */
    private int last;
    /**
     * 价格增长率
     * 例如：0.2表示价格上涨20%，即 price * 1.2
     */
    private double increment;

    /**
     * 构造函数，初始化任务属性
     *
     * @param products  产品集合对象（共享数据）
     * @param first     处理的起始位置（包含）
     * @param last      处理的结束位置（不包含）
     * @param increment 价格增长率
     */
    public Task(List<Product> products, int first, int last, double increment) {
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    /**
     * 核心计算方法 - Fork/Join框架会自动调用此方法
     *
     * 实现分治算法：
     * 1. 判断任务规模：如果处理的产品数量少于10个，直接更新价格
     * 2. 否则，拆分为两个子任务，递归处理
     * 3. 使用invokeAll()方法同时执行两个子任务
     */
    @Override
    protected void compute() {
        // 计算要处理的产品数量
        if (last - first < 10) {
            // ========== 终止条件：产品数量少于10个 ==========
            // 当任务足够小时，直接执行价格更新操作
            // 不再继续拆分，减少线程创建和管理的开销
            updatePrices();
        } else {
            // ========== 递归拆分：产品数量 >= 10个 ==========
            // 将任务拆分成两个相等的子任务

            // 计算中间位置
            int middle = (first + last) / 2;

            // 打印当前挂起任务数量，帮助观察任务拆分情况
            System.out.printf("Task: Pending tasks: %s\n", getQueuedTaskCount());

            // 创建左子任务：处理[first, middle+1)范围
            Task t1 = new Task(products, first, middle + 1, increment);
            // 创建右子任务：处理[middle+1, last)范围
            Task t2 = new Task(products, middle + 1, last, increment);

            // invokeAll()方法：
            // 1. 使用fork()异步执行子任务
            // 2. 在当前线程同步等待所有子任务完成
            // 这确保了父任务在子任务完成前不会返回
            invokeAll(t1, t2);
        }
    }

    /**
     * 更新产品价格 - 实际执行价格增长操作
     *
     * 遍历指定范围内的所有产品，按increment比率更新价格
     * 例如：increment=0.2 表示价格上涨20%
     *   原价: 10 -> 新价: 10 * (1 + 0.2) = 12
     */
    private void updatePrices() {
        for (int i = first; i < last; i++) {
            Product product = products.get(i);
            // 计算新价格：原价 * (1 + 增长率)
            product.setPrice(product.getPrice() * (1 + increment));
        }
    }
}
