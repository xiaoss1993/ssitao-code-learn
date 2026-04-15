# Java 基础面试题

> 内容整理自 [JavaGuide](https://github.com/Snailclimb/JavaGuide)（Star 150k+）、[doocs/advanced-java](https://github.com/doocs/advanced-java)（Star 78k+）等 GitHub 高星项目

## 一、基础概念与常识

### 1. Java 语言有哪些特点？

1. 简单易学（语法简单，上手容易）
2. 面向对象（封装，继承，多态）
3. 平台无关性（Java 虚拟机实现平台无关性）
4. 支持多线程
5. 可靠性（具备异常处理和自动内存管理机制）
6. 安全性（多重安全防护机制）
7. 高效性（Just In Time 编译器优化）
8. 支持网络编程
9. 编译与解释并存

### 2. Java SE vs Java EE vs Java ME

| 版本 | 说明 |
|------|------|
| Java SE | 标准版，桌面/服务器应用基础，包含核心类库和虚拟机 |
| Java EE | 企业版，支持分布式、可移植、健壮的服务端应用（Servlet、JSP、EJB 等） |
| Java ME | 微型版，用于嵌入式消费电子设备（手机、PDA 等），已过时 |

### 3. ⭐ JVM vs JDK vs JRE

| 组件 | 说明 |
|------|------|
| **JVM** | Java 虚拟机，运行 Java 字节码的引擎，不同系统有不同实现 |
| **JDK** | Java 开发工具包 = JRE + 开发工具（javac、javadoc、jdb、javap 等） |
| **JRE** | Java 运行时环境 = JVM + 核心类库 |

**关系图**：
```
JDK = JRE + 编译器和工具
JRE = JVM + 核心类库
```

> 注意：JDK 9 开始不再区分 JDK/JRE，取而代之的是模块系统。JDK 11 开始 Oracle 不再提供单独 JRE 下载。

### 4. ⭐ 什么是字节码？采用字节码的好处？

**字节码**（.class 文件）是 JVM 可以理解的代码，只面向虚拟机。

**好处**：
- 解决了解释型语言执行效率低的问题
- 保留了解释型语言可移植的特点
- Java 程序无需编译即可在多种操作系统运行

**执行流程**：
```
源代码(.java) → 编译器(javac) → 字节码(.class) → JVM → 机器码
                           ↑
                    解释执行 + JIT 编译
```

### 5. 为什么说 Java 是"编译与解释并存"？

- **编译型语言**：编译一次，生成机器码，执行快（C、C++、Go、Rust）
- **解释型语言**：逐行解释执行，开发快（Python、JavaScript）

Java 先编译成字节码，再解释执行。但 JIT 编译器会在运行时将热点代码编译成机器码，下次直接执行，所以 Java 是"编译与解释并存"。

### 6. AOT vs JIT

| 对比 | JIT（即时编译） | AOT（提前编译） |
|------|----------------|----------------|
| 编译时机 | 运行时编译 | 运行前编译 |
| 启动速度 | 较慢（需要预热） | 快 |
| 峰值性能 | 更高 | 较低 |
| 内存占用 | 较高 | 较低 |
| 动态特性 | 完全支持 | 受限（反射、动态代理） |

JDK 9 引入 AOT，适合云原生/Serverless 场景。但无法支持动态特性（反射、动态代理、CGLIB）。

### 7. Oracle JDK vs OpenJDK

| 对比 | Oracle JDK | OpenJDK |
|------|-----------|---------|
| 开源 | 否 | 是 |
| 免费 | 有时间限制 | 完全免费 |
| 稳定性 | LTS 版本更稳定 | 更新频繁 |
| 特有功能 | Java Flight Recorder、JFR 等 | 无 |

> JDK 17 之后两者功能基本一致

### 8. Java 是值传递还是引用传递？

**Java 只有值传递**：
- 基本类型：传递值的副本
- 引用类型：传递引用地址的副本（副本指向同一对象）

```java
void swap(int a, int b) {
    int temp = a;
    a = b;  // 交换的是副本
    b = temp;
}
```

---

## 二、String 专题

### 9. String 的不可变性

String 被声明为 `final`，内部 `char[] value` 也是 `final`：

```java
public final class String {
    private final char value[];
    private int hash;
}
```

**不可变性保证了**：
- 线程安全
- HashMap 等数据结构的键值不会失效
- 字符串常量池的实现基础

### 10. String s = new String("abc") 创建了几个对象？

| 情况 | 创建对象数 |
|------|-----------|
| 常量池没有 "abc" | 2 个（常量池 + 堆） |
| 常量池已有 "abc" | 1 个（堆） |

```java
String s1 = "abc";           // 字面量，从常量池获取
String s2 = new String("abc"); // 堆中创建新对象
System.out.println(s1 == s2);   // false
```

### 11. String.intern() 的作用

将字符串放入常量池，返回池中引用：

```java
String s1 = new String("abc");
String s2 = s1.intern();
String s3 = "abc";
System.out.println(s1 == s2);  // false
System.out.println(s3 == s2);  // true
```

### 12. String、StringBuilder、StringBuffer 对比

| 类型 | 线程安全 | 性能 | 使用场景 |
|------|----------|------|----------|
| String | 安全 | 每次创建新对象，低 | 少量字符串操作 |
| StringBuilder | 不安全 | 只操作自身，高 | 单线程拼接 |
| StringBuffer | 安全（synchronized） | 有同步开销 | 多线程拼接 |

---

## 三、面向对象

### 13. 重载（Overload）vs 重写（Override）

| 特征 | 重载 | 重写 |
|------|------|------|
| 方法名 | 相同 | 相同 |
| 参数列表 | **不同** | 相同 |
| 返回类型 | 可不同 | 相同或协变 |
| 访问修饰符 | 无限制 | 不能更严格 |
| 发生位置 | 同一类 | 子类 |

### 14. 抽象类 vs 接口

| 特征 | 抽象类 | 接口 |
|------|--------|------|
| 继承 | 单继承 | 多实现 |
| 构造方法 | 可以有 | 不能有 |
| 属性 | 任意类型 | public static final |
| 方法实现 | 可以有 | JDK 8+ default/static |
| 静态方法 | 可以有 | JDK 8+ 可以有 |

```java
abstract class AbstractClass {
    abstract void method();
    void concreteMethod() {}
}

interface Interface {
    void method();
    default void defaultMethod() {}
    static void staticMethod() {}
}
```

### 15. this vs super

| 特征 | this | super |
|------|------|-------|
| 指向 | 当前对象 | 父类对象 |
| 用途 | 调用本类构造/方法 | 调用父类构造/方法 |
| 位置 | 构造器首行 | 构造器首行 |

### 16. static 关键字

| 修饰内容 | 作用 |
|----------|------|
| 变量 | 类所有对象共享 |
| 方法 | 静态方法，不能访问实例成员 |
| 代码块 | 类加载时执行一次 |
| 内部类 | 静态内部类，不持有外部类引用 |

### 17. final 关键字

| 修饰内容 | 作用 |
|----------|------|
| 类 | 不能被继承 |
| 方法 | 不能被重写 |
| 变量 | 初始化后不能修改 |

### 18. Object 类的常用方法

```java
public class Object {
    public final Class<?> getClass() {}     // 获取 Class
    public int hashCode() {}               // hash 值
    public boolean equals(Object obj) {}   // 比较
    protected Object clone() {}            // 浅拷贝
    public String toString() {}            // 转字符串
    protected void finalize() {}           // GC 前调用（已废弃）
    public void notify() {}                // 唤醒单线程
    public void notifyAll() {}             // 唤醒所有
    public void wait() {}                  // 等待
}
```

### 19. hashCode 和 equals 的关系

- equals 相等 → hashCode 必须相等
- hashCode 相等 → equals 不一定相等
- 重写 equals 必须重写 hashCode

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Person person = (Person) o;
    return age == person.age && Objects.equals(name, person.name);
}

