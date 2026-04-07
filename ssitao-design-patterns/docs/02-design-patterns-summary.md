# 设计模式总结

## 目录

1. [设计模式概述](#1-设计模式概述)
2. [创建型模式 (Creational)](#2-创建型模式-creational)
3. [结构型模式 (Structural)](#3-结构型模式-structural)
4. [行为型模式 (Behavioral)](#4-行为型模式-behavioral)
5. [并发模式 (Concurrency)](#5-并发模式-concurrency)

---

## 1. 设计模式概述

### 1.1 什么是设计模式

设计模式是软件开发中**常见问题的通用、可重用的解决方案**。它们是最佳实践的总结，帮助我们构建可维护、可扩展、可重用的软件。

### 1.2 设计模式分类

```
设计模式
├── 创建型模式 (5种)
│   ├── 单例模式 (Singleton)
│   ├── 工厂方法 (Factory Method)
│   ├── 抽象工厂 (Abstract Factory)
│   ├── 建造者模式 (Builder)
│   └── 原型模式 (Prototype)
│
├── 结构型模式 (7种)
│   ├── 适配器模式 (Adapter)
│   ├── 桥接模式 (Bridge)
│   ├── 组合模式 (Composite)
│   ├── 装饰器模式 (Decorator)
│   ├── 外观模式 (Facade)
│   ├── 享元模式 (Flyweight)
│   └── 代理模式 (Proxy)
│
├── 行为型模式 (11种)
│   ├── 责任链模式 (Chain of Responsibility)
│   ├── 命令模式 (Command)
│   ├── 迭代器模式 (Iterator)
│   ├── 中介者模式 (Mediator)
│   ├── 备忘录模式 (Memento)
│   ├── 观察者模式 (Observer)
│   ├── 状态模式 (State)
│   ├── 策略模式 (Strategy)
│   ├── 模板方法 (Template Method)
│   ├── 访问者模式 (Visitor)
│   └── 解释器模式 (Interpreter)
│
└── 并发模式
    ├── 双检查锁定 (Double-Checked Locking)
    ├── 线程池 (Thread Pool)
    ├── 生产者-消费者 (Producer-Consumer)
    ├── 异步方法调用 (Async Method Invocation)
    ├── 读写锁 (Reader-Writer Lock)
    └── 回调 (Callback)
```

### 1.3 设计原则 (SOLID)

| 原则 | 全称 | 说明 |
|------|------|------|
| S | 单一职责原则 | 一个类只有一个引起变化的原因 |
| O | 开闭原则 | 对扩展开放，对修改关闭 |
| L | 里氏替换原则 | 子类可以替换父类 |
| I | 接口隔离原则 | 使用小而专门的接口 |
| D | 依赖倒置原则 | 依赖抽象，不依赖具体 |

---

## 2. 创建型模式 (Creational)

创建型模式关注**对象的创建机制**，帮助我们以适当的方式创建对象。

### 2.1 单例模式 (Singleton)

**意图**：确保一个类只有一个实例，并提供全局访问点。

**UML结构**：
```
┌─────────────────────────────────┐
│      <<unique>>                 │
│       Singleton                 │
├─────────────────────────────────┤
│ - instance: Singleton           │
├─────────────────────────────────┤
│ - Singleton()                  │
│ + getInstance(): Singleton      │
│ + operation()                   │
└─────────────────────────────────┘
```

**核心实现**：
```java
public class Singleton {
    // 1. 私有静态实例（延迟加载）
    private static volatile Singleton instance;

    // 2. 私有构造函数
    private Singleton() {}

    // 3. 公共静态获取方法（双重检查锁定）
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

**应用场景**：
- 数据库连接池
- 日志系统
- 配置管理器
- 线程池

**项目代码**：`singleton/` 目录

---

### 2.2 工厂方法模式 (Factory Method)

**意图**：定义创建对象的接口，让子类决定实例化哪个类。

**UML结构**：
```
        ┌─────────────────┐
        │    Creator      │
        │  <<abstract>>  │
        ├─────────────────┤
        │ + create()      │
        └────────┬────────┘
                 │
     ┌───────────┼───────────┐
     │           │           │
     ▼           ▼           ▼
┌─────────┐ ┌─────────┐ ┌─────────┐
│CreatorA │ │CreatorB │ │CreatorN │
└─────────┘ └─────────┘ └─────────┘
```

**核心实现**：
```java
// 抽象创建者
public interface Blacksmith {
    Weapon createWeapon();
}

// 具体创建者
public class ElfBlacksmith implements Blacksmith {
    @Override
    public Weapon createWeapon() {
        return new ElfWeapon();
    }
}

public class OrcBlacksmith implements Blacksmith {
    @Override
    public Weapon createWeapon() {
        return new OrcWeapon();
    }
}
```

**应用场景**：
- JDK: `Calendar.getInstance()`, `Collection.iterator()`
- Spring: `BeanFactory`, `FactoryBean`
- 连接池管理

**项目代码**：`factory-method/` 目录

---

### 2.3 抽象工厂模式 (Abstract Factory)

**意图**：提供一个创建一系列相关对象的接口，而无需指定它们的具体类。

**UML结构**：
```
        ┌─────────────────────┐
        │  AbstractFactory    │
        │  <<interface>>     │
        ├─────────────────────┤
        │ + createProductA()  │
        │ + createProductB()  │
        └──────────┬──────────┘
                   │
    ┌──────────────┼──────────────┐
    │              │              │
    ▼              ▼              ▼
┌─────────┐  ┌─────────┐    ┌─────────┐
│Factory1 │  │Factory2 │    │FactoryN │
└────┬────┘  └────┬────┘    └────┬────┘
     │            │              │
     ▼            ▼              ▼
┌─────────┐  ┌─────────┐    ┌─────────┐
│ProductA1│  │ProductA2│    │ProductA3│
│ProductB1│  │ProductB2│    │ProductB3│
└─────────┘  └─────────┘    └─────────┘
```

**核心实现**：
```java
// 抽象工厂
public interface KingdomFactory {
    Castle createCastle();
    King createKing();
    Army createArmy();
}

// 具体工厂
public class ElfKingdomFactory implements KingdomFactory {
    @Override
    public Castle createCastle() {
        return new ElfCastle();
    }
    @Override
    public King createKing() {
        return new ElfKing();
    }
    @Override
    public Army createArmy() {
        return new ElfArmy();
    }
}
```

**应用场景**：
- 跨平台UI组件（Windows、Mac、Linux）
- 数据库访问（MySQL、Oracle、PostgreSQL）
- 游戏中的种族系统

**项目代码**：`abstract-factory/` 目录

---

### 2.4 建造者模式 (Builder)

**意图**：将一个复杂对象的构建与它的表示分离。

**UML结构**：
```
┌─────────────────────────────────────────┐
│               Director                  │
├─────────────────────────────────────────┤
│ - builder: Builder                      │
├─────────────────────────────────────────┤
│ + construct()                           │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│            <<interface>>                 │
│               Builder                   │
├─────────────────────────────────────────┤
│ + buildPart1()                          │
│ + buildPart2()                          │
│ + getResult()                           │
└─────────────────┬───────────────────────┘
                  │
    ┌─────────────┼─────────────┐
    ▼             ▼             ▼
┌─────────┐ ┌─────────┐   ┌─────────┐
│Builder1 │ │Builder2 │   │BuilderN │
└────┬────┘ └────┬────┘   └────┬────┘
     │           │             │
     ▼           ▼             ▼
┌─────────┐ ┌─────────┐   ┌─────────┐
│Product1 │ │Product2 │   │Product3 │
└─────────┘ └─────────┘   └─────────┘
```

**核心实现**：
```java
// 产品
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    // ... 不可变对象

    // 建造者
    public static class Builder {
        private int servingSize = 0;
        private int servings = 0;
        private int calories = 0;

        public Builder servingSize(int val) {
            servingSize = val;
            return this;
        }
        public Builder servings(int val) {
            servings = val;
            return this;
        }
        public Builder calories(int val) {
            calories = val;
            return this;
        }
        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize = builder.servingSize;
        servings = builder.servings;
        calories = builder.calories;
    }
}

// 使用
NutritionFacts facts = new NutritionFacts.Builder()
    .servingSize(240)
    .servings(8)
    .calories(100)
    .build();
```

**应用场景**：
- `StringBuilder`, `StringBuffer`
- `DocumentBuilder`, `DocumentBuilderFactory`
- `AlertDialog.Builder` (Android)
- `JdbcTemplate` (Spring)

**项目代码**：`builder/` 目录

---

### 2.5 原型模式 (Prototype)

**意图**：通过复制现有对象来创建新对象，无需知道其具体类型。

**UML结构**：
```
┌─────────────────────────────────┐
│      <<interface>>              │
│       Prototype                 │
├─────────────────────────────────┤
│ + clone(): Prototype            │
└─────────────┬───────────────────┘
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌───────────┐   ┌───────────┐
│ConcreteA  │   │ConcreteB  │
│prototype  │   │prototype  │
└───────────┘   └───────────┘
```

**核心实现**：
```java
public interface Prototype<T> {
    T clone();
}

public class ConcretePrototype implements Prototype<ConcretePrototype> {
    private String name;

    @Override
    public ConcretePrototype clone() {
        ConcretePrototype copy = new ConcretePrototype();
        copy.name = this.name;
        return copy;
    }
}

// 或者使用Cloneable接口
public class Sheep implements Cloneable {
    private String name;

    @Override
    protected Sheep clone() throws CloneNotSupportedException {
        return (Sheep) super.clone();
    }
}
```

**应用场景**：
- `ArrayList`, `HashMap` 的 clone()
- 游戏中的怪物复制
- 文档模板复制

**项目代码**：`prototype/` 目录

---

## 3. 结构型模式 (Structural)

结构型模式关注**类和对象的组合**，帮助我们构建更大的结构。

### 3.1 适配器模式 (Adapter)

**意图**：将一个类的接口转换成客户期望的另一个接口。

**UML结构**：
```
    客户期望的接口              现有的不同接口
    ┌─────────────┐           ┌─────────────┐
    │   Target    │           │   Adaptee   │
    ├─────────────┤           ├─────────────┤
    │ + request() │           │ + specific()│
    └──────┬──────┘           └──────┬──────┘
           │                         │
           │                         │
           ▼                         │
    ┌─────────────────┐              │
    │     Adapter     │──────────────┘
    ├─────────────────┤
    │ - adaptee       │
    ├─────────────────┤
    │ + request()─────┼─── adaptee.specificRequest()
    └─────────────────┘
```

**核心实现**：
```java
// 目标接口
public interface MediaPlayer {
    void play(String filename);
}

// 现有类（不兼容的接口）
public class AdvancedMediaPlayer {
    public void playVlc(String filename) { }
    public void playMp4(String filename) { }
}

// 适配器
public class MediaAdapter implements MediaPlayer {
    private AdvancedMediaPlayer advancedPlayer;

    @Override
    public void play(String filename) {
        if (filename.endsWith(".vlc")) {
            advancedPlayer.playVlc(filename);
        } else if (filename.endsWith(".mp4")) {
            advancedPlayer.playMp4(filename);
        }
    }
}
```

**应用场景**：
- JDK: `InputStreamReader` 适配 `Reader`
- Spring: `HandlerAdapter`
- 第三方库集成

**项目代码**：`adapter/` 目录

---

### 3.2 桥接模式 (Bridge)

**意图**：将抽象部分与实现部分分离，使它们可以独立变化。

**UML结构**：
```
         抽象部分                    实现部分
    ┌─────────────────┐        ┌─────────────────┐
    │  MagicWeapon    │◇───────│MagicWeaponImpl  │
    │                 │        │  <<interface>>  │
    ├─────────────────┤        ├─────────────────┤
    │ + wield()       │        │ + wieldImpl()   │
    │ + swing()       │        │ + swingImpl()   │
    │ + repair()      │        └────────┬────────┘
    └────────┬────────┘                 │
             │              ┌───────────┼───────────┐
             ▼              ▼           ▼           ▼
    ┌─────────────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐
    │FlyingMagicWeapon│ │SoulEat │ │Blinding │ │  ...   │
    └─────────────────┘ └─────────┘ └─────────┘ └─────────┘
             │              │           │
             ▼              ▼           ▼
    ┌─────────────────┐ ┌─────────────────────────┐
    │ Excalibur       │ │ Mjollnir, Stormbringer │
    │ (武器+能力组合)  │ │                         │
    └─────────────────┘ └─────────────────────────┘
```

**核心实现**：
```java
// 实现部分接口
public interface MagicWeaponImpl {
    void wieldImpl();
    void swingImpl();
    void unwieldImpl();
}

// 具体实现
public class SoulEatingMagicWeaponImpl implements MagicWeaponImpl {
    @Override
    public void wieldImpl() { /* ... */ }
    @Override
    public void swingImpl() { /* ... */ }
    @Override
    public void unwieldImpl() { /* ... */ }
}

// 抽象部分
public abstract class MagicWeapon {
    protected MagicWeaponImpl impl;

    public MagicWeapon(MagicWeaponImpl impl) {
        this.impl = impl;
    }

    public abstract void wield();
    public abstract void swing();
    public abstract void unwield();
}

// 具体抽象
public class SoulEatingMagicWeapon extends MagicWeapon {
    public SoulEatingMagicWeapon(MagicWeaponImpl impl) {
        super(impl);
    }

    @Override
    public void wield() {
        impl.wieldImpl();
    }
    // ...
}
```

**应用场景**：
- JDBC驱动架构
- AWT跨平台GUI
- 消息发送（短信、邮件、微信）+ 发送渠道组合

**项目代码**：`bridge/` 目录

---

### 3.3 组合模式 (Composite)

**意图**：将对象组合成树形结构以表示"部分-整体"的层次结构。

**UML结构**：
```
            ┌─────────────┐
            │  Component │
            │ <<abstract>>│
            ├─────────────┤
            │ + operation()│
            └──────┬──────┘
                   │
       ┌───────────┴───────────┐
       │                       │
       ▼                       ▼
┌─────────────┐         ┌─────────────┐
│    Leaf     │         │  Composite  │
├─────────────┤         ├─────────────┤
│ + operation │         │ + operation │
└─────────────┘         │ + add()     │
                        │ + remove() │
                        │ + getChild()│
                        └─────────────┘
                                │
                                ▼
                        ┌─────────────┐
                        │    Leaf     │
                        └─────────────┘
```

**核心实现**：
```java
public abstract class FileSystemComponent {
    protected String name;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    public abstract int getSize();
    public abstract void print(String indent);
}

public class File extends FileSystemComponent {
    private int size;

    public File(String name, int size) {
        super(name);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name + " (" + size + "KB)");
    }
}

public class Folder extends FileSystemComponent {
    private List<FileSystemComponent> children = new ArrayList<>();

    public Folder(String name) {
        super(name);
    }

    public void add(FileSystemComponent component) {
        children.add(component);
    }

    public void remove(FileSystemComponent component) {
        children.remove(component);
    }

    @Override
    public int getSize() {
        return children.stream().mapToInt(FileSystemComponent::getSize).sum();
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + name + " (folder)");
        for (FileSystemComponent child : children) {
            child.print(indent + "  ");
        }
    }
}
```

**应用场景**：
- 文件系统
- GUI组件树
- 组织架构
- XML/HTML DOM

**项目代码**：`composite/` 目录

---

### 3.4 装饰器模式 (Decorator)

**意图**：动态地给对象添加额外的职责。

**UML结构**：
```
┌───────────────────────┐
│   <<interface>>       │
│     Component         │
├───────────────────────┤
│ + operation()          │
└───────────┬───────────┘
            │
    ┌───────┴───────┐
    │               │
    ▼               ▼
┌───────────┐ ┌───────────────────────┐
│ Concrete  │ │      Decorator        │
│Component  │ │   <<abstract>>        │
├───────────┤ ├───────────────────────┤
│+ operation│ │- component: Component│
└───────────┘ ├───────────────────────┤
              │ + operation()         │
              └───────────┬───────────┘
                          │
        ┌─────────────────┼─────────────────┐
        ▼                 ▼                 ▼
  ┌───────────┐     ┌───────────┐     ┌───────────┐
  │DecoratorA │     │DecoratorB │     │DecoratorC │
  │(增强功能1)│     │(增强功能2)│     │(增强功能3)│
  └───────────┘     └───────────┘     └───────────┘
```

**核心实现**：
```java
public interface Coffee {
    int getCost();
    String getDescription();
}

public class SimpleCoffee implements Coffee {
    @Override
    public int getCost() { return 10; }
    @Override
    public String getDescription() { return "Simple Coffee"; }
}

public class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    @Override
    public int getCost() { return coffee.getCost(); }
    @Override
    public String getDescription() { return coffee.getDescription(); }
}

public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public int getCost() { return super.getCost() + 5; }
    @Override
    public String getDescription() {
        return super.getDescription() + ", Milk";
    }
}

