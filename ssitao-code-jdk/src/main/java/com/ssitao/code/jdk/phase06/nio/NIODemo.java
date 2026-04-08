package com.ssitao.code.jdk.phase06.nio;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

/**
 * NIO示例
 */
public class NIODemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== NIO Demo ===\n");

        // 1. Buffer操作
        demonstrateBuffer();

        // 2. FileChannel
        demonstrateFileChannel();

        // 3. Files工具类
        demonstrateFiles();

        // 4. Path操作
        demonstratePath();
    }

    private static void demonstrateBuffer() {
        System.out.println("--- Buffer Operations ---");

        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("Initial state:");
        System.out.println("  capacity=" + buffer.capacity() +
                           ", position=" + buffer.position() +
                           ", limit=" + buffer.limit());

        // 写入数据
        buffer.put((byte) 'H');
        buffer.put((byte) 'e');
        buffer.put((byte) 'l');
        buffer.put((byte) 'l');
        buffer.put((byte) 'o');
        System.out.println("\nAfter writing 'Hello':");
        System.out.println("  capacity=" + buffer.capacity() +
                           ", position=" + buffer.position() +
                           ", limit=" + buffer.limit());

        // flip切换为读模式
        buffer.flip();
        System.out.println("\nAfter flip():");
        System.out.println("  capacity=" + buffer.capacity() +
                           ", position=" + buffer.position() +
                           ", limit=" + buffer.limit());

        // 读取数据
        System.out.print("\nReading: ");
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println();

        // clear清空缓冲区
        buffer.clear();
        System.out.println("\nAfter clear():");
        System.out.println("  capacity=" + buffer.capacity() +
                           ", position=" + buffer.position() +
                           ", limit=" + buffer.limit());

        // compact保留未读数据
        buffer.put("HelloWorld".getBytes());
        buffer.flip();
        buffer.get(new byte[5]);  // 读取5字节
        buffer.compact();
        System.out.println("\nAfter compact():");
        System.out.println("  position=" + buffer.position() +
                           ", limit=" + buffer.limit());
        System.out.print("Remaining: ");
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        System.out.println("\n");
    }

    private static void demonstrateFileChannel() throws Exception {
        System.out.println("--- FileChannel ---");

        String file = "channel.txt";

        // 写入
        try (FileOutputStream fos = new FileOutputStream(file);
             FileChannel channel = fos.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("Hello from FileChannel\n".getBytes());
            buffer.flip();
            channel.write(buffer);
        }
        System.out.println("Written to FileChannel");

        // 读取
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel channel = fis.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            buffer.flip();
            System.out.println("Read from FileChannel: " +
                StandardCharsets.UTF_8.decode(buffer).toString().trim());
        }

        // 文件复制
        String src = "source.txt";
        String dest = "dest_copy.txt";
        Files.write(Paths.get(src), "Source content".getBytes());

        long start = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(src);
             FileOutputStream fos = new FileOutputStream(dest);
             FileChannel in = fis.getChannel();
             FileChannel out = fos.getChannel()) {

            // transferTo
            in.transferTo(0, in.size(), out);
        }
        long time = System.nanoTime() - start;
        System.out.println("File copied via FileChannel.transferTo in " + time / 1000 + "us");

        // 验证复制 (JDK 8 compatible)
        byte[] copiedBytes = Files.readAllBytes(Paths.get(dest));
        String copied = new String(copiedBytes, StandardCharsets.UTF_8);
        System.out.println("Copied content: " + copied);

        System.out.println();
    }

    private static void demonstrateFiles() throws Exception {
        System.out.println("--- Files Utility ---");

        Path testDir = Paths.get("testdir");
        Path testFile = testDir.resolve("test.txt");

        // 创建目录
        Files.createDirectories(testDir);
        System.out.println("Created directory: " + testDir);

        // 写入文件 (JDK 8 compatible)
        String content = "Line 1\nLine 2\nLine 3\n";
        Files.write(testFile, content.getBytes(StandardCharsets.UTF_8));
        System.out.println("Written to: " + testFile);

        // 读取文件 (JDK 8 compatible)
        String read = new String(Files.readAllBytes(testFile), StandardCharsets.UTF_8);
        System.out.println("Read content: " + read.trim());

        // 按行读取
        System.out.println("Lines:");
        List<String> lines = Files.readAllLines(testFile);
        for (int i = 0; i < lines.size(); i++) {
            System.out.println("  " + (i + 1) + ": " + lines.get(i));
        }

        // 遍历目录
        System.out.println("\nWalking directory:");
        Files.walk(testDir)
            .forEach(p -> System.out.println("  " + p));

        // Stream遍历（过滤）
        System.out.println("\n.txt files:");
        Files.list(testDir)
            .filter(p -> p.toString().endsWith(".txt"))
            .forEach(p -> System.out.println("  " + p));

        // 复制和移动
        Path copy = testDir.resolve("test_copy.txt");
        Files.copy(testFile, copy);
        System.out.println("\nCopied to: " + copy);

        // 删除
        Files.deleteIfExists(copy);
        Files.deleteIfExists(testFile);
        Files.deleteIfExists(testDir);
        System.out.println("Cleaned up test files");

        System.out.println();
    }

    private static void demonstratePath() {
        System.out.println("--- Path Operations ---");

        Path path = Paths.get("/home/user/docs/report.txt");

        System.out.println("Path: " + path);
        System.out.println("getFileName: " + path.getFileName());
        System.out.println("getParent: " + path.getParent());
        System.out.println("getRoot: " + path.getRoot());
        System.out.println("getNameCount: " + path.getNameCount());

        System.out.println("\nName components:");
        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println("  [" + i + "] " + path.getName(i));
        }

        // 路径操作
        Path base = Paths.get("/home/user");
        Path docs = base.resolve("docs");
        Path file = docs.resolve("report.txt");
        System.out.println("\nResolved path: " + file);

        // 规范化
        Path messy = Paths.get("/home/user/../user/./docs/file.txt");
        System.out.println("Original: " + messy);
        System.out.println("Normalized: " + messy.normalize());

        // 相对路径
        Path from = Paths.get("/home/user/docs");
        Path to = Paths.get("/home/user/images/logo.png");
        Path relative = from.relativize(to);
        System.out.println("\nFrom: " + from);
        System.out.println("To: " + to);
        System.out.println("Relative: " + relative);

        // 转换为其他类型
        Path p = Paths.get("test.txt");
        File file2 = p.toFile();
        URI uri = p.toUri();
        System.out.println("\ntoFile: " + file2);
        System.out.println("toUri: " + uri);

        System.out.println();
    }
}
