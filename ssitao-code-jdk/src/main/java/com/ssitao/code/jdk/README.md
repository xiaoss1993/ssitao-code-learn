# JDK 核心基础学习

本模块系统学习 Java 核心基础语法和标准库，通过文档教程和代码示例全面掌握 JDK。

---

## 学习路径总览

### ✅ 第一阶段：核心语法基础（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | Object类 | ✅ | `phase01/object/` |
| 2 | String家族 | ✅ | `phase01/string/` |
| 3 | 包装类 | ✅ | `phase01/wrapper/` |
| 4 | 常用工具类 | ✅ | `phase01/utils/` |

[进入学习](./phase01/README.md)

---

### ✅ 第二阶段：集合框架基础（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | List集合 | ✅ | [Step01_List.md](./phase02/Step01_List.md) |
| 2 | Set集合 | ✅ | [Step02_Set.md](./phase02/Step02_Set.md) |
| 3 | Map集合 | ✅ | [Step03_Map.md](./phase02/Step03_Map.md) |
| 4 | Queue集合 | ✅ | [Step04_Queue.md](./phase02/Step04_Queue.md) |
| 5 | 迭代器 | ✅ | [Step05_Iterator.md](./phase02/Step05_Iterator.md) |

[进入学习](./phase02/README.md)

---

### ✅ 第三阶段：泛型与反射（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | 泛型 | ✅ | [Step01_Generics.md](./phase03/Step01_Generics.md) |
| 2 | 反射 | ✅ | [Step02_Reflection.md](./phase03/Step02_Reflection.md) |

[进入学习](./phase03/README.md)

---

### ✅ 第四阶段：Lambda与Stream（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | Lambda表达式 | ✅ | [Step01_Lambda.md](./phase04/Step01_Lambda.md) |
| 2 | Stream API | ✅ | [Step02_StreamAPI.md](./phase04/Step02_StreamAPI.md) |

[进入学习](./phase04/README.md)

---

### ✅ 第五阶段：IO流（完成）📍 刚完成

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | IO概述 | ✅ | [Step01_IOOverview.md](./io/Step01_IOOverview.md) |
| 2 | 字节流 | ✅ | [Step02_ByteStream.md](./io/Step02_ByteStream.md) |
| 3 | 字符流 | ✅ | [Step03_CharStream.md](./io/Step03_CharStream.md) |
| 4 | 缓冲流 | ✅ | [Step04_BufferedStream.md](./io/Step04_BufferedStream.md) |
| 5 | 对象序列化 | ✅ | [Step05_ObjectStream.md](./io/Step05_ObjectStream.md) |
| 6 | NIO | ✅ | [Step06_NIO.md](./io/Step06_NIO.md) |

[进入学习](./io/README.md)

---

### ✅ 第六阶段：并发编程（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | 线程基础 | ✅ | [Step01_Thread.md](./phase05/Step01_Thread.md) |
| 2 | 并发工具类 | ✅ | [Step02_ConcurrentUtils.md](./phase05/Step02_ConcurrentUtils.md) |
| 3 | 线程安全与锁 | ✅ | [Step03_ThreadSafety.md](./phase05/Step03_ThreadSafety.md) |

[进入学习](./phase05/README.md)

---

### ✅ 第七阶段：集合框架进阶（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | 集合概述 | ✅ | [Step01_Collection.md](./phase06/Step01_Collection.md) |
| 2 | List接口 | ✅ | [Step02_List.md](./phase06/Step02_List.md) |
| 3 | Map接口 | ✅ | [Step03_Map.md](./phase06/Step03_Map.md) |
| 4 | Set接口 | ✅ | [Step04_Set.md](./phase06/Step04_Set.md) |
| 5 | Queue接口 | ✅ | [Step05_Queue.md](./phase06/Step05_Queue.md) |

[进入学习](./phase06/README.md)

---

### ✅ 第八阶段：日期时间API（完成）📍 新增

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | LocalDateTime | ✅ | [Step01_LocalDateTime.md](./phase08/Step01_LocalDateTime.md) |
| 2 | 格式化与解析 | ✅ | [Step02_DateTimeFormatter.md](./phase08/Step02_DateTimeFormatter.md) |
| 3 | Instant与Duration | ✅ | [Step03_Instant.md](./phase08/Step03_Instant.md) |