// 使用
Coffee coffee = new SimpleCoffee();
coffee = new MilkDecorator(coffee);
coffee = new SugarDecorator(coffee);
```

**应用场景**：
- Java I/O: `BufferedInputStream`, `GzipInputStream`
- `HttpServletRequestWrapper`
- `TransactionAwareDataSourceProxy`
- 日志、缓存、权限等横切关注点

**项目代码**：`decorator/` 目录

---

### 3.5 外观模式 (Facade)

**意图**：为复杂的子系统提供一个统一的接口。

**UML结构**：
```
         ┌─────────────────────────────────────┐
         │              Facade                │
         ├─────────────────────────────────────┤
         │ - subsystem1: Subsystem1           │
         │ - subsystem2: Subsystem2           │
         │ - subsystem3: Subsystem3           │
         ├─────────────────────────────────────┤
         │ + operation()                       │
         └─────────────────────────────────────┘
                       │
       ┌───────────────┼───────────────┐
       │               │               │
       ▼               ▼               ▼
  ┌─────────┐    ┌─────────┐    ┌─────────┐
  │SubSystem│    │SubSystem│    │SubSystem│
  │    1    │    │    2    │    │    3    │
  └─────────┘    └─────────┘    └─────────┘
```

**核心实现**：
```java
public class DwarvenGoldmineFacade {
    private final List<DwarvenMineWorker> workers;

