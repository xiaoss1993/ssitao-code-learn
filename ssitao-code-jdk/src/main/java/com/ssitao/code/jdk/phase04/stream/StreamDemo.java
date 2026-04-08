package com.ssitao.code.jdk.phase04.stream;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.time.LocalDate;

/**
 * Stream API 示例
 */
public class StreamDemo {

    public static void main(String[] args) {
        System.out.println("=== Stream API Demo ===\n");

        // 1. 创建Stream
        demonstrateCreating();

        // 2. 中间操作 - filter, map, flatMap
        demonstrateTransform();

        // 3. 中间操作 - distinct, sorted, limit, skip
        demonstrateProcessing();

        // 4. 终止操作 - collect
        demonstrateCollect();

        // 5. 终止操作 - reduce
        demonstrateReduce();

        // 6. 终止操作 - match和find
        demonstrateMatchFind();

        // 7. 并行流
        demonstrateParallel();

        // 8. 实战示例
        demonstrateRealWorld();
    }

    private static void demonstrateCreating() {
        System.out.println("--- Creating Stream ---");

        // 从集合创建
        List<String> list = Arrays.asList("a", "b", "c");
        Stream<String> stream1 = list.stream();

        // 从数组创建
        String[] array = {"x", "y", "z"};
        Stream<String> stream2 = Arrays.stream(array);

        // Stream.of
        Stream<String> stream3 = Stream.of("1", "2", "3");

        // 无限流 - iterate
        System.out.print("Iterate (1-5): ");
        Stream.iterate(1, n -> n + 1)
            .limit(5)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // 无限流 - generate
        System.out.print("Generate (5 randoms): ");
        Stream.generate(Math::random)
            .limit(5)
            .forEach(n -> System.out.printf("%.4f ", n));
        System.out.println();

        // 基本类型流
        System.out.print("IntStream range(1,6): ");
        IntStream.range(1, 6).forEach(n -> System.out.print(n + " "));
        System.out.println();

        System.out.print("IntStream rangeClosed(1,5): ");
        IntStream.rangeClosed(1, 5).forEach(n -> System.out.print(n + " "));
        System.out.println();

        // 空Stream
        Stream<String> empty = Stream.empty();
        System.out.println("Empty stream count: " + empty.count());
    }

