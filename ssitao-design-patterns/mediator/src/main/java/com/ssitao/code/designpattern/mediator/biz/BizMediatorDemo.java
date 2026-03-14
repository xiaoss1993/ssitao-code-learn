package com.ssitao.code.designpattern.mediator.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务场景中的中介者模式
 *
 * 典型应用：
 * 1. 航空管制塔 - 飞机之间不直接通信，通过塔台协调
 * 2. 智能家居控制中心 - 设备通过中控协调
 * 3. 股票交易系统 - 买方卖方通过交易所撮合
 * 4. 招聘系统 - 猎头作为中介协调HR和候选人
 */
public class BizMediatorDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景中介者模式 ===\n");

        // 1. 航空管制示例
        System.out.println("1. 航空管制塔示例");
        airTrafficControlDemo();

        // 2. 智能家居示例
        System.out.println("\n2. 智能家居示例");
        smartHomeDemo();

        // 3. 股票交易示例
        System.out.println("\n3. 股票交易示例");
        stockTradingDemo();
    }

    /**
     * 航空管制塔示例
     */
    private static void airTrafficControlDemo() {
        // 创建管制塔中介者
        ControlTower tower = new ControlTower();

        // 创建飞机
        Aircraft plane1 = new Aircraft("CA981", tower);
        Aircraft plane2 = new Aircraft("UA860", tower);
        Aircraft plane3 = new Aircraft("BA249", tower);

        // 飞机注册到塔台
        tower.register(plane1);
        tower.register(plane2);
        tower.register(plane3);

        // 飞机请求起飞
        System.out.println("--- 飞机请求起飞 ---");
        plane1.requestTakeoff();

        // 飞机请求降落
        System.out.println("\n--- 飞机请求降落 ---");
        plane2.requestLanding();

        // 飞机请求进入空域
        System.out.println("\n--- 飞机请求进入空域 ---");
        plane3.requestEnterAirspace();
    }

    /**
     * 智能家居示例
     */
    private static void smartHomeDemo() {
        // 创建智能家居中控
        SmartHomeMediator home = new SmartHomeMediator();

        // 创建设备
        TemperatureSensor sensor = new TemperatureSensor(home);
        AirConditioner ac = new AirConditioner(home);
        Heater heater = new Heater(home);
        Fan fan = new Fan(home);
        Window window = new Window(home);

        // 设置设备关系
        home.setSensor(sensor);
        home.setAc(ac);
        home.setHeater(heater);
        home.setFan(fan);
        home.setWindow(window);

        // 模拟温度变化
        System.out.println("--- 温度升高到30度 ---");
        sensor.setTemperature(30);

        System.out.println("\n--- 温度降低到15度 ---");
        sensor.setTemperature(15);

        System.out.println("\n--- 温度恢复到22度 ---");
        sensor.setTemperature(22);
    }

    /**
     * 股票交易示例
     */
    private static void stockTradingDemo() {
        // 创建证券交易所
        StockExchange exchange = new StockExchange();

        // 创建投资者
        Investor buyer1 = new Investor("张三", true, exchange);
        Investor buyer2 = new Investor("李四", true, exchange);
        Investor seller1 = new Investor("王五", false, exchange);
        Investor seller2 = new Investor("赵六", false, exchange);

        // 投资者下单
        System.out.println("--- 投资者下单 ---");
        buyer1.placeOrder("AAPL", 100, 150.0);  // 买单
        buyer2.placeOrder("AAPL", 50, 155.0);   // 买单
        seller1.placeOrder("AAPL", 80, 148.0);  // 卖单
        seller2.placeOrder("AAPL", 120, 150.0); // 卖单

        System.out.println("\n--- 交易撮合 ---");
        exchange.matchOrders("AAPL");
    }
}

// ============================================
// 1. 航空管制相关类
// ============================================

/**
 * 航空管制塔中介者
 */
