package com.ssitao.code.effectivejava.ch04.item28;

/**
 * 条目28：列表优于数组
 *
 * 数组的问题：
 * 1. 数组是协变的（covariant）：String[] 是 Object[] 的子类型
 *    - 这导致可以在编译时通过，但运行时抛出 ArrayStoreException
 * 2. 泛型是不变的（invariant）：List<String> 不是 List<Object> 的子类型
 *    - 这在编译时就能捕获错误，更安全
 * 3. 数组不支持泛型：不能创建 new List<E>() 或 new E[]
 *
 * 结论：优先使用泛型集合（如List<E>），而不是数组
 */
import java.util.ArrayList;
import java.util.List;

public class ArrayVsList {
    public static void main(String[] args) {
        System.out.println("=== 数组 vs 列表 ===\n");

        // 问题1：数组是协变的（不安全）
        System.out.println("--- 问题1：协变性 ---");

        // String[] 是 Object[] 的子类型
        Object[] objArr = new String[3];
        System.out.println("String[] 是 Object[] 的子类型：编译时正确");

        try {
            objArr[0] = 123;  // 但运行时会抛出 ArrayStoreException！
            System.out.println("在String[]中存储Integer - 不应该到达这里");
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException: " + e.getMessage());
        }

        // 问题2：泛型是不变的（安全）
        System.out.println("\n--- 问题2：不变性 ---");
        // List<String> 不是 List<Object> 的子类型
        // List<String> stringList = new ArrayList<Object>();  // 编译错误！
        System.out.println("List<String> 不是 List<Object> 的子类型：编译错误");

        // 泛型列表在编译时捕获类型错误
        List<String> stringList = new ArrayList<>();
        // stringList.add(123);  // 编译错误！
        System.out.println("向 List<String> 添加 Integer：编译错误（在编译时捕获）");

        // 问题3：数组不支持泛型
        System.out.println("\n--- 问题3：数组不支持泛型 ---");
        // new List<E>() - 编译错误，不能创建泛型数组
        // new E[] - 编译错误，不能创建泛型数组

        List<String>[] stringLists = (List<String>[]) new ArrayList<?>[3];  // 需要unchecked转换
        System.out.println("创建泛型数组必须使用unchecked转换: (List<String>[]) new ArrayList<?>[3]");

        System.out.println("\n--- 结论 ---");
        System.out.println("列表比数组更安全：");
        System.out.println("1. 类型错误在编译时捕获，而非运行时");
        System.out.println("2. 没有 ClassCastException 或 ArrayStoreException");
        System.out.println("3. 完全支持泛型");
    }
}
