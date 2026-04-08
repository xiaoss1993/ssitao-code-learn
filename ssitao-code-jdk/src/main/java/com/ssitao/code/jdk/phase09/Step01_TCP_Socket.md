# 步骤1：TCP Socket - 面向连接的通信

---

## 1.1 TCP协议概述

### 1.1.1 TCP特点

```
TCP (Transmission Control Protocol) 传输控制协议

特点：
- 面向连接（三次握手建立连接）
- 可靠传输（确认、重传、排序）
- 字节流（无消息边界）
- 全双工通信

适用场景：
- HTTP/HTTPS
- FTP
- SMTP/POP3
- SSH
- 任何需要可靠传输的应用
```

### 1.1.2 TCP通信模型

```
Server端:
1. 创建ServerSocket，绑定端口
2. 调用accept()等待客户端连接
3. 获取Socket，进行通信
4. 关闭连接

Client端:
1. 创建Socket，指定服务器地址和端口
2. 获取输入/输出流
3. 进行通信
4. 关闭连接
```

---

## 1.2 ServerSocket

### 1.2.1 创建服务端

```java
// 创建ServerSocket，绑定端口
ServerSocket serverSocket = new ServerSocket(8080);

// 等待客户端连接（阻塞）
Socket clientSocket = serverSocket.accept();

// 获取客户端信息
InetAddress clientAddress = clientSocket.getInetAddress();
int clientPort = clientSocket.getPort();

System.out.println("客户端连接: " + clientAddress + ":" + clientPort);

// 通信
// ...

// 关闭
clientSocket.close();
serverSocket.close();
```

### 1.2.2 ServerSocket构造方法

```java
// 绑定指定端口
ServerSocket socket = new ServerSocket(8080);

// 绑定指定端口，设置连接队列长度
ServerSocket socket = new ServerSocket(8080, 50);

// 绑定指定端口，IP自动分配
ServerSocket socket = new ServerSocket(0);  // 系统分配可用端口
int port = socket.getLocalPort();  // 获取分配的端口
```

---

## 1.3 Socket

### 1.3.1 创建客户端Socket

```java
// 连接指定服务器
Socket socket = new Socket("localhost", 8080);

// 或指定IP地址
Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 8080);

// 设置连接超时
Socket socket = new Socket();
socket.connect(new InetSocketAddress("localhost", 8080), 3000);

// 设置读取超时
socket.setSoTimeout(5000);
```

### 1.3.2 Socket常用方法

```java
// 获取输入/输出流
InputStream in = socket.getInputStream();
OutputStream out = socket.getOutputStream();

// 获取本地/远程地址信息
InetAddress localAddress = socket.getLocalAddress();
int localPort = socket.getLocalPort();
InetAddress remoteAddress = socket.getInetAddress();
int remotePort = socket.getPort();

// 设置选项
socket.setSoTimeout(5000);        // 读取超时
socket.setKeepAlive(true);        // TCP保活
socket.setTcpNoDelay(true);        // 禁用Nagle算法
socket.setReuseAddress(true);      // 地址复用
socket.setReceiveBufferSize(8192); // 接收缓冲区
socket.setSendBufferSize(8192);    // 发送缓冲区
```

---

## 1.4 数据读写

### 1.4.1 使用字节流

```java
// 服务端
ServerSocket serverSocket = new ServerSocket(8080);
Socket clientSocket = serverSocket.accept();

BufferedReader reader = new BufferedReader(
    new InputStreamReader(clientSocket.getInputStream()));
PrintWriter writer = new PrintWriter(
    new OutputStreamWriter(clientSocket.getOutputStream()), true);

// 读取一行
String line = reader.readLine();

// 写入一行
writer.println("Hello from server");

// 关闭
reader.close();
writer.close();
clientSocket.close();
serverSocket.close();

// 客户端
Socket socket = new Socket("localhost", 8080);

BufferedReader reader = new BufferedReader(
    new InputStreamReader(socket.getInputStream()));
PrintWriter writer = new PrintWriter(
    new OutputStreamWriter(socket.getOutputStream()), true);

// 发送数据
writer.println("Hello from client");

// 接收响应
String response = reader.readLine();

// 关闭
reader.close();
writer.close();
socket.close();
```

