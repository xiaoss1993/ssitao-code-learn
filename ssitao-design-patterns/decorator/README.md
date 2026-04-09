# 装饰器模式 (Decorator Pattern)

## 模式定义

装饰器模式动态地给对象添加一些额外的职责，就增加功能来说，装饰器模式比继承更加灵活。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│    Component    │       │    Decorator    │
│  <<interface>>  │       │   <<abstract>>  │
├─────────────────┤       ├─────────────────┤
│ + operation()   │       │ - component     │
└─────────────────┘       │ + operation()   │
        △                 └────────┬────────┘
        │                          │
        │                  ┌────────┴────────┐
        │                  │                 │
┌───────┴───────┐   ┌──────┴──────┐  ┌──────┴──────┐
│ConcreteCompA  │   │DecoratorA   │  │DecoratorB   │
│               │   │+ addedBehavior()│           │
└───────────────┘   └─────────────┘  └─────────────┘
```

## 核心思想

```
装饰器模式 = 接口 + 装饰器基类 + 具体装饰器

问题：需要在运行时给对象增加功能，但继承会导致类爆炸
解决：
    Component (接口)
        └── ConcreteComponent (基本组件)
    Decorator (装饰器基类)
        ├── DecoratorA (增加功能A)
        └── DecoratorB (增加功能B)

    组合：decoratorA(decoratorB(concreteComponent))
    运行时动态组装功能
```

---

## 示例代码

### 基础示例：咖啡

```java
// 组件接口
public interface Coffee {
    int getCost();
    String getDescription();
}

// 具体组件
public class SimpleCoffee implements Coffee {
    @Override
    public int getCost() { return 10; }
    @Override
    public String getDescription() { return "Simple Coffee"; }
}

// 装饰器基类
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    public CoffeeDecorator(Coffee coffee) { this.coffee = coffee; }
}

// 具体装饰器：牛奶
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }
    @Override
    public int getCost() { return coffee.getCost() + 5; }
    @Override
    public String getDescription() { return coffee.getDescription() + ", Milk"; }
}

// 具体装饰器：糖
public class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) { super(coffee); }
    @Override
    public int getCost() { return coffee.getCost() + 2; }
    @Override
    public String getDescription() { return coffee.getDescription() + ", Sugar"; }
}

// 使用
Coffee coffee = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
System.out.println(coffee.getDescription()); // Simple Coffee, Milk, Sugar
System.out.println(coffee.getCost()); // 17
```

---

## JDK中的应用

### 1. IO Stream (最重要)

```java
// Component: InputStream
// ConcreteComponent: FileInputStream, ByteArrayInputStream
// Decorator: FilterInputStream
// ConcreteDecorator: BufferedInputStream, DataInputStream, GZIPInputStream

// 基础组件
InputStream fis = new FileInputStream("data.txt");

// 装饰器：缓冲
InputStream bis = new BufferedInputStream(fis);

// 装饰器：数据输入
InputStream dis = new DataInputStream(bis);

// 装饰器：压缩
InputStream gis = new GZIPInputStream(dis);

// 组合使用
DataInputStream dis = new DataInputStream(
    new BufferedInputStream(
        new FileInputStream("data.gz")));
```

### 2. Reader/Writer

```java
// Component: Reader
// ConcreteComponent: FileReader, StringReader
// Decorator: FilterReader
// ConcreteDecorator: BufferedReader, LineNumberReader

// 带缓冲的字符流
BufferedReader reader = new BufferedReader(new FileReader("data.txt"));
String line = reader.readLine();

// 带行号
LineNumberReader lnr = new LineNumberReader(new FileReader("data.txt"));

// 装饰器组合
BufferedReader br = new BufferedReader(
    new FileReader("data.txt"));
```

### 3. Collections

```java
// Collections提供静态装饰器方法

// 同步装饰器
List<E> syncedList = Collections.synchronizedList(new ArrayList<E>());
Map<K, V> syncedMap = Collections.synchronizedMap(new HashMap<>());

// 只读装饰器
List<E> unmodifiableList = Collections.unmodifiableList(new ArrayList<E>());
Map<K, V> unmodifiableMap = Collections.unmodifiableMap(new HashMap<>());

// 检查类型装饰器
List<String> checkedList = Collections.checkedList(new ArrayList<>(), String.class);
```

---

## Spring框架中的应用

### 1. BufferedReaderFactory

```java
// Spring使用装饰器模式处理IO

// Resource是Component
public interface Resource extends InputStreamSource {
    boolean exists();
    boolean isReadable();
    boolean isOpen();
    URL getURL() throws IOException;
}

// InputStreamResource是具体组件
public class ByteArrayResource implements Resource {
    private final byte[] byteArray;
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(byteArray);
    }
}

