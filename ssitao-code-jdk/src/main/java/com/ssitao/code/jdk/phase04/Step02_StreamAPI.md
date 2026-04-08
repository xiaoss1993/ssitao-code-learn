# 步骤2：Stream API - 流水线数据处理

---

## 2.1 Stream概述

### 2.1.1 什么是Stream

```
Stream = 数据流处理API
- 不是数据结构，不存储数据
- 不修改源数据
- 惰性求值（Lazy Evaluation）
- 一次性使用（不可重用）

核心概念：
  数据源（Source） -> 中间操作（Intermediate） -> 终止操作（Terminal）
```

### 2.1.2 Stream vs Collection

```java
// Collection：存储数据
List<String> list = Arrays.asList("a", "b", "c");
for (String s : list) {  // 外部迭代
    System.out.println(s);
}

// Stream：处理数据
list.stream()              // 获取Stream
    .forEach(s -> System.out.println(s));  // 内部迭代
```

---

## 2.2 创建Stream

### 2.2.1 从集合创建

```java
List<String> list = Arrays.asList("a", "b", "c");

// 从List创建
Stream<String> stream1 = list.stream();

// 从Set创建
Set<String> set = new HashSet<>(list);
Stream<String> stream2 = set.stream();

// 从Map创建
Map<String, Integer> map = new HashMap<>();
map.put("a", 1);
map.put("b", 2);

// 获取键的Stream
Stream<String> keys = map.keySet().stream();
// 获取值的Stream
Stream<Integer> values = map.values().stream();
// 获取键值对的Stream
Stream<Map.Entry<String, Integer>> entries = map.entrySet().stream();
```

### 2.2.2 从数组创建

```java
String[] array = {"a", "b", "c"};

// Arrays.stream
Stream<String> stream1 = Arrays.stream(array);
Stream<String> stream2 = Arrays.stream(array, 0, 2);  // 指定范围

// Stream.of
Stream<String> stream3 = Stream.of("a", "b", "c");
Stream<String> stream4 = Stream.of(array);
```

### 2.2.3 特殊Stream创建

```java
// 空Stream
Stream<String> empty = Stream.empty();

// 无限Stream - iterate
Stream<Integer> naturals = Stream.iterate(1, n -> n + 1);  // 1, 2, 3, ...
Stream<Integer> naturalsLimit = Stream.iterate(1, n -> n <= 10, n -> n + 1);  // JDK 9+

// 无限Stream - generate
Stream<Double> randoms = Stream.generate(Math::random);
Stream<String> constantly = Stream.generate(() -> "constant");

// 基本类型Stream
IntStream intStream = IntStream.range(1, 10);        // 1-9
IntStream intStream2 = IntStream.rangeClosed(1, 10); // 1-10
LongStream longStream = LongStream.of(1, 2, 3);
DoubleStream doubleStream = DoubleStream.generate(() -> 1.0);
```

### 2.2.4 从其他来源创建

```java
// 从文件
try (Stream<String> lines = Files.lines(Paths.get("file.txt"))) {
    lines.forEach(System.out::println);
}

// 从字符串
Stream<String> stringStream = Pattern.compile(",").splitAsStream("a,b,c");

// 从Builder
Stream<String> builderStream = Stream.<String>builder()
    .add("a")
    .add("b")
    .add("c")
    .build();
```

---

## 2.3 中间操作

### 2.3.1 filter - 过滤

```java
// filter: 保留满足条件的元素
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 保留偶数
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// [2, 4, 6, 8, 10]

// 保留长度大于3的字符串
List<String> words = Arrays.asList("hi", "hello", "world", "java");
List<String> longWords = words.stream()
    .filter(w -> w.length() > 3)
    .collect(Collectors.toList());
// [hello, world]
```

### 2.3.2 map - 转换

