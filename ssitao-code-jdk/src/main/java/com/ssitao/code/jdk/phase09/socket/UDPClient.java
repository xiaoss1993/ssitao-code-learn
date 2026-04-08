package com.ssitao.code.jdk.phase09.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * UDP客户端示例
 */
public class UDPClient {
    private static final String HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        System.out.println("=== UDP Client Demo ===\n");

        try (DatagramSocket socket = new DatagramSocket()) {
            // 服务器地址
            InetAddress serverAddress = InetAddress.getByName(HOST);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("请输入消息: ");
                String message = scanner.nextLine();

                // 发送数据
                byte[] data = message.getBytes();
                DatagramPacket packet = new DatagramPacket(
                    data, data.length, serverAddress, SERVER_PORT);
                socket.send(packet);

                if ("bye".equals(message)) {
                    break;
                }

                // 接收响应（带超时）
                socket.setSoTimeout(5000);
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(responsePacket);

                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    System.out.println("服务器响应: " + response);
                } catch (java.net.SocketTimeoutException e) {
                    System.out.println("接收超时");
                }
            }

            scanner.close();
            System.out.println("连接关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
