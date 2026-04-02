# Code Learn - Java 学习项目导航

Java 技术栈学习项目集，包含设计模式、并发编程、数据结构、MyBatis 等核心主题。

---

## 学习目录

### 1. [设计模式](./ssitao-design-patterns/)

创建型、结构型、行为型、并发模式完整实现。

| 分类 | 内容 |
|------|------|
| 创建型 | Singleton, Factory Method, Abstract Factory, Builder, Prototype, Factory Kit |
| 结构型 | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy |
| 行为型 | Chain, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor |
| 并发型 | Thread Pool, Producer-Consumer, Reader-Writer Lock, Double-Checked Locking, Async Method Invocation, Callback |

### 2. [并发编程](./ssitao-code-concurrent/)

Java 多线程与并发编程实战。

| 模块 | 说明 |
|------|------|
| thread-base | 线程基础 |

### 3. [数据结构](./ssitao-code-datastruct/)

常用数据结构实现与算法学习。

### 4. [Effective Java](./ssitao-code-effectivejava/)

《Effective Java》书籍条款实践代码。

### 5. [MyBatis 源码学习](./ssitao-code-learn-mybatis/)

MyBatis 源码分析学习，包含 11 个步骤模块。

| 步骤 | 内容 |
|------|------|
| step-01 ~ step-11 | MyBatis 核心机制逐行解析 |

### 6. [LangChain Java](./ssitao-code-langchain/)

Java 版 LangChain AI 应用开发。

---

## 快速开始

```bash
# 编译全部项目
mvn clean compile

# 运行测试
mvn test

# 编译特定模块
cd ssitao-design-patterns/singleton
mvn clean compile exec:java
```

---

## 项目结构

```
ssitao-code-learn/
├── ssitao-design-patterns/      # 23种设计模式实现
│   ├── singleton/
│   ├── factory-method/
│   ├── abstract-factory/
│   ├── builder/
│   ├── prototype/
│   ├── adapter/
│   ├── bridge/
│   ├── composite/
│   ├── decorator/
│   ├── facade/
│   ├── flyweight/
│   ├── proxy/
│   ├── chain/
│   ├── command/
│   ├── iterator/
│   ├── mediator/
│   ├── memento/
│   ├── observer/
│   ├── state/
│   ├── strategy/
│   ├── template-method/
│   ├── visitor/
│   ├── thread-pool/
│   ├── producer-consumer/
│   ├── reader-writer/
│   ├── double-checked-locking/
│   ├── async-method-invocation/
│   └── callback/
├── ssitao-code-concurrent/       # 并发编程
│   └── thread-base/
├── ssitao-code-datastruct/       # 数据结构
├── ssitao-code-effectivejava/    # Effective Java
├── ssitao-code-langchain/         # LangChain
└── ssitao-code-learn-mybatis/     # MyBatis 源码 (step-01 ~ step-11)
```
