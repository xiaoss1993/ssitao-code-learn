# 观察者模式 (Observer Pattern)

## 模式定义

观察者模式定义对象间的一种一对多依赖关系，当一个对象状态改变时，所有依赖它的对象都会收到通知并自动更新。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│    Subject     │──────▶│    Observer     │
│  (Observable)  │       │   <<interface>> │
├─────────────────┤       └────────┬────────┘
│ + attach()     │                │
│ + detach()     │       ┌────────┴────────┐
│ + notify()     │       │                 │
└─────────────────┘       ▼                 ▼
        │         ┌───────────┐     ┌───────────┐
        │         │ConcreteA  │     │ConcreteB  │
        └────────▶│Observer   │     │Observer   │
                  └───────────┘     └───────────┘
```

## 核心思想

```
观察者模式 = 主题 + 观察者 + 通知机制

关键点：
1. Subject (主题) - 维护观察者列表，状态变化时通知
2. Observer (观察者) - 定义更新方法接收通知
3. 订阅/取消订阅 - 动态管理观察者

应用场景：
- 消息推送系统
- 事件监听系统
- MVC架构
- 分布式系统事件处理
```

---

## 示例代码

### 基础示例：天气订阅

```java
// 观察者接口
public interface Observer {
    void update(WeatherType weather);
}

// 主题接口
public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// 具体主题：天气
public class Weather implements Observable {
    private List<Observer> observers = new ArrayList<>();
    private WeatherType currentWeather;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(currentWeather);
        }
    }

    public void setWeather(WeatherType weather) {
        this.currentWeather = weather;
        notifyObservers();
    }
}

// 具体观察者：兽人
public class Orcs implements Observer {
    @Override
    public void update(WeatherType weather) {
        // 根据天气做出反应
    }
}

// 使用
Weather weather = new Weather();
weather.addObserver(new Orcs());
weather.addObserver(new Hobbits());
weather.setWeather(WeatherType.SUNNY);
```

---

## JDK中的应用

### 1. Observable/Observer (JDK9已废弃)

```java
// Observable - 被观察者基类
public class Observable {
    private boolean changed = false;
    private Vector<Observer> obs = new Vector<>();

    public synchronized void addObserver(Observer o) {}
    public synchronized void deleteObserver(Observer o) {}
    public void notifyObservers() {}
    public void notifyObservers(Object arg) {}
    protected synchronized void setChanged() {}
    protected synchronized void clearChanged() {}
}

// Observer - 观察者接口
public interface Observer {
    void update(Observable o, Object arg);
}

// 注意：JDK9已废弃，建议自行实现观察者模式
```

### 2. EventListener

```java
// AWT中使用观察者模式
public interface ActionListener extends EventListener {
    void actionPerformed(ActionEvent e);
}

// 常见的监听器都是观察者模式
// MouseListener, KeyListener, WindowListener等
// Component.addActionListener() -> 观察者注册
// 事件触发 -> notifyListeners() -> 回调
```

### 3. JavaBeans PropertyChangeSupport

```java
// PropertyChangeSupport实现属性变化监听
public class PropertyChangeSupport {
    private Map<String, List<PropertyChangeListener>> listeners = new HashMap<>();

    public void addPropertyChangeListener(PropertyChangeListener listener) {}
    public void removePropertyChangeListener(PropertyChangeListener listener) {}
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        for (PropertyChangeListener listener : listeners.get(propertyName)) {
            listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }
}

// 使用
public class User {
    private PropertyChangeSupport support = new PropertyChangeSupport();

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        support.firePropertyChange("name", old, name);
    }
}
```

---

## Spring框架中的应用

### 1. ApplicationEvent

```java
// Spring事件机制使用观察者模式

// 事件源
public class User {
    private String name;
    // getters and setters
}

// 事件
public class UserCreatedEvent extends ApplicationEvent {
    private User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() { return user; }
}

// 监听器
@Component
public class UserCreatedListener implements ApplicationListener<UserCreatedEvent> {
    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        System.out.println("用户创建事件: " + event.getUser().getName());
    }
}

// 发布事件
@Service
public class UserService {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void createUser(User user) {
        // 创建用户逻辑
        publisher.publishEvent(new UserCreatedEvent(this, user));
    }
}
```

### 2. Spring的事件机制原理

```java
// ApplicationEventPublisher接口
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}

// 事件广播器
public class SimpleApplicationEventMulticaster {
    private List<ApplicationListener<ApplicationEvent>> listeners = new ArrayList<>();

    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<ApplicationEvent> listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }
}
```

### 3. @EventListener

```java
// 基于注解的事件监听
@Service
public class OrderService {

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("订单创建: " + event.getOrderId());
    }

    @EventListener(condition = "#event.status == 'PAID'")
    public void handleOrderPaid(OrderCreatedEvent event) {
        System.out.println("订单已支付: " + event.getOrderId());
    }

    @Async  // 异步处理
    @EventListener
    public void handleOrderNotification(OrderCreatedEvent event) {
        // 发送通知
    }
}
```

---

## MyBatis中的应用

### 1. Cache缓存失效机制

```java
// MyBatis的一级缓存使用观察者模式
// 当SQL执行时，相关的缓存应该失效

// Cache维系的本质是一个Map
// key = MappedStatementId + params
// value = 查询结果

// 当发生更新操作时，通知相关缓存失效
public class CachingExecutor implements Executor {
    private final Executor delegate;

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        // 执行更新
        int result = delegate.update(ms, parameter);
        // 通知缓存失效
        flush扣dStatements();
        return result;
    }

    @Override
    public void flushStatements(boolean isRollback) throws SQLException {
        delegate.flushStatements(isRollback);
        // 清除所有缓存
        cache.clear();
    }
}
```

### 2. ResultSetWrapper

```java
// MyBatis监听ResultSet状态变化
// 虽然不是标准的观察者模式，但实现了类似机制
public class ResultSetWrapper {
    private final ResultSet resultSet;
    private final List<String> columnNames = new ArrayList<>();
    private final List<Class<?>> columnTypes = new ArrayList<>();

    // 结果集关闭时触发回调
    public void close() throws SQLException {
        resultSet.close();
        // 触发监听器
        for (ResultSetClosedListener listener : closedListeners) {
            listener.onClose(this);
        }
    }
}
```

---

## 适用场景

1. **消息推送**：当对象状态变化需要通知多个其他对象
2. **事件监听**：GUI事件处理
3. **数据绑定**：模型数据变化自动更新视图
4. **日志系统**：记录操作日志

## 优点

- **松耦合**：主题和观察者不直接依赖
- **动态管理**：运行时可以动态添加/删除观察者
- **广播通信**：一个通知可以更新所有观察者
- **开闭原则**：新增观察者不修改主题代码

## 缺点

- **内存泄漏**：未正确移除观察者可能导致内存泄漏
- **通知顺序**：通知顺序不可控
- **循环依赖**：复杂场景可能导致循环依赖
- **性能问题**：观察者过多时通知耗时

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 观察者模式 | 一对多依赖，广播通信 |
| 发布订阅模式 | 中间件解耦，更灵活 |
| 事件驱动 | 异步处理，解耦更彻底 |
| MVC模式 | 观察者模式的实际应用 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/observer
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.observer.App"
```

### 运行JDK观察者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.observer.jdk.JdkObserverDemo"
```

### 运行Spring观察者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.observer.web.WebObserverDemo"
```

### 运行业务观察者示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.observer.biz.BizObserverDemo"
```

### 运行Guava EventBus示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.observer.framework.EventBusDemo"
```
