# 步骤6：NIO - 新的IO模型

---

## 6.1 NIO概述

### 6.1.1 NIO vs 传统IO

```java
// 传统IO（IO）：
// - 面向流（Stream Oriented）
// - 阻塞IO（Blocking）
// - 一个线程处理一个连接

// NIO（New IO / Non-blocking IO）：
// - 面向缓冲区（Buffer Oriented）
// - 非阻塞IO（Non-blocking）
// - 选择器（Selector）实现多路复用
// - 一个线程处理多个连接

// 主要组件：
// - Buffer：缓冲区，数据读写的中转站
// - Channel：通道，类似双向流
// - Selector：选择器，监控多个Channel的IO状态
```

### 6.1.2 适用场景

```java
// 传统IO适用：
// - 简单文件读写
// - 小规模应用
// - 顺序读写

// NIO适用：
// - 高并发服务（如代理、负载均衡器）
// - 网络通信（聊天室、游戏服务器）
// - 需要处理大量连接的场景
// - 事件驱动架构
```

---

## 6.2 Buffer

### 6.2.1 Buffer概述

```java
// Buffer：缓冲区，存储数据的容器
// 本质是一个数组，加上一些位置和容量信息

// 核心属性：
// - capacity：容量，缓冲区大小
// - position：位置，下一个读写的位置
// - limit：限制，可读/可写的边界

// 关系：
// 0 <= position <= limit <= capacity
```

### 6.2.2 Buffer类型

```java
// Buffer实现类：
// - ByteBuffer
// - CharBuffer
// - ShortBuffer
// - IntBuffer
// - LongBuffer
// - FloatBuffer
// - DoubleBuffer
// - MappedByteBuffer

// 创建ByteBuffer
ByteBuffer buf1 = ByteBuffer.allocate(1024);        // 堆内存
ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);  // 直接内存（系统内存）
ByteBuffer buf3 = ByteBuffer.wrap(new byte[1024]);  // 包装已有数组
ByteBuffer buf4 = ByteBuffer.wrap("Hello".getBytes());  // 包装字符串

// 创建其他类型
IntBuffer intBuf = IntBuffer.allocate(100);
CharBuffer charBuf = CharBuffer.allocate(100);
```

### 6.2.3 Buffer操作

```java
// Buffer基本操作

ByteBuffer buf = ByteBuffer.allocate(10);

// 写入数据
buf.put((byte) 1);
buf.put((byte) 2);
buf.put((byte) 3);

// 切换为读模式
buf.flip();

// 读取数据
while (buf.hasRemaining()) {
    byte b = buf.get();
    System.out.println(b);
}

// 清空缓冲区（但数据仍在，可覆盖）
buf.clear();

// compact() - 压缩，保留未读数据
buf.compact();
```

### 6.2.4 flip和compact

```java
// flip：切换为读模式
// 写入后：position = 写入位置，limit = capacity
// flip后：limit = position，position = 0

ByteBuffer buf = ByteBuffer.allocate(10);
buf.put("Hello".getBytes());  // position = 5, limit = 10

buf.flip();  // position = 0, limit = 5

// compact：压缩缓冲区
// 将未读数据移到开头，position设为未读数据之后

ByteBuffer buf = ByteBuffer.allocate(10);
buf.put("HelloWorld".getBytes());  // 写入10字节

buf.flip();  // position = 0, limit = 10
buf.get(new byte[5]);  // 读取5字节，position = 5

buf.compact();  // 未读的"World"移到开头，position = 5
```

### 6.2.5 mark和reset

```java
// mark：标记当前位置
// reset：重置到标记位置

ByteBuffer buf = ByteBuffer.allocate(10);
buf.put("Hello".getBytes());

buf.flip();  // position = 0
buf.get(new byte[3]);  // 读取3字节，position = 3

buf.mark();  // 标记position = 3

buf.get(new byte[2]);  // 再读2字节，position = 5

buf.reset();  // position回到3

buf.clear();  // position = 0, limit = capacity
```

---

## 6.3 Channel

### 6.3.1 Channel概述

```java
// Channel：通道，类似双向流
// - 可以读也可以写
// - 通常与Buffer配合使用
// - 非阻塞模式支持选择器

// 主要Channel实现：
// - FileChannel：文件通道
// - SocketChannel：TCP客户端通道
// - ServerSocketChannel：TCP服务端通道
// - DatagramChannel：UDP通道
```

### 6.3.2 FileChannel

```java
// FileChannel：文件通道

// 获取FileChannel
RandomAccessFile raf = new RandomAccessFile("file.txt", "rw");
FileChannel channel = raf.getChannel();

// 或通过FileInputStream/FileOutputStream
FileInputStream fis = new FileInputStream("file.txt");
FileChannel channel = fis.getChannel();

// 读取
ByteBuffer buf = ByteBuffer.allocate(1024);
int bytesRead = channel.read(buf);

// 写入
ByteBuffer buf2 = ByteBuffer.allocate(1024);
buf2.put("Hello".getBytes());
buf2.flip();
channel.write(buf2);

// 关闭
channel.close();
raf.close();
```

