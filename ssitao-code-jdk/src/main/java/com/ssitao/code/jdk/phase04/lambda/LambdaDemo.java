package com.ssitao.code.jdk.phase04.lambda;

import java.util.*;
import java.util.function.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Lambda表达式和函数式接口示例
 */
public class LambdaDemo {

    public static void main(String[] args) {
        System.out.println("=== Lambda Demo ===\n");

        // 1. Supplier - 生产者
        demonstrateSupplier();

        // 2. Consumer - 消费者
        demonstrateConsumer();

        // 3. Function - 转换
        demonstrateFunction();

        // 4. Predicate - 断言
        demonstratePredicate();

        // 5. 方法引用
        demonstrateMethodReference();

        // 6. 集合排序
        demonstrateSorting();

        // 7. 组合操作
        demonstrateComposition();
    }

    private static void demonstrateSupplier() {
        System.out.println("--- Supplier ---");

        // 无参Supplier
        Supplier<String> supplier1 = () -> "Hello from Supplier";
        System.out.println(supplier1.get());

        // 生成当前日期
        Supplier<LocalDate> dateSupplier = LocalDate::now;
        System.out.println("Today: " + dateSupplier.get());

        // 生成随机数
        Supplier<Random> randomSupplier = Random::new;
        System.out.println("Random: " + randomSupplier.get().nextInt(100));
    }

    private static void demonstrateConsumer() {
        System.out.println("\n--- Consumer ---");

        // 基本Consumer
        Consumer<String> consumer1 = s -> System.out.println("Consumer1: " + s);
        consumer1.accept("test");

        // 链式Consumer
        Consumer<String> consumer2 = s -> System.out.print("Upper: ");
        Consumer<String> consumer3 = s -> System.out.println(s.toUpperCase());
        Consumer<String> combined = consumer2.andThen(consumer3);
        combined.accept("hello");

        // forEach中使用Consumer
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        System.out.print("Names: ");
        names.forEach(name -> System.out.print(name + " "));
        System.out.println();

        // BiConsumer
        BiConsumer<String, Integer> agePrinter = (name, age) ->
            System.out.println(name + " is " + age + " years old");
        agePrinter.accept("Alice", 25);
    }

    private static void demonstrateFunction() {
        System.out.println("\n--- Function ---");

        // 基本Function
        Function<String, Integer> strToInt = Integer::parseInt;
        System.out.println("'123' -> " + strToInt.apply("123"));

        // 链式Function
        Function<String, String> trim = String::trim;
        Function<String, String> upper = String::toUpperCase;
        Function<String, String> pipeline = trim.andThen(upper);
        System.out.println("'  hello  ' -> '" + pipeline.apply("  hello  ") + "'");

        // compose: 先执行参数function，再执行调用者
        Function<String, String> composed = trim.compose(upper);
        System.out.println("'  HELLO  ' -> '" + composed.apply("  HELLO  ") + "'");

        // identity
        Function<String, String> identity = Function.identity();
        System.out.println("identity('test') = " + identity.apply("test"));

        // BiFunction
        BiFunction<String, String, String> concat = (s1, s2) -> s1 + s2;
        System.out.println("concat('Hello', ' World') = " + concat.apply("Hello", " World"));
    }

    private static void demonstratePredicate() {
        System.out.println("\n--- Predicate ---");

        // 基本Predicate
        Predicate<String> isEmpty = String::isEmpty;
        System.out.println("isEmpty('') = " + isEmpty.test(""));
        System.out.println("isEmpty('hello') = " + isEmpty.test("hello"));

        // 组合Predicate
        Predicate<String> isNotEmpty = isEmpty.negate();
        Predicate<String> hasLength = s -> s.length() > 3;
        Predicate<String> isValid = isNotEmpty.and(hasLength);

        System.out.println("isValid('') = " + isValid.test(""));
        System.out.println("isValid('ab') = " + isValid.test("ab"));
        System.out.println("isValid('abcd') = " + isValid.test("abcd"));

        // or组合
        Predicate<String> startsWithA = s -> s.startsWith("A");
        Predicate<String> isLong = s -> s.length() > 10;
        Predicate<String> combined = startsWithA.or(isLong);

        System.out.println("combined('Apple') = " + combined.test("Apple"));
        System.out.println("combined('VeryLongString') = " + combined.test("VeryLongString"));
        System.out.println("combined('Normal') = " + combined.test("Normal"));

        // isEqual
        Predicate<String> isHello = Predicate.isEqual("hello");
        System.out.println("isHello('hello') = " + isHello.test("hello"));
        System.out.println("isHello('world') = " + isHello.test("world"));
    }

