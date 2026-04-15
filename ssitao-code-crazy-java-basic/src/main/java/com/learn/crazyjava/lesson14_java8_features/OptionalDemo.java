package com.learn.crazyjava.lesson14_java8_features;

import java.util.Optional;

/**
 * 第14课：Java 8新特性 - Optional
 */
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
        System.out.println("orElse: " + empty.orElse("默认值"));

        // orElseGet延迟计算
        System.out.println("orElseGet: " + empty.orElseGet(() -> "计算值"));

        // ifPresent
        of.ifPresent(System.out::println);

        // map转换
        Optional<Integer> len = of.map(String::length);
        System.out.println("长度：" + len.orElse(0));

        // flatMap
        Optional<String> upper = of.flatMap(s -> Optional.of(s.toUpperCase()));
        System.out.println("大写：" + upper.orElse(""));

        // 链式操作
        String result = getUser()
            .flatMap(OptionalDemo::getAddress)
            .flatMap(OptionalDemo::getCity)
            .orElse("未知城市");
        System.out.println("用户城市：" + result);
    }

    static Optional<User> getUser() {
        return Optional.of(new User("张三"));
    }

    static Optional<Address> getAddress(User user) {
        return Optional.of(new Address(user));
    }

    static Optional<String> getCity(Address addr) {
        return Optional.of(addr.city);
    }

    static class User {
        String name;
        User(String name) { this.name = name; }
    }

    static class Address {
        User user;
        String city = "北京";
        Address(User user) { this.user = user; }
    }
}
