package com.ssitao.code.thread.code40.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * 文件夹处理任务类：使用 Fork/Join 框架递归搜索文件
 *
 * Fork/Join 核心机制：
 * - RecursiveTask<V>: 带返回值的递归任务类型
 * - fork(): 异步提交任务到线程池，继续执行其他任务（不阻塞）
 * - join(): 等待任务完成并获取结果
 *
 * 工作流程：
 * 1. 扫描当前目录，发现子文件夹时创建新的 FolderProcessor 任务
 * 2. 通过 fork() 异步执行子任务，实现并行处理
 * 3. 通过 join() 收集子任务结果并合并
 */
public class FolderProcessor extends RecursiveTask<List<String>> {
    private static final long serialVersionUID = -6119741136325003142L;

    /**
     * 要搜索的起始目录路径
     */
    private String path;

    /**
     * 要匹配的文件扩展名（如 "log" 表示匹配 *.log 文件）
     */
    private String extension;

    /**
     * 构造函数
     *
     * @param path      起始搜索目录
     * @param extension 要匹配的文件扩展名
     */
    public FolderProcessor(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    /**
     * 核心计算方法：递归扫描目录并查找匹配的文件
     *
     * Fork/Join 执行流程：
     * 1. 遍历目录内容
     * 2. 如果是子文件夹 -> 创建新的 FolderProcessor 任务并 fork()
     * 3. 如果是文件 -> 检查扩展名是否匹配
     * 4. 调用 addResultsFromTasks() 合并所有子任务的结果
     *
     * @return 匹配的文件绝对路径列表
     */
    @Override
    protected List<String> compute() {
        List<String> list = new ArrayList<>();
        // 用于存储所有子任务，以便后续合并结果
        List<FolderProcessor> tasks = new ArrayList<>();

        File file = new File(path);
        File content[] = file.listFiles();

        if (content != null) {
            for (int i = 0; i < content.length; i++) {
                if (content[i].isDirectory()) {
                    // ========== 发现子目录：Fork一个新任务 ==========
                    // 为子目录创建新的 FolderProcessor 任务
                    FolderProcessor task = new FolderProcessor(
                        content[i].getAbsolutePath(),
                        extension
                    );
                    // fork(): 异步提交任务，立即返回，不阻塞当前线程
                    // 这样多个子目录可以并行扫描
                    task.fork();
                    tasks.add(task);
                } else {
                    // ========== 发现文件：检查是否匹配 ==========
                    if (checkFile(content[i].getName())) {
                        list.add(content[i].getAbsolutePath());
                    }
                }
            }

            // 当子任务数量过多时打印统计信息（帮助观察任务拆分情况）
            if (tasks.size() > 50) {
                System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), tasks.size());
            }

            // ========== 合并子任务结果 ==========
            addResultsFromTasks(list, tasks);
        }

        return list;
    }

    /**
     * 汇总所有子任务的结果
     *
     * 工作原理：
     * - 遍历所有子任务
     * - 对每个子任务调用 join() 阻塞等待其完成并返回结果
     * - 将子任务的结果合并到当前任务的结果列表中
     *
     * @param list  当前任务的结果列表（用于收集子任务结果）
     * @param tasks 子任务列表
     */
    private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
        for (FolderProcessor item : tasks) {
            // join(): 阻塞等待任务完成，返回任务compute()的返回值
            list.addAll(item.join());
        }
    }

    /**
     * 检查文件名是否以指定扩展名结尾
     *
     * @param name 文件名
     * @return true 如果文件扩展名匹配
     */
    private boolean checkFile(String name) {
        return name.endsWith(extension);
    }
}
