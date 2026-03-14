package com.ssitao.code.designpattern.observer.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Web应用中的观察者模式示例
 *
 * Web应用场景：
 * 1. 事件监听器 - ServletContextListener
 * 2. Spring事件机制 - ApplicationEvent
 * 3. WebSocket实时推送
 * 4. 消息队列发布/订阅
 * 5. 缓存失效通知
 */
public class WebObserverDemo {

    public static void main(String[] args) {
        System.out.println("=== Web观察者模式示例 ===\n");

        // 1. 事件监听器示例
        System.out.println("1. 事件监听器示例");
        eventListenerDemo();

        // 2. Spring事件机制示例
        System.out.println("\n2. Spring事件机制示例");
        springEventDemo();

        // 3. 实时消息推送示例
        System.out.println("\n3. 实时消息推送示例");
        realtimePushDemo();
    }

    /**
     * 事件监听器示例 - 类似ServletContextListener
     */
    private static void eventListenerDemo() {
        // 创建应用上下文
        ApplicationContext context = new ApplicationContext();

        // 添加监听器
        context.addListener(new DatabaseConnectionListener());
        context.addListener(new CacheInitializationListener());
        context.addListener(new LoggerInitializationListener());

        // 启动应用
        System.out.println("--- 启动应用 ---");
        context.start();

        // 关闭应用
        System.out.println("\n--- 关闭应用 ---");
        context.stop();
    }

    /**
     * Spring事件机制示例
     */
    private static void springEventDemo() {
        // 创建事件发布器
        ApplicationEventPublisher publisher = new ApplicationEventPublisher();

        // 注册监听器
        publisher.addListener(new UserRegisterListener());
        publisher.addListener(new SendWelcomeEmailListener());
        publisher.addListener(new PointsAwardListener());

        // 发布用户注册事件
        System.out.println("--- 用户注册 ---");
        publisher.publishEvent(new UserRegisterEvent("张三", "zhangsan@example.com"));

        // 发布另一个注册事件
        System.out.println("\n--- 另一个用户注册 ---");
        publisher.publishEvent(new UserRegisterEvent("李四", "lisi@example.com"));
    }

    /**
     * 实时消息推送示例 - 类似WebSocket
     */
    private static void realtimePushDemo() {
        // 创建消息服务器
        MessageServer server = new MessageServer();

        // 用户订阅主题
        UserClient client1 = new UserClient("用户A");
        UserClient client2 = new UserClient("用户B");
        UserClient client3 = new UserClient("用户C");

        server.subscribe(client1, "news");
        server.subscribe(client2, "news");
        server.subscribe(client3, "sports");

        // 发布消息
        System.out.println("--- 发布新闻 ---");
        server.publish("news", "重大新闻：Java 22发布了！");

        System.out.println("\n--- 发布体育 ---");
        server.publish("sports", "足球比赛：阿根廷战胜巴西");

        System.out.println("\n--- 再次发布新闻 ---");
        server.publish("news", "科技新闻：AI技术新突破");
    }
}

// ============================================
// 1. 事件监听器相关类
// ============================================

/**
 * 应用上下文 - 被观察者
 */
class ApplicationContext {
    private List<AppListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(AppListener listener) {
        listeners.add(listener);
    }

    public void removeListener(AppListener listener) {
        listeners.remove(listener);
    }

    public void start() {
        // 触发启动事件
        AppEvent event = new AppEvent("APPLICATION_START");
        for (AppListener listener : listeners) {
            listener.onApplicationStart(event);
        }
    }

    public void stop() {
        // 触发关闭事件
        AppEvent event = new AppEvent("APPLICATION_STOP");
        for (AppListener listener : listeners) {
            listener.onApplicationStop(event);
        }
    }
}

/**
 * 应用事件
 */
class AppEvent {
    private String type;