    private static void demonstrateMethodReference() {
        System.out.println("\n--- Method Reference ---");

        List<String> words = Arrays.asList("hello", "world", "java");

        // 静态方法引用
        Function<String, Integer> parser = Integer::parseInt;
        System.out.println("parseInt('123') = " + parser.apply("123"));

        // 实例方法引用-任意对象
        List<String> upper = words.stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("Upper: " + upper);

        // 构造方法引用
        Supplier<ArrayList<String>> listSupplier = ArrayList::new;
        ArrayList<String> newList = listSupplier.get();
        System.out.println("New list created: " + newList);

        // Function构造
        Function<Integer, ArrayList<String>> sizedListFactory = ArrayList::new;
        ArrayList<String> sizedList = sizedListFactory.apply(10);
        System.out.println("Sized list capacity: " + sizedList.size());
    }

    private static void demonstrateSorting() {
        System.out.println("\n--- Sorting with Lambda ---");

        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

        // 自然排序
        List<String> sorted = names.stream()
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Sorted: " + sorted);

        // 降序
        List<String> desc = names.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        System.out.println("Desc: " + desc);

        // 按长度排序
        List<String> byLength = names.stream()
            .sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.toList());
        System.out.println("By length: " + byLength);

        // 按长度降序
        List<String> byLengthDesc = names.stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .collect(Collectors.toList());
        System.out.println("By length desc: " + byLengthDesc);

        // 对象排序
        List<Person> people = Arrays.asList(
            new Person("Bob", 25),
            new Person("Alice", 30),
            new Person("Charlie", 20)
        );

        System.out.println("\nPeople sorting:");
        System.out.println("By name: " + people.stream()
            .sorted(Comparator.comparing(Person::getName))
            .map(Person::getName)
            .collect(Collectors.toList()));

        System.out.println("By age: " + people.stream()
            .sorted(Comparator.comparingInt(Person::getAge))
            .map(p -> p.getName() + "(" + p.getAge() + ")")
            .collect(Collectors.toList()));

        // 多条件排序
        List<Person> multiSort = Arrays.asList(
            new Person("Bob", 25),
            new Person("Bob", 30),
            new Person("Alice", 25)
        );
        System.out.println("Multi-sort: " + multiSort.stream()
            .sorted(Comparator.comparing(Person::getName)
                             .thenComparingInt(Person::getAge))
            .map(p -> p.getName() + "(" + p.getAge() + ")")
            .collect(Collectors.toList()));
    }

    private static void demonstrateComposition() {
        System.out.println("\n--- Composition Demo ---");

        // 组合多个Function
        Function<String, String> step1 = s -> {
            System.out.println("Step1: trim");
            return s.trim();
        };
        Function<String, String> step2 = s -> {
            System.out.println("Step2: upper");
            return s.toUpperCase();
        };
        Function<String, String> step3 = s -> {
            System.out.println("Step3: reverse");
            return new StringBuilder(s).reverse().toString();
        };

        Function<String, String> pipeline = step1.andThen(step2).andThen(step3);
        String result = pipeline.apply("  hello  ");
        System.out.println("Pipeline result: " + result);

        // 组合多个Predicate
        Predicate<String> p1 = s -> s.length() > 0;
        Predicate<String> p2 = s -> s.length() < 10;
        Predicate<String> p3 = s -> !s.equals("test");

        Predicate<String> combined = p1.and(p2).and(p3);
        System.out.println("\nCombined Predicate:");
        System.out.println("'hello': " + combined.test("hello"));
        System.out.println("'test': " + combined.test("test"));
        System.out.println("'verylongstring': " + combined.test("verylongstring"));
    }

    // 测试用类
    static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
    }
}
