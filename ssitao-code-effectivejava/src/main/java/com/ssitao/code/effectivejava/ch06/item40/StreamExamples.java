package com.ssitao.code.effectivejava.ch06.item40;

import java.util.*;
import java.util.stream.*;

/**
 * 条目40：谨慎使用流
 *
 * 流的组成：
 * - source: 数据源（如 List.stream()）
 * - intermediate ops: 中间操作（如 filter, map, sorted）
 * - terminal op: 终端操作（如 collect, forEach, reduce）
 *
 * 流适合的场景：
 * - 转换序列
 * - 过滤收集
 * - 分组聚合
 *
 * 流不适合的场景：
 * - 需要访问循环状态（计数器、标志）
 * - 多次遍历且条件依赖数据
 * - 修改共享状态
 */
public class StreamExamples {
    public static void main(String[] args) {
        System.out.println("=== 流管道 ===\n");

        // 流管道的组成：
        // source.stream() -> intermediate ops -> terminal op

        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");

        // 基本的流管道
        List<String> longWords = words.stream()
            .filter(w -> w.length() > 5)
            .map(String::toUpperCase)
            .collect(Collectors.toList());
        System.out.println("长度>5的单词，大写: " + longWords);

        System.out.println("\n=== 流的适合场景 ===\n");

        // 1. 转换序列
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("求和: " + sum);

        // 2. 过滤收集
        Set<String> uniqueFirstLetters = words.stream()
            .map(w -> w.substring(0, 1))
            .collect(Collectors.toSet());
        System.out.println("唯一首字母: " + uniqueFirstLetters);

        // 3. 分组
        Map<Integer, List<String>> byLength = words.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("按长度分组: " + byLength);

        System.out.println("\n=== 流不适合的场景 ===\n");

        // 流不合适的情况：
        // 1. 需要访问循环状态（计数器、标志）
        // 2. 多次遍历且条件依赖数据
        // 3. 修改共享状态

        // 示例：找第一个匹配条件的数并更新计数器
        int[] counter = {0};  // 外部可变状态 - 不适合流
        int result = numbers.stream()
            .filter(n -> {
                counter[0]++;
                return n > 3;
            })
            .findFirst()
            .orElse(-1);
        System.out.println("找到 " + result + "，检查了 " + counter[0] + " 个元素");

        System.out.println("\n=== 避免滥用流 ===\n");

        // 不要：强制所有操作都用流
        // 最糟糕：words.stream().forEach(System.out::println);  // 直接用forEach就好！

        // 不要：链太长 - 提取为方法
        // 正确：尽可能使用方法引用

        words.stream()
            .filter(w -> w.length() > 4)
            .map(String::toUpperCase)
            .sorted()
            .forEach(System.out::println);

        System.out.println("\n=== 惰性求值 ===\n");

        // 流是惰性的 - 中间操作不会执行，直到终端操作
        Stream<String> stream = words.stream()
            .filter(w -> {
                System.out.println("过滤: " + w);
                return w.length() > 3;
            });

        System.out.println("流已创建，还没打印任何内容...");
        System.out.println("只取第一个: " + stream.findFirst().orElse(""));

        System.out.println("\n=== 并行流 ===\n");

        // 只在以下情况使用并行流：
        // - 大数据集
        // - 无状态、纯函数操作
        // - 无顺序要求
        long parallelSum = numbers.parallelStream()
            .mapToLong(Integer::longValue)
            .sum();
        System.out.println("并行求和: " + parallelSum);
    }
}