    public AppEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

/**
 * 应用监听器接口
 */
interface AppListener {
    void onApplicationStart(AppEvent event);
    void onApplicationStop(AppEvent event);
}

/**
 * 数据库连接监听器
 */
class DatabaseConnectionListener implements AppListener {

    @Override
    public void onApplicationStart(AppEvent event) {
        System.out.println("[DatabaseListener] 初始化数据库连接池");
    }

    @Override
    public void onApplicationStop(AppEvent event) {
        System.out.println("[DatabaseListener] 关闭数据库连接");
    }
}

/**
 * 缓存初始化监听器
 */
class CacheInitializationListener implements AppListener {

    @Override
    public void onApplicationStart(AppEvent event) {
        System.out.println("[CacheListener] 初始化缓存");
    }

    @Override
    public void onApplicationStop(AppEvent event) {
        System.out.println("[CacheListener] 清除缓存");
    }
}

/**
 * 日志初始化监听器
 */
class LoggerInitializationListener implements AppListener {

    @Override
    public void onApplicationStart(AppEvent event) {
        System.out.println("[LoggerListener] 初始化日志系统");
    }

    @Override
    public void onApplicationStop(AppEvent event) {
        System.out.println("[LoggerListener] 关闭日志");
    }
}

// ============================================
// 2. Spring事件机制相关类
// ============================================

/**
 * Spring事件发布器
 */
class ApplicationEventPublisher {
    private List<ApplicationListener> listeners = new ArrayList<>();

    public void addListener(ApplicationListener listener) {
        listeners.add(listener);
    }

    public void publishEvent(ApplicationEvent event) {
        System.out.println("[EventPublisher] 发布事件: " + event.getClass().getSimpleName());
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }
}

/**
 * Spring事件
 */
abstract class ApplicationEvent {
    private long timestamp = System.currentTimeMillis();

    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * 用户注册事件
 */
class UserRegisterEvent extends ApplicationEvent {
    private String username;
    private String email;

    public UserRegisterEvent(String username, String email) {
        super();
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
}

/**
 * Spring应用监听器
 */
interface ApplicationListener {
    void onApplicationEvent(ApplicationEvent event);
}

/**
 * 用户注册监听器
 */
class UserRegisterListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("[UserRegisterListener] 用户注册: " + registerEvent.getUsername());
        }
    }
}

/**
 * 发送欢迎邮件监听器
 */
class SendWelcomeEmailListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("[WelcomeEmailListener] 发送欢迎邮件到: " + registerEvent.getEmail());
        }
    }
}

/**
 * 积分奖励监听器
 */
class PointsAwardListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserRegisterEvent) {
            UserRegisterEvent registerEvent = (UserRegisterEvent) event;
            System.out.println("[PointsAwardListener] 给用户 " + registerEvent.getUsername() + " 奖励100积分");
        }
    }
}

// ============================================
// 3. 实时消息推送相关类
// ============================================

/**
 * 消息服务器 - 主题
 */
class MessageServer {
    private Map<String, List<UserClient>> topicSubscribers = new HashMap<>();

    // 订阅主题
    public void subscribe(UserClient client, String topic) {
        topicSubscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(client);
        System.out.println(client.getName() + " 订阅了主题: " + topic);
    }

    // 取消订阅
    public void unsubscribe(UserClient client, String topic) {
        List<UserClient> clients = topicSubscribers.get(topic);
        if (clients != null) {
            clients.remove(client);
        }
    }

    // 发布消息
    public void publish(String topic, String message) {
        System.out.println("[服务器] 主题 " + topic + " 发布消息: " + message);
        List<UserClient> clients = topicSubscribers.get(topic);
        if (clients != null) {
            for (UserClient client : clients) {
                client.receiveMessage(topic, message);
            }
        }
    }
}

/**
 * 用户客户端 - 观察者
 */
class UserClient {
    private String name;

    public UserClient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void receiveMessage(String topic, String message) {
        System.out.println("  -> [" + name + "] 收到[" + topic + "]消息: " + message);
    }
}
