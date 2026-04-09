package com.learn.crazyjava.lesson12_network;

import java.io.*;
import java.net.*;

/**
 * 第12课：网络编程 - TCP服务器
 */
public class TCPServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("服务器启动，监听端口8888...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端连接：" + clientSocket.getRemoteSocketAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(
                socket.getOutputStream(), true)) {

            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println("收到：" + request);
                writer.println("服务器收到：" + request);
                if ("bye".equals(request)) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
