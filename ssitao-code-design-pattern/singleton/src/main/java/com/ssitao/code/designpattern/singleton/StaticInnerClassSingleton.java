package com.ssitao.code.designpattern.singleton;

/**
 * 静态内部类单例模式
 *
 * 优点：
 * 1. 延迟加载
 * 2. 线程安全(JVM保证)
 * 3. 性能好
 * 4. 代码简洁
 *
 * 缺点：
 * 1. 无法传递参数
 */
public class StaticInnerClassSingleton {

    private StaticInnerClassSingleton() {
    }

    // 静态内部类，在getInstance时加载
    private static class SingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void showMessage() {
        System.out.println("静态内部类单例: " + this.hashCode());
    }
}
