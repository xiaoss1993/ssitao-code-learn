package com.ssitao.code.designpattern.mediator.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web应用中的中介者模式示例
 *
 * Web应用场景：
 * 1. 聊天室 - 用户之间通过聊天室中介者通信
 * 2. 直播间 - 观众通过直播间与主播互动
 * 3. 多人协作编辑 - 文档协作
 * 4. 游戏大厅 - 玩家匹配、房间管理
 *
 * 框架应用：
 * - Spring MVC: DispatcherServlet作为中介者
 * - Spring Cloud Gateway: 网关路由
 * - WebSocket: 消息broker
 */
public class WebMediatorDemo {

    public static void main(String[] args) {
        System.out.println("=== Web中介者模式示例 ===\n");

        // 1. 聊天室示例
        System.out.println("1. 聊天室示例");
        chatRoomDemo();

        // 2. 直播间示例
        System.out.println("\n2. 直播间示例");
        liveStreamDemo();

        // 3. 订单处理流程示例
        System.out.println("\n3. 订单处理流程示例");
        orderProcessingDemo();
    }

    /**
     * 聊天室示例
     */
    private static void chatRoomDemo() {
        // 创建聊天室中介者
        ChatRoomMediator chatRoom = new ChatRoomMediator("技术交流群");

        // 创建用户
        ChatUser user1 = new ChatUser("张三", chatRoom);
        ChatUser user2 = new ChatUser("李四", chatRoom);
        ChatUser user3 = new ChatUser("王五", chatRoom);

        // 加入聊天室
        chatRoom.join(user1);
        chatRoom.join(user2);
        chatRoom.join(user3);

        // 发送消息
        user1.send("大家好，我是新人！");
        user2.send("欢迎欢迎！");
        user3.send("群里有Java专家吗？");
    }

    /**
     * 直播间示例
     */
    private static void liveStreamDemo() {
        // 创建直播间中介者
        LiveRoomMediator liveRoom = new LiveRoomMediator("直播间-编程课堂");

        // 创建观众
        Audience audience1 = new Audience("观众A");
        Audience audience2 = new Audience("观众B");
        Anchor anchor = new Anchor("主播-老师");

        // 进入直播间
        liveRoom.enterRoom(anchor);
        liveRoom.enterRoom(audience1);
        liveRoom.enterRoom(audience2);

        // 发送弹幕
        audience1.sendDanmaku("老师讲得真好！");
        audience2.sendDanmaku("这个知识点不太懂");
        anchor.sendDanmaku("不懂的可以课后问我");
    }

    /**
     * 订单处理流程示例 - 模拟微服务协调
     */
    private static void orderProcessingDemo() {
        // 创建订单协调中介者
        OrderCoordinator coordinator = new OrderCoordinator();

        // 创建各个服务（同事）
        OrderService orderService = new OrderService(coordinator);
        InventoryService inventoryService = new InventoryService(coordinator);
        PaymentService paymentService = new PaymentService(coordinator);
        ShippingService shippingService = new ShippingService(coordinator);
        NotificationService notificationService = new NotificationService(coordinator);

        // 注册服务
        coordinator.registerService("order", orderService);
        coordinator.registerService("inventory", inventoryService);
        coordinator.registerService("payment", paymentService);
        coordinator.registerService("shipping", shippingService);
        coordinator.registerService("notification", notificationService);

        // 创建订单
        System.out.println("--- 创建订单 ---");
        Order order = new Order("ORD-001", "用户123", 500.0);
        orderService.createOrder(order);

        System.out.println("\n--- 订单处理完成 ---");
    }
}

// ============================================
// 1. 聊天室相关类
// ============================================

/**
 * 聊天室中介者
 */
class ChatRoomMediator {
    private String roomName;
    private List<ChatUser> users = new ArrayList<>();

    public ChatRoomMediator(String roomName) {
        this.roomName = roomName;
        System.out.println("聊天室创建: " + roomName);
    }

    public void join(ChatUser user) {
        users.add(user);
        System.out.println(user.getName() + " 加入了 " + roomName);
        // 通知其他用户
        broadcast(user.getName() + " 加入了聊天室", user);
    }

    public void sendMessage(String message, ChatUser sender) {
        System.out.println("[" + sender.getName() + "] 说: " + message);
        broadcast(message, sender);
    }

    private void broadcast(String message, ChatUser exclude) {
        for (ChatUser user : users) {
            if (user != exclude) {
                user.receiveMessage(message);
            }
        }
    }
}

/**
 * 聊天用户
 */
class ChatUser {
    private String name;
    private ChatRoomMediator chatRoom;

    public ChatUser(String name, ChatRoomMediator chatRoom) {
        this.name = name;
        this.chatRoom = chatRoom;
    }

    public String getName() {
        return name;
    }

    public void send(String message) {
        chatRoom.sendMessage(message, this);
    }

    public void receiveMessage(String message) {
        System.out.println("  -> [" + name + "] 收到: " + message);
    }
}

