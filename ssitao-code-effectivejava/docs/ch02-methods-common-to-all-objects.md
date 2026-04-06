# 第二章：所有对象通用的方法

> Effective Java Chapter 2: Methods Common to All Objects

## 章节概览

本章涵盖 7 个条目（8-14），讨论 Object 类的方法重写规范。

---

## Item 8: Obey the general contract when overriding equals

### 什么时候不需要重写 equals？

- 类的每个实例本质上是唯一的（如 Thread）
- 类不需要提供"逻辑相等"的测试（如 Pattern）
- 父类已经重写了 equals，且行为适用
- 类是私有的或包级私有的

### equals 的等价关系

| 特性 | 要求 |
|------|------|
| 自反性 | `x.equals(x)` 必须为 true |
| 对称性 | `x.equals(y) == y.equals(x)` |
| 传递性 | `x.equals(y) && y.equals(z) => x.equals(z)` |
| 一致性 | 多次调用结果一致 |
| 非空性 | `x.equals(null)` 必须为 false |

### 正确的 equals 重写模板

```java
@Override
public boolean equals(Object obj) {
    // 1. 使用 == 检查是否是同一对象
    if (this == obj) {
        return true;
    }

    // 2. 检查 obj 是否为 null
    if (obj == null) {
        return false;
    }

    // 3. 检查是否是同一类型
    if (getClass() != obj.getClass()) {
        return false;
    }

    // 4. 强制转型并比较关键字段
    MyClass other = (MyClass) obj;
    return field1.equals(other.field1)
        && field2 == other.field2
        && Objects.equals(field3, other.field3);
}
```

### 示例

```java
// com.ssitao.code.effectivejava.ch02.item08.PhoneNumber
PhoneNumber pn1 = new PhoneNumber((short) 707, (short) 867, (short) 5309);
PhoneNumber pn2 = new PhoneNumber((short) 707, (short) 867, (short) 5309);
System.out.println(pn1.equals(pn2));  // true - 不同引用但值相等
```

---

## Item 9: Always override hashCode when you override equals

### 违规示例

```java
// 违反了 hashCode 约定
Map<PhoneNumber, String> map = new HashMap<>();
map.put(new PhoneNumber(707, 867, 5309), "alice");
map.get(new PhoneNumber(707, 867, 5309));  // 返回 null！
```

### hashCode 的约定

1. **相等对象必须有相同的 hashCode**：如果 `a.equals(b)` 为 true，则 `a.hashCode() == b.hashCode()` 必须为 true
2. **不相等对象尽量有不同的 hashCode**：提高 HashMap 等数据结构的性能

### 正确的 hashCode 实现

```java
@Override
public int hashCode() {
    int result = Short.hashCode(areaCode);
    result = 31 * result + Short.hashCode(prefix);
    result = 31 * result + Short.hashCode(lineNum);
    return result;
}
```

### 使用 Objects.hash()

```java
// 简洁方式，但性能稍差（会创建数组）
@Override
public int hashCode() {
    return Objects.hash(areaCode, prefix, lineNum);
}
```

### 示例

```java
// com.ssitao.code.effectivejava.ch02.item09.PhoneNumberWithHash
PhoneNumberWithHash pn1 = new PhoneNumberWithHash((short) 707, (short) 867, (short) 5309);
PhoneNumberWithHash pn2 = new PhoneNumberWithHash((short) 707, (short) 867, (short) 5309);
System.out.println(pn1.hashCode() == pn2.hashCode());  // true

Map<PhoneNumberWithHash, String> map = new HashMap<>();
map.put(pn1, "alice");
System.out.println(map.get(pn2));  // "alice" - 正常工作
```

---

## Item 10: Always override toString

### 为什么要重写 toString？

```java
// 没有重写 toString
System.out.println(pn);  // PhoneNumber@4554617c (无意义)

// 重写 toString 后
System.out.println(pn);  // 707-867-5309 (清晰有意义)
```

### toString 重写示例

```java
@Override
public String toString() {
    return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
}
```

### 建议

- 在 toString 中返回所有值得关注的信息
- 考虑提供静态工厂方法或构造器来解析 toString 返回的格式

### 示例

```java
// com.ssitao.code.effectivejava.ch02.item10.PhoneNumberWithToString
PhoneNumberWithToString pn = new PhoneNumberWithToString((short) 707, (short) 867, (short) 5309);
System.out.println(pn);  // 707-867-5309
```

---

## Item 11: Override clone judiciously

### Cloneable 接口的问题

```java
// Cloneable 是标记接口，不包含任何方法
// Object.clone() 的行为依赖于它
public class CloneableExample implements Cloneable {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();  // 如果不实现 Cloneable 会抛异常
    }
}
```

### 复制对象的更好方案

#### 1. 复制构造器

```java
public class Person {
    private final String name;
    private final int age;

    public Person(Person other) {
        this.name = other.name;
        this.age = other.age;
    }
}
```

#### 2. 静态工厂方法

```java
public static Person newInstance(Person original) {
    return new Person(original.getName(), original.getAge());
}
```

### 何时可以使用 clone？

- 实现 `Cloneable` 的数组
- 性能敏感且不可变对象的复制场景

---

## Item 12: Consider implementing Comparable

### compareTo 的约定

| 特性 | 要求 |
|------|------|
| 对称性 | `signum(x.compareTo(y)) == -signum(y.compareTo(x))` |
| 传递性 | `x.compareTo(y) > 0 && y.compareTo(z) > 0 => x.compareTo(z) > 0` |
| 建议 | `x.compareTo(y) == 0` 意味着 `signum(x.compareTo(z)) == signum(y.compareTo(z))` |

### compareTo 与 equals 的关系

```java
// 当 compareTo 返回 0 时，equals 也应返回 true
// 否则基于 TreeSet、TreeMap 的行为会不符合预期
BigDecimal one = new BigDecimal("1.0");
BigDecimal two = new BigDecimal("1.00");
one.equals(two);              // false（精度不同）
one.compareTo(two) == 0;      // true（值相同）
```

### compareTo 实现示例

```java
@Override
public int compareTo(WordList o) {
    return Comparator
        .comparing((WordList w) -> w.words.length)
        .thenComparing(w -> w.words[0])
        .compare(this, o);
}
```

### 示例

```java
// com.ssitao.code.effectivejava.ch02.item12.WordList
WordList[] lists = {
    new WordList("hello", "world"),
    new WordList("hi"),
    new WordList("good", "morning")
};
Arrays.sort(lists);
// 按长度排序：[hi] -> [hello world] -> [good morning]
```

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 8 | 重写 equals 时遵守通用约定（自反性、对称性、传递性、一致性、非空性）|
| 9 | 重写 equals 必须重写 hashCode |
| 10 | 始终重写 toString |
| 11 | 谨慎使用 clone，优先考虑复制构造器或工厂方法 |
| 12 | 考虑实现 Comparable |

---

## 常见错误汇总

```java
// 错误 1: equals 使用 instanceof 而不是 getClass()
public boolean equals(Object obj) {
    if (obj instanceof MyClass) {  // 错误！
        ...
    }
}

// 错误 2: 重写了 equals 但不重写 hashCode
// 导致 HashMap/HashSet 行为异常

// 错误 3: toString 返回 null 或空字符串
public String toString() {
    return null;  // 错误！
}
```

---

## 练习题

1. 写一个类正确重写 equals、hashCode 和 toString
2. 解释为什么 `new BigDecimal("1.0").equals(new BigDecimal("1.00"))` 返回 false
3. 找出你项目中违反 equals/hashCode 约定的代码
4. 比较 clone、复制构造器、静态工厂方法的优缺点
