# 第三章：类和接口

> Effective Java Chapter 3: Classes and Interfaces

## 章节概览

本章涵盖 8 个条目（15-22），讨论类和接口的设计原则。

---

## Item 15: Minimize the accessibility of classes and members

### 核心原则

**封装**：信息隐藏，减少模块间的耦合

### 访问级别

| 修饰符 | 访问范围 | 用途 |
|--------|----------|------|
| `private` | 仅本类 | 成员级别最小化 |
| `package-private` | 同包 | 类内部使用 |
| `protected` | 子类 + 同包 | API 契约 |
| `public` | 任意位置 | API |

### 错误示例

```java
// 过度暴露内部实现
public class Stack {
    public Object[] elements;  // 错误！
    public int size;
}
```

### 正确示例

```java
// 正确：封装
public class Stack<E> {
    private E[] elements;
    private int size;

    public List<E> getElements() {  // 返回不可变视图
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
}
```

---

## Item 16: Public classes should use accessor methods, not public fields

### 错误示例

```java
// 直接暴露字段
public class Point {
    public double x;
    public double y;
}
```

### 正确示例

```java
// 使用访问器方法
public class Point {
    private double x;
    private double y;

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}
```

---

## Item 17: Minimize mutability

### 不可变类的五大原则

1. **不提供修改对象状态的方法**
2. **确保类不会被继承**（用 final 或私有构造器）
3. **所有字段用 final**
4. **所有字段用 private**
5. **确保对可变组件的访问是独占的**

### 不可变类的示例

```java
public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // 返回新实例，不修改原对象
    public Complex add(Complex other) {
        return new Complex(re + other.re, im + other.im);
    }

    public double getReal() { return re; }
    public double getImaginary() { return im; }
}
```

### 不可变对象的优势

| 优势 | 说明 |
|------|------|
| 线程安全 | 无需同步 |
| 可共享 | 如 `String`、`BigInteger` |
| 失败原子性 | 状态不会在异常后被破坏 |
| 简单 | 无复杂状态管理 |

### 性能优化

```java
// 缓存哈希码
public final class PhoneNumber {
    private final short areaCode, prefix, lineNum;
    private volatile int hashCode;  // 延迟初始化

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Short.hashCode(areaCode);
            result = 31 * result + Short.hashCode(prefix);
            result = 31 * result + Short.hashCode(lineNum);
            hashCode = result;
        }
        return result;
    }
}
```

---

## Item 18: Favor composition over inheritance

### 继承的问题

```java
// InstrumentedHashSet 通过继承获取 addAll
public class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
}

// 问题：HashSet.addAll() 内部调用 add()
// 导致 addCount 被加了两次！
InstrumentedHashSet<String> s = new InstrumentedHashSet<>();
s.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
System.out.println(s.getAddCount());  // 期望 3，实际 6
```

### 组合方案（包装类 / Decorator 模式）

```java
public class InstrumentedSet<E> {
    private final Set<E> inner;
    private int addCount = 0;

    public InstrumentedSet(Set<E> inner) {
        this.inner = inner;
    }

    public boolean add(E e) {
        addCount++;
        return inner.add(e);
    }

    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return inner.addAll(c);
    }

    public int getAddCount() { return addCount; }
}
```

### 何时可以使用继承？

- 两者之间存在"is-a"关系
- 子类是父类的严格子类型
- 父类的 API 没有缺陷

---

## Item 19: Design and document for inheritance or else prohibit it

### 必要条件

如果类允许子类化，必须文档化：

1. **覆盖方法的自检效果**
2. **回调框架的使用**
3. **受保护方法的作用**

```java
/**
 * @throws UnsupportedOperationException if remove is called
 *         on an unbuilt parser
 */
public abstract class AbstractParser {
    // Template method
    public final void parse() {
        parseHeaders();
        parseBody();
    }

    // Subclasses must override this
    protected abstract void parseHeaders();

    // Default implementation - can be overridden
    protected void parseBody() {
        // default: do nothing
    }
}
```

### 解决方案：私有构造器 + 静态工厂

```java
public class MyCollection {
    private MyCollection() {}  // 禁止继承

    public static MyCollection create() {
        return new MyCollection();
    }
}
```

---

## Item 20: Prefer interfaces to abstract classes

### 接口的优势

```java
// 接口：任何非 final 类都可以实现
public interface Serializable {
    // 可序列化契约
}

// 抽象类：只能单继承
public abstract class AbstractList<E> {
    // 提供部分实现
}
```

### 接口 + 抽象骨架实现

```java
// 接口定义类型
public interface List<E> {
    void add(E e);
    E get(int index);
    int size();
}

// 骨架实现
public abstract class AbstractList<E> implements List<E> {
    // 通用实现，供用户继承
}
```

### 示例：Comparable 和 AbstractList

```java
public class StringList extends AbstractList<String> {
    // 只需实现 get() 和 size()
    // add() 等方法已有默认实现
}
```

---

## Item 21: Interface only for defining types

### 错误：常量接口

```java
// 错误：常量接口
public interface PhysicalConstants {
    double AVOGADRO = 6.022140857;
    double BOLTZMANN = 1.3806488e-23;
}
```

### 正确做法：枚举或工具类

```java
// 枚举（如果常量相关联）
public enum PhysicalConstants {
    AVOGADRO(6.022140857),
    BOLTZMANN(1.3806488e-23);

    private final double value;
    PhysicalConstants(double value) { this.value = value; }
}

// 工具类（如果常量是独立的）
public class PhysicalConstants {
    private PhysicalConstants() {}  // 防止实例化

    public static final double AVOGADRO = 6.022140857;
    public static final double BOLTZMANN = 1.3806488e-23;
}
```

---

## Item 22: Prefer class hierarchies to tagged classes

### 标签类的问题

```java
// 错误：标签类
class Figure {
    enum Shape { CIRCLE, RECTANGLE; }

    Shape shape;
    double radius;       // for circle
    double length, width; // for rectangle

    double area() {
        switch (shape) {
            case CIRCLE:
                return Math.PI * radius * radius;
            case RECTANGLE:
                return length * width;
            default:
                throw new AssertionError();
        }
    }
}
```

### 类层次方案

```java
// 正确：类层次
abstract class Figure {
    abstract double area();
}

class Circle extends Figure {
    private final double radius;

    Circle(double radius) { this.radius = radius; }
    double area() { return Math.PI * radius * radius; }
}

class Rectangle extends Figure {
    private final double length, width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
    double area() { return length * width; }
}
```

### 类层次的优势

- 代码简洁，每个类只有相关字段
- 编译器检查，所有子类必须有 `area()` 实现
- 易于扩展新类型

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 15 | 最小化类和成员的可访问性 |
| 16 | public 类使用访问方法而非 public 字段 |
| 17 | 最小化可变性（不可变对象） |
| 18 | 组合优于继承 |
| 19 | 继承要设计并文档化，否则禁用 |
| 20 | 接口优于抽象类 |
| 21 | 接口仅用于定义类型 |
| 22 | 类层次优于标签类 |

---

## 设计模式对应

| 设计模式 | 对应 Item |
|----------|-----------|
| Template Method | Item 19 |
| Composition | Item 18 |
| Decorator | Item 18 |
| Factory Method | Item 1, 20 |
| Abstract Factory | Item 20 |

---

## 练习题

1. 分析你项目中的继承使用是否违反了 Item 18
2. 将一个标签类重构为类层次
3. 设计一个不可变类，说明其所有字段为什么是 final
4. 为什么常量不应该放在接口中？
