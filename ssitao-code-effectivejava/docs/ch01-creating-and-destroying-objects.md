# 第一章：创建和销毁对象

> Effective Java Chapter 1: Creating and Destroying Objects

## 章节概览

本章涵盖 7 个条目，讨论对象的创建、销毁和生命周期管理。

---

## Item 1: Consider static factory methods instead of constructors

### 核心概念

静态工厂方法是有名称的静态方法，返回类的实例。

### 优势

| 优势 | 说明 |
|------|------|
| 有名称 | 可以描述返回对象的特征，如 `BigInteger.probablePrime()` |
| 不必每次创建新对象 | 可以缓存复用，如 `Boolean.valueOf()` |
| 可以返回子类型 | 灵活返回不同实现，如 `Collections.singletonList()` |

### 常见命名模式

```java
// from - 单参数转换
Date d = Date.from(instant);

// of - 多参数聚合
Set<String> set = EnumSet.of("A", "B", "C");

// valueOf - 类似 from
Integer i = Integer.valueOf(42);

// getInstance - 返回单例或新实例
Calendar c = Calendar.getInstance();
```

### 缺点

- 如果类没有 public/protected 构造器，子类无法继承
- 难以被发现，不像构造器在文档中清晰列出

### 示例

```java
// com.ssitao.code.effectivejava.ch01.item01.BooleanCache
Boolean b1 = Boolean.valueOf(true);
Boolean b2 = Boolean.valueOf(true);
System.out.println(b1 == b2);  // true - 复用缓存对象
```

---

## Item 2: Builder pattern when constructor has multiple parameters

### 问题：重叠构造器难以阅读

```java
// 难以阅读，容易出错
NutritionFacts cocaCola = new NutritionFacts(240, 100, 0, 35, 27, 3);
```

### 解决方案：Builder 模式

```java
// 链式调用，清晰易读
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
    .calories(100)
    .fat(0)
    .sodium(35)
    .carbohydrate(27)
    .build();
```

### 适用场景

- 类有多个可选参数（4个以上）
- 希望构建不可变对象
- 需要在构建时验证参数

### 示例

```java
// com.ssitao.code.effectivejava.ch01.item02.NutritionFacts
NutritionFacts orangeJuice = new Builder(240, 8)
    .calories(110)
    .fat(0)
    .carbohydrate(26)
    .build();
```

---

## Item 3: Enforce singleton with enum type

### 三种实现方式

#### 1. 公有静态 final 字段

```java
public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() {}
}
```

#### 2. 公有静态工厂方法

```java
public class Elvis {
    private static final Elvis INSTANCE = new Elvis();
    public static Elvis getInstance() { return INSTANCE; }
}
```

#### 3. 枚举单例（推荐）

```java
public enum Elvis {
    INSTANCE;
}
```

### 为什么枚举是最佳单例？

- 反射无法破坏
- 序列化安全（自动提供 readResolve）
- 简洁

### 示例

```java
// com.ssitao.code.effectivejava.ch01.item03.Elvis
Elvis elvis1 = Elvis.INSTANCE;
Elvis elvis2 = Elvis.INSTANCE;
System.out.println(elvis1 == elvis2);  // true
```

---

## Item 4: Noninstantiation with private constructor

### 场景

工具类（如 `Arrays`、`Math`）只包含静态方法，不应该被实例化。

### 错误做法

```java
// 抽象类可以被子类化，不推荐
public abstract class StringUtils {
    public static boolean isBlank(String s) { ... }
}
```

### 正确做法

```java
public class StringUtils {
    private StringUtils() {
        throw new AssertionError();
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
```

---

## Item 5: Prefer dependency injection

### 问题：硬编码依赖

```java
// 紧耦合，难以测试
public class SpellChecker {
    private final Dictionary dictionary = new EnglishDictionary();
}
```

### 解决方案：依赖注入

```java
// 通过构造器注入依赖
public class SpellChecker {
    private final Dictionary dictionary;

    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
```

### 好处

- 可测试性：可以注入 mock 对象
- 灵活性：可以注入不同实现
- 清晰性：依赖关系明确

### 示例

```java
// com.ssitao.code.effectivejava.ch01.item05.SpellChecker
SpellChecker englishChecker = new SpellChecker(new EnglishDictionary());
SpellChecker frenchChecker = new SpellChecker(new FrenchDictionary());
// 同一份代码，不同行为
```

---

## Item 6: Avoid creating unnecessary objects

### 1. 优先使用基本类型而不是装箱类型

