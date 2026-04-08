package com.ssitao.code.jdk.phase01.string;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 第一阶段步骤2: String家族 - 练习题答案
 */
public class StringExercises {

    public static void main(String[] args) {
        System.out.println("=== 字符串练习题 ===\n");

        // 1. 反转字符串
        System.out.println("--- 1. 反转字符串 ---");
        String input1 = "hello";
        String reversed = reverseString(input1);
        System.out.println("reverse(\"" + input1 + "\") = \"" + reversed + "\"");

        // 2. 判断回文
        System.out.println("\n--- 2. 判断回文 ---");
        System.out.println("isPalindrome(\"abcba\") = " + isPalindrome("abcba"));
        System.out.println("isPalindrome(\"abc\") = " + isPalindrome("abc"));
        System.out.println("isPalindrome(\"aba\") = " + isPalindrome("aba"));

        // 3. 统计字符出现次数
        System.out.println("\n--- 3. 统计字符出现次数 ---");
        String input3 = "hello world";
        Map<Character, Long> charCount = countChars(input3);
        System.out.println("countChars(\"" + input3 + "\") = " + charCount);

        // 4. 字符串压缩
        System.out.println("\n--- 4. 字符串压缩 ---");
        String input4 = "aaabbbcc";
        String compressed = compress(input4);
        System.out.println("compress(\"" + input4 + "\") = \"" + compressed + "\"");

        // 5. 字符串常量池面试题
        System.out.println("\n--- 5. 字符串常量池面试题 ---");
        String s1 = "abc";
        String s2 = new String("abc");
        String s3 = s2.intern();
        System.out.println("s1 == s2: " + (s1 == s2) + " (s2是堆对象,s1是常量池)");
        System.out.println("s1.equals(s2): " + s1.equals(s2) + " (内容相等)");
        System.out.println("s1 == s3: " + (s1 == s3) + " (s3返回常量池引用)");
    }

    /**
     * 1. 反转字符串
     */
    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 2. 判断回文
     */
    public static boolean isPalindrome(String str) {
        int left = 0, right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left++) != str.charAt(right--)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 3. 统计字符出现次数
     */
    public static Map<Character, Long> countChars(String str) {
        return str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
    }

    /**
     * 4. 字符串压缩
     * 例如: "aaabbbcc" -> "a3b3c2"
     */
    public static String compress(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder sb = new StringBuilder();
        int count = 1;

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                sb.append(str.charAt(i - 1)).append(count);
                count = 1;
            }
        }
        sb.append(str.charAt(str.length() - 1)).append(count);

        return sb.toString();
    }
}