### 1.4.2 使用字符流（解决乱码）

```java
// 服务端 - 使用UTF-8编码
ServerSocket serverSocket = new ServerSocket(8080);
Socket clientSocket = serverSocket.accept();

BufferedReader reader = new BufferedReader(
    new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
PrintWriter writer = new PrintWriter(
    new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);

// 客户端同样指定编码
Socket socket = new Socket("localhost", 8080);
BufferedReader reader = new BufferedReader(
    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
PrintWriter writer = new PrintWriter(
    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
```

---

## 1.5 完整示例

### 1.5.1 简单聊天服务端

```java
public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器启动，监听端口8080...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("客户端连接: " + clientSocket.getInetAddress());

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

        // 关闭
        scanner.close();
        reader.close();
        writer.close();
        clientSocket.close();
        serverSocket.close();
    }
}
```

### 1.5.2 简单聊天客户端

```java
public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);
        System.out.println("连接服务器成功");

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

        // 关闭
        scanner.close();
        reader.close();
        writer.close();
        socket.close();
    }
}
```

---

## 1.6 多客户端处理

### 1.6.1 线程池处理多客户端

```java
public class MultiThreadServer {
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器启动，监听端口8080...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端连接: " + clientSocket.getInetAddress());

            // 为每个客户端分配一个线程处理
            pool.execute(() -> handleClient(clientSocket));
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()), true);

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("收到: " + message);
                writer.println("服务器收到: " + message);

                if ("bye".equals(message)) {
                    break;
                }
            }

            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 1.6.2 多客户端消息广播

```java
public class ChatServerWithBroadcast {
    private static List<PrintWriter> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()), true);
            clients.add(writer);

            Thread clientThread = new Thread(() -> handleClient(clientSocket, writer));
            clientThread.start();
        }
    }

    private static void handleClient(Socket clientSocket, PrintWriter writer) {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = reader.readLine()) != null) {
                broadcast(message);
                if ("bye".equals(message)) {
                    break;
                }
            }

            clients.remove(writer);
            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message) {
        for (PrintWriter writer : clients) {
            writer.println(message);
        }
    }
}
```

---

## 1.7 练习题

```java
// 1. 实现一个TCP文件传输服务器和客户端

// 2. 实现一个带用户名的聊天室

// 3. 说明TCP三次握手的过程

// 4. Socket的close()和shutdownOutput()有什么区别？

// 5. 实现一个Echo服务器，回显客户端发送的所有消息
```

---

## 1.8 参考答案

```java
// 1. 文件传输（服务端）
public class FileServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();

        // 接收文件名
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        String fileName = reader.readLine();

        // 发送文件内容
        FileInputStream fis = new FileInputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(
            clientSocket.getOutputStream());

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.flush();
        fis.close();
        bos.close();
        clientSocket.close();
        serverSocket.close();
    }
}

// 2. 带用户名的聊天室需要维护 用户名->PrintWriter 的映射

// 3. TCP三次握手
// 第一次: 客户端发送SYN包，请求建立连接
// 第二次: 服务器返回ACK+SYN，确认并请求建立连接
// 第三次: 客户端返回ACK，确认连接建立

// 4. close() vs shutdownOutput()
// close(): 关闭整个Socket连接
// shutdownOutput(): 只关闭输出流，Socket仍可用

// 5. Echo服务器
public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket = serverSocket.accept();

        BufferedReader reader = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(
            new OutputStreamWriter(clientSocket.getOutputStream()), true);

        String message;
        while ((message = reader.readLine()) != null) {
            writer.println("Echo: " + message);
        }

        reader.close();
        writer.close();
        clientSocket.close();
        serverSocket.close();
    }
}
```

---

[返回目录](./README.md) | [下一步：UDP Socket](./Step02_UDP_Socket.md)
