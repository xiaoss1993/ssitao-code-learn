# 第六章：Lambda和流

> Effective Java Chapter 6: Lambdas and Streams

## 章节概览

本章涵盖 7 个条目（38-44），讨论 Lambda 和流 API 的最佳实践。

---

## Item 38: Prefer method references to lambdas

### 方法引用的类型

| 类型 | 示例 | 等价 Lambda |
|------|------|------------|
| 静态方法 | `Integer::parseInt` | `(s) -> Integer.parseInt(s)` |
| 绑定实例 | `str::startsWith` | `(s) -> str.startsWith(s)` |
| 未绑定实例 | `String::toLowerCase` | `(s) -> s.toLowerCase()` |
| 构造器 | `ArrayList::new` | `() -> new ArrayList()` |

### 示例

```java
// Lambda (verbose)
map.forEach((k, v) -> System.out.println(k + "=" + v));

// Method reference (concise)
map.forEach((k, v) -> System.out.println(k + "=" + v));  // Can't improve further
map.forEach(System.out::println);  // When just printing values

// Function
Function<String, Integer> parser1 = s -> Integer.parseInt(s);
Function<String, Integer> parser2 = Integer::parseInt;

// Comparator
Comparator<String> comp = (a, b) -> a.compareToIgnoreCase(b);
Comparator<String> comp2 = String::compareToIgnoreCase;
```

### 何时保留 Lambda

```java
// Lambda 更清晰时
Comparator<String> revOrder = (a, b) -> b.compareTo(a);  // OK

// Method reference 反而复杂时
// (a, b) -> a.substring(b).length() 不值得提取
```

---

## Item 39: Prefer standard functional interfaces

### 常用标准接口

| 接口 | 签名 | 说明 |
|------|------|------|
| `Function<T, R>` | `T -> R` | 转换 |
| `UnaryOperator<T>` | `T -> T` | 一元运算 |
| `BinaryOperator<T>` | `(T, T) -> T` | 二元运算 |
| `Predicate<T>` | `T -> boolean` | 条件判断 |
| `Supplier<T>` | `() -> T` | 生产者 |
| `Consumer<T>` | `T -> void` | 消费者 |

### 原始类型特化

```java
// 避免装箱
IntFunction<double[]> arrayCreator = double[]::new;  // (int) -> double[]
IntPredicate isEven = i -> i % 2 == 0;
IntToDoubleFunction sqrt = Math::sqrt;  // int -> double

// 不要这样！
// Function<Integer, Double> slow = Math::sqrt;  // 装箱！
```

### 何时需要自定义接口

```java
// Comparator<T> 是特殊的（有语义，用于排序）
Comparator<String> byLength = Comparator.comparingInt(String::length);

// 当需要唯一签名且标准库没有时
// 如 TriFunction<T, U, V, R> 在 Java 8 不存在
```

---

## Item 40: Use streams carefully

### 流的正确用法

```java
// 管道：source -> intermediate -> intermediate -> terminal

List<String> words = Arrays.asList("apple", "banana", "cherry");

// 过滤、映射、收集
List<String> result = words.stream()
    .filter(w -> w.length() > 5)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

### 适合流的场景

1. **转换序列**：`map`, `filter`, `flatMap`
2. **聚合操作**：`reduce`, `sum`, `max`, `collect`
3. **分组**：`groupingBy`, `partitioningBy`
4. **查找**：`findFirst`, `findAny`, `anyMatch`

### 不适合流的场景

```java
// 需要访问循环状态（计数器、标志）
int[] count = {0};
boolean found = stream.filter(x -> {
    count[0]++;  // 外部状态 - 尴尬
    return x > 0;
}).findFirst().isPresent();

// 需要多趟处理且条件依赖数据
// 需要修改共享状态
```

### 惰性求值

```java
Stream<String> stream = words.stream()
    .filter(w -> {
        System.out.println("Filtering: " + w);
        return w.length() > 3;
    });

// 不会打印任何内容 - 还没执行 terminal 操作！
stream.findFirst();  // 现在才执行
```

### 并行流

```java
// 只有在以下情况使用并行流：
// - 大数据集
// - 无状态、纯函数操作
// - 无顺序要求

long sum = LongStream.rangeClosed(1, 1_000_000)
    .parallel()
    .sum();
```

---

## Item 41: Use streams judiciously

### 不要滥用流

```java
// 糟糕：过度使用
"Hello".chars()
    .mapToObj(c -> (char) c)
    .forEach(System.out::println);