### 6.3.3 文件复制示例

```java
// 使用FileChannel复制文件
public static void copyFile(String src, String dest) throws IOException {
    try (FileInputStream fis = new FileInputStream(src);
         FileOutputStream fos = new FileOutputStream(dest)) {

        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        // 方法1：transferTo（推荐）
        long bytesTransferred = 0;
        while (bytesTransferred < inChannel.size()) {
            bytesTransferred += inChannel.transferTo(
                bytesTransferred,
                inChannel.size() - bytesTransferred,
                outChannel
            );
        }

        // 方法2：手动读写
        /*
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        while (inChannel.read(buffer) != -1) {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
        */
    }
}
```

### 6.3.4 文件映射

```java
// 文件映射：将文件直接映射到内存
// MappedByteBuffer：操作内存像操作文件

// 将文件映射到内存
try (RandomAccessFile raf = new RandomAccessFile("largefile.dat", "r")) {
    FileChannel channel = raf.getChannel();

    // map模式：(FileChannel.MapMode.READ_ONLY, position, size)
    MappedByteBuffer buffer = channel.map(
        FileChannel.MapMode.READ_ONLY,  // 只读模式
        0,                              // 起始位置
        channel.size()                  // 映射大小
    );

    // 像操作普通ByteBuffer一样
    while (buffer.hasRemaining()) {
        System.out.print((char) buffer.get());
    }
}

// 修改映射的文件（JDK 6+）
try (RandomAccessFile raf = new RandomAccessFile("file.txt", "rw")) {
    FileChannel channel = raf.getChannel();
    MappedByteBuffer buffer = channel.map(
        FileChannel.MapMode.READ_WRITE,
        0,
        channel.size()
    );
    buffer.position(0);
    buffer.put("Modified".getBytes());
}
```

---

## 6.4 Selector

### 6.4.1 Selector概述

```java
// Selector：选择器
// - 监控多个Channel的IO事件
// - 单个线程可以处理多个连接
// - 适用于高并发场景

// 选择器模式：
// - 创建一个或多个Selector
// - 将Channel注册到Selector
// -  Selector监控各Channel的状态
// - 当某个Channel准备好时，处理它
```

### 6.4.2 使用Selector

```java
// 1. 创建Selector
Selector selector = Selector.open();

// 2. 将Channel设置为非阻塞
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.socket().bind(new InetSocketAddress(8080));
serverChannel.configureBlocking(false);  // 非阻塞

// 3. 注册Channel到Selector
// OP_ACCEPT: 接受连接
// OP_CONNECT: 连接建立
// OP_READ: 可读
// OP_WRITE: 可写
serverChannel.register(selector, SelectionKey.OP_ACCEPT);

// 4. 监听事件
while (true) {
    // 阻塞，直到有事件发生
    int readyChannels = selector.select();

    if (readyChannels == 0) continue;

    // 获取所有SelectionKey
    Set<SelectionKey> keys = selector.selectedKeys();
    Iterator<SelectionKey> keyIterator = keys.iterator();

    while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();

        if (key.isAcceptable()) {
            // 处理接受连接
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);

        } else if (key.isReadable()) {
            // 处理读事件
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            client.read(buffer);
            // 处理数据
        }

        // 处理完后移除
        keyIterator.remove();
    }
}
```

---

## 6.5 NIO vs IO对比

### 6.5.1 核心区别

| 特性 | 传统IO | NIO |
|------|--------|-----|
| 方向 | 单向流 | 双向Channel |
| 读写 | 基于流（Stream） | 基于缓冲区（Buffer） |
| 阻塞 | 阻塞 | 非阻塞/阻塞 |
| 线程模型 | 一个连接一个线程 | 一个线程处理多个连接 |
| 适用场景 | 简单IO | 高并发 |

### 6.5.2 代码对比

```java
// 传统IO - 阻塞模式
ServerSocket server = new ServerSocket(8080);
while (true) {
    Socket client = server.accept();  // 阻塞
    new Thread(() -> {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(client.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // 处理
            }
            client.close();
        } catch (IOException e) { }
    }).start();
}

// NIO - 非阻塞模式
ServerSocketChannel server = ServerSocketChannel.open();
server.bind(new InetSocketAddress(8080));
server.configureBlocking(false);
Selector selector = Selector.open();
server.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select();  // 阻塞直到有事件
    for (SelectionKey key : selector.selectedKeys()) {
        if (key.isAcceptable()) {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            SocketChannel client = (SocketChannel) key.channel();
            client.read(buffer);
        }
    }
}
```

---

## 6.6 Path和Files

### 6.6.1 Path（JDK 7+）

