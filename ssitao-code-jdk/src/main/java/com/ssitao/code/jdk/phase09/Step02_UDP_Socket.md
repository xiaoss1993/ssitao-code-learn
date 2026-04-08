# 步骤2：UDP Socket - 无连接的通信

---

## 2.1 UDP协议概述

### 2.1.1 UDP特点

```
UDP (User Datagram Protocol) 用户数据报协议

特点：
- 无连接（不需要建立连接）
- 不可靠传输（不保证送达、不保证顺序）
- 数据报（有消息边界）
- 高效低延迟

适用场景：
- DNS查询
- 视频流/音频流
- 实时游戏
- 广播/多播
- 允许少量丢包的应用
```

### 2.1.2 TCP vs UDP

| 特性 | TCP | UDP |
|------|-----|-----|
| 连接 | 面向连接 | 无连接 |
| 可靠性 | 可靠 | 不可靠 |
| 传输单位 | 字节流 | 数据报 |
| 消息边界 | 无 | 有 |
| 速度 | 较慢 | 较快 |
| 资源 | 较多 | 较少 |

---

## 2.2 DatagramSocket

### 2.2.1 创建UDP Socket

```java
// 创建Socket，绑定端口
DatagramSocket socket = new DatagramSocket(8080);

// 系统分配可用端口
DatagramSocket socket = new DatagramSocket(0);
int port = socket.getLocalPort();

// 连接指定地址（可选）
socket.connect(InetAddress.getByName("localhost"), 8080);

// 断开连接
socket.disconnect();
```

### 2.2.2 发送数据

```java
DatagramSocket socket = new DatagramSocket();

// 创建数据
byte[] data = "Hello".getBytes();

// 创建数据包（包含数据、长度、目标地址、端口）
InetAddress address = InetAddress.getByName("localhost");
DatagramPacket packet = new DatagramPacket(data, data.length, address, 8080);

// 发送
socket.send(packet);

// 关闭
socket.close();
```

### 2.2.3 接收数据

```java
DatagramSocket socket = new DatagramSocket(8080);

// 创建接收缓冲区
byte[] buffer = new byte[1024];
DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

// 接收数据（阻塞）
socket.receive(packet);

// 获取数据
byte[] receivedData = packet.getData();
int length = packet.getLength();
InetAddress senderAddress = packet.getAddress();
int senderPort = packet.getPort();

String message = new String(receivedData, 0, length);
System.out.println("收到: " + message + " from " + senderAddress + ":" + senderPort);

// 关闭
socket.close();
```

---

## 2.3 完整示例

### 2.3.1 UDP服务端

```java
public class UDPServer {
    public static void main(String[] args) throws IOException {
        // 创建Socket，绑定端口
        DatagramSocket socket = new DatagramSocket(8080);
        System.out.println("UDP服务器启动，监听端口8080...");

        byte[] buffer = new byte[1024];

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

        socket.close();
        System.out.println("服务器关闭");
    }
}
```

### 2.3.2 UDP客户端

```java
public class UDPClient {
    public static void main(String[] args) throws IOException {
        // 创建Socket
        DatagramSocket socket = new DatagramSocket();

        // 服务器地址
        InetAddress serverAddress = InetAddress.getByName("localhost");
        int serverPort = 8080;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("请输入消息: ");
            String message = scanner.nextLine();

            // 发送数据
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                data, data.length, serverAddress, serverPort);
            socket.send(packet);

            if ("bye".equals(message)) {
                break;
            }

            // 接收响应
            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(responsePacket);  // 阻塞

            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("服务器响应: " + response);
        }

        scanner.close();
        socket.close();
    }
}
```

---

## 2.4 DatagramPacket

### 2.4.1 创建数据包

```java
// 发送数据包
byte[] data = "Hello".getBytes();
DatagramPacket packet = new DatagramPacket(
    data,                    // 数据
    data.length,             // 长度
    InetAddress.getByName("localhost"),  // 目标地址
    8080                     // 目标端口
);

// 接收数据包（只需要缓冲区）
byte[] buffer = new byte[1024];
DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
```

### 2.4.2 数据包方法

```java
// 发送数据包的方法
packet.getData();           // 获取数据字节数组
packet.getLength();         // 获取实际数据长度
packet.getAddress();        // 获取发送/接收方地址
packet.getPort();           // 获取发送/接收方端口

// 设置数据包（用于接收）
packet.setData(buffer);
packet.setLength(buffer.length);
```

---

## 2.5 UDP广播

### 2.5.1 广播地址

```java
// IPv4广播地址：255.255.255.255
// 或者指定网段的广播地址，如 192.168.1.255

InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
```

### 2.5.2 广播示例

```java
public class UDPBroadcast {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        // 设置广播选项
        socket.setBroadcast(true);

        // 广播消息
        String message = "Hello Broadcast";
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(
            data, data.length,
            InetAddress.getByName("255.255.255.255"),
            8888
        );

        socket.send(packet);
        System.out.println("广播消息已发送");

        socket.close();
    }
}
```

---

## 2.6 UDP注意事项

### 2.6.1 数据大小限制

```java
// UDP数据报最大为65,535字节
// 其中IP头占20字节，UDP头占8字节
// 所以实际数据最大为65,507字节

// 如果数据过大，需要分包发送
byte[] largeData = getLargeData();
int maxSize = 65507;  // 最大数据大小

for (int offset = 0; offset < largeData.length; offset += maxSize) {
    int length = Math.min(maxSize, largeData.length - offset);
    DatagramPacket packet = new DatagramPacket(
        largeData, offset, length,
        address, port
    );
    socket.send(packet);
}
```

### 2.6.2 超时设置

```java
DatagramSocket socket = new DatagramSocket(8080);

// 设置超时（避免receive()永久阻塞
socket.setSoTimeout(5000);  // 5秒超时

try {
    socket.receive(packet);
} catch (SocketTimeoutException e) {
    System.out.println("接收超时");
}
```

---

## 2.7 练习题

```java
// 1. 实现一个UDP ping程序，测量网络延迟

// 2. 说明UDP为什么不适合传输大文件

// 3. 实现一个简单的UDP文件传输（分包发送）

// 4. TCP和UDP各有什么优缺点？

// 5. 实现一个UDPdiscovery服务，客户端发送广播查找服务器
```

---

## 2.8 参考答案

```java
// 1. UDP Ping
public class UDPPing {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(3000);

        InetAddress address = InetAddress.getByName("localhost");
        int port = 8080;

        for (int i = 1; i <= 4; i++) {
            String message = "Ping " + i + " " + System.currentTimeMillis();
            long sendTime = System.currentTimeMillis();

            // 发送
            byte[] data = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
            socket.send(sendPacket);

            // 接收
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(receivePacket);
                long receiveTime = System.currentTimeMillis();
                System.out.println(message + " - RTT: " + (receiveTime - sendTime) + "ms");
            } catch (SocketTimeoutException e) {
                System.out.println(message + " - 超时");
            }

            Thread.sleep(1000);
        }

        socket.close();
    }
}

// 2. UDP不适合传大文件的原因
// - 无连接，丢包后无法重传
// - 无序，需要应用层处理排序
// - 数据报大小有限制

// 4. TCP vs UDP优缺点
// TCP: 可靠但慢，适合重要数据传输
// UDP: 快但不可靠，适合实时性要求高的场景

// 5. UDP Discovery
// 服务器监听特定端口，收到广播后返回自己的地址
```

---

[返回目录](./README.md) | [下一步：NIO网络编程](./Step03_NIO_Network.md)
