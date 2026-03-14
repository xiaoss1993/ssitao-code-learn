package com.ssitao.code.designpattern.observer.jdk;

import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK内置观察者模式示例
 *
 * JDK提供的支持：
 * - java.util.Observable: 被观察者基类
 * - java.util.Observer: 观察者接口
 *
 * 注意：Observable在JDK9已被标记为@Deprecated，建议自行实现
 */
public class JdkObserverDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK观察者模式示例 ===\n");

        // 创建被观察者
        ProductStock stock = new ProductStock("iPhone 15");

        // 创建观察者
        Observer customerA = new CustomerObserver("顾客A");
        Observer customerB = new CustomerObserver("顾客B");
        Observer warehouseManager = new WarehouseObserver("仓库管理员");

        // 注册观察者
        stock.addObserver(customerA);
        stock.addObserver(customerB);
        stock.addObserver(warehouseManager);

        // 库存变化
        System.out.println("--- 进货100台 ---");
        stock.setStock(100);

        System.out.println("\n--- 卖出50台 ---");
        stock.setStock(50);

        System.out.println("\n--- 卖出30台 ---");
        stock.setStock(20);

        // 移除观察者
        System.out.println("\n--- 顾客B取消订阅 ---");
        stock.deleteObserver(customerB);

        System.out.println("\n--- 卖出10台 ---");
        stock.setStock(10);
    }
}

/**
 * 商品库存 - 使用JDK的Observable
 */
class ProductStock extends Observable {
    private String productName;
    private int stock;

    public ProductStock(String productName) {
        this.productName = productName;
        this.stock = 0;
    }

    public String getProductName() {
        return productName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        int oldStock = this.stock;
        this.stock = stock;

        // 只有库存变化时才通知
        if (stock != oldStock) {
            // 标记数据已改变
            setChanged();
            // 通知所有观察者
            notifyObservers(new StockEvent(productName, oldStock, stock));
        }
    }
}

/**
 * 库存事件
 */
class StockEvent {
    private String productName;
    private int oldStock;
    private int newStock;

    public StockEvent(String productName, int oldStock, int newStock) {
        this.productName = productName;
        this.oldStock = oldStock;
        this.newStock = newStock;
    }

    public String getProductName() { return productName; }
    public int getOldStock() { return oldStock; }
    public int getNewStock() { return newStock; }
    public int getChange() { return newStock - oldStock; }
}

/**
 * 顾客观察者
 */
class CustomerObserver implements Observer {
    private String name;

    public CustomerObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof StockEvent) {
            StockEvent event = (StockEvent) arg;
            System.out.println("[" + name + "] 收到通知: " + event.getProductName() +
                " 库存从 " + event.getOldStock() + " 变为 " + event.getNewStock());

            // 顾客根据库存决定是否购买
            if (event.getNewStock() > 0 && event.getNewStock() <= 20) {
                System.out.println("  -> 库存紧张，准备下单！");
            } else if (event.getNewStock() > 20) {
                System.out.println("  -> 库存充足，不着急购买");
            }
        }
    }
}

/**
 * 仓库管理员观察者
 */
class WarehouseObserver implements Observer {
    private String name;

    public WarehouseObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof StockEvent) {
            StockEvent event = (StockEvent) arg;
            System.out.println("[" + name + "] 收到通知: " + event.getProductName());

            if (event.getNewStock() < 10) {
                System.out.println("  -> 库存不足，需要补货！");
            } else if (event.getChange() > 0) {
                System.out.println("  -> 已入库 " + event.getChange() + " 件");
            } else {
                System.out.println("  -> 已出库 " + Math.abs(event.getChange()) + " 件");
            }
        }
    }
}

/**
 * 自定义实现观察者模式（替代JDK已废弃的Observable）
 */
class CustomObservableDemo {

    public static void main(String[] args) {
        System.out.println("\n=== 自定义观察者实现 ===\n");

        // 创建主题
        NewsAgency newsAgency = new NewsAgency();

        // 创建订阅者
        Subscriber subscriber1 = new EmailSubscriber("zhangsan@example.com");
        Subscriber subscriber2 = new SmsSubscriber("13800138000");
        Subscriber subscriber3 = new WechatSubscriber("微信号123");

        // 订阅
        newsAgency.subscribe(subscriber1);
        newsAgency.subscribe(subscriber2);
        newsAgency.subscribe(subscriber3);

        // 发布新闻
        System.out.println("--- 发布新闻1 ---");
        newsAgency.publishNews("Java 21正式发布");

        System.out.println("\n--- 发布新闻2 ---");
        newsAgency.publishNews("Spring Boot 3.0新特性");

        // 取消订阅
        System.out.println("\n--- 邮件订阅者取消订阅 ---");
        newsAgency.unsubscribe(subscriber1);

        System.out.println("\n--- 发布新闻3 ---");
        newsAgency.publishNews("云计算发展趋势");
    }
}

/**
 * 订阅者接口
 */
interface Subscriber {
    void receive(String news);
}

/**
 * 邮件订阅者
 */
class EmailSubscriber implements Subscriber {
    private String email;

    public EmailSubscriber(String email) {
        this.email = email;
    }

    @Override
    public void receive(String news) {
        System.out.println("[邮件] 发送到 " + email + ": " + news);
    }
}

/**
 * 短信订阅者
 */
class SmsSubscriber implements Subscriber {
    private String phone;

    public SmsSubscriber(String phone) {
        this.phone = phone;
    }

    @Override
    public void receive(String news) {
        System.out.println("[短信]发送到 " + phone + ": " + news);
    }
}

/**
 * 微信订阅者
 */
class WechatSubscriber implements Subscriber {
    private String wechatId;

    public WechatSubscriber(String wechatId) {
        this.wechatId = wechatId;
    }

    @Override
    public void receive(String news) {
        System.out.println("[微信]推送给 " + wechatId + ": " + news);
    }
}

/**
 * 新闻社 - 主题
 */
class NewsAgency {
    private List<Subscriber> subscribers = new ArrayList<>();

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        System.out.println("新订阅者: " + subscriber.getClass().getSimpleName());
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
        System.out.println("取消订阅: " + subscriber.getClass().getSimpleName());
    }

    public void publishNews(String news) {
        System.out.println("发布新闻: " + news);
        for (Subscriber subscriber : subscribers) {
            subscriber.receive(news);
        }
    }
}
