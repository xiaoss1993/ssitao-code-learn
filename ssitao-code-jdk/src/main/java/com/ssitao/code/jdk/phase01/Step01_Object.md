# 第一阶段：核心语法基础

## 步骤1：Object类 - Java所有类的父类

---

### 1.1 Object类概述

`Object` 是Java中所有类的根父类，所有对象（包括数组）都直接或间接继承自Object。

```java
public class Object {
    // 注册本地方法
    private static native void registerNatives();
    static {
        registerNatives();
    }

    // 返回对象的Class类型
    public final native Class<?> getClass();

    // hashCode值，用于HashMap/HashSet等哈希表
    public native int hashCode();

    // 判断是否相等，默认比较引用地址
    public boolean equals(Object obj) {
        return (this == obj);
    }

    // 创建并返回对象的副本（浅拷贝）
    protected native Object clone() throws CloneNotSupportedException;

    // 返回对象的字符串表示
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    // 唤醒一个等待该对象监视器的线程
    public final native void notify();

    // 唤醒所有等待该对象监视器的线程
    public final native void notifyAll();

    // 让当前线程等待，直到其他线程调用notify()或notifyAll()
    public final native void wait() throws InterruptedException;

    // 带超时时间的wait
    public final native void wait(long timeout) throws InterruptedException;
    public final void wait(long timeout, int nanos) throws InterruptedException {
        // ...
    }

    // 垃圾回收前调用的方法
    protected void finalize() throws Throwable { }
}
```

---

### 1.2 equals() 方法

#### 1.2.1 默认equals()实现

```java
public boolean equals(Object obj) {
    return (this == obj);  // 仅比较引用地址
}
```

#### 1.2.2 自定义equals()的步骤（黄金法则）

1. **检查是否为同一个引用**（this == obj）
2. **检查是否为null**
3. **检查是否为同一个类**（getClass() == obj.getClass()）
4. **比较关键字段**

```java
public class Person {
    private String name;
    private int age;

    @Override
    public boolean equals(Object obj) {
        // 1. 检查是否为同一个引用
        if (this == obj) {
            return true;
        }
        // 2. 检查是否为null
        if (obj == null) {
            return false;
        }
        // 3. 检查是否为同一个类
        if (getClass() != obj.getClass()) {
            return false;
        }
        // 4. 比较关键字段
        Person other = (Person) obj;
        return age == other.age && Objects.equals(name, other.name);
    }
}
```

#### 1.2.3 equals()的五个特性

| 特性 | 描述 |
|------|------|
| 自反性 | `x.equals(x)` 必须返回 `true` |
| 对称性 | `x.equals(y)` 和 `y.equals(x)` 结果一致 |
| 传递性 | `x.equals(y)` 且 `y.equals(z)`，则 `x.equals(z)` |
| 一致性 | 多次调用结果一致 |
| 非空性 | `x.equals(null)` 必须返回 `false` |

---

### 1.3 hashCode() 方法

#### 1.3.1 hashCode的三个黄金法则

1. **一致性**：同一个对象多次调用hashCode()必须返回相同值
2. **相等对象必须相等**：如果 `a.equals(b)` 为 `true`，则 `a.hashCode() == b.hashCode()`
3. **不必不相等则hash不同**：hashCode相同不代表equals为true（哈希碰撞）

#### 1.3.2 不重写hashCode()的问题

```java
// 不重写hashCode会导致HashMap/HashSet失效
public class Person {
    private String name;

    // 只重写了equals，没重写hashCode
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(name, person.name);
    }
    // 缺少 hashCode() 重写！
}

// 使用时出现问题
public class HashCodeProblem {
    public static void main(String[] args) {
        Person p1 = new Person("Alice");
        Person p2 = new Person("Alice");

        System.out.println(p1.equals(p2));  // true (正确)
        System.out.println(p1.hashCode());  // 例如: 1234567
        System.out.println(p2.hashCode());  // 例如: 7654321 (不同!)
        // 违反hashCode黄金法则！
    }
}
```

#### 1.3.3 正确的hashCode()实现模式

```java
public class Person {
    private String name;
    private int age;

    @Override
    public int hashCode() {
        int result = 17;  // 初始质数
        result = 31 * result + age;  // 乘以质数并加字段
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // 同前文
    }
}
```

**为什么用31？**
- 31是质数
- 31 = 32 - 1，JVM会优化为 `i * 31 = (i << 5) - i`（位运算更快）
- 碰撞概率低

---

### 1.4 toString() 方法

#### 1.4.1 默认toString()行为

```java
public String toString() {
    return getClass().getName() + "@" + Integer.toHexString(hashCode());
}
// 输出示例: com.example.Person@7a8119d
```

#### 1.4.2 实际项目中的toString()