    public DwarvenGoldmineFacade() {
        workers = Arrays.asList(
            new DwarvenGoldDigger(),
            new DwarvenCartOperator(),
            new DwarvenTunnelDigger()
        );
    }

    public void newDay() {
        makeActions(workers, Action.WAKE_UP, Action.GO_TO_MINE);
    }

    public void endDay() {
        makeActions(workers, Action.GO_HOME, Action.GO_TO_SLEEP);
    }

    public void mineGold() {
        makeActions(workers, Action.WORK);
    }

    private void makeActions(List<DwarvenMineWorker> workers,
                             Action... actions) {
        for (DwarvenMineWorker worker : workers) {
            worker.executeActions(actions);
        }
    }
}

// 使用：客户端只需与外观交互
DwarvenGoldmineFacade facade = new DwarvenGoldmineFacade();
facade.newDay();
facade.mineGold();
facade.endDay();
```

**应用场景**：
- Spring JDBC Template
- 第三方SDK封装
- 复杂系统初始化

**项目代码**：`facade/` 目录

---

### 3.6 享元模式 (Flyweight)

**意图**：利用共享技术有效地支持大量细粒度的对象。

**UML结构**：
```
┌─────────────────────────────────────────┐
│           FlyweightFactory              │
├─────────────────────────────────────────┤
│ - flyweights: Map<String, Flyweight>    │
├─────────────────────────────────────────┤
│ + getFlyweight(key): Flyweight          │
└─────────────────┬───────────────────────┘
                  │
                  │ creates &
                  │ returns
                  ▼
