# 适配器模式 (Adapter Pattern)

## 模式定义

适配器模式将一个类的接口转换成客户端期望的另一个接口，使原本接口不兼容的类可以一起工作。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│     Client      │       │     Adapter     │       │     Adaptee    │
│                 │       │                 │       │                 │
│                 │──────▶│                 │──────▶│                 │
│                 │       │                 │       │                 │
│                 │       │  implements    │       │                 │
│                 │       │  Target        │       │                 │
└─────────────────┘       └─────────────────┘       └─────────────────┘
                                    △
                                    │
                           ┌────────┴────────┐
                           │                 │
                    ┌──────┴──────┐  ┌───────┴───────┐
                    │  Class      │  │   Object       │
                    │  Adapter   │  │   Adapter      │
                    │             │  │                │
                    │  extends    │  │  contains      │
                    │  Adaptee   │  │  Adaptee       │
                    └─────────────┘  └────────────────┘
```

## 两种变体

### 1. 类适配器（Class Adapter）
- 通过继承来实现
- Java中受限（单继承）

### 2. 对象适配器（Object Adapter）
- 通过组合来实现
- 更灵活，常用

## 示例代码

### 基础示例：战舰与渔船

```java
// 目标接口：战舰
public interface BattleShip {
    void fire();
    void move();
}

// 适配者：渔船
public class FishingBoat {
    public void sail() {
        System.out.println("渔船航行...");
    }
}

// 适配器：将渔船适配为战舰
public class BattleFishingBoat implements BattleShip {
    private final FishingBoat boat;

    public BattleFishingBoat(FishingBoat boat) {
        this.boat = boat;
    }

    @Override
    public void fire() {
        System.out.println("发射炮弹!");
    }

    @Override
    public void move() {
        boat.sail();  // 调用适配者的方法
    }
}

// 客户端：船长
public class Captain {
    private final BattleShip ship;

    public Captain(BattleShip ship) {
        this.ship = ship;
    }

    public void attack() {
        ship.fire();
        ship.move();
    }
}
```

---

## JDK中的应用

### 1. InputStreamReader / OutputStreamWriter

```java
// InputStreamReader: 将InputStream适配为Reader
// OutputStreamWriter: 将Writer适配为OutputStream

InputStream is = new FileInputStream("file.txt");
Reader reader = new InputStreamReader(is, "UTF-8");  // 字节流 -> 字符流

Writer writer = new OutputStreamWriter(os, "UTF-8");  // 字符流 -> 字节流
```

### 2. Arrays.asList()

```java
String[] array = {"A", "B", "C"};
List<String> list = Arrays.asList(array);  // 数组 -> List
```

### 3. Collections.enumeration()

```java
List<String> list = Arrays.asList("A", "B", "C");
Iterator<String> iterator = list.iterator();
Enumeration<String> enumeration = Collections.enumeration(list);  // Iterator -> Enumeration
```

### 4. Properties

```java
Properties props = new Properties();
props.setProperty("name", "value");  // 同时支持Map和Hashtable API
```

---

## 框架中的应用

### MyBatis

#### TypeHandler - JDBC类型 ↔ Java类型

```java
// MyBatis的TypeHandler负责类型转换
public interface TypeHandler<T> {
    T getResult(ResultSet rs, String columnName) throws Exception;
    void setParameter(PreparedStatement ps, int position, T parameter) throws Exception;
}

// 内置的TypeHandler
// StringTypeHandler: String <-> VARCHAR
// DateTypeHandler: java.util.Date <-> TIMESTAMP
// IntegerTypeHandler: Integer <-> INTEGER
```

#### ResultSetHandler - ResultSet ↔ Java对象

```java
// MyBatis的ResultSetHandler将JDBC ResultSet映射为Java对象
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement statement) throws SQLException;
}

// 实现类负责：
// 1. ResultSet.next() - 移动游标
// 2. ResultSet.getXxx() - 获取列值
// 3. TypeHandler.getXxx() - 类型转换
// 4. 创建Java对象并填充属性
```

#### Log适配器

```java
// MyBatis定义统一Log接口，内部适配各种日志框架
public interface Log {
    void debug(String message);
    void info(String message);
}

// 适配SLF4J
class Slf4jLoggerImpl implements Log {
    private final org.slf4j.Logger logger;
    // ...
}
```

---

## Spring框架中的应用

### 1. JdbcTemplate

```java
// JdbcTemplate使用适配器模式
// 将JDBC的checked SQLException适配为RuntimeException
// 简化JDBC编程，无需处理异常
```

### 2. HandlerAdapter

```java
// Spring MVC的HandlerAdapter将不同类型的Handler适配为统一的接口
public interface HandlerAdapter {
    boolean supports(Object handler);
    ModelAndView handle(HttpServletRequest request,
                       HttpServletResponse response, Object handler) throws Exception;
}

// 支持多种Handler：
// - ControllerHandlerAdapter -> @Controller
// - HttpRequestHandlerAdapter -> HttpRequestHandler
// - SimpleServletHandlerAdapter -> Servlet
```

### 3. TransactionManager适配Hibernate

```java
// Spring将不同的事务API统一
// HibernateTransactionManager
// DataSourceTransactionManager
// JpaTransactionManager
```

---

## XML解析中的适配器

### DocumentBuilder

```java
// DocumentBuilder将XML适配为DOM Document
DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document doc = builder.parse(new FileInputStream("config.xml"));
// 之后可以像操作对象一样操作XML
```

### SAXParser

```java
// SAXParser将XML适配为事件流
SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
parser.parse(inputStream, new DefaultHandler() {
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        // 元素开始事件
    }
});
```

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/adapter
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.adapter.App"
```

### 运行JDK示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.adapter.jdk.JDKAdapterDemo"
```

### 运行MyBatis示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.adapter.mybatis.MyBatisAdapterDemo"
```

### 运行XML适配器示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.adapter.xml.XMLAdapterDemo"
```

---

## 适用场景

1. **接口不兼容**：现有类的接口与需要的接口不匹配
2. **复用旧代码**：复用已有类，但接口不兼容
3. **统一接口**：创建统一接口，让不同实现可以互换
4. **版本兼容**：新旧版本接口过渡

## 优点

- 提高类的复用性
- 提高代码灵活性
- 符合开闭原则（不修改原有代码）

## 缺点

- 过度使用会增加系统复杂度
- 有时直接修改接口更简单

## 对比装饰器模式

| 特性 | 适配器模式 | 装饰器模式 |
|------|------------|------------|
| 目的 | 接口转换 | 功能增强 |
| 关系 | 组合/继承 | 组合 |
| 原接口 | 通常不变 | 通常扩展 |
