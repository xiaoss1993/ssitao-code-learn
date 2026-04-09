# 桥接模式 (Bridge Pattern)

## 模式定义

桥接模式将抽象部分与它的实现部分分离，使它们都可以独立地变化。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│  Abstraction    │──────▶│ Implementor    │
│                 │       │  <<interface>>  │
│  + operation()  │       └────────┬────────┘
└─────────────────┘                │
                           ┌────────┴────────┐
                           │                 │
                           ▼                 ▼
                  ┌─────────────┐   ┌─────────────┐
                  │ConcreteImplA │   │ConcreteImplB│
                  └─────────────┘   └─────────────┘

桥接模式关键：
- Abstraction (抽象部分) 持有 Implementor 的引用
- Abstraction 和 Implementor 都可以独立扩展
- 两者通过组合关系连接，而非继承
```

## 核心思想

```
桥接模式 = 抽象部分 + 实现部分 + 组合关系

问题：多种形状 × 多种绘制方式 = 类的组合爆炸
解决：
    Shape (抽象)
        ├── Circle
        └── Square
    DrawAPI (实现)
        ├── RedDraw
        └── BlueDraw

    组合：Circle + RedDraw, Circle + BlueDraw, Square + RedDraw...
    而不是：RedCircle, BlueCircle, RedSquare, BlueSquare...
```

---

## 示例代码

### 基础示例：魔法武器

```java
// 实现部分接口
public abstract class MagicWeaponImpl {
    public abstract void wieldImp();
    public abstract void swingImp();
    public abstract void unwieldImp();
}

// 具体实现：Excalibur
public class Excalibur extends MagicWeaponImpl {
    @Override
    public void wieldImp() { System.out.println("wielding Excalibur"); }
    @Override
    public void swingImp() { System.out.println("swinging Excalibur"); }
    @Override
    public void unwieldImp() { System.out.println("unwielding Excalibur"); }
}

// 具体实现：Stormbringer
public class Stormbringer extends SoulEatingMagicWeaponImpl {
    @Override
    public void eatSoulImp() { System.out.println("Stormbringer devours the enemy's soul"); }
}

// 抽象部分
public abstract class MagicWeapon {
    protected MagicWeaponImpl imp;
    public MagicWeapon(MagicWeaponImpl imp) { this.imp = imp; }
    public abstract void wield();
    public abstract void swing();
    public abstract void unwield();
}

// 扩展抽象：飞行武器
public class FlyingMagicWeapon extends MagicWeapon {
    public FlyingMagicWeapon(FlyingMagicWeaponImpl imp) { super(imp); }
    public void fly() { getImp().flyImp(); }
}

// 扩展抽象：致盲武器
public class BlindingMagicWeapon extends MagicWeapon {
    public BlindingMagicWeapon(BlindingMagicWeaponImpl imp) { super(imp); }
    public void blind() { getImp().blindImp(); }
}

// 使用
public class App {
    public static void main(String[] args) {
        FlyingMagicWeapon flyingMagicWeapon = new FlyingMagicWeapon(new Mjollnir());
        flyingMagicWeapon.wield();
        flyingMagicWeapon.fly();
        flyingMagicWeapon.swing();
        flyingMagicWeapon.unwield();
    }
}
```

---

## JDK中的应用

### 1. AWT Peer Architecture

```java
// Java AWT使用桥接模式分离平台无关的Component和平台相关的Peer
// Component (抽象) <--------> Peer (实现)

// Component抽象了UI组件
public abstract class Component {
    // 内部持有Peer引用，但不直接调用
    // 而是通过ComponentPeer接口调用平台相关实现
}

// ComponentPeer是实现接口
public interface ComponentPeer {
    void paint(Graphics g);
    void setBounds(int x, int y, int w, int h);
}

// Windows实现的Peer
class WComponentPeer implements ComponentPeer {
    // Windows平台特定实现
}

