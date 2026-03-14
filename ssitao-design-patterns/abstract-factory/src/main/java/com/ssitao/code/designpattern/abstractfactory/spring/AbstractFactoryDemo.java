package com.ssitao.code.designpattern.abstractfactory.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * Spring中的抽象工厂模式示例
 *
 * Spring框架中大量使用了抽象工厂模式，这里展示几个典型应用：
 *
 * 1. BeanFactory - Spring的核心容器，是Bean的抽象工厂
 * 2. ApplicationContext - BeanFactory的扩展，提供更多企业级功能
 * 3. AutowireCapableBeanFactory - 自动装配Bean的工厂
 * 4. BeanDefinitionRegistry - Bean定义的注册工厂
 * 5. ObjectProvider - 延迟依赖查找和获取Bean的工厂
 */
public class AbstractFactoryDemo {

    /**
     * 1. BeanFactory 示例
     *
     * BeanFactory 是 Spring 中最核心的抽象工厂接口，
     * 负责创建和管理 Spring Bean
     */
    public static void beanFactoryExample() {
        System.out.println("=== BeanFactory 示例 ===");

        // 创建 BeanFactory 实例（使用 DefaultListableBeanFactory）
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 注册 Bean 定义
        AbstractBeanDefinition userBeanDefinition = BeanDefinitionBuilder
            .genericBeanDefinition(User.class)
            .addPropertyValue("name", "张三")
            .addPropertyValue("age", 25)
            .getBeanDefinition();

        beanFactory.registerBeanDefinition("user", userBeanDefinition);

        // 通过工厂获取 Bean
        User user = beanFactory.getBean("user", User.class);
        System.out.println("获取到的用户: " + user);
        System.out.println("BeanFactory 类型: " + beanFactory.getClass().getName());
    }

    /**
     * 2. ApplicationContext 示例
     *
     * ApplicationContext 是 BeanFactory 的扩展接口，
     * 提供了更多企业级功能，是更高级的抽象工厂
     */
    public static void applicationContextExample() {
        System.out.println("\n=== ApplicationContext 示例 ===");

        // 使用注解配置的 ApplicationContext
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 获取 Bean（通过工厂方法）
        UserService userService = context.getBean(UserService.class);
        OrderService orderService = context.getBean(OrderService.class);

        System.out.println("UserService: " + userService.getClass().getName());
        System.out.println("OrderService: " + orderService.getClass().getName());
        System.out.println("ApplicationContext 类型: " + context.getClass().getName());

        // 获取某个接口的所有实现（抽象工厂的典型应用）
        System.out.println("\n所有 Service 类型的 Bean:");
        Map<String, Object> services = context.getBeansWithAnnotation(org.springframework.stereotype.Service.class);
        services.forEach((name, bean) -> System.out.println("  - " + name + ": " + bean.getClass().getSimpleName()));

        ((AnnotationConfigApplicationContext) context).close();
    }

    /**
     * 3. AutowireCapableBeanFactory 示例
     *
     * AutowireCapableBeanFactory 提供了自动装配能力，
     * 可以创建和装配 Bean 的依赖关系
     */
    public static void autowireCapableBeanFactoryExample() {
        System.out.println("\n=== AutowireCapableBeanFactory 示例 ===");

        AutowireCapableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 自动创建并装配 Bean
        OrderService orderService = beanFactory.createBean(OrderService.class);
        System.out.println("自动创建的 OrderService: " + orderService.getClass().getName());
    }

    /**
     * 4. BeanDefinitionRegistry 示例
     *
     * BeanDefinitionRegistry 是 Bean 定义的注册工厂，
     * 可以动态注册和管理 Bean 定义
     */
    public static void beanDefinitionRegistryExample() {
        System.out.println("\n=== BeanDefinitionRegistry 示例 ===");

        // BeanDefinitionRegistry 通常由 BeanFactory 实现
        DefaultListableBeanFactory registry = new DefaultListableBeanFactory();

        // 动态注册多个 Bean 定义
        registry.registerBeanDefinition("userService1",
            BeanDefinitionBuilder.genericBeanDefinition(UserService.class).getBeanDefinition());

        registry.registerBeanDefinition("userService2",
            BeanDefinitionBuilder.genericBeanDefinition(UserService.class).getBeanDefinition());

        // 获取所有 Bean 名称
        String[] beanNames = registry.getBeanDefinitionNames();
        System.out.println("注册的 Bean 数量: " + beanNames.length);
        for (String beanName : beanNames) {
            System.out.println("  - " + beanName);
        }
    }

