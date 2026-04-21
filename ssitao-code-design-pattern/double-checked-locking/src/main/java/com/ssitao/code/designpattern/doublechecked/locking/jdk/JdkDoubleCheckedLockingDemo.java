package com.ssitao.code.designpattern.doublechecked.locking.jdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JDK中的双重检查锁定详解
 *
 * JDK中使用DCL的典型场景：
 * 1. 懒汉式单例
 * 2. 线程局部变量 ThreadLocal
 * 3. 锁对象的初始化
 * 4. 容器单例管理
 *
 * volatile的作用：
 * - 保证可见性：一个线程对共享变量的修改，其他线程立即可见
 * - 禁止指令重排：防止构造函数调用在对象引用赋值之后执行
 *
 * JDK5之前的DCL问题：
 * - 指令重排导致其他线程可能看到未完全初始化的对象
 * - 解决方案：使用volatile修饰
 *
 * @author ssitao
 */
public class JdkDoubleCheckedLockingDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK 双重检查锁定详解 ===\n");

        // 示例1：volatile作用演示
        System.out.println("1. volatile作用演示");
        volatileDemo();

        // 示例2：ThreadLocal中的DCL
        System.out.println("\n2. ThreadLocal中的DCL");
        threadLocalDemo();

        // 示例3：自定义线程安全的配置类
        System.out.println("\n3. 配置类DCL实现");
        configDemo();

        // 示例4：多线程环境下的单例测试
        System.out.println("\n4. 多线程单例测试");
        multiThreadSingletonTest();
    }

    /**
     * volatile作用演示
     * 展示volatile如何保证可见性和防止指令重排
     */
    private static void volatileDemo() {
        VolatileDemo demo = new VolatileDemo();

        // 线程1：写入flag
        Thread writer = new Thread(() -> {
            demo.write();
        }, "Writer");

        // 线程2：读取flag
        Thread reader = new Thread(() -> {
            demo.read();
        }, "Reader");

        writer.start();
        reader.start();

        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ThreadLocal中的DCL
     * ThreadLocal使用DCL来初始化ThreadLocalMap
     */
    private static void threadLocalDemo() {
        // ThreadLocal内部使用DCL来初始化
        ThreadLocal<String> threadLocal = new ThreadLocal<>();

        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            final int id = i;
            executor.submit(() -> {
                threadLocal.set("值-" + id);
                System.out.println(Thread.currentThread().getName() + " 设置: " + threadLocal.get());
                // 每个线程独立存储，不会互相影响
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 配置类DCL实现
     * 使用DCL实现延迟加载的配置管理器
     */
    private static void configDemo() {
        // 多次获取配置实例，验证DCL
        ConfigManager mgr1 = ConfigManager.getInstance();
        ConfigManager mgr2 = ConfigManager.getInstance();

        System.out.println("两次获取是同一实例: " + (mgr1 == mgr2));

        System.out.println("数据库URL: " + mgr1.getConfig("db.url"));
        System.out.println("最大连接数: " + mgr1.getConfig("db.maxConnections"));
    }

    /**
     * 多线程环境下的单例测试
     * 验证DCL在多线程环境下是否只创建一个实例
     */
    private static void multiThreadSingletonTest() {
        final SingletonTest[] holder = new SingletonTest[1];
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 10个线程同时获取实例
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                SingletonTest instance = SingletonTest.getInstance();
                synchronized (holder) {
                    if (holder[0] == null) {
                        holder[0] = instance;
                    }
                }
                System.out.println(Thread.currentThread().getName() + " 获取实例: " + instance.hashCode());
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("所有线程获取的是同一实例: " + (holder[0] == SingletonTest.getInstance()));
    }
}

/**
 * volatile演示类
 */
class VolatileDemo {
    // 不使用volatile可能出现问题
    // private boolean flag = false;
    private volatile boolean flag = false;

    public void write() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("Writer: flag = " + flag);
    }

    public void read() {
        int count = 0;
        while (!flag) {
            count++;
            if (count > 1000000) {
                System.out.println("Reader: 等待超时，未发现flag变化");
                return;
            }
        }
        System.out.println("Reader: 检测到flag变化，flag = " + flag);
    }
}

/**
 * 配置管理器 - 使用DCL实现
 */
class ConfigManager {
    // volatile确保instance可见性
    private static volatile ConfigManager instance;
    private java.util.Map<String, String> configs;

    private ConfigManager() {
        // 模拟配置加载
        configs = new java.util.HashMap<>();
        configs.put("db.url", "jdbc:mysql://localhost:3306/test");
        configs.put("db.user", "root");
        configs.put("db.maxConnections", "100");
        configs.put("app.name", "MyApplication");
        System.out.println("ConfigManager 初始化完成");
    }

    /**
     * 双重检查锁定获取实例
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String getConfig(String key) {
        return configs.get(key);
    }
}

/**
 * 单例测试类 - DCL实现
 */
class SingletonTest {
    // volatile确保instance可见性和防止指令重排
    private static volatile SingletonTest instance;

    // 模拟耗时初始化
    static {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SingletonTest() {
        System.out.println("SingletonTest 实例创建，线程: " + Thread.currentThread().getName());
    }

    /**
     * DCL实现
     */
    public static SingletonTest getInstance() {
        if (instance == null) {
            synchronized (SingletonTest.class) {
                if (instance == null) {
                    instance = new SingletonTest();
                }
            }
        }
        return instance;
    }
}

/**
 * 无DCL的懒汉式（对比用）
 * 可能会创建多个实例
 */
class UnsafeSingleton {
    private static UnsafeSingleton instance;

    private UnsafeSingleton() {
        System.out.println("UnsafeSingleton 实例创建");
    }

    /**
     * 没有DCL，多线程下可能创建多个实例
     */
    public static UnsafeSingleton getInstance() {
        if (instance == null) {
            // 多个线程可能同时通过第一个if
            synchronized (UnsafeSingleton.class) {
                // 第二个检查可以防止重复创建，但每次都要获取锁
                instance = new UnsafeSingleton();
            }
        }
        return instance;
    }
}

/**
 * 改进版：无DCL但每次都加锁
 * 线程安全但性能差
 */
class SafeSingleton {
    private static SafeSingleton instance;

    private SafeSingleton() {
        System.out.println("SafeSingleton 实例创建");
    }

    /**
     * 每次获取都要同步，性能差
     */
    public static synchronized SafeSingleton getInstance() {
        if (instance == null) {
            instance = new SafeSingleton();
        }
        return instance;
    }
}

/**
 * JDK中DCL的典型应用场景总结：
 *
 * 1. 懒加载单例
 * private static volatile Singleton instance;
 * public static Singleton getInstance() {
 *     if (instance == null) {
 *         synchronized(Singleton.class) {
 *             if (instance == null) {
 *                 instance = new Singleton();
 *             }
 *         }
 *     }
 *     return instance;
 * }
 *
 * 2. ThreadLocal
 * ThreadLocalMap使用了类似的延迟初始化模式
 *
 * 3. 容器管理
 * Spring中的BeanFactory、ApplicationContext等
 *
 * 4. 线程池
 * ExecutorService的某些实现
 *
 * 注意事项：
 * - 必须使用volatile（JDK5+）
 * - 适用于实例字段，不适用于静态字段
 * - 静态字段建议使用静态内部类或枚举
 */
