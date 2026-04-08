package com.ssitao.code.jdk.phase09.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * TCP客户端示例
 */
public class TCPClient {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("=== TCP Client Demo ===\n");

        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("连接服务器成功");

            // 创建IO流
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);

            Scanner scanner = new Scanner(System.in);

            // 创建线程读取服务器消息
            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println("服务器: " + message);
                        if ("bye".equals(message)) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

            // 主线程发送消息
            String sendMessage;
            while (!(sendMessage = scanner.nextLine()).equals("bye")) {
                writer.println(sendMessage);
            }
            writer.println("bye");

            // 等待读取线程结束
            readerThread.join();

            // 关闭
            scanner.close();
            reader.close();
            writer.close();

            System.out.println("连接关闭");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
