# 第四阶段：Lambda与Stream API

## 学习目标

掌握Java 8函数式编程的核心特性：Lambda表达式和Stream API。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | Lambda表达式 | [Step01_Lambda.md](./Step01_Lambda.md) | [lambda/*.java](./lambda/) |
| 2 | Stream API | [Step02_StreamAPI.md](./Step02_StreamAPI.md) | [stream/*.java](./stream/) |

---

## 核心概念概览

### Lambda表达式

```
Lambda = 匿名函数 = 函数式接口的实例

语法:
  (params) -> expression
  (params) -> { statements; }
  () -> expression
  (type1 param1, type2 param2) -> result

函数式接口:
  @FunctionalInterface
  - Runnable: void run()
  - Supplier<T>: T get()
  - Consumer<T>: void accept(T t)
  - Function<T,R>: R apply(T t)
  - Predicate<T>: boolean test(T t)
  - BiFunction<T,U,R>: R apply(T t, U u)
  - BiConsumer<T,U>: void accept(T t, U u)
```

### Stream API

```
Stream = 流水线数据处理

创建:
  Stream.of()
  Arrays.stream()
  collection.stream()
  Stream.iterate()
  Stream.generate()

操作:
  filter(Predicate)      - 过滤
  map(Function)          - 转换
  flatMap(Function)      - 扁平化
  distinct()             - 去重
  sorted(Comparator)    - 排序
  limit(n)               - 截取
  skip(n)                - 跳过

终止操作:
  collect(Collectors.toList())
  forEach(Consumer)
  count()
  max/min(Comparator)
  reduce(identity, accumulator)
  anyMatch/allMatch/noneMatch
  findFirst/findAny
```

---

## 学习建议

1. **Lambda**: 重点理解函数式接口和函数式接口的类型推断
2. **Stream**: 理解惰性求值和管道流的概念
3. **实践**: 用Stream重写传统循环操作

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase04.lambda.LambdaDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase04.stream.StreamDemo"
```

---

## 下一步

[第五阶段：并发编程](../phase05/README.md)