@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### 20. 深拷贝 vs 浅拷贝

| 类型 | 说明 |
|------|------|
| 浅拷贝 | 对象复制，引用类型字段指向原对象 |
| 深拷贝 | 对象及所有引用类型都复制 |

```java
// 浅拷贝
@Override
protected Object clone() {
    return super.clone();
}

// 深拷贝
@Override
protected Object clone() {
    DeepCopy copy = (DeepCopy) super.clone();
    copy.arr = arr.clone();  // 复制引用类型
    return copy;
}
```

---

## 四、异常处理

### 21. 异常体系

```
Throwable
├── Error（无法恢复）
│   ├── StackOverflowError
│   └── OutOfMemoryError
└── Exception
    ├── RuntimeException（运行时）
    │   ├── NullPointerException
    │   ├── ClassCastException
    │   └── IndexOutOfBoundsException
    └── 非 RuntimeException（编译时）
        ├── IOException
        └── SQLException
```

### 22. throw vs throws

| 关键字 | 作用 | 位置 |
|--------|------|------|
| throw | 抛出具体异常对象 | 方法体内 |
| throws | 声明可能抛出的异常 | 方法签名 |

### 23. try-catch-finally 执行顺序

```java
try {
    return 1;
} catch (Exception e) {
    return 2;
} finally {
    // 始终执行，return 前执行
}
// finally 有 return 会覆盖 try/catch 的 return
```

