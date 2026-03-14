package com.ssitao.code.designpattern.singleton;

/**
 * 饿汉式单例模式
 *
 * 优点：
 * 1. 线程安全
 * 2. 简单易实现
 *
 * 缺点：
 * 1. 类加载时就实例化，可能造成资源浪费
 * 2. 无法传递参数
 */
public class HungrySingleton {

    // 类加载时就创建实例
    private static final HungrySingleton INSTANCE = new HungrySingleton();

    // 私有构造函数
    private HungrySingleton() {
    }

    public static HungrySingleton getInstance() {
        return INSTANCE;
    }

    public void showMessage() {
        System.out.println("饿汉式单例: " + this.hashCode());
    }
}
