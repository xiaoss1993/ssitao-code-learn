# 外观模式 (Facade Pattern)

## 模式定义

外观模式为子系统中的一组接口提供统一的接口，使子系统更容易使用。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│     Client      │       │     Facade      │
│                 │──────▶│                 │
│                 │       │  - subsystem    │
└─────────────────┘       └────────┬────────┘
                                   │
                    ┌──────────────┼──────────────┐
                    │              │              │
                    ▼              ▼              ▼
           ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
           │ Subsystem1 │ │ Subsystem2 │ │ Subsystem3 │
           │             │ │             │ │             │
           └─────────────┘ └─────────────┘ └─────────────┘
```

## 核心思想

```
外观模式 = 统一入口 + 简化调用

子系统（复杂）
    ├── SubsystemA.methodA()
    ├── SubsystemA.methodB()
    ├── SubsystemB.methodC()
    └── SubsystemB.methodD()

外观（简单）
    └── Facade.method() {
            SubsystemA.methodA()
            SubsystemB.methodC()
        }
```

---

## 示例代码

### 基础示例：矿场

```java
// 子系统：矿工
class DwarvenGoldDigger {
    public void mine() {
        System.out.println("矿工挖掘金矿...");
    }
}

// 子系统：矿道挖掘
class DwarvenTunnelDigger {
    public void dig() {
        System.out.println("挖掘矿道...");
    }
}

// 子系统：矿车操作
class DwarvenCartOperator {
    public void operateCart() {
        System.out.println("操作矿车...");
    }
}

// 外观：统一入口
class DwarvenGoldmineFacade {
    private DwarvenGoldDigger goldDigger = new DwarvenGoldDigger();
    private DwarvenTunnelDigger tunnelDigger = new DwarvenTunnelDigger();
    private DwarvenCartOperator cartOperator = new DwarvenCartOperator();

    public void extractGold() {
        goldDigger.mine();
        tunnelDigger.dig();
        cartOperator.operateCart();
    }
}

// 客户端：只需要调用外观
public class App {
    public static void main(String[] args) {
        DwarvenGoldmineFacade facade = new DwarvenGoldmineFacade();
        facade.extractGold();  // 简化调用
    }
}
```

---

## JDK中的应用

### 1. Class.newInstance()

```java
// 隐藏反射复杂性
List<String> list = ArrayList.class.newInstance();
```

### 2. Charset编码/解码

```java
Charset charset = StandardCharsets.UTF_8;
ByteBuffer buffer = charset.encode("Hello");
String decoded = charset.decode(buffer).toString();
```

### 3. DriverManager.getConnection()

```java
// 统一管理数据库连接
Connection conn = DriverManager.getConnection(url, user, password);
```

### 4. Files工具类

```java
// 简化文件操作
List<String> lines = Files.readAllLines(path);
Files.write(path, lines);
```

---

## Spring框架中的应用

### 1. ApplicationContext

```java
// 统一管理所有Bean
ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
MyService service = context.getBean(MyService.class);
```

### 2. WebMvcConfigurer

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("*");
    }
}
```

### 3. TransactionSynchronizationManager

```java
// 统一事务管理
TransactionSynchronizationManager.bindResource(dataSource, connection);
```

---

## MyBatis中的应用

### 1. SqlSessionFactory

```java
// 统一管理SqlSession创建
SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
SqlSession session = factory.openSession();
```

### 2. Configuration

```java
// 统一配置管理
Configuration config = new Configuration();
config.addMapper(UserMapper.class);
```

### 3. TransactionFactory

```java
// 统一事务管理
TransactionFactory factory = new JdbcTransactionFactory();
Transaction tx = factory.newTransaction();
```

---

## 适用场景

1. **简化复杂系统**：为复杂子系统提供简单接口
2. **层次化设计**：每层只依赖外观，不直接依赖子系统
3. **遗留系统包装**：为旧系统提供新的统一接口
4. **第三方库集成**：封装第三方库复杂性

## 优点

- **降低耦合**：客户端与子系统解耦
- **简化调用**：提供统一的简单接口
- **提高复用**：子系统可在多处被外观包装使用
- **易于迁移**：可以逐步替换子系统而不影响客户端

## 缺点

- 外观可能成为上帝类（承担过多职责）
- 可能引入抽象层增加复杂度

## 对比其他模式

| 模式 | 目的 | 关系 |
|------|------|------|
| 外观模式 | 简化接口 | 提供统一入口 |
| 适配器模式 | 接口转换 | 转换不兼容接口 |
| 装饰器模式 | 功能增强 | 动态添加功能 |
| 代理模式 | 间接访问 | 控制直接访问 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/facade
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.facade.App"
```

### 运行JDK示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.facade.jdk.JdkFacadeDemo"
```

### 运行Spring示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.facade.spring.SpringFacadeDemo"
```

### 运行MyBatis示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.facade.mybatis.MyBatisFacadeDemo"
```
