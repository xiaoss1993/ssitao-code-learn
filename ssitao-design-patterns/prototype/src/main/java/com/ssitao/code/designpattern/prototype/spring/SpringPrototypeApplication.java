package com.ssitao.code.designpattern.prototype.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring 原型模式示例启动类
 */
public class SpringPrototypeApplication {

    public static void main(String[] args) {
        // 使用AnnotationConfigApplicationContext替代SpringApplication
        ApplicationContext context = new AnnotationConfigApplicationContext(
                SpringPrototypeConfig.class
        );

        System.out.println("\n========== Spring 原型模式示例 ==========\n");

        // 1. 演示每次注入都是新实例
        System.out.println("--- 方式1: 直接注入每次创建新实例 ---");
        OrderService orderService = context.getBean(OrderService.class);
        orderService.demonstratePrototype();

        System.out.println("\n--- 方式2: 使用ObjectFactory ---");
        Order order1 = orderService.createOrderViaFactory("iPhone", 1, 9999.0);
        Order order2 = orderService.createOrderViaFactory("MacBook", 1, 15000.0);

        System.out.println("Order1: " + order1);
        System.out.println("Order2: " + order2);
        System.out.println("是同一实例? " + (order1 == order2));

        // 3. 演示配置类中的原型Bean
        System.out.println("\n--- 方式3: 使用配置类中的原型Bean ---");
        Product product1 = context.getBean(Product.class);
        Product product2 = context.getBean(Product.class);
        System.out.println("Product1: " + product1);
        System.out.println("Product2: " + product2);
        System.out.println("是同一实例? " + (product1 == product2));

        ShoppingCart cart1 = context.getBean(ShoppingCart.class);
        ShoppingCart cart2 = context.getBean(ShoppingCart.class);
        System.out.println("Cart1: " + cart1);
        System.out.println("Cart2: " + cart2);
        System.out.println("是同一实例? " + (cart1 == cart2));

        // 4. 实际使用场景：每个用户有自己的购物车
        System.out.println("\n--- 实际场景: 每个用户独立购物车 ---");
        ShoppingCart userCart1 = new ShoppingCart("user001");
        ShoppingCart userCart2 = new ShoppingCart("user002");

        userCart1.addProduct(new Product("Java核心技术", 89.0));
        userCart1.addProduct(new Product("Spring Boot实战", 119.0));

        userCart2.addProduct(new Product("Effective Java", 79.0));

        System.out.println("用户1购物车: " + userCart1);
        System.out.println("用户2购物车: " + userCart2);
    }
}
