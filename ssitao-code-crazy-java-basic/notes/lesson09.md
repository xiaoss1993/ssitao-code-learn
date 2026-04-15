# 第9课：枚举

## 核心概念

### 9.1 枚举的定义
- 枚举是有限的、固定的常量集合
- 使用`enum`关键字定义
- 枚举隐式继承`java.lang.Enum`

### 9.2 枚举特点
- 枚举构造器只能是private或默认
- 枚举实例必须放在最前面
- 枚举可以实现接口

### 9.3 枚举方法
- `values()`：获取所有枚举值
- `valueOf(String)`：根据名称获取枚举值
- `ordinal()`：获取枚举序号
- `name()`：获取枚举名称

## 代码示例

### 示例1：基本枚举定义
```java
public enum Season {
    SPRING,  // 春
    SUMMER,  // 夏
    AUTUMN,  // 秋
    WINTER;  // 冬

    public static void main(String[] args) {
        // 遍历枚举
        for (Season s : Season.values()) {
            System.out.println(s.ordinal() + ": " + s.name());
        }

        // 根据名称获取枚举值
        Season s = Season.valueOf("SPRING");
        System.out.println(s);

        // switch语句使用枚举
        printSeason(Season.SPRING);
    }

    public static void printSeason(Season season) {
        switch (season) {
            case SPRING:
                System.out.println("春天");
                break;
            case SUMMER:
                System.out.println("夏天");
                break;
            case AUTUMN:
                System.out.println("秋天");
                break;
            case WINTER:
                System.out.println("冬天");
                break;
        }
    }
}
```

### 示例2：带属性和方法的枚举
```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6),
    MARS(6.421e+23, 3.3972e6),
    JUPITER(1.9e+27, 7.1492e7),
    SATURN(5.688e+26, 6.0268e7),
    URANUS(8.686e+25, 2.5559e7),
    NEPTUNE(1.024e+26, 2.4746e7);

    private final double mass;   // 质量（千克）
    private final double radius; // 半径（米）

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public double getMass() { return mass; }
    public double getRadius() { return radius; }

    // 计算表面重力
    public double surfaceGravity() {
        final double G = 6.673e-11;
        return G * mass / (radius * radius);
    }

    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }

    public static void main(String[] args) {
        double earthWeight = 70;
        double mass = earthWeight / EARTH.surfaceGravity();
        for (Planet p : Planet.values()) {
            System.out.printf("在%s上，你的体重是%.2f%n",
                p, p.surfaceWeight(mass));
        }
    }
}
```

### 示例3：枚举实现接口
```java
public interface Operation {
    double apply(double x, double y);
}

public enum BasicOperation implements Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    BasicOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() { return symbol; }

    public static void main(String[] args) {
        double x = 10;
        double y = 5;
        for (BasicOperation op : BasicOperation.values()) {
            System.out.printf("%f %s %f = %f%n",
                x, op, y, op.apply(x, y));
        }
    }
}
```

### 示例4：枚举与单例模式
```java
// 枚举实现单例（推荐）
public enum Singleton {
    INSTANCE;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Singleton s1 = Singleton.INSTANCE;
        Singleton s2 = Singleton.INSTANCE;
        System.out.println(s1 == s2);  // true
        s1.setName("单例");
        System.out.println(s2.getName());  // 单例
    }
}
```

### 示例5：枚举描述状态机
```java
public enum OrderStatus {
    // 待支付 -> 已支付 -> 已发货 -> 已完成
    //         -> 已取消
    //                   -> 已收货
    PENDING_PAYMENT("待支付") {
        @Override
        public OrderStatus next() { return PAID; }
    },
    PAID("已支付") {
        @Override
        public OrderStatus next() { return SHIPPED; }
    },
    SHIPPED("已发货") {
        @Override
        public OrderStatus next() { return RECEIVED; }
    },
    RECEIVED("已收货") {
        @Override
        public OrderStatus next() { return COMPLETED; }
    },
    COMPLETED("已完成") {
        @Override
        public OrderStatus next() { return null; }
    },
    CANCELLED("已取消") {
        @Override
        public OrderStatus next() { return null; }
    };

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }

    public abstract OrderStatus next();

    public static void main(String[] args) {
        OrderStatus status = OrderStatus.PENDING_PAYMENT;
        while (status != null) {
            System.out.println(status.getDescription());
            status = status.next();
        }
    }
}
```

## 枚举应用场景

| 场景 | 说明 |
|------|------|
| 状态机 | 订单状态、流程状态 |
| 固定常量 | 星期、月份、季节 |
| 单例模式 | 枚举实现最安全 |
| 策略模式 | 不同枚举值对应不同策略 |

## 常见面试题

1. **枚举为什么是线程安全的？**
   - Java枚举天然线程安全
   - 枚举实例创建是线程安全的

2. **枚举能继承其他类吗？**
   - 不能，枚举已经隐式继承java.lang.Enum
   - 但可以实现接口

3. **枚举和常量类的区别？**
   - 枚举是类，有类型安全
   - 枚举有额外方法、功能

## 练习题

1. 定义一个交通灯枚举（红、黄、绿），实现获取下一个灯
2. 用枚举实现四则运算
3. 使用枚举实现一个简单状态机（订单流程）

## 要点总结

- 枚举是固定的常量集合
- 枚举构造器私有
- 枚举可以实现接口
- 枚举可以带属性和方法
- 枚举是线程安全的
