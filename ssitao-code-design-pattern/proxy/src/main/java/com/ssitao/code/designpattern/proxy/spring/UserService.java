package com.ssitao.code.designpattern.proxy.spring;

/**
 * Spring AOP示例 - 业务接口
 */
public interface UserService {

    // 创建订单
    void createOrder(String product);

    // 查询订单
    String queryOrder(String orderId);
}