### 24. try-with-resources

```java
try (FileInputStream fis = new FileInputStream("file")) {
    // 使用资源，自动关闭
} catch (IOException e) {
    // 处理异常
}
```

---

## 五、泛型

### 25. 泛型类型擦除

泛型只在编译期有效，运行期擦除为上限类型或 Object：

```java
List<String> list1 = new ArrayList<>();
List<Integer> list2 = new ArrayList<>();
list1.getClass() == list2.getClass(); // true
```

### 26. PECS 原则

```java
// 生产者（读取）：用 extends
void read(List<? extends Number> list) {
    Number num = list.get(0);  // 可以读
}

// 消费者（写入）：用 super
void write(List<? super Integer> list) {
    list.add(1);  // 可以写
}
```

### 27. 泛型不能使用的情况

- 不能实例化 `new T()`
- 不能创建泛型数组 `new T[]`
- 不能使用 static 泛型
- 不能 `instanceof T`

---

## 六、反射

### 28. 反射的优缺点

- **优点**：动态性（Spring DI、AOP、MyBatis）
- **缺点**：性能开销大、破坏封装

```java
Class<?> clazz = Class.forName("com.example.User");
Object obj = clazz.getDeclaredConstructor().newInstance();
Method method = clazz.getMethod("setName", String.class);
method.invoke(obj, "张三");
```

### 29. 获取 Class 的方式

```java
// 1. Class.forName
Class<?> c1 = Class.forName("com.example.User");
// 2. .class
Class<?> c2 = User.class;
// 3. getClass()
User user = new User();
Class<?> c3 = user.getClass();
```

---

## 七、新特性（Java 8+）

### 30. Java 8 新特性

- Lambda 表达式
- Stream API
- 接口默认方法（default）
- 新的日期时间 API（LocalDateTime）
- Optional 类
- 方法引用（::）
- 重复注解

### 31. Lambda 表达式

```java
// Lambda
list.forEach(s -> System.out.println(s));

// 方法引用
list.forEach(System.out::println);

// 四种形式：
// 1. 类名::静态方法     Integer::sum
// 2. 类名::实例方法    String::length
// 3. 对象::实例方法    obj::equals
// 4. 类名::new         User::new
```

### 32. Stream 常用操作

```java
list.stream()
    .filter(s -> s.length() > 3)     // 中间操作
    .map(String::toUpperCase)         // 中间操作
    .sorted()                         // 中间操作
    .collect(Collectors.toList());    // 终止操作
```

| 类型 | 操作 |
|------|------|
| 中间 | filter、map、flatMap、distinct、sorted、limit、skip |
| 终止 | forEach、collect、count、min、max、reduce |

### 33. Optional 使用

```java
Optional<String> opt = Optional.of("value");
opt.isPresent();                    // 是否有值
opt.orElse("default");             // 为空默认值
opt.ifPresent(System.out::println); // 存在时执行
opt.map(String::toUpperCase);      // 转换
```

---

## 八、序列化

### 34. 序列化注意事项

```java
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient int age;      // 不序列化
    private static int count;       // 不序列化
}
```

### 35. serialVersionUID 的作用

- 版本号，用于反序列化验证兼容性
- 不声明会根据类结构自动生成
- 修改类可能导致反序列化失败

---

## 九、集合框架

### 36. ArrayList vs LinkedList

| 特征 | ArrayList | LinkedList |
|------|-----------|------------|
| 底层结构 | 动态数组 | 双向链表 |
| 随机访问 | O(1) | O(n) |
| 头尾插入 | O(n) | O(1) |
| 内存占用 | 低 | 高 |

### 37. ⭐ HashMap 底层实现

| 版本 | 底层结构 |
|------|----------|
| JDK 1.7 | 数组 + 链表（头插法） |
| JDK 1.8+ | 数组 + 链表/红黑树（尾插法） |