// 好：直接用 forEach
"Hello".chars().forEach(System.out::println);

// 更好：不用流
for (char c : "Hello".toCharArray()) {
    System.out.println(c);
}
```

### 最佳实践

1. **流链不要太长** - 提取到方法
2. **map 之后要 filter** - 但 filter 之后 map 也正常
3. **避免副作用** - `forEach` 应只用于打印

```java
// 糟糕
stream.collect(Collectors.toList()).forEach(System.out::println);

// 好
stream.forEach(System.out::println);

// 好
List<String> list = stream.filter(...).collect(toList());
```

---

## Item 42: Prefer lambda to anonymous classes

### Lambda vs 匿名类

```java
// 匿名类（冗长）
Comparator<String> c1 = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};

// Lambda（简洁）
Comparator<String> c2 = (a, b) -> Integer.compare(a.length(), b.length());

// 最佳：方法引用
Comparator<String> c3 = Comparator.comparingInt(String::length);
```

### `this` 的区别

```java
class Outer {
    void demonstrate() {
        Runnable lambda = () -> System.out.println(this);  // Outer 实例
        Runnable anon = new Runnable() {
            @Override
            public void run() {
                System.out.println(Outer.this);  // 需要 Outer.this
            }
        };
    }
}
```

### 何时仍用匿名类

```java
// 多抽象方法的接口 - 但这种情况很少
// 大多数 JDK 接口是函数式接口（单抽象方法）

// 匿名类仍用于：
// - 需要引用自己（lambda 不能引用自己）
// - 复杂的多方法接口（如 ActionListener）
```

---

## Item 43: Prefer method references to lambdas

### 方法引用优先

```java
// 方法引用更易读 - 意图更明确
map.forEach((k, v) -> Collections.sort(map.get(k)));
map.forEach((k, v) -> v.sort(Collections.reverseOrder()));
map.forEach((k, v) -> System.out.println(k + "=" + v));

// 改进后
map.forEach(Collections::sort);  // 需要实例方法绑定
map.values().forEach(v -> v.sort(Collections.reverseOrder()));
map.forEach((k, v) -> System.out.println(k + "=" + v));  // 不好改，但可接受
```

---

## Item 44: Favor the use of standard functional interfaces

### 完整清单

| 接口 | 签名 | 说明 |
|------|------|------|
| `BiFunction<T, U, R>` | `(T, U) -> R` | 双参数 |
| `BiPredicate<T, U>` | `(T, U) -> boolean` | 双条件 |
| `BiConsumer<T, U>` | `(T, U) -> void` | 双消费者 |
| `ToIntFunction<T>` | `T -> int` | 返回 int |
| `IntToDoubleFunction` | `int -> double` | int 转 double |

### 记住

- **43个**标准函数接口在 `java.util.function`
- 只有当需要**唯一签名**或**特定语义**时才自定义
- `Comparator<T>` 是特殊接口，有语义（用于排序），可接受

---

## 章节总结

| Item | 核心原则 |
|------|----------|
| 38 | 方法引用优于 Lambda |
| 39 | 优先使用标准函数接口 |
| 40 | 谨慎使用流 |
| 41 | 明智地使用流 |
| 42 | Lambda 优于匿名类 |
| 43 | 方法引用优先 |
| 44 | 优先使用标准函数接口 |

---

## 实战示例

```java
// 1. 用流和 Lambda 实现复杂查询
List<Student> topStudents = students.stream()
    .filter(s -> s.getGPA() > 3.5)
    .sorted(Comparator.comparingDouble(Student::getGPA).reversed())
    .limit(10)
    .collect(Collectors.toList());

// 2. 用 EnumMap 和 streams 分组
EnumMap<Phase, List<Transition>> byFrom = Stream.of(Transition.values())
    .collect(Collectors.groupingBy(
        Transition::getFrom,
        () -> new EnumMap<>(Phase.class),
        Collectors.toList()
    ));

// 3. 不用流的情况：需要提前退出或复杂逻辑
List<String> result = new ArrayList<>();
for (String s : input) {
    if (s.isEmpty()) break;  // 流无法优雅处理
    result.add(s.toUpperCase());
}
```

---

## 练习题

1. 将匿名类Comparator改为Lambda和方法引用
2. 用流实现：过滤、映射、分组操作
3. 什么时候不适合用流？举出3个例子
4. `Function.identity()` 什么时候有用？
5. 为什么不要滥用流？
