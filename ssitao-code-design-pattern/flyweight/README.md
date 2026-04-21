# 享元模式 (Flyweight Pattern)

## 模式定义

享元模式运用共享技术有效地支持大量细粒度的对象。

## UML结构

```
┌─────────────────────────┐
│      FlyweightFactory   │
├─────────────────────────┤
│ - flyweights: Map       │
│ + getFlyweight(key)     │
└───────────┬─────────────┘
            │ creates
            ▼
┌─────────────────────────┐
│      Flyweight          │
│   <<interface>>         │
├─────────────────────────┤
│ + operation(extState)   │
└───────────┬─────────────┘
            △
            │
┌───────────┴─────────────┐
│   ConcreteFlyweight     │
├─────────────────────────┤
│ - intrinsicState        │
│ + operation(extState)   │
└─────────────────────────┘

Intrinsic State: 存储在Flyweight内部，可共享
Extrinsic State: 由客户端传入，不可共享
```

## 核心思想

```
享元模式 = FlyweightFactory + Flyweight + Intrinsic/Extrinsic State

关键点：
1. Flyweight是享元接口
2. ConcreteFlyweight是具体享元，存储内在状态
3. FlyweightFactory管理享元池
4. 客户端传入外在状态

问题：大量相似对象占用大量内存
解决：
    共享内在状态
    外在状态由客户端传入
```

---

## 示例代码

### 基础示例：炼金术商店

```java
// 享元接口
public interface Potion {
    void drink();
}

// 具体享元
public class HealingPotion implements Potion {
    @Override
    public void drink() {
        System.out.println("饮用治疗药水 (内在状态: HEALING)");
    }
}

public class HolyWaterPotion implements Potion {
    @Override
    public void drink() {
        System.out.println("饮用圣水 (内在状态: HOLY_WATER)");
    }
}

public class InvisibilityPotion implements Potion {
    @Override
    public void drink() {
        System.out.println("饮用隐身药水 (内在状态: INVISIBILITY)");
    }
}

// 享元工厂
public class PotionFactory {
    private final Map<PotionType, Potion> potions;

    public PotionFactory() {
        potions = new EnumMap<>(PotionType.class);
    }

    Potion createPotion(PotionType type) {
        Potion potion = potions.get(type);
        if (potion == null) {
            switch (type) {
                case HEALING:
                    potion = new HealingPotion();
                    potions.put(type, potion);
                    break;
                case HOLY_WATER:
                    potion = new HolyWaterPotion();
                    potions.put(type, potion);
                    break;
                case INVISIBILITY:
                    potion = new InvisibilityPotion();
                    potions.put(type, potion);
                    break;
                // ...
            }
        }
        return potion;
    }
}

// 客户端
public class AlchemistShop {
    private List<Potion> shelf = new ArrayList<>();

    public void enumerate() {
        PotionFactory factory = new PotionFactory();
        shelf.add(factory.createPotion(PotionType.HEALING));
        shelf.add(factory.createPotion(PotionType.HOLY_WATER));
        shelf.add(factory.createPotion(PotionType.HEALING));  // 复用
    }
}
```

---

## JDK中的应用

### 1. String Pool

```java
// String字符串池使用享元模式
String s1 = "Hello";
String s2 = "Hello";
System.out.println(s1 == s2);  // true，共享同一个对象

// String.intern()
String s3 = new String("World");
String s4 = s3.intern();
System.out.println(s3 == s4);  // false -> true（intern后）
```

### 2. Integer Cache

```java
// Integer使用享元模式
Integer i1 = 127;  // 享元池中的对象
Integer i2 = 127;
System.out.println(i1 == i2);  // true

// Integer.valueOf()使用享元池
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high) {
        return IntegerCache.cache[i + (-IntegerCache.low)];
    }
    return new Integer(i);  // 超出范围创建新对象
}
```

### 3. Character.UnicodeBlock

```java
// Character使用享元模式管理字符块
public static UnicodeBlock of(char c) {
    // 内部使用缓存机制
}
```

---

## Spring框架中的应用

### 1. StringCache

```java
// Spring内部也使用享元模式优化字符串
// Spring的某些组件会缓存重复的字符串
```

### 2. CGLIB ClassInfo

```java
// Spring使用CGLIB动态代理时会缓存类信息
public class CglibClassUtils {
    private static Map<Class<?>, Class<?>> classCache = new ConcurrentHashMap<>();

    public static Class<?> getEnhancedClass(Class<?> target) {
        return classCache.computeIfAbsent(target, CglibAopProxy::createEnhancedClass);
    }
}
```

---

## MyBatis中的应用

### 1. Pools (各种池化技术)

```java
// MyBatis使用多种享元池化技术

// 1. TypeHandler缓存
public class TypeHandlerRegistry {
    private final Map<JdbcType, Map<JavaType, TypeHandler<?>>> typeHandlerMap;
    private final Map<Class<?>, TypeHandler<?>> javaTypeHandlerMap;
    private final Map<TypeHandlerRegistry, TypeHandler<?>> allTypeHandlersCache;

    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        // 从缓存获取或创建新的TypeHandler
        return javaTypeHandlerMap.get(type);
    }
}

// 2. MappedStatement缓存
public class Configuration {
    protected final Map<String, MappedStatement> mappedStatements;

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }
}

// 3. ResultMap缓存
public class Configuration {
    protected final Map<String, ResultMap> resultMaps;
}
```

### 2. SqlSessionFactory缓存

```java
// MyBatis缓存SqlSessionFactory
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private final Configuration configuration;

    // Configuration被缓存复用
}
```

---

## 适用场景

1. **大量相似对象**：需要大量对象
2. **对象可共享**：对象状态可以外部化
3. **内存优化**：需要节省内存
4. **字符串池化**：字符串常量池
5. **连接池**：数据库连接池

## 优点

- **节省内存**：大量相似对象共享内在状态
- **提高性能**：减少对象创建开销
- **提高一致性**：共享对象状态一致

## 缺点

- **复杂性增加**：需要区分内在和外在状态
- **线程安全**：共享对象需要考虑线程安全
- **维护困难**：状态分离可能增加维护难度

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 享元模式 | 共享细粒度对象 |
| 单例模式 | 全局单一实例 |
| 组合模式 | 整体-部分树形结构 |
| 装饰器模式 | 动态增加功能 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/flyweight
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.flyweight.App"
```

### 运行JDK享元示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.flyweight.jdk.JdkFlyweightDemo"
```

### 运行Spring享元示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.flyweight.spring.SpringFlyweightDemo"
```

### 运行MyBatis享元示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.flyweight.mybatis.MyBatisFlyweightDemo"
```
