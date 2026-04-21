package com.ssitao.code.designpattern.abstractfactory.mybatis;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatis中的抽象工厂模式示例
 *
 * MyBatis框架中大量使用了抽象工厂模式，这里展示几个典型应用：
 *
 * 1. SqlSessionFactory - 创建SqlSession的抽象工厂
 * 2. ObjectFactory - MyBatis对象创建工厂
 * 3. DataSourceFactory - 数据源工厂
 * 4. TransactionFactory - 事务工厂
 * 5. CacheFactory - 缓存工厂
 * 6. TypeHandlerFactory - 类型处理器工厂
 * 7. MapperProxyFactory - Mapper代理工厂
 * 8. BaseBuilder及其子类 - 配置构建工厂
 */
public class AbstractFactoryDemo {

    /**
     * 1. SqlSessionFactory 示例
     *
     * SqlSessionFactory 是 MyBatis 中最重要的抽象工厂，
     * 负责创建 SqlSession 实例
     */
    public static void sqlSessionFactoryExample() {
        System.out.println("=== SqlSessionFactory 示例 ===");

        // 创建数据源
        DataSource dataSource = new UnpooledDataSource(
            "org.h2.Driver",
            "jdbc:h2:mem:test",
            "sa",
            ""
        );

        // 创建事务工厂（抽象工厂的另一种形式）
        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        // 创建环境配置
        Environment environment = new Environment(
            "development",
            transactionFactory,
            dataSource
        );

        // 创建配置对象
        org.apache.ibatis.session.Configuration configuration =
            new org.apache.ibatis.session.Configuration(environment);

        // 创建 SqlSessionFactory（抽象工厂）
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        System.out.println("SqlSessionFactory 类型: " + sqlSessionFactory.getClass().getName());

        // 通过工厂创建 SqlSession（产品）
        try (SqlSession session = sqlSessionFactory.openSession()) {
            System.out.println("SqlSession 类型: " + session.getClass().getName());
            System.out.println("成功通过工厂创建 SqlSession");
        }
    }

    /**
     * 2. ObjectFactory 示例
     *
     * ObjectFactory 负责创建 MyBatis 需要的各种对象实例
     */
    public static void objectFactoryExample() {
        System.out.println("\n=== ObjectFactory 示例 ===");

        // 使用默认的对象工厂
        org.apache.ibatis.reflection.factory.ObjectFactory defaultFactory =
            new org.apache.ibatis.reflection.factory.DefaultObjectFactory();

        System.out.println("ObjectFactory 类型: " + defaultFactory.getClass().getName());

        // 使用工厂创建对象
        User user = defaultFactory.create(User.class);
        System.out.println("通过工厂创建的 User: " + user.getClass().getName());

        // 创建带参数的对象
     }

    /**
     * 3. DataSourceFactory 示例
     *
     * DataSourceFactory 是数据源的抽象工厂，
     * MyBatis提供了多种实现：Pooled、Unpooled、JNDI
     */
    public static void dataSourceFactoryExample() {
        System.out.println("\n=== DataSourceFactory 示例 ===");

        // UnpooledDataSourceFactory（无池化数据源工厂）
        org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory unpooledFactory =
            new org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory();
        Properties unpooledProps = new Properties();
        unpooledProps.setProperty("driver", "org.h2.Driver");
        unpooledProps.setProperty("url", "jdbc:h2:mem:test");
        unpooledProps.setProperty("username", "sa");
        unpooledProps.setProperty("password", "");
        unpooledFactory.setProperties(unpooledProps);

        DataSource unpooledDataSource = unpooledFactory.getDataSource();
        System.out.println("UnpooledDataSource: " + unpooledDataSource.getClass().getName());

        // PooledDataSourceFactory（池化数据源工厂）
        org.apache.ibatis.datasource.pooled.PooledDataSourceFactory pooledFactory =
            new org.apache.ibatis.datasource.pooled.PooledDataSourceFactory();
        Properties pooledProps = new Properties();
        pooledProps.setProperty("driver", "org.h2.Driver");
        pooledProps.setProperty("url", "jdbc:h2:mem:test");
        pooledProps.setProperty("username", "sa");
        pooledProps.setProperty("password", "");
        pooledProps.setProperty("poolMaximumActiveConnections", "10");
        pooledFactory.setProperties(pooledProps);

        DataSource pooledDataSource = pooledFactory.getDataSource();
        System.out.println("PooledDataSource: " + pooledDataSource.getClass().getName());
    }

