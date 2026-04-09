# 单例模式 (Singleton Pattern)

## 模式定义

单例模式确保一个类只有一个实例，并提供一个全局访问点。

## UML结构

```
┌─────────────────┐
│   Singleton     │
├─────────────────┤
│ - instance      │
├─────────────────┤
│ - Singleton()   │  (private)
│ + getInstance() │
└─────────────────┘
```

## 核心思想

```
单例模式 = 私有构造函数 + 静态实例 + 静态获取方法

关键点：
1. 私有构造函数 - 防止外部new
2. 静态实例 - 类级变量
3. 静态获取方法 - 全局访问点

常见实现方式：
1. 饿汉式 - 类加载时创建
2. 懒汉式 - 延迟加载
3. 双重检查锁 - 线程安全且高效
4. 静态内部类 - JVM保证线程安全
5. 枚举 - 最安全
```

---

## 实现方式对比

```
┌───────────────────┬──────────┬────────────┬─────────────────────────┐
│ 方式              │ 线程安全  │ 延迟加载   │ 特点                     │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 饿汉式            │ 是       │ 否        │ 简单，类加载时创建        │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 懒汉式            │ 否       │ 是        │ 不安全                   │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 同步方法          │ 是       │ 是        │ 简单，但性能差           │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 双重检查锁         │ 是       │ 是        │ 推荐，性能好             │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 静态内部类         │ 是       │ 是        │ 推荐，JVM保证            │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 枚举              │ 是       │ 否        │ 最安全，防止反射/序列化   │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ ThreadLocal       │ 是       │ 是        │ 线程内唯一               │
├───────────────────┼──────────┼────────────┼─────────────────────────┤
│ 容器              │ 是       │ 是        │ 统一管理多个单例          │
└───────────────────┴──────────┴────────────┴─────────────────────────┘
```

---

## 示例代码

### 1. 饿汉式

```java
public class HungrySingleton {
    // 类加载时就创建实例
    private static final HungrySingleton INSTANCE = new HungrySingleton();

    private HungrySingleton() {}

    public static HungrySingleton getInstance() {
        return INSTANCE;
    }
}
```

### 2. 懒汉式（非线程安全）

```java
public class LazySingleton {
    private static LazySingleton instance;

    private LazySingleton() {}

    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}
```

### 3. 同步方法（线程安全但性能差）

```java
public class SynchronizedLazySingleton {
    private static SynchronizedLazySingleton instance;

    private SynchronizedLazySingleton() {}

    public synchronized static SynchronizedLazySingleton getInstance() {
        if (instance == null) {
            instance = new SynchronizedLazySingleton();
        }
        return instance;
    }
}
```

### 4. 双重检查锁（推荐）

```java
public class DoubleCheckLockingSingleton {
    // volatile防止指令重排
    private static volatile DoubleCheckLockingSingleton instance;

    private DoubleCheckLockingSingleton() {}

    public static DoubleCheckLockingSingleton getInstance() {
        if (instance == null) {  // 第一次检查
            synchronized (DoubleCheckLockingSingleton.class) {
                if (instance == null) {  // 第二次检查
                    instance = new DoubleCheckLockingSingleton();
                }
            }
        }
        return instance;
    }
}
```

### 5. 静态内部类（推荐）

```java
public class StaticInnerClassSingleton {
    private StaticInnerClassSingleton() {}

    // 静态内部类，JVM保证线程安全
    private static class SingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE =
            new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

### 6. 枚举（最安全，推荐用于重要实例）

```java
public enum EnumSingleton {
    INSTANCE;

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }
}
```

---

## JDK中的应用

### 1. Runtime

```java
// Runtime是典型的单例模式
public class Runtime {
    private static Runtime currentRuntime;

    public static Runtime getRuntime() {
        return currentRuntime;
    }

    private Runtime() {
        currentRuntime = this;
    }

    // 典型用法
    // Runtime runtime = Runtime.getRuntime();
    // runtime.gc();
    // long mem = runtime.freeMemory();
}
```

### 2. Desktop

```java
// Desktop类使用单例模式
public class Desktop {
    // Desktop是抽象，不是单例
    // 但通常通过Desktop.getDesktop()获取实例
    public static Desktop getDesktop() {
        // 内部实现可能是单例
        return new Desktop();
    }
}
```

### 3. Logger

```java
// 日志框架通常使用单例或工厂模式
// java.util.logging.Logger使用LogManager管理
public class LogManager {
    private static final LogManager manager;

