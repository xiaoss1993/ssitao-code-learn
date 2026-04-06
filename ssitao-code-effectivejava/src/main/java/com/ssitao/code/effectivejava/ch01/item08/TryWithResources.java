package com.ssitao.code.effectivejava.ch01.item08;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Item 9: Try-with-resources for automatic resource management
 *
 * Demonstrates the proper way to handle closeable resources
 */
public class TryWithResources {

    // ==================== Problem: Traditional try-finally ====================
    // Traditional way - verbose and error-prone
    static String readFirstLine(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally {
            if (br != null) {
                br.close();  // Error-prone: if readLine() throws, close() might not run
            }
        }
    }

    // ==================== Solution: Try-with-resources ====================
    // Automatically closes resources, even if exception occurs
    static String readFirstLineBetter(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }  // br.close() called automatically
    }

    // ==================== Multiple Resources ====================
    // Easy to handle multiple resources
    static void copyFile(String source, String target) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(source));
             BufferedWriter out = new BufferedWriter(new FileWriter(target))) {
            String line;
            while ((line = in.readLine()) != null) {
                out.write(line);
                out.newLine();
            }
        }  // Both in and out closed automatically
    }

    // ==================== Suppressed Exceptions ====================
    // Try-with-resources preserves both primary and suppressed exceptions
    static void demonstrateSuppressed() {
        try (AutoCloseableDemo demo = new AutoCloseableDemo()) {
            demo.doSomething();
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            System.out.println("Suppressed exceptions: " + e.getSuppressed().length);
            for (Throwable t : e.getSuppressed()) {
                System.out.println("  Suppressed: " + t.getMessage());
            }
        }
    }

    // ==================== Custom Resource Class ====================
    static class DatabaseConnection implements AutoCloseable {
        private final String name;
        private boolean closed = false;

        public DatabaseConnection(String name) {
            this.name = name;
            System.out.println("Opening connection: " + name);
        }

        public void query(String sql) {
            if (closed) throw new IllegalStateException("Connection closed");
            System.out.println("Executing: " + sql);
        }

        @Override
        public void close() {
            if (closed) return;
            closed = true;
            System.out.println("Closing connection: " + name);
        }
    }

    static void databaseExample() {
        System.out.println("\n=== Database Connection Demo ===");

        // Try-with-resources ensures proper cleanup
        try (DatabaseConnection conn = new DatabaseConnection("MAIN_DB")) {
            conn.query("SELECT * FROM users");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Multiple resources
        System.out.println("\n--- Multiple Connections ---");
        try (DatabaseConnection conn1 = new DatabaseConnection("DB1");
             DatabaseConnection conn2 = new DatabaseConnection("DB2")) {
            conn1.query("SELECT FROM DB1");
            conn2.query("SELECT FROM DB2");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==================== Resource with Exception in Constructor ====================
    static class ResourceA implements AutoCloseable {
        public ResourceA() throws Exception {
            throw new Exception("Failed to create ResourceA");
        }

        @Override
        public void close() {
            System.out.println("ResourceA.close()");
        }
    }

    static class ResourceB implements AutoCloseable {
        public ResourceB() {
            System.out.println("ResourceB created");
        }

        @Override
        public void close() {
            System.out.println("ResourceB.close()");
        }
    }

    static void resourceCreationOrder() {
        System.out.println("\n=== Resource Creation Order ===");

        // ResourceB IS closed even if ResourceA construction fails
        // But ResourceA.close() is NOT called because it was never created
        try (ResourceB b = new ResourceB();
             ResourceA a = new ResourceA()) {
            System.out.println("Using resources");
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println("Only ResourceB was created, so only ResourceB.close() is called");
        }
    }

    // ==================== Helper Classes for Demo ====================
    static class AutoCloseableDemo implements AutoCloseable {
        public void doSomething() throws Exception {
            throw new Exception("Primary exception in doSomething()");
        }

        @Override
        public void close() throws Exception {
            throw new Exception("Suppressed exception in close()");
        }
    }

    // ==================== JDK Resources Using TWR ====================
    static void jdkExamples() {
        System.out.println("\n=== JDK Resource Examples ===");

        // java.nio.file.Files.lines()
        // try (Stream<String> lines = Files.lines(path)) {
        //     lines.forEach(System.out::println);
        // }

        // java.sql
        // try (Connection conn = ds.getConnection();
        //      Statement stmt = conn.createStatement()) {
        //     ResultSet rs = stmt.executeQuery(query);
        // }

        // java.util.zip.ZipFile
        // try (ZipFile zf = new ZipFile(zipFileName)) {
        //     // process entries
        // }

        System.out.println("Common JDK classes implementing AutoCloseable:");
        System.out.println("- java.io.InputStream, OutputStream, Reader, Writer");
        System.out.println("- java.sql.Connection, Statement, ResultSet");
        System.out.println("- java.nio.channels.Channel");
        System.out.println("- java.util.zip.ZipFile, JarFile");
        System.out.println("- java.nio.file.FileSystem");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Try-with-Resources Demo");
        System.out.println("========================================\n");

        System.out.println("--- Traditional vs TWR ---");
        System.out.println("Traditional try-finally:");
        System.out.println("  - Manual close() call required");
        System.out.println("  - Error-prone with exceptions");
        System.out.println("  - Verbose code");

        System.out.println("\nTry-with-resources:");
        System.out.println("  - Automatic close()");
        System.out.println("  - Suppressed exceptions preserved");
        System.out.println("  - Cleaner, readable code");

        databaseExample();
        demonstrateSuppressed();
        resourceCreationOrder();
        jdkExamples();

        System.out.println("\n========================================");
        System.out.println("Key Benefits:");
        System.out.println("1. Automatic resource cleanup");
        System.out.println("2. Preserves exception information");
        System.out.println("3. Cleaner, more readable code");
        System.out.println("4. Prevents resource leaks");
        System.out.println("========================================");
    }
}