// Unix实现的Peer
class XComponentPeer implements ComponentPeer {
    // Unix平台特定实现
}
```

### 2. JDBC Driver Architecture

```java
// JDBC使用桥接模式分离DriverManager和具体驱动
// DriverManager (抽象) <--------> Driver (实现)

// java.sql.Driver是实现接口
public interface Driver {
    Connection connect(String url, Properties info);
    boolean acceptsURL(String url);
}

// MySQL驱动实现
public class MySQLDriver implements Driver {
    @Override
    public Connection connect(String url, Properties info) {
        // MySQL协议实现
        return new MySQLConnection();
    }
}

// Oracle驱动实现
public class OracleDriver implements Driver {
    @Override
    public Connection connect(String url, Properties info) {
        // Oracle协议实现
        return new OracleConnection();
    }
}

// DriverManager持有Driver的引用
public class DriverManager {
    // 通过DriverManager获取Connection，具体Driver可以动态替换
    public static Connection getConnection(String url) {
        Driver driver = getDriver(url);  // 根据URL选择Driver
        return driver.connect(url, new Properties());
    }
}
```

### 3. NIO Channel Architecture

```java
// NIO使用桥接模式分离Channel和具体实现
// Channel (抽象) <--------> ChannelImpl (实现)

// Channel是抽象部分
public interface Channel {
    boolean isOpen();
    void close();
}

// ReadableByteChannel是抽象
public interface ReadableByteChannel extends Channel {
    int read(ByteBuffer dst) throws IOException;
}

// 具体实现：SocketChannel
public abstract class SocketChannel implements ReadableByteChannel {
    // 内部持有SelChanel或原生Socket的引用
    private final SelChannelImpl impl;  // 真正的实现
}

// FileChannel
public abstract class FileChannel implements ByteChannel {
    // 持有原生文件描述符的实现
}
```

---

## Spring框架中的应用

### 1. DataSource Transaction

```java
// Spring使用桥接模式分离PlatformTransactionManager和具体事务实现
// TransactionManager (抽象) <--------> Transaction (实现)

// Transaction是实现接口
public interface Transaction {
    void commit();
    void rollback();
    Object getResource();
}

// PlatformTransactionManager是抽象部分
public abstract class PlatformTransactionManager {
    // 持有Transaction的引用
    protected abstract Transaction doGetTransaction();
    protected abstract void doCommit(Transaction status);
    protected abstract void doRollback(Transaction status);
}

// DataSourceTransactionManager - 使用JDBC事务实现
public class DataSourceTransactionManager extends PlatformTransactionManager {
    @Override
    protected Transaction doGetTransaction() {
        return new DataSourceTransactionObject();
    }
}

// JpaTransactionManager - 使用JPA事务实现
public class JpaTransactionManager extends PlatformTransactionManager {
    @Override
    protected Transaction doGetTransaction() {
        return new JpaTransactionObject();
    }
}

// HibernateTransactionManager - 使用Hibernate事务实现
public class HibernateTransactionManager extends PlatformTransactionManager {
    @Override
    protected Transaction doGetTransaction() {
        return new HibernateTransactionObject();
    }
}
```

### 2. ViewResolver

```java
// Spring MVC使用桥接模式分离View和具体解析实现
// View (抽象) <--------> ViewResolver (实现)

// View是抽象部分
public interface View {
    void render(Map<String, ?> model, HttpServletRequest request,
                HttpServletResponse response) throws Exception;
}

// InternalResourceView是具体抽象
public class InternalResourceView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        // 渲染逻辑
    }
}

// ViewResolver是实现接口
public interface ViewResolver {
    View resolveViewName(String viewName, Locale locale) throws Exception;
}

// UrlBasedViewResolver是扩展实现
public class UrlBasedViewResolver extends AbstractCachingViewResolver {
    private Class<?> viewClass;
    private String prefix;
    private String suffix;

    @Override
    protected View createView(String viewName, Locale locale) {
        // 创建具体View
    }
}

