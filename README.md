# Design Patterns Tutorial

Java 设计模式学习项目，包含了常用设计模式的实现及实际应用案例。

## 项目简介

本项目通过详细的代码示例，演示了各种设计模式的核心概念、使用场景和实现方式。每个模式都配有完整的代码实现，帮助开发者深入理解面向对象设计原则。

## 技术栈

- **Java** - 编程语言
- **Maven** - 项目构建工具
- **Spring Framework** - 依赖注入和 AOP
- **JUnit** - 单元测试

## 设计模式列表

### 创建型模式 (Creational)

| 模式 | 说明 |
|------|------|
| [Singleton](./ssitao-design-patterns/singleton/) | 单例模式 - 确保类只有一个实例 |
| [Factory Method](./ssitao-design-patterns/factory-method/) | 工厂方法模式 - 定义创建对象的接口 |
| [Abstract Factory](./ssitao-design-patterns/abstract-factory/) | 抽象工厂模式 - 创建一系列相关对象 |
| [Builder](./ssitao-design-patterns/builder/) | 建造者模式 - 分离对象构建和表示 |
| [Prototype](./ssitao-design-patterns/prototype/) | 原型模式 - 通过克隆创建对象 |
| [Factory Kit](./ssitao-design-patterns/factory-kit/) | 工厂套件模式 - 灵活的工厂注册机制 |

### 结构型模式 (Structural)

| 模式 | 说明 |
|------|------|
| [Adapter](./ssitao-design-patterns/adapter/) | 适配器模式 - 转换接口兼容 |
| [Bridge](./ssitao-design-patterns/bridge/) | 桥接模式 - 分离抽象和实现 |
| [Composite](./ssitao-design-patterns/composite/) | 组合模式 - 树形结构统一处理 |
| [Decorator](./ssitao-design-patterns/decorator/) | 装饰器模式 - 动态添加职责 |
| [Facade](./ssitao-design-patterns/facade/) | 外观模式 - 统一简化接口 |
| [Flyweight](./ssitao-design-patterns/flyweight/) | 享元模式 - 共享细粒度对象 |
| [Proxy](./ssitao-design-patterns/proxy/) | 代理模式 - 控制对象访问 |

### 行为型模式 (Behavioral)

| 模式 | 说明 |
|------|------|
| [Chain of Responsibility](./ssitao-design-patterns/chain/) | 责任链模式 - 请求传递处理 |
| [Command](./ssitao-design-patterns/command/) | 命令模式 - 请求封装为对象 |
| [Iterator](./ssitao-design-patterns/iterator/) | 迭代器模式 - 统一遍历接口 |
| [Mediator](./ssitao-design-patterns/mediator/) | 中介者模式 - 对象间协调 |
| [Memento](./ssitao-design-patterns/memento/) | 备忘录模式 - 状态快照保存 |
| [Observer](./ssitao-design-patterns/observer/) | 观察者模式 - 事件发布订阅 |
| [State](./ssitao-design-patterns/state/) | 状态模式 - 对象行为切换 |
| [Strategy](./ssitao-design-patterns/strategy/) | 策略模式 - 算法替换 |
| [Template Method](./ssitao-design-patterns/template-method/) | 模板方法模式 - 算法框架定义 |
| [Visitor](./ssitao-design-patterns/visitor/) | 访问者模式 - 操作分离扩展 |

### 并发模式 (Concurrency)

| 模式 | 说明 |
|------|------|
| [Thread Pool](./ssitao-design-patterns/thread-pool/) | 线程池模式 - 复用线程资源 |
| [Producer-Consumer](./ssitao-design-patterns/producer-consumer/) | 生产者-消费者模式 - 线程间通信 |
| [Reader-Writer Lock](./ssitao-design-patterns/reader-writer/) | 读写锁模式 - 优化读写操作 |
| [Double-Checked Locking](./ssitao-design-patterns/double-checked-locking/) | 双重检查锁定 - 线程安全单例 |
| [Async Method Invocation](./ssitao-design-patterns/async-method-invocation/) | 异步方法调用 - 非阻塞执行 |

### 其他模式

| 模式 | 说明 |
|------|------|
| [Callback](./ssitao-design-patterns/callback/) | 回调模式 - 异步通知机制 |

## 模块结构

```
ssitao-design-patterns/
├── adapter/           # 适配器模式
├── abstract-factory/   # 抽象工厂模式
├── async-method-invocation/  # 异步方法调用
├── bridge/            # 桥接模式
├── builder/          # 建造者模式
├── callback/         # 回调模式
├── chain/            # 责任链模式
├── command/          # 命令模式
├── composite/        # 组合模式
├── decorator/        # 装饰器模式
├── double-checked-locking/  # 双重检查锁定
├── factory-kit/      # 工厂套件模式
├── factory-method/   # 工厂方法模式
├── facade/           # 外观模式
├── flyweight/        # 享元模式
├── iterator/         # 迭代器模式
├── mediator/         # 中介者模式
├── memento/          # 备忘录模式
├── observer/         # 观察者模式
├── producer-consumer/  # 生产者-消费者
├── prototype/        # 原型模式
├── proxy/            # 代理模式
├── reader-writer/    # 读写锁模式
├── singleton/        # 单例模式
├── state/           # 状态模式
├── strategy/         # 策略模式
├── template-method/  # 模板方法模式
├── thread-pool/      # 线程池模式
└── visitor/          # 访问者模式
```

## 快速开始

### 编译项目

```bash
mvn clean compile
```

### 运行测试

```bash
mvn test
```

### 运行特定模块

```bash
cd ssitao-design-patterns/decorator
mvn clean compile exec:java
```

## 学习建议

1. **从基础开始**: 建议先学习创建型模式（Singleton、Factory Method 等）
2. **结合实际**: 每个模式都包含实际应用场景的示例
3. **动手实践**: 尝试修改示例代码加深理解
4. **注意 JDK/Spring 应用**: 代码中包含了 JDK 和 Spring 框架中设计模式的应用示例

## 参考资源

- [Design Patterns - Gang of Four](https://en.wikipedia.org/wiki/Design_Patterns)
- [Refactoring Guru - Design Patterns](https://refactoring.guru/design-patterns)
- [Source Making - Design Patterns](https://sourcemaking.com/design_patterns)

## License

MIT License
