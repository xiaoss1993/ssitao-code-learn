# 第四章：泛型

> Effective Java Chapter 4: Generics

## 章节概览

本章涵盖 8 个条目（26-33），讨论泛型的最佳实践。

---

## Item 26: Don't use raw types

### 什么是原生态类型？

```java
// 原生态类型 - 危险！失去类型安全
List list = new ArrayList();
list.add("hello");
list.add(123);  // 编译通过，运行时可能 ClassCastException

// 泛型 - 类型安全
List<String> list = new ArrayList<>();
list.add("hello");
list.add(123);  // 编译错误！
```

### 何时可以使用原生态类型？

| 场景 | 示例 |
|------|------|
| 类字面量 | `List.class`, `String.class` |
| instanceof 检查 | `if (obj instanceof List)` |

```java
// instanceof 必须使用通配符
if (obj instanceof List) {
    List<?> list = (List<?>) obj;
    // 可以安全地读取元素
}
```

---

## Item 27: Eliminate unchecked warnings

### 常见警告

```java
Set<Lotto> lotto = new HashSet();  // Unchecked conversion warning
```

### 如何消除

```java
// 1. 使用泛型
Set<Lotto> lotto = new HashSet<>();

// 2. 添加 @SuppressWarnings 注解（仅在无法消除时）
@SuppressWarnings("unchecked")
T result = (T) cache.get(key);
```

### 最佳实践

```java
// 始终使用泛型，减少强制类型转换
List<String> list = new ArrayList<>();  // 无警告
```

---

## Item 28: Prefer lists to arrays

### 数组的问题：协变性

```java
// 数组是协变的 - 编译通过但危险
Object[] objArr = new String[10];
objArr[0] = 123;  // ArrayStoreException at runtime

// 泛型是不变的 - 编译错误，更安全
List<Object> objList = new ArrayList<String>();  // 编译错误
```

### 列表的优势

```java
// 编译时类型安全
List<Fruit> fruits = new ArrayList<>();
fruits.add(new Apple());  // OK
// fruits.add(new Tool());  // 编译错误
```

### 何时可以使用数组？

```java
// 泛型数组无法创建
// new E[]  // 编译错误

// 解决方案：使用 List
List<E> elements = new ArrayList<>();
```

---

## Item 29: Favor generic types

### 问题：使用 Object 数组

```java
// 危险：需要强制类型转换
public class Chooser {
    private final Object[] choices;

    public Chooser(Object[] choices) {
        this.choices = choices;
    }

    public Object choose() {
        Random rand = new Random();
        return choices[rand.nextInt(choices.length)];  // 返回 Object
    }
}

// 使用时需要强制转换
String choice = (String) chooser.choose();  // 危险！
```

### 解决方案：泛型

```java
public class Chooser<T> {
    private final T[] choices;

    public Chooser(T[] choices) {
        this.choices = choices;
    }

    public T choose() {
        Random rand = new Random();
        return choices[rand.nextInt(choices.length)];  // 返回 T，无需强制转换
    }
}

// 使用时无需强制转换
Chooser<String> chooser = new Chooser<>(stringArray);
String choice = chooser.choose();  // 类型安全
```

---

## Item 30: Favor generic methods

### 泛型方法示例

```java
// 静态工具方法使用泛型
public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}

// 使用
Set<String> strings = union(stringSet1, stringSet2);
Set<Integer> integers = union(intSet1, intSet2);
```

### 泛型单例工厂

```java
// 泛型约束的恒等函数
private static <T> T identity(T input) {
    return input;
}
```

### 类型约束

```java
// 使用 <T extends Comparable<T>> 约束类型
public static <T extends Comparable<T>> T max(List<T> list) {
    T max = list.get(0);
    for (T item : list) {
        if (item.compareTo(max) > 0) {
            max = item;
        }
    }
    return max;
}
```

---

## Item 31: Use bounded wildcards to increase flexibility

### PECS 原则

```
PECS: Producer Extends, Consumer Super
```

- **Extends**：要从泛型读取数据（生产者）
- **Super**：要将数据写入泛型（消费者）

### 示例：Stack

```java
public class Stack<E> {
    public void push(E item) { ... }      // 写入元素
    public E pop() { ... }                // 读取元素

    // pushAll - 生产者，使用 extends
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    // popAll - 消费者，使用 super
    public void popAll(Collection<? super E> dest) {
        while (!isEmpty()) {
            dest.add(pop());
        }
    }
}
```

### 协变 vs 逆变

| 场景 | 代码 | 说明 |
|------|------|------|
| 生产者 | `List<? extends Number>` | 可以读取 Number |
| 消费者 | `List<? super Integer>` | 可以写入 Integer |
| 两者 | `List<Integer>` | 既可读又可写 |

---

## Item 32: Don't blindly use varargs

### varargs 的问题

```java
// 危险：数组可能泄露
public void dangerousMethod(String... args) {
    String[] array = args;  // 引用暴露
}
```

### 解决方案：泛型 + 数组包装

```java
// 使用 @SafeVarargs 标注安全的方法
@SafeVarargs
public <T> void safeMethod(T... args) {
    // 编译器保证不会修改数组
    List<T> list = Arrays.asList(args);
}
```

### 正确使用场景

```java
// varargs 适用于可变参数数量的场景
// 但不应该用于集合类构造
public static <E> Set<E> of(E... elements) {
    return new HashSet<>(Arrays.asList(elements));
}
```

---

## Item 33: Consider typesafe heterogeneous containers

### 异构容器

普通容器的限制：`Map<String, String>` 只能存储 String 类型的值

### 类型安全的异构容器

```java
public class Favorites {
    private final Map<Class<?>, Object> favorites = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(type, type.cast(instance));
    }

    public <T> T getFavorite(Class<T> type) {
        return type.cast(favorites.get(type));
    }
}

// 使用
Favorites f = new Favorites();
f.putFavorite(String.class, "hello");
f.putFavorite(Integer.class, 123);

String s = f.getFavorite(String.class);  // 类型安全
Integer i = f.getFavorite(Integer.class);
```

### Class.cast() 方法

```java
// 类型安全的类型转换
Class<String> stringClass = String.class;
String s = stringClass.cast("hello");  // 安全
// Integer n = stringClass.cast(123);   // ClassCastException
```

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 26 | 不使用原生态类型 |
| 27 | 消除非受检警告 |
| 28 | 列表优于数组 |
| 29 | 优先考虑泛型 |
| 30 | 优先考虑泛型方法 |
| 31 | 使用带边界的通配符（PECS） |
| 32 | 谨慎使用 varargs |
| 33 | 考虑类型安全的异构容器 |

---

## PECS 速记

```java
// Producer Extends - 读取数据用 extends
public void copyAll(List<? extends Number> from, List<? super Number> to) {
    for (Number n : from) {  // 读取，用 extends
        to.add(n);           // 写入，用 super
    }
}
```

---

## 练习题

1. 解释为什么 `List<String>` 不是 `List<Object>` 的子类型
2. 写一个泛型方法，实现二分查找
3. 实现一个 `Pair<A, B>` 类
4. 解释 PECS 原则并给出例子
5. 实现一个类型安全的异构容器
