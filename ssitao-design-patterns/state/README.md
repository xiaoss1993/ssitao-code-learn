# 状态模式 (State Pattern)

## 模式定义

状态模式允许对象在内部状态改变时改变它的行为，看起来好像修改了它的类。

## UML结构

```
┌─────────────────────────┐
│       Context           │
├─────────────────────────┤
│ - state: State          │
│ + request()             │
└───────────┬─────────────┘
            │ has
            ▼
┌─────────────────────────┐
│       State             │
│   <<interface>>         │
├─────────────────────────┤
│ + handle(Context)       │
└───────────┬─────────────┘
            △
            │
┌───────────┴─────────────┐
│    ConcreteStateA      │
├─────────────────────────┤
│ + handle(Context)       │
│ + nextState()           │
└─────────────────────────┘
            │
            ▼
┌─────────────────────────┐
│    ConcreteStateB       │
├─────────────────────────┤
│ + handle(Context)       │
│ + nextState()           │
└─────────────────────────┘
```

## 核心思想

```
状态模式 = Context + State + ConcreteState

关键点：
1. Context持有当前State的引用
2. State定义状态行为接口
3. ConcreteState实现具体状态行为
4. 状态转换可以由Context或State触发

与策略模式区别：
- 状态模式：状态决定行为，状态可自动转换
- 策略模式：客户端选择策略，委托执行
```

---

## 示例代码

### 基础示例：猛犸象

```java
// 状态接口
public interface State {
    void onEnterState();
    void observe();
}

// 具体状态：平静状态
public class PeacefulState implements State {
    private Mammoth mammoth;

    public PeacefulState(Mammoth mammoth) {
        this.mammoth = mammoth;
    }

    @Override
    public void onEnterState() {
        System.out.println("猛犸象变得平静...");
    }

    @Override
    public void observe() {
        System.out.println("猛犸象正在安静地吃草。");
    }
}

// 具体状态：愤怒状态
public class AngryState implements State {
    private Mammoth mammoth;

    public AngryState(Mammoth mammoth) {
        this.mammoth = mammoth;
    }

    @Override
    public void onEnterState() {
        System.out.println("猛犸象变得愤怒！");
    }

    @Override
    public void observe() {
        System.out.println("猛犸象愤怒地跺脚！");
    }
}

// 上下文：猛犸象
public class Mammoth {
    private State state;

    public Mammoth() {
        this.state = new PeacefulState(this);
    }

    public void timePasses() {
        if (state.getClass().equals(PeacefulState.class)) {
            changeStateTo(new AngryState(this));
        } else {
            changeStateTo(new PeacefulState(this));
        }
    }

    private void changeStateTo(State newState) {
        this.state = newState;
        this.state.onEnterState();
    }

    public void observe() {
        this.state.observe();
    }
}

// 使用
Mammoth mammoth = new Mammoth();
mammoth.observe();  // 平静状态
mammoth.timePasses();
mammoth.observe();  // 愤怒状态
mammoth.timePasses();
mammoth.observe();  // 平静状态
```

---

## JDK中的应用

### 1. Thread.State

```java
// Java线程状态使用状态模式
public class Thread {
    public enum State {
        NEW,           // 新建
        RUNNABLE,      // 可运行
        BLOCKED,       // 阻塞
        WAITING,       // 等待
        TIMED_WAITING, // 定时等待
        TERMINATED;    // 终止
    }

    private State state;  // 内部状态

    public State getState() {
        return state;
    }

    // 状态转换由JVM控制
}
```

### 2. Java并发状态管理

```java
// FutureTask使用状态模式管理任务状态
public class FutureTask<V> implements RunnableFuture<V> {
    private volatile int state;
    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;
    private static final int EXCEPTIONAL = 3;
    private static final int CANCELLED = 4;
    private static final int INTERRUPTED = 5;

    // 状态转换
    protected void set(V v) {
        if (state == COMPLETING) {
            state = NORMAL;
            outcome = v;
        }
    }

    protected void setException(Throwable t) {
        if (state == COMPLETING) {
            state = EXCEPTIONAL;
            outcome = t;
        }
    }
}
```

