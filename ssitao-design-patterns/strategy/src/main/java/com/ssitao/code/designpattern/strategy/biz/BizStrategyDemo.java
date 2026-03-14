package com.ssitao.code.designpattern.strategy.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务场景中的策略模式
 *
 * 典型应用：
 * 1. 订单计算 - 不同活动不同优惠
 * 2. 物流计算 - 不同物流不同费用
 * 3. 风控审核 - 不同规则不同审核
 * 4. 搜索引擎 - 不同排序策略
 * 5. 推荐系统 - 不同推荐算法
 */
public class BizStrategyDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景策略模式 ===\n");

        // 1. 订单优惠策略
        System.out.println("1. 订单优惠策略");
        discountDemo();

        // 2. 物流策略
        System.out.println("\n2. 物流策略");
        logisticsDemo();

        // 3. 风控策略
        System.out.println("\n3. 风控策略");
        riskControlDemo();
    }

    /**
     * 订单优惠策略示例
     */
    private static void discountDemo() {
        OrderPriceCalculator calculator = new OrderPriceCalculator();

        // 创建订单
        Order order = new Order("ORD-001", 1000.0);
        order.setUserLevel("VIP");

        // 无优惠
        System.out.println("--- 无优惠 ---");
        calculator.setStrategy(new NoDiscountStrategy());
        double price1 = calculator.calculate(order);
        System.out.println("原价: 1000, 折后价: " + price1);

        // 满减优惠
        System.out.println("\n--- 满减优惠 ---");
        calculator.setStrategy(new FullDiscountStrategy());
        double price2 = calculator.calculate(order);
        System.out.println("原价: 1000, 折后价: " + price2);

        // VIP专属折扣
        System.out.println("\n--- VIP专属折扣 ---");
        calculator.setStrategy(new VipDiscountStrategy());
        double price3 = calculator.calculate(order);
        System.out.println("原价: 1000, 折后价: " + price3);

        // 限时折扣
        System.out.println("\n--- 限时折扣 ---");
        calculator.setStrategy(new FlashSaleStrategy());
        double price4 = calculator.calculate(order);
        System.out.println("原价: 1000, 折后价: " + price4);
    }

    /**
     * 物流策略示例
     */
    private static void logisticsDemo() {
        LogisticsCalculator calculator = new LogisticsCalculator();

        // 创建物流订单
        LogisticsOrder logistics = new LogisticsOrder("ORD-001", "北京", "上海", 5.0);

        // 顺丰
        System.out.println("--- 顺丰快递 ---");
        calculator.setStrategy(new SfExpressStrategy());
        double fee1 = calculator.calculate(logistics);
        System.out.println("重量: 5kg, 费用: " + fee1);

        // 中通
        System.out.println("\n--- 中通快递 ---");
        calculator.setStrategy(new ZtoExpressStrategy());
        double fee2 = calculator.calculate(logistics);
        System.out.println("重量: 5kg, 费用: " + fee2);

        // 邮政
        System.out.println("\n--- 邮政包裹 ---");
        calculator.setStrategy(new PostExpressStrategy());
        double fee3 = calculator.calculate(logistics);
        System.out.println("重量: 5kg, 费用: " + fee3);

        // 同城配送
        System.out.println("\n--- 同城配送 ---");
        calculator.setStrategy(new LocalDeliveryStrategy());
        double fee4 = calculator.calculate(logistics);
        System.out.println("重量: 5kg, 费用: " + fee4);
    }

    /**
     * 风控策略示例
     */
    private static void riskControlDemo() {
        RiskChecker riskChecker = new RiskChecker();

        // 创建风控请求
        RiskRequest request = new RiskRequest();
        request.setUserId("user123");
        request.setAmount(50000.0);
        request.setIp("192.168.1.100");
        request.setDeviceId("device_xxx");

        // 基础风控
        System.out.println("--- 基础风控 ---");
        riskChecker.setStrategy(new BasicRiskStrategy());
        RiskResult result1 = riskChecker.check(request);
        System.out.println("风险等级: " + result1.getRiskLevel() + ", 描述: " + result1.getDescription());

        // 严格风控
        System.out.println("\n--- 严格风控 ---");
        riskChecker.setStrategy(new StrictRiskStrategy());
        RiskResult result2 = riskChecker.check(request);
        System.out.println("风险等级: " + result2.getRiskLevel() + ", 描述: " + result2.getDescription());

        // 业务风控
        System.out.println("\n--- 业务风控 ---");
        riskChecker.setStrategy(new BusinessRiskStrategy());
        RiskResult result3 = riskChecker.check(request);
        System.out.println("风险等级: " + result3.getRiskLevel() + ", 描述: " + result3.getDescription());
    }
}

// ============================================
// 1. 订单优惠相关类
// ============================================

/**
 * 订单
 */
class Order {
    private String orderId;
    private double originalPrice;
    private String userLevel;

    public Order(String orderId, double originalPrice) {
        this.orderId = orderId;
        this.originalPrice = originalPrice;
    }

    public String getOrderId() { return orderId; }
    public double getOriginalPrice() { return originalPrice; }
    public String getUserLevel() { return userLevel; }
    public void setUserLevel(String userLevel) { this.userLevel = userLevel; }
}

