package com.ssitao.code.designpattern.observer.biz;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务场景中的观察者模式
 *
 * 典型应用：
 * 1. 股票价格监控 - 价格变化通知订阅者
 * 2. 订单状态变更 - 通知相关人员
 * 3. 库存预警 - 库存低于阈值通知
 * 4. 消息推送系统 - 消息推送
 */
public class BizObserverDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景观察者模式 ===\n");

        // 1. 股票价格监控
        System.out.println("1. 股票价格监控");
        stockPriceDemo();

        // 2. 订单状态变更
        System.out.println("\n2. 订单状态变更");
        orderStatusDemo();
    }

    /**
     * 股票价格监控示例
     */
    private static void stockPriceDemo() {
        // 创建股票
        Stock stock = new Stock("AAPL", 150.0);

        // 创建观察者
        StockObserver investor1 = new StockInvestor("投资者A");
        StockObserver investor2 = new StockInvestor("投资者B");
        StockObserver analyst = new StockAnalyst("分析师C");

        // 订阅股票
        stock.addObserver(investor1);
        stock.addObserver(investor2);
        stock.addObserver(analyst);

        // 价格变化
        System.out.println("--- 价格上涨 ---");
        stock.setPrice(155.0);

        System.out.println("\n--- 价格下跌 ---");
        stock.setPrice(148.0);

        System.out.println("\n--- 价格涨停 ---");
        stock.setPrice(163.0);

        // 取消订阅
        System.out.println("\n--- 投资者B取消订阅 ---");
        stock.removeObserver(investor2);

        System.out.println("\n--- 价格波动 ---");
        stock.setPrice(160.0);
    }

    /**
     * 订单状态变更示例
     */
    private static void orderStatusDemo() {
        // 创建订单服务
        OrderService orderService = new OrderService();

        // 添加观察者
        orderService.addObserver(new OrderSmsNotifier());
        orderService.addObserver(new OrderEmailNotifier());
        orderService.addObserver(new OrderWmsNotifier());

        // 创建订单
        System.out.println("--- 创建订单 ---");
        Order order = orderService.createOrder("用户123", "商品A", 1);

        System.out.println("\n--- 支付订单 ---");
        orderService.payOrder(order.getOrderId());

        System.out.println("\n--- 发货 ---");
        orderService.shipOrder(order.getOrderId());

        System.out.println("\n--- 确认收货 ---");
        orderService.confirmReceipt(order.getOrderId());
    }
}

// ============================================
// 1. 股票监控相关类
// ============================================

/**
 * 股票 - 主题
 */
class Stock {
    private String code;
    private double price;
    private List<StockObserver> observers = new ArrayList<>();

    public Stock(String code, double price) {
        this.code = code;
        this.price = price;
    }

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (StockObserver observer : observers) {
            observer.update(code, price);
        }
    }

    public void setPrice(double newPrice) {
        double oldPrice = this.price;
        this.price = newPrice;
        System.out.println("[股票] " + code + " 价格变化: " + oldPrice + " -> " + newPrice);
        notifyObservers();
    }

    public double getPrice() {
        return price;
    }

    public String getCode() {
        return code;
    }
}

/**
 * 股票观察者接口
 */
interface StockObserver {
    void update(String code, double price);
}

/**
 * 股票投资者
 */
class StockInvestor implements StockObserver {
    private String name;
    private double lastPrice;

    public StockInvestor(String name) {
        this.name = name;
    }

    @Override
    public void update(String code, double price) {
        this.lastPrice = price;
        System.out.println("[投资者 " + name + "] " + code + " 当前价格: " + price);

        // 简单决策逻辑
        if (price > lastPrice) {
            System.out.println("  -> 价格上涨，考虑买入或持有");
        } else {
            System.out.println("  -> 价格下跌，考虑卖出或观望");
        }
    }
}

/**
 * 股票分析师
 */
class StockAnalyst implements StockObserver {

    public StockAnalyst(String name) {
    }

    @Override
    public void update(String code, double price) {
        String recommendation;
        if (price > 160) {
            recommendation = "建议卖出";
        } else if (price < 140) {
            recommendation = "建议买入";
        } else {
            recommendation = "建议持有";
        }
        System.out.println("[分析师] " + code + " 价格: " + price + ", " + recommendation);
    }
}

// ============================================
// 2. 订单状态变更相关类
// ============================================

