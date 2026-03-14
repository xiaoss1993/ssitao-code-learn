package com.ssitao.code.designpattern.state.basic;

import java.util.HashMap;
import java.util.Map;

/**
 * 状态模式基础示例
 *
 * 状态模式特点：
 * 1. 允许对象在内部状态改变时改变行为
 * 2. 对象看起来似乎修改了它的类
 * 3. 将状态特定行为封装在独立的状态类中
 *
 * 与策略模式区别：
 * - 策略模式：客户端决定使用哪个策略
 * - 状态模式：状态自动转换
 *
 * 组成：
 * - Context: 上下文，持有当前状态
 * - State: 状态接口
 * - ConcreteState: 具体状态实现
 */
public class BasicStateDemo {

    public static void main(String[] args) {
        System.out.println("=== 状态模式基础示例 ===\n");

        // 创建订单
        Order order = new Order("ORD-001");

        // 订单生命周期
        System.out.println("--- 创建订单 ---");
        order.showStatus();

        System.out.println("\n--- 支付订单 ---");
        order.pay();
        order.showStatus();

        System.out.println("\n--- 发货 ---");
        order.ship();
        order.showStatus();

        System.out.println("\n--- 确认收货 ---");
        order.confirm();
        order.showStatus();

        System.out.println("\n--- 申请售后 ---");
        order.applyRefund();
        order.showStatus();

        System.out.println("\n--- 再次购买 ---");
        order.buyAgain();
        order.showStatus();
    }
}

/**
 * 订单上下文
 */
class Order {
    private String orderId;
    private OrderState state;

    // 订单状态映射
    private static Map<String, OrderState> stateMap = new HashMap<>();
    static {
        stateMap.put("CREATED", new CreatedState());
        stateMap.put("PAID", new PaidState());
        stateMap.put("SHIPPED", new ShippedState());
        stateMap.put("CONFIRMED", new ConfirmedState());
        stateMap.put("REFUNDING", new RefundingState());
    }

    public Order(String orderId) {
        this.orderId = orderId;
        this.state = stateMap.get("CREATED");
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void setState(String stateName) {
        this.state = stateMap.get(stateName);
    }

    // 展示当前状态
    public void showStatus() {
        System.out.println("订单 " + orderId + " 当前状态: " + state.getStateName());
    }

    // 支付
    public void pay() {
        state.handlePay(this);
    }

    // 发货
    public void ship() {
        state.handleShip(this);
    }

    // 确认收货
    public void confirm() {
        state.handleConfirm(this);
    }

    // 申请退款
    public void applyRefund() {
        state.handleRefund(this);
    }

    // 再次购买
    public void buyAgain() {
        this.state = stateMap.get("CREATED");
        System.out.println("订单 " + orderId + " 已重置为初始状态");
    }
}

/**
 * 订单状态接口
 */
interface OrderState {
    String getStateName();

    // 处理支付
    void handlePay(Order order);

    // 处理发货
    void handleShip(Order order);

    // 处理确认收货
    void handleConfirm(Order order);

    // 处理退款
    void handleRefund(Order order);
}

/**
 * 已创建状态
 */
class CreatedState implements OrderState {

    @Override
    public String getStateName() {
        return "已创建";
    }

    @Override
    public void handlePay(Order order) {
        System.out.println("执行支付操作");
        order.setState("PAID");
        System.out.println("状态变更为: 已支付");
    }

    @Override
    public void handleShip(Order order) {
        System.out.println("错误：订单未支付，不能发货");
    }

    @Override
    public void handleConfirm(Order order) {
        System.out.println("错误：订单未支付，不能确认");
    }

    @Override
    public void handleRefund(Order order) {
        System.out.println("错误：订单未支付，不能退款");
    }
}

/**
 * 已支付状态
 */
class PaidState implements OrderState {

    @Override
    public String getStateName() {
        return "已支付";
    }

    @Override
    public void handlePay(Order order) {
        System.out.println("订单已支付，无需重复支付");
    }

    @Override
    public void handleShip(Order order) {
        System.out.println("执行发货操作");
        order.setState("SHIPPED");
        System.out.println("状态变更为: 已发货");
    }

    @Override
    public void handleConfirm(Order order) {
        System.out.println("错误：订单未发货，不能确认");
    }

    @Override
    public void handleRefund(Order order) {
        System.out.println("申请退款");
        order.setState("REFUNDING");
        System.out.println("状态变更为: 退款中");
    }
}

/**
 * 已发货状态
 */
class ShippedState implements OrderState {

    @Override
    public String getStateName() {
        return "已发货";
    }

    @Override
    public void handlePay(Order order) {
        System.out.println("错误：订单已支付");
    }

    @Override
    public void handleShip(Order order) {
        System.out.println("订单已发货，无需重复发货");
    }

    @Override
    public void handleConfirm(Order order) {
        System.out.println("确认收货");
        order.setState("CONFIRMED");
        System.out.println("状态变更为: 已确认");
    }

    @Override
    public void handleRefund(Order order) {
        System.out.println("申请退款");
        order.setState("REFUNDING");
        System.out.println("状态变更为: 退款中");
    }
}

/**
 * 已确认状态
 */
class ConfirmedState implements OrderState {

    @Override
    public String getStateName() {
        return "已完成";
    }

    @Override
    public void handlePay(Order order) {
        System.out.println("错误：订单已完成");
    }

    @Override
    public void handleShip(Order order) {
        System.out.println("错误：订单已完成");
    }

    @Override
    public void handleConfirm(Order order) {
        System.out.println("订单已完成");
    }

    @Override
    public void handleRefund(Order order) {
        System.out.println("订单已完成，无法直接退款，请联系客服");
    }
}

/**
 * 退款中状态
 */
class RefundingState implements OrderState {

    @Override
    public String getStateName() {
        return "退款中";
    }

    @Override
    public void handlePay(Order order) {
        System.out.println("错误：订单正在退款中");
    }

    @Override
    public void handleShip(Order order) {
        System.out.println("错误：订单正在退款中");
    }

    @Override
    public void handleConfirm(Order order) {
        System.out.println("错误：订单正在退款中");
    }

    @Override
    public void handleRefund(Order order) {
        System.out.println("退款处理中，请耐心等待");
    }
}
