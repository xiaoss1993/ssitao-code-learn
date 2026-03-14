package com.ssitao.code.designpattern.singleton;

/**
 * 单例模式演示类
 */
public class SingletonDemo {

    public static void main(String[] args) {
        System.out.println("========== 单例模式示例 ==========\n");

        // 1. 饿汉式
        System.out.println("--- 饿汉式 ---");
        HungrySingleton hungry1 = HungrySingleton.getInstance();
        HungrySingleton hungry2 = HungrySingleton.getInstance();
        System.out.println("hashCode相同: " + (hungry1 == hungry2));

        // 2. 懒汉式(线程不安全)
        System.out.println("\n--- 懒汉式(线程不安全) ---");
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        System.out.println("hashCode相同: " + (lazy1 == lazy2));

        // 3. 懒汉式(同步方法)
        System.out.println("\n--- 懒汉式(同步方法) ---");
        SynchronizedLazySingleton sync1 = SynchronizedLazySingleton.getInstance();
        SynchronizedLazySingleton sync2 = SynchronizedLazySingleton.getInstance();
        System.out.println("hashCode相同: " + (sync1 == sync2));

        // 4. 双重检查锁
        System.out.println("\n--- 双重检查锁 ---");
        DoubleCheckLockingSingleton dcl1 = DoubleCheckLockingSingleton.getInstance();
        DoubleCheckLockingSingleton dcl2 = DoubleCheckLockingSingleton.getInstance();
        System.out.println("hashCode相同: " + (dcl1 == dcl2));

        // 5. 静态内部类
        System.out.println("\n--- 静态内部类 ---");
        StaticInnerClassSingleton inner1 = StaticInnerClassSingleton.getInstance();
        StaticInnerClassSingleton inner2 = StaticInnerClassSingleton.getInstance();
        System.out.println("hashCode相同: " + (inner1 == inner2));

        // 6. 枚举
        System.out.println("\n--- 枚举 ---");
        EnumSingleton enum1 = EnumSingleton.INSTANCE;
        EnumSingleton enum2 = EnumSingleton.INSTANCE;
        System.out.println("hashCode相同: " + (enum1 == enum2));

        // 7. ThreadLocal(每个线程内唯一)
        System.out.println("\n--- ThreadLocal ---");
        ThreadLocalSingleton threadLocal1 = ThreadLocalSingleton.getInstance();
        ThreadLocalSingleton threadLocal2 = ThreadLocalSingleton.getInstance();
        System.out.println("同一线程内hashCode相同: " + (threadLocal1 == threadLocal2));

        // 不同线程
        new Thread(() -> {
            ThreadLocalSingleton t1 = ThreadLocalSingleton.getInstance();
            System.out.println("新线程hashCode: " + t1.hashCode());
        }).start();

        // 8. 容器单例
        System.out.println("\n--- 容器单例 ---");
        ContainerSingleton.register("singleton", new Object());
        Object obj1 = ContainerSingleton.getInstance("singleton");
        Object obj2 = ContainerSingleton.getInstance("singleton");
        System.out.println("hashCode相同: " + (obj1 == obj2));
    }
}
