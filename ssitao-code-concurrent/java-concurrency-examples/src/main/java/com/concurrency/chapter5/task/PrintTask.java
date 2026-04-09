package com.concurrency.chapter5.task;

import java.util.concurrent.RecursiveAction;

/**
 * 打印任务 - 使用RecursiveAction（无返回值）
 */
public class PrintTask extends RecursiveAction {

    private static final int THRESHOLD = 5;
    private final String taskName;
    private final int depth;

    public PrintTask(String taskName, int depth) {
        this.taskName = taskName;
        this.depth = depth;
    }

    @Override
    protected void compute() {
        if (depth <= THRESHOLD) {
            System.out.println(indent(depth) + "任务[" + taskName + "] depth=" + depth +
                    " 由 " + Thread.currentThread().getName() + " 执行");
            return;
        }

        // 分成两个子任务
        PrintTask leftTask = new PrintTask(taskName + "-L", depth / 2);
        PrintTask rightTask = new PrintTask(taskName + "-R", depth / 2);

        leftTask.fork();
        rightTask.fork();

        leftTask.join();
        rightTask.join();
    }

    private static String indent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
}