// ============================================
// 2. 直播间相关类
// ============================================

/**
 * 直播间中介者
 */
class LiveRoomMediator {
    private String roomName;
    private List<LiveUser> users = new ArrayList<>();

    public LiveRoomMediator(String roomName) {
        this.roomName = roomName;
        System.out.println("直播间开播: " + roomName);
    }

    public void enterRoom(LiveUser user) {
        users.add(user);
        System.out.println(user.getName() + " 进入直播间");
    }

    public void sendDanmaku(String danmaku, LiveUser sender) {
        System.out.println("[" + sender.getName() + "] 弹幕: " + danmaku);
        for (LiveUser user : users) {
            if (user != sender) {
                user.receiveDanmaku(sender.getName(), danmaku);
            }
        }
    }
}

/**
 * 直播间用户
 */
abstract class LiveUser {
    protected String name;
    protected LiveRoomMediator mediator;

    public LiveUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void sendDanmaku(String message);
    public abstract void receiveDanmaku(String sender, String message);
}

/**
 * 主播
 */
class Anchor extends LiveUser {
    public Anchor(String name) {
        super(name);
    }

    @Override
    public void sendDanmaku(String message) {
        System.out.println("[主播] " + name + ": " + message);
    }

    @Override
    public void receiveDanmaku(String sender, String message) {
        System.out.println("  -> [" + name + "] 收到观众弹幕: " + message);
    }
}

/**
 * 观众
 */
class Audience extends LiveUser {
    public Audience(String name) {
        super(name);
    }

    @Override
    public void sendDanmaku(String message) {
        System.out.println("[观众] " + name + ": " + message);
    }

    @Override
    public void receiveDanmaku(String sender, String message) {
        // 观众收到弹幕不做特殊处理
    }
}

// ============================================
// 3. 订单处理流程相关类
// ============================================

/**
 * 订单协调中介者
 */
class OrderCoordinator {
    private Map<String, OrderServiceInterface> services = new HashMap<>();

    public void registerService(String name, OrderServiceInterface service) {
        services.put(name, service);
    }

    public void notify(String serviceName, String event, Object data) {
        System.out.println("  [协调者] 收到 " + serviceName + " 的 " + event + " 事件");
        // 根据事件协调其他服务
        if ("order.created".equals(event)) {
            services.get("inventory").process(data);
        } else if ("inventory.checked".equals(event)) {
            services.get("payment").process(data);
        } else if ("payment.completed".equals(event)) {
            services.get("shipping").process(data);
        } else if ("shipping.shipped".equals(event)) {
            services.get("notification").process(data);
        }
    }
}

/**
 * 订单服务接口
 */
interface OrderServiceInterface {
    void process(Object data);
}

/**
 * 订单服务
 */
class OrderService implements OrderServiceInterface {
    private OrderCoordinator coordinator;

    public OrderService(OrderCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void createOrder(Order order) {
        System.out.println("[OrderService] 创建订单: " + order.getOrderId());
        coordinator.notify("order", "order.created", order);
    }

    @Override
    public void process(Object data) {
        // 处理其他服务的结果
    }
}

/**
 * 库存服务
 */
class InventoryService implements OrderServiceInterface {
    private OrderCoordinator coordinator;

    public InventoryService(OrderCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void process(Object data) {
        Order order = (Order) data;
        System.out.println("[InventoryService] 检查库存: " + order.getOrderId());
        System.out.println("[InventoryService] 库存充足，扣减库存");
        coordinator.notify("inventory", "inventory.checked", order);
    }
}

/**
 * 支付服务
 */
class PaymentService implements OrderServiceInterface {
    private OrderCoordinator coordinator;

    public PaymentService(OrderCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void process(Object data) {
        Order order = (Order) data;
        System.out.println("[PaymentService] 处理支付: " + order.getOrderId() + ", 金额: " + order.getAmount());
        System.out.println("[PaymentService] 支付成功");
        coordinator.notify("payment", "payment.completed", order);
    }
}

/**
 * 物流服务
 */
class ShippingService implements OrderServiceInterface {
    private OrderCoordinator coordinator;

    public ShippingService(OrderCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void process(Object data) {
        Order order = (Order) data;
        System.out.println("[ShippingService] 创建物流订单: " + order.getOrderId());
        System.out.println("[ShippingService] 商品已发货");
        coordinator.notify("shipping", "shipping.shipped", order);
    }
}

/**
 * 通知服务
 */
class NotificationService implements OrderServiceInterface {
    private OrderCoordinator coordinator;

    public NotificationService(OrderCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    @Override
    public void process(Object data) {
        Order order = (Order) data;
        System.out.println("[NotificationService] 发送通知给用户: " + order.getUserId());
        System.out.println("[NotificationService] 订单已完成，商品已发货");
    }
}

/**
 * 订单
 */
class Order {
    private String orderId;
    private String userId;
    private double amount;

    public Order(String orderId, String userId, double amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
    }

    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
}
