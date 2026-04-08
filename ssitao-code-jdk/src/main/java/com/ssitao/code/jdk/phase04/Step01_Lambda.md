# 步骤1：Lambda表达式 - 函数式编程入门

---

## 1.1 Lambda概述

### 1.1.1 什么是Lambda表达式

```java
// Lambda表达式 = 匿名函数 = 函数式接口的实例

// 传统写法：匿名内部类
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

// Lambda写法
Runnable r2 = () -> System.out.println("Hello");

// 执行
r1.run();
r2.run();
```

### 1.1.2 Lambda的基本语法

```java
// 完整语法
(int a, int b) -> { return a + b; }

// 简化1：参数类型可以省略
(a, b) -> { return a + b; }

// 简化2：只有单个参数，括号可以省略
x -> x * 2

// 简化3：只有单个表达式，return和花括号可以省略
x -> x * 2

// 无参数
() -> System.out.println("Hello")

// 多行语句
(x, y) -> {
    int sum = x + y;
    System.out.println(sum);
    return sum;
}
```

### 1.1.3 Lambda与函数式接口

```java
// 函数式接口：只有一个抽象方法的接口
@FunctionalInterface
interface Calculator {
    int calculate(int a, int b);
}

// 使用Lambda
Calculator add = (a, b) -> a + b;
Calculator multiply = (a, b) -> a * b;

System.out.println(add.calculate(1, 2));       // 3
System.out.println(multiply.calculate(3, 4));   // 12

// @FunctionalInterface注解不是必须的，但建议添加
// 它会在你添加多个抽象方法时报错
```

---

## 1.2 内置函数式接口

### 1.2.1 Supplier<T> - 生产者

```java
// Supplier<T>: 不接受参数，返回T
// T get()

Supplier<String> supplier1 = () -> "Hello";
Supplier<LocalDate> supplier2 = () -> LocalDate.now();
Supplier<Random> supplier3 = Random::new;

System.out.println(supplier1.get());  // Hello
System.out.println(supplier2.get());  // 2026-04-08
```

### 1.2.2 Consumer<T> - 消费者

```java
// Consumer<T>: 接受T，无返回值
// void accept(T t)

// 简单示例
Consumer<String> consumer1 = s -> System.out.println(s);
consumer1.accept("Hello");  // 打印 Hello

// 链式Consumer
Consumer<String> consumer2 = s -> System.out.println("1: " + s);
Consumer<String> consumer3 = s -> System.out.println("2: " + s);
Consumer<String> combined = consumer2.andThen(consumer3);
combined.accept("Hello");
// 输出:
// 1: Hello
// 2: Hello

// 实际应用：遍历打印
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
names.forEach(name -> System.out.println(name));
names.forEach(System.out::println);  // 方法引用简化
```

### 1.2.3 BiConsumer<T,U> - 双参数消费者

```java
// BiConsumer<T,U>: 接受两个参数，无返回值
// void accept(T t, U u)

BiConsumer<String, Integer> printPair = (name, age) ->
    System.out.println(name + " is " + age + " years old");

printPair.accept("Alice", 25);  // Alice is 25 years old

// Map的forEach使用BiConsumer
Map<String, Integer> ages = new HashMap<>();
ages.put("Alice", 25);
ages.put("Bob", 30);
ages.forEach((name, age) -> System.out.println(name + ": " + age));
```

### 1.2.4 Function<T,R> - 转换

```java
// Function<T,R>: 接受T，返回R
// R apply(T t)

// 字符串转整数
Function<String, Integer> strToInt = s -> Integer.parseInt(s);
System.out.println(strToInt.apply("123"));  // 123

// 链式Function
Function<String, String> trim = s -> s.trim();
Function<String, String> uppercase = s -> s.toUpperCase();
Function<String, String> pipeline = trim.andThen(uppercase);
System.out.println(pipeline.apply("  hello  "));  // HELLO

// compose: 先执行参数function，再执行调用者
Function<String, String> result = trim.compose(uppercase);
System.out.println(result.apply("  HELLO  "));  // hello
```

### 1.2.5 BiFunction<T,U,R> - 双参数转换

```java
// BiFunction<T,U,R>: 接受两个参数，返回R
// R apply(T t, U u)

BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;
System.out.println(concat.apply("Hello", " World"));  // Hello World

BiFunction<Integer, Integer, Double> average = (a, b) -> (a + b) / 2.0;
System.out.println(average.apply(10, 20));  // 15.0
```

### 1.2.6 Predicate<T> - 断言