    /**
     * 4. TransactionFactory 示例
     *
     * TransactionFactory 是事务工厂接口，
     * 提供了 JDBC 和 MANAGED 两种实现
     */
    public static void transactionFactoryExample() {
        System.out.println("\n=== TransactionFactory 示例 ===");

        DataSource dataSource = new UnpooledDataSource(
            "org.h2.Driver",
            "jdbc:h2:mem:test",
            "sa",
            ""
        );

        // JDBC 事务工厂
        TransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();
        org.apache.ibatis.transaction.Transaction jdbcTransaction =
            jdbcTransactionFactory.newTransaction(
                dataSource,
                org.apache.ibatis.session.TransactionIsolationLevel.READ_COMMITTED,
                false
            );
        System.out.println("JDBC Transaction: " + jdbcTransaction.getClass().getName());

        // Managed 事务工厂
        TransactionFactory managedTransactionFactory = new org.apache.ibatis.transaction.managed.ManagedTransactionFactory();
        org.apache.ibatis.transaction.Transaction managedTransaction =
            managedTransactionFactory.newTransaction(dataSource, null, false);
        System.out.println("Managed Transaction: " + managedTransaction.getClass().getName());
    }

    /**
     * 5. CacheFactory 示例
     *
     * MyBatis使用Cache接口和装饰器模式实现缓存，
     * 可以直接创建缓存实例
     */
    public static void cacheFactoryExample() {
        System.out.println("\n=== Cache 示例 ===");
    }

    /**
     * 6. TypeHandlerFactory 示例
     *
     * TypeHandlerRegistry 充当类型处理器的工厂，
     * 负责创建和注册TypeHandler
     */
    public static void typeHandlerFactoryExample() {
        System.out.println("\n=== TypeHandlerRegistry 示例 ===");

        org.apache.ibatis.session.Configuration configuration =
            new org.apache.ibatis.session.Configuration();

        org.apache.ibatis.type.TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();

        // 通过注册表获取类型处理器
        org.apache.ibatis.type.TypeHandler<String> stringHandler =
            registry.getTypeHandler(String.class);

        System.out.println("String TypeHandler: " + stringHandler.getClass().getName());

        // 注册自定义类型处理器
        registry.register(String.class, CustomStringTypeHandler.class);
        System.out.println("已注册自定义类型处理器");
    }

    /**
     * 7. MapperProxyFactory 示例
     *
     * MapperProxyFactory 是 Mapper 接口的代理工厂，
     * 负责创建 Mapper 接口的代理实例
     */
    public static void mapperProxyFactoryExample() {
        System.out.println("\n=== MapperProxyFactory 示例 ===");

        // 创建配置
        org.apache.ibatis.session.Configuration configuration =
            new org.apache.ibatis.session.Configuration();

        // 添加 Mapper 接口
        configuration.addMapper(UserMapper.class);

        // MapperProxyFactory 在内部被使用
        // 当调用 getMapper 时，会使用 MapperProxyFactory 创建代理
        System.out.println("MapperProxyFactory 由 MapperRegistry 内部使用");
        System.out.println("UserMapper 接口已注册: " +
            configuration.hasMapper(UserMapper.class));
    }

    /**
     * 8. 构建器工厂示例
     *
     * BaseBuilder 及其子类充当配置的构建工厂
     */
    public static void builderFactoryExample() {
        System.out.println("\n=== 构建器工厂 示例 ===");

        // XMLConfigBuilder - 配置构建器
        // XMLMapperBuilder - Mapper构建器
        // SqlSourceBuilder - SQL源构建器
        // ParameterExpression - 参数表达式构建器
        // etc.

        System.out.println("MyBatis 构建器类家族:");
        System.out.println("  - XMLConfigBuilder: 解析 mybatis-config.xml");
        System.out.println("  - XMLMapperBuilder: 解析 Mapper XML 文件");
        System.out.println("  - XMLStatementBuilder: 解析 SQL 语句");
        System.out.println("  - SqlSourceBuilder: 构建 SqlSource");
        System.out.println("  - ParameterExpression: 解析参数表达式");
    }

