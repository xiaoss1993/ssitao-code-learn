package com.learn.crazyjava.lesson03_string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式演示
 */
public class RegexDemo {
    public static void main(String[] args) {
        // 验证手机号
        String phone = "13812345678";
        System.out.println("手机号验证：" + phone.matches("1[3-9]\\d{9}"));

        // 验证邮箱
        String email = "test@example.com";
        System.out.println("邮箱验证：" + email.matches("\\w+@\\w+\\.\\w+"));

        // 使用Pattern和Matcher
        String text = "我的手机号是13812345678，另一个是13987654321";
        Pattern pattern = Pattern.compile("1[3-9]\\d{9}");
        Matcher matcher = pattern.matcher(text);

        System.out.print("找到的手机号：");
        while (matcher.find()) {
            System.out.print(matcher.group() + " ");
        }
        System.out.println();

        // 替换
        String masked = text.replaceAll("1[3-9]\\d{9}", "***********");
        System.out.println("脱敏后：" + masked);
    }
}
