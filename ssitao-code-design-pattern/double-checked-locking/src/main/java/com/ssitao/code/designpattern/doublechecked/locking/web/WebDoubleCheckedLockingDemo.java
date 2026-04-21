package com.ssitao.code.designpattern.doublechecked.locking.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Web应用中的双重检查锁定
 *
 * Web应用典型使用场景：
 * 1. 数据库连接池 - 延迟初始化连接池
 * 2. 缓存管理器 - 延迟加载缓存实例
 * 3. 消息生产者 - 单例消息发送器
 * 4. 配置服务 - 延迟加载配置
 * 5. Spring Bean - 原型Bean的懒加载
 *
 * @author ssitao
 */
public class WebDoubleCheckedLockingDemo {

    public static void main(String[] args) {
        System.out.println("=== Web应用双重检查锁定示例 ===\n");

        // 示例1：数据库连接池
        System.out.println("1. 数据库连接池示例");
        connectionPoolDemo();

        // 示例2：缓存管理器
        System.out.println("\n2. 缓存管理器示例");
        cacheManagerDemo();

        // 示例3：Spring中的DCL
        System.out.println("\n3. Spring中的DCL示例");
        springDclDemo();
    }

    /**
     * 数据库连接池示例
     * 使用DCL延迟初始化连接池
     */
    private static void connectionPoolDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 多个线程同时获取连接池
        for (int i = 0; i < 5; i++) {
            final int id = i;
            executor.submit(() -> {
                ConnectionPool pool = ConnectionPool.getInstance();
                System.out.println("请求-" + id + " 获取连接池: " + pool.hashCode());
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
     * 缓存管理器示例
     * 使用DCL实现延迟加载的缓存
     */
    private static void cacheManagerDemo() {
        CacheManager manager = CacheManager.getInstance();

        // 模拟缓存操作
        manager.put("user:1", "User1");
        manager.put("user:2", "User2");

        System.out.println("获取user:1: " + manager.get("user:1"));
        System.out.println("获取user:2: " + manager.get("user:2"));
    }

    /**
     * Spring中的DCL示例
     * 展示Spring中如何使用DCL
     */
    private static void springDclDemo() {
        // Spring中的Service单例默认是线程安全的
        // 但某些场景需要手动实现DCL

        // 模拟获取Spring Bean
        UserService userService = UserService.getInstance();
        UserService userService2 = UserService.getInstance();

        System.out.println("同一实例: " + (userService == userService2));

        userService.queryUser("user-001");
        userService.updateUser("user-001");
    }
}

/**
 * 数据库连接池 - DCL实现
 * 延迟初始化连接池，只在首次使用时创建
 */
class ConnectionPool {
    // volatile确保可见性
    private static volatile ConnectionPool instance;
    private java.util.Queue<Connection> pool;
    private final int poolSize = 10;

    private ConnectionPool() {
        pool = new java.util.LinkedList<>();
        // 初始化连接
        for (int i = 0; i < poolSize; i++) {
            pool.add(new Connection("Connection-" + i));
        }
        System.out.println("连接池初始化完成，大小: " + poolSize);
    }

    /**
     * 双重检查锁定获取连接池实例
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    /**
     * 获取连接
     */
    public Connection getConnection() {
        synchronized (pool) {
            return pool.poll();
        }
    }

    /**
     * 归还连接
     */
    public void returnConnection(Connection conn) {
        synchronized (pool) {
            pool.offer(conn);
        }
    }
}

/**
 * 数据库连接
 */
class Connection {
    private String name;

    public Connection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

/**
 * 缓存管理器 - DCL实现
 * 延迟加载缓存实例
 */
class CacheManager {
    private static volatile CacheManager instance;
    private java.util.Map<String, Object> cache;

    private CacheManager() {
        cache = new java.util.HashMap<>();
        System.out.println("CacheManager 初始化");
    }

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public void clear() {
        cache.clear();
    }
}

/**
 * 用户服务 - 模拟Spring中的Service
 * 使用DCL实现延迟加载
 */
class UserService {
    private static volatile UserService instance;
    private java.util.Map<String, User> userStore;

    private UserService() {
        userStore = new java.util.HashMap<>();
        // 初始化一些用户数据
        userStore.put("user-001", new User("user-001", "张三"));
        userStore.put("user-002", new User("user-002", "李四"));
        System.out.println("UserService 初始化完成");
    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    public void queryUser(String userId) {
        User user = userStore.get(userId);
        System.out.println("查询用户: " + (user != null ? user.getName() : "不存在"));
    }

    public void updateUser(String userId) {
        System.out.println("更新用户: " + userId);
    }
}

/**
 * 用户实体
 */
class User {
    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

/**
 * Spring中的DCL使用示例
 *
 * 1. 自定义FactoryBean中使用DCL
 * @Component
 * public class MyFactoryBean {
 *     private static volatile MyBean instance;
 *
 *     public static MyBean getInstance() {
 *         if (instance == null) {
 *             synchronized (MyFactoryBean.class) {
 *                 if (instance == null) {
 *                     instance = new MyBean();
 *                 }
 *             }
 *         }
 *         return instance;
 *     }
 * }
 *
 * 2. 配置属性类中使用DCL
 * @ConfigurationProperties(prefix = "my.config")
 * @Component
 * public class MyConfigProperties {
 *     private static volatile MyConfigProperties instance;
 *
 *     public static MyConfigProperties getInstance() {
 *         if (instance == null) {
 *             synchronized (MyConfigProperties.class) {
 *                 if (instance == null) {
 *                     instance = new MyConfigProperties();
 *                 }
 *             }
 *         }
 *         return instance;
 *     }
 * }
 *
 * 3. 静态工具类中使用DCL
 * @Component
 * public class DateUtil {
 *     private static volatile DateFormat df;
 *
 *     public static DateFormat getDateFormat() {
 *         if (df == null) {
 *             synchronized (DateUtil.class) {
 *                 if (df == null) {
 *                     df = new SimpleDateFormat("yyyy-MM-dd");
 *                 }
 *             }
 *         }
 *         return df;
 *     }
 * }
 *
 * 4. 消息队列生产者单例
 * @Component
 * public class MessageProducer {
 *     private static volatile MessageProducer instance;
 *     private Connection connection;
 *
 *     private MessageProducer() {
 *         // 初始化连接
 *     }
 *
 *     public static MessageProducer getInstance() {
 *         if (instance == null) {
 *             synchronized (MessageProducer.class) {
 *                 if (instance == null) {
 *                     instance = new MessageProducer();
 *                 }
 *             }
 *         }
 *         return instance;
 *     }
 *
 *     public void send(String queue, String message) {
 *         // 发送消息
 *     }
 * }
 *
 * 5. Redis连接池
 * @Component
 * public class RedisConnectionPool {
 *     private static volatile JedisPool instance;
 *
 *     public static JedisPool getInstance() {
 *         if (instance == null) {
 *             synchronized (RedisConnectionPool.class) {
 *                 if (instance == null) {
 *                     instance = new JedisPool(config);
 *                 }
 *             }
 *         }
 *         return instance;
 *     }
 * }
 */
