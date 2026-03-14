package com.ssitao.code.designpattern.decorator;

import java.io.*;

/**
 * JDK IO流中的装饰器模式
 *
 * InputStream是Component
 * FileInputStream是具体组件
 * FilterInputStream是装饰器基类
 * BufferedInputStream/DataInputStream是具体装饰器
 */
public class InputStreamExample {

    public static void main(String[] args) {
        System.out.println("========== 装饰器模式 - JDK IO流示例 ==========\n");

        String filePath = "test.txt";

        // 创建测试文件
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Hello World!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1. 原始文件读取
        System.out.println("--- 原始文件读取 ---");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int data;
            while ((data = fis.read()) != -1) {
                System.out.print((char) data);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 带缓冲的文件读取 (装饰器)
        System.out.println("\n--- 带缓冲的文件读取 ---");
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                System.out.print(new String(buffer, 0, len));
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. 数据输入流 (装饰器)
        System.out.println("\n--- 数据输入流 ---");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {

            dos.writeInt(100);
            dos.writeDouble(3.14);
            dos.writeUTF("装饰器模式");

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            DataInputStream dis = new DataInputStream(bais);

            System.out.println("读取int: " + dis.readInt());
            System.out.println("读取double: " + dis.readDouble());
            System.out.println("读取UTF: " + dis.readUTF());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. 组合装饰器: 缓冲 + 数据输入
        System.out.println("\n--- 组合装饰器 ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("读取: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 清理测试文件
        new File(filePath).delete();
    }
}
