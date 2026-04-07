package com.ssitao.code.effectivejava.ch01.item08;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 条目9：使用try-with-resources处理自动资源管理
 *
 * try-with-resources的优点：
 * 1. 自动关闭资源：即使发生异常也会调用close()
 * 2. 保留被抑制的异常：可以同时看到主异常和close()抛出的异常
 * 3. 代码更简洁：无需手动在finally中关闭资源
 * 4. 防止资源泄漏：确保资源被正确关闭
 *
 * 只要实现了AutoCloseable接口的类都可以使用TWR
 */
public class TryWithResources {

    // ==================== 问题：传统的try-finally ====================
    /**
     * 传统方式 - 冗长且容易出错
     * 问题：如果readLine()抛出异常，close()可能不会执行
     */
    static String readFirstLine(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            if (br != null) {
                br.close();  // 容易出错：如果readLine()抛异常，close()可能不执行
            }
        }
    }

    // ==================== 解决方案：try-with-resources ====================
    /**
     * TWR方式 - 自动关闭资源，即使发生异常
     */
    static String readFirstLineBetter(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }  // br.close() 自动调用
    }

    // ==================== 多个资源 ====================
    /**
     * 轻松处理多个资源
     */
    static void copyFile(String source, String target) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(source));
             BufferedWriter out = new BufferedWriter(new FileWriter(target))) {
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
        }  // in和out都会自动关闭
    }

    // ==================== 被抑制的异常 ====================
    /**
     * TWR保留主异常和被抑制的异常
     * 如果close()也抛出异常，原异常会被保留，close()的异常被抑制
     */
    static void demonstrateSuppressed() {
        try (AutoCloseableDemo demo = new AutoCloseableDemo()) {
            demo.doSomething();
        } catch (Exception e) {
            System.out.println("捕获的异常: " + e.getMessage());
            System.out.println("被抑制的异常数量: " + e.getSuppressed().length);
            for (Throwable t : e.getSuppressed()) {
                System.out.println("  被抑制的: " + t.getMessage());
            }
        }
    }

    // ==================== 自定义资源类 ====================
    /**
     * 数据库连接 - 实现AutoCloseable接口
     */
    static class DatabaseConnection implements AutoCloseable {
        private final String name;
        private boolean closed = false;

        public DatabaseConnection(String name) {
            this.name = name;
            System.out.println("打开连接: " + name);
        }

        public void query(String sql) {
            if (closed) throw new IllegalStateException("连接已关闭");
            System.out.println("执行: " + sql);
        }

        @Override
        public void close() {
            if (closed) return;
            closed = true;
            System.out.println("关闭连接: " + name);
        }
    }

    static void databaseExample() {
        System.out.println("\n=== 数据库连接示例 ===");

        // TWR确保资源正确清理
        try (DatabaseConnection conn = new DatabaseConnection("MAIN_DB")) {
            conn.query("SELECT * FROM users");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }

        // 多个资源
        System.out.println("\n--- 多个连接 ---");
        try (DatabaseConnection conn1 = new DatabaseConnection("DB1");
             DatabaseConnection conn2 = new DatabaseConnection("DB2")) {
            conn1.query("从DB1查询");
            conn2.query("从DB2查询");
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
        }
    }

    // ==================== 构造器中抛异常的资源 ====================
    /**
     * 资源A：构造器抛异常
     */
    static class ResourceA implements AutoCloseable {
        public ResourceA() throws Exception {
            throw new Exception("创建ResourceA失败");
        }

        @Override
        public void close() {
            System.out.println("ResourceA.close()");
        }
    }

    /**
     * 资源B：正常构造
     */
    static class ResourceB implements AutoCloseable {
        public ResourceB() {
            System.out.println("ResourceB已创建");
        }

        @Override
        public void close() {
            System.out.println("ResourceB.close()");
        }
    }

    static void resourceCreationOrder() {
        System.out.println("\n=== 资源创建顺序 ===");

        // ResourceB会被关闭，即使ResourceA构造失败
        // 但ResourceA.close()不会被调用，因为它从未被创建
        try (ResourceB b = new ResourceB();
             ResourceA a = new ResourceA()) {
            System.out.println("使用资源");
        } catch (Exception e) {
            System.out.println("异常: " + e.getMessage());
            System.out.println("只有ResourceB被创建，所以只有ResourceB.close()被调用");
        }
    }

    // ==================== 示例辅助类 ====================
    /**
     * 演示：doSomething()和close()都抛异常
     */
    static class AutoCloseableDemo implements AutoCloseable {
        public void doSomething() throws Exception {
            throw new Exception("doSomething()中的主异常");
        }

        @Override
        public void close() throws Exception {
            throw new Exception("close()中的被抑制异常");
        }
    }

    // ==================== JDK中使用TWR的类 ====================
    static void jdkExamples() {
        System.out.println("\n=== JDK中的TWR示例 ===");

        System.out.println("实现了AutoCloseable的常用JDK类:");
        System.out.println("- java.io.InputStream, OutputStream, Reader, Writer");
        System.out.println("- java.sql.Connection, Statement, ResultSet");
        System.out.println("- java.nio.channels.Channel");
        System.out.println("- java.util.zip.ZipFile, JarFile");
        System.out.println("- java.nio.file.FileSystem");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Try-with-Resources 示例");
        System.out.println("========================================\n");

        System.out.println("--- 传统方式 vs TWR ---");
        System.out.println("传统try-finally:");
        System.out.println("  - 需要手动调用close()");
        System.out.println("  - 异常处理容易出错");
        System.out.println("  - 代码冗长");

        System.out.println("\ntry-with-resources:");
        System.out.println("  - 自动调用close()");
        System.out.println("  - 保留被抑制的异常");
        System.out.println("  - 代码更简洁清晰");

        databaseExample();
        demonstrateSuppressed();
        resourceCreationOrder();
        jdkExamples();

        System.out.println("\n========================================");
        System.out.println("主要优点:");
        System.out.println("1. 自动资源清理");
        System.out.println("2. 保留异常信息");
        System.out.println("3. 代码更简洁清晰");
        System.out.println("4. 防止资源泄漏");
        System.out.println("========================================");
    }
}
