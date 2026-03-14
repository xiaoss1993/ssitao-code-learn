package com.ssitao.code.designpattern.singleton;

/**
 * ThreadLocal单例模式
 *
 * 优点：
 * 1. 线程内唯一
 * 2. 线程安全
 *
 * 缺点：
 * 1. 每个线程都有独立实例
 */
public class ThreadLocalSingleton {

    private static final ThreadLocal<ThreadLocalSingleton> THREAD_LOCAL =
        new ThreadLocal<ThreadLocalSingleton>() {
            @Override
            protected ThreadLocalSingleton initialValue() {
                return new ThreadLocalSingleton();
            }
        };

    private ThreadLocalSingleton() {
    }

    public static ThreadLocalSingleton getInstance() {
        return THREAD_LOCAL.get();
    }

    public void showMessage() {
        System.out.println("ThreadLocal单例, 线程: " + Thread.currentThread().getName() +
                           ", HashCode: " + this.hashCode());
    }
}
