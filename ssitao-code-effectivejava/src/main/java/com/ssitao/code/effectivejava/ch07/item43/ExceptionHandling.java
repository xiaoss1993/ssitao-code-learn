package com.ssitao.code.effectivejava.ch07.item43;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * 条目43：抛出与抽象层次相符的异常
 *
 * 异常翻译：将低层异常转换为高层异常
 * 异常链：保留原始异常原因
 *
 * 原则：不要泄露实现细节给调用者
 */
public class ExceptionHandling {
    public static void main(String[] args) {
        System.out.println("=== 异常翻译 ===\n");

        // 错误：泄露实现细节
        try {
            processBad();
        } catch (NullPointerException e) {
            // 调用者不应该知道内部实现！
            throw new RuntimeException("处理失败", e);
        }

        // 正确：翻译为抽象层次的异常
        try {
            processGood();
        } catch (IOException e) {
            // 翻译为高层异常
            throw new ServiceException("无法处理请求", e);
        }

        System.out.println("\n=== Try-With-Resources ===\n");

        // 手动资源管理（冗长）
        try {
            manualResourceManagement();
        } catch (IOException e) {
            System.out.println("已处理: " + e.getMessage());
        }

        // TWR方式（简洁）
        tryWithResources();

        System.out.println("\n=== Lambda中的异常处理 ===\n");

        // Lambda和受查异常
        Callable<Integer> task = () -> {
            // 这个可以编译
            return 42;
        };

        // 如果需要在Lambda中处理受查异常
        Callable<Integer> withHandling = () -> {
            try {
                return riskyOperation();
            } catch (IOException e) {
                throw new RuntimeException("失败", e);
            }
        };

        System.out.println("Lambda异常处理示例");
    }

    static void processBad() {
        throw new NullPointerException("泄露了实现细节");
    }

    static void processGood() throws IOException {
        throw new IOException("低级IO错误");
    }

    static void manualResourceManagement() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("nonexistent.txt"));
            br.readLine();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    static void tryWithResources() {
        // Try-with-resources 自动关闭资源
        try (BufferedReader br = new BufferedReader(
                new FileReader("nonexistent.txt"))) {
            br.readLine();
        } catch (IOException e) {
            System.out.println("捕获异常: " + e.getMessage());
        }

        // 多个资源
        try (InputStream in = new FileInputStream("input.txt");
             OutputStream out = new FileOutputStream("output.txt")) {
            // 处理文件
        } catch (IOException e) {
            System.out.println("多资源: " + e.getMessage());
        }
    }

    static int riskyOperation() throws IOException {
        return 42;
    }

    static class ServiceException extends RuntimeException {
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
