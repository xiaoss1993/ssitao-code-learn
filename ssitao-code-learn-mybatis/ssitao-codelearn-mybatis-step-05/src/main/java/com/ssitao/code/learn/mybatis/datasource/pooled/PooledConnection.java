package com.ssitao.code.learn.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 池化连接代理类
 *
 * 该类是连接池的核心组件，通过 JDK 动态代理模式包装真实的数据库连接。
 *
 * 设计思路：
 * 1. 创建一个真实连接 (realConnection) 和一个代理连接 (proxyConnection)
 * 2. 用户获取的是 proxyConnection，而不是直接使用 realConnection
 * 3. 当调用 proxyConnection.close() 时，并不是真正关闭连接，而是将连接归还到连接池
 * 4. 这样就实现了连接的复用，避免频繁创建和销毁连接带来的性能开销
 *
 * @author sizt
 * @description: 池化代理的链接
 * @date 2026/1/30 16:30
 */
public class PooledConnection implements InvocationHandler {

    /** 标识要拦截的方法名 - close 方法 */
    private static final String CLOSE = "close";

    /** 代理要实现的接口数组 - 仅实现 java.sql.Connection 接口 */
    private static final Class<?>[] IFACES = new Class[]{Connection.class};

    /** 代理连接的 hashCode，用于标识这个代理连接 */
    private int hashCode = 0;

    /** 所属的数据源 */
    private PooledDataSource dataSource;

    /** 真实的数据库连接（非代理） */
    private Connection realConnection;

    /** 代理后的数据库连接（返回给用户使用） */
    private Connection proxyConnection;

    /** 检出时间 - 从连接池取出时的时间戳 */
    private long checkoutTimestamp;

    /** 创建时间 - 连接创建时的时间戳 */
    private long createdTimestamp;

    /** 最后使用时间 - 连接最后一次被使用时的时间戳 */
    private long lastUsedTimestamp;

    /** 连接类型码 - 用于标识不同的连接类型 */
    private int connectionTypeCode;

    /** 连接是否有效 - 无效的连接会被关闭并从池中移除 */
    private boolean valid;

    /**
     * 构造方法
     *
     * @param connection 真实的数据库连接
     * @param dataSource 所属的连接池数据源
     */
    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        // 保存真实连接的 hashCode，用于标识和比较
        this.hashCode = connection.hashCode();
        // 保存真实连接
        this.realConnection = connection;
        // 保存数据源引用，用于归还连接时操作连接池
        this.dataSource = dataSource;
        // 记录创建时间
        this.createdTimestamp = System.currentTimeMillis();
        // 记录最后使用时间（初始与创建时间相同）
        this.lastUsedTimestamp = System.currentTimeMillis();
        // 初始状态为有效
        this.valid = true;
        // 创建代理连接，this 作为InvocationHandler，当调用代理连接的方法时会触发 invoke 方法
        this.proxyConnection = (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                IFACES,
                this
        );
    }

    /**
     * 动态代理的拦截方法
     *
     * 所有对 proxyConnection 的方法调用都会经过此方法。
     * 这是实现连接池"假关闭"的关键。
     *
     * @param proxy  代理对象本身（一般不使用）
     * @param method 被调用的方法
     * @param args   方法参数
     * @return 方法返回值
     * @throws Throwable 可能抛出的异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        /*
         * 如果调用的是 close() 方法，则将连接归还到连接池，而不是真正关闭连接。
         * 这是连接池实现的核心：
         * - 普通连接：close() -> 关闭连接
         * - 池化连接：close() -> 归还连接到池中，供后续复用
         */
        if (CLOSE.hashCode() == methodName.hashCode() && CLOSE.equals(methodName)) {
            // 将连接归还到连接池
            dataSource.pushConnection(this);
        } else {
            // 其他方法调用真实连接
            // TODO: 需要实现真实的方法调用逻辑
        }
        return null;
    }

    /**
     * 检查连接是否有效
     *
     * @throws SQLException 如果连接无效则抛出异常
     */
    private void checkConnection() throws SQLException {
        if (!valid) {
            throw new SQLException("Error accessing PooledConnection. Connection is invalid.");
        }
    }

    /**
     * 使连接失效
     *
     * 通常在连接发生严重错误时调用，失效的连接会被关闭并从池中移除
     */
    public void invalidate() {
        valid = false;
    }

    /**
     * 检查连接是否有效
     *
     * 有效的连接需要满足：
     * 1. valid 标志为 true
     * 2. 真实连接不为 null
     * 3. 能够通过 ping 测试（检测连接是否真的可用）
     *
     * @return 连接是否有效
     */
    public boolean isValid() {
        return valid && realConnection != null && dataSource.pingConnection(this);
    }

    /**
     * 获取真实连接
     *
     * @return 真实的数据库连接
     */
    public Connection getRealConnection() {
        return realConnection;
    }

    /**
     * 获取代理连接
     *
     * 返回给用户使用的是代理连接，而非真实连接。
     * 这样用户对代理连接的 close() 调用会被拦截，从而实现连接归还而非真正关闭。
     *
     * @return 代理后的数据库连接
     */
    public Connection getProxyConnection() {
        return proxyConnection;
    }

    /**
     * 获取真实连接的 hashCode
     *
     * @return 真实连接的 hashCode，如果为 null 则返回 0
     */
    public int getRealHashCode() {
        return realConnection == null ? 0 : realConnection.hashCode();
    }

    /**
     * 获取连接类型码
     *
     * @return 连接类型码
     */
    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    /**
     * 设置连接类型码
     *
     * @param connectionTypeCode 连接类型码
     */
    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    /**
     * 获取连接创建时间戳
     *
     * @return 创建时间的时间戳
     */
    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * 设置连接创建时间戳
     *
     * @param createdTimestamp 创建时间的时间戳
     */
    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    /**
     * 获取连接最后使用时间戳
     *
     * @return 最后使用时间的时间戳
     */
    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    /**
     * 设置连接最后使用时间戳
     *
     * @param lastUsedTimestamp 最后使用时间的时间戳
     */
    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    /**
     * 获取距离上次使用的时间间隔
     *
     * @return 当前时间与上次使用时间的差值（毫秒）
     */
    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - lastUsedTimestamp;
    }

    /**
     * 获取连接的年龄（从创建到现在的时间）
     *
     * @return 连接创建到现在的时间（毫秒）
     */
    public long getAge() {
        return System.currentTimeMillis() - createdTimestamp;
    }

    /**
     * 获取连接被检出（从池中取出）的时间戳
     *
     * @return 检出时间的时间戳
     */
    public long getCheckoutTimestamp() {
        return checkoutTimestamp;
    }

    /**
     * 设置连接被检出的时间戳
     *
     * @param timestamp 检出时间的时间戳
     */
    public void setCheckoutTimestamp(long timestamp) {
        this.checkoutTimestamp = timestamp;
    }

    /**
     * 获取连接被检出的时长
     *
     * @return 当前时间与检出时间的差值（毫秒）
     */
    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimestamp;
    }

    /**
     * 返回代理连接的 hashCode
     *
     * @return hashCode 值
     */
    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
     * 判断对象是否与此代理连接相等
     *
     * 支持与 PooledConnection 或 Connection 类型进行比较
     *
     * @param obj 要比较的对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection) {
            // 与另一个 PooledConnection 比较时，比较其真实连接的 hashCode
            return realConnection.hashCode() == (((PooledConnection) obj).realConnection.hashCode());
        } else if (obj instanceof Connection) {
            // 与普通 Connection 比较时，比较 hashCode
            return hashCode == obj.hashCode();
        } else {
            return false;
        }
    }
}