**转换条件**：
- 链表长度 > 8 且数组长度 > 64 → 红黑树
- 树节点数 < 6 → 退化为链表

### 38. HashMap vs Hashtable vs ConcurrentHashMap

| 对比 | HashMap | Hashtable | ConcurrentHashMap |
|------|---------|-----------|-------------------|
| 线程安全 | 否 | 是（synchronized） | 是（CAS + synchronized） |
| 效率 | 高 | 低（全表锁） | 较高（局部锁） |
| Null 支持 | key/value 都可以 | 都不可以 | key 不可以，value 可以 |
| 锁机制 | 无 | 表级锁 | 桶级锁（1.8+） |

### 39. HashMap 加载因子 0.75

- 空间利用率高
- 减少哈希冲突
- 链表/红黑树转换概率合理

---

## 十、多线程与并发

### 40. ⭐ 线程与进程的区别

| 对比 | 进程 | 线程 |
|------|------|------|
| 定义 | 程序的一次执行 | 进程的最小执行单位 |
| 资源 | 独立地址空间 | 共享进程资源 |
| 开销 | 大（创建、切换） | 小（轻量级） |
| 通信 | 复杂（管道、消息队列等） | 简单（共享堆） |

**线程私有**：程序计数器、虚拟机栈、本地方法栈
**线程共享**：堆、方法区

### 41. ⭐ 线程生命周期状态

```
NEW → RUNNABLE → TERMINATED
              ↓
         BLOCKED（等待锁）
              ↓
         WAITING（无限等待）
              ↓
         TIME_WAITING（超时等待）
```

### 42. ⭐ 线程死锁

**四个必要条件**：
1. 互斥条件
2. 请求与保持
3. 不剥夺条件
4. 循环等待

**预防**：破坏任一条件
**避免**：银行家算法（安全状态）

```java
// 死锁示例
synchronized (resource1) {
    synchronized (resource2) {
        // 操作
    }
}
```

### 43. ⭐ synchronized vs ReentrantLock

| 对比 | synchronized | ReentrantLock |
|------|-------------|---------------|
| 锁类型 | 隐式锁 | 显式锁 |
| 公平锁 | 非公平 | 可配置 |
| 锁超时 | 不可 | 可 |
| 条件 Condition | 不可 | 可多个 |
| 中断 | 不可 | 可 |

### 44. volatile 的作用

- **可见性**：修改立即刷新到主内存
- **禁止指令重排序**
- **不**保证原子性（如 i++）

### 45. ThreadLocal 原理

- 每个线程持有独立的 ThreadLocalMap
- key 是 ThreadLocal，value 是变量副本
- 线程退出时需调用 remove() 防止内存泄漏

### 46. 线程池参数

```java
new ThreadPoolExecutor(
    corePoolSize,      // 核心线程数
    maxPoolSize,       // 最大线程数
    keepAliveTime,     // 空闲线程存活时间
    TimeUnit,          // 时间单位
    workQueue,         // 阻塞队列
    threadFactory,     // 线程工厂
    handler            // 拒绝策略
);
```

**拒绝策略**：AbortPolicy、CallerRunsPolicy、DiscardOldestPolicy、DiscardPolicy

### 47. 创建线程的方式

1. 继承 Thread
2. 实现 Runnable
3. 实现 Callable + FutureTask
4. 线程池

---

## 十一、JVM

### 48. ⭐ JVM 内存区域

| 区域 | 说明 |
|------|------|
| 程序计数器 | 线程私有，记录字节码行号 |
| 虚拟机栈 | 线程私有，方法调用栈帧 |
| 本地方法栈 | 线程私有，Native 方法 |
| 堆 | 线程共享，对象实例 |
| 方法区 | 线程共享，类信息/常量/静态变量（JDK 8+ 元空间） |

### 49. ⭐ 堆内存结构（JDK 8+）

```
堆 = 新生代 + 老年代
新生代 = Eden + Survivor0 + Survivor1
```

- **新生代**：对象优先分配，Minor GC
- **老年代**：长期存活对象，Full GC
- **元空间**：类信息，使用直接内存

### 50. ⭐ 垃圾回收算法

| 算法 | 特点 |
|------|------|
| 标记-清除 | 效率低，产生碎片 |
| 复制 | 效率高，但浪费一半空间（新生代） |
| 标记-整理 | 无碎片，效率低（老年代） |

