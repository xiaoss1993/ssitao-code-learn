package com.ssitao.code.jdk.phase01.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 第一阶段步骤4: 常用工具类 - Arrays工具类演示
 */
public class ArraysDemo {

    public static void main(String[] args) {
        System.out.println("=== Arrays工具类 ===\n");

        // 1. 基本操作
        System.out.println("--- 1. 基本操作 ---");
        int[] arr = {5, 2, 8, 1, 9};
        System.out.println("原数组: " + Arrays.toString(arr));

        // 数组复制
        int[] copy = Arrays.copyOf(arr, arr.length);
        System.out.println("复制: " + Arrays.toString(copy));

        // 数组填充
        int[] filled = new int[5];
        Arrays.fill(filled, 10);
        System.out.println("填充10: " + Arrays.toString(filled));

        // 数组比较
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        System.out.println("Arrays.equals(a, b): " + Arrays.equals(a, b));
        System.out.println();

        // 2. 排序
        System.out.println("--- 2. 排序 ---");
        int[] toSort = {5, 2, 8, 1, 9};
        System.out.println("排序前: " + Arrays.toString(toSort));

        Arrays.sort(toSort);
        System.out.println("排序后: " + Arrays.toString(toSort));

        // 部分排序
        int[] partial = {5, 2, 8, 1, 9, 3};
        Arrays.sort(partial, 0, 3);
        System.out.println("部分排序(前3个): " + Arrays.toString(partial));

        // 降序
        Integer[] toSortDesc = {5, 2, 8, 1, 9};
        Arrays.sort(toSortDesc, Comparator.reverseOrder());
        System.out.println("降序: " + Arrays.toString(toSortDesc));
        System.out.println();

        // 3. 二分查找
        System.out.println("--- 3. 二分查找 ---");
        int[] sorted = {1, 3, 5, 7, 9};
        System.out.println("sorted数组: " + Arrays.toString(sorted));
        System.out.println("binarySearch(sorted, 5): " + Arrays.binarySearch(sorted, 5));
        System.out.println("binarySearch(sorted, 6): " + Arrays.binarySearch(sorted, 6) + " (插入点-3)");
        System.out.println();

        // 4. 数组与集合转换
        System.out.println("--- 4. 数组与集合转换 ---");
        String[] array = {"a", "b", "c"};
        List<String> list = Arrays.asList(array);
        System.out.println("Arrays.asList: " + list);

        // List转数组
        String[] newArray = list.toArray(new String[0]);
        System.out.println("toArray: " + Arrays.toString(newArray));

        // Stream操作
        int sum = Arrays.stream(new int[]{1, 2, 3, 4, 5}).sum();
        System.out.println("Stream求和: " + sum);
        System.out.println();

        // 5. 多维数组
        System.out.println("--- 5. 多维数组 ---");
        int[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        System.out.println("深toString: " + Arrays.deepToString(matrix));

        int[][] a2 = {{1, 2}, {3, 4}};
        int[][] b2 = {{1, 2}, {3, 4}};
        System.out.println("deepEquals: " + Arrays.deepEquals(a2, b2));
    }
}
