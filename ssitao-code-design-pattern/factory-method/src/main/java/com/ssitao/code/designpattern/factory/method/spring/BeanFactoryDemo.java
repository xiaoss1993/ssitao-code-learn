package com.ssitao.code.designpattern.factory.method.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.io.ClassPathResource;

/**
 * Spring 工厂方法模式示例 - BeanFactory
 *
 * BeanFactory 是 Spring 容器的核心接口，是工厂方法模式的典型应用
 * getBean() 是核心工厂方法，根据 bean 名称/类型返回 bean 实例
 *
 * 工厂方法模式体现：
 * - 抽象工厂：BeanFactory
 * - 具体工厂：XmlBeanFactory, DefaultListableBeanFactory, ApplicationContext 等
 * - 抽象产品：Object（所有 Bean 的父类）
 * - 具体产品：UserService, OrderService 等各种 Spring 管理的 Bean
 *
 * BeanFactory 继承体系：
 * - BeanFactory (根接口)
 *   - ListableBeanFactory
 *   - HierarchicalBeanFactory
 *   - AutowireCapableBeanFactory
 *   - ConfigurableBeanFactory
 *   - DefaultListableBeanFactory (最常用的实现)
 *
 * 注意：XmlBeanFactory 在 Spring 5.1 已废弃，推荐使用 DefaultListableBeanFactory 或 ApplicationContext
 */
public class BeanFactoryDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring BeanFactory 工厂方法示例 ===\n");

        // 1. 使用 XmlBeanFactory（已废弃但经典示例）
        // BeanFactory factory = new XmlBeanFactory(new ClassPathResource("beans.xml"));

        // 2. 使用 DefaultListableBeanFactory + BeanDefinitionReader
        org.springframework.beans.factory.support.DefaultListableBeanFactory factory =
                new org.springframework.beans.factory.support.DefaultListableBeanFactory();

        // 注册 Bean 定义（编程式方式）
        registerUserService(factory);

        // 3. 使用 getBean() 工厂方法获取 Bean
        System.out.println("--- 使用 getBean() 工厂方法 ---");

        // 按名称获取
        Object userService1 = factory.getBean("userService");
        System.out.println("按名称获取: " + userService1.getClass().getSimpleName());

        // 按类型获取
        UserService userService2 = factory.getBean(UserService.class);
        System.out.println("按类型获取: " + userService2.getClass().getSimpleName());

        // 按名称+类型获取
        UserService userService3 = factory.getBean("userService", UserService.class);
        System.out.println("按名称+类型获取: " + userService3.getClass().getSimpleName());

        // 4. 演示工厂方法的其他变体
        System.out.println("\n--- 工厂方法的其他变体 ---");
        System.out.println("isSingleton(\"userService\"): " + factory.isSingleton("userService"));
        System.out.println("getBeanType(\"userService\"): " + factory.getType("userService"));
        System.out.println("containsBean(\"userService\"): " + factory.containsBean("userService"));

        // 5. 使用 ApplicationContext（更现代的方式）
        System.out.println("\n--- 使用 ApplicationContext ---");
        org.springframework.context.annotation.AnnotationConfigApplicationContext context =
                new org.springframework.context.annotation.AnnotationConfigApplicationContext(
                        AppConfig2.class);

        UserService userServiceFromContext = context.getBean(UserService.class);
        System.out.println("从 ApplicationContext 获取: " + userServiceFromContext.getClass().getSimpleName());
    }

    /**
     * 编程式注册 Bean 定义
     */
    private static void registerUserService(org.springframework.beans.factory.support.DefaultListableBeanFactory factory) {
        // 创建 BeanDefinition
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(UserService.class);
        builder.addPropertyValue("name", "Spring User");
        org.springframework.beans.factory.config.BeanDefinition beanDefinition = builder.getBeanDefinition();

        // 注册到容器
        factory.registerBeanDefinition("userService", beanDefinition);
    }

    /**
     * 简单的用户服务类
     */
    public static class UserService {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void sayHello() {
            System.out.println("  Hello, I am " + name + "!");
        }
    }

    /**
     * Spring 配置类
     */
    public static class AppConfig2 {
        @org.springframework.context.annotation.Bean
        public UserService userService() {
            UserService userService = new UserService();
            userService.setName("Config User");
            return userService;
        }
    }
}

