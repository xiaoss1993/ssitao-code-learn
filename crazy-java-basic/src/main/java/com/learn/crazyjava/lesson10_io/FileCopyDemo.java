package com.learn.crazyjava.lesson10_io;

import java.io.*;

/**
 * 第10课：I/O流 - 文件复制
 */
public class FileCopyDemo {
    public static void main(String[] args) throws IOException {
        String source = "source.txt";
        String target = "target.txt";

        // 使用缓冲流提高效率
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(source));
             BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target))) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            System.out.println("文件复制完成");
        }
    }
}
