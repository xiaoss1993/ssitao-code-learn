package com.ssitao.code.designpattern.prototype.spring;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Spring中原型模式示例 - 使用ObjectFactory获取原型Bean
 */
@Service
public class OrderService {

    /**
     * 使用ObjectFactory，每次调用getObject()都会创建新的原型Bean实例
     */
    @Autowired
    private ObjectFactory<Order> orderObjectFactory;

    /**
     * 使用ObjectProvider (Spring 5.1+)
     */
    @Autowired
    private org.springframework.beans.factory.ObjectProvider<Order> orderObjectProvider;

    /**
     * 通过ObjectFactory创建新订单
     */
    public Order createOrderViaFactory(String productName, int quantity, double price) {
        Order order = orderObjectFactory.getObject();
        order.createOrder(productName, quantity, price);
        return order;
    }

    /**
     * 通过ObjectProvider创建新订单
     */
    public Order createOrderViaProvider(String productName, int quantity, double price) {
        Order order = orderObjectProvider.getObject();
        order.createOrder(productName, quantity, price);
        return order;
    }

    /**
     * 演示每次获取都是新实例
     */
    public void demonstratePrototype() {
        System.out.println("=== 演示原型模式 ===");

        Order order1 = orderObjectFactory.getObject();
        Order order2 = orderObjectFactory.getObject();

        System.out.println("Order1 ID: " + order1.getOrderId());
        System.out.println("Order2 ID: " + order2.getOrderId());
        System.out.println("是同一实例? " + (order1 == order2));
    }
}
