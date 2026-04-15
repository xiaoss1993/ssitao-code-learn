# 第14课：Java 8新特性

## 核心概念

### 14.1 Lambda表达式
- 匿名函数
- 语法：`(参数) -> { 方法体 }`
- 简化函数式接口实现

### 14.2 方法引用
- `类名::静态方法`
- `对象::实例方法`
- `类名::实例方法`
- `类::new`

### 14.3 Stream API
- `filter()` - 过滤
- `map()` - 转换
- `sorted()` - 排序
- `collect()` - 收集

### 14.4 接口默认方法
- 接口可以定义default方法
- 多继承冲突解决规则

### 14.5 Optional
- 解决NPE问题
- 链式处理

## 代码示例

### 示例1：Lambda表达式
```java
import java.util.*;

public class LambdaDemo {
    public static void main(String[] args) {
        // 原始写法
        Comparator<String> comp1 = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        };

        // Lambda写法
        Comparator<String> comp2 = (s1, s2) -> s1.length() - s2.length();

        // Lambda体只有一条语句
        Comparator<String> comp3 = (s1, s2) -> s1.length() - s2.length();

        // Lambda体有多条语句
        Comparator<String> comp4 = (s1, s2) -> {
            int result = s1.length() - s2.length();
            return result;
        };

        // 参数类型可以省略
        Comparator<String> comp5 = (s1, s2) -> s1.length() - s2.length();

        List<String> list = Arrays.asList("apple", "banana", "orange");
        Collections.sort(list, (s1, s2) -> s1.length() - s2.length());
        System.out.println(list);
    }
}
```

### 示例2：方法引用
```java
import java.util.*;
import java.util.function.*;

public class MethodReferenceDemo {
    public static void main(String[] args) {
        // 静态方法引用
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println(parser.apply("123"));  // 123

        // 实例方法引用
        String str = "Hello";
        Supplier<Integer> len = str::length;
        System.out.println(len.get());  // 5

        // 对象方法引用
        List<String> names = Arrays.asList("Tom", "Jerry", "Mike");
        names.forEach(System.out::println);

        // 构造器引用
        Supplier<ArrayList<String>> listSupplier = ArrayList::new;
        ArrayList<String> list = listSupplier.get();

        // 数组构造器引用
        IntFunction<int[]> arrayCreator = int[]::new;
        int[] array = arrayCreator.apply(5);
    }
}
```

### 示例3：Stream基础操作
```java
import java.util.*;
import java.util.stream.*;

public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter - 过滤
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("偶数：" + evens);  // [2, 4, 6, 8, 10]

        // map - 转换
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("平方：" + squares);

        // limit - 限制
        List<Integer> first3 = numbers.stream()
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("前3个：" + first3);  // [1, 2, 3]

        // skip - 跳过
        List<Integer> skip3 = numbers.stream()
            .skip(3)
            .collect(Collectors.toList());
        System.out.println("跳过前3个：" + skip3);  // [4, 5, 6, 7, 8, 9, 10]

        // sorted - 排序
        List<Integer> sorted = numbers.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        System.out.println("降序：" + sorted);

        // distinct - 去重
        List<Integer> withDup = Arrays.asList(1, 2, 2, 3, 3, 3);
        List<Integer> unique = withDup.stream()
            .distinct()
            .collect(Collectors.toList());
        System.out.println("去重：" + unique);  // [1, 2, 3]
    }
}
```

### 示例4：Stream聚合操作
```java
import java.util.*;
import java.util.stream.*;

public class StreamAggregateDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // count
        long count = numbers.stream().count();
        System.out.println("数量：" + count);

        // sum
        int sum = numbers.stream().mapToInt(Integer::intValue).sum();
        System.out.println("求和：" + sum);

        // average
        OptionalDouble avg = numbers.stream().mapToInt(Integer::intValue).average();
        System.out.println("平均值：" + avg.orElse(0));

        // max/min
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        Optional<Integer> min = numbers.stream().min(Integer::compareTo);
        System.out.println("最大值：" + max.orElse(0));
        System.out.println("最小值：" + min.orElse(0));

        // reduce
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println("乘积：" + product);

        // collect to Collection
        Set<Integer> set = numbers.stream().collect(Collectors.toSet());
        Map<Integer, Integer> map = numbers.stream()
            .collect(Collectors.toMap(Function.identity(), n -> n * 2));
    }
}
```