    private static void demonstrateTransform() {
        System.out.println("\n--- Transform Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // filter - 过滤
        System.out.print("Filter evens: ");
        numbers.stream()
            .filter(n -> n % 2 == 0)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // map - 转换
        System.out.print("Map to squares: ");
        numbers.stream()
            .map(n -> n * n)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // mapToInt/mapToLong/mapToDouble
        int sum = numbers.stream()
            .mapToInt(Integer::intValue)
            .sum();
        System.out.println("Sum: " + sum);

        // flatMap - 扁平化
        System.out.print("FlatMap words: ");
        String[] sentences = {"hello world", "java stream"};
        Arrays.stream(sentences)
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .forEach(w -> System.out.print(w + " "));
        System.out.println();

        // 链式操作
        System.out.print("Chain filter->map: ");
        numbers.stream()
            .filter(n -> n > 2)
            .map(n -> n * 10)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    private static void demonstrateProcessing() {
        System.out.println("\n--- Processing Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5);

        // distinct - 去重
        System.out.print("Distinct: ");
        numbers.stream()
            .distinct()
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // sorted - 排序
        System.out.print("Sorted: ");
        numbers.stream()
            .distinct()
            .sorted()
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // sorted with comparator
        System.out.print("Sorted desc: ");
        numbers.stream()
            .distinct()
            .sorted(Comparator.reverseOrder())
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // limit - 截取
        System.out.print("Limit 3: ");
        numbers.stream()
            .distinct()
            .sorted()
            .limit(3)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // skip - 跳过
        System.out.print("Skip first 3: ");
        numbers.stream()
            .distinct()
            .sorted()
            .skip(3)
            .forEach(n -> System.out.print(n + " "));
        System.out.println();

        // peek - 调试
        System.out.print("Peek (trace): ");
        List<Integer> result = numbers.stream()
            .filter(n -> n > 2)
            .peek(n -> System.out.print("[" + n + "]"))
            .map(n -> n * 2)
            .peek(n -> System.out.print("(" + n + ")"))
            .limit(3)
            .collect(Collectors.toList());
        System.out.println("\nResult: " + result);
    }

    private static void demonstrateCollect() {
        System.out.println("\n--- Collect Operations ---");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

        // toList / toSet
        List<String> list = names.stream().collect(Collectors.toList());
        Set<String> set = names.stream().collect(Collectors.toSet());

        // joining
        String joined = names.stream().collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);

        // counting
        long count = names.stream().collect(Collectors.counting());
        System.out.println("Count: " + count);

        // summingInt/double/long
        List<Integer> ages = Arrays.asList(25, 30, 35, 40);
        int sum = ages.stream().collect(Collectors.summingInt(Integer::intValue));
        System.out.println("Sum of ages: " + sum);

        double avg = ages.stream().collect(Collectors.averagingInt(Integer::intValue));
        System.out.println("Avg of ages: " + avg);

        // summarizingInt
        IntSummaryStatistics stats = ages.stream()
            .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println("Stats: " + stats);

        // toMap
        Map<String, Integer> nameToAge = names.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                String::length
            ));
        System.out.println("Name to length: " + nameToAge);

        // groupingBy
        List<String> words = Arrays.asList("hello", "hi", "world", "java", "join");
        Map<Integer, List<String>> byLength = words.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("Group by length: " + byLength);

        // partitioningBy
        Map<Boolean, List<String>> byLong = words.stream()
            .collect(Collectors.partitioningBy(w -> w.length() > 4));
        System.out.println("Partition by length>4: " + byLong);

        // mapping
        Map<Integer, Long> lengthCount = words.stream()
            .collect(Collectors.groupingBy(
                String::length,
                Collectors.counting()
            ));
        System.out.println("Length counts: " + lengthCount);
    }

    private static void demonstrateReduce() {
        System.out.println("\n--- Reduce Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        // reduce with identity
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // reduce without identity
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        System.out.println("Max: " + max.orElse(0));

        // reduce with complex operation
        String concatenated = names().stream()
            .reduce("", (a, b) -> a + b + ", ");
        System.out.println("Concatenated: " + concatenated);

        // map + reduce
        int sumOfLengths = names().stream()
            .map(String::length)
            .reduce(0, Integer::sum);
        System.out.println("Sum of name lengths: " + sumOfLengths);

        // count via reduce
        long count = names().stream()
            .map(s -> 1L)
            .reduce(0L, Long::sum);
        System.out.println("Count via reduce: " + count);
    }

    private static void demonstrateMatchFind() {
        System.out.println("\n--- Match and Find Operations ---");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // anyMatch
        boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0);
        System.out.println("Has even: " + hasEven);

        // allMatch
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        System.out.println("All positive: " + allPositive);

        // noneMatch
        boolean noneNegative = numbers.stream().noneMatch(n -> n < 0);
        System.out.println("None negative: " + noneNegative);

        // findFirst
        Optional<Integer> first = numbers.stream()
            .filter(n -> n > 3)
            .findFirst();
        System.out.println("First > 3: " + first.orElse(0));

        // findAny (useful in parallel)
        Optional<Integer> any = numbers.stream()
            .filter(n -> n > 3)
            .findAny();
        System.out.println("Any > 3: " + any.orElse(0));

        // Optional handling
        List<String> empty = Collections.emptyList();
        Optional<String> firstEmpty = empty.stream().findFirst();
        System.out.println("First from empty: " + firstEmpty.orElse("none"));
    }

