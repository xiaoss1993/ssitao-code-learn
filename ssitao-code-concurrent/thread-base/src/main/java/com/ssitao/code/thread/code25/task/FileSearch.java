package com.ssitao.code.thread.code25.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 文件查找类 - Phaser多阶段同步演示
 *
 * 该类实现了在指定目录及其子目录中搜索符合条件文件的功能。
 * 使用Phaser进行三个阶段的同步控制：
 *
 * 阶段划分：
 * 1. 查找阶段：递归遍历目录树，查找所有指定扩展名的文件
 * 2. 过滤阶段：将查找结果过滤，只保留24小时内修改过的文件
 * 3. 显示阶段：打印最终结果
 *
 * Phaser关键方法说明：
 * - arriveAndAwaitAdvance()：到达同步点，等待其他参与者，完成当前阶段后阻塞自己直到下一阶段开始
 * - arriveAndDeregister()：到达同步点，完成当前阶段后注销自己，不再参与后续阶段
 * - getPhase()：获取当前处于哪个阶段（从0开始计数）
 */
public class FileSearch implements Runnable {
    /**
     * 用于查找的起始文件夹路径
     */
    private String initPath;
    /**
     * 要查找的文件的扩展名（如"log"表示查找.log文件）
     */
    private String end;
    /**
     * 查找到的文件的完整路径列表
     */
    private List<String> results;
    /**
     * Phaser阶段同步器，用于控制多线程在各阶段的同步
     */
    private Phaser phaser;

    /**
     * 构造函数，初始化文件搜索任务的配置参数
     *
     * @param initPath 用于查找的起始文件夹
     * @param end      要查找的文件的扩展名
     * @param phaser   阶段同步器，用于线程间同步
     */
    public FileSearch(String initPath, String end, Phaser phaser) {
        this.initPath = initPath;
        this.end = end;
        this.phaser = phaser;
        this.results = new ArrayList<>();
    }

    /**
     * 线程执行入口
     *
     * 任务执行流程：
     * 1. 调用arriveAndAwaitAdvance()等待其他线程创建完成（初始同步点）
     * 2. 执行第一阶段：递归查找所有匹配的文件
     * 3. 调用checkResults()检查并同步（无结果则退出）
     * 4. 执行第二阶段：过滤24小时内修改的文件
     * 5. 调用checkResults()检查并同步（无结果则退出）
     * 6. 执行第三阶段：打印结果
     * 7. 调用arriveAndDeregister()完成并注销，不再参与后续阶段
     */
    @Override
    public void run() {
        // 第一次同步：等待所有线程都创建完毕，确保三个搜索任务同时开始
        // arriveAndAwaitAdvance会使当前线程阻塞，直到所有注册的参与者都到达此同步点
        this.phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting.\n", Thread.currentThread().getName());

        // ========== 第一阶段：文件查找 ==========
        // 从起始目录开始，递归遍历所有子目录，查找匹配的文件
        File file = new File(initPath);
        if (file.isDirectory()) {
            directoryProcess(file);
        }

        // 第一阶段结束检查：等待所有线程完成查找，并决定是否继续
        if (!checkResults()) {
            // 没有找到任何匹配文件，线程退出（已通过arriveAndDeregister注销）
            return;
        }

        // ========== 第二阶段：结果过滤 ==========
        // 过滤掉24小时之前修改的文件，只保留最近24小时内修改的文件
        filterResults();

        // 第二阶段结束检查：等待所有线程完成过滤，并决定是否继续
        if (!checkResults()) {
            // 过滤后没有结果，线程退出（已通过arriveAndDeregister注销）
            return;
        }

        // ========== 第三阶段：结果展示 ==========
        showInfo();

        // 所有阶段完成，从Phaser注销，不再参与可能的更多阶段
        // 此时Phaser的已注册参与者数减1，当减为0时Phaser终止
        phaser.arriveAndDeregister();
        System.out.printf("%s: Work completed.\n", Thread.currentThread().getName());
    }

