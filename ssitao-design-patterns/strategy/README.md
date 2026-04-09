# 策略模式 (Strategy Pattern)

## 模式定义

策略模式定义一系列算法，将每个算法封装起来，并使它们可以互换。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│     Context     │──────▶│    Strategy     │
│                 │       │   <<interface>> │
│  - strategy     │       └────────┬────────┘
│  + execute()    │                │
└─────────────────┘        ┌────────┴────────┐
                           │                │
                           ▼                ▼
                  ┌─────────────┐  ┌─────────────┐
                  │ ConcreteA   │  │ ConcreteB   │
                  │             │  │             │
                  └─────────────┘  └─────────────┘
```

## 核心思想

```
策略模式 = 接口 + 多实现 + 动态替换

问题：多种支付方式、多种排序算法、多种验证规则

解决：
    Strategy (接口)
        ├── PayByAlipay
        ├── PayByWechat
        └── PayByBankCard

    Context (上下文)
        - setStrategy(Strategy)
        - execute()
```

---

## 示例代码

### 基础示例：屠龙

```java
// 策略接口
public interface DragonSlayingStrategy {
    void execute();
}

// 具体策略：近战
public class MeleeStrategy implements DragonSlayingStrategy {
    @Override
    public void execute() {
        System.out.println("用剑与龙近身搏斗!");
    }
}

// 具体策略：投掷
public class ProjectileStrategy implements DragonSlayingStrategy {
    @Override
    public void execute() {
        System.out.println("向龙投掷魔法长矛!");
    }
}

// 具体策略：魔法
public class SpellStrategy implements DragonSlayingStrategy {
    @Override
    public void execute() {
        System.out.println("施展最强法术!");
    }
}

// 上下文
public class DragonSlayer {
    private DragonSlayingStrategy strategy;

    public DragonSlayer(DragonSlayingStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DragonSlayingStrategy strategy) {
        this.strategy = strategy;
    }

    public void changeStrategy(DragonSlayingStrategy strategy) {
        setStrategy(strategy);
        System.out.println("更换战术...");
    }

    public void goToBattle() {
        strategy.execute();
    }
}

// 使用
public class App {
    public static void main(String[] args) {
        DragonSlayer slayer = new DragonSlayer(new MeleeStrategy());
        slayer.goToBattle();  // 用剑近战

        slayer.changeStrategy(new SpellStrategy());
        slayer.goToBattle();  // 施展魔法
    }
}
```

---

## JDK中的应用

### 1. Comparator

```java
// 策略接口
public interface Comparator<T> {
    int compare(T o1, T o2);
}

// 具体策略
Comparator<String> byLength = (s1, s2) -> s1.length() - s2.length();
Comparator<String> alphabetically = String::compareTo;

// 使用
List<String> list = Arrays.asList("ccc", "aaa", "bbb");
list.sort(byLength);  // 按长度排序
```

### 2. ThreadPoolExecutorRejectedExecutionHandler

```java
// 拒绝策略接口
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}

// 具体策略
AbortPolicy abortPolicy = new AbortPolicy();     // 抛出异常
CallerRunsPolicy callerRunsPolicy = new CallerRunsPolicy();  // 由调用线程执行
DiscardPolicy discardPolicy = new DiscardPolicy();  // 丢弃
DiscardOldestPolicy discardOldestPolicy = new DiscardOldestPolicy();  // 丢弃最旧的
```

### 3. javax.script.Invoker

```java
// 脚本引擎策略
ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
// 不同的引擎实现不同的脚本解析策略
```

---

## Spring框架中的应用

### 1. Resource

```java
// 资源访问策略接口
public interface Resource extends InputStreamSource {
    boolean exists();
    boolean isReadable();
    boolean isOpen();
    // ...
}

// 具体策略
UrlResource urlResource = new UrlResource("file:data.txt");
ClassPathResource classPathResource = new ClassPathResource("data.txt");
FileSystemResource fileSystemResource = new FileSystemResource("data.txt");
```

### 2. Layout

```java
// Spring MVC中的视图解析策略
InternalResourceViewResolver beanNameViewResolver = new InternalResourceViewResolver();
beanNameViewResolver.setPrefix("/WEB-INF/views/");
beanNameViewResolver.setSuffix(".jsp");
```

### 3. PropertySource

```java
// 属性源策略
MapPropertySource mapPropertySource = new MapPropertySource("map", map);
SystemEnvironmentPropertySource systemEnv = new SystemEnvironmentPropertySource("env");
PropertiesPropertySource propertiesSource = new PropertiesPropertySource("props", props);
```

---

## MyBatis中的应用

### 1. ResultSetHandler

```java
// 结果集处理策略
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement stmt) throws SQLException;
}

// 具体策略
DefaultResultSetHandler defaultHandler = new DefaultResultSetHandler(executor);
NestedResultSetHandler nestedHandler = new NestedResultSetHandler(executor);
```

### 2. StatementHandler

```java
// SQL语句处理策略
SimpleStatementHandler simpleHandler = new SimpleStatementHandler(mappedStatement, parameter);
PreparedStatementHandler preparedHandler = new PreparedStatementHandler(mappedStatement, parameter);
CallableStatementHandler callableHandler = new CallableStatementHandler(mappedStatement, parameter);
```

---

## 适用场景

1. **多种算法/行为**：需要动态切换不同的算法
2. **避免条件语句**：替代大量的if-else或switch
3. **算法封装**：将相关算法封装成独立类
4. **规则引擎**：业务规则的可配置替换

## 优点

- **算法可替换**：策略可以动态切换
- **消除条件语句**：避免大量if-else
- **算法复用**：每个策略可独立复用
- **开闭原则**：新增策略不修改现有代码

## 缺点

- **类数量增加**：每个策略都需要一个类
- **客户端需了解策略**：客户端需要知道所有策略
- **复杂度增加**：策略选择需要额外代码

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 策略模式 | 组合到Context，委托执行 |
| 状态模式 | 状态决定行为，状态可自动转换 |
| 模板方法 | 子类决定步骤，继承实现 |
| 命令模式 | 请求封装，可排队、日志、撤销 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/strategy
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.strategy.App"
```

### 运行业务示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.strategy.biz.OrderStrategyDemo"
```

### 运行Spring示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.strategy.spring.SpringStrategyDemo"
```

### 运行Lambda策略示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.strategy.functional.LambdaStrategyDemo"
```