    public static LogManager getLogManager() {
        return manager;
    }

    private LogManager() {
        // 初始化
    }
}
```

---

## Spring框架中的应用

### 1. Spring Bean Scope

```java
// Spring默认使用单例模式
// 整个IOC容器只有一个Bean实例

@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }
    // 默认scope="singleton"
}

// 获取单例
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
UserService s1 = context.getBean(UserService.class);
UserService s2 = context.getBean(UserService.class);
System.out.println(s1 == s2);  // true
```

### 2. Spring单例 vs 单例模式

```
Spring单例与经典单例模式的区别：

经典单例模式：
  - 一个类全局只有一个实例
  - 通过私有构造函数实现

Spring单例：
  - 一个容器一个实例
  - 不同容器可以有多个实例
  - 由BeanFactory/ApplicationContext管理

Spring单例的优势：
  - 无需手动实现单例
  - 可配置
  - 可管理生命周期
```

### 3. Spring中的单例管理

```java
// DefaultSingletonBeanRegistry提供单例管理
public class DefaultSingletonBeanRegistry {
    // 存储单例对象的Map
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 获取单例
    protected Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    // 注册单例
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
        }
    }
}
```

### 4. 其他Spring Bean Scope

```java
// Spring支持多种Scope

// 单例 - 整个容器一个实例
@Scope("singleton")

// 原型 - 每次获取创建新实例
@Scope("prototype")

// 请求 - 每次HTTP请求一个实例
@Scope("request")

// 会话 - 每次HTTP会话一个实例
@Scope("session")
```

---

## MyBatis中的应用

### 1. SqlSessionFactory

```java
// SqlSessionFactory通常作为单例
// 整个应用只有一个SqlSessionFactory实例

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private static SqlSessionFactory instance;

    public static SqlSessionFactory build(String configPath) {
        if (instance == null) {
            synchronized (SqlSessionFactory.class) {
                if (instance == null) {
                    instance = new DefaultSqlSessionFactory(configPath);
                }
            }
        }
        return instance;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}

// 使用
SqlSessionFactory factory = SqlSessionFactory.build("mybatis-config.xml");
SqlSession session1 = factory.openSession();
SqlSession session2 = factory.openSession();
```

### 2. Configuration

```java
// MyBatis的Configuration通常是单例
// 整个应用共享一个Configuration

public class Configuration {
    // 类型注册器
    private final TypeHandlerRegistry typeHandlerRegistry;

    // 结果映射
    private final Map<String, ResultMap> resultMaps;

    // SQL语句映射
    private final Map<String, MappedStatement> mappedStatements;

    // 单例实例
    private static Configuration instance;

    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
}
```

### 3. LogFactory

```java
// LogFactory管理日志实现，也是单例
public final class LogFactory {
    // 使用的日志实现
    private static Log log;

    public static synchronized void useLogFramework(Class<? extends Log> logClass) {
        this.log = logClass.newInstance();
    }

    public static Log getLog(Class<?> clazz) {
        return log;
    }
}
```

---

## 适用场景

1. **全局唯一对象**：日志对象、配置对象
2. **资源共享**：线程池、数据库连接池
3. **重量级对象**：避免重复创建开销

## 优点

- **控制实例数量**：防止大量创建
- **节省资源**：避免重复创建开销
- **全局访问**：提供统一访问点

## 缺点

- **职责过重**：可能违背单一职责原则
- **隐藏依赖**：通过全局访问可能隐藏依赖
- **线程安全问题**：需要考虑线程安全
- **难以测试**：难以mock单例

## 推荐使用

1. **双重检查锁** - 通用场景
2. **静态内部类** - 无参数需求
3. **枚举** - 需要防止反射/序列化攻击

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/singleton
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.singleton.SingletonDemo"
```

### 运行线程安全示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.singleton.ThreadSafeDoubleCheckLocking"
```

### 运行Spring单例示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.singleton.SpringSingletonExample"
```

### 查看单例模式总结

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.singleton.SingletonSummary"
```