```java
// map: 转换每个元素

// 字符串转大写
List<String> words = Arrays.asList("hello", "world");
List<String> upper = words.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// [HELLO, WORLD]

// 提取对象属性
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 30)
);
List<String> names = people.stream()
    .map(Person::getName)
    .collect(Collectors.toList());
// [Alice, Bob]

// 类型转换
List<String> strings = Arrays.asList("1", "2", "3");
List<Integer> ints = strings.stream()
    .map(Integer::parseInt)
    .collect(Collectors.toList());
// [1, 2, 3]

// mapToInt/mapToLong/mapToDouble
List<String> strNums = Arrays.asList("1", "2", "3");
int sum = strNums.stream()
    .mapToInt(Integer::parseInt)
    .sum();
// 6
```

### 2.3.3 flatMap - 扁平化

```java
// flatMap: 将每个元素转换为流，然后合并

// 例子：将句子拆分为单词
List<String> sentences = Arrays.asList("hello world", "java stream");
List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .collect(Collectors.toList());
// [hello, world, java, stream]

// 例子：去重并合并
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2),
    Arrays.asList(2, 3),
    Arrays.asList(3, 4)
);
List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .distinct()
    .collect(Collectors.toList());
// [1, 2, 3, 4]

// 例子：获取所有教师的的所有学生
List<Teacher> teachers = getTeachers();
List<Student> allStudents = teachers.stream()
    .flatMap(t -> t.getStudents().stream())
    .collect(Collectors.toList());
```

### 2.3.4 distinct - 去重

```java
// distinct: 去除重复元素（使用equals）

List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4);
List<Integer> distinct = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
// [1, 2, 3, 4]

// 对象去重需要正确实现equals/hashCode
List<Person> people = ...;
List<Person> distinctPeople = people.stream()
    .distinct()
    .collect(Collectors.toList());
```

### 2.3.5 sorted - 排序

```java
// sorted: 排序

List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);

// 自然顺序
List<Integer> sorted = numbers.stream()
    .sorted()
    .collect(Collectors.toList());
// [1, 1, 2, 3, 4, 5, 6, 9]

// 降序
List<Integer> desc = numbers.stream()
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());

// 自定义排序
List<String> words = Arrays.asList("bb", "aaa", "c");
List<String> byLength = words.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());
// [c, bb, aaa]

// 多条件排序
List<Person> people = Arrays.asList(
    new Person("Bob", 25),
    new Person("Alice", 30),
    new Person("Bob", 20)
);
List<Person> sortedPeople = people.stream()
    .sorted(Comparator.comparing(Person::getName)
                      .thenComparingInt(Person::getAge))
    .collect(Collectors.toList());
```

### 2.3.6 limit 和 skip

```java
// limit(n): 取前n个元素

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<Integer> limited = numbers.stream()
    .limit(3)
    .collect(Collectors.toList());
// [1, 2, 3]

// skip(n): 跳过前n个元素
List<Integer> skipped = numbers.stream()
    .skip(2)
    .collect(Collectors.toList());
// [3, 4, 5]

// 分页
int page = 2;
int pageSize = 3;
List<Integer> pageData = numbers.stream()
    .skip((page - 1) * pageSize)
    .limit(pageSize)
    .collect(Collectors.toList());
// [4, 5]

// 获取前3个偶数
numbers.stream()
    .filter(n -> n % 2 == 0)
    .limit(3)
    .collect(Collectors.toList());
```

### 2.3.7 peek - 调试

```java
// peek: 查看流中的元素（不改变流），用于调试

List<Integer> result = Arrays.asList(1, 2, 3, 4, 5)
    .stream()
    .peek(n -> System.out.println("Before filter: " + n))
    .filter(n -> n % 2 == 0)
    .peek(n -> System.out.println("After filter: " + n))
    .map(n -> n * 2)
    .peek(n -> System.out.println("After map: " + n))
    .collect(Collectors.toList());
// Before filter: 1
// Before filter: 2
// After filter: 2
// After map: 4
// ... etc
```

### 2.3.8 takeWhile 和 dropWhile (JDK 9+)

```java
// takeWhile: 从头开始取，满足条件就停止
List<Integer> numbers = Arrays.asList(1, 3, 5, 6, 7, 8);
List<Integer> result = numbers.stream()
    .takeWhile(n -> n % 2 == 1)  // 遇到偶数6就停止
    .collect(Collectors.toList());
// [1, 3, 5]

// dropWhile: 从头开始丢弃，满足条件就停止
List<Integer> result2 = numbers.stream()
    .dropWhile(n -> n % 2 == 1)  // 遇到偶数6就停止丢弃
    .collect(Collectors.toList());
// [6, 7, 8]
```