```java
// Path：文件系统路径

// 创建Path
Path p1 = Paths.get("file.txt");  // 相对路径
Path p2 = Paths.get("/home/user/file.txt");  // 绝对路径
Path p3 = FileSystems.getDefault().getPath("/home/user", "file.txt");

// 路径操作
Path path = Paths.get("/home/user/docs/file.txt");
path.getFileName();      // file.txt
path.getParent();        // /home/user/docs
path.getRoot();          // /
path.getNameCount();     // 4

// 路径拼接
Path combined = Paths.get("/home/user").resolve("docs/file.txt");

// 规范化
Path p = Paths.get("/home/user/../user/file.txt");
p.normalize();  // /home/user/file.txt

// 转换为File
File file = path.toFile();

// 转换为URI
URI uri = path.toUri();
```

### 6.6.2 Files工具类（JDK 7+）

```java
// Files：文件操作工具类

// 读取文件
String content = Files.readString(Path.of("file.txt"));  // JDK 11+
byte[] bytes = Files.readAllBytes(Path.of("file.txt"));
List<String> lines = Files.readAllLines(Path.of("file.txt"));

// 写入文件
Files.writeString(Path.of("file.txt"), "content");  // JDK 11+
Files.write(Path.of("file.txt"), "content".getBytes());
Files.writeLines(Path.of("file.txt"), List.of("line1", "line2"));

// 文件操作
Files.createFile(Path.of("newfile.txt"));
Files.createDirectory(Path.of("newdir"));
Files.createDirectories(Path.of("parent/child"));  // 递归创建
Files.delete(Path.of("file.txt"));
Files.deleteIfExists(Path.of("file.txt"));  // 存在才删除

// 复制和移动
Files.copy(Path.of("src.txt"), Path.of("dest.txt"));
Files.move(Path.of("src.txt"), Path.of("dest.txt"));

// 检查属性
Files.exists(Path.of("file.txt"));
Files.isDirectory(Path.of("dir"));
Files.isRegularFile(Path.of("file.txt"));
Files.size(Path.of("file.txt"));
Files.isReadable(Path.of("file.txt"));
Files.isWritable(Path.of("file.txt"));

// 遍历目录
try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of("dir"))) {
    for (Path entry : stream) {
        System.out.println(entry.getFileName());
    }
}

// 使用Stream遍历（JDK 8+）
Files.list(Path.of("dir"))
    .filter(Files::isRegularFile)
    .forEach(System.out::println);
```

### 6.6.3 文件遍历示例

```java
// 遍历目录树
public static void walkDirectory(Path dir) throws IOException {
    Files.walk(dir)
        .forEach(path -> System.out.println(path));
}

// 只遍历到指定深度
public static void walkDirectoryMaxDepth(Path dir, int maxDepth) throws IOException {
    Files.walk(dir, maxDepth)
        .forEach(path -> System.out.println(path));
}

// 过滤文件
public static void findFiles(Path dir, String extension) throws IOException {
    Files.walk(dir)
        .filter(Files::isRegularFile)
        .filter(p -> p.toString().endsWith(extension))
        .forEach(System.out::println);
}
```

---

## 6.7 练习题

```java
// 1. 说明NIO和传统IO的区别

// 2. Buffer的核心属性有哪些？说明它们的关系

// 3. 使用NIO实现文件复制

// 4. Selector的工作原理是什么？

// 5. 使用Files工具类实现：查找目录下所有.java文件
```

---

## 6.8 参考答案

```java
// 1. NIO vs 传统IO
// 传统IO：面向流，阻塞，一个连接一个线程
// NIO：面向缓冲区，非阻塞，一个线程处理多个连接
// NIO适合高并发场景，传统IO适合简单IO

// 2. Buffer核心属性
// capacity: 容量，固定大小
// position: 当前位置，下一个读写的位置
// limit: 限制，可读/可写的边界
// 关系: 0 <= position <= limit <= capacity

// 3. NIO文件复制
public static void copyFile(String src, String dest) throws IOException {
    try (FileInputStream fis = new FileInputStream(src);
         FileOutputStream fos = new FileOutputStream(dest)) {
        FileChannel in = fis.getChannel();
        FileChannel out = fos.getChannel();
        in.transferTo(0, in.size(), out);
    }
}

// 4. Selector原理
// - 创建Selector，将Channel注册到Selector
// - Selector监控各Channel的IO状态
// - select()阻塞直到有Channel准备好
// - selectedKeys()返回已准备好的Channel集合

// 5. 查找.java文件
public static void findJavaFiles(Path dir) throws IOException {
    Files.walk(dir)
        .filter(p -> p.toString().endsWith(".java"))
        .forEach(System.out::println);
}
```

---

[返回目录](./README.md)

## IO专题总结

### 核心知识点

| 主题 | 核心类 | 特点 |
|------|--------|------|
| 字节流 | InputStream/OutputStream | 二进制数据 |
| 字符流 | Reader/Writer | 文本数据 |
| 缓冲流 | BufferedInputStream等 | 减少IO次数 |
| 对象流 | ObjectInputStream/ObjectOutputStream | 对象持久化 |
| NIO | Buffer/Channel/Selector | 非阻塞多路复用 |
