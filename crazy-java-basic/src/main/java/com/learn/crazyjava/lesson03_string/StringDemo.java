package com.learn.crazyjava.lesson03_string;

/**
 * 第3课：字符串处理 - String常用操作
 */
public class StringDemo {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = new String("hello");

        // 常用方法
        System.out.println("长度：" + s1.length());
        System.out.println("首字符：" + s1.charAt(0));
        System.out.println("子串：" + s1.substring(1, 3));
        System.out.println("ll位置：" + s1.indexOf("ll"));
        System.out.println("替换：" + s1.replace("l", "L"));
        System.out.println("大写：" + s1.toUpperCase());
        System.out.println("去空格：" + "  abc  ".trim());

        // 比较
        System.out.println("equals：" + s1.equals(s2));
        System.out.println("==：" + (s1 == s2));

        // 分割
        String str = "apple,banana,orange";
        String[] fruits = str.split(",");
        for (String fruit : fruits) {
            System.out.println("水果：" + fruit);
        }

        // 格式化
        String msg = String.format("姓名：%s，年龄：%d，身高：%.2f", "张三", 25, 1.75);
        System.out.println(msg);
    }
}