┌─────────────────────────────────────────┐
│            <<interface>>                │
│            Flyweight                    │
├─────────────────────────────────────────┤
│ + operation(extrinsic)                  │
└─────────────────┬───────────────────────┘
                  │
    ┌─────────────┴─────────────┐
    │                           │
    ▼                           ▼
┌───────────┐           ┌───────────┐
│ConcreteFly│           │UnsharedCon│
│  weight   │           │  creteFly │
│(共享状态) │           │  weight   │
└───────────┘           └───────────┘
```

**核心实现**：
```java
public interface Flyweight {
    void operation(String extrinsicState);
}

public class FlyweightFactory {
    private Map<String, Flyweight> flyweights = new HashMap<>();

    public Flyweight getFlyweight(String key) {
        if (!flyweights.containsKey(key)) {
            flyweights.put(key, new ConcreteFlyweight(key));
        }
        return flyweights.get(key);
    }
}

public class ConcreteFlyweight implements Flyweight {
    private String intrinsicState;  // 可共享的内部状态

    public ConcreteFlyweight(String key) {
        this.intrinsicState = key;
    }

    @Override
    public void operation(String extrinsicState) {
        // extrinsicState是每个对象特有的外部状态
    }
}
```

**应用场景**：
- String常量池
- 线程池、数据库连接池
- 大量相似对象的场景（如文档中的字符）

**项目代码**：`flyweight/` 目录

---

### 3.7 代理模式 (Proxy)

**意图**：为另一个对象提供一个替身或占位符以控制对它的访问。

**UML结构**：
```
┌─────────────────────────────────────────┐
│             <<interface>>               │
│              Subject                    │
├─────────────────────────────────────────┤
│ + request()                             │
└────────────────┬────────────────────────┘
                 │
       ┌─────────┴─────────┐
       │                   │
       ▼                   ▼
┌─────────────┐     ┌─────────────┐
│    Proxy    │     │   Real      │
│             │     │   Subject   │
├─────────────┤     ├─────────────┤
│- realSubject│     │+ request()  │
├─────────────┤     └─────────────┘
│+ request()  │
│ (控制访问)   │
└─────────────┘
```

**核心实现**：
```java
public interface Image {
    void display();
}

public class RealImage implements Image {
    private String filename;

    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();
    }

    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }

    private void loadFromDisk() {
        System.out.println("Loading " + filename);
    }
}

public class ImageProxy implements Image {
    private RealImage realImage;
    private String filename;

    public ImageProxy(String filename) {
        this.filename = filename;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);  // 延迟加载
        }
        realImage.display();
    }
}

// JDK动态代理
public class PerformanceInvocationHandler implements InvocationHandler {
    private Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        long start = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        System.out.println(method.getName() + " took " +
            (System.currentTimeMillis() - start) + "ms");
        return result;
    }
}
```

**应用场景**：
- Spring AOP
- Hibernate延迟加载
- 远程代理（RMI）
- 保护代理、虚代理

**项目代码**：`proxy/` 目录

---

## 4. 行为型模式 (Behavioral)

行为型模式关注**对象之间的通信和职责分配**。

### 4.1 责任链模式 (Chain of Responsibility)

**意图**：将请求沿着处理者链传递，直到有一个处理者处理它。

**UML结构**：
```
┌─────────────────────────────────┐
│      <<interface>>              │
│      Handler                    │
├─────────────────────────────────┤
│ + setNext(handler)              │
│ + handle(request)               │
└─────────────┬───────────────────┘
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌───────────┐     ┌───────────┐
│ HandlerA  │     │ HandlerB  │
├───────────┤     ├───────────┤
│+ handle() │────▶│+ handle() │
└───────────┘     └───────────┘
```

**核心实现**：
```java
public interface Handler {
    Handler setNext(Handler handler);
    boolean handle(String request);
}

