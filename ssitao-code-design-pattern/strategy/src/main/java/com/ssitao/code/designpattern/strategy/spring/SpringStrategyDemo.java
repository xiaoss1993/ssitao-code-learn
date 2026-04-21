package com.ssitao.code.designpattern.strategy.spring;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * Spring中的策略模式示例
 *
 * Spring应用：
 * 1. @Qualifier - 选择具体策略实现
 * 2. @Primary - 默认策略实现
 * 3. @Profile - 不同环境使用不同策略
 * 4. FactoryBean - 策略工厂
 * 5. Strategy模式 + Spring容器
 */
public class SpringStrategyDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring策略模式示例 ===\n");

        // 1. Spring Bean策略选择
        System.out.println("1. Spring Bean策略选择");
        beanStrategyDemo();

        // 2. 策略工厂模式
        System.out.println("\n2. 策略工厂模式");
        strategyFactoryDemo();

        // 3. 条件选择策略
        System.out.println("\n3. 条件选择策略");
        conditionalStrategyDemo();

        // 4. SPI机制
        System.out.println("\n4. SPI机制");
        spiStrategyDemo();
    }

    /**
     * Spring Bean策略选择示例
     */
    private static void beanStrategyDemo() {
        // 模拟Spring容器中的Bean
        Map<String, NotificationService> beans = new HashMap<>();
        beans.put("email", new EmailNotificationService());
        beans.put("sms", new SmsNotificationService());
        beans.put("push", new PushNotificationService());

        // 使用Bean名称获取策略
        System.out.println("--- 发送邮件通知 ---");
        NotificationService emailService = beans.get("email");
        emailService.send("用户注册成功");

        System.out.println("\n--- 发送短信通知 ---");
        NotificationService smsService = beans.get("sms");
        smsService.send("验证码: 123456");

        System.out.println("\n--- 发送推送通知 ---");
        NotificationService pushService = beans.get("push");
        pushService.send("您有新的消息");
    }

    /**
     * 策略工厂模式示例
     */
    private static void strategyFactoryDemo() {
        // 创建策略工厂
        PaymentStrategyFactory factory = new PaymentStrategyFactory();
        factory.register("alipay", new AlipayPaymentStrategy());
        factory.register("wechat", new WechatPaymentStrategy());
        factory.register("card", new CardPaymentStrategy());

        // 使用工厂获取策略
        System.out.println("--- 支付宝支付 ---");
        PaymentStrategy alipay = factory.getStrategy("alipay");
        alipay.pay(100.0);

        System.out.println("\n--- 微信支付 ---");
        PaymentStrategy wechat = factory.getStrategy("wechat");
        wechat.pay(100.0);

        System.out.println("\n--- 银行卡支付 ---");
        PaymentStrategy card = factory.getStrategy("card");
        card.pay(100.0);
    }

    /**
     * 条件选择策略示例
     */
    private static void conditionalStrategyDemo() {
        // 模拟根据用户等级选择策略
        String userLevel = "GOLD";

        System.out.println("--- 用户等级: " + userLevel + " ---");
        PricingStrategy strategy = PricingStrategyFactory.getStrategy(userLevel);
        double price = strategy.calculate(1000.0);
        System.out.println("原价: 1000, 折后价: " + price);

        // 切换用户等级
        userLevel = "SILVER";
        System.out.println("\n--- 用户等级: " + userLevel + " ---");
        strategy = PricingStrategyFactory.getStrategy(userLevel);
        price = strategy.calculate(1000.0);
        System.out.println("原价: 1000, 折后价: " + price);

        userLevel = "NORMAL";
        System.out.println("\n--- 用户等级: " + userLevel + " ---");
        strategy = PricingStrategyFactory.getStrategy(userLevel);
        price = strategy.calculate(1000.0);
        System.out.println("原价: 1000, 折后价: " + price);
    }

    /**
     * SPI机制示例（类似Dubbo/Feign的扩展机制）
     */
    private static void spiStrategyDemo() {
        // 模拟使用ServiceLoader加载策略
        // 实际项目中会使用 META-INF/services 配置

        System.out.println("--- 加载序列化策略 ---");
        // 模拟加载多个序列化实现
        SerializationStrategy jsonStrategy = new JsonSerializationStrategy();
        SerializationStrategy xmlStrategy = new XmlSerializationStrategy();
        SerializationStrategy protoStrategy = new ProtobufSerializationStrategy();

        // 使用策略
        System.out.println("JSON序列化:");
        String json = jsonStrategy.serialize(new User("Tom", 25));
        System.out.println("  " + json);

        System.out.println("\nXML序列化:");
        String xml = xmlStrategy.serialize(new User("Jerry", 30));
        System.out.println("  " + xml);

        System.out.println("\nProtobuf序列化:");
        String proto = protoStrategy.serialize(new User("Bob", 35));
        System.out.println("  " + proto);
    }
}

// ============================================
// 1. 通知服务相关类
// ============================================

/**
 * 通知服务接口
 */
