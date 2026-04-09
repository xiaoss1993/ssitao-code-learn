package com.learn.crazyjava.lesson06_generics;

/**
 * 第6课：泛型 - 泛型方法
 */
public class GenericMethod {
    // 泛型方法
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.println(element);
        }
    }

    // 泛型方法：返回最大值
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    public static void main(String[] args) {
        Integer[] ints = {1, 2, 3};
        String[] strs = {"a", "b", "c"};

        printArray(ints);
        printArray(strs);

        System.out.println("最大值：" + max(10, 20));
        System.out.println("最大值：" + max("apple", "banana"));
    }
}
