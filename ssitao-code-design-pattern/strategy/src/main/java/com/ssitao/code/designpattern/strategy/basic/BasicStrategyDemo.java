package com.ssitao.code.designpattern.strategy.basic;

import java.util.HashMap;
import java.util.Map;

/**
 * 策略模式基础示例
 *
 * 策略模式特点：
 * 1. 定义一系列算法，将每个算法封装起来
 * 2. 算法之间可以互相替换
 * 3. 客户端可以在运行时选择算法
 *
 * 与状态模式区别：
 * - 策略模式：客户端主动选择策略
 * - 状态模式：状态自动转换
 *
 * 组成：
 * - Strategy: 策略接口
 * - ConcreteStrategy: 具体策略实现
 * - Context: 上下文，持有策略引用
 */
public class BasicStrategyDemo {

    public static void main(String[] args) {
        System.out.println("=== 策略模式基础示例 ===\n");

        // 创建支付上下文
        PaymentContext context = new PaymentContext();

        // 使用支付宝支付
        System.out.println("--- 使用支付宝支付 ---");
        context.setStrategy(new AlipayStrategy());
        context.pay(100.0);

        // 使用微信支付
        System.out.println("\n--- 使用微信支付 ---");
        context.setStrategy(new WechatPayStrategy());
        context.pay(100.0);

        // 使用银行卡支付
        System.out.println("\n--- 使用银行卡支付 ---");
        context.setStrategy(new CreditCardStrategy());
        context.pay(100.0);

        // 使用积分支付
        System.out.println("\n--- 使用积分支付 ---");
        context.setStrategy(new PointsStrategy());
        context.pay(100.0);
    }
}

/**
 * 支付策略接口
 */
interface PaymentStrategy {
    void pay(double amount);
    String getPaymentName();
}

/**
 * 支付宝支付策略
 */
class AlipayStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("使用支付宝支付: " + amount + " 元");
        System.out.println("跳转支付宝APP...");
    }

    @Override
    public String getPaymentName() {
        return "支付宝";
    }
}

/**
 * 微信支付策略
 */
class WechatPayStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("使用微信支付: " + amount + " 元");
        System.out.println("打开微信支付码...");
    }

    @Override
    public String getPaymentName() {
        return "微信支付";
    }
}

/**
 * 银行卡支付策略
 */
class CreditCardStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        System.out.println("使用银行卡支付: " + amount + " 元");
        System.out.println("请输入银行卡号和密码...");
    }

    @Override
    public String getPaymentName() {
        return "银行卡";
    }
}

/**
 * 积分支付策略
 */
class PointsStrategy implements PaymentStrategy {

    @Override
    public void pay(double amount) {
        int points = (int) (amount * 10); // 1元=10积分
        System.out.println("使用积分支付: " + amount + " 元");
        System.out.println("扣除积分: " + points + " 积分");
    }

    @Override
    public String getPaymentName() {
        return "积分";
    }
}

/**
 * 支付上下文
 */
class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void pay(double amount) {
        if (strategy == null) {
            System.out.println("请选择支付方式");
            return;
        }
        System.out.println("订单金额: " + amount + " 元");
        strategy.pay(amount);
    }
}
