# 第五阶段：并发编程

## 学习目标

掌握Java多线程编程的核心概念，了解并发工具类的使用，理解线程安全问题的解决方法。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | 线程基础 | [Step01_Thread.md](./Step01_Thread.md) | [thread/*.java](./thread/) |
| 2 | 并发工具类 | [Step02_ConcurrentUtils.md](./Step02_ConcurrentUtils.md) | [concurrent/*.java](./concurrent/) |
| 3 | 线程安全与锁 | [Step03_ThreadSafety.md](./Step03_ThreadSafety.md) | [util/*.java](./util/) |

---

## 核心概念概览

### 线程生命周期

```
NEW → RUNNABLE → BLOCKED/WAITING → TERMINATED
       (start)    (sleep/yield/join)   (run完成)

Thread状态:
  NEW         - 创建但未启动
  RUNNABLE    - 可运行（可能正在运行）
  BLOCKED     - 被阻塞（等待获取锁）
  WAITING     - 无限等待（wait/join/park）
  TIMED_WAITING - 限时等待（sleep/yield/wait(timeout)/join(timeout)）
  TERMINATED  - 已终止
```

### 关键概念

```
volatile         - 保证可见性（不保证原子性）
synchronized     - 保证原子性和可见性
wait/notify      - 等待/通知模式
ThreadLocal      - 线程本地存储
原子类           - AtomicInteger/AtomicLong/AtomicReference
并发容器         - ConcurrentHashMap/CopyOnWriteArrayList
同步器           - CountDownLatch/CyclicBarrier/Semaphore/Exchanger
```

---

## 学习建议

1. **线程基础**: 理解线程创建、生命周期、状态转换
2. **并发工具**: 重点理解各种同步器的适用场景
3. **线程安全**: 理解synchronized和volatile的区别

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase05.thread.ThreadDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase05.concurrent.ConcurrentDemo"
```

---

## 下一步

[第六阶段：集合框架](../phase06/README.md)
