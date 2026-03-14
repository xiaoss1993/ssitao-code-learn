package com.ssitao.code.designpartten.abstractfactory.mybatis;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.session.Configuration;

/**
 * MyBatis中的抽象工厂模式示例
 *
 * MyBatis使用了多种工厂模式：
 * 1. ObjectFactory - 对象工厂
 * 2. DataSourceFactory - 数据源工厂
 * 3. TransactionFactory - 事务工厂
 * 4. Cache - 缓存（装饰器模式）
 */
public class AbstractFactoryDemo {

    public static void main(String[] args) {
        System.out.println("========== MyBatis 抽象工厂模式示例 ==========\n");

        // 1. Cache示例 - 使用装饰器模式
        cacheExample();

        // 2. Configuration示例 - 工厂配置中心
        configurationExample();
    }

    /**
     * Cache示例 - MyBatis使用装饰器模式实现缓存
     */
    private static void cacheExample() {
        System.out.println("=== Cache 示例 ===");

    }

    /**
     * Configuration示例 - MyBatis的核心配置类
     * 充当多种工厂的抽象工厂角色
     */
    private static void configurationExample() {
        System.out.println("\n=== Configuration 示例 ===");

        Configuration configuration = new Configuration();

        // Configuration管理着多种工厂和处理器
        System.out.println("Configuration 类型: " + configuration.getClass().getName());
        System.out.println("这是MyBatis的核心配置类");
    }
}