class ControlTower {
    private List<Aircraft> aircrafts = new ArrayList<>();

    public void register(Aircraft aircraft) {
        aircrafts.add(aircraft);
        System.out.println("[塔台] " + aircraft.getCallSign() + " 注册到空域");
    }

    public void handleTakeoff(Aircraft aircraft) {
        System.out.println("[塔台] 允许 " + aircraft.getCallSign() + " 起飞");
    }

    public void handleLanding(Aircraft aircraft) {
        System.out.println("[塔台] 允许 " + aircraft.getCallSign() + " 降落");
    }

    public void handleAirspaceEntry(Aircraft aircraft) {
        System.out.println("[塔台] 允许 " + aircraft.getCallSign() + " 进入空域");
    }

    public void broadcast(String message, Aircraft exclude) {
        for (Aircraft aircraft : aircrafts) {
            if (aircraft != exclude) {
                aircraft.receive(message);
            }
        }
    }
}

/**
 * 飞机
 */
class Aircraft {
    private String callSign;
    private ControlTower tower;

    public Aircraft(String callSign, ControlTower tower) {
        this.callSign = callSign;
        this.tower = tower;
    }

    public String getCallSign() {
        return callSign;
    }

    public void requestTakeoff() {
        System.out.println("[" + callSign + "] 请求起飞");
        tower.handleTakeoff(this);
        tower.broadcast(callSign + " 起飞了", this);
    }

    public void requestLanding() {
        System.out.println("[" + callSign + "] 请求降落");
        tower.handleLanding(this);
        tower.broadcast(callSign + " 准备降落", this);
    }

    public void requestEnterAirspace() {
        System.out.println("[" + callSign + "] 请求进入空域");
        tower.handleAirspaceEntry(this);
    }

    public void receive(String message) {
        System.out.println("  -> [" + callSign + "] 收到塔台通知: " + message);
    }
}

// ============================================
// 2. 智能家居相关类
// ============================================

/**
 * 家庭设备中介者接口
 */
interface HomeMediator {
    void onTemperatureChanged(int temperature);
}

/**
 * 智能家居中控
 */
class SmartHomeMediator implements HomeMediator {
    private TemperatureSensor sensor;
    private AirConditioner ac;
    private Heater heater;
    private Fan fan;
    private Window window;

    public void setSensor(TemperatureSensor sensor) { this.sensor = sensor; }
    public void setAc(AirConditioner ac) { this.ac = ac; }
    public void setHeater(Heater heater) { this.heater = heater; }
    public void setFan(Fan fan) { this.fan = fan; }
    public void setWindow(Window window) { this.window = window; }

    @Override
    public void onTemperatureChanged(int temperature) {
        System.out.println("[中控] 检测到温度变化: " + temperature + "°C");

        if (temperature > 25) {
            // 高温：开空调，关窗，开风扇
            System.out.println("  -> 开启制冷模式");
            ac.turnOn();
            heater.turnOff();
            fan.turnOn();
            window.close();
        } else if (temperature < 18) {
            // 低温：开暖气，关窗
            System.out.println("  -> 开启制热模式");
            heater.turnOn();
            ac.turnOff();
            fan.turnOff();
            window.close();
        } else {
            // 适宜温度：关闭所有设备，开窗通风
            System.out.println("  -> 开启通风模式");
            ac.turnOff();
            heater.turnOff();
            fan.turnOff();
            window.open();
        }
    }
}

/**
 * 温度传感器
 */
class TemperatureSensor {
    private HomeMediator mediator;
    private int temperature;

    public TemperatureSensor(HomeMediator mediator) {
        this.mediator = mediator;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        System.out.println("[温度传感器] 当前温度: " + temperature + "°C");
        mediator.onTemperatureChanged(temperature);
    }
}

/**
 * 空调
 */
class AirConditioner {
    private HomeMediator mediator;

