package com.ssitao.code.designpattern.read.writer.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Web应用中的读写锁模式
 *
 * 典型应用场景：
 * 1. 配置信息读取 - 配置很少修改，但被频繁读取
 * 2. 缓存数据访问 - 热点数据读多写少
 * 3. 用户信息查询 - 用户数据读多写少
 * 4. 商品信息展示 - 商品详情读多写少
 * 5. 统计数据分析 - 统计数据读多写少
 *
 * @author ssitao
 */
public class WebReaderWriterDemo {

    public static void main(String[] args) {
        System.out.println("=== Web应用读写锁示例 ===\n");

        // 示例1：配置中心
        System.out.println("1. 配置中心示例");
        configCenterDemo();

        // 示例2：本地缓存
        System.out.println("\n2. 本地缓存示例");
        localCacheDemo();

        // 示例3：用户会话管理
        System.out.println("\n3. 用户会话管理示例");
        sessionManagementDemo();
    }

    /**
     * 配置中心示例
     * 场景：系统配置很少修改，但所有服务都需要频繁读取
     */
    private static void configCenterDemo() {
        // 模拟配置中心
        ConfigCenter config = new ConfigCenter();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟大量读取配置请求（99%读）
        for (int i = 0; i < 50; i++) {
            final int id = i;
            executor.submit(() -> {
                String dbUrl = config.getConfig("db.url");
                String dbUser = config.getConfig("db.user");
                System.out.println("请求-" + id + " 读取配置: db.url=" + dbUrl);
            });
        }

        // 模拟配置更新（1%写）
        executor.submit(() -> {
            config.updateConfig("db.url", "jdbc:mysql://new-host:3306/db");
            System.out.println("配置更新: db.url");
        });

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地缓存示例
     * 场景：热点数据缓存在本地，多个线程频繁读取，偶尔更新
     */
    private static void localCacheDemo() {
        LocalCache cache = new LocalCache();

        // 初始化缓存
        cache.put("user:1", "{\"name\":\"张三\",\"age\":25}");
        cache.put("user:2", "{\"name\":\"李四\",\"age\":30}");

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟大量缓存读取
        for (int i = 0; i < 30; i++) {
            final int userId = (i % 3) + 1;
            final int requestId = i;
            executor.submit(() -> {
                String userData = cache.get("user:" + userId);
                System.out.println("请求-" + requestId + " 读取缓存: user:" + userId);
            });
        }

        // 模拟缓存更新
        executor.submit(() -> {
            cache.put("user:1", "{\"name\":\"张三（已更新）\",\"age\":26}");
            System.out.println("缓存更新: user:1");
        });

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户会话管理示例
     * 场景：用户登录后创建会话，会话信息读多写少
     */
    private static void sessionManagementDemo() {
        SessionManager manager = new SessionManager();

        // 创建一些会话
        manager.createSession("session-1", "user-001");
        manager.createSession("session-2", "user-002");

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟频繁的会话验证（读操作）
        for (int i = 0; i < 20; i++) {
            final String sessionId = "session-" + ((i % 2) + 1);
            executor.submit(() -> {
                String userId = manager.getUserId(sessionId);
                if (userId != null) {
                    System.out.println("会话验证成功: " + sessionId + " -> " + userId);
                } else {
                    System.out.println("会话不存在: " + sessionId);
                }
            });
        }

        // 模拟会话更新（写操作）
        executor.submit(() -> {
            manager.updateLastAccessTime("session-1");
            System.out.println("更新会话时间: session-1");
        });

        executor.submit(() -> {
            manager.destroySession("session-2");
            System.out.println("销毁会话: session-2");
        });

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 配置中心
 * 使用读写锁保护配置信息
 */
class ConfigCenter {
    private final java.util.Map<String, String> configs = new java.util.HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ConfigCenter() {
        // 初始化默认配置
        configs.put("db.url", "jdbc:mysql://localhost:3306/test");
        configs.put("db.user", "root");
        configs.put("db.password", "123456");
        configs.put("app.name", "MyApp");
    }

    /**
     * 读取配置 - 读多写少，使用读锁
     */
    public String getConfig(String key) {
        lock.readLock().lock();
        try {
            return configs.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 更新配置 - 使用写锁
     */
    public void updateConfig(String key, String value) {
        lock.writeLock().lock();
        try {
            configs.put(key, value);
            System.out.println("配置已更新: " + key + " = " + value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 批量获取配置 - 读锁
     */
    public java.util.Map<String, String> getAllConfigs() {
        lock.readLock().lock();
        try {
            return new java.util.HashMap<>(configs);
        } finally {
            lock.readLock().unlock();
        }
    }
}

/**
 * 本地缓存
 * 使用读写锁保护缓存数据
 */
class LocalCache {
    private final java.util.Map<String, String> cache = new java.util.HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 获取缓存 - 读锁，可并发
     */
    public String get(String key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 设置缓存 - 写锁，独占
     */
    public void put(String key, String value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 删除缓存 - 写锁
     */
    public void remove(String key) {
        lock.writeLock().lock();
        try {
            cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 清空缓存 - 写锁
     */
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}

/**
 * 会话管理器
 * 管理用户会话信息，读多写少
 */
class SessionManager {
    private final java.util.Map<String, Session> sessions = new java.util.HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 创建会话
     */
    public void createSession(String sessionId, String userId) {
        lock.writeLock().lock();
        try {
            sessions.put(sessionId, new Session(sessionId, userId));
            System.out.println("创建会话: " + sessionId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 获取用户ID - 读锁
     */
    public String getUserId(String sessionId) {
        lock.readLock().lock();
        try {
            Session session = sessions.get(sessionId);
            return session != null ? session.getUserId() : null;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 更新最后访问时间 - 写锁
     */
    public void updateLastAccessTime(String sessionId) {
        lock.writeLock().lock();
        try {
            Session session = sessions.get(sessionId);
            if (session != null) {
                session.updateLastAccessTime();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 销毁会话 - 写锁
     */
    public void destroySession(String sessionId) {
        lock.writeLock().lock();
        try {
            sessions.remove(sessionId);
        } finally {
            lock.writeLock().unlock();
        }
    }
}

/**
 * 会话对象
 */
class Session {
    private final String sessionId;
    private final String userId;
    private long lastAccessTime;

    public Session(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }
}

/**
 * Spring中的读写锁使用示例
 *
 * 1. 缓存使用
 * @Service
 * public class UserCacheService {
 *     private final Map<String, User> cache = new HashMap<>();
 *     private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
 *
 *     public User getUser(String id) {
 *         lock.readLock().lock();
 *         try {
 *             return cache.get(id);
 *         } finally {
 *             lock.readLock().unlock();
 *         }
 *     }
 *
 *     public void updateUser(User user) {
 *         lock.writeLock().lock();
 *         try {
 *             cache.put(user.getId(), user);
 *         } finally {
 *             lock.writeLock().unlock();
 *         }
 *     }
 * }
 *
 * 2. 配置属性读取
 * @Component
 * public class AppConfig {
 *     private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
 *     private Map<String, String> properties;
 *
 *     @Value("${app.config}")
 *     private String configPath;
 *
 *     @PostConstruct
 *     public void init() {
 *         // 加载配置
 *         properties = loadProperties(configPath);
 *     }
 *
 *     public String getProperty(String key) {
 *         lock.readLock().lock();
 *         try {
 *             return properties.get(key);
 *         } finally {
 *             lock.readLock().unlock();
 *         }
 *     }
 *
 *     @Scheduled(fixedRate = 60000)
 *     public void reloadConfig() {
 *         lock.writeLock().lock();
 *         try {
 *             properties = loadProperties(configPath);
 *         } finally {
 *             lock.writeLock().unlock();
 *         }
 *     }
 * }
 *
 * 3. 统计数据访问
 * @Service
 * public class MetricsService {
 *     private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
 *     private Map<String, AtomicLong> metrics = new HashMap<>();
 *
 *     public long getMetric(String key) {
 *         lock.readLock().lock();
 *         try {
 *             AtomicLong counter = metrics.get(key);
 *             return counter != null ? counter.get() : 0;
 *         } finally {
 *             lock.readLock().unlock();
 *         }
 *     }
 *
 *     public void incrementMetric(String key) {
 *         lock.writeLock().lock();
 *         try {
 *             metrics.computeIfAbsent(key, k -> new AtomicLong()).incrementAndGet();
 *         } finally {
 *             lock.writeLock().unlock();
 *         }
 *     }
 * }
 */