    /**
     * 9. 完整的抽象工厂应用示例
     *
     * 展示如何使用抽象工厂模式创建不同类型的SqlSession
     */
    public static void completeFactoryExample() {
        System.out.println("\n=== 完整抽象工厂应用示例 ===");

        // 创建数据源工厂并获取数据源
        DataSourceFactory dsFactory = createDataSourceFactory("pooled");
        DataSource dataSource = dsFactory.getDataSource();

        // 创建事务工厂并获取事务
        TransactionFactory txFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", txFactory, dataSource);

        // 创建配置
        org.apache.ibatis.session.Configuration configuration =
            new org.apache.ibatis.session.Configuration(environment);

        // 创建 SqlSession 工厂
        SqlSessionFactory sqlSessionFactory =
            new SqlSessionFactoryBuilder().build(configuration);

        System.out.println("成功创建完整的 MyBatis 工厂链:");
        System.out.println("  1. DataSourceFactory -> DataSource");
        System.out.println("  2. TransactionFactory -> Transaction");
        System.out.println("  3. Environment");
        System.out.println("  4. Configuration");
        System.out.println("  5. SqlSessionFactory -> SqlSession");
    }

    // ==================== 辅助类 ====================

    /**
     * 数据源工厂辅助方法
     */
    private static DataSourceFactory createDataSourceFactory(String type) {
        Properties props = new Properties();
        props.setProperty("driver", "org.h2.Driver");
        props.setProperty("url", "jdbc:h2:mem:test");
        props.setProperty("username", "sa");
        props.setProperty("password", "");

        if ("pooled".equalsIgnoreCase(type)) {
            org.apache.ibatis.datasource.pooled.PooledDataSourceFactory factory =
                new org.apache.ibatis.datasource.pooled.PooledDataSourceFactory();
            factory.setProperties(props);
            return new DataSourceFactoryAdapter(factory);
        } else {
            org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory factory =
                new org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory();
            factory.setProperties(props);
            return new DataSourceFactoryAdapter(factory);
        }
    }

    /**
     * 数据源工厂适配器
     */
    static class DataSourceFactoryAdapter implements DataSourceFactory {
        private final org.apache.ibatis.datasource.DataSourceFactory delegate;

        public DataSourceFactoryAdapter(org.apache.ibatis.datasource.DataSourceFactory delegate) {
            this.delegate = delegate;
        }

        @Override
        public void setProperties(Properties properties) {
            delegate.setProperties(properties);
        }

        @Override
        public DataSource getDataSource() {
            return delegate.getDataSource();
        }
    }

    /**
     * 数据源工厂接口（简化版）
     */
    interface DataSourceFactory {
        void setProperties(Properties properties);
        DataSource getDataSource();
    }

    /**
     * 用户实体
     */
    static class User {
        private String name;
        private Integer age;

        public User() {}

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) { this.name = name; }
        public void setAge(Integer age) { this.age = age; }

        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }

    /**
     * 用户Mapper接口
     */
    interface UserMapper {
        User selectById(Long id);
        void insert(User user);
    }

    /**
     * 自定义字符串类型处理器
     */
    static class CustomStringTypeHandler extends org.apache.ibatis.type.BaseTypeHandler<String> {
        @Override
        public void setNonNullParameter(
            java.sql.PreparedStatement ps,
            int i,
            String parameter,
            org.apache.ibatis.type.JdbcType jdbcType
        ) throws java.sql.SQLException {
            ps.setString(i, parameter);
        }

        @Override
        public String getNullableResult(
            java.sql.ResultSet rs,
            String columnName
        ) throws java.sql.SQLException {
            return rs.getString(columnName);
        }

        @Override
        public String getNullableResult(
            java.sql.ResultSet rs,
            int columnIndex
        ) throws java.sql.SQLException {
            return rs.getString(columnIndex);
        }

        @Override
        public String getNullableResult(
            java.sql.CallableStatement cs,
            int columnIndex
        ) throws java.sql.SQLException {
            return cs.getString(columnIndex);
        }
    }

    public static void main(String[] args) {
        sqlSessionFactoryExample();
        objectFactoryExample();
        dataSourceFactoryExample();
        transactionFactoryExample();
        cacheFactoryExample();
        typeHandlerFactoryExample();
        mapperProxyFactoryExample();
        builderFactoryExample();
        completeFactoryExample();
    }
}
