package com.ssitao.code.designpattern.prototype.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Spring中原型模式示例 - 原型Bean
 * 使用 @Scope("prototype") 注解，每次注入都会创建新实例
 */
@Component
@Scope("prototype")
public class Order {

    private String orderId;
    private String productName;
    private int quantity;
    private double price;

    public Order() {
        // 构造方法中生成唯一订单ID
        this.orderId = UUID.randomUUID().toString();
    }

    @Autowired
    private OrderValidator validator;

    public void createOrder(String productName, int quantity, double price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        System.out.println("创建订单: " + this);
        validator.validate(this);
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderValidator getValidator() {
        return validator;
    }

    public void setValidator(OrderValidator validator) {
        this.validator = validator;
    }

    @Override
    public String toString() {
        return "Order{orderId='" + orderId + "', productName='" + productName +
                "', quantity=" + quantity + ", price=" + price + "}";
    }
}