// InternalResourceViewResolver扩展UrlBasedViewResolver
public class InternalResourceViewResolver extends UrlBasedViewResolver {
    public InternalResourceViewResolver(String prefix, String suffix) {
        setViewClass(InternalResourceView.class);
        setPrefix(prefix);
        setSuffix(suffix);
    }
}
```

---

## MyBatis中的应用

### 1. Executor

```java
// MyBatis使用桥接模式分离Executor和具体执行实现
// Executor (抽象) <--------> Transaction (实现)

// Transaction是实现接口
public interface Transaction {
    Connection getConnection() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    void close() throws SQLException;
}

// JdbcTransaction是具体实现
public class JdbcTransaction implements Transaction {
    private Connection connection;
    private DataSource dataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }
}

// ManagedTransaction是另一种实现
public class ManagedTransaction implements Transaction {
    private Connection connection;

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }
}

// Executor是抽象部分
public interface Executor {
    <E> List<E> query(MappedStatement ms, Object parameter) throws SQLException;
    int update(MappedStatement ms, Object parameter) throws SQLException;
    Transaction getTransaction();
}

// SimpleExecutor是具体抽象
public class SimpleExecutor implements Executor {
    private Transaction transaction;

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) throws SQLException {
        // 使用transaction执行查询
        Connection conn = transaction.getConnection();
        // ...
        return results;
    }
}

// ReuseExecutor重用预编译语句
public class ReuseExecutor implements Executor {
    private Transaction transaction;
    private Map<String, PreparedStatement> statementMap;

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter) throws SQLException {
        // 重用预编译语句
    }
}
```

### 2. ResultSetHandler

```java
// MyBatis的ResultSetHandler使用桥接模式
// ResultSetHandler (抽象) <--------> ResultSetWrapper (实现)

// TypeHandlerRegistry管理所有TypeHandler
public class TypeHandlerRegistry {
    private Map<JdbcType, TypeHandler<?>> jdbcTypeHandlerMap;
    private Map<Class<?>, TypeHandler<?>> typeHandlerMap;

    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return typeHandlerMap.get(type);
    }
}

// TypeHandler是实现接口
public interface TypeHandler<T> {
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType);
    T getResult(ResultSet rs, String columnName);
}

// ResultSetHandler是抽象部分
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement stmt) throws SQLException;
}

// DefaultResultSetHandler是具体抽象
public class DefaultResultSetHandler implements ResultSetHandler {
    private final Executor executor;
    private final TypeHandlerRegistry typeHandlerRegistry;

    @Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        ResultSetWrapper rsw = new ResultSetWrapper((ResultSet) stmt.getResultSet());
        // 使用TypeHandler处理类型转换
    }
}
```

---

## 适用场景

1. **不希望或不适用于继承的场景**：类的爆炸性增长
2. **抽象和实现需要独立扩展**：两个维度独立变化
3. **需要切换实现而不影响客户端**：运行时替换
4. **跨平台的系统设计**：如JDBC、AWT

## 优点

- **独立扩展**：抽象和实现可以独立扩展
- **开闭原则**：新增抽象和实现不需修改现有代码
- **单一职责**：抽象关注自身，实现关注具体实现
- **减少类数量**：避免类的组合爆炸

## 缺点

- **增加复杂度**：增加了系统的理解难度
- **设计难度**：需要正确识别两个独立变化的维度

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 桥接模式 | 抽象与实现分离，两个层次独立变化 |
| 适配器模式 | 接口转换，让不兼容的接口兼容 |
| 装饰器模式 | 动态增加功能，叠加功能 |
| 策略模式 | 算法替换，委托执行 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/bridge
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.bridge.App"
```

### 运行JDK示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.bridge.jdk.JDKBridgeDemo"
```

### 运行Spring示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.bridge.spring.SpringBridgeDemo"
```

### 运行MyBatis示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.bridge.mybatis.MyBatisBridgeDemo"
```
