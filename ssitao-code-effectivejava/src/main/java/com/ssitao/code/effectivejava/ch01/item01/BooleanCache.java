package com.ssitao.code.effectivejava.ch01.item01;

/**
 * 条目1：考虑使用静态工厂方法代替构造器
 *
 * 演示 Boolean.valueOf() 的缓存机制
 *
 * 静态工厂方法的优点：
 * 1. 有名称，更易读
 * 2. 不需要每次都创建新对象
 * 3. 可以返回原返回类型的任意子类型
 * 4. 创建参数化类型实例时，代码更简洁
 */
public class BooleanCache {
    public static void main(String[] args) {
        System.out.println("=== 静态工厂方法示例 ===\n");

        // Boolean.valueOf() 会复用缓存的对象
        // true 返回 Boolean.TRUE，false 返回 Boolean.FALSE
        Boolean b1 = Boolean.valueOf(true);
        Boolean b2 = Boolean.valueOf(true);
        Boolean b3 = Boolean.valueOf(false);
        Boolean b4 = Boolean.valueOf(false);

        System.out.println("Boolean.valueOf(true) == Boolean.valueOf(true): " + (b1 == b2));
        // 结果为 true，因为 b1 和 b2 引用的是同一个缓存对象

        System.out.println("Boolean.valueOf(false) == Boolean.valueOf(false): " + (b3 == b4));
        // 结果为 true，因为 b3 和 b4 引用的是同一个缓存对象

        System.out.println("\n结论：静态工厂方法可以复用对象，减少内存开销");
    }
}
