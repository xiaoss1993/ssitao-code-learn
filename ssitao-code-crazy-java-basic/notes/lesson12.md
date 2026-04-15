# 第12课：网络编程

## 核心概念

### 12.1 网络分层
- 应用层：HTTP、FTP、SMTP
- 传输层：TCP、UDP
- 网络层：IP
- 链路层：MAC地址

### 12.2 TCP vs UDP
| 特性 | TCP | UDP |
|------|-----|-----|
| 连接 | 面向连接 | 无连接 |
| 可靠性 | 可靠 | 不可靠 |
| 速度 | 较慢 | 较快 |
| 场景 | 文件传输、HTTP | 视频、语音 |

### 12.3 Socket
- Socket是网络通信的端点
- ServerSocket用于服务器监听
- Socket用于客户端连接

## 代码示例

### 示例1：TCP服务端
```java
import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        // 创建服务端Socket，监听端口
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("服务器启动，监听端口8888...");

            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("客户端连接：" + clientSocket.getRemoteSocketAddress());

                // 处理客户端请求
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

                // 响应
                String response = "服务器收到：" + request;
                writer.println(response);

                if ("bye".equals(request)) {
                    break;
                }
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
```

### 示例2：TCP客户端
```java
import java.io.*;
import java.net.*;

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

            // 发送消息
            String msg;
            while (true) {
                System.out.print("发送：");
                msg = console.readLine();
                writer.println(msg);

                // 接收响应
                String response = reader.readLine();
                System.out.println("收到：" + response);

                if ("bye".equals(msg)) {
                    break;
                }
            }
        }
    }
}
```

### 示例3：UDP服务端
```java
import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        // 创建UDP Socket
        DatagramSocket socket = new DatagramSocket(8888);
        System.out.println("UDP服务器启动，监听端口8888...");

        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            // 接收数据
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength());
            System.out.println("收到：" + msg + "，来自：" + packet.getAddress());

            // 发送响应
            String response = "服务器收到：" + msg;
            DatagramPacket responsePacket = new DatagramPacket(
                response.getBytes(),
                response.getBytes().length,
                packet.getAddress(),
                packet.getPort()
            );
            socket.send(responsePacket);
        }
    }
}
```

### 示例4：UDP客户端
```java
import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("localhost");
        int port = 8888;

        // 发送数据
        String msg = "Hello UDP";
        DatagramPacket packet = new DatagramPacket(
            msg.getBytes(),
            msg.getBytes().length,
            address,
            port
        );
        socket.send(packet);
        System.out.println("发送：" + msg);

        // 接收响应
        byte[] buffer = new byte[1024];
        DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(responsePacket);
        String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
        System.out.println("收到：" + response);

        socket.close();
    }
}
```

### 示例5：多客户端聊天服务器
```java
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    // 保存所有客户端连接
    private static Set<PrintWriter> clients =
        Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("聊天服务器启动...");

        ExecutorService pool = Executors.newFixedThreadPool(10);

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("用户加入：" + socket.getRemoteSocketAddress());

            pool.execute(() -> handleClient(socket));
        }
    }

    private static void handleClient(Socket socket) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
            clients.add(writer);

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

            String msg;
            while ((msg = reader.readLine()) != null) {
                broadcast(socket.getRemoteSocketAddress() + ": " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(writer);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcast(String msg) {
        for (PrintWriter client : clients) {
            client.println(msg);
        }
    }
}
```

### 示例6：URL下载
```java
import java.io.*;
import java.net.*;

public class URLDemo {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.example.com");
        URLConnection conn = url.openConnection();

        try (InputStream in = conn.getInputStream();
             BufferedReader reader = new BufferedReader(
                new InputStreamReader(in))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
```

## 常见面试题

1. **TCP三次握手？**
   - 客户端发送SYN
   - 服务器发送SYN+ACK
   - 客户端发送ACK

2. **Socket和ServerSocket的区别？**
   - ServerSocket用于服务器监听
   - Socket用于客户端连接

3. **什么是长连接和短连接？**
   - 短连接：每次请求建立连接，请求后关闭
   - 长连接：建立连接后复用，如HTTP Keep-Alive

## 练习题

1. 实现一个简单的HTTP服务器
2. 实现文件传输（TCP）
3. 实现广播消息功能

## 要点总结

- TCP面向连接，可靠传输
- UDP无连接，快速但不可靠
- Socket是通信端点
- ServerSocket监听端口
- 多线程处理多客户端
