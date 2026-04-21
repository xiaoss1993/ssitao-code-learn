package com.ssitao.code.designpattern.prototype.spring;

import org.springframework.stereotype.Component;

/**
 * 订单验证器
 */
@Component
public class OrderValidator {

    public void validate(Order order) {
        if (order.getProductName() == null || order.getProductName().isEmpty()) {
            throw new IllegalArgumentException("产品名称不能为空");
        }
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }
        if (order.getPrice() <= 0) {
            throw new IllegalArgumentException("价格必须大于0");
        }
        System.out.println("订单验证通过: " + order.getOrderId());
    }
}
