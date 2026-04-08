package com.ssitao.code.jdk.phase01.string;

/**
 * 第一阶段步骤2: String家族 - StringBuilder vs StringBuffer
 */
public class StringBuilderDemo {

    public static void main(String[] args) {
        System.out.println("=== StringBuilder vs StringBuffer ===\n");

        // StringBuilder演示
        System.out.println("--- StringBuilder ---");
        StringBuilder sb = new StringBuilder();
        System.out.println("初始容量: " + sb.capacity());

        sb.append("Hello");
        System.out.println("append(\"Hello\")后, 长度: " + sb.length() + ", 容量: " + sb.capacity());

        sb.append(" World");
        System.out.println("append(\" World\")后, 长度: " + sb.length() + ", 容量: " + sb.capacity());

        sb.insert(5, ",");
        System.out.println("insert(5, \",\")后: " + sb.toString());

        sb.delete(5, 6);
        System.out.println("delete(5, 6)后: " + sb.toString());

        sb.reverse();
        System.out.println("reverse()后: " + sb.toString());

        // StringJoiner演示
        System.out.println("\n--- StringJoiner (JDK 8+) ---");
        java.util.StringJoiner sj = new java.util.StringJoiner(", ", "[", "]");
        sj.add("Apple").add("Banana").add("Cherry");
        System.out.println("StringJoiner: " + sj.toString());

        // 扩容演示
        System.out.println("\n--- StringBuilder扩容机制 ---");
        StringBuilder sb2 = new StringBuilder();
        System.out.println("初始: 容量=" + sb2.capacity());
        for (int i = 0; i < 20; i++) {
            sb2.append("a");
        }
        System.out.println("添加20字符后: 容量=" + sb2.capacity());
    }
}