public abstract class AbstractHandler implements Handler {
    private Handler next;

    @Override
    public Handler setNext(Handler handler) {
        this.next = handler;
        return handler;
    }

    @Override
    public boolean handle(String request) {
        if (next != null) {
            return next.handle(request);
        }
        return false;
    }
}

public class OrcOfficer extends AbstractHandler {
    @Override
    public boolean handle(String request) {
        if (request.equals("attack")) {
            System.out.println("OrcOfficer will attack!");
            return true;
        }
        return super.handle(request);
    }
}

// 使用
Handler chain = new OrcOfficer();
chain.setNext(new OrcCommander()).setNext(new OrcCaptain());
chain.handle("attack");
```

**应用场景**：
- Servlet过滤器链
- Spring Security过滤器链
- 日志级别处理
- 审批流程

**项目代码**：`chain/` 目录

---

### 4.2 命令模式 (Command)

**意图**：将请求封装成对象，从而允许参数化和排队请求。

**UML结构**：
```
┌─────────────────────────────────┐
│      <<interface>>              │
│       Command                   │
├─────────────────────────────────┤
│ + execute()                     │
│ + undo()                        │
└─────────────┬───────────────────┘
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌───────────┐     ┌───────────┐
│CommandA   │     │CommandB   │
├───────────┤     ├───────────┤
│+ execute()│     │+ execute()│
│+ undo()   │     │+ undo()   │
└───────────┘     └───────────┘
        │
        │ creates
        ▼
┌─────────────────────────────────┐
│           Invoker               │
├─────────────────────────────────┤
│ - commands: List<Command>       │
├─────────────────────────────────┤
│ + executeCommand()              │
│ + undoCommand()                 │
└─────────────────────────────────┘
```

**核心实现**：
```java
public interface Command {
    void execute();
    void undo();
}

public class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}

public class RemoteControl {
    private List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void executeAll() {
        commands.forEach(Command::execute);
    }

    public void undoAll() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
}
```

**应用场景**：
- GUI按钮/菜单
- 撤销/重做功能
- 事务管理
- 宏录制

**项目代码**：`command/` 目录

---

### 4.3 迭代器模式 (Iterator)

**意图**：提供一种顺序访问集合元素的方法，而不暴露其底层表示。

**UML结构**：
```
┌─────────────────────────────────┐
│      <<interface>>              │
│      Iterator<E>                │
├─────────────────────────────────┤
│ + hasNext(): boolean            │
│ + next(): E                     │
└─────────────────────────────────┘
              ▲
              │ returns
┌─────────────┴─────────────────────┐
│      <<interface>>               │
│       Container                  │
├─────────────────────────────────┤
│ + getIterator(): Iterator        │
└─────────────────────────────────┘
              ▲
              │ creates
┌─────────────┴─────────────────────┐
│        ConcreteContainer          │
├─────────────────────────────────┤
│ + getIterator(): Iterator        │
└─────────────────────────────────┘
```

**核心实现**：
```java
public interface Iterator<T> {
    boolean hasNext();
    T next();
}

public class ArrayIterator<T> implements Iterator<T> {
    private T[] array;
    private int position = 0;

    public ArrayIterator(T[] array) {
        this.array = array;
    }

    @Override
    public boolean hasNext() {
        return position < array.length;
    }

    @Override
    public T next() {
        return array[position++];
    }
}

public interface Container<T> {
    Iterator<T> getIterator();
}

public class ArrayContainer<T> implements Container<T> {
    private T[] array;

    public ArrayContainer(T[] array) {
        this.array = array;
    }

    @Override
    public Iterator<T> getIterator() {
        return new ArrayIterator<>(array);
    }
}
```

**应用场景**：
- JDK: `Iterator`, `ListIterator`
- `Enumeration`
- Guava, Apache Commons迭代工具

**项目代码**：`iterator/` 目录

---

### 4.4 中介者模式 (Mediator)

**意图**：用一个中介对象来封装一系列的对象交互。

**UML结构**：
```
┌─────────────────────────────────────────┐
│             Mediator                    │
│        <<interface>>                    │
├─────────────────────────────────────────┤
│ + notifyColleague()                    │
└─────────────────┬───────────────────────┘
                  │
    ┌─────────────┴─────────────┐
    │                           │
    ▼                           ▼
┌───────────┐             ┌───────────┐
│ Colleague1│◀───────────▶│ Colleague2│
└───────────┘             └───────────┘
    │                           │
    └─────────────┬─────────────┘
                  │
                  ▼
         ┌───────────────┐
         │ConcreteMediator│
         └───────────────┘
```

**核心实现**：
```java
public interface Mediator {
    void notifyColleague(Colleague colleague, String event);
}

public class ConcreteMediator implements Mediator {
    private Colleague1 colleague1;
    private Colleague2 colleague2;

    public void setColleague1(Colleague1 colleague1) {
        this.colleague1 = colleague1;
    }

    public void setColleague2(Colleague2 colleague2) {
        this.colleague2 = colleague2;
    }

    @Override
    public void notifyColleague(Colleague colleague, String event) {
        if (colleague == colleague1) {
            colleague2.react(event);
        } else if (colleague == colleague2) {
            colleague1.react(event);
        }
    }
}

public abstract class Colleague {
    protected Mediator mediator;

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }
}

public class Colleague1 extends Colleague {
    public Colleague1(Mediator mediator) {
        super(mediator);
    }

    public void doSomething() {
        mediator.notifyColleague(this, "event");
    }