[进入学习](./phase08/README.md)

---

### ✅ 第九阶段：JVM与性能优化（完成）

| 步骤 | 主题 | 状态 | 文档 |
|------|------|------|------|
| 1 | JVM概述 | ✅ | [Step01_JVM.md](./phase07/Step01_JVM.md) |
| 2 | 垃圾回收 | ✅ | [Step02_GC.md](./phase07/Step02_GC.md) |
| 3 | 性能优化 | ✅ | [Step03_Performance.md](./phase07/Step03_Performance.md) |

[进入学习](./phase07/README.md)

---

## 项目结构

```
ssitao-code-jdk/
├── src/main/java/com/ssitao/code/jdk/
│   ├── phase01/          # 第一阶段: 核心语法基础 ✅
│   ├── phase02/          # 第二阶段: 集合框架基础 ✅
│   ├── phase03/          # 第三阶段: 泛型与反射 ✅
│   ├── phase04/          # 第四阶段: Lambda与Stream ✅
│   ├── io/               # 第五阶段: IO流 ✅
│   ├── phase05/          # 第六阶段: 并发编程 ✅
│   ├── phase06/          # 第七阶段: 集合框架进阶 ✅
│   ├── phase08/          # 第八阶段: 日期时间API ✅
│   └── phase07/          # 第九阶段: JVM与性能优化 ✅
└── pom.xml
```

---

## 核心知识点速查表

### 集合框架

| 接口 | 主要实现 | 特点 |
|------|----------|------|
| List | ArrayList, LinkedList, Vector | 有序、可重复 |
| Set | HashSet, TreeSet, LinkedHashSet | 无序/有序、去重 |
| Map | HashMap, TreeMap, LinkedHashMap | 键值对 |
| Queue | ArrayQueue, PriorityQueue | 队列、FIFO |

### 并发编程

| 类别 | 关键类 |
|------|--------|
| 基础 | Thread, Runnable, Callable |
| 同步 | synchronized, Lock, ReentrantLock |
| 协作 | wait, notify, Condition |
| 工具 | CountDownLatch, CyclicBarrier, Semaphore |
| 原子类 | AtomicInteger, AtomicLong, AtomicReference |
| 并发容器 | ConcurrentHashMap, CopyOnWriteArrayList |
| 线程池 | ExecutorService, ThreadPoolExecutor |

### IO流

| 类别 | 关键类 |
|------|--------|
| 字节流 | InputStream, OutputStream |
| 字符流 | Reader, Writer |
| 缓冲流 | BufferedInputStream, BufferedReader |
| 对象流 | ObjectInputStream, ObjectOutputStream |
| NIO | Buffer, Channel, Selector |

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
mvn compile

# 运行集合框架示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase02.list.ArrayListDemo"

# 运行Lambda示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase04.lambda.LambdaDemo"

# 运行Stream示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase04.stream.StreamDemo"

# 运行IO示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.io.basic.ByteStreamDemo"

# 运行并发示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase05.thread.ThreadDemo"

# 运行反射示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase03.reflection.ReflectionDemo"

# 运行日期时间示例
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase08.datetime.LocalDateTimeDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase08.datetime.InstantDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase08.format.DateTimeFormatterDemo"
```

---

## ✅ 所有阶段学习完成！

恭喜！JDK核心API的9个阶段已全部完成学习。

| 阶段 | 主题 | 状态 |
|------|------|------|
| Phase 01 | 核心语法基础 | ✅ |
| Phase 02 | 集合框架基础 | ✅ |
| Phase 03 | 泛型与反射 | ✅ |
| Phase 04 | Lambda与Stream | ✅ |
| Phase 05 | IO流 | ✅ |
| Phase 06 | 并发编程 | ✅ |
| Phase 07 | 集合框架进阶 | ✅ |
| Phase 08 | 日期时间API | ✅ |
| Phase 09 | JVM与性能优化 | ✅ |

---

*最后更新：2026-04-09*