### 51. ⭐ 垃圾收集器

| 收集器 | 区域 | 特点 |
|--------|------|------|
| Serial | 新生代 | 单线程，stop the world |
| ParNew | 新生代 | Serial 多线程版 |
| Parallel Scavenge | 新生代 | 吞吐量优先 |
| CMS | 老年代 | 并发收集，短停顿 |
| G1 | 全堆 | 分区回收，可预测停顿 |
| ZGC | 全堆 | 低延迟（亚毫秒级） |

### 52. ⭐ Minor GC vs Full GC

| 对比 | Minor GC | Full GC |
|------|----------|---------|
| 区域 | 新生代 | 老年代 + 方法区 |
| 触发条件 | Eden 满 | 老年代满、显式调用 |
| 停顿时间 | 短 | 长 |

### 53. 判断对象是否死亡

**引用计数法**：简单高效，但无法处理循环引用

**可达性分析法**：从 GC Roots 向下搜索
- GC Roots 包括：虚拟机栈、本地方法栈、方法区静态/常量引用、锁对象

### 54. 引用类型

| 类型 | 特点 |
|------|------|
| 强引用 | 永远不会回收 |
| 软引用 | 内存不足时回收 |
| 弱引用 | 下次 GC 时回收 |
| 虚引用 | 必须配合队列，无法获取对象 |

### 55. ⭐ 类加载过程

```
加载 → 验证 → 准备 → 解析 → 初始化 → 使用 → 卸载
```

- **加载**：通过类加载器加载 .class 文件
- **验证**：文件格式、元数据、字节码、符号引用验证
- **准备**：分配内存，设置初始值
- **解析**：符号引用转为直接引用
- **初始化**：执行静态初始化块，给静态变量赋值

### 56. 类加载器

| 类加载器 | 加载范围 |
|----------|----------|
| Bootstrap | 核心类库（rt.jar） |
| Extension | jre/lib/ext |
| Application | classpath |

**双亲委派**：向上委托，向下加载
- 保证类的唯一性和安全性

---

## 十二、其他

### 57. 8 种基本数据类型

| 类型 | 字节 | 范围/说明 |
|------|------|-----------|
| byte | 1 | -128 ~ 127 |
| short | 2 | -32768 ~ 32767 |
| int | 4 | -2^31 ~ 2^31-1 |
| long | 8 | -2^63 ~ 2^63-1 |
| float | 4 | 3.4e-38 ~ 3.4e+38 |
| double | 8 | 1.7e-308 ~ 1.7e+308 |
| char | 2 | Unicode |
| boolean | 1/4 | true/false |

### 58. 自动装箱的陷阱

```java
Integer a = 127;  // 缓存 -128~127
Integer b = 127;
System.out.println(a == b);  // true

Integer c = 128;
Integer d = 128;
System.out.println(c == d);  // false
```

### 59. BIO vs NIO vs AIO

| 类型 | 说明 |
|------|------|
| BIO | 同步阻塞，流式 IO |
| NIO | 同步非阻塞，多路复用 |
| AIO | 异步非阻塞，事件驱动 |

### 60. 常见内存泄漏原因

- 静态集合持有对象引用
- 未关闭的资源（连接、流）
- 监听器未注销
- ThreadLocal 未清理
- 内部类持有外部类引用

---

## 十三、代码输出题

### 61. String 创建对象题

```java
String s1 = "abc";
String s2 = new String("abc");
System.out.println(s1 == s2);           // false
System.out.println(s1.intern() == s2);  // false
System.out.println(s1 == s2.intern()); // true
```

### 62. Integer 缓存题

```java
Integer a = 100;
Integer b = 100;
System.out.println(a == b);  // true

Integer c = 200;
Integer d = 200;
System.out.println(c == d);  // false
```

### 63. i++ vs ++i

```java
int i = 1;
int j = i++;
System.out.println("i=" + i + ", j=" + j);  // i=2, j=1

int a = 1;
int b = ++a;
System.out.println("a=" + a + ", b=" + b);  // a=2, b=2
```

### 64. 浮点数精度

```java
System.out.println(0.1 + 0.2);  // 0.30000000000000004
// 正确比较方式
Math.abs(0.1 + 0.2 - 0.3) < 0.0001  // true
```

### 65. 死锁代码分析

```java
// 线程1持有resource1，等待resource2
// 线程2持有resource2，等待resource1
// 形成死锁
```
