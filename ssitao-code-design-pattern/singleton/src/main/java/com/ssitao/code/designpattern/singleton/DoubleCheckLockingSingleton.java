package com.ssitao.code.designpattern.singleton;

/**
 * 双重检查锁(DCL)单例模式
 *
 * 优点：
 * 1. 延迟加载
 * 2. 线程安全
 * 3. 性能较好
 *
 * 缺点：
 * 1. 代码较复杂
 * 2. JDK1.5之前有序列化问题
 */
public class DoubleCheckLockingSingleton {

    // volatile保证可见性和禁止指令重排序
    private static volatile DoubleCheckLockingSingleton INSTANCE;

    private DoubleCheckLockingSingleton() {
    }

    public static DoubleCheckLockingSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (DoubleCheckLockingSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckLockingSingleton();
                }
            }
        }
        return INSTANCE;
    }

    public void showMessage() {
        System.out.println("双重检查锁单例: " + this.hashCode());
    }
}