    public void react(String event) {
        System.out.println("Colleague1 reacts to " + event);
    }
}
```

**应用场景**：
- GUI对话框组件通信
- 航空交通管制系统
- 聊天室消息转发

**项目代码**：`mediator/` 目录

---

### 4.5 备忘录模式 (Memento)

**意图**：在不破坏封装性的情况下，捕获对象的内部状态并保存。

**UML结构**：
```
┌───────────┐      creates       ┌───────────┐
│  Origin   │───────────────────▶│  Memento  │
│   ator    │                    │ (快照)    │
└─────┬─────┘                    └─────┬─────┘
      │                               │
      │ stores                        │
      │                               │
      │◀──────────────────────────────│
      │        restores               │
      └───────────────────────────────┘
              via caretaker
```

**核心实现**：
```java
public class Memento {
    private final String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}

public class Originator {
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public Memento save() {
        return new Memento(state);
    }

    public void restore(Memento memento) {
        this.state = memento.getState();
    }
}

public class Caretaker {
    private List<Memento> mementos = new ArrayList<>();

    public void addMemento(Memento memento) {
        mementos.add(memento);
    }

    public Memento getMemento(int index) {
        return mementos.get(index);
    }
}
```

**应用场景**：
- 撤销/恢复功能
- 事务回滚
- 检查点保存

**项目代码**：`memento/` 目录

---

### 4.6 观察者模式 (Observer)

**意图**：定义对象之间的一对多依赖关系，当一个对象改变时，所有依赖它的对象都会收到通知。

**UML结构**：
```
┌─────────────────────────────────────┐
│            Subject                  │
├─────────────────────────────────────┤
│ + attach(observer)                  │
│ + detach(observer)                  │
│ + notify()                          │
└─────────────────┬───────────────────┘
                  │
                  ▼
┌─────────────────────────────────────┐
│      <<interface>>                  │
│         Observer                    │
├─────────────────────────────────────┤
│ + update(subject)                   │
└─────────────────────────────────────┘
                  ▲
        ┌─────────┴─────────┐
        │                   │
        ▼                   ▼
  ┌───────────┐       ┌───────────┐
  │ ObserverA │       │ ObserverB │
  └───────────┘       └───────────┘
```

**核心实现**：
```java
public interface Observer<E> {
    void added(E element);
}

public class Observable<E> {
    private List<Observer<E>> observers = new ArrayList<>();

    public void addObserver(Observer<E> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<E> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(E element) {
        for (Observer<E> observer : observers) {
            observer.added(element);
        }
    }
}

public class ObservableSet<E> extends Observable<E> {
    private Set<E> delegate = new HashSet<>();

    public void add(E element) {
        delegate.add(element);
        notifyObservers(element);  // 变化时通知
    }
}

// 使用
ObservableSet<Integer> set = new ObservableSet<>();
set.addObserver(element -> System.out.println("Added: " + element));
set.add(1);  // 打印: Added: 1
```

**应用场景**：
- JDK: `Observer`/`Observable` (已废弃)
- Guava `EventBus`
- Spring `ApplicationEvent`
- MVC架构

**项目代码**：`observer/` 目录

---

### 4.7 状态模式 (State)

**意图**：允许对象在内部状态改变时改变它的行为。

**UML结构**：
```
┌─────────────────────────────────┐
│           Context               │
├─────────────────────────────────┤
│ - state: State                  │
├─────────────────────────────────┤
│ + setState(state)               │
│ + request()                     │
└─────────────┬───────────────────┘
              │ delegates
              ▼
┌─────────────────────────────────┐
│        <<interface>>            │
│           State                 │
├─────────────────────────────────┤
│ + handle()                      │
└─────────────┬───────────────────┘
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌───────────┐     ┌───────────┐
│ StateA    │     │ StateB    │
├───────────┤     ├───────────┤
│+ handle()│────▶│+ handle() │
└───────────┘     └───────────┘
```

**核心实现**：
```java
public interface State {
    void insertCoin();
    void ejectCoin();
    void turnCrank();
    void dispense();
}

public class HasCoinState implements State {
    private VendingMachine machine;

    public HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    @Override
    public void insertCoin() {
        System.out.println("Coin already inserted");
    }

    @Override
    public void ejectCoin() {
        System.out.println("Coin returned");
        machine.setState(machine.getNoCoinState());
    }

    @Override
    public void turnCrank() {
        System.out.println("Crank turned");
        machine.setState(machine.getSoldState());
    }

    @Override
    public void dispense() {
        // 不可能
    }
}

public class VendingMachine {
    private State noCoinState;
    private State hasCoinState;
    private State soldState;
    private State state;

    public VendingMachine() {
        noCoinState = new NoCoinState(this);
        hasCoinState = new HasCoinState(this);
        soldState = new SoldState(this);
        state = noCoinState;
    }

    // ...
}
```

**应用场景**：
- 状态机实现
- 订单流程（待付款、已付款、已发货）
- 游戏状态（开始、运行、暂停、结束）

**项目代码**：`state/` 目录

---

### 4.8 策略模式 (Strategy)

**意图**：定义一系列算法，把它们一个个封装起来，并使它们可以相互替换。

**UML结构**：
```
┌─────────────────────────────────┐
│          Context               │
├─────────────────────────────────┤
│ - strategy: Strategy             │
├─────────────────────────────────┤
│ + setStrategy(strategy)         │
│ + execute()                     │
└─────────────┬───────────────────┘
              │
              │ uses
              ▼
┌─────────────────────────────────┐
│        <<interface>>            │
│          Strategy               │
├─────────────────────────────────┤
│ + algorithm()                   │
└─────────────┬───────────────────┘
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌───────────┐     ┌───────────┐
│StrategyA  │     │StrategyB  │
├───────────┤     ├───────────┤
│+algorithm│     │+algorithm│
└───────────┘     └───────────┘
```

**核心实现**：
```java
public interface SortStrategy<T> {
    void sort(List<T> list);
}

public class BubbleSort<T> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        // 冒泡排序实现
    }
}

public class QuickSort<T> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list) {
        // 快速排序实现
    }
}

public class Sorter<T> {
    private SortStrategy<T> strategy;

