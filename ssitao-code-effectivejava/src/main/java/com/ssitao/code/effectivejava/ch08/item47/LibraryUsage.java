package com.ssitao.code.effectivejava.ch08.item47;

import java.util.*;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 条目47：了解并使用库
 *
 * 使用标准库的好处：
 * 1. 专家知识：库经过大量测试和使用
 * 2. 无维护成本：不需自己维护
 * 3. 互操作性：与其他代码兼容
 * 4. 性能提升：持续优化
 */
public class LibraryUsage {
    public static void main(String[] args) {
        System.out.println("=== 使用标准库 ===\n");

        // 错误：自己实现常见操作
        // randomNumber = (int)(Math.random() * N);
        // while (true) { ... }  // 重复造轮子

        // 正确：使用库方法

        // 指定范围内的随机数
        Random rnd = new Random();
        int randomNumber = rnd.nextInt(100);  // 0-99
        System.out.println("随机数0-99: " + randomNumber);

        // List操作
        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        Collections.shuffle(list);
        System.out.println("打乱后: " + list);

        // 二分搜索
        Collections.sort(list);
        int index = Collections.binarySearch(list, "b");
        System.out.println("二分搜索'b'位置: " + index);

        System.out.println("\n=== 常被误用的库 ===\n");

        // Math.abs() 可能溢出！
        System.out.println("Math.abs(Integer.MIN_VALUE) = " + Math.abs(Integer.MIN_VALUE));
        // 应该使用BigInteger或避免这种情况

        // Date/Calendar已过时 - 使用java.time
        System.out.println("\n使用java.time而非java.util.Date");
        System.out.println("LocalDate.now() = " + java.time.LocalDate.now());
        System.out.println("LocalDateTime.now() = " + java.time.LocalDateTime.now());

        System.out.println("\n=== 安全最佳实践 ===\n");

        // 不要自己写加密
        System.out.println("使用MessageDigest进行哈希");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest("password".getBytes());
            System.out.println("SHA-256哈希长度: " + hash.length);
        } catch (Exception e) {
            System.out.println("错误: " + e);
        }

        System.out.println("\n=== 需要了解的关键库 ===\n");
        System.out.println("- java.util: 集合、 streams、并发");
        System.out.println("- java.math: BigInteger、BigDecimal");
        System.out.println("- java.time: LocalDate、LocalDateTime、Duration");
        System.out.println("- java.nio.file: Path、Files");
        System.out.println("- java.util.concurrent: ExecutorService、Future");
    }
}
