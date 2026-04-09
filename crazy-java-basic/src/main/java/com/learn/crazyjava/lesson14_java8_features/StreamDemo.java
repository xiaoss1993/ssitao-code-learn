package com.learn.crazyjava.lesson14_java8_features;

import java.util.*;
import java.util.stream.*;

/**
 * 第14课：Java 8新特性 - Stream API
 */
public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter - 过滤偶数
        List<Integer> evens = numbers.stream()
            .filter(n -> n % 2 == 0)
            .collect(Collectors.toList());
        System.out.println("偶数：" + evens);

        // map - 平方
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("平方：" + squares);

        // limit + skip
        List<Integer> middle = numbers.stream()
            .skip(2)
            .limit(5)
            .collect(Collectors.toList());
        System.out.println("中间5个：" + middle);

        // sorted
        List<Integer> sorted = numbers.stream()
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        System.out.println("降序：" + sorted);

        // aggregate
        int sum = numbers.stream().mapToInt(Integer::intValue).sum();
        OptionalDouble avg = numbers.stream().mapToInt(Integer::intValue).average();
        Optional<Integer> max = numbers.stream().max(Integer::compareTo);
        System.out.println("Sum: " + sum + ", Avg: " + avg.orElse(0) + ", Max: " + max.orElse(0));

        // groupingBy
        List<Person> people = Arrays.asList(
            new Person("张三", 25, "北京"),
            new Person("李四", 30, "上海"),
            new Person("王五", 25, "北京")
        );
        Map<String, List<Person>> byCity = people.stream()
            .collect(Collectors.groupingBy(Person::getCity));
        System.out.println("按城市分组：" + byCity);
    }
}

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