    public Sorter(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(SortStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void sort(List<T> list) {
        strategy.sort(list);
    }
}

// 使用
List<Integer> list = Arrays.asList(3, 1, 2);
Sorter<Integer> sorter = new Sorter<>(new BubbleSort<>());
sorter.sort(list);
sorter.setStrategy(new QuickSort<>());
sorter.sort(list);
```

**应用场景**：
- JDK: `Comparator`
- `Arrays.sort()`, `Collections.sort()`
- 支付方式选择
- 出行路线规划

**项目代码**：`strategy/` 目录

---

### 4.9 模板方法模式 (Template Method)

**意图**：定义算法骨架，将某些步骤延迟到子类。

**UML结构**：
```
┌─────────────────────────────────┐
│     <<abstract>>                │
│      AbstractClass              │
├─────────────────────────────────┤
│ + templateMethod()              │
│   {                             │
│     step1();                    │
│     step2();                    │
│     hook();                     │
│   }                             │
├─────────────────────────────────┤
│ # step1()                       │
│ # step2()                       │
│ # hook()                        │
└─────────────┬───────────────────┘
              │
    ┌─────────┴─────────┐
    │                   │
    ▼                   ▼
┌───────────┐     ┌───────────┐
│ConcreteA  │     │ConcreteB  │
├───────────┤     ├───────────┤
│+ step1()  │     │+ step1()  │
│+ hook()   │     │+ step2()  │
└───────────┘     └───────────┘
```

**核心实现**：
```java
public abstract class Game {
    abstract void initialize();
    abstract void start();
    abstract void end();

    // 模板方法
    public final void play() {
        initialize();
        start();
        while (!isEnd()) {
            update();
        }
        end();
    }

    // 钩子方法 - 子类可以选择性覆盖
    protected boolean isEnd() {
        return false;
    }

    abstract void update();
}

public class Chess extends Game {
    @Override
    void initialize() {
        System.out.println("Chess Game Initialized");
    }

    @Override
    void start() {
        System.out.println("Chess Game Started");
    }

    @Override
    void end() {
        System.out.println("Chess Game Finished");
    }

    @Override
    void update() {
        System.out.println("Chess game updated");
    }
}
```

**应用场景**：
- JDK: `InputStream`, `AbstractList`
- Spring `JdbcTemplate`
- Servlet的 `service()` 方法
- 测试框架的 `setUp()`/`tearDown()`

**项目代码**：`template-method/` 目录

---

### 4.10 访问者模式 (Visitor)

**意图**：将算法与对象结构分离。

**UML结构**：
```
                    ┌─────────────┐
                    │ ElementA   │
                    └──────┬──────┘
                           │ accepts
                           ▼
                    ┌─────────────┐
                    │  Visitor    │
                    │<<interface>>│
                    ├─────────────┤
                    │+visitA()    │
                    │+visitB()    │
                    └──────┬──────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
 ┌────────────┐    ┌────────────┐    ┌────────────┐
 │ ConcreteV1 │    │ ConcreteV2 │    │ ConcreteV3 │
 └────────────┘    └────────────┘    └────────────┘
```

**核心实现**：
```java
public interface Visitor {
    void visitDot(Dot dot);
    void visitCircle(Circle circle);
    void visitRectangle(Rectangle rectangle);
}

public interface Shape {
    void accept(Visitor visitor);
}

public class Dot implements Shape {
    private int x, y;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDot(this);
    }
}

public class Circle implements Shape {
    private int radius;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitCircle(this);
    }
}

public class AreaCalculator implements Visitor {
    private double area = 0;

    @Override
    public void visitDot(Dot dot) {
        // 点没有面积
    }

    @Override
    public void visitCircle(Circle circle) {
        area += Math.PI * circle.getRadius() * circle.getRadius();
    }

    @Override
    public void visitRectangle(Rectangle rectangle) {
        area += rectangle.getWidth() * rectangle.getHeight();
    }

    public double getArea() {
        return area;
    }
}
```

**应用场景**：
- 编译器AST处理
- 文件系统遍历
- XML/JSON解析

**项目代码**：`visitor/` 目录

---

## 5. 并发模式 (Concurrency)

### 5.1 双检查锁定 (Double-Checked Locking)

**意图**：在多线程环境下安全地延迟初始化单例。

**核心实现**：
```java
public class Singleton {
    // 使用volatile防止指令重排
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {  // 第一次检查
            synchronized (Singleton.class) {
                if (instance == null) {  // 第二次检查
                    instance = new Singleton();  // 可能发生指令重排
                }
            }
        }
        return instance;
    }
}
```

**项目代码**：`double-checked-locking/` 目录

---

### 5.2 线程池 (Thread Pool)

**意图**：重用一组线程，减少创建/销毁线程的开销。

**UML结构**：
```
┌─────────────────────────────────┐
│         ThreadPool              │
├─────────────────────────────────┤
│ - workers: List<Worker>         │
│ - taskQueue: BlockingQueue      │
├─────────────────────────────────┤
│ + execute(task)                 │
│ + shutdown()                    │
└─────────────────────────────────┘
```

**核心实现**：
```java
public class ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final List<Worker> workers;

    public ThreadPool(int poolSize) {
        taskQueue = new LinkedBlockingQueue<>();
        workers = new ArrayList<>();

        for (int i = 0; i < poolSize; i++) {
            Worker worker = new Worker(taskQueue);
            worker.start();
            workers.add(worker);
        }
    }

    public void execute(Runnable task) {
        taskQueue.offer(task);
    }

    public void shutdown() {
        workers.forEach(Worker::stop);
    }
}
```

**项目代码**：`thread-pool/` 目录

---

### 5.3 生产者-消费者 (Producer-Consumer)

**意图**：解耦生产者和消费者的并发执行。

**UML结构**：
```
    ┌─────────────┐         ┌─────────────┐
    │  Producer   │────────▶│             │
    └─────────────┘         │  Blocking  │
                            │    Queue   │
    ┌─────────────┐         │             │
    │  Consumer   │◀────────│             │
    └─────────────┘         └─────────────┘