### 示例5：Stream综合示例
```java
import java.util.*;
import java.util.stream.*;

class Person {
    private String name;
    private int age;
    private String city;

    public Person(String name, int age, String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCity() { return city; }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", city='" + city + "'}";
    }
}

public class StreamComplexDemo {
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("张三", 25, "北京"),
            new Person("李四", 30, "上海"),
            new Person("王五", 25, "北京"),
            new Person("赵六", 35, "上海"),
            new Person("钱七", 30, "北京")
        );

        // 1. 找出北京的人，按年龄降序，取前3个
        List<Person> beijing = people.stream()
            .filter(p -> "北京".equals(p.getCity()))
            .sorted(Comparator.comparingInt(Person::getAge).reversed())
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("北京人（年龄降序）：" + beijing);

        // 2. 按城市分组
        Map<String, List<Person>> byCity = people.stream()
            .collect(Collectors.groupingBy(Person::getCity));
        System.out.println("按城市分组：" + byCity);

        // 3. 各城市人数
        Map<String, Long> cityCount = people.stream()
            .collect(Collectors.groupingBy(Person::getCity, Collectors.counting()));
        System.out.println("各城市人数：" + cityCount);

        // 4. 各城市平均年龄
        Map<String, Double> cityAvgAge = people.stream()
            .collect(Collectors.groupingBy(
                Person::getCity,
                Collectors.averagingInt(Person::getAge)
            ));
        System.out.println("各城市平均年龄：" + cityAvgAge);

        // 5. 名字列表
        List<String> names = people.stream()
            .map(Person::getName)
            .collect(Collectors.toList());
        System.out.println("所有名字：" + names);
    }
}
```

### 示例6：接口默认方法
```java
interface A {
    default void hello() {
        System.out.println("A.hello()");
    }
}

interface B {
    default void hello() {
        System.out.println("B.hello()");
    }
}

// 类优先原则：如果接口有默认方法，类优先
class C implements A, B {
    @Override
    public void hello() {
        // 必须重写解决冲突
        System.out.println("C.hello()");
        // 可以调用父接口的方法
        A.super.hello();
        B.super.hello();
    }
}

public class DefaultMethodDemo {
    public static void main(String[] args) {
        new C().hello();
    }
}
```

### 示例7：Optional
```java
import java.util.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        // 创建Optional
        Optional<String> empty = Optional.empty();
        Optional<String> of = Optional.of("hello");
        Optional<String> nullable = Optional.ofNullable(null);

        // isPresent判断
        if (empty.isPresent()) {
            System.out.println(empty.get());
        }

        // orElse默认值
        System.out.println(empty.orElse("默认值"));  // 默认值

        // orElseGet延迟计算
        System.out.println(empty.orElseGet(() -> "计算值"));

        // ifPresent
        of.ifPresent(System.out::println);

        // map转换
        Optional<Integer> len = of.map(String::length);
        System.out.println(len.orElse(0));

        // flatMap
        Optional<String> result = of.flatMap(s -> Optional.of(s.toUpperCase()));
        System.out.println(result.orElse(""));

        // 链式操作
        String city = getUser()
            .flatMap(User::getAddress)
            .flatMap(Address::getCity)
            .orElse("未知");
        System.out.println("城市：" + city);
    }

    static Optional<User> getUser() {
        return Optional.of(new User(new Address(new City("北京"))));
    }
}

class User {
    private Address address;
    public User(Address address) { this.address = address; }
    public Optional<Address> getAddress() { return Optional.ofNullable(address); }
}

class Address {
    private City city;
    public Address(City city) { this.city = city; }
    public Optional<City> getCity() { return Optional.ofNullable(city); }
}

class City {
    private String name;
    public City(String name) { this.name = name; }
    public String getName() { return name; }
}
```

## 函数式接口

| 接口 | 方法 | 用途 |
|------|------|------|
| Function<T,R> | R apply(T t) | 转换 |
| Consumer<T> | void accept(T t) | 消费 |
| Supplier<T> | T get() | 生产 |
| Predicate<T> | boolean test(T t) | 判断 |

## 常见面试题

1. **Stream和Collection的区别？**
   - Collection是存储数据结构
   - Stream是计算/处理数据
   - Stream不修改源数据

2. **parallelStream()和Stream()的区别？**
   - parallelStream使用多线程
   - 适合大数据量处理
   - 小数据量可能更慢

3. **Lambda表达式和匿名内部类的区别？**
   - Lambda没有自己的类
   - Lambda this指向外层类
   - 匿名内部类创建新类

## 练习题

1. 使用Stream实现员工工资排名
2. 使用Optional处理空值
3. 实现方法引用各类用法

## 要点总结

- Lambda简化函数式接口实现
- 方法引用是Lambda的语法糖
- Stream是数据处理工具
- Optional解决NPE问题
- 接口默认方法兼容旧代码