---

## 2.4 终止操作

### 2.4.1 collect - 收集

```java
// collect: 将流转换为集合或字符串

List<String> words = Arrays.asList("hello", "world");

// 收集为List
List<String> list = words.stream().collect(Collectors.toList());
List<String> list2 = words.stream().toList();  // JDK 16+

// 收集为Set
Set<String> set = words.stream().collect(Collectors.toSet());

// 收集为Map
Map<String, Integer> map = words.stream()
    .collect(Collectors.toMap(Function.identity(), String::length));
// {hello=5, world=5}

// 分组收集
List<Person> people = getPeople();
Map<String, List<Person>> byCity = people.stream()
    .collect(Collectors.groupingBy(Person::getCity));

// 分区（按条件分为两组）
Map<Boolean, List<Person>> byAge = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() > 18));

// 统计
IntSummaryStatistics stats = people.stream()
    .collect(Collectors.summarizingInt(Person::getAge));
System.out.println("Average age: " + stats.getAverage());

// 拼接字符串
String joined = words.stream()
    .collect(Collectors.joining(", "));
// hello, world
```

### 2.4.2 forEach - 遍历

```java
// forEach: 遍历每个元素

List<String> words = Arrays.asList("hello", "world");

// 基本用法
words.stream().forEach(System.out::println);

// 带索引（JDK 9+）
AtomicInteger index = new AtomicInteger();
words.stream()
    .forEach(item -> System.out.println(index.getAndIncrement() + ": " + item));

// 注意：forEach是终止操作，不是中间操作
// words.forEach(); // 这是Collection的forEach
```

### 2.4.3 count, min, max

```java
// count: 计数
long count = list.stream().filter(s -> s.length() > 3).count();

// min: 最小值
Optional<String> min = list.stream()
    .min(Comparator.comparingInt(String::length));

// max: 最大值
Optional<String> max = list.stream()
    .max(Comparator.comparingInt(String::length));

// 注意：返回Optional，需要处理
String result = list.stream()
    .min(Comparator.comparingInt(String::length))
    .orElse("default");
```

### 2.4.4 reduce - 归约

```java
// reduce: 将所有元素组合成一个值

// 无初始值 - 返回Optional
Optional<Integer> sum = numbers.stream()
    .reduce((a, b) -> a + b);

// 有初始值
int sum2 = numbers.stream()
    .reduce(0, (a, b) -> a + b);

// 使用方法引用
int sum3 = numbers.stream()
    .reduce(0, Integer::sum);

// 求乘积
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);

// 找最长字符串
Optional<String> longest = words.stream()
    .reduce((a, b) -> a.length() > b.length() ? a : b);

// 字符串拼接
String concat = words.stream()
    .reduce("", String::concat);
```

### 2.4.5 match - 匹配

```java
// anyMatch: 任一元素满足
boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);

// allMatch: 所有元素满足
boolean allPositive = numbers.stream().allMatch(n -> n > 0);

// noneMatch: 所有元素都不满足
boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);

// 注意：短路操作，找到答案就停止
```

### 2.4.6 find - 查找

```java
// findFirst: 返回第一个元素（Optional）
Optional<Integer> first = numbers.stream()
    .filter(n -> n > 3)
    .findFirst();

// findAny: 返回任意一个元素（Optional）
Optional<Integer> any = numbers.stream()
    .filter(n -> n > 3)
    .findAny();

// 在并行流中，findAny可能比findFirst更快
```

### 2.4.7 toArray - 转换为数组

```java
String[] array = words.stream().toArray(String[]::new);

// 或
Object[] array2 = words.stream().toArray();
```

---

## 2.5 短路操作

