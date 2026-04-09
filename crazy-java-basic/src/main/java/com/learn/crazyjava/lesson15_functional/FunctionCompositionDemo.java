package com.learn.crazyjava.lesson15_functional;

import java.util.function.*;

/**
 * 第15课：函数式编程 - 函数组合
 */
public class FunctionCompositionDemo {
    public static void main(String[] args) {
        Function<Integer, Integer> add1 = x -> x + 1;
        Function<Integer, Integer> multiply2 = x -> x * 2;

        // andThen: 先执行调用者，再执行参数
        Function<Integer, Integer> addThenMultiply = add1.andThen(multiply2);
        // compose: 先执行参数，再执行调用者
        Function<Integer, Integer> multiplyThenAdd = add1.compose(multiply2);

        System.out.println("(5+1)*2 = " + addThenMultiply.apply(5));   // 12
        System.out.println("5*2+1 = " + multiplyThenAdd.apply(5));     // 11

        // Predicate组合
        Predicate<String> startsWithA = s -> s.startsWith("A");
        Predicate<String> endsWithZ = s -> s.endsWith("Z");

        Predicate<String> AZ = startsWithA.and(endsWithZ);
        Predicate<String> AorZ = startsWithA.or(endsWithZ);
        Predicate<String> notA = startsWithA.negate();

        System.out.println("AZ: " + AZ.test("ABC"));       // true
        System.out.println("A or Z: " + AorZ.test("Hello")); // false
        System.out.println("not A: " + notA.test("Apple")); // false

        // 高阶函数：返回函数的函数
        Function<Integer, Function<Integer, Integer>> makeAdder = base -> x -> base + x;
        Function<Integer, Integer> add10 = makeAdder.apply(10);
        System.out.println("10+5 = " + add10.apply(5));
        System.out.println("10+20 = " + add10.apply(20));
    }
}
