# 第五章：枚举和注解

> Effective Java Chapter 5: Enums and Annotations

## 章节概览

本章涵盖 4 个条目（34-37），讨论枚举和注解的最佳实践。

---

## Item 34: Use enums instead of int constants

### 枚举的优势

```java
// int常量模式 - 危险！
public static final int APPLE_FUJI = 0;
public static final int APPLE_PIPPIN = 1;
public static final int ORANGE_NAVEL = 2;

// 枚举 - 类型安全
public enum Apple { FUJI, PIPPIN }
public enum Orange { NAVEL, BLOOD }
```

### 枚举的特性

| 特性 | 说明 |
|------|------|
| 编译时类型安全 | 编译器检查 |
| 单例模式天然实现 | 每个枚举常量唯一实例 |
| 可添加方法和字段 | 支持丰富的行为 |
| 可实现接口 | 灵活扩展 |

### 枚举示例：行星

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6);

    private final double mass;
    private final double radius;

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public double surfaceGravity() {
        return G * mass / (radius * radius);
    }
}
```

### 枚举方法实现：Operation

```java
public enum Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    };

    private final String symbol;

    Operation(String symbol) { this.symbol = symbol; }

    public abstract double apply(double x, double y);
}
```

---

## Item 35: Use instance fields instead of ordinal values

### 错误：使用 ordinal()

```java
// 错误：依赖 ordinal() 是脆弱的
public enum Ensemble {
    SOLO, DUET, TRIO, QUARTET, QUINTET;

    public int numberOfMusicians() {
        return ordinal() + 1;  // 危险！
    }
}
```

### 正确：使用实例字段

```java
// 正确：使用实例字段
public enum Ensemble {
    SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5);

    private final int numberOfMusicians;

    Ensemble(int size) {
        this.numberOfMusicians = size;
    }

    public int numberOfMusicians() {
        return numberOfMusicians;
    }
}
```

### 为什么不使用 ordinal()？

- 枚举顺序改变时，ordinal() 返回值也会改变
- 代码难以维护，容易出错
- 无法表示重复值（如 DOUBLE_QUARTET = 8 与 OCTET = 8）

---

## Item 36: Use EnumSet instead of bit fields

### 位域的问题

```java
// 旧式位域 - 危险！
public static final int STYLE_BOLD = 1 << 0;      // 1
public static final int STYLE_ITALIC = 1 << 1;   // 2
public static final int STYLE_UNDERLINE = 1 << 2; // 4
public static final int STYLE_STRIKETHROUGH = 1 << 3; // 8

private int styles;  // 位域
```

### EnumSet 解决方案

```java
public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }

// 类型安全且高效
EnumSet<Style> styles = EnumSet.of(Style.BOLD, Style.ITALIC);
```

### EnumSet 特性

| 特性 | 说明 |
|------|------|
| 内部实现 | long 数组（位向量） |
| 性能 | 与位域相当 |
| 类型安全 | 泛型约束 |
| 简洁 API | addAll(), complementOf() |

### EnumSet 常用操作

```java
EnumSet<Style> set1 = EnumSet.of(Style.BOLD, Style.ITALIC);
EnumSet<Style> set2 = EnumSet.allOf(Style.class);      // 所有
EnumSet<Style> none = EnumSet.noneOf(Style.class);     // 空
EnumSet<Style> range = EnumSet.range(Style.BOLD, Style.STRIKETHROUGH);
EnumSet<Style> complement = EnumSet.complementOf(set1); // 补集
```

---

## Item 37: Use EnumMap instead of ordinal indexing

### 问题：使用 ordinal() 索引数组

```java
// 错误：使用 ordinal() 索引数组
Phase[] phases = Phase.values();
Phase.Transition[][] transitions = new Phase.Transition[3][3];
// ordinal() - 1 魔法数字！
```

### 正确：使用 EnumMap

```java
// 正确：类型安全
Map<Phase, Map<Phase, Transition>> transitionMap = new EnumMap<>(Phase.class);
// 或使用 Stream
Map<Phase, Map<Phase, Transition>> TRANSITION_MAP = Stream.of(values())
    .collect(Collectors.groupingBy(
        t -> t.from,
        () -> new EnumMap<>(Phase.class),
        Collectors.toMap(...)
    ));
```

### EnumMap vs 数组索引

| 方案 | 安全性 | 性能 | 可读性 |
|------|--------|------|--------|
| `array[phase.ordinal()]` | 差 | O(1) | 需要魔法数字 |
| `EnumMap<Phase, V>` | 好 | O(1) | 清晰直观 |

### 典型用法：状态机

```java
public enum Phase {
    SOLID, LIQUID, GAS;

    public enum Transition {
        MELT(SOLID, LIQUID),
        BOIL(LIQUID, GAS),
        // ...

        private final Phase from;
        private final Phase to;

        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        private static final Map<Phase, Map<Phase, Transition>> MAP = ...;

        public static Transition from(Phase from, Phase to) {
            return MAP.get(from).get(to);
        }
    }
}
```

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 34 | 用枚举代替 int 常量 |
| 35 | 用实例字段代替序数值 |
| 36 | 用 EnumSet 代替位域 |
| 37 | 用 EnumMap 代替序数索引 |

---

## 枚举最佳实践

```java
// 1. 枚举天然单例
public enum Elvis {
    INSTANCE;

    public void method() { }
}

// 2. 枚举可实现接口
public enum Operation implements BinaryOperator<Double> {
    PLUS { public Double apply(Double a, Double b) { return a + b; } },
    MINUS { public Double apply(Double a, Double b) { return a - b; } };

    // 通用实现
    @Override
    public Double apply(Double a, Double b) {
        return switch (this) {
            case PLUS -> a + b;
            case MINUS -> a - b;
        };
    }
}

// 3. 策略枚举 - 不同常量不同策略
enum PayrollDay {
    MONDAY(PayType.WEEKDAY),
    SATURDAY(PayType.WEEKEND);

    private final PayType payType;

    PayrollDay(PayType payType) {
        this.payType = payType;
    }

    public double pay(double hours, double rate) {
        return payType.pay(hours, rate);
    }

    private enum PayType {
        WEEKDAY {
            @Override
            double overtimePay(double h, double r) {
                return h <= 8 ? 0 : (h - 8) * r * 1.5;
            }
        },
        WEEKEND {
            @Override
            double overtimePay(double h, double r) {
                return h * r * 1.5;
            }
        };

        abstract double overtimePay(double hours, double rate);

        double pay(double hours, double rate) {
            return hours * rate + overtimePay(hours, rate);
        }
    }
}
```

---

## 注解要点

```java
// 注解本质是接口
public @interface Test {
    long timeout() default 0L;
}

// 注解使用
@Test(timeout = 1000)
public void method() { }

// 常用预定义注解
@Override      // 编译器检查重写
@Deprecated    // 标记废弃
@SuppressWarnings("unchecked")  // 抑制警告
@SafeVarargs   // varargs 安全
```

---

## 练习题

1. 将一个 int 常量枚举重构为 Java 枚举
2. 实现一个 `Operation` 枚举，支持 `PLUS`, `MINUS`, `TIMES`, `DIVIDE`
3. 解释为什么 ordinal() 是危险的
4. 用 EnumMap 实现一个简单的状态转换表
5. 枚举实现策略模式的例子
