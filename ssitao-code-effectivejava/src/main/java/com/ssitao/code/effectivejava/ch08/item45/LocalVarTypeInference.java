package com.ssitao.code.effectivejava.ch08.item45;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * 条目45：最小化局部变量的作用域
 *
 * 原则：
 * 1. 变量声明靠近使用处
 * 2. 优先使用for-each循环
 */
public class LocalVarTypeInference {
    public static void main(String[] args) {
        System.out.println("=== 最小化局部变量作用域 ===\n");

        // 错误：声明过早
        // String name = "";  // 作用域太宽
        // if (condition) {
        //     use(name);
        // }

        // 正确：在使用处声明
        if (condition()) {
            String name = getName();
            use(name);
        }

        System.out.println("\n=== 优先使用for-each而非传统for ===\n");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

        // 传统for（冗长，容易出错）
        System.out.println("传统for:");
        for (int i = 0; i < names.size(); i++) {
            System.out.println(names.get(i));
        }

        // For-each（更简洁，无索引变量）
        System.out.println("\nFor-each:");
        for (String name : names) {
            System.out.println(name);
        }

        System.out.println("\n=== 何时需要传统for ===\n");

        // 需要索引或修改循环变量
        // 1. 倒序遍历
        for (int i = names.size() - 1; i >= 0; i--) {
            System.out.println("倒序: " + names.get(i));
        }

        // 2. 循环体中需要索引
        for (int i = 0; i < names.size(); i++) {
            System.out.println(i + ": " + names.get(i));
        }

        // 3. 遍历时转换
        List<String> upper = new ArrayList<>();
        for (String name : names) {
            upper.add(name.toUpperCase());
        }
        System.out.println("转换后: " + upper);

        System.out.println("\n=== 何时不用for-each ===\n");

        // 1. 过滤（需要iterator.remove()）
        // 2. 转换（需要修改正在遍历的对象）
        // 3. 并行迭代（多个循环）
    }

    static boolean condition() { return true; }
    static String getName() { return "Test"; }
    static void use(String s) { System.out.println("Used: " + s); }
}
