package com.ssitao.code.designpattern.facade.jdk;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.*;

/**
 * JDK中的外观模式示例
 *
 * JDK中使用外观模式的场景：
 * 1. Class.newInstance() - 简化反射创建对象
 * 2. Charset编码/解码 - 统一字符集操作
 * 3. DriverManager.getConnection() - 统一数据库连接
 * 4. Files工具类 - 简化文件操作
 */
public class JdkFacadeDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK Facade Pattern Demo ===\n");

        // 1. Class.newInstance() - 简化反射
        demonstrateClassNewInstance();

        // 2. Charset编码/解码
        demonstrateCharset();

        // 3. DriverManager - 统一数据库连接
        demonstrateDriverManager();

        // 4. Files工具类 - 简化文件操作
        demonstrateFiles();
    }

    /**
     * 1. Class.newInstance() / Constructor.newInstance()
     * 隐藏了反射创建对象的复杂性
     */
    private static void demonstrateClassNewInstance() {
        System.out.println("--- 1. Class.newInstance() (简化反射) ---");

        try {
            // 传统反射方式（复杂）
            Class<?> clazz = Class.forName("java.util.ArrayList");
            Object list1 = clazz.newInstance();

            // 使用newInstance创建对象
            List<String> list2 = ArrayList.class.newInstance();
            list2.add("item1");
            list2.add("item2");
            System.out.println("ArrayList created via newInstance: " + list2);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * 2. Charset编码/解码
     * Charset提供了统一的字符集操作接口，隐藏了底层的编码解码细节
     */
    private static void demonstrateCharset() {
        System.out.println("--- 2. Charset (统一字符集操作) ---");

        String content = "Hello 你好 世界";

        // 获取Charset
        Charset utf8 = StandardCharsets.UTF_8;
        Charset gbk = Charset.forName("GBK");

        // 编码
        ByteBuffer utf8Bytes = utf8.encode(content);
        ByteBuffer gbkBytes = gbk.encode(content);

        System.out.println("原文: " + content);
        System.out.println("UTF-8编码字节数: " + utf8Bytes.remaining());
        System.out.println("GBK编码字节数: " + gbkBytes.remaining());

        // 解码
        utf8Bytes.flip();
        try {
            String decoded = utf8.decode(utf8Bytes).toString();
            System.out.println("UTF-8解码: " + decoded);
        } catch (Exception e) {
            System.out.println("解码失败: " + e.getMessage());
        }

        System.out.println();
    }

    /**
     * 3. DriverManager.getConnection()
     * 外观模式：统一管理各种数据库驱动，简化数据库连接
     */
    private static void demonstrateDriverManager() {
        System.out.println("--- 3. DriverManager (统一数据库连接) ---");

        // DriverManager是外观模式
        // 隐藏了：
        // 1. 加载数据库驱动
        // 2. 管理多个Driver实现
        // 3. 处理连接参数

        // 使用方式
        // Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println("DriverManager外观模式：");
        System.out.println("  - 简化数据库连接流程");
        System.out.println("  - 统一管理多种数据库驱动");
        System.out.println("  - 自动加载和注册驱动");
        System.out.println("  - 处理连接参数");

        // 常见的JDBC URL格式
        // jdbc:mysql://localhost:3306/database
        // jdbc:oracle:thin:@localhost:1521:orcl
        // jdbc:sqlserver://localhost:1433;databaseName=mydb

        System.out.println();
    }

    /**
     * 4. Files工具类
     * Files是文件操作的外观模式，简化了复杂的文件操作
     */
    private static void demonstrateFiles() {
        System.out.println("--- 4. Files工具类 (简化文件操作) ---");

        // Files工具类隐藏了：
        // 1. 打开和关闭文件流
        // 2. 处理各种文件类型
        // 3. 异常处理
        // 4. 平台差异

        // 读取文件
        String content = "Hello World";
        File tempFile = new File("temp_facade.txt");

        try {
            // 写入文件
            List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
            java.nio.file.Files.write(
                tempFile.toPath(),
                lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );
            System.out.println("写入文件成功");

            // 读取文件
            List<String> readLines = java.nio.file.Files.readAllLines(tempFile.toPath());
            System.out.println("读取内容: " + readLines);

            // 读取为字节数组
            byte[] bytes = java.nio.file.Files.readAllBytes(tempFile.toPath());
            System.out.println("字节数组: " + Arrays.toString(bytes));

            // 删除文件
            java.nio.file.Files.delete(tempFile.toPath());
            System.out.println("删除文件成功");

        } catch (IOException e) {
            System.out.println("IO错误: " + e.getMessage());
        }

        System.out.println();
    }
}
