package com.ssitao.code.jdk.phase06.basic;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 字符流示例
 */
public class CharStreamDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Char Stream Demo ===\n");

        // 1. FileReader/FileWriter
        demonstrateFileReaderWriter();

        // 2. BufferedReader/BufferedWriter
        demonstrateBufferedReaderWriter();

        // 3. InputStreamReader/OutputStreamWriter
        demonstrateInputStreamReaderWriter();

        // 4. StringReader/StringWriter
        demonstrateStringReaderWriter();
    }

    private static void demonstrateFileReaderWriter() throws Exception {
        System.out.println("--- FileReader/FileWriter ---");

        String file = "text.txt";

        // 写入
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Hello ");
            fw.write("World\n");
            fw.write("中文内容");
        }
        System.out.println("Written to " + file);

        // 读取
        try (FileReader fr = new FileReader(file)) {
            int ch;
            while ((ch = fr.read()) != -1) {
                System.out.print((char) ch);
            }
        }

        System.out.println();
    }

    private static void demonstrateBufferedReaderWriter() throws Exception {
        System.out.println("--- BufferedReader/BufferedWriter ---");

        String file = "buffered.txt";

        // 写入（带缓冲）
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            bw.write("Line 1");
            bw.newLine();
            bw.write("Line 2");
            bw.newLine();
            bw.write("中文行");
        }
        System.out.println("Written with BufferedWriter");

        // 按行读取
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                System.out.println(lineNum++ + ": " + line);
            }
        }

        // 使用lines()方法（JDK 8+）
        System.out.println("\nUsing lines():");
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            br.lines()
                .filter(l -> !l.isEmpty())
                .forEach(l -> System.out.println("  " + l));
        }

        System.out.println();
    }

    private static void demonstrateInputStreamReaderWriter() throws Exception {
        System.out.println("--- InputStreamReader/OutputStreamWriter ---");

        String file = "utf8.txt";

        // 指定编码写入
        try (OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8)) {
            osw.write("Hello World\n");
            osw.write("中文内容");
        }
        System.out.println("Written with UTF-8 encoding");

        // 读取时指定编码
        try (InputStreamReader isr = new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8)) {
            char[] buffer = new char[1024];
            int charsRead = isr.read(buffer);
            System.out.println("Read " + charsRead + " chars");
            System.out.println("Content: " + new String(buffer, 0, charsRead));
        }

        System.out.println();
    }

    private static void demonstrateStringReaderWriter() throws Exception {
        System.out.println("--- StringReader/StringWriter ---");

        // StringWriter
        StringWriter sw = new StringWriter();
        sw.write("Hello ");
        sw.write("World\n");
        sw.append("Appended");
        String result = sw.toString();
        System.out.println("StringWriter result:");
        System.out.println(result);

        // StringReader
        String text = "Hello\nWorld";
        StringReader sr = new StringReader(text);
        BufferedReader br = new BufferedReader(sr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("Read: " + line);
        }

        System.out.println();
    }
}