```

**核心实现**：
```java
public class ItemQueue {
    private final Queue<Item> queue = new LinkedList<>();
    private final int capacity;

    public ItemQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(Item item) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait();
        }
        queue.offer(item);
        notifyAll();
    }

    public synchronized Item take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        Item item = queue.poll();
        notifyAll();
        return item;
    }
}
```

**项目代码**：`producer-consumer/` 目录

---

### 5.4 异步方法调用 (Async Method Invocation)

**意图**：不阻塞等待方法完成，而是通过回调或Future获取结果。

**核心实现**：
```java
public interface AsyncResult<T> {
    T get();
    boolean isCompleted();
}

public interface AsyncExecutor {
    <T> AsyncResult<T> execute(Callable<T> task);
    <T> AsyncResult<T> execute(Callable<T> task, AsyncCallback<T> callback);
}

public class ThreadAsyncExecutor implements AsyncExecutor {
    private final Map<Integer, FutureTask<?>> tasks = new ConcurrentHashMap<>();

    @Override
    public <T> AsyncResult<T> execute(Callable<T> task) {
        FutureTask<T> future = new FutureTask<>(task);
        tasks.put(future.hashCode(), future);
        new Thread(future).start();
        return () -> {
            while (!future.isDone()) {
                Thread.sleep(10);
            }
            return future.get();
        };
    }
}
```

**项目代码**：`async-method-invocation/` 目录

---

### 5.5 读写锁 (Reader-Writer Lock)

**意图**：允许多个读者同时读，但写者独占访问。

**核心实现**：
```java
public class ReadWriteLock {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    public synchronized void lockRead() throws InterruptedException {
        while (writers > 0 || writeRequests > 0) {
            wait();
        }
        readers++;
    }

    public synchronized void unlockRead() {
        readers--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        writeRequests++;
        while (readers > 0 || writers > 0) {
            wait();
        }
        writeRequests--;
        writers++;
    }

    public synchronized void unlockWrite() {
        writers--;
        notifyAll();
    }
}
```

**项目代码**：`reader-writer/` 目录

---

### 5.6 回调 (Callback)

**意图**：将代码作为参数传递，在特定事件发生时调用。

**核心实现**：
```java
public interface Callback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}

public class AsyncService {
    public <T> void executeAsync(Callable<T> task, Callback<T> callback) {
        new Thread(() -> {
            try {
                T result = task.call();
                callback.onSuccess(result);
            } catch (Exception e) {
                callback.onError(e);
            }
        }).start();
    }
}

// 使用
asyncService.executeAsync(
    () -> {
        // 耗时操作
        return "result";
    },
    new Callback<String>() {
        @Override
        public void onSuccess(String result) {
            System.out.println("Success: " + result);
        }

        @Override
        public void onError(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
);
```

**项目代码**：`callback/` 目录

---

## 6. 设计模式对比

### 6.1 创建型模式对比

| 模式 | 特点 | 适用场景 |
|------|------|---------|
| 单例 | 一个实例 | 全局唯一对象 |
| 工厂方法 | 延迟到子类创建 | 创建过程可能变化 |
| 抽象工厂 | 创建一系列对象 | 产品族 |
| 建造者 | 分步构建复杂对象 | 对象有多个可选参数 |
| 原型 | 复制现有对象 | 创建成本高 |

### 6.2 结构型模式对比

| 模式 | 特点 | 适用场景 |
|------|------|---------|
| 适配器 | 接口转换 | 集成不兼容接口 |
| 桥接 | 分离抽象与实现 | 多维度变化 |
| 组合 | 树形结构 | 部分-整体层次 |
| 装饰器 | 动态添加功能 | 功能可选组合 |
| 外观 | 简化接口 | 封装复杂子系统 |
| 享元 | 共享细粒度对象 | 对象数量大 |
| 代理 | 间接访问 | 访问控制 |

### 6.3 行为型模式对比

| 模式 | 特点 | 适用场景 |
|------|------|---------|
| 责任链 | 链式处理请求 | 多个对象处理请求 |
| 命令 | 请求封装 | 撤销/重做 |
| 迭代器 | 顺序访问 | 集合遍历 |
| 中介者 | 集中交互 | 对象网状通信 |
| 备忘录 | 状态快照 | 撤销机制 |
| 观察者 | 一对多通知 | 事件监听 |
| 状态 | 状态切换行为 | 状态机 |
| 策略 | 算法替换 | 多算法选择 |
| 模板方法 | 算法骨架 | 步骤固定，部分变化 |
| 访问者 | 数据与操作分离 | 结构稳定，操作多变 |

---

## 7. 设计模式与SOLID原则

| 设计原则 | 相关设计模式 |
|---------|------------|
| 单一职责 | 组合模式、外观模式 |
| 开闭原则 | 策略模式、装饰器模式、观察者模式 |
| 里氏替换 | 组合模式、策略模式 |
| 接口隔离 | 装饰器模式、代理模式 |
| 依赖倒置 | 工厂方法、模板方法、策略模式 |

---

## 8. 总结

### 学习设计模式的建议

1. **理解而非死记**：理解每个模式解决的问题和使用场景
2. **结合UML**：UML是理解模式结构的好工具
3. **实践应用**：在真实项目中尝试使用设计模式
4. **避免滥用**：不是所有场景都需要设计模式
5. **理解权衡**：每个模式都有优缺点，要权衡使用

### 项目中的设计模式

本项目包含 **30+** 设计模式的实现：

| 类别 | 数量 | 模式 |
|------|------|------|
| 创建型 | 5 | Singleton, Factory Method, Abstract Factory, Builder, Prototype |
| 结构型 | 7 | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy |
| 行为型 | 11 | Chain, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor |
| 并发型 | 6 | Double-Checked Locking, Thread Pool, Producer-Consumer, Async Invocation, Reader-Writer Lock, Callback |

---

*文档版本: 1.0*
*最后更新: 2026-04-07*