// EncodedResource是装饰器 - 添加字符编码支持
public class EncodedResource implements Resource {
    private final Resource resource;
    private final Charset charset;

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream stream = resource.getInputStream();
        return new ReaderInputStream(
            new InputStreamReader(stream, charset), charset);
    }
}

// 使用
Resource resource = new EncodedResource(
    new ClassPathResource("config.xml"), StandardCharsets.UTF_8);
```

### 2. WebRequest装饰器

```java
// Spring使用装饰器模式包装请求

// WebRequest是Component
public interface WebRequest {
    String getParameter(String name);
    String[] getParameterValues(String name);
}

// ServletWebRequest是具体组件
public class ServletWebRequest implements WebRequest {
    private HttpServletRequest request;
    // ...
}

// ParameterEncodingRequestDecorator是装饰器
public class ParameterEncodingRequestDecorator extends WebRequestDecorator {
    private Charset encoding;

    @Override
    public String getParameter(String name) {
        String value = getRequest().getParameter(name);
        return convertEncoding(value);
    }
}
```

---

## MyBatis中的应用

### 1. Cache缓存装饰器

```java
// MyBatis使用装饰器模式实现多级缓存

// Cache是Component
public interface Cache {
    String getId();
    void putObject(Object key, Object value);
    Object getObject(Object key);
    Object removeObject(Object key);
    void clear();
    int getSize();
}

// PerpetualCache是具体组件（基础缓存）
public class PerpetualCache implements Cache {
    private Map<Object, Object> cache = new HashMap<>();
    // ...
}

// 装饰器：缓存大小限制
public class ScheduledCache implements Cache {
    private final Cache delegate;
    private long clearInterval;
    private long lastClear;

    @Override
    public void putObject(Object key, Object value) {
        delegate.putObject(key, value);
        if (shouldClear()) { delegate.clear(); }
    }
}

// 装饰器：同步
public class SynchronizedCache implements Cache {
    private final Cache delegate;

    @Override
    public void putObject(Object key, Object value) {
        synchronized (this) { delegate.putObject(key, value); }
    }
}

// 装饰器：LRU策略
public class LruCache implements Cache {
    private final Cache delegate;
    private Map<Object, Object> keyMap;

    @Override
    public void putObject(Object key, Object value) {
        if (keyMap.size() >= maxSize) {
            Object oldestKey = keyMap.keySet().iterator().next();
            delegate.removeObject(oldestKey);
        }
        keyMap.put(key, key);
        delegate.putObject(key, value);
    }
}

// 装饰器：日志
public class LoggingCache implements Cache {
    private final Cache delegate;

    @Override
    public Object getObject(Object key) {
        Object value = delegate.getObject(key);
        if (value != null) { log.getObject(key); }
        return value;
    }
}

// 使用示例 - 装饰器组合
Cache cache = new LoggingCache(
    new SynchronizedCache(
        new LruCache(
            new ScheduledCache(
                new PerpetualCache("id")))));
```

### 2. ResultSetWrapper

```java
// MyBatis使用装饰器模式包装ResultSet

// ResultSet是JDBC的Component
// ResultSetWrapper是装饰器，添加列名映射等功能

public class ResultSetWrapper {
    private final ResultSet resultSet;
    private final List<String> columnNames;
    private final List<Class<?>> columnTypes;

    public String getColumnName(int i) {
        return columnNames.get(i - 1);
    }

    public Class<?> getColumnClass(int i) {
        return columnTypes.get(i - 1);
    }
}
```

---

## 适用场景

1. **动态增加功能**：运行时给对象添加职责
2. **避免类爆炸**：不用继承就能扩展功能
3. **功能组合**：可以组合多个装饰器
4. **IO流处理**：Java IO库大量使用装饰器

## 优点

- **灵活扩展**：比继承更灵活
- **单一职责**：每个装饰器只关注一个功能
- **可叠加使用**：多个装饰器可以组合
- **开闭原则**：对扩展开放，对修改关闭

## 缺点

- **复杂度增加**：多层装饰可能导致复杂性
- **调试困难**：装饰器层层嵌套，调试困难
- **顺序敏感**：装饰器顺序很重要

## 对比代理模式

| 特性 | 装饰器模式 | 代理模式 |
|------|-----------|----------|
| 目的 | 功能增强 | 控制访问 |
| 关系 | 组合 | 组合 |
| 接口 | 通常相同 | 通常相同 |
| 创建 | 运行时组装 | 静态或动态 |

## 对比适配器模式

| 特性 | 装饰器模式 | 适配器模式 |
|------|-----------|------------|
| 目的 | 功能增强 | 接口转换 |
| 关系 | 组合 | 组合/继承 |
| 原接口 | 保持不变 | 转换为新接口 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/decorator
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.decorator.App"
```

### 运行咖啡示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.decorator.CoffeeDemo"
```

### 运行数据源装饰器示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.decorator.DataSourceDemo"
```

### 运行JDK IO流示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.decorator.InputStreamExample"
```
