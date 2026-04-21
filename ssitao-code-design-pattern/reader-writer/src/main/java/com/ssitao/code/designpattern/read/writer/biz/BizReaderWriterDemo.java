package com.ssitao.code.designpattern.read.writer.biz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 业务场景中的读写锁模式
 *
 * 典型业务应用：
 * 1. 库存管理 - 库存查询多，改动少
 * 2. 账户余额 - 余额查询频繁，交易时需要锁
 * 3. 点赞/收藏 - 数量查询多，变动少
 * 4. 排行榜 - 读取频繁，更新定时
 * 5. 订单状态 - 状态查询多，状态变更少
 *
 * @author ssitao
 */
public class BizReaderWriterDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景读写锁示例 ===\n");

        // 示例1：库存管理
        System.out.println("1. 库存管理示例");
        inventoryDemo();

        // 示例2：账户余额
        System.out.println("\n2. 账户余额示例");
        accountBalanceDemo();

        // 示例3：点赞统计
        System.out.println("\n3. 点赞统计示例");
        likeCountDemo();
    }

    /**
     * 库存管理示例
     * 场景：商品库存查询频繁，下单时扣减库存
     */
    private static void inventoryDemo() {
        Inventory inventory = new Inventory("商品-001", 100);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟大量库存查询（读操作）
        for (int i = 0; i < 30; i++) {
            final int queryId = i;
            executor.submit(() -> {
                int stock = inventory.getStock();
                System.out.println("查询-" + queryId + " 当前库存: " + stock);
            });
        }

        // 模拟下单扣减库存（写操作）
        for (int i = 0; i < 5; i++) {
            final int orderId = i;
            executor.submit(() -> {
                boolean success = inventory.decreaseStock(10);
                if (success) {
                    System.out.println("订单-" + orderId + " 扣减库存成功，剩余: " + inventory.getStock());
                } else {
                    System.out.println("订单-" + orderId + " 库存不足");
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 账户余额示例
     * 场景：账户余额查询频繁，转账时需要扣款
     */
    private static void accountBalanceDemo() {
        Account account = new Account("ACC-001", 10000.0);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟大量余额查询（读操作）
        for (int i = 0; i < 20; i++) {
            final int queryId = i;
            executor.submit(() -> {
                double balance = account.getBalance();
                System.out.println("查询-" + queryId + " 余额: " + balance);
            });
        }

        // 模拟转账操作（写操作）
        for (int i = 0; i < 3; i++) {
            final int transId = i;
            executor.submit(() -> {
                boolean success = account.withdraw(500);
                if (success) {
                    System.out.println("转账-" + transId + " 成功，余额: " + account.getBalance());
                } else {
                    System.out.println("转账-" + transId + " 失败，余额不足");
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点赞统计示例
     * 场景：文章点赞数查询频繁，用户点赞时增加
     */
    private static void likeCountDemo() {
        LikeCounter likeCounter = new LikeCounter("article-001");

        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 模拟点赞数查询（读操作）
        for (int i = 0; i < 20; i++) {
            final int queryId = i;
            executor.submit(() -> {
                long count = likeCounter.getLikeCount();
                System.out.println("查询-" + queryId + " 点赞数: " + count);
            });
        }

        // 模拟用户点赞（写操作）
        for (int i = 0; i < 5; i++) {
            final int userId = i;
            executor.submit(() -> {
                likeCounter.addLike();
                System.out.println("用户-" + userId + " 点赞，当前: " + likeCounter.getLikeCount());
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 库存管理
 * 使用读写锁保护库存数据
 */
class Inventory {
    private final String productId;
    private int stock;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Inventory(String productId, int stock) {
        this.productId = productId;
        this.stock = stock;
    }

    /**
     * 获取库存 - 读锁，可并发
     */
    public int getStock() {
        lock.readLock().lock();
        try {
            return stock;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 扣减库存 - 写锁，独占
     */
    public boolean decreaseStock(int quantity) {
        lock.writeLock().lock();
        try {
            if (stock >= quantity) {
                stock -= quantity;
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 增加库存 - 写锁
     */
    public void increaseStock(int quantity) {
        lock.writeLock().lock();
        try {
            stock += quantity;
        } finally {
            lock.writeLock().unlock();
        }
    }
}

/**
 * 账户余额管理
 * 使用读写锁保护账户资金
 */
class Account {
    private final String accountId;
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Account(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    /**
     * 获取余额 - 读锁
     */
    public double getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 存款 - 写锁
     */
    public void deposit(double amount) {
        lock.writeLock().lock();
        try {
            balance += amount;
            System.out.println("存款: " + amount + ", 当前余额: " + balance);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 取款 - 写锁
     */
    public boolean withdraw(double amount) {
        lock.writeLock().lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 转账 - 写锁
     */
    public boolean transfer(Account target, double amount) {
        lock.writeLock().lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                target.deposit(amount);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
}

/**
 * 点赞计数器
 * 使用读写锁保护点赞数
 */
class LikeCounter {
    private final String targetId;
    private long likeCount;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public LikeCounter(String targetId) {
        this.targetId = targetId;
        this.likeCount = 0;
    }

    /**
     * 获取点赞数 - 读锁
     */
    public long getLikeCount() {
        lock.readLock().lock();
        try {
            return likeCount;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 点赞 - 写锁
     */
    public void addLike() {
        lock.writeLock().lock();
        try {
            likeCount++;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 取消点赞 - 写锁
     */
    public void removeLike() {
        lock.writeLock().lock();
        try {
            if (likeCount > 0) {
                likeCount--;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}

/**
 * 订单状态管理
 * 使用读写锁保护订单状态
 */
class OrderStatus {
    private String orderId;
    private String status; // PENDING, PAID, SHIPPED, COMPLETED, CANCELLED
    private long updateTime;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public OrderStatus(String orderId) {
        this.orderId = orderId;
        this.status = "PENDING";
        this.updateTime = System.currentTimeMillis();
    }

    /**
     * 获取订单状态 - 读锁
     */
    public String getStatus() {
        lock.readLock().lock();
        try {
            return status;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 获取订单信息 - 读锁
     */
    public String getOrderInfo() {
        lock.readLock().lock();
        try {
            return String.format("订单号: %s, 状态: %s, 更新时间: %d",
                orderId, status, updateTime);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * 更新订单状态 - 写锁
     */
    public boolean updateStatus(String newStatus) {
        lock.writeLock().lock();
        try {
            // 状态流转校验
            if (!isValidTransition(status, newStatus)) {
                System.out.println("无效的状态转换: " + status + " -> " + newStatus);
                return false;
            }
            status = newStatus;
            updateTime = System.currentTimeMillis();
            System.out.println("订单状态更新: " + orderId + " -> " + newStatus);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * 状态流转校验
     */
    private boolean isValidTransition(String current, String next) {
        if (current.equals("PENDING")) {
            return next.equals("PAID") || next.equals("CANCELLED");
        } else if (current.equals("PAID")) {
            return next.equals("SHIPPED") || next.equals("CANCELLED");
        } else if (current.equals("SHIPPED")) {
            return next.equals("COMPLETED");
        }
        return false;
    }
}

/**
 * 分布式锁中的读写锁应用示例
 *
 * 1. 分布式读写锁（Redis + Redisson）
 * @Service
 * public class DistributedLockDemo {
 *     @Autowired
 *     private RedissonClient redissonClient;
 *
 *     public String readData(String key) {
 *         RReadWriteLock rwLock = redissonClient.getReadWriteLock("lock:" + key);
 *         Lock readLock = rwLock.readLock();
 *         readLock.lock();
 *         try {
 *             // 读取数据
 *             return redisTemplate.opsForValue().get(key);
 *         } finally {
 *             readLock.unlock();
 *         }
 *     }
 *
 *     public void writeData(String key, String value) {
 *         RReadWriteLock rwLock = redissonClient.getReadWriteLock("lock:" + key);
 *         Lock writeLock = rwLock.writeLock();
 *         writeLock.lock();
 *         try {
 *             // 写入数据
 *             redisTemplate.opsForValue().set(key, value);
 *         } finally {
 *             writeLock.unlock();
 *         }
 *     }
 * }
 *
 * 2. 数据库读写锁
 * // 读锁（S锁）- 防止其他事务修改
 * SELECT * FROM user WHERE id = 1 LOCK IN SHARE MODE;
 *
 * // 写锁（X锁）- 防止其他事务读取和修改
 * SELECT * FROM user WHERE id = 1 FOR UPDATE;
 *
 * 3. Zookeeper读写锁
 * // 使用Curator的InterProcessReadWriteLock
 * InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, "/lock");
 * Lock readLock = lock.readLock();
 * Lock writeLock = lock.writeLock();
 */
