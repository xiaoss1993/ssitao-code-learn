package com.ssitao.code.designpattern.singleton;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Spring中的单例模式
 *
 * Spring默认使用单例模式(Scope=Singleton)
 * 整个容器只有一个Bean实例
 */
public class SpringSingletonExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        System.out.println("========== Spring单例模式 ==========\n");

        // 获取两次实例
        UserService user1 = context.getBean(UserService.class);
        UserService user2 = context.getBean(UserService.class);

        System.out.println("UserService1: " + user1.hashCode());
        System.out.println("UserService2: " + user2.hashCode());
        System.out.println("是同一实例: " + (user1 == user2));

        // 原型Bean测试
        System.out.println("\n--- 原型Bean对比 ---");
        OrderService order1 = context.getBean(OrderService.class);
        OrderService order2 = context.getBean(OrderService.class);

        System.out.println("OrderService1: " + order1.hashCode());
        System.out.println("OrderService2: " + order2.hashCode());
        System.out.println("是同一实例: " + (order1 == order2));
    }

    @Configuration
    static class AppConfig {

        // 默认单例
        @Bean
        public UserService userService() {
            return new UserService();
        }

        // 原型Bean
        @Bean
        @Scope("prototype")
        public OrderService orderService() {
            return new OrderService();
        }
    }

    static class UserService {
        public UserService() {
            System.out.println("UserService 构造方法被调用");
        }
    }

    static class OrderService {
        public OrderService() {
            System.out.println("OrderService 构造方法被调用");
        }
    }
}