    /**
     * 5. ObjectProvider 示例
     *
     * ObjectProvider 是 Spring 5 引入的，用于延迟查找和获取 Bean，
     * 是一种增强型的工厂接口
     */
    public static void objectProviderExample() {
        System.out.println("\n=== ObjectProvider 示例 ===");

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 获取 ObjectProvider（工厂的一种形式）
        org.springframework.beans.factory.ObjectProvider<UserService> userServiceProvider =
            context.getBeanProvider(UserService.class);

        // 延迟获取 Bean
        UserService userService = userServiceProvider.getIfAvailable(() -> new UserService());
        System.out.println("通过 ObjectProvider 获取: " + userService.getClass().getName());

        // 获取所有实现
        userServiceProvider.forEach(service ->
            System.out.println("  - UserService 实现: " + service.getClass().getSimpleName()));

        ((AnnotationConfigApplicationContext) context).close();
    }

    /**
     * 6. 自定义抽象工厂在Spring中的应用示例
     *
     * 演示如何在 Spring 中实现和使用自定义的抽象工厂模式
     */
    public static void customAbstractFactoryExample() {
        System.out.println("\n=== 自定义抽象工厂示例 ===");

        // 使用 Spring 容器管理工厂
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 获取工厂实例
        DatabaseConnectionFactory connectionFactory =
            context.getBean(DatabaseConnectionFactory.class);

        // 使用工厂创建产品
        Connection connection = connectionFactory.createConnection();
        connection.connect();

        ((AnnotationConfigApplicationContext) context).close();
    }

    // ==================== 辅助类和配置 ====================

    /**
     * 配置类
     */
    @org.springframework.context.annotation.Configuration
    static class AppConfig {

        @org.springframework.context.annotation.Bean
        public UserService userService() {
            return new UserService();
        }

        @org.springframework.context.annotation.Bean
        public OrderService orderService() {
            return new OrderService();
        }

        @org.springframework.context.annotation.Bean
        public DatabaseConnectionFactory connectionFactory() {
            return new MysqlConnectionFactory();
        }
    }

    /**
     * 用户实体
     */
    static class User {
        private String name;
        private int age;

        public User() {}

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) { this.name = name; }
        public void setAge(int age) { this.age = age; }

        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }

    /**
     * 用户服务
     */
    @org.springframework.stereotype.Service
    static class UserService {
        public void createUser() {
            System.out.println("创建用户...");
        }
    }

    /**
     * 订单服务
     */
    @org.springframework.stereotype.Service
    static class OrderService {
        public void createOrder() {
            System.out.println("创建订单...");
        }
    }

    // ==================== 自定义抽象工厂示例 ====================

    /**
     * 数据库连接接口（抽象产品）
     */
    interface Connection {
        void connect();
        void disconnect();
    }

    /**
     * MySQL连接实现（具体产品）
     */
    static class MySqlConnection implements Connection {
        @Override
        public void connect() {
            System.out.println("连接到 MySQL 数据库");
        }

        @Override
        public void disconnect() {
            System.out.println("断开 MySQL 连接");
        }
    }

    /**
     * PostgreSQL连接实现（具体产品）
     */
    static class PostgreSqlConnection implements Connection {
        @Override
        public void connect() {
            System.out.println("连接到 PostgreSQL 数据库");
        }

        @Override
        public void disconnect() {
            System.out.println("断开 PostgreSQL 连接");
        }
    }

    /**
     * 数据库连接工厂接口（抽象工厂）
     */
    interface DatabaseConnectionFactory {
        Connection createConnection();
    }

    /**
     * MySQL连接工厂（具体工厂）
     */
    static class MysqlConnectionFactory implements DatabaseConnectionFactory {
        @Override
        public Connection createConnection() {
            return new MySqlConnection();
        }
    }

    /**
     * PostgreSQL连接工厂（具体工厂）
     */
    static class PostgreSqlConnectionFactory implements DatabaseConnectionFactory {
        @Override
        public Connection createConnection() {
            return new PostgreSqlConnection();
        }
    }

    public static void main(String[] args) {
        beanFactoryExample();
        applicationContextExample();
        autowireCapableBeanFactoryExample();
        beanDefinitionRegistryExample();
        objectProviderExample();
        customAbstractFactoryExample();
    }
}
