package com.learn.crazyjava.lesson05_collection;

import java.util.*;

/**
 * 第5课：集合框架 - Map操作
 */
public class MapDemo {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("语文", 90);
        map.put("数学", 95);
        map.put("英语", 88);

        // 获取
        System.out.println("数学成绩：" + map.get("数学"));
        System.out.println("物理成绩（默认）：" + map.getOrDefault("物理", 0));

        // 遍历方式1：keySet
        System.out.println("=== keySet遍历 ===");
        for (String key : map.keySet()) {
            System.out.println(key + ":" + map.get(key));
        }

        // 遍历方式2：entrySet
        System.out.println("=== entrySet遍历 ===");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

        // 遍历方式3：forEach (JDK 8+)
        System.out.println("=== forEach遍历 ===");
        map.forEach((k, v) -> System.out.println(k + ":" + v));

        // 统计字符出现次数
        String str = "abracadabra";
        Map<Character, Integer> charCount = new HashMap<>();
        for (char c : str.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        System.out.println("字符统计：" + charCount);
    }
}