/**
 * 订单
 */
class Order {
    private String orderId;
    private String userId;
    private String product;
    private int quantity;
    private OrderStatus status;

    public Order(String orderId, String userId, String product, int quantity) {
        this.orderId = orderId;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.status = OrderStatus.CREATED;
    }

    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}

enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    CONFIRMED,
    CANCELLED
}

/**
 * 订单服务 - 主题
 */
class OrderService {
    private List<Order> orders = new ArrayList<>();
    private List<OrderObserver> observers = new ArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        OrderEvent event = new OrderEvent(order, oldStatus, newStatus);
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChange(event);
        }
    }

    public Order createOrder(String userId, String product, int quantity) {
        String orderId = "ORD-" + System.currentTimeMillis();
        Order order = new Order(orderId, userId, product, quantity);
        orders.add(order);
        System.out.println("[订单服务] 创建订单: " + orderId);
        notifyObservers(order, null, OrderStatus.CREATED);
        return order;
    }

    public void payOrder(String orderId) {
        Order order = findOrder(orderId);
        if (order != null) {
            OrderStatus oldStatus = order.getStatus();
            order.setStatus(OrderStatus.PAID);
            System.out.println("[订单服务] 订单支付: " + orderId);
            notifyObservers(order, oldStatus, OrderStatus.PAID);
        }
    }

    public void shipOrder(String orderId) {
        Order order = findOrder(orderId);
        if (order != null) {
            OrderStatus oldStatus = order.getStatus();
            order.setStatus(OrderStatus.SHIPPED);
            System.out.println("[订单服务] 订单发货: " + orderId);
            notifyObservers(order, oldStatus, OrderStatus.SHIPPED);
        }
    }

    public void confirmReceipt(String orderId) {
        Order order = findOrder(orderId);
        if (order != null) {
            OrderStatus oldStatus = order.getStatus();
            order.setStatus(OrderStatus.CONFIRMED);
            System.out.println("[订单服务] 确认收货: " + orderId);
            notifyObservers(order, oldStatus, OrderStatus.CONFIRMED);
        }
    }

    private Order findOrder(String orderId) {
        return orders.stream()
            .filter(o -> o.getOrderId().equals(orderId))
            .findFirst()
            .orElse(null);
    }
}

/**
 * 订单事件
 */
class OrderEvent {
    private Order order;
    private OrderStatus oldStatus;
    private OrderStatus newStatus;

    public OrderEvent(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        this.order = order;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public Order getOrder() { return order; }
    public OrderStatus getOldStatus() { return oldStatus; }
    public OrderStatus getNewStatus() { return newStatus; }
}

/**
 * 订单观察者接口
 */
interface OrderObserver {
    void onOrderStatusChange(OrderEvent event);
}

/**
 * 短信通知观察者
 */
class OrderSmsNotifier implements OrderObserver {

    @Override
    public void onOrderStatusChange(OrderEvent event) {
        Order order = event.getOrder();
        String message;

        switch (event.getNewStatus()) {
            case CREATED:
                message = "订单已创建: " + order.getOrderId();
                break;
            case PAID:
                message = "订单已支付: " + order.getOrderId();
                break;
            case SHIPPED:
                message = "订单已发货: " + order.getOrderId();
                break;
            case CONFIRMED:
                message = "订单已完成: " + order.getOrderId();
                break;
            default:
                return;
        }

        System.out.println("[短信通知] 发送给用户 " + order.getUserId() + ": " + message);
    }
}

/**
 * 邮件通知观察者
 */
class OrderEmailNotifier implements OrderObserver {

    @Override
    public void onOrderStatusChange(OrderEvent event) {
        Order order = event.getOrder();

        System.out.println("[邮件通知] 发送邮件给用户 " + order.getUserId() +
            ", 订单状态: " + event.getNewStatus());
    }
}

/**
 * WMS仓储观察者
 */
class OrderWmsNotifier implements OrderObserver {

    @Override
    public void onOrderStatusChange(OrderEvent event) {
        if (event.getNewStatus() == OrderStatus.PAID) {
            System.out.println("[WMS] 收到订单 " + event.getOrder().getOrderId() +
                ", 准备拣货");
        } else if (event.getNewStatus() == OrderStatus.SHIPPED) {
            System.out.println("[WMS] 订单 " + event.getOrder().getOrderId() +
                " 已发货");
        }
    }
}
