# 第八章：通用编程

> Effective Java Chapter 8: General Programming

## 章节概览

本章涵盖 7 个条目（45-51），讨论通用编程最佳实践。

---

## Item 45: Minimize the scope of local variables

### 核心原则

**变量尽量靠近使用处声明**

```java
// 错误：声明过早
String name = "";
if (condition) {
    use(name);
}

// 正确：使用时再声明
if (condition) {
    String name = getName();
    use(name);
}
```

### For-each 优于传统 for

```java
// 传统 for（容易出错）
for (int i = 0; i < list.size(); i++) {
    doSomething(list.get(i));
}

// For-each（简洁安全）
for (String item : list) {
    doSomething(item);
}
```

### 何时用传统 for

| 场景 | 原因 |
|------|------|
| 倒序遍历 | 无法用 for-each |
| 需要索引 | for-each 没有索引变量 |
| 修改循环变量 | for-each 不能修改 |

---

## Item 46: Prefer for-each loops to traditional for loops

### 三种不适用 for-each 的情况

```java
// 1. 过滤 - 需要 iterator.remove()
for (Iterator<String> i = list.iterator(); i.hasNext(); ) {
    if (condition(i.next())) {
        i.remove();
    }
}

// 2. 转换 - 需要修改元素
for (int i = 0; i < list.size(); i++) {
    list.set(i, transform(list.get(i)));
}

// 3. 并行迭代 - 需要显式控制
for (int i = 0; i < list1.size(); i++) {
    process(list1.get(i), list2.get(i));
}
```

---

## Item 47: Know and use the libraries

### 使用标准库的好处

| 好处 | 说明 |
|------|------|
| 专家知识 | 库经过大量测试和使用 |
| 无维护成本 | 不需要自己维护 |
| 互操作性 | 与其他代码兼容 |
| 性能提升 | 持续优化 |

### 常用库

```java
// 随机数
Random rnd = new Random();
int n = rnd.nextInt(100);

// 打乱
Collections.shuffle(list);

// 二分搜索（需先排序）
Collections.sort(list);
int index = Collections.binarySearch(list, target);

// 时间 (Java 8+)
LocalDate.now();
LocalDateTime.now();
```

### Math.abs 的陷阱

```java
// Integer.MIN_VALUE 会溢出！
Math.abs(Integer.MIN_VALUE)  // 仍是负数！

// 解决方案
Math.absExact(Integer.MIN_VALUE);  // Java 11+ 抛异常
```

---

## Item 48: Avoid float and double where exact answers are required

### 问题

```java
// 错误：用 double 计算 money
double funds = 1.00;
double items = 0.10;
while (funds >= items) {
    funds -= items;
    items -= 0.10;
}
// 结果：0.7999999999999999 而非 0.8
```

### 解决方案：BigDecimal

```java
// 正确：用 BigDecimal
BigDecimal funds = new BigDecimal("1.00");
BigDecimal items = new BigDecimal("0.10");
while (funds.compareTo(items) >= 0) {
    funds = funds.subtract(items);
    items = items.subtract(new BigDecimal("0.10"));
}
```

### 类型选择

| 类型 | 精度 | 用途 |
|------|------|------|
| int | 精确 | 默认整数 |
| long | 精确 | 大整数 |
| double | 二进制浮点 | 科学计算 |
| **BigDecimal** | 精确 | 货币计算 |

---

## Item 49: Prefer primitive types to boxed primitives

### 问题

```java
// 装箱类型比较（危险）
Integer a = new Integer(127);  // 缓存
Integer b = new Integer(127);
a == b  // false！对象比较

Integer c = 127;  // 自动装箱
Integer d = 127;
c == d  // true（缓存）

// 超出缓存范围
Integer e = 128;
Integer f = 128;
e == f  // false！
```

### 解决方案

```java
// 始终用原始类型比较
int a = 127;
int b = 127;
a == b  // true

// 如果必须用装箱类型，用 equals
Integer c = 128;
Integer d = 128;
c.equals(d)  // true
```

---

## Item 50: Avoid strings where other types are more appropriate

### 错误使用 String

```java
// 1. 用 String 表示数值
int age = Integer.parseInt("25");  // 应该用 int

// 2. 用 String 表示布尔值
boolean b = Boolean.parseBoolean("true");  // 应该用 boolean

// 3. 用 String 表示枚举
Status s = Status.valueOf("ACTIVE");  // 可以，但不推荐

// 4. 用 String 聚合多个值
String key = "name:age:city";  // 用类/记录代替
```

---

## Item 51: Beware the performance of string concatenation

### 问题

```java
// 循环中用 + 连接（每次创建新 String）
String s = "";
for (int i = 0; i < 1000; i++) {
    s += "x";  // 1000 个中间 String 对象！
}
```

### 解决方案：StringBuilder

```java
// StringBuilder（高效）
StringBuilder sb = new StringBuilder(1000);  // 预分配容量
for (int i = 0; i < 1000; i++) {
    sb.append("x");
}
String s = sb.toString();
```

### 最佳实践

| 场景 | 方法 |
|------|------|
| 单次连接 | `+` 即可，编译器优化 |
| 少量已知字符串 | `+` 即可 |
| 循环中连接 | StringBuilder |
| 连接字符串列表 | `String.join(", ", list)` |

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 45 | 最小化局部变量作用域 |
| 46 | 优先使用 for-each |
| 47 | 了解并使用库 |
| 48 | 避免用 float/double 计算精确值 |
| 49 | 优先原始类型而非装箱类型 |
| 50 | 其他类型优于 String |
| 51 | 注意字符串拼接性能 |

---

## 实战示例

```java
// 最佳实践组合
public String buildReport(List<Item> items) {
    // 1. 不用 String，用 StringBuilder
    StringBuilder sb = new StringBuilder();

    // 2. for-each 遍历
    for (Item item : items) {
        // 3. 不用 + 在循环中拼接
        sb.append(item.getName())
          .append(": ")
          .append(item.getPrice())
          .append("\n");
    }

    return sb.toString();
}

// 字符串 join（Java 8+）
String csv = String.join(",", names);
```

---

## 练习题

1. 什么时候应该用传统 for 而不是 for-each？
2. 为什么 double 不适合用于货币计算？
3. `Integer == Integer` 什么时候返回 true？
4. StringBuilder 相比 + 的优势是什么？
5. 举出 3 个不应该用 String 的场景。