    private static void demonstrateParallel() {
        System.out.println("\n--- Parallel Stream ---");

        List<Integer> numbers = IntStream.range(1, 1001).boxed().collect(Collectors.toList());

        // Sequential
        long start = System.nanoTime();
        long seqSum = numbers.stream()
            .filter(n -> n % 2 == 0)
            .mapToLong(Integer::intValue)
            .sum();
        long seqTime = System.nanoTime() - start;

        // Parallel
        start = System.nanoTime();
        long parSum = numbers.parallelStream()
            .filter(n -> n % 2 == 0)
            .mapToLong(Integer::intValue)
            .sum();
        long parTime = System.nanoTime() - start;

        System.out.println("Sequential sum: " + seqSum + " (time: " + seqTime / 1000 + "us)");
        System.out.println("Parallel sum: " + parSum + " (time: " + parTime / 1000 + "us)");

        // 注意：并行流结果应该一致
        System.out.println("Results match: " + (seqSum == parSum));
    }

    private static void demonstrateRealWorld() {
        System.out.println("\n--- Real World Examples ---");

        // 模拟数据
        List<Product> products = Arrays.asList(
            new Product("Laptop", "Electronics", 999.99, true),
            new Product("Phone", "Electronics", 599.99, true),
            new Product("Desk", "Furniture", 299.99, false),
            new Product("Chair", "Furniture", 149.99, true),
            new Product("Headphones", "Electronics", 149.99, true),
            new Product("Watch", "Electronics", 299.99, false)
        );

        // 1. 按分类分组
        System.out.println("Products by category:");
        products.stream()
            .collect(Collectors.groupingBy(Product::getCategory))
            .forEach((cat, prods) -> {
                System.out.println("  " + cat + ": " + prods.stream()
                    .map(Product::getName)
                    .collect(Collectors.joining(", ")));
            });

        // 2. 计算每个分类的总价
        System.out.println("\nTotal price by category:");
        products.stream()
            .collect(Collectors.groupingBy(
                Product::getCategory,
                Collectors.summingDouble(Product::getPrice)
            ))
            .forEach((cat, total) ->
                System.out.println("  " + cat + ": $" + total));

        // 3. 找出最便宜的产品
        Optional<Product> cheapest = products.stream()
            .min(Comparator.comparingDouble(Product::getPrice));
        cheapest.ifPresent(p ->
            System.out.println("\nCheapest: " + p.getName() + " ($" + p.getPrice() + ")"));

        // 4. 在库产品按价格排序
        System.out.println("\nIn-stock products sorted by price:");
        products.stream()
            .filter(Product::isInStock)
            .sorted(Comparator.comparingDouble(Product::getPrice))
            .forEach(p -> System.out.println("  " + p.getName() + ": $" + p.getPrice()));

        // 5. 检查是否有产品超过500
        boolean hasExpensive = products.stream()
            .anyMatch(p -> p.getPrice() > 500);
        System.out.println("\nHas product > $500: " + hasExpensive);

        // 6. 统计信息
        DoubleSummaryStatistics stats = products.stream()
            .collect(Collectors.summarizingDouble(Product::getPrice));
        System.out.println("\nPrice stats:");
        System.out.println("  Count: " + stats.getCount());
        System.out.println("  Sum: $" + stats.getSum());
        System.out.println("  Avg: $" + stats.getAverage());
        System.out.println("  Min: $" + stats.getMin());
        System.out.println("  Max: $" + stats.getMax());
    }

    private static List<String> names() {
        return Arrays.asList("Alice", "Bob", "Charlie", "David");
    }

    // 测试用类
    static class Product {
        private String name;
        private String category;
        private double price;
        private boolean inStock;

        public Product(String name, String category, double price, boolean inStock) {
            this.name = name;
            this.category = category;
            this.price = price;
            this.inStock = inStock;
        }

        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public boolean isInStock() { return inStock; }
    }
}
