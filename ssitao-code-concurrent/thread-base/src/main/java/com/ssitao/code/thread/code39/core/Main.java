package com.ssitao.code.thread.code39.core;
/**
 * Fork/Join框架示例 - 并行处理产品价格更新
 *
 * 本程序演示Java Fork/Join框架的使用：
 * 1. 使用ForkJoinPool创建线程池
 * 2. 使用RecursiveAction实现分治任务（无返回值）
 * 3. 任务会被递归拆分，直到子任务处理的产品数量少于10个
 *
 * 工作原理：
 * - ForkJoinPool使用工作窃取(work-stealing)算法
 * - 空闲线程会从繁忙线程的工作队列中"窃取"任务
 * - 这样可以充分利用多核CPU资源
 */


import com.ssitao.code.thread.code39.task.Task;
import com.ssitao.code.thread.code39.utils.Product;
import com.ssitao.code.thread.code39.utils.ProductListGenerator;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // ==================== 1. 生成测试数据 ====================
        // 创建产品生成器对象，并且生产10000个产品
        // 每个产品初始价格为10，名称为"Product 0"到"Product 9999"
        ProductListGenerator generator = new ProductListGenerator();
        List<Product> products = generator.generate(10000);

        // ==================== 2. 创建Fork/Join任务 ====================
        // 创建一个任务对象，处理全部10000个产品，价格增长率20%(0.2)
        // 参数说明：
        //   - products: 产品列表
        //   - 0: 处理的起始位置
        //   - products.size(): 处理的结束位置（不包含）
        //   - 0.2: 价格增长率（每个产品价格变为原来的1.2倍）
        Task task = new Task(products, 0, products.size(), 0.2);

        // ==================== 3. 创建ForkJoinPool线程池 ====================
        // ForkJoinPool是专门为Fork/Join任务设计的线程池
        // 它使用工作窃取算法来平衡所有线程的工作负载
        ForkJoinPool pool = new ForkJoinPool();

        // ==================== 4. 异步执行任务 ====================
        // execute()方法异步执行任务，不会阻塞当前线程
        pool.execute(task);

        // ==================== 5. 监控线程池状态 ====================
        // 在任务执行期间，定期输出线程池的运行状态信息
        // - getActiveThreadCount(): 当前活跃线程数
        // - getStealCount(): 线程从其他线程窃取任务的总次数
        // - getParallelism(): 并行级别（等于CPU核心数）
        do {
            System.out.printf("Main: Thread Count: %d\n", pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal: %d\n", pool.getStealCount());
            System.out.printf("Main: Paralelism: %d\n", pool.getParallelism());
            try {
                // 每5毫秒输出一次状态
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 循环直到任务完成（isDone()返回true）
        } while (!task.isDone());

        // ==================== 6. 关闭线程池 ====================
        // 关闭ForkJoinPool，不再接受新任务，但会等待已提交的任务完成
        pool.shutdown();

        // ==================== 7. 验证执行结果 ====================
        // 检查任务是否正常完成（而非被中断或异常终止）
        if (task.isCompletedNormally()) {
            System.out.printf("Main: The process has completed normally.\n");
        }

        // ==================== 8. 验证价格更新正确性 ====================
        // 由于价格增长率为0.2，所以：
        // 原始价格: 10
        // 增长后价格: 10 * (1 + 0.2) = 12
        // 遍历产品列表，检查是否有产品的价格不是12（用于验证并行处理是否正确）
        for (Product product : products) {
            if (product.getPrice() != 12) {
                System.out.printf("Product %s: %f\n", product.getName(), product.getPrice());
            }
        }

        // ==================== 9. 程序结束 ====================
        System.out.println("Main: End of the program.\n");
    }
}
