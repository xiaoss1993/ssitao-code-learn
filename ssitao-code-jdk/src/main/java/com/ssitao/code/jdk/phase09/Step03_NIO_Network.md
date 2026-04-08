# 步骤3：NIO网络编程 - 非阻塞IO

---

## 3.1 NIO网络概述

### 3.1.1 BIO vs NIO

```
BIO (Blocking IO) 阻塞IO
- 传统Socket编程
- accept()和read()都是阻塞的
- 每个连接需要一个线程

NIO (Non-blocking IO) 非阻塞IO
- Selector监控多个Channel
- 非阻塞模式
- 单线程可以处理多个连接
- 适合高并发场景
```

### 3.1.2 NIO网络核心组件

```
Selector      - 选择器，监控多个Channel的IO状态
Channel      - 通道，类似Socket
Buffer       - 缓冲区，数据读写的中转站
SelectionKey - 选择键，标识Channel在Selector上的注册状态
```

---

## 3.2 ServerSocketChannel

### 3.2.1 创建服务端

```java
// 打开Channel
ServerSocketChannel serverChannel = ServerSocketChannel.open();

// 绑定端口
serverChannel.socket().bind(new InetSocketAddress(8080));

// 设置为非阻塞模式
serverChannel.configureBlocking(false);

// 获取ServerSocket
ServerSocket serverSocket = serverChannel.socket();
```

### 3.2.2 接受连接

```java
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.socket().bind(new InetSocketAddress(8080));
serverChannel.configureBlocking(false);  // 非阻塞

// 在循环中接受连接
while (true) {
    SocketChannel clientChannel = serverChannel.accept();

    if (clientChannel != null) {
        // 有客户端连接
        clientChannel.configureBlocking(false);
        // 注册到Selector
        clientChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("客户端连接: " + clientChannel);
    } else {
        // 没有连接，处理其他事情
    }
}
```

---

## 3.3 SocketChannel

### 3.3.1 连接服务器

```java
// 创建Channel
SocketChannel clientChannel = SocketChannel.open();

// 设置为非阻塞
clientChannel.configureBlocking(false);

// 连接服务器（非阻塞模式下可能立即返回）
clientChannel.connect(new InetSocketAddress("localhost", 8080));

// 完成连接（如果是阻塞模式，会自动完成）
if (!clientChannel.isConnected()) {
    // 连接尚未完成，可以做其他事情
    while (!clientChannel.finishConnect()) {
        // 等待连接完成
    }
}
```

### 3.3.2 读写数据

```java
// 读取数据
ByteBuffer buffer = ByteBuffer.allocate(1024);
int bytesRead = clientChannel.read(buffer);

// 写入数据
ByteBuffer buffer = ByteBuffer.allocate(1024);
buffer.put("Hello".getBytes());
buffer.flip();
while (buffer.hasRemaining()) {
    clientChannel.write(buffer);
}
```

### 3.3.3 关闭Channel

```java
// 关闭Channel
clientChannel.close();

// 或先关闭输出，再关闭输入
clientChannel.shutdownOutput();
clientChannel.shutdownInput();
clientChannel.close();
```

---

## 3.4 Selector

### 3.4.1 创建Selector

```java
// 创建Selector
Selector selector = Selector.open();

// 注册Channel到Selector
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.socket().bind(new InetSocketAddress(8080));
serverChannel.configureBlocking(false);

// 注册到Selector
// OP_ACCEPT - 接受连接
// OP_READ   - 读就绪
// OP_WRITE  - 写就绪
// OP_CONNECT - 连接就绪
serverChannel.register(selector, SelectionKey.OP_ACCEPT);
```

### 3.4.2 监听就绪事件

```java
while (true) {
    // 阻塞，直到有Channel就绪
    int readyChannels = selector.select();

    if (readyChannels == 0) {
        continue;
    }

    // 获取所有就绪的SelectionKey
    Set<SelectionKey> selectedKeys = selector.selectedKeys();
    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

    while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();

        if (key.isAcceptable()) {
            // 有新的连接 accept
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);

        } else if (key.isReadable()) {
            // 有数据可读 read
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = client.read(buffer);
            if (bytesRead > 0) {
                // 处理数据
            }

        } else if (key.isWritable()) {
            // 可以写数据 write
            // ...
        }

        // 处理完后移除
        keyIterator.remove();
    }
}
```

---

## 3.5 完整示例

### 3.5.1 NIO服务端