```java
// Predicate<T>: 接受T，返回boolean
// boolean test(T t)

// 判断是否为空
Predicate<String> isEmpty = s -> s.isEmpty();
System.out.println(isEmpty.test(""));      // true
System.out.println(isEmpty.test("hello")); // false

// 组合Predicate
Predicate<String> isNotEmpty = isEmpty.negate();
Predicate<String> hasLength = s -> s.length() > 3;

// and: 两个条件都满足
Predicate<String> isValid = isNotEmpty.and(hasLength);
System.out.println(isValid.test(""));      // false
System.out.println(isValid.test("ab"));    // false
System.out.println(isValid.test("abcd"));  // true

// or: 满足任一条件
Predicate<String> startsWithA = s -> s.startsWith("A");
Predicate<String> isLong = s -> s.length() > 10;
Predicate<String> combined = startsWithA.or(isLong);

// isEqual: 判断相等
Predicate<Object> isNull = Objects::isNull;
Predicate<Object> isEqual = Objects::isEqual("hello");
Predicate<Object> isHello = Predicate.isEqual("hello");
```

### 1.2.7 BiPredicate<T,U> - 双参数断言

```java
// BiPredicate<T,U>: 接受两个参数，返回boolean
// boolean test(T t, U u)

BiPredicate<String, Integer> nameAndAge = (name, age) ->
    name.length() > 3 && age > 18;

System.out.println(nameAndAge.test("Alice", 25));    // true
System.out.println(nameAndAge.test("Bob", 20));     // false
```

### 1.2.8 UnaryOperator<T> 和 BinaryOperator<T>

```java
// UnaryOperator<T>: Function<T,T>的特化，输入输出类型相同
// T apply(T t)

UnaryOperator<Integer> doubleIt = x -> x * 2;
UnaryOperator<String> uppercase = String::toUpperCase;

// 等价于 Function.identity()
UnaryOperator<String> identity = Function.identity();

// BinaryOperator<T>: BiFunction<T,T,T>的特化
// T apply(T t1, T t2)

BinaryOperator<Integer> add = (a, b) -> a + b;
BinaryOperator<Integer> max = Integer::max;
BinaryOperator<Integer> min = Integer::min;

System.out.println(add.apply(1, 2));   // 3
System.out.println(max.apply(1, 2));  // 2
System.out.println(min.apply(1, 2));  // 1
```

---

## 1.3 方法引用

### 1.3.1 四种方法引用类型

```java
class Person {
    private String name;
    private int age;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void staticMethod() {
        System.out.println("Static method");
    }
}

// 1. 静态方法引用: ClassName::staticMethod
Function<String, Integer> parser = Integer::parseInt;
System.out.println(parser.apply("123"));  // 123

Consumer<String> printer = System.out::println;
printer.accept("Hello");

// 2. 实例方法引用-特定对象: object::instanceMethod
Person person = new Person("Alice");
Supplier<String> nameGetter = person::getName;
System.out.println(nameGetter.get());  // Alice

// 3. 实例方法引用-任意对象: ClassName::instanceMethod
Function<String, String> upper = String::toUpperCase;
System.out.println(upper.apply("hello"));  // HELLO

// 这是因为"hello"::toUpperCase等价于 x -> x.toUpperCase()
// 常用于Comparator
Comparator<Person> byName = Comparator.comparing(Person::getName);

// 4. 构造方法引用: ClassName::new
Supplier<Person> personCreator = Person::new;
Person p = personCreator.get();  // 调用new Person()

Function<String, Person> personCreator2 = Person::new;
Person p2 = personCreator2.apply("Alice");  // 调用new Person(String)
```

### 1.3.2 方法引用示例对比

```java
// Lambda表达式 vs 方法引用

// Lambda
list.stream().map(s -> s.toUpperCase()).collect(Collectors.toList());

// 方法引用
list.stream().map(String::toUpperCase).collect(Collectors.toList());

// Lambda
list.forEach(s -> System.out.println(s));

// 方法引用
list.forEach(System.out::println);

// Lambda
button.setOnAction(event -> handle(event));

// 方法引用
button.setOnAction(this::handle);
```

---

## 1.4 Lambda表达式作用域

### 1.4.1 访问局部变量

```java
// Lambda可以访问外部的final或有效final的变量
int num = 10;  // 有效final
Consumer<String> consumer = s -> System.out.println(s + num);

// 注意：Lambda不能修改外部变量（隐式final）
// num = 20;  // 编译错误！
```

### 1.4.2 访问成员变量和静态变量

```java
class Example {
    private int instanceVar = 10;
    private static int staticVar = 20;

    public void demo() {
        // Lambda可以访问和修改实例变量
        Consumer<Integer> c1 = x -> {
            this.instanceVar = x;  // 可以
        };

        // Lambda可以访问和修改静态变量
        Consumer<Integer> c2 = x -> {
            staticVar = x;  // 可以
        };
    }
}
```

### 1.4.3 this引用

```java
class LambdaThis {
    private String value = "outer";

    public void demo() {
        // Lambda中的this指的是包围它的类的实例
        Runnable r = () -> {
            System.out.println(this.value);  // "outer"
        };
        r.run();
    }

    public String toString() {
        return "LambdaThis";
    }
}

// 对比：匿名内部类中的this指的是匿名内部类自己
class AnonymousThis {
    private String value = "outer";

    public void demo() {
        Runnable r = new Runnable() {
            private String value = "inner";

            @Override
            public void run() {
                System.out.println(this.value);  // "inner"
            }
        };
        r.run();
    }
}
```