```java
// 以下操作在找到结果后会立即停止（短路）

// 1. anyMatch, allMatch, noneMatch
// 一旦确定结果就停止

// 2. findFirst, findAny
// 找到第一个就停止

// 3. limit
// 达到数量就停止

// 示例：找一个长度大于3的字符串
Optional<String> found = words.stream()
    .filter(w -> w.length() > 3)
    .findFirst();
// 一旦找到符合条件的就停止搜索
```

---

## 2.6 并行流

### 2.6.1 使用并行流

```java
// parallel() 将顺序流转换为并行流
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

int sum = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .mapToInt(n -> n)
    .sum();

// 或
int sum2 = numbers.stream()
    .parallel()
    .filter(n -> n % 2 == 0)
    .mapToInt(n -> n)
    .sum();

// parallelStream() 直接创建并行流
int sum3 = IntStream.range(1, 11)
    .parallel()
    .filter(n -> n % 2 == 0)
    .sum();
```

### 2.6.2 并行流注意事项

```java
// 1. 有状态的操作可能有问题
// distinct, sorted 在并行流中需要额外同步

// 2. 副作用
// 并行流中要避免有副作用的操作
// 不要在Lambda中修改共享变量

// 错误示例
int[] sum = {0};
numbers.parallelStream()
    .forEach(n -> sum[0] += n);  // 不安全！

// 正确做法
int sum = numbers.parallelStream()
    .reduce(0, Integer::sum);  // 使用reduce

// 3. 顺序无关的操作
// 使用findFirst时要小心，并行流中可能返回非第一个
// 使用findAny更安全

// 4. 判断是否需要并行
// 大数据量、计算密集、顺序无关 -> 使用并行流
// 小数据量、I/O密集、有状态 -> 使用顺序流
```

---

## 2.7 常用操作速查表

### 2.7.1 中间操作

| 操作 | 签名 | 说明 |
|------|------|------|
| filter | `Stream<T> filter(Predicate<? super T>)` | 过滤 |
| map | `<R> Stream<R> map(Function<? super T, ? extends R>)` | 转换 |
| flatMap | `<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>>)` | 扁平化 |
| distinct | `Stream<T> distinct()` | 去重 |
| sorted | `Stream<T> sorted()` / `sorted(Comparator)` | 排序 |
| limit | `Stream<T> limit(long maxSize)` | 截取 |
| skip | `Stream<T> skip(long n)` | 跳过 |
| peek | `Stream<T> peek(Consumer<? super T>)` | 调试 |

### 2.7.2 终止操作

| 操作 | 签名 | 返回类型 |
|------|------|----------|
| collect | `<R, A> R collect(Collector<? super T, A, R>)` | R |
| forEach | `void forEach(Consumer<? super T>)` | void |
| forEachOrdered | `void forEachOrdered(Consumer<? super T>)` | void |
| count | `long count()` | long |
| min | `Optional<T> min(Comparator<? super T>)` | Optional |
| max | `Optional<T> max(Comparator<? super T>)` | Optional |
| reduce | `Optional<T> reduce(BinaryOperator<T>)` | Optional |
| anyMatch | `boolean anyMatch(Predicate<? super T>)` | boolean |
| allMatch | `boolean allMatch(Predicate<? super T>)` | boolean |
| noneMatch | `boolean noneMatch(Predicate<? super T>)` | boolean |
| findFirst | `Optional<T> findFirst()` | Optional |
| findAny | `Optional<T> findAny()` | Optional |

---

## 2.8 实战示例

### 2.8.1 数据转换

```java
// 将List<Person>转换为Map<String, List<String>>
Map<String, List<String>> byCity = people.stream()
    .collect(Collectors.groupingBy(
        Person::getCity,
        Collectors.mapping(Person::getName, Collectors.toList())
    ));

// 提取多个字段
List<Transaction> transactions = getTransactions();

// 按年份分组，每组取金额最高的
Map<Integer, Optional<Transaction>> topByYear = transactions.stream()
    .collect(Collectors.groupingBy(
        Transaction::getYear,
        Collectors.maxBy(Comparator.comparingDouble(Transaction::getAmount))
    ));

// 计算每个部门的平均工资
Map<String, Double> avgSalaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDepartment,
        Collectors.averagingDouble(Employee::getSalary)
    ));
```

