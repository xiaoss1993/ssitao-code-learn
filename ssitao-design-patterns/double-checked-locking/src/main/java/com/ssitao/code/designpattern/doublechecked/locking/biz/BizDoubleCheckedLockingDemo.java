package com.ssitao.code.designpattern.doublechecked.locking.biz;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 业务场景中的双重检查锁定
 *
 * 典型业务应用：
 * 1. 订单号生成器 - 单例生成器
 * 2. 支付渠道管理 - 延迟初始化渠道
 * 3. 库存服务 - 单例库存操作
 * 4. 短信/邮件发送器 - 单例发送服务
 * 5. 第三方API客户端 - 单例API调用
 *
 * @author ssitao
 */
public class BizDoubleCheckedLockingDemo {

    public static void main(String[] args) {
        System.out.println("=== 业务场景双重检查锁定示例 ===\n");

        // 示例1：订单号生成器
        System.out.println("1. 订单号生成器示例");
        orderIdGeneratorDemo();

        // 示例2：支付渠道管理
        System.out.println("\n2. 支付渠道管理示例");
        paymentChannelDemo();

        // 示例3：第三方API客户端
        System.out.println("\n3. 第三方API客户端示例");
        apiClientDemo();
    }

    /**
     * 订单号生成器示例
     * 使用DCL实现单例订单号生成器
     */
    private static void orderIdGeneratorDemo() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 10个线程同时生成订单号
        for (int i = 0; i < 10; i++) {
            final int id = i;
            executor.submit(() -> {
                String orderId = OrderIdGenerator.getInstance().generate();
                System.out.println("线程-" + id + " 生成订单号: " + orderId);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 支付渠道管理示例
     * 使用DCL延迟初始化支付渠道
     */
    private static void paymentChannelDemo() {
        PaymentChannelManager manager = PaymentChannelManager.getInstance();

        // 获取不同的支付渠道
        PaymentChannel alipay = manager.getChannel("ALIPAY");
        PaymentChannel wechat = manager.getChannel("WECHAT");
        PaymentChannel bank = manager.getChannel("BANK");

        System.out.println("支付宝渠道: " + alipay.getName());
        System.out.println("微信渠道: " + wechat.getName());
        System.out.println("银行渠道: " + bank.getName());

        // 再次获取，验证是同一实例
        PaymentChannel alipay2 = manager.getChannel("ALIPAY");
        System.out.println("两次获取同一实例: " + (alipay == alipay2));
    }

    /**
     * 第三方API客户端示例
     * 使用DCL实现单例API客户端
     */
    private static void apiClientDemo() {
        ApiClient client1 = ApiClient.getInstance();
        ApiClient client2 = ApiClient.getInstance();

        System.out.println("同一实例: " + (client1 == client2));

        // 模拟API调用
        client1.get("user/info");
        client2.post("user/update", "data");
    }
}

/**
 * 订单号生成器 - DCL实现
 * 订单号生成器必须是单例，确保订单号唯一
 */
class OrderIdGenerator {
    private static volatile OrderIdGenerator instance;
    private long orderCount = 0;
    private final String prefix;

    private OrderIdGenerator() {
        this.prefix = "ORD-" + System.currentTimeMillis() + "-";
        System.out.println("OrderIdGenerator 初始化，前缀: " + prefix);
    }

    /**
     * 双重检查锁定获取实例
     */
    public static OrderIdGenerator getInstance() {
        if (instance == null) {
            synchronized (OrderIdGenerator.class) {
                if (instance == null) {
                    instance = new OrderIdGenerator();
                }
            }
        }
        return instance;
    }

    /**
     * 生成订单号
     * 格式：ORD-时间戳-序号
     */
    public synchronized String generate() {
        orderCount++;
        return prefix + String.format("%05d", orderCount);
    }

    public long getOrderCount() {
        return orderCount;
    }
}

/**
 * 支付渠道接口
 */
interface PaymentChannel {
    String getName();
    boolean pay(double amount);
    boolean refund(double amount);
}

/**
 * 支付渠道管理器 - DCL实现
 * 延迟初始化支付渠道
 */
class PaymentChannelManager {
    private static volatile PaymentChannelManager instance;
    private java.util.Map<String, PaymentChannel> channels;

    private PaymentChannelManager() {
        channels = new java.util.HashMap<>();
        System.out.println("PaymentChannelManager 初始化");
    }

    public static PaymentChannelManager getInstance() {
        if (instance == null) {
            synchronized (PaymentChannelManager.class) {
                if (instance == null) {
                    instance = new PaymentChannelManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取支付渠道 - 懒加载，使用DCL确保每个渠道只创建一次
     */
    public PaymentChannel getChannel(String channelCode) {
        // 先检查是否存在
        PaymentChannel channel = channels.get(channelCode);
        if (channel == null) {
            synchronized (this) {
                channel = channels.get(channelCode);
                if (channel == null) {
                    channel = createChannel(channelCode);
                    channels.put(channelCode, channel);
                }
            }
        }
        return channel;
    }

    private PaymentChannel createChannel(String channelCode) {
        switch (channelCode) {
            case "ALIPAY":
                return AlipayChannel.getInstance();
            case "WECHAT":
                return WechatPayChannel.getInstance();
            case "BANK":
                return BankChannel.getInstance();
            default:
                throw new IllegalArgumentException("未知渠道: " + channelCode);
        }
    }
}

/**
 * 支付宝渠道
 */
class AlipayChannel implements PaymentChannel {
    private static volatile AlipayChannel instance;

    private AlipayChannel() {
        System.out.println("AlipayChannel 初始化");
    }

    public static AlipayChannel getInstance() {
        if (instance == null) {
            synchronized (AlipayChannel.class) {
                if (instance == null) {
                    instance = new AlipayChannel();
                }
            }
        }
        return instance;
    }

    @Override
    public String getName() {
        return "支付宝";
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("支付宝支付: " + amount);
        return true;
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("支付宝退款: " + amount);
        return true;
    }
}

/**
 * 微信支付渠道
 */
class WechatPayChannel implements PaymentChannel {
    private static volatile WechatPayChannel instance;

    private WechatPayChannel() {
        System.out.println("WechatPayChannel 初始化");
    }

    public static WechatPayChannel getInstance() {
        if (instance == null) {
            synchronized (WechatPayChannel.class) {
                if (instance == null) {
                    instance = new WechatPayChannel();
                }
            }
        }
        return instance;
    }

    @Override
    public String getName() {
        return "微信支付";
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("微信支付: " + amount);
        return true;
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("微信退款: " + amount);
        return true;
    }
}

/**
 * 银行支付渠道
 */
class BankChannel implements PaymentChannel {
    private static volatile BankChannel instance;

    private BankChannel() {
        System.out.println("BankChannel 初始化");
    }

    public static BankChannel getInstance() {
        if (instance == null) {
            synchronized (BankChannel.class) {
                if (instance == null) {
                    instance = new BankChannel();
                }
            }
        }
        return instance;
    }

    @Override
    public String getName() {
        return "银行卡";
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("银行卡支付: " + amount);
        return true;
    }

    @Override
    public boolean refund(double amount) {
        System.out.println("银行卡退款: " + amount);
        return true;
    }
}

/**
 * API客户端 - DCL实现
 * 单例API客户端，管理HTTP连接和请求
 */
class ApiClient {
    private static volatile ApiClient instance;
    private String baseUrl;
    private int timeout;

    private ApiClient() {
        this.baseUrl = "https://api.example.com";
        this.timeout = 30000;
        System.out.println("ApiClient 初始化，baseUrl: " + baseUrl);
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    public String get(String endpoint) {
        System.out.println("GET请求: " + baseUrl + "/" + endpoint);
        return "{\"status\":\"success\"}";
    }

    public String post(String endpoint, String data) {
        System.out.println("POST请求: " + baseUrl + "/" + endpoint + ", data: " + data);
        return "{\"status\":\"success\"}";
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}

/**
 * 业务场景中DCL的最佳实践总结
 *
 * 1. 订单号/流水号生成器
 * - 必须单例，确保唯一性
 * - DCL + synchronized方法
 *
 * 2. 第三方服务客户端
 * - 连接池、HTTP客户端单例
 * - 减少连接创建开销
 *
 * 3. 缓存/配置服务
 * - 延迟初始化
 * - 只创建一次
 *
 * 4. 消息发送服务
 * - 邮件、短信、推送单例
 * - 复用连接
 *
 * 5. 分布式锁服务
 * - Zookeeper/Redis分布式锁客户端
 * - 确保只有一个客户端实例
 *
 * 注意事项：
 * - volatile是必须的（JDK5+）
 * - 适用于实例字段的延迟初始化
 * - 静态字段建议使用静态内部类或枚举
 * - 如果类会被序列化，不要使用DCL单例
 */
