# 第七章：异常

> Effective Java Chapter 7: Exceptions

## 章节概览

本章涵盖 7 个条目（39-45），讨论异常的最佳实践。

---

## Item 39: Use exceptions only for exceptional conditions

### 核心原则

**异常用于异常情况，不用于普通控制流**

```java
// 错误：用异常控制流程（慢！）
try {
    int i = 0;
    while (true) {
        array[i++];  // 越界时抛异常
    }
} catch (ArrayIndexOutOfBoundsException e) {
    // 找到末尾了
}

// 正确：用循环
for (int item : array) {
    // 处理 item
}
```

### 异常的性能问题

| 操作 | 相对时间 |
|------|---------|
| 简单 if 检查 | 1x |
| 抛出并捕获异常 | 1000x+ |

异常创建涉及栈轨迹抓取，开销很大。

---

## Item 40: Follow proven conventions for checked exceptions

### 三种异常类型

```
Throwable
├── Error (unchecked)        - JVM错误，不捕获
│   └── OutOfMemoryError, StackOverflowError
└── Exception
    ├── RuntimeException (unchecked)  - 程序错误
    │   └── NullPointerException, IllegalArgumentException
    └── IOException (checked)          - 可恢复情况
```

### 选择规则

| 类型 | 使用场景 | 示例 |
|------|---------|------|
| **Checked** | 调用方可恢复 | `IOException`, `SQLException` |
| **Runtime** | 程序错误（bug） | `NullPointerException`, `IllegalArgumentException` |
| **Error** | JVM错误 | `OutOfMemoryError` |

### 检查异常最佳实践

```java
// 1. 提供辅助方法让调用者避免异常
public class Account {
    public boolean tryWithdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    // 2. 必须时再抛检查异常
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Balance: " + balance);
        }
        balance -= amount;
    }
}
```

---

## Item 41: Use exception chaining to preserve cause

### 异常链

```java
// 丢失原始原因（差）
try {
    // 低层操作
} catch (RuntimeException e) {
    throw new RuntimeException("高层错误");  // 原因丢失！
}

// 保留原始原因（好）
try {
    // 低层操作
} catch (RuntimeException e) {
    throw new HigherLevelException("高层错误", e);  // 链式保留
}
```

### 实现方式

```java
// 方式1：构造器传入
class HigherLevelException extends Exception {
    HigherLevelException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

// 方式2：initCause()
catch (RuntimeException e) {
    RuntimeException high = new RuntimeException("High-level");
    high.initCause(e);
    throw high;
}
```

---

## Item 42: Use exceptions carefully

### 不要过度使用异常

```java
// 错误：过度使用异常
try {
    return Integer.parseInt(s);
} catch (NumberFormatException e) {
    return 0;
}

// 正确：先检查
if (s != null && s.matches("\\d+")) {
    return Integer.parseInt(s);
}
```

---

## Item 43: Throw exceptions appropriate to the abstraction

### 异常翻译

```java
// 错误：暴露实现细节
try {
    // 低层实现
} catch (NullPointerException e) {
    throw new RuntimeException("处理失败");  // 暴露了内部实现
}

// 正确：转换为抽象层异常
try {
    // 低层实现
} catch (IOException e) {
    throw new ServiceException("无法处理请求", e);  // 抽象层异常
}
```

### Try-With-Resources

```java
// 手动管理（冗长）
BufferedReader br = null;
try {
    br = new BufferedReader(new FileReader("file.txt"));
    // ...
} finally {
    if (br != null) br.close();
}

// Try-With-Resources（简洁）
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // ...
} // 自动关闭

// 多资源
try (InputStream in = new FileInputStream("in.txt");
     OutputStream out = new FileOutputStream("out.txt")) {
    // ...
}
```

---

## Item 44: Favor standard exceptions

### 常用标准异常

| 异常 | 使用场景 |
|------|---------|
| `IllegalArgumentException` | 非null参数值不合法 |
| `IllegalStateException` | 方法调用时对象状态不合法 |
| `NullPointerException` | 参数为null |
| `IndexOutOfBoundsException` | 索引越界 |
| `UnsupportedOperationException` | 对象不支持该操作 |
| `NoSuchElementException` | 迭代/枚举没有更多元素 |
| `ConcurrentModificationException` | 并发修改检测 |

### 不要复用Exception/RuntimeException

```java
// 错误：直接复用 Exception
throw new Exception("message");

// 正确：使用更具体的异常
throw new IllegalArgumentException("message");
```

---

## Item 45: Use exceptions sparingly

### 总结

1. **异常用于异常情况** - 不要用于控制流
2. **性能开销大** - 创建栈轨迹代价高
3. **检查异常用于可恢复情况** - RuntimeException用于bug
4. **保留异常链** - 让调试更容易
5. **Try-With-Resources** - 自动资源管理
6. **异常翻译** - 转换为抽象层异常

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 39 | 仅在异常情况使用异常 |
| 40 | 遵循检查异常的规范 |
| 41 | 使用异常链保留原因 |
| 42 | 谨慎使用异常 |
| 43 | 异常要符合抽象层级 |
| 44 | 优先使用标准异常 |
| 45 | 尽量少用异常 |

---

## 实战示例

```java
// 完整的异常处理示例
public class Example {
    public Result process(Input input) {
        // 1. 前置条件检查
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        // 2. 业务逻辑 + 异常翻译
        try {
            return doProcess(input);
        } catch (LowLevelException e) {
            throw new HighLevelException("Processing failed", e);
        }
    }

    // 3. Try-With-Resources
    public String readFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);  // 包装为 unchecked
        }
    }
}
```

---

## 练习题

1. 为什么用异常做控制流是错误的？
2. Checked Exception 和 RuntimeException 的区别？
3. 什么是异常链？如何实现？
4. Try-With-Resources 相比传统 try-finally 有什么优点？
5. 何时应该使用 `UnsupportedOperationException`？