### 2.8.2 数据统计

```java
// 统计词频
Map<String, Long> wordCount = text.lines()
    .flatMap(line -> Arrays.stream(line.toLowerCase().split("\\s+")))
    .filter(word -> !word.isEmpty())
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

// 按值排序取Top N
List<Map.Entry<String, Long>> topWords = wordCount.entrySet().stream()
    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
    .limit(10)
    .collect(Collectors.toList());

// 计算百分比
DoubleSummaryStatistics stats = values.stream()
    .mapToDouble(Double::doubleValue)
    .summaryStatistics();

double percentile95 = stats.getMin() + 0.95 * (stats.getMax() - stats.getMin());
```

### 2.8.3 条件筛选

```java
// 多条件过滤
List<Product> products = getProducts();

List<Product> result = products.stream()
    .filter(p -> p.getPrice() <= 100)
    .filter(p -> p.getCategory().equals("electronics"))
    .sorted(Comparator.comparing(Product::getPrice).reversed())
    .collect(Collectors.toList());

// 动态条件
public List<Product> filterProducts(List<Product> products,
                                    String category,
                                    Double maxPrice,
                                    Boolean inStock) {
    return products.stream()
        .filter(p -> category == null || p.getCategory().equals(category))
        .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
        .filter(p -> inStock == null || p.getInStock() == inStock)
        .collect(Collectors.toList());
}
```

---

## 2.9 性能优化

### 2.9.1 操作的顺序

```java
// 减少操作次数
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// 不好：先filter再map，要处理更多数据
numbers.stream()
    .filter(n -> n % 2 == 0)  // 5个
    .map(n -> n * 2)
    .collect(Collectors.toList());

// 好：先map再filter
numbers.stream()
    .map(n -> n * 2)
    .filter(n -> n % 4 == 0)  // 2个
    .collect(Collectors.toList());

// 使用limit短路
numbers.stream()
    .filter(n -> n > 0)
    .limit(1)
    .collect(Collectors.toList());
```

### 2.9.2 使用正确类型

```java
// 使用基本类型流避免装箱拆箱
List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5);

// 不好：IntStream转Integer再求和
int sum = ints.stream()
    .map(Integer::doubleValue)  // 装箱
    .mapToInt(Double::intValue)  // 拆箱
    .sum();

// 好：直接用mapToInt
int sum2 = ints.stream()
    .mapToInt(Integer::intValue)
    .sum();

// 更好：用IntStream
int sum3 = IntStream.of(1, 2, 3, 4, 5).sum();
```

---

## 2.10 练习题

```java
// 1. 使用Stream找出列表中的最大值

// 2. 使用Stream统计单词出现频率

// 3. 以下代码输出什么？
IntStream.range(1, 10)
    .filter(n -> n % 2 == 0)
    .limit(2)
    .forEach(System.out::print);

// 4. 重写以下代码为Stream
List<String> result = new ArrayList<>();
for (String s : list) {
    if (s.length() > 3) {
        result.add(s.toUpperCase());
    }
}

// 5. 使用并行流注意事项有哪些？
```

---

## 2.11 参考答案

```java
// 1. 找最大值
Optional<Integer> max = numbers.stream()
    .max(Integer::compareTo);
int maxVal = max.orElseThrow();

// 2. 统计单词频率
Map<String, Long> wordFreq = Arrays.asList("hello", "world", "hello", "java")
    .stream()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
// {hello=2, world=1, java=1}

// 3. 输出: 24
// range(1,10) = 1-9, filter偶数 = 2,4,6,8, limit(2) = 2,4

// 4. Stream重写
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

// 5. 并行流注意事项
//    - 避免共享变量修改
//    - 有状态操作可能有问题
//    - 小数据量不一定更快
//    - 线程安全的数据源
```

---

[返回目录](./README.md)

## 第四阶段总结

### 核心知识点

| 步骤 | 主题 | 核心概念 |
|------|------|----------|
| 1 | Lambda | 函数式接口、方法引用、闭包 |
| 2 | Stream | 惰性求值、中间操作、终止操作、并行流 |