    /**
     * 展示搜索结果
     *
     * 将过滤后的文件路径逐个打印到控制台。
     * 打印完成后调用arriveAndAwaitAdvance()等待其他线程完成打印，
     * 确保所有线程都完成第三阶段后才算真正结束。
     */
    private void showInfo() {
        for (String result : this.results) {
            File file = new File(result);
            System.out.printf("%s: %s\n", Thread.currentThread().getName(), file.getAbsolutePath());
        }

        // 通知Phaser当前线程已完成第三阶段，然后阻塞自己等待其他线程
        this.phaser.arriveAndAwaitAdvance();
    }

    /**
     * 检查当前阶段结果并处理同步
     *
     * 该方法是Phaser同步的关键：
     * - 如果结果集为空：说明当前线程任务失败，调用arriveAndDeregister()注销自己，
     *   并返回false通知调用者停止执行后续阶段
     * - 如果结果集非空：调用arriveAndAwaitAdvance()等待其他线程到达此同步点，
     *   所有线程都到达后，Phaser放行所有线程进入下一阶段
     *
     * @return true结果集非空可继续执行，false结果集为空应退出
     */
    private boolean checkResults() {
        if (this.results.isEmpty()) {
            // 当前阶段没有找到任何结果，通知Phaser并注销
            System.out.printf("%s: Phase %d: 0 results.\n", Thread.currentThread().getName(), phaser.getPhase());
            System.out.printf("%s: Phase %d: End.\n", Thread.currentThread().getName(), phaser.getPhase());
            // arriveAndDeregister：完成当前阶段并注销，不再参与后续阶段
            this.phaser.arriveAndDeregister();
            return false;
        } else {
            // 找到了一些结果，输出数量并等待其他线程
            System.out.printf("%s: Phase %d: %d results.\n", Thread.currentThread().getName(), phaser.getPhase(), results.size());
            // arriveAndAwaitAdvance：完成当前阶段并阻塞，等待所有线程完成此阶段
            this.phaser.arriveAndAwaitAdvance();
            return true;
        }
    }

    /**
     * 过滤查找结果
     *
     * 将结果列表中的文件与当前时间比较，只保留在24小时内修改过的文件。
     * 这是第二阶段的核心逻辑。
     */
    private void filterResults() {
        List<String> newResults = new ArrayList<>();
        long actualDate = new Date().getTime(); // 获取当前时间戳

        for (String result : results) {
            File file = new File(result);
            long fileDate = file.lastModified(); // 获取文件最后修改时间
            // 如果文件在24小时内被修改过（当前时间 - 文件修改时间 < 24小时）
            if (actualDate - fileDate < TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)) {
                newResults.add(result);
            }
        }
        // 用过滤后的结果替换原结果
        results = newResults;
    }

    /**
     * 递归处理目录
     *
     * 深度优先遍历目录树：
     * - 如果是目录，递归调用自己处理子目录
     * - 如果是文件，调用fileProcess()处理
     *
     * @param file 要处理的目录
     */
    private void directoryProcess(File file) {
        // 获取该目录下所有文件和子目录
        File list[] = file.listFiles();
        if (list != null) {
            for (File aFile : list) {
                if (aFile.isDirectory()) {
                    // 目录：递归处理
                    directoryProcess(aFile);
                } else {
                    // 文件：检查是否匹配搜索条件
                    fileProcess(aFile);
                }
            }
        }
    }

    /**
     * 处理单个文件
     *
     * 检查文件名是否以指定的扩展名结尾，如果是则加入结果列表。
     *
     * @param file 要检查的文件
     */
    private void fileProcess(File file) {
        // endsWith("log") 会匹配 "test.log" 但不会匹配 "test.logback"
        // 为避免这种问题，通常应该用 endsWith("." + end) 或使用其他匹配方式
        if (file.getName().endsWith(end)) {
            results.add(file.getAbsolutePath());
        }
    }
}
