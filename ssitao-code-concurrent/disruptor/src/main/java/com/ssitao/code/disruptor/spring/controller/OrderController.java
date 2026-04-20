package com.ssitao.code.disruptor.spring.controller;

import com.ssitao.code.disruptor.spring.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器 - 提供 HTTP 接口触发 Disruptor 处理
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 下单接口
     * GET /order/place?orderId=xxx&symbol=AAPL&price=100.0&quantity=10
     */
    @GetMapping("/order/place")
    public String placeOrder(
            @RequestParam String orderId,
            @RequestParam String symbol,
            @RequestParam double price,
            @RequestParam int quantity) {
        orderService.placeOrder(orderId, symbol, price, quantity);
        return "订单已提交: " + orderId;
    }

    /**
     * 批量下单接口
     * GET /order/batch?count=100
     */
    @GetMapping("/order/batch")
    public String placeOrders(@RequestParam(defaultValue = "100") int count) {
        long startTime = System.currentTimeMillis();
        orderService.placeOrders(count);
        long endTime = System.currentTimeMillis();
        return String.format("批量提交 %d 订单完成，耗时: %d ms", count, (endTime - startTime));
    }
}