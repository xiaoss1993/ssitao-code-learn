package com.ssitao.code.jdk.phase09.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO Echo服务器示例
 */
public class NIOEchoServer {
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        System.out.println("=== NIO Echo Server Demo ===\n");

        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            // 绑定端口
            serverChannel.socket().bind(new InetSocketAddress(PORT));

            // 设置非阻塞
            serverChannel.configureBlocking(false);

            // 注册到Selector
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动，监听端口 " + PORT + "...");

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                // 阻塞等待就绪的Channel
                selector.select();

                // 获取所有就绪的SelectionKey
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        // 处理连接
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println("客户端连接: " + client);

                    } else if (key.isReadable()) {
                        // 处理读取
                        SocketChannel client = (SocketChannel) key.channel();
                        buffer.clear();
                        int bytesRead = client.read(buffer);

                        if (bytesRead > 0) {
                            buffer.flip();
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);
                            String message = new String(data);
                            System.out.println("收到: " + message);

                            // 发送Echo响应
                            buffer.clear();
                            buffer.put(("Echo: " + message).getBytes());
                            buffer.flip();
                            client.write(buffer);

                            if ("bye".equals(message)) {
                                client.close();
                                System.out.println("客户端关闭");
                            }
                        } else if (bytesRead == -1) {
                            // 客户端关闭
                            client.close();
                            System.out.println("客户端关闭");
                        }
                    }

                    keyIterator.remove();
                }

                // 检查是否有客户端断开
                if (selector.keys().isEmpty()) {
                    break;
                }
            }

            System.out.println("服务器关闭");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
