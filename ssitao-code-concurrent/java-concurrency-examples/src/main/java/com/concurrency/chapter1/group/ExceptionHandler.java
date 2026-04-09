package com.concurrency.chapter1.group;

/**
 * 线程组中不可控制异常的处理示例
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("========================================");
        System.out.println("线程组异常处理器捕获到异常:");
        System.out.println("  线程名称: " + t.getName());
        System.out.println("  线程组: " + t.getThreadGroup().getName());
        System.out.println("  异常类型: " + e.getClass().getName());
        System.out.println("  异常信息: " + e.getMessage());
        System.out.println("========================================");
    }

    public static void handlerDemo() {
        // 创建带有自定义异常处理器的线程组
        ThreadGroup group = new ThreadGroup("CustomGroup") {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("组级别的异常处理:");
                System.out.println("  线程: " + t.getName() + " 发生异常: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(group, () -> {
            throw new RuntimeException("测试异常1");
        }, "ErrorThread-1");

        Thread t2 = new Thread(group, () -> {
            System.out.println("正常线程执行");
        }, "NormalThread");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