```java
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建Selector
        Selector selector = Selector.open();

        // 创建ServerSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false);

        // 注册到Selector
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动，监听端口8080...");

        ByteBuffer buffer = ByteBuffer.allocate(1024);

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

                        // 发送响应
                        buffer.clear();
                        buffer.put(("服务器收到: " + message).getBytes());
                        buffer.flip();
                        client.write(buffer);
                    } else if (bytesRead == -1) {
                        // 客户端关闭
                        client.close();
                        System.out.println("客户端关闭");
                    }
                }

                keyIterator.remove();
            }
        }
    }
}
```

### 3.5.2 NIO客户端

```java
public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 创建Channel
        SocketChannel clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);

        // 连接服务器
        clientChannel.connect(new InetSocketAddress("localhost", 8080));

        // 等待连接完成
        while (!clientChannel.finishConnect()) {
            Thread.sleep(100);
        }

        System.out.println("连接服务器成功");

        // 发送数据
        Scanner scanner = new Scanner(System.in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            System.out.print("请输入消息: ");
            String message = scanner.nextLine();

            // 发送
            buffer.clear();
            buffer.put(message.getBytes());
            buffer.flip();
            clientChannel.write(buffer);

            if ("bye".equals(message)) {
                break;
            }

            // 接收响应
            buffer.clear();
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                System.out.println("服务器响应: " + new String(data));
            }
        }

        scanner.close();
        clientChannel.close();
    }
}
```

---

## 3.6 SelectionKey

### 3.6.1 SelectionKey操作

```java
// 获取关联的Channel
SelectableChannel channel = key.channel();

// 获取关联的Selector
Selector selector = key.selector();

// 判断就绪状态
key.isAcceptable();   // OP_ACCEPT
key.isReadable();     // OP_READ
key.isWritable();     // OP_WRITE
key.isConnectable();  // OP_CONNECT

// 取消注册
key.cancel();

// 关闭Channel时自动取消
```

### 3.6.2 附加对象

```java
// 附加对象到Key
key.attach(new Object());
Object obj = key.attachment();

// 或注册时附加
clientChannel.register(selector, SelectionKey.OP_READ, attachment);
```

---

## 3.7 NIO vs BIO对比

### 3.7.1 连接模型对比

```java
// BIO: 每个连接一个线程
ServerSocket serverSocket = new ServerSocket(8080);
while (true) {
    Socket clientSocket = serverSocket.accept();  // 阻塞
    new Thread(() -> handleClient(clientSocket)).start();
}

// NIO: 单线程处理多个连接
Selector selector = Selector.open();
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select();  // 阻塞，直到有就绪的Channel
    Set<SelectionKey> keys = selector.selectedKeys();
    for (SelectionKey key : keys) {
        if (key.isAcceptable()) {
            // 处理连接
        } else if (key.isReadable()) {
            // 处理读取
        }
    }
}
```

### 3.7.2 性能对比

| 场景 | BIO | NIO |
|------|-----|-----|
| 少量连接 | 简单高效 | 复杂但可管理 |
| 大量连接 | 线程资源消耗大 | 高效利用 |
| 代码复杂度 | 简单 | 较复杂 |
| 适用场景 | 低并发 | 高并发 |

---

## 3.8 练习题

```java
// 1. 使用NIO实现一个Echo服务器

// 2. 说明Selector的select()和selectNow()的区别

// 3. NIO为什么比BIO更适合高并发场景

// 4. 使用NIO实现群聊功能

// 5. SelectionKey的attach()方法有什么用途
```

---

## 3.9 参考答案

```java
// 1. NIO Echo服务器
// 参考上面的NIOServer示例，响应时改为 echo "服务器收到: " + message

// 2. select() vs selectNow()
// select(): 阻塞直到有Channel就绪
// selectNow(): 立即返回，不阻塞

// 3. NIO优势
// - 非阻塞IO，不会永久阻塞
// - 单线程处理多个连接
// - 减少线程上下文切换
// - 降低内存消耗

// 5. attach()用途
// 可以附加任意对象到SelectionKey，如：
// - 客户端身份信息
// - 未完成的处理状态
// - Buffer缓冲区
```

---

[返回目录](./README.md)

## 第九阶段总结

### 核心知识点

| 主题 | 核心类 | 特点 |
|------|--------|------|
| TCP Socket | Socket, ServerSocket | 面向连接，可靠传输 |
| UDP Socket | DatagramSocket, DatagramPacket | 无连接，高效低延迟 |
| NIO网络 | Selector, SocketChannel | 非阻塞，多路复用 |

### TCP vs UDP

| 特性 | TCP | UDP |
|------|-----|-----|
| 连接 | 面向连接 | 无连接 |
| 可靠性 | 可靠 | 不可靠 |
| 速度 | 较慢 | 较快 |
| 应用 | HTTP, FTP | DNS, 视频流 |