```java
// 使用IDE自动生成的toString()
@Override
public String toString() {
    return "Person{name='" + name + "', age=" + age + '}';
}

// Java 14+ 使用文本块
@Override
public String toString() {
    return "Person{" +
           "name='" + name + '\'' +
           ", age=" + age +
           '}';
}

// 使用String.format
@Override
public String toString() {
    return String.format("Person{name='%s', age=%d}", name, age);
}
```

---

### 1.5 clone() 方法

#### 1.5.1 clone()的基本概念

```java
protected native Object clone() throws CloneNotSupportedException;
```

- 是**浅拷贝**（只拷贝基本类型和引用地址）
- 对象类必须实现 `Cloneable` 接口，否则抛出 `CloneNotSupportedException`
- 重写时返回类型应为具体类型

#### 1.5.2 深拷贝 vs 浅拷贝

```java
// 浅拷贝示例
public class ShallowCopy implements Cloneable {
    private int[] arr;

    @Override
    protected ShallowCopy clone() throws CloneNotSupportedException {
        return (ShallowCopy) super.clone();  // 只复制引用
    }
}

// 深拷贝示例
public class DeepCopy implements Cloneable {
    private int[] arr;

    @Override
    protected DeepCopy clone() throws CloneNotSupportedException {
        DeepCopy result = (DeepCopy) super.clone();
        result.arr = this.arr.clone();  // 创建新的数组副本
        return result;
    }
}
```

#### 1.5.3 clone()的替代方案

```java
// 推荐：使用拷贝构造器或静态工厂方法
public class Person {
    private final String name;

    // 拷贝构造器
    public Person(Person other) {
        this.name = other.name;
    }

    // 拷贝静态工厂方法
    public static Person copyOf(Person original) {
        return new Person(original.name);
    }
}

// 或者使用克隆元模型（Effective Java推荐）
public class Person implements Cloneable<Person> {
    private String name;

    @Override
    public Person clone() {
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();  // 不应该发生
        }
    }
}
```

---

### 1.6 finalize() 方法（已废弃）

#### 1.6.1 背景

```java
@Deprecated(since="9")
protected void finalize() throws Throwable { }
```

- 原本用于资源释放
- Java 9+ 已废弃
- 不保证何时执行，甚至不执行

#### 1.6.2 现代替代方案

```java
// 使用 try-with-resources
public class Resource implements AutoCloseable {
    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    @Override
    public void close() {
        System.out.println("资源释放: " + name);
    }

    // 使用
    public static void main(String[] args) {
        try (Resource r = new Resource("文件")) {
            // 使用资源
        }  // 自动调用close()
    }
}
```

---

### 1.7 getClass() 方法

```java
public final native Class<?> getClass();

// 获取对象的运行时类型
public class GetClassDemo {
    public static void main(String[] args) {
        Object obj = new String("Hello");

        // 编译时类型是Object，运行时类型是String
        System.out.println(obj.getClass());  // class java.lang.String

        // 反射获取类信息
        Class<?> clazz = obj.getClass();
        System.out.println(clazz.getName());       // java.lang.String
        System.out.println(clazz.getSimpleName()); // String
    }
}
```

---

### 1.8 wait/notify 线程通信

```java
// 线程等待和唤醒
public class WaitNotifyDemo {
    private final Object lock = new Object();
    private boolean ready = false;

    // 等待线程
    public void waitForReady() throws InterruptedException {
        synchronized (lock) {
            while (!ready) {
                lock.wait();  // 释放锁并等待
            }
            System.out.println("继续执行");
        }
    }

    // 唤醒线程
    public void setReady() {
        synchronized (lock) {
            ready = true;
            lock.notifyAll();  // 唤醒所有等待的线程
        }
    }
}
```

---

### 1.9 最佳实践总结

```java
/**
 * Object最佳实践
 */
public class ObjectBestPractices {
    public static void main(String[] args) {
        System.out.println("=== Object类最佳实践 ===\n");

        // 1. 始终重写equals、hashCode、toString
        // 2. equals必须满足五个特性
        // 3. hashCode必须与equals一致
        // 4. 优先使用组合而非继承
        // 5. 使用try-with-resources替代finalize
        // 6. 谨慎使用clone()，考虑拷贝构造器
    }
}
```

---

### 1.10 练习题

1. **判断题**：`hashCode()` 相同则 `equals()` 一定为 `true` 吗？

2. **编程题**：实现一个 `Point` 类，重写 `equals()`、`hashCode()` 和 `toString()` 方法。

3. **思考题**：为什么String类要重写 `equals()` 和 `hashCode()`？

---

### 1.11 参考答案

```java
// 编程题答案
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return String.format("Point{x=%d, y=%d}", x, y);
    }
}
```

---

[返回目录](../README.md) | [下一步：String家族](./Step02_String.md)