```java
// 错误：每次 sum += i 都会创建新的 Long 对象
Long sum = 0L;
for (long i = 0; i < 1_000_000; i++) {
    sum += i;
}

// 正确：使用基本类型
long sum = 0L;
```

### 2. 字符串池复用

```java
// 错误：永远不要这样做
String s1 = new String("hello");

// 正确：从字符串池获取
String s2 = "hello";
```

### 3. Pattern 缓存

```java
// 错误：每次调用都编译正则表达式
boolean isValid = input.matches("\\d+");

// 正确：静态编译 Pattern 并复用
private static final Pattern DIGITS = Pattern.compile("\\d+");
boolean isValid = DIGITS.matcher(input).matches();
```

### 示例

```java
// com.ssitao.code.effectivejava.ch01.item06.AvoidUnnecessaryObjects
Long badSum = 0L;   // ~2000ms
long goodSum = 0L;   // ~5ms
```

---

## Item 7: Eliminate obsolete object references

### 过期引用问题

```java
public Object pop() {
    if (size == 0) throw new EmptyStackException();
    return elements[--size];
    // 问题：elements[size] 的引用仍然存在，造成内存泄漏
}
```

### 解决方案：清除过期引用

```java
public Object pop() {
    if (size == 0) throw new EmptyStackException();
    Object result = elements[--size];
    elements[size] = null;  // 清除过期引用
    return result;
}
```

### 常见过期引用场景

#### 1. 缓存

```java
// 问题：缓存可能无限增长
Map cache = new HashMap();

// 解决：使用 WeakHashMap 或定期清理
Map cache = new WeakHashMap<>();
```

#### 2. 监听器和回调

```java
// 注册监听器后必须提供注销方法
public void register(Listener l) { listeners.add(l); }
public void unregister(Listener l) { listeners.remove(l); }
```

---

## 扩展：Java 引用类型详解

Java 提供了四种引用类型，从强到弱依次为：**强引用、软引用、弱引用、虚引用**

### 引用类型对比

| 类型 | GC 行为 | 典型用途 |
|------|---------|----------|
| **强引用 (Strong Reference)** | 从不自动回收 | 普通对象引用 |
| **软引用 (Soft Reference)** | 内存不足时回收 | 内存敏感缓存 |
| **弱引用 (Weak Reference)** | 下次 GC 时回收 | 规范化映射、缓存 |
| **虚引用 (Phantom Reference)** | 始终返回 null | 预清理操作 |

### 1. 强引用 (Strong Reference)

```java
Object obj = new Object();  // 强引用
obj = null;                 // 断开引用，对象可被 GC
```

### 2. 软引用 (SoftReference)

内存不足时才会被回收，适合实现内存敏感缓存：

```java
SoftReference<byte[]> ref = new SoftReference<>(new byte[1024 * 1024 * 10]);
// 内存不足时回收
```

### 3. 弱引用 (WeakReference)

下次 GC 时立即回收：

```java
WeakReference<Object> ref = new WeakReference<>(new Object());
// 下次 GC 时回收
```

### 4. WeakHashMap 示例

```java
// 键不再被使用时自动移除
Map<String, String> cache = new WeakHashMap<>();
String key = new String("key");
cache.put(key, "value");
key = null;  // 键可被 GC
System.gc(); // 缓存条目被自动移除
```

### 5. 虚引用 (PhantomReference)

始终返回 `null`，用于资源清理：

```java
ReferenceQueue<Object> queue = new ReferenceQueue<>();
PhantomReference<Object> ref = new PhantomReference<>(obj, queue);
// ref.get() 始终返回 null
// 用于预清理操作
```

### 示例

```java
// com.ssitao.code.effectivejava.ch01.item07.ReferenceTypes
// com.ssitao.code.effectivejava.ch01.item07.WeakHashMapCache
```

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 1 | 静态工厂方法优先于构造器 |
| 2 | 多个构造器参数时使用 Builder 模式 |
| 3 | 用枚举实现单例 |
| 4 | 工具类用私有构造器防止实例化 |
| 5 | 优先使用依赖注入 |
| 6 | 避免创建不必要的对象 |
| 7 | 消除过期的对象引用 + 弱引用 |

---

## 练习题

1. 写一个类，用静态工厂方法返回不同的 List 实现
2. 用 Builder 模式实现一个 User 类（name, email, age, phone 可选参数）
3. 用枚举实现一个 Operation 计算器类
4. 分析你项目中的单例实现，是否符合 Item 3
5. 找一个违反 Item 6 的代码例子并修正
6. 用 WeakReference 实现一个简单的缓存类
