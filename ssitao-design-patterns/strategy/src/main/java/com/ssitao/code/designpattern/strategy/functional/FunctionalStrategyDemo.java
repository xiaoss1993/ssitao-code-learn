package com.ssitao.code.designpattern.strategy.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 函数式策略示例（Lambda/函数式接口）
 *
 * Java8+特性：
 * 1. Lambda表达式简化策略实现
 * 2. 函数式接口作为策略接口
 * 3. 方法引用作为策略
 */
public class FunctionalStrategyDemo {

    public static void main(String[] args) {
        System.out.println("=== 函数式策略示例 ===\n");

        // 1. 使用Lambda作为排序策略
        System.out.println("1. 排序策略");
        sortStrategyDemo();

        // 2. 使用Predicate作为过滤策略
        System.out.println("\n2. 过滤策略");
        filterStrategyDemo();

        // 3. 使用Function作为转换策略
        System.out.println("\n3. 转换策略");
        transformStrategyDemo();

        // 4. 自定义函数式接口策略
        System.out.println("\n4. 自定义策略");
        customStrategyDemo();
    }

    /**
     * 排序策略示例
     */
    private static void sortStrategyDemo() {
        List<String> names = Arrays.asList("Tom", "Jerry", "Alice", "Bob", "Charlie");

        // 策略1: 按字母顺序
        System.out.println("按字母顺序:");
        names.sort(Comparator.naturalOrder());
        System.out.println(names);

        // 策略2: 按字母逆序
        System.out.println("\n按字母逆序:");
        names.sort(Comparator.reverseOrder());
        System.out.println(names);

        // 策略3: 按长度
        System.out.println("\n按长度排序:");
        names.sort(Comparator.comparingInt(String::length));
        System.out.println(names);

        // 策略4: Lambda自定义
        System.out.println("\n按长度降序:");
        names.sort((a, b) -> b.length() - a.length());
        System.out.println(names);
    }

    /**
     * 过滤策略示例
     */
    private static void filterStrategyDemo() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 策略1: 过滤偶数
        System.out.println("过滤偶数:");
        List<Integer> evens = filter(numbers, n -> n % 2 == 0);
        System.out.println(evens);

        // 策略2: 过滤大于5的数
        System.out.println("\n过滤大于5的数:");
        List<Integer> greaterThan5 = filter(numbers, n -> n > 5);
        System.out.println(greaterThan5);

        // 策略3: 过滤奇数
        System.out.println("\n过滤奇数:");
        List<Integer> odds = filter(numbers, n -> n % 2 != 0);
        System.out.println(odds);

        // 策略4: 过滤包含特定条件的数
        System.out.println("\n过滤能被3整除的数:");
        List<Integer> divisibleBy3 = filter(numbers, n -> n % 3 == 0);
        System.out.println(divisibleBy3);
    }

    /**
     * 转换策略示例
     */
    private static void transformStrategyDemo() {
        List<String> names = Arrays.asList("alice", "bob", "charlie");

        // 策略1: 转大写
        System.out.println("转大写:");
        List<String> upper = transform(names, String::toUpperCase);
        System.out.println(upper);

        // 策略2: 转小写
        System.out.println("\n转小写:");
        List<String> lower = transform(names, String::toLowerCase);
        System.out.println(lower);

        // 策略3: 添加前缀
        System.out.println("\n添加前缀:");
        List<String> prefix = transform(names, s -> "User-" + s);
        System.out.println(prefix);

        // 策略4: 计算长度
        System.out.println("\n计算长度:");
        List<Integer> lengths = transform(names, String::length);
        System.out.println(lengths);
    }

    /**
     * 自定义策略接口示例
     */
    private static void customStrategyDemo() {
        // 使用策略接口
        DiscountCalculator calculator = new DiscountCalculator();

        // 无折扣
        System.out.println("无折扣:");
        double price1 = calculator.calculate(100, amount -> amount);
        System.out.println("原价: 100, 折后价: " + price1);

        // 9折
        System.out.println("\n9折:");
        double price2 = calculator.calculate(100, amount -> amount * 0.9);
        System.out.println("原价: 100, 折后价: " + price2);

        // 满100减20
        System.out.println("\n满100减20:");
        double price3 = calculator.calculate(100, amount -> amount >= 100 ? amount - 20 : amount);
        System.out.println("原价: 100, 折后价: " + price3);

        // 新用户额外减10
        System.out.println("\n新用户额外减10:");
        double price4 = calculator.calculate(100, amount -> amount - 10);
        System.out.println("原价: 100, 折后价: " + price4);
    }

    // 通用过滤方法
    private static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // 通用转换方法
    private static <T, R> List<R> transform(List<T> list, Function<T, R> function) {
        List<R> result = new ArrayList<>();
        for (T item : list) {
            result.add(function.apply(item));
        }
        return result;
    }
}

/**
 * 折扣计算器 - 使用策略模式
 */
class DiscountCalculator {
    public double calculate(double originalPrice, DiscountStrategy strategy) {
        return strategy.applyDiscount(originalPrice);
    }
}

/**
 * 折扣策略接口（函数式接口）
 */
@FunctionalInterface
interface DiscountStrategy {
    double applyDiscount(double amount);
}