---

## 1.5 常用函数式接口速查表

| 接口 | 方法 | 说明 |
|------|------|------|
| `Supplier<T>` | `T get()` | 生产者 |
| `Consumer<T>` | `void accept(T)` | 单参数消费者 |
| `BiConsumer<T,U>` | `void accept(T,U)` | 双参数消费者 |
| `Function<T,R>` | `R apply(T)` | 单参数转换 |
| `BiFunction<T,U,R>` | `R apply(T,U)` | 双参数转换 |
| `Predicate<T>` | `boolean test(T)` | 断言 |
| `BiPredicate<T,U>` | `boolean test(T,U)` | 双参数断言 |
| `UnaryOperator<T>` | `T apply(T)` | 一元运算 |
| `BinaryOperator<T>` | `T apply(T,T)` | 二元运算 |
| `Runnable` | `void run()` | 无参数无返回值 |

---

## 1.6 Lambda的实际应用

### 1.6.1 集合排序

```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

// 传统Comparator
names.sort(new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
});

// Lambda表达式
names.sort((s1, s2) -> s1.compareTo(s2));

// 更简化
names.sort(String::compareTo);

// 降序
names.sort(Comparator.reverseOrder());

// 按长度排序
names.sort(Comparator.comparingInt(String::length));

// 多条件排序
List<Person> people = Arrays.asList(
    new Person("Alice", 25),
    new Person("Bob", 20),
    new Person("Alice", 30)
);
people.sort(Comparator.comparing(Person::getName)
                      .thenComparingInt(Person::getAge));
```

### 1.6.2 事件处理

```java
// Swing按钮事件
button.addActionListener(e -> System.out.println("Button clicked"));

// JavaFX
button.setOnAction(e -> handle());
button.setOnAction(this::handle);

// 简化事件对象使用
button.addActionListener(e -> {
    String action = e.getActionCommand();
    System.out.println("Action: " + action);
});
```

### 1.6.3 延迟执行

```java
// 使用Supplier实现延迟计算
public class LazyDemo {
    public static void main(String[] args) {
        // 即使HeavyObject构造很耗时，这里也不会真正创建
        Supplier<HeavyObject> lazyObject = HeavyObject::new;

        System.out.println("Object created?");
        // 只有在调用get()时才会真正创建
        HeavyObject obj = lazyObject.get();
    }
}

// 对比：如果不使用延迟
HeavyObject obj = new HeavyObject();  // 立即创建
System.out.println("Object created?");
```

### 1.6.4 条件执行

```java
// 使用Function实现策略模式
public class StrategyDemo {
    public static void main(String[] args) {
        Map<String, Function<String, String>> strategies = new HashMap<>();
        strategies.put("upper", String::toUpperCase);
        strategies.put("lower", String::toLowerCase);
        strategies.put("trim", String::trim);

        String input = "  Hello  ";
        String mode = "upper";

        String result = strategies.getOrDefault(mode, Function.identity())
                                   .apply(input);
        System.out.println(result);  // HELLO
    }
}
```

---

## 1.7 练习题

```java
// 1. 将以下代码改写为Lambda表达式
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello World");
    }
};

// 2. 写一个方法，使用Predicate过滤列表

// 3. 使用Function实现字符串反转和去空格

// 4. 说明以下代码是否能编译
List<String> list = Arrays.asList("a", "b", "c");
list.forEach(s -> {
    System.out.println(s);
    if (s.equals("a")) {
        return;  // 这个return的作用是什么？
    }
});

// 5. 用方法引用替换以下Lambda
list.stream()
    .map(s -> s.toUpperCase())
    .filter(s -> s.length() > 3)
    .collect(Collectors.toList());
```

---

## 1.8 参考答案

```java
// 1. Lambda改写
Runnable r = () -> System.out.println("Hello World");

// 2. Predicate过滤
public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    List<T> result = new ArrayList<>();
    for (T item : list) {
        if (predicate.test(item)) {
            result.add(item);
        }
    }
    return result;
}

// 使用
filter(names, s -> s.length() > 3);

// 3. 字符串处理
Function<String, String> process = s -> {
    s = s.trim();
    return new StringBuilder(s).reverse().toString();
};
// 或组合
Function<String, String> trim = String::trim;
Function<String, String> reverse = s -> new StringBuilder(s).reverse().toString();
Function<String, String> processChain = trim.andThen(reverse);

// 4. 解析
// 可以编译！这里的return只是结束当前Lambda，不影响forEach继续执行
// 它只结束当前的lambda表达式，不算break或continue

// 5. 方法引用
list.stream()
    .map(String::toUpperCase)
    .filter(s -> s.length() > 3)  // filter不能简化为方法引用
    .collect(Collectors.toList());
```

---

[返回目录](./README.md) | [下一步：Stream API](./Step02_StreamAPI.md)