/**
 * 优惠策略接口
 */
interface DiscountStrategy {
    double calculate(Order order);
}

/**
 * 无优惠
 */
class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        return order.getOriginalPrice();
    }
}

/**
 * 满减优惠
 */
class FullDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        double price = order.getOriginalPrice();
        if (price >= 500) {
            price -= 50;
        }
        if (price >= 800) {
            price -= 100;
        }
        return price;
    }
}

/**
 * VIP专属折扣
 */
class VipDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        double price = order.getOriginalPrice();
        if ("VIP".equals(order.getUserLevel())) {
            price *= 0.85; // 85折
        }
        return price;
    }
}

/**
 * 限时折扣
 */
class FlashSaleStrategy implements DiscountStrategy {
    @Override
    public double calculate(Order order) {
        return order.getOriginalPrice() * 0.7; // 7折
    }
}

/**
 * 价格计算器
 */
class OrderPriceCalculator {
    private DiscountStrategy strategy;

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(Order order) {
        return strategy.calculate(order);
    }
}

// ============================================
// 2. 物流相关类
// ============================================

/**
 * 物流订单
 */
class LogisticsOrder {
    private String orderId;
    private String fromCity;
    private String toCity;
    private double weight;

    public LogisticsOrder(String orderId, String fromCity, String toCity, double weight) {
        this.orderId = orderId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.weight = weight;
    }

    public String getOrderId() { return orderId; }
    public String getFromCity() { return fromCity; }
    public String getToCity() { return toCity; }
    public double getWeight() { return weight; }
}

/**
 * 物流策略接口
 */
interface LogisticsStrategy {
    double calculate(LogisticsOrder order);
    String getName();
}

/**
 * 顺丰
 */
class SfExpressStrategy implements LogisticsStrategy {
    @Override
    public double calculate(LogisticsOrder order) {
        return 12 + order.getWeight() * 8;
    }

    @Override
    public String getName() {
        return "顺丰";
    }
}

/**
 * 中通
 */
class ZtoExpressStrategy implements LogisticsStrategy {
    @Override
    public double calculate(LogisticsOrder order) {
        return 8 + order.getWeight() * 5;
    }

    @Override
    public String getName() {
        return "中通";
    }
}

/**
 * 邮政
 */
class PostExpressStrategy implements LogisticsStrategy {
    @Override
    public double calculate(LogisticsOrder order) {
        return 5 + order.getWeight() * 3;
    }

    @Override
    public String getName() {
        return "邮政";
    }
}

/**
 * 同城配送
 */
class LocalDeliveryStrategy implements LogisticsStrategy {
    @Override
    public double calculate(LogisticsOrder order) {
        return 10 + order.getWeight() * 2;
    }

    @Override
    public String getName() {
        return "同城配送";
    }
}

/**
 * 物流计算器
 */
class LogisticsCalculator {
    private LogisticsStrategy strategy;

    public void setStrategy(LogisticsStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(LogisticsOrder order) {
        return strategy.calculate(order);
    }
}

// ============================================
// 3. 风控相关类
// ============================================

/**
 * 风控请求
 */
class RiskRequest {
    private String userId;
    private double amount;
    private String ip;
    private String deviceId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
}

/**
 * 风控结果
 */
class RiskResult {
    private String riskLevel;
    private String description;

    public RiskResult(String riskLevel, String description) {
        this.riskLevel = riskLevel;
        this.description = description;
    }

    public String getRiskLevel() { return riskLevel; }
    public String getDescription() { return description; }
}

/**
 * 风控策略接口
 */
interface RiskStrategy {
    RiskResult check(RiskRequest request);
}

/**
 * 基础风控
 */
class BasicRiskStrategy implements RiskStrategy {
    @Override
    public RiskResult check(RiskRequest request) {
        if (request.getAmount() > 10000) {
            return new RiskResult("HIGH", "金额超过10000，需要人工审核");
        }
        return new RiskResult("LOW", "通过");
    }
}

/**
 * 严格风控
 */
class StrictRiskStrategy implements RiskStrategy {
    @Override
    public RiskResult check(RiskRequest request) {
        if (request.getAmount() > 5000) {
            return new RiskResult("HIGH", "金额超过5000，需要人工审核");
        }
        if (request.getAmount() > 1000) {
            return new RiskResult("MEDIUM", "金额超过1000，需要短信验证");
        }
        return new RiskResult("LOW", "通过");
    }
}

/**
 * 业务风控
 */
class BusinessRiskStrategy implements RiskStrategy {
    @Override
    public RiskResult check(RiskRequest request) {
        // 模拟业务规则
        if (request.getAmount() > 50000) {
            return new RiskResult("HIGH", "大额交易，需要多因素认证");
        }
        if (request.getAmount() > 10000) {
            return new RiskResult("MEDIUM", "中高风险，需要人工审核");
        }
        return new RiskResult("LOW", "业务风控通过");
    }
}

/**
 * 风控检查器
 */
class RiskChecker {
    private RiskStrategy strategy;

    public void setStrategy(RiskStrategy strategy) {
        this.strategy = strategy;
    }

    public RiskResult check(RiskRequest request) {
        return strategy.check(request);
    }
}
