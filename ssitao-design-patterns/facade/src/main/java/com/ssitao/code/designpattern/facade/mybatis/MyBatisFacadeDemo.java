package com.ssitao.code.designpattern.facade.mybatis;

/**
 * MyBatis中的外观模式示例
 *
 * MyBatis中使用外观模式的场景：
 * 1. SqlSessionFactory - 统一管理SqlSession创建
 * 2. Configuration - 统一配置管理
 * 3. TransactionFactory - 统一事务管理
 */
public class MyBatisFacadeDemo {

    public static void main(String[] args) {
        System.out.println("=== MyBatis Facade Pattern Demo ===\n");

        // 1. SqlSessionFactory - 统一管理SqlSession
        demonstrateSqlSessionFactory();

        // 2. Configuration - 统一配置管理
        demonstrateConfiguration();

        // 3. TransactionFactory - 统一事务管理
        demonstrateTransactionFactory();
    }

    /**
     * 1. SqlSessionFactory - SqlSession工厂
     * 外观模式：统一管理SqlSession的创建
     */
    private static void demonstrateSqlSessionFactory() {
        System.out.println("--- 1. SqlSessionFactory (统一SqlSession创建) ---");

        // SqlSessionFactory隐藏了：
        // 1. Configuration的加载和解析
        // 2. Mapper文件的解析
        // 3. SQL语句的预编译
        // 4. Statement的创建

        System.out.println("SqlSessionFactory外观模式：");
        System.out.println("  - 隐藏了Configuration的复杂性");
        System.out.println("  - 统一管理SqlSession生命周期");
        System.out.println("  - 维护连接池");
        System.out.println("  - 预编译SQL语句");

        // 使用流程
        System.out.println("\n使用流程：");
        System.out.println("  1. SqlSessionFactoryBuilder读取配置文件");
        System.out.println("  2. 创建SqlSessionFactory");
        System.out.println("  3. 通过SqlSessionFactory.openSession()获取SqlSession");
        System.out.println("  4. 通过SqlSession执行SQL");

        System.out.println();
    }

    /**
     * 2. Configuration - 统一配置管理
     * Configuration是MyBatis的核心配置类
     */
    private static void demonstrateConfiguration() {
        System.out.println("--- 2. Configuration (统一配置管理) ---");

        // Configuration隐藏了：
        // 1. 环境配置（数据源、事务）
        // 2. Mapper注册
        // 3. 类型别名
        // 4. 类型处理器
        // 5. 语句映射

        System.out.println("Configuration统一管理：");
        System.out.println("  - Environment: 环境配置（数据源、事务）");
        System.out.println("  - MapperRegistry: Mapper注册");
        System.out.println("  - TypeAliasRegistry: 类型别名");
        System.out.println("  - TypeHandlerRegistry: 类型处理器");
        System.out.println("  - MappedStatements: SQL语句映射");
        System.out.println("  - ResultMaps: 结果集映射");

        System.out.println();
    }

    /**
     * 3. TransactionFactory - 事务工厂
     * TransactionFactory是事务管理的外观
     */
    private static void demonstrateTransactionFactory() {
        System.out.println("--- 3. TransactionFactory (统一事务管理) ---");

        // TransactionFactory隐藏了：
        // 1. JDBC事务管理
        // 2. 容器事务管理（如Spring）
        // 3. 事务的创建和提交

        System.out.println("TransactionFactory实现：");
        System.out.println("  - JdbcTransactionFactory: JDBC事务");
        System.out.println("  - ManagedTransactionFactory: 容器管理事务");
        System.out.println("  - SpringManagedTransactionFactory: Spring事务");

        System.out.println("\n事务接口方法：");
        System.out.println("  - getConnection() - 获取连接");
        System.out.println("  - commit() - 提交");
        System.out.println("  - rollback() - 回滚");
        System.out.println("  - close() - 关闭");

        System.out.println();
    }
}

/**
 * 模拟MyBatis的SqlSessionFactory
 */
interface SqlSessionFactory {
    SqlSession openSession();
}

/**
 * 模拟MyBatis的SqlSession
 */
interface SqlSession {
    <T> T getMapper(Class<T> type);
    void commit();
    void rollback();
    void close();
}

/**
 * 模拟MyBatis的Configuration
 */
class Configuration {
    private Environment environment;
    private MapperRegistry mapperRegistry;
    private TypeAliasRegistry typeAliasRegistry;
    private TypeHandlerRegistry typeHandlerRegistry;

    public Configuration() {
        this.environment = new Environment("development", new JdbcTransactionFactory());
        this.mapperRegistry = new MapperRegistry();
        this.typeAliasRegistry = new TypeAliasRegistry();
        this.typeHandlerRegistry = new TypeHandlerRegistry();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }
}

/**
 * 模拟MyBatis的环境配置
 */
class Environment {
    private String id;
    private TransactionFactory transactionFactory;

    public Environment(String id, TransactionFactory transactionFactory) {
        this.id = id;
        this.transactionFactory = transactionFactory;
    }

    public String getId() {
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }
}

/**
 * 模拟事务工厂
 */
interface TransactionFactory {
    Transaction newTransaction();
}

/**
 * 模拟JDBC事务工厂
 */
class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction() {
        System.out.println("创建JDBC事务");
        return new JdbcTransaction();
    }
}

/**
 * 模拟事务接口
 */
interface Transaction {
    Connection getConnection();
    void commit();
    void rollback();
    void close();
}

/**
 * 模拟JDBC事务
 */
class JdbcTransaction implements Transaction {
    private Connection connection;

    @Override
    public Connection getConnection() {
        System.out.println("JDBC获取连接");
        return null;
    }

    @Override
    public void commit() {
        System.out.println("JDBC提交事务");
    }

    @Override
    public void rollback() {
        System.out.println("JDBC回滚事务");
    }

    @Override
    public void close() {
        System.out.println("JDBC关闭连接");
    }
}

/**
 * 模拟Mapper注册表
 */
class MapperRegistry {
    public <T> void addMapper(Class<T> type) {
        System.out.println("注册Mapper: " + type.getName());
    }
}

/**
 * 模拟类型别名注册表
 */
class TypeAliasRegistry {
    public void registerAlias(Class<?> type) {
        System.out.println("注册类型别名: " + type.getName());
    }
}

/**
 * 模拟类型处理器注册表
 */
class TypeHandlerRegistry {
    public void register(Class<?> typeHandler) {
        System.out.println("注册TypeHandler: " + typeHandler.getName());
    }
}
