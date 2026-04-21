package com.ssitao.code.designpattern.proxy.spring;

/**
 * Spring AOP示例 - 业务实现
 */
public class UserServiceImpl implements UserService {

    @Override
    public void createOrder(String product) {
        System.out.println("创建订单: " + product);
    }

    @Override
    public String queryOrder(String orderId) {
        System.out.println("查询订单: " + orderId);
        return "Order_" + orderId;
    }
}
