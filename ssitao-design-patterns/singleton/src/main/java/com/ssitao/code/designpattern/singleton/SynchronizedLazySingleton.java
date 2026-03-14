package com.ssitao.code.designpattern.singleton;

/**
 * 懒汉式单例模式 - 线程安全(synchronized方法)
 *
 * 优点：
 * 1. 延迟加载
 * 2. 线程安全
 *
 * 缺点：
 * 1. 性能较差，每次获取实例都要同步
 */
public class SynchronizedLazySingleton {

    private static SynchronizedLazySingleton INSTANCE;

    private SynchronizedLazySingleton() {
    }

    // 同步方法，线程安全但性能差
    public static synchronized SynchronizedLazySingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SynchronizedLazySingleton();
        }
        return INSTANCE;
    }

    public void showMessage() {
        System.out.println("懒汉式单例(同步方法): " + this.hashCode());
    }
}
