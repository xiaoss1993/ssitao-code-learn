package com.ssitao.code.jdk.phase01.string;

/**
 * 第一阶段步骤2: String家族 - String类常用方法演示
 */
public class StringDemo {

    public static void main(String[] args) {
        System.out.println("=== String类常用方法 ===\n");

        String str = "Hello World, Hello Java";

        // 1. 基础属性
        System.out.println("--- 基础属性 ---");
        System.out.println("str = \"" + str + "\"");
        System.out.println("str.length() = " + str.length());
        System.out.println("str.isEmpty() = " + str.isEmpty());
        System.out.println();

        // 2. 字符访问
        System.out.println("--- 字符访问 ---");
        System.out.println("str.charAt(0) = " + str.charAt(0));
        System.out.println("str.charAt(6) = '" + str.charAt(6) + "'");
        System.out.println("str.codePointAt(0) = " + str.codePointAt(0));
        System.out.println();

        // 3. 比较操作
        System.out.println("--- 比较操作 ---");
        String s1 = "hello";
        String s2 = "hello";
        String s3 = "Hello";
        System.out.println("s1 = \"" + s1 + "\", s2 = \"" + s2 + "\", s3 = \"" + s3 + "\"");
        System.out.println("s1 == s2 = " + (s1 == s2) + " (字面量,同一引用)");
        System.out.println("s1.equals(s2) = " + s1.equals(s2));
        System.out.println("s1.equals(s3) = " + s1.equals(s3) + " (区分大小写)");
        System.out.println("s1.equalsIgnoreCase(s3) = " + s1.equalsIgnoreCase(s3));
        System.out.println("s1.compareTo(s3) = " + s1.compareTo(s3) + " (字典顺序)");
        System.out.println();

        // 4. 查找与判断
        System.out.println("--- 查找与判断 ---");
        System.out.println("str.contains(\"World\") = " + str.contains("World"));
        System.out.println("str.startsWith(\"Hello\") = " + str.startsWith("Hello"));
        System.out.println("str.startsWith(\"World\", 6) = " + str.startsWith("World", 6));
        System.out.println("str.endsWith(\"Java\") = " + str.endsWith("Java"));
        System.out.println("str.indexOf(\"Hello\") = " + str.indexOf("Hello"));
        System.out.println("str.lastIndexOf(\"Hello\") = " + str.lastIndexOf("Hello"));
        System.out.println();

        // 5. 截取与分割
        System.out.println("--- 截取与分割 ---");
        System.out.println("str.substring(6) = \"" + str.substring(6) + "\"");
        System.out.println("str.substring(6, 11) = \"" + str.substring(6, 11) + "\"");
        String[] parts = str.split(" ");
        System.out.print("str.split(\" \") = [");
        for (int i = 0; i < parts.length; i++) {
            System.out.print("\"" + parts[i] + "\"");
            if (i < parts.length - 1) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println();

        // 6. 替换操作
        System.out.println("--- 替换操作 ---");
        System.out.println("str.replace(\"Hello\", \"Hi\") = \"" + str.replace("Hello", "Hi") + "\"");
        System.out.println("str.replaceFirst(\"Hello\", \"Hi\") = \"" + str.replaceFirst("Hello", "Hi") + "\"");
        System.out.println("str.replaceAll(\"[A-Z]\", \"#\") = \"" + str.replaceAll("[A-Z]", "#") + "\"");
        System.out.println();

        // 7. 大小写转换
        System.out.println("--- 大小写转换 ---");
        System.out.println("str.toUpperCase() = \"" + str.toUpperCase() + "\"");
        System.out.println("str.toLowerCase() = \"" + str.toLowerCase() + "\"");
        System.out.println();

        // 8. 空白处理
        System.out.println("--- 空白处理 ---");
        String withSpaces = "  Hello World  ";
        System.out.println("withSpaces = \"  Hello World  \"");
        System.out.println("withSpaces.trim() = \"" + withSpaces.trim() + "\"");
        System.out.println();

        // 9. 字符串拼接
        System.out.println("--- 字符串拼接 ---");
        System.out.println("String.join(\"-\", \"a\", \"b\", \"c\") = \"" + String.join("-", "a", "b", "c") + "\"");
    }
}
