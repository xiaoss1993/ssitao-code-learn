# 第一阶段：核心语法基础

## 学习目标

掌握Java核心基础语法，包括Object类、String家族、包装类和常用工具类的使用与原理。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | Object类 | [Step01_Object.md](./Step01_Object.md) | [object/*.java](./object/) |
| 2 | String家族 | [Step02_String.md](./Step02_String.md) | [string/*.java](./string/) |
| 3 | 包装类 | [Step03_Wrapper.md](./Step03_Wrapper.md) | [wrapper/*.java](./wrapper/) |
| 4 | 常用工具类 | [Step04_Utils.md](./Step04_Utils.md) | [utils/*.java](./utils/) |

---

## 步骤1: Object类

### 核心知识点

- `equals()` - 判断对象相等性，需要满足自反性、对称性、传递性、一致性、非空性
- `hashCode()` - 与equals必须配对重写，用于哈希表
- `toString()` - 返回可读的字符串表示
- `clone()` - 对象拷贝（浅拷贝），需实现Cloneable接口
- `wait/notify` - 线程间通信

### 黄金法则

> **如果两个对象equals()返回true，则它们的hashCode()必须返回相同的值**

### 代码文件

- `object/Person.java` - equals/hashCode/toString重写示例
- `object/ShallowCopyDemo.java` - 浅拷贝演示
- `object/DeepCopyDemo.java` - 深拷贝演示
- `object/WaitNotifyDemo.java` - 线程通信演示
- `object/Point.java` - 练习题答案

---

## 步骤2: String家族

### 核心知识点

- String是不可变类（final class + private final char[]）
- 字符串常量池节省内存
- StringBuilder（单线程，高效）和StringBuffer（多线程，安全）
- 字符串拼接优先使用StringBuilder

### 扩容机制

```
初始容量: 16
扩容公式: newCapacity = oldCapacity * 2 + 2
```

### 代码文件

- `string/StringDemo.java` - String常用方法
- `string/StringBuilderDemo.java` - StringBuilder/StringBuffer演示
- `string/StringExercises.java` - 练习题答案

---

## 步骤3: 包装类

### 核心知识点

- 8种基本类型对应的包装类
- 自动装箱与拆箱（Auto Boxing/Unboxing）
- Integer缓存范围：-128 ~ 127
- 避免装箱类型使用==比较

### 装箱陷阱

```java
Integer a = 127;  // 使用缓存
Integer b = 127;
a == b;  // true

Integer c = 128;  // 超出缓存
Integer d = 128;
c == d;  // false
c.equals(d);  // true
```

### 代码文件

- `wrapper/WrapperDemo.java` - 包装类与自动装箱演示
- `wrapper/MathDemo.java` - Math工具类演示

---

## 步骤4: 常用工具类

### 核心知识点

- Arrays - 数组排序、查找、复制、转换
- Collections - 集合排序、查找、不可变集合
- Objects - 空值检查、对象比较、hashCode

### JDK 9+ 不可变集合

```java
List<String> list = List.of("a", "b", "c");
Set<String> set = Set.of("a", "b", "c");
Map<String, Integer> map = Map.of("a", 1, "b", 2);
```

### 代码文件

- `utils/ArraysDemo.java` - Arrays工具类演示
- `utils/CollectionsDemo.java` - Collections工具类演示
- `utils/ObjectsDemo.java` - Objects和System演示

---

## 学习建议

1. **先看文档**：理解每个概念的原理和注意事项
2. **再跑代码**：运行示例代码，观察输出
3. **做练习题**：独立完成练习，验证理解
4. **阅读源码**：有余力可以阅读JDK源码实现

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase01.object.Person"
```

---

## 下一步

[第二阶段：集合框架](../phase02/README.md)
