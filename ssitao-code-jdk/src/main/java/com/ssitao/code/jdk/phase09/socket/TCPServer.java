package com.ssitao.code.jdk.phase09.socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * TCP服务端示例
 */
public class TCPServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("=== TCP Server Demo ===\n");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("服务器启动，监听端口 " + PORT + "...");

            // 等待客户端连接
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端连接: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            // 创建IO流
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()), true);

            Scanner scanner = new Scanner(System.in);

            // 创建线程读取客户端消息
            Thread readerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println("客户端: " + message);
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
            clientSocket.close();

            System.out.println("服务器关闭");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
