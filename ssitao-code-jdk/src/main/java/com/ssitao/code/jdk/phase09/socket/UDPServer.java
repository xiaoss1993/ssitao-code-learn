package com.ssitao.code.jdk.phase09.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP服务端示例
 */
public class UDPServer {
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        System.out.println("=== UDP Server Demo ===\n");

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP服务器启动，监听端口 " + PORT + "...");

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                // 创建接收数据包
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // 接收数据（阻塞）
                socket.receive(packet);

                // 处理数据
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("收到客户端[" + packet.getAddress() + ":" +
                                 packet.getPort() + "]: " + message);

                // 发送响应
                String response = "服务器收到: " + message;
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                    responseData, responseData.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);

                if ("bye".equals(message)) {
                    break;
                }
            }

            System.out.println("服务器关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
