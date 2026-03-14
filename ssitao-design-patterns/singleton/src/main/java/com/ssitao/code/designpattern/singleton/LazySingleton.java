package com.ssitao.code.designpattern.singleton;

/**
 * 懒汉式单例模式 - 线程不安全
 *
 * 优点：
 * 1. 延迟加载，按需创建
 *
 * 缺点：
 * 1. 线程不安全
 */
public class LazySingleton {

    private static LazySingleton INSTANCE;

    private LazySingleton() {
    }

    public static LazySingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LazySingleton();
        }
        return INSTANCE;
    }

    public void showMessage() {
        System.out.println("懒汉式单例(线程不安全): " + this.hashCode());
    }
}
