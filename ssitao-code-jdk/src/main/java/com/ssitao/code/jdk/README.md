# JDK 核心基础学习

本模块系统学习 Java 核心基础语法和标准库，通过文档教程和代码示例全面掌握 JDK。

---

## 学习路径

### 第一阶段：核心语法基础（1-2周）📍 当前

| 步骤 | 主题 | 状态 |
|------|------|------|
| 1 | Object类 | ✅ |
| 2 | String家族 | ✅ |
| 3 | 包装类 | ✅ |
| 4 | 常用工具类 | ✅ |

[进入学习](./phase01/README.md)

---

### 第二阶段：集合框架（2-3周）

| 步骤 | 主题 |
|------|------|
| 5 | List接口 - ArrayList, LinkedList, Vector |
| 6 | Map接口 - HashMap, LinkedHashMap, TreeMap |
| 7 | Set接口 - HashSet, LinkedHashSet, TreeSet |
| 8 | Queue接口 - PriorityQueue, ArrayDeque |

---

### 第三阶段：并发与线程（2-3周）

| 步骤 | 主题 |
|------|------|
| 9 | Thread与Runnable |
| 10 | 同步原语 - synchronized, volatile |
| 11 | 并发工具 - CountDownLatch, CyclicBarrier |
| 12 | 并发容器 - ConcurrentHashMap |

---

### 第四阶段：IO与NIO（1-2周）

| 步骤 | 主题 |
|------|------|
| 13 | 字节流 - InputStream, OutputStream |
| 14 | 字符流 - Reader, Writer |
| 15 | NIO - Channel, Buffer, Selector |

---

### 第五阶段：高级特性（2-3周）

| 步骤 | 主题 |
|------|------|
| 16 | 反射 - Class, Method, Field |
| 17 | 代理 - Proxy, InvocationHandler |
| 18 | 注解 - @Retention, @Target |
| 19 | 泛型 - Type体系 |
| 20 | 时间日期 - LocalDateTime, Instant |

---

## 学习方法

### 每步骤的四步法

```
1️⃣ 文档教程  →  理解概念和原理
2️⃣ 代码示例  →  运行观察输出
3️⃣ 练习题    →  独立完成验证
4️⃣ 源码分析  →  深入理解实现
```

### 代码运行

```bash
# 编译
mvn compile -pl ssitao-code-jdk

# 运行示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase01.object.Person" -pl ssitao-code-jdk

# 运行测试
mvn test -pl ssitao-code-jdk
```

---

## 项目结构

```
ssitao-code-jdk/
├── src/main/java/com/ssitao/code/jdk/
│   ├── phase01/          # 第一阶段: 核心语法基础
│   │   ├── object/       # Object类代码
│   │   ├── string/      # String家族代码
│   │   ├── wrapper/     # 包装类代码
│   │   └── utils/       # 常用工具类代码
│   ├── phase02/          # 第二阶段: 集合框架
│   ├── phase03/          # 第三阶段: 并发与线程
│   ├── phase04/          # 第四阶段: IO与NIO
│   └── phase05/          # 第五阶段: 高级特性
└── pom.xml
```

---

## 相关资源

- [Effective Java](../ssitao-code-effectivejava/) - JDK最佳实践
- [数据结构](../ssitao-code-datastruct/) - 集合框架原理
- [并发编程](../ssitao-code-concurrent/) - 多线程与并发

---

## 学习笔记

建议在学习过程中记录以下内容：

1. **核心概念**：每个类/接口的核心作用
2. **常用方法**：最常用的5-10个方法
3. **注意事项**：容易踩的坑
4. **面试考点**：常见的面试问题

---

*持续更新中...*
