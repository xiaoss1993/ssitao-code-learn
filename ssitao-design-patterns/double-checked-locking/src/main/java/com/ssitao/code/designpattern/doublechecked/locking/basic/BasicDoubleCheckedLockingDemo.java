package com.ssitao.code.designpattern.doublechecked.locking.basic;

/**
 * 双重检查锁定（DCL）基础示例
 *
 * 双重检查锁定（Double-Checked Locking）是一种并发设计模式，用于：
 * 1. 减少锁竞争，提高性能
 * 2. 实现线程安全的延迟初始化
 *
 * 核心原理：
 * - 第一次检查：不需要加锁，判断对象是否已创建
 * - 第二次检查：需要加锁，确保只有一个线程创建对象
 *
 * 为什么要双重检查？
 * - 如果只加锁：每次获取实例都要加锁，性能差
 * - 如果不双重检查：多个线程可能同时创建多个实例
 *
 * 注意：DCL在JDK5之前存在bug，需要使用volatile修饰
 *
 * @author ssitao
 */
public class BasicDoubleCheckedLockingDemo {

    public static void main(String[] args) {
        System.out.println("=== 双重检查锁定基础示例 ===\n");

        // 示例1：单例模式的多种实现对比
        System.out.println("1. 单例模式实现对比");
        singletonCompare();

        // 示例2：DCL原理演示
        System.out.println("\n2. DCL原理演示");
        dclPrincipleDemo();
    }

    /**
     * 单例模式实现对比
     * 展示不同实现方式的线程安全性和性能
     */
    private static void singletonCompare() {
        System.out.println("--- 饿汉式 ---");
        // 优点：线程安全，简洁
        // 缺点：类加载时就初始化，可能造成资源浪费
        System.out.println("EagerSingleton.getInstance(): " + EagerSingleton.getInstance());
        System.out.println("EagerSingleton.getInstance(): " + EagerSingleton.getInstance());
        System.out.println("两次获取相同实例: " + (EagerSingleton.getInstance() == EagerSingleton.getInstance()));

        System.out.println("\n--- 懒汉式（同步方法）---");
        // 优点：延迟初始化
        // 缺点：每次获取实例都要同步，性能差
        System.out.println("LazySingletonSync.getInstance(): " + LazySingletonSync.getInstance());
        System.out.println("LazySingletonSync.getInstance(): " + LazySingletonSync.getInstance());

        System.out.println("\n--- 双重检查锁定 ---");
        // 优点：延迟初始化 + 高性能
        // 缺点：代码稍复杂
        System.out.println("DCLSingleton.getInstance(): " + DCLSingleton.getInstance());
        System.out.println("DCLSingleton.getInstance(): " + DCLSingleton.getInstance());

        System.out.println("\n--- 静态内部类 ---");
        // 优点：延迟初始化 + 高性能 + 代码简洁
        // 推荐使用
        System.out.println("StaticInnerClassSingleton.getInstance(): " + StaticInnerClassSingleton.getInstance());
        System.out.println("StaticInnerClassSingleton.getInstance(): " + StaticInnerClassSingleton.getInstance());

        System.out.println("\n--- 枚举（推荐）---");
        // 优点：线程安全 + 防止反射攻击 + 防止序列化攻击
        System.out.println("EnumSingleton.INSTANCE: " + EnumSingleton.INSTANCE);
        System.out.println("EnumSingleton.INSTANCE: " + EnumSingleton.INSTANCE);
    }

    /**
     * DCL原理演示
     * 展示DCL的工作流程
     */
    private static void dclPrincipleDemo() {
        // 演示多线程环境下DCL的行为
        final boolean[] created = {false};

        Thread t1 = new Thread(() -> {
            DCLObject obj = DCLObject.getInstance();
            System.out.println("线程1创建对象: " + obj);
            created[0] = true;
        }, "线程1");

        Thread t2 = new Thread(() -> {
            // 等待线程1开始创建
            while (!created[0]) {
                Thread.yield();
            }
            DCLObject obj = DCLObject.getInstance();
            System.out.println("线程2获取对象: " + obj);
        }, "线程2");

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

/**
 * 饿汉式单例
 * 类加载时就创建实例
 */
class EagerSingleton {
    // 类加载时就创建实例
    private static final EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
        System.out.println("EagerSingleton 构造函数被调用");
    }

    public static EagerSingleton getInstance() {
        return instance;
    }
}

/**
 * 懒汉式（同步方法）
 * 每次获取实例都要获取锁，性能较差
 */
class LazySingletonSync {
    private static LazySingletonSync instance;

    private LazySingletonSync() {
        System.out.println("LazySingletonSync 构造函数被调用");
    }

    // 同步方法，每次都要获取锁
    public static synchronized LazySingletonSync getInstance() {
        if (instance == null) {
            instance = new LazySingletonSync();
        }
        return instance;
    }
}

/**
 * 双重检查锁定单例
 * 结合了延迟初始化和性能优化
 */
class DCLSingleton {
    // 使用volatile防止指令重排（JDK5+）
    private static volatile DCLSingleton instance;

    private DCLSingleton() {
        System.out.println("DCLSingleton 构造函数被调用");
    }

    /**
     * 双重检查锁定
     *
     * 第一次检查：不用加锁，判断实例是否已创建
     * 第二次检查：需要加锁，确保只有一个线程创建实例
     */
    public static DCLSingleton getInstance() {
        // 第一次检查：不需要加锁
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                // 第二次检查：需要加锁
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }
}

/**
 * 静态内部类单例
 * 利用类加载机制实现延迟初始化
 */
class StaticInnerClassSingleton {
    private StaticInnerClassSingleton() {
        System.out.println("StaticInnerClassSingleton 构造函数被调用");
    }

    /**
     * 静态内部类
     * 内部类只有在被使用时才会加载，实现延迟初始化
     */
    private static class SingletonHolder {
        private static final StaticInnerClassSingleton instance = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.instance;
    }
}

/**
 * 枚举单例
 * 最安全、最简洁的实现方式
 */
enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("枚举单例方法被调用");
    }
}

/**
 * DCL原理演示类
 * 展示在多线程环境下DCL的行为
 */
class DCLObject {
    // volatile确保可见性和防止指令重排
    private static volatile DCLObject instance;

    private DCLObject() {
        System.out.println("DCLObject 构造函数被调用，线程: " + Thread.currentThread().getName());
    }

    public static DCLObject getInstance() {
        if (instance == null) {
            synchronized (DCLObject.class) {
                if (instance == null) {
                    // 这不是一个原子操作，会发生指令重排
                    // 1. 分配内存
                    // 2. 调用构造函数
                    // 3. 将引用指向内存
                    // volatile可以禁止指令重排，确保2在3之前执行
                    instance = new DCLObject();
                }
            }
        }
        return instance;
    }
}
