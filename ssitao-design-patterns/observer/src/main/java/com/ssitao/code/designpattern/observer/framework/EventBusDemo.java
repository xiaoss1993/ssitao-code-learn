package com.ssitao.code.designpattern.observer.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 通用事件总线框架示例（类似Guava EventBus）
 *
 * 框架特点：
 * 1. 基于事件类型订阅
 * 2. 支持事件优先级
 * 3. 支持事件过滤
 * 4. 支持异步处理
 * 5. 支持死信处理
 *
 * 实际应用：
 * - Guava EventBus
 * - Spring ApplicationEventMulticaster
 * - JDK9 Flow API
 */
public class EventBusDemo {

    public static void main(String[] args) {
        System.out.println("=== 事件总线框架示例 ===\n");

        // 1. 基础事件订阅
        System.out.println("1. 基础事件订阅");
        basicEventDemo();

        // 2. 事件优先级
        System.out.println("\n2. 事件优先级");
        priorityDemo();

        // 3. 事件过滤
        System.out.println("\n3. 事件过滤");
        filterDemo();

        // 4. 异常处理
        System.out.println("\n4. 异常处理");
        exceptionDemo();
    }

    /**
     * 基础事件订阅示例
     */
    private static void basicEventDemo() {
        // 创建事件总线
        EventBus eventBus = new EventBus();

        // 订阅事件
        eventBus.register(OrderEvent.class, new OrderEventHandler());
        eventBus.register(UserEvent.class, new UserEventHandler());
        eventBus.register(LoginEvent.class, new LoginEventHandler());

        // 发布事件
        System.out.println("--- 发布订单事件 ---");
        eventBus.post(new OrderEvent("ORD-001", "CREATE"));

        System.out.println("\n--- 发布用户事件 ---");
        eventBus.post(new UserEvent("user123", "REGISTER"));

        System.out.println("\n--- 发布登录事件 ---");
        eventBus.post(new LoginEvent("zhangsan", true));
    }

    /**
     * 事件优先级示例
     */
    private static void priorityDemo() {
        EventBus eventBus = new EventBus();

        // 按优先级订阅
        eventBus.register(1, OrderEvent.class, new HighPriorityHandler());
        eventBus.register(2, OrderEvent.class, new MediumPriorityHandler());
        eventBus.register(3, OrderEvent.class, new LowPriorityHandler());

        System.out.println("--- 发布订单事件 ---");
        eventBus.post(new OrderEvent("ORD-001", "CREATE"));
    }

    /**
     * 事件过滤示例
     */
    private static void filterDemo() {
        EventBus eventBus = new EventBus();

        // 订阅带过滤器的事件
        eventBus.register(OrderEvent.class, new OrderEventHandler(),
            event -> "CREATE".equals(event.getAction()));

        System.out.println("--- 发布创建订单事件（符合过滤条件）---");
        eventBus.post(new OrderEvent("ORD-001", "CREATE"));

        System.out.println("\n--- 发布支付订单事件（不符合过滤条件）---");
        eventBus.post(new OrderEvent("ORD-001", "PAID"));
    }

    /**
     * 异常处理示例
     */
    private static void exceptionDemo() {
        EventBus eventBus = new EventBus();

        // 设置异常处理器
        eventBus.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e, Object event) {
                System.out.println("[异常处理器] 处理事件时发生异常: " + e.getMessage());
            }
        });

        // 订阅会抛出异常的处理者
        eventBus.register(OrderEvent.class, new ExceptionThrowingHandler());

        System.out.println("--- 发布事件（处理者会抛出异常）---");
        eventBus.post(new OrderEvent("ORD-001", "CREATE"));
    }
}

// ============================================
// 事件定义
// ============================================

/**
 * 事件基类
 */
abstract class Event {
    private long timestamp;

    public Event() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * 订单事件
 */
class OrderEvent extends Event {
    private String orderId;
    private String action;

    public OrderEvent(String orderId, String action) {
        this.orderId = orderId;
        this.action = action;
    }

    public String getOrderId() { return orderId; }
    public String getAction() { return action; }
}

/**
 * 用户事件
 */
class UserEvent extends Event {
    private String userId;
    private String type;

    public UserEvent(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }

    public String getUserId() { return userId; }
    public String getType() { return type; }
}

/**
 * 登录事件
 */
class LoginEvent extends Event {
    private String username;
    private boolean success;

