package com.ssitao.code.jdk.phase01.wrapper;

/**
 * 第一阶段步骤3: 包装类 - 自动装箱与拆箱演示
 */
public class WrapperDemo {

    public static void main(String[] args) {
        System.out.println("=== 包装类与自动装箱/拆箱 ===\n");

        // 1. 基本类型与包装类型
        System.out.println("--- 1. 基本类型与包装类型 ---");
        int primitiveInt = 10;
        Integer wrapperInt = 10;  // 自动装箱
        System.out.println("primitiveInt = " + primitiveInt);
        System.out.println("wrapperInt = " + wrapperInt);
        System.out.println("primitiveInt == wrapperInt: " + (primitiveInt == wrapperInt));  // 自动拆箱
        System.out.println();

        // 2. Integer缓存机制
        System.out.println("--- 2. Integer缓存机制 (-128~127) ---");
        Integer a = 127;
        Integer b = 127;
        System.out.println("Integer(127) == Integer(127): " + (a == b) + " (缓存范围内,同一引用)");

        Integer c = 128;
        Integer d = 128;
        System.out.println("Integer(128) == Integer(128): " + (c == d) + " (超出缓存,不同对象)");
        System.out.println("Integer(128).equals(Integer(128)): " + c.equals(d));
        System.out.println();

        // 3. 装箱的陷阱
        System.out.println("--- 3. 装箱的陷阱 ---");

        // 陷阱1: equals和==混淆
        Integer e = 100;
        Integer f = 100;
        System.out.println("e == f: " + (e == f) + " (缓存范围内)");
        System.out.println("e.equals(f): " + e.equals(f));

        // 陷阱2: 运算时自动拆箱
        Integer g = 10;
        Integer h = 20;
        System.out.println("g + h = " + (g + h) + " (运算时自动拆箱)");

        // 陷阱3: 空指针
        Integer i = null;
        try {
            int j = i;  // 自动拆箱,抛出NPE
        } catch (NullPointerException e2) {
            System.out.println("Integer为null时自动拆箱抛出NPE!");
        }
        System.out.println();

        // 4. Integer常用方法
        System.out.println("--- 4. Integer常用方法 ---");
        System.out.println("Integer.MAX_VALUE = " + Integer.MAX_VALUE);
        System.out.println("Integer.MIN_VALUE = " + Integer.MIN_VALUE);
        System.out.println("Integer.bitCount(10) = " + Integer.bitCount(10) + " (10的二进制1的个数)");
        System.out.println("Integer.parseInt(\"123\") = " + Integer.parseInt("123"));
        System.out.println("Integer.parseInt(\"FF\", 16) = " + Integer.parseInt("FF", 16) + " (十六进制)");
        System.out.println("Integer.toBinaryString(10) = " + Integer.toBinaryString(10));
        System.out.println();

        // 5. Boolean
        System.out.println("--- 5. Boolean ---");
        System.out.println("Boolean.parseBoolean(\"true\") = " + Boolean.parseBoolean("true"));
        System.out.println("Boolean.parseBoolean(\"TRUE\") = " + Boolean.parseBoolean("TRUE"));
        System.out.println("Boolean.parseBoolean(\"anything\") = " + Boolean.parseBoolean("anything"));
        System.out.println();

        // 6. Character
        System.out.println("--- 6. Character ---");
        char ch = 'A';
        System.out.println("Character.isUpperCase('A') = " + Character.isUpperCase(ch));
        System.out.println("Character.isLowerCase('a') = " + Character.isLowerCase('a'));
        System.out.println("Character.isLetter('中') = " + Character.isLetter('中'));
        System.out.println("Character.isDigit('5') = " + Character.isDigit('5'));
        System.out.println("Character.toLowerCase('A') = " + Character.toLowerCase('A'));
        System.out.println("Character.toUpperCase('a') = " + Character.toUpperCase('a'));
    }
}