    public AirConditioner(HomeMediator mediator) {
        this.mediator = mediator;
    }

    public void turnOn() {
        System.out.println("  [空调] 开启制冷");
    }

    public void turnOff() {
        System.out.println("  [空调] 关闭");
    }
}

/**
 * 暖气
 */
class Heater {
    private HomeMediator mediator;

    public Heater(HomeMediator mediator) {
        this.mediator = mediator;
    }

    public void turnOn() {
        System.out.println("  [暖气] 开启制热");
    }

    public void turnOff() {
        System.out.println("  [暖气] 关闭");
    }
}

/**
 * 风扇
 */
class Fan {
    private HomeMediator mediator;

    public Fan(HomeMediator mediator) {
        this.mediator = mediator;
    }

    public void turnOn() {
        System.out.println("  [风扇] 开启");
    }

    public void turnOff() {
        System.out.println("  [风扇] 关闭");
    }
}

/**
 * 窗户
 */
class Window {
    private HomeMediator mediator;

    public Window(HomeMediator mediator) {
        this.mediator = mediator;
    }

    public void open() {
        System.out.println("  [窗户] 打开");
    }

    public void close() {
        System.out.println("  [窗户] 关闭");
    }
}

// ============================================
// 3. 股票交易相关类
// ============================================

/**
 * 证券交易所中介者
 */
class StockExchange {
    private List<Order> buyOrders = new ArrayList<>();
    private List<Order> sellOrders = new ArrayList<>();

    public void addOrder(Order order) {
        if (order.isBuyOrder()) {
            buyOrders.add(order);
            System.out.println("[交易所] 收到买单: " + order.getInvestorName() +
                " 买入 " + order.getQuantity() + "股，价格 " + order.getPrice());
        } else {
            sellOrders.add(order);
            System.out.println("[交易所] 收到卖单: " + order.getInvestorName() +
                " 卖出 " + order.getQuantity() + "股，价格 " + order.getPrice());
        }
    }

    public void matchOrders(String stockCode) {
        System.out.println("\n[交易所] 开始撮合 " + stockCode + " 股票订单");

        // 简单撮合逻辑：价格匹配即可成交
        for (Order buy : buyOrders) {
            for (Order sell : sellOrders) {
                if (buy.getPrice() >= sell.getPrice()) {
                    int quantity = Math.min(buy.getQuantity(), sell.getQuantity());
                    double price = sell.getPrice();

                    System.out.println("[交易所] 成交! " +
                        buy.getInvestorName() + " 买入 " + quantity + "股 @" + price +
                        " <- " + sell.getInvestorName() + " 卖出 " + quantity + "股 @" + price);

                    // 减少数量
                    buy.setQuantity(buy.getQuantity() - quantity);
                    sell.setQuantity(sell.getQuantity() - quantity);

                    if (buy.getQuantity() <= 0) break;
                }
            }
        }
    }
}

/**
 * 订单
 */
class Order {
    private String investorName;
    private boolean buyOrder;
    private String stockCode;
    private int quantity;
    private double price;

    public Order(String investorName, boolean buyOrder, String stockCode,
                 int quantity, double price) {
        this.investorName = investorName;
        this.buyOrder = buyOrder;
        this.stockCode = stockCode;
        this.quantity = quantity;
        this.price = price;
    }

    public String getInvestorName() { return investorName; }
    public boolean isBuyOrder() { return buyOrder; }
    public String getStockCode() { return stockCode; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}

/**
 * 投资者
 */
class Investor {
    private String name;
    private boolean isBuyer;
    private StockExchange exchange;

    public Investor(String name, boolean isBuyer, StockExchange exchange) {
        this.name = name;
        this.isBuyer = isBuyer;
        this.exchange = exchange;
    }

    public void placeOrder(String stockCode, int quantity, double price) {
        Order order = new Order(name, isBuyer, stockCode, quantity, price);
        exchange.addOrder(order);
    }
}