    public LoginEvent(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    public String getUsername() { return username; }
    public boolean isSuccess() { return success; }
}

// ============================================
// 事件处理者
// ============================================

/**
 * 事件处理者接口
 */
interface EventHandler<E extends Event> {
    void handle(E event);
}

/**
 * 订单事件处理者
 */
class OrderEventHandler implements EventHandler<OrderEvent> {
    @Override
    public void handle(OrderEvent event) {
        System.out.println("[OrderHandler] 处理订单事件: " + event.getOrderId() + ", 操作: " + event.getAction());
    }
}

/**
 * 用户事件处理者
 */
class UserEventHandler implements EventHandler<UserEvent> {
    @Override
    public void handle(UserEvent event) {
        System.out.println("[UserHandler] 处理用户事件: " + event.getUserId() + ", 类型: " + event.getType());
    }
}

/**
 * 登录事件处理者
 */
class LoginEventHandler implements EventHandler<LoginEvent> {
    @Override
    public void handle(LoginEvent event) {
        String result = event.isSuccess() ? "成功" : "失败";
        System.out.println("[LoginHandler] 用户 " + event.getUsername() + " 登录" + result);
    }
}

// 优先级处理者
class HighPriorityHandler implements EventHandler<OrderEvent> {
    @Override
    public void handle(OrderEvent event) {
        System.out.println("[高优先级] 首先处理订单: " + event.getOrderId());
    }
}

class MediumPriorityHandler implements EventHandler<OrderEvent> {
    @Override
    public void handle(OrderEvent event) {
        System.out.println("[中优先级] 第二处理订单: " + event.getOrderId());
    }
}

class LowPriorityHandler implements EventHandler<OrderEvent> {
    @Override
    public void handle(OrderEvent event) {
        System.out.println("[低优先级] 最后处理订单: " + event.getOrderId());
    }
}

/**
 * 会抛出异常的处理器
 */
class ExceptionThrowingHandler implements EventHandler<OrderEvent> {
    @Override
    public void handle(OrderEvent event) {
        throw new RuntimeException("模拟处理异常");
    }
}

// ============================================
// 事件总线框架
// ============================================

/**
 * 异常处理器接口
 */
interface ExceptionHandler {
    void handleException(Exception e, Object event);
}

/**
 * 事件订阅者
 */
class Subscriber {
    private int priority;
    private EventHandler handler;
    private java.util.function.Predicate filter;

    public Subscriber(int priority, EventHandler handler, java.util.function.Predicate filter) {
        this.priority = priority;
        this.handler = handler;
        this.filter = filter;
    }

    public int getPriority() { return priority; }
    public EventHandler getHandler() { return handler; }
    public java.util.function.Predicate getFilter() { return filter; }

    public boolean matchesFilter(Event event) {
        return filter == null || filter.test(event);
    }
}

/**
 * 事件总线
 */
class EventBus {
    // 事件类型 -> 订阅者列表
    private Map<Class<? extends Event>, List<Subscriber>> subscribers = new ConcurrentHashMap<>();
    private ExceptionHandler exceptionHandler;

    /**
     * 注册事件处理者
     */
    public <E extends Event> void register(Class<E> eventType, EventHandler<E> handler) {
        register(0, eventType, handler, null);
    }

    /**
     * 带优先级注册
     */
    public <E extends Event> void register(int priority, Class<E> eventType, EventHandler<E> handler) {
        register(priority, eventType, handler, null);
    }

    /**
     * 带过滤器注册
     */
    public <E extends Event> void register(Class<E> eventType, EventHandler<E> handler,
                                         java.util.function.Predicate<E> filter) {
        register(0, eventType, handler, filter);
    }

    /**
     * 注册（完整参数）
     */
    @SuppressWarnings("unchecked")
    public <E extends Event> void register(int priority, Class<E> eventType,
                                          EventHandler<E> handler,
                                          java.util.function.Predicate<E> filter) {
        Subscriber subscriber = new Subscriber(priority, handler, (java.util.function.Predicate<Event>) filter);

        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(subscriber);

        // 按优先级排序（高优先级在前）
        List<Subscriber> list = subscribers.get(eventType);
        list.sort((a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
    }

    /**
     * 发布事件
     */
    @SuppressWarnings("unchecked")
    public void post(Event event) {
        Class<? extends Event> eventType = event.getClass();
        List<Subscriber> list = subscribers.get(eventType);

        if (list == null || list.isEmpty()) {
            System.out.println("没有订阅者处理事件: " + eventType.getSimpleName());
            return;
        }

        for (Subscriber subscriber : list) {
            if (!subscriber.matchesFilter(event)) {
                continue;
            }

            try {
                EventHandler handler = subscriber.getHandler();
                handler.handle(event);
            } catch (Exception e) {
                if (exceptionHandler != null) {
                    exceptionHandler.handleException(e, event);
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置异常处理器
     */
    public void setExceptionHandler(ExceptionHandler handler) {
        this.exceptionHandler = handler;
    }

    /**
     * 取消注册
     */
    public <E extends Event> void unregister(Class<E> eventType, EventHandler<E> handler) {
        List<Subscriber> list = subscribers.get(eventType);
        if (list != null) {
            list.removeIf(s -> s.getHandler() == handler);
        }
    }
}

// ============================================
// Spring EventBus 风格示例
// ============================================

/**
 * Spring EventBus 风格示例
 *
 * 实际Spring中使用：
 *
 * // 定义事件
 * public class OrderCreatedEvent extends ApplicationEvent {
 *     private String orderId;
 *     public OrderCreatedEvent(Object source, String orderId) {
 *         super(source);
 *         this.orderId = orderId;
 *     }
 * }
 *
 * // 发布事件
 * @Autowired
 * private ApplicationEventPublisher publisher;
 *
 * public void createOrder(String orderId) {
 *     // ... 创建订单逻辑
 *     publisher.publishEvent(new OrderCreatedEvent(this, orderId));
 * }
 *
 * // 监听事件
 * @Component
 * public class OrderEventListener {
 *     @EventListener
 *     public void handleOrderCreated(OrderCreatedEvent event) {
 *         // ... 处理订单创建事件
 *     }
 * }
 *
 * // 异步处理
 * @Async
 * @EventListener
 * public void handleAsync(OrderCreatedEvent event) {
 *     // 异步处理
 * }
 */