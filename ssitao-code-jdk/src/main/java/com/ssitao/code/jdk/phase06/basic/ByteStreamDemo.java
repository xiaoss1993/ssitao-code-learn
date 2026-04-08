package com.ssitao.code.jdk.phase06.basic;

import java.io.*;

/**
 * 字节流示例
 */
public class ByteStreamDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Byte Stream Demo ===\n");

        // 1. FileInputStream/FileOutputStream
        demonstrateFileStream();

        // 2. ByteArrayInputStream/ByteArrayOutputStream
        demonstrateByteArray();

        // 3. DataInputStream/DataOutputStream
        demonstrateDataStream();

        // 4. 文件复制
        copyFile("input.txt", "output.txt");
    }

    private static void demonstrateFileStream() throws Exception {
        System.out.println("--- FileInputStream/FileOutputStream ---");

        String file = "test.dat";

        // 写入
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(65);  // 写入'A'
            fos.write("Hello".getBytes());
            fos.write(new byte[]{66, 67, 68});  // 写入'BCD'
        }
        System.out.println("Written to " + file);

        // 读取
        try (FileInputStream fis = new FileInputStream(file)) {
            System.out.print("Content: ");
            int data;
            while ((data = fis.read()) != -1) {
                System.out.print((char) data);
            }
            System.out.println();
        }

        // 按数组读取
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead = fis.read(buffer);
            System.out.println("Read " + bytesRead + " bytes");
        }

        System.out.println();
    }

    private static void demonstrateByteArray() throws Exception {
        System.out.println("--- ByteArrayInputStream/ByteArrayOutputStream ---");

        // ByteArrayInputStream
        byte[] data = "Hello World".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        System.out.print("ByteArrayInputStream: ");
        int ch;
        while ((ch = bais.read()) != -1) {
            System.out.print((char) ch);
        }
        System.out.println();

        // ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("Hello".getBytes());
        baos.write(" ".getBytes());
        baos.write("World".getBytes());
        baos.writeTo(new FileOutputStream("baos.txt"));

        System.out.println("ByteArrayOutputStream size: " + baos.size());
        System.out.println("ByteArrayOutputStream content: " + baos.toString());

        System.out.println();
    }

    private static void demonstrateDataStream() throws Exception {
        System.out.println("--- DataInputStream/DataOutputStream ---");

        String file = "data.bin";

        // 写入各种数据类型
        try (DataOutputStream dos = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            dos.writeInt(42);
            dos.writeDouble(3.14159);
            dos.writeBoolean(true);
            dos.writeUTF("Hello");
            dos.writeChars("World");  // 写入字符（无编码）
        }
        System.out.println("Written various data types");

        // 读取
        try (DataInputStream dis = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            int num = dis.readInt();
            double pi = dis.readDouble();
            boolean flag = dis.readBoolean();
            String utf = dis.readUTF();

            System.out.println("int: " + num);
            System.out.println("double: " + pi);
            System.out.println("boolean: " + flag);
            System.out.println("UTF: " + utf);
        }

        System.out.println();
    }

    private static void copyFile(String src, String dest) throws Exception {
        System.out.println("--- File Copy ---");

        long start = System.nanoTime();
        try (FileInputStream fis = new FileInputStream(src);
             FileOutputStream fos = new FileOutputStream(dest)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
        long time = System.nanoTime() - start;
        System.out.println("File copied in " + time / 1_000_000 + "ms");

        System.out.println();
    }
}