interface NotificationService {
    void send(String message);
    String getType();
}

/**
 * 邮件通知
 */
class EmailNotificationService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("[邮件] 发送通知: " + message);
    }

    @Override
    public String getType() {
        return "email";
    }
}

/**
 * 短信通知
 */
class SmsNotificationService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("[短信] 发送通知: " + message);
    }

    @Override
    public String getType() {
        return "sms";
    }
}

/**
 * 推送通知
 */
class PushNotificationService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("[推送] 发送通知: " + message);
    }

    @Override
    public String getType() {
        return "push";
    }
}

// ============================================
// 2. 支付相关类
// ============================================

/**
 * 支付策略接口
 */
interface PaymentStrategy {
    void pay(double amount);
    String getType();
}

/**
 * 支付宝
 */
class AlipayPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("使用支付宝支付: " + amount + "元");
    }

    @Override
    public String getType() {
        return "alipay";
    }
}

/**
 * 微信支付
 */
class WechatPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("使用微信支付: " + amount + "元");
    }

    @Override
    public String getType() {
        return "wechat";
    }
}

/**
 * 银行卡
 */
class CardPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("使用银行卡支付: " + amount + "元");
    }

    @Override
    public String getType() {
        return "card";
    }
}

/**
 * 策略工厂
 */
class PaymentStrategyFactory {
    private Map<String, PaymentStrategy> strategies = new HashMap<>();

    public void register(String name, PaymentStrategy strategy) {
        strategies.put(name, strategy);
    }

    public PaymentStrategy getStrategy(String name) {
        PaymentStrategy strategy = strategies.get(name);
        if (strategy == null) {
            throw new IllegalArgumentException("未知的支付方式: " + name);
        }
        return strategy;
    }
}

// ============================================
// 3. 价格计算相关类
// ============================================

/**
 * 价格计算策略接口
 */
interface PricingStrategy {
    double calculate(double originalPrice);
}

/**
 * 定价策略工厂
 */
class PricingStrategyFactory {
    private static final Map<String, PricingStrategy> strategies = new HashMap<>();

    static {
        strategies.put("GOLD", new GoldPricingStrategy());
        strategies.put("SILVER", new SilverPricingStrategy());
        strategies.put("NORMAL", new NormalPricingStrategy());
    }

    public static PricingStrategy getStrategy(String userLevel) {
        PricingStrategy strategy = strategies.get(userLevel);
        return strategy != null ? strategy : strategies.get("NORMAL");
    }
}

/**
 * 金卡定价
 */
class GoldPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(double originalPrice) {
        return originalPrice * 0.7; // 7折
    }
}

/**
 * 银卡定价
 */
class SilverPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(double originalPrice) {
        return originalPrice * 0.85; // 85折
    }
}

/**
 * 普通定价
 */
class NormalPricingStrategy implements PricingStrategy {
    @Override
    public double calculate(double originalPrice) {
        return originalPrice;
    }
}

// ============================================
// 4. 序列化相关类
// ============================================

/**
 * 用户对象
 */
class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
}

/**
 * 序列化策略接口
 */
interface SerializationStrategy {
    String serialize(Object obj);
}

/**
 * JSON序列化
 */
class JsonSerializationStrategy implements SerializationStrategy {
    @Override
    public String serialize(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return "{\"name\":\"" + user.getName() + "\",\"age\":" + user.getAge() + "}";
        }
        return "{}";
    }
}

/**
 * XML序列化
 */
class XmlSerializationStrategy implements SerializationStrategy {
    @Override
    public String serialize(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return "<User><name>" + user.getName() + "</name><age>" + user.getAge() + "</age></User>";
        }
        return "<Object/>";
    }
}

/**
 * Protobuf序列化（模拟）
 */
class ProtobufSerializationStrategy implements SerializationStrategy {
    @Override
    public String serialize(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return "[User] name=" + user.getName() + ", age=" + user.getAge();
        }
        return "[Object]";
    }
}

/**
 * Spring中的策略模式使用示例
 *
 * 实际Spring项目中的使用方式：
 *
 * 1. 使用@Qualifier选择策略
 * @Service
 * public class OrderService {
 *     @Autowired
 *     @Qualifier("alipayStrategy")
 *     private PaymentStrategy paymentStrategy;
 * }
 *
 * 2. 使用@Primary设置默认策略
 * @Service
 * @Primary
 * public class DefaultPaymentStrategy implements PaymentStrategy {}
 *
 * 3. 使用@Profile不同环境
 * @Service
 * @Profile("dev")
 * public class DevPaymentStrategy implements PaymentStrategy {}
 *
 * 4. 使用@ConditionalOnProperty
 * @Service
 * @ConditionalOnProperty(name = "pay.type", havingValue = "alipay")
 * public class AlipayStrategy implements PaymentStrategy {}
 *
 * 5. 使用FactoryBean
 * @Bean
 * public PaymentStrategyFactory paymentStrategyFactory() {
 *     return new PaymentStrategyFactory();
 * }
 */
