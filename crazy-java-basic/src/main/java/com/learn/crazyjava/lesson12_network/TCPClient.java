package com.learn.crazyjava.lesson12_network;

import java.io.*;
import java.net.*;

/**
 * 第12课：网络编程 - TCP客户端
 */
public class TCPClient {
    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", 8888);
             BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(
                socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(
                new InputStreamReader(System.in))) {

            System.out.println("已连接服务器");

            String msg;
            while (true) {
                System.out.print("发送：");
                msg = console.readLine();
                writer.println(msg);

                String response = reader.readLine();
                System.out.println("收到：" + response);

                if ("bye".equals(msg)) break;
            }
        }
    }
}
