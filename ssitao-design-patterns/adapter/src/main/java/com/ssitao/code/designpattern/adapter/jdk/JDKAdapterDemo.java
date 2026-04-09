package com.ssitao.code.designpattern.adapter.jdk;

import java.io.*;
import java.util.*;

/**
 * JDK中的适配器模式示例
 *
 * JDK中大量使用了适配器模式，常见场景：
 * 1. InputStreamReader - 将字节流转换为字符流
 * 2. OutputStreamWriter - 将字符流转换为字节流
 * 3. Arrays.asList() - 将数组转换为List
 * 4. Collections.enumeration() - 将Iterator转换为Enumeration
 */
public class JDKAdapterDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK Adapter Pattern Demo ===\n");

        // 1. InputStreamReader - 字节流到字符流的适配
        demonstrateInputStreamReader();

        // 2. OutputStreamWriter - 字符流到字节流的适配
        demonstrateOutputStreamWriter();

        // 3. Arrays.asList() - 数组到List的适配
        demonstrateArraysAsList();

        // 4. Collections.enumeration() - Iterator到Enumeration的适配
        demonstrateEnumeration();

        // 5. Properties到Map的适配
        demonstrateProperties();
    }

    /**
     * 1. InputStreamReader示例
     * InputStreamReader将InputStream（字节流）适配为Reader（字符流）
     */
    private static void demonstrateInputStreamReader() {
        System.out.println("--- 1. InputStreamReader (字节流 -> 字符流) ---");

        // 场景：读取包含中文的文件，需要将字节流转换为字符流
        String content = "Hello World 你好世界";
        byte[] bytes = content.getBytes();

        // 使用ByteArrayInputStream模拟字节流
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

        // InputStreamReader就是适配器：将InputStream适配为Reader
        // 可以指定字符编码
        try (InputStreamReader reader = new InputStreamReader(byteStream, "UTF-8")) {
            char[] buffer = new char[1024];
            int length = reader.read(buffer);
            System.out.println("InputStreamReader读取: " + new String(buffer, 0, length));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 等价于直接使用FileReader
        // FileReader本身就继承了InputStreamReader，是将FileInputStream适配为Reader
        System.out.println();
    }

    /**
     * 2. OutputStreamWriter示例
     * OutputStreamWriter将Writer（字符流）适配为OutputStream（字节流）
     */
    private static void demonstrateOutputStreamWriter() {
        System.out.println("--- 2. OutputStreamWriter (字符流 -> 字节流) ---");

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        // OutputStreamWriter就是适配器：将Writer适配为OutputStream
        try (OutputStreamWriter writer = new OutputStreamWriter(byteStream, "UTF-8")) {
            writer.write("Hello World 你好世界");
            writer.flush();

            System.out.println("OutputStreamWriter写入: " + byteStream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    /**
     * 3. Arrays.asList()示例
     * Arrays.asList()将数组适配为List视图
     * 这是一种特殊的适配：让数组表现出List的行为
     */
    private static void demonstrateArraysAsList() {
        System.out.println("--- 3. Arrays.asList() (数组 -> List) ---");

        String[] array = {"Apple", "Banana", "Orange"};

        // Arrays.asList()将数组适配为List
        // 返回的List背后还是数组，但提供了List的接口
        List<String> list = Arrays.asList(array);

        System.out.println("原始数组: " + Arrays.toString(array));
        System.out.println("asList后: " + list);
        System.out.println("list.get(0): " + list.get(0));
        System.out.println("list.size(): " + list.size());

        // 注意：对List的修改会影响原数组
        list.set(0, "Grape");
        System.out.println("修改后数组: " + Arrays.toString(array));

        // 注意：不能进行add/remove操作（因为底层是固定大小的数组）
        // list.add("Watermelon"); // 会抛出UnsupportedOperationException

        System.out.println();
    }

    /**
     * 4. Collections.enumeration()示例
     * Collections.enumeration()将Iterator适配为Enumeration
     * 这是为了兼容旧的JDK代码（如Hashtable的keys()方法返回Enumeration）
     */
    private static void demonstrateEnumeration() {
        System.out.println("--- 4. Collections.enumeration() (Iterator -> Enumeration) ---");

        List<String> list = Arrays.asList("A", "B", "C");

        // 获取List的Iterator
        Iterator<String> iterator = list.iterator();

        // 将Iterator适配为Enumeration（为了兼容旧API）
        Enumeration<String> enumeration = Collections.enumeration(list);

        System.out.print("Enumeration遍历: ");
        while (enumeration.hasMoreElements()) {
            System.out.print(enumeration.nextElement() + " ");
        }
        System.out.println();

        // 相反的操作：Collections.list()将Enumeration转换为List
        List<String> newList = Collections.list(enumeration);
        System.out.println("转换为List: " + newList);

        System.out.println();
    }

    /**
     * 5. Properties到Map的适配
     * Properties继承自Hashtable，实际上是一种适配
     */
    private static void demonstrateProperties() {
        System.out.println("--- 5. Properties (Hashtable -> Map) ---");

        Properties props = new Properties();

        // Properties提供了方便的方法来读写属性
        props.setProperty("username", "admin");
        props.setProperty("password", "123456");
        props.setProperty("url", "jdbc:mysql://localhost:3306/test");

        // 可以像Map一样使用
        System.out.println("username: " + props.getProperty("username"));
        System.out.println("所有属性: " + props);

        // 同时也兼容旧API：get和put
        props.put("newKey", "newValue");  // 使用Hashtable的方法
        System.out.println("newKey: " + props.get("newKey"));

        System.out.println();
    }
}
