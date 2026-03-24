package com.ssitao.code.thread.code54.task;

import java.util.concurrent.TimeUnit;

/**
 * 任务类 - 简单的 Runnable 实现
 *
 * 实现 Runnable 接口，作为定时线程池执行的任务
 *
 * 任务逻辑：
 * - 打印任务开始信息
 * - 睡眠2秒，模拟耗时操作
 * - 打印任务结束信息
 *
 * 设计目的：
 * - 作为 ScheduledThreadPoolExecutor 的任务示例
 * - 用于测试一次性任务和周期任务的执行
 */
public class Task implements Runnable {

	/**
	 * 任务执行方法
	 *
	 * 打印开始信息，睡眠2秒，打印结束信息
	 */
	@Override
	public void run() {
		// 打印任务开始信息
		System.out.printf("Task: Begin.\n");
		try {
			// 睡眠2秒，模拟耗时操作
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 打印任务结束信息
		System.out.printf("Task: End.\n");
	}
}