---

## Spring框架中的应用

### 1. Spring StateMachine

```java
// Spring StateMachine使用状态模式
// 订单状态机示例
public enum OrderState {
    UNPAID,       // 未支付
    PAID,         // 已支付
    SHIPPED,      // 已发货
    DELIVERED,    // 已送达
    CANCELLED     // 已取消
}

public enum OrderEvent {
    PAY,          // 支付
    SHIP,         // 发货
    DELIVER,      // 送达
    CANCEL        // 取消
}

// 状态转换配置
@Configuration
public class OrderStateMachineConfig {
    @Bean
    public StateMachine<OrderState, OrderEvent> orderStateMachine() {
        StateMachineBuilder.Builder<OrderState, OrderEvent> builder =
            StateMachineBuilder.builder();

        builder.configureStates()
            .withStates()
            .initial(OrderState.UNPAID)
            .states(EnumSet.allOf(OrderState.class))
            .end(OrderState.DELIVERED)
            .end(OrderState.CANCELLED);

        builder.configureTransitions()
            .withExternal()
            .source(OrderState.UNPAID).target(OrderState.PAID)
            .event(OrderEvent.PAY)
            .and()
            .withExternal()
            .source(OrderState.PAID).target(OrderState.SHIPPED)
            .event(OrderEvent.SHIP)
            // ...

        return builder.build();
    }
}
```

### 2. IntegrationFlow

```java
// Spring Integration使用状态模式处理消息流
// MessageHeaderAccessor管理消息状态
public class MessageHeaderAccessor {
    private Map<String, Object> headers = new HashMap<>();

    public void setHeader(String name, Object value) {
        // 设置消息头
    }

    // 消息状态在header中传递
}
```

---

## MyBatis中的应用

### 1. Transaction State

```java
// MyBatis事务状态管理
public interface Transaction {
    Connection getConnection() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    void close() throws SQLException;
    boolean isActive() throws SQLException;
}

// JdbcTransaction状态
public class JdbcTransaction implements Transaction {
    private boolean autoCommit = true;
    private boolean committed = false;
    private boolean closed = false;

    @Override
    public void commit() throws SQLException {
        if (!autoCommit && !committed) {
            connection.commit();
            committed = true;
        }
    }

    @Override
    public boolean isActive() {
        return !closed && !committed;
    }
}
```

### 2. Executor State

```java
// MyBatis的Executor维护查询状态
public abstract class BaseExecutor implements Executor {
    protected boolean closed = false;

    @Override
    public void close(boolean forceRollback) {
        if (closed) {
            throw new RuntimeException("Executor already closed");
        }
        closed = true;
        if (forceRollback) {
            rollback(forceRollback);
        }
    }

    protected void throwIfClosed() {
        if (closed) {
            throw new RuntimeException("Executor was closed");
        }
    }
}
```

---

## 适用场景

1. **状态机**：订单、审批等流程
2. **游戏状态**：角色状态、游戏关卡
3. **对象行为依赖状态**：根据状态决定行为
4. **复杂条件分支**：替代大量if-else

## 优点

- **消除if-else**：状态行为封装
- **状态转换封装**：转换逻辑集中
- **易于扩展**：新增状态容易

## 缺点

- **类数量增加**：每个状态一个类
- **过度设计**：简单场景可能过度
- **状态耦合**：状态间可能耦合

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 状态模式 | 状态决定行为，自动转换 |
| 策略模式 | 客户端选择策略 |
| 桥接模式 | 分离抽象与实现 |
| 模板方法 | 继承实现，部分重写 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/state
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.state.App"
```

### 运行基本状态示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.state.basic.BasicStateDemo"
```

### 运行业务状态示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.state.biz.BizStateDemo"
```

### 运行Spring状态机示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.state.web.WebStateDemo"
```

### 运行糖果机示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.state.gumball.GumballStateDemo"
```
