# 步骤2：字节流 - 二进制数据处理

---

## 2.1 InputStream

### 2.1.1 InputStream概述

```java
// InputStream：字节输入流的抽象基类

public abstract class InputStream
    implements Closeable {

    // 读取单个字节
    public abstract int read() throws IOException;

    // 读取字节数组
    public int read(byte[] b) throws IOException;
    public int read(byte[] b, int off, int len) throws IOException;

    // 跳过字节
    public long skip(long n) throws IOException;

    // 可用字节数
    public int available() throws IOException;

    // 是否支持标记
    public boolean markSupported();
    public void mark(int readLimit);
    public void reset() throws IOException;

    // 关闭流
    public void close() throws IOException;
}
```

### 2.1.2 read方法详解

```java
// read() - 读取单个字节
// 返回值：0-255（读取的字节），-1（到达末尾）
FileInputStream fis = new FileInputStream("file.txt");
int data;
while ((data = fis.read()) != -1) {
    System.out.print((char) data);
}
fis.close();

// read(byte[]) - 读取到字节数组
// 返回值：读取的字节数，-1（到达末尾）
FileInputStream fis = new FileInputStream("file.txt");
byte[] buffer = new byte[1024];
int bytesRead;
while ((bytesRead = fis.read(buffer)) != -1) {
    // 处理 buffer[0..bytesRead-1]
}
fis.close();

// read(byte[], int off, int len) - 读取到指定范围
int bytesRead = fis.read(buffer, 0, buffer.length);
```

### 2.1.3 常用方法

```java
// skip - 跳过字节
FileInputStream fis = new FileInputStream("file.txt");
fis.skip(100);  // 跳过前100字节

// available - 返回可读的字节数（不阻塞）
int available = fis.available();

// mark/reset - 支持标记和重置
FileInputStream fis = new FileInputStream("file.txt");
fis.mark(100);  // 标记当前位置，最多读100字节
// ... 读取一些数据
fis.reset();    // 重置到标记位置

// 注意：不是所有InputStream都支持mark/reset
// markSupported()可检查
if (fis.markSupported()) {
    fis.mark(100);
}
```

---

## 2.2 OutputStream

### 2.2.1 OutputStream概述

```java
// OutputStream：字节输出流的抽象基类

public abstract class OutputStream
    implements Closeable, Flushable {

    // 写入单个字节
    public abstract void write(int b) throws IOException;

    // 写入字节数组
    public void write(byte[] b) throws IOException;
    public void write(byte[] b, int off, int len) throws IOException;

    // 刷新缓冲区
    public void flush() throws IOException;

    // 关闭流
    public void close() throws IOException;
}
```

### 2.2.2 write方法详解

```java
// write(int) - 写入单个字节（低8位）
FileOutputStream fos = new FileOutputStream("file.txt");
fos.write(65);  // 写入字符'A'
fos.write(66);   // 写入字符'B'
fos.close();

// write(byte[]) - 写入整个数组
byte[] data = "Hello".getBytes();
fos.write(data);

// write(byte[], int off, int len) - 写入部分数组
byte[] buffer = new byte[1024];
// ... 填充buffer
fos.write(buffer, 0, bytesToWrite);

// 追加模式
FileOutputStream fos = new FileOutputStream("file.txt", true);  // true=追加
```

### 2.2.3 flush方法

```java
// flush - 刷新缓冲区，强制写出数据
// 对于BufferedOutputStream尤其重要

BufferedOutputStream bos = new BufferedOutputStream(
    new FileOutputStream("file.txt"));

bos.write("Hello".getBytes());
// 数据可能在缓冲区，未写出
bos.flush();  // 强制写出
// 或
bos.close();  // close会自动flush
```

---

## 2.3 FileInputStream

### 2.3.1 基本使用

```java
// FileInputStream：从文件读取字节

// 构造方式
FileInputStream fis1 = new FileInputStream("file.txt");  // 路径
FileInputStream fis2 = new FileInputStream(new File("file.txt"));  // File对象
FileInputStream fis3 = new FileInputStream(fd);  // FileDescriptor

// 读取所有字节
try (FileInputStream fis = new FileInputStream("file.txt")) {
    byte[] allBytes = fis.readAllBytes();  // JDK 9+
    String content = new String(allBytes);
}

// 按字节数组读取
try (FileInputStream fis = new FileInputStream("file.txt")) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    StringBuilder sb = new StringBuilder();
    while ((bytesRead = fis.read(buffer)) != -1) {
        sb.append(new String(buffer, 0, bytesRead));
    }
}
```

---

## 2.4 FileOutputStream

### 2.4.1 基本使用

```java
// FileOutputStream：向文件写入字节

// 构造方式
FileOutputStream fos1 = new FileOutputStream("file.txt");  // 覆盖
FileOutputStream fos2 = new FileOutputStream("file.txt", false);  // 覆盖
FileOutputStream fos3 = new FileOutputStream("file.txt", true);  // 追加
FileOutputStream fos4 = new FileOutputStream(new File("file.txt"));
FileOutputStream fos5 = new FileOutputStream(fd);

// 写入字节
try (FileOutputStream fos = new FileOutputStream("output.txt")) {
    fos.write(65);  // 写入'A'
    fos.write("Hello".getBytes());
}

// 追加模式
try (FileOutputStream fos = new FileOutputStream("log.txt", true)) {
    fos.write("New log entry\n".getBytes());
}
```

---

## 2.5 ByteArrayInputStream

### 2.5.1 基本使用

```java
// ByteArrayInputStream：从字节数组读取

byte[] data = "Hello World".getBytes();
ByteArrayInputStream bais = new ByteArrayInputStream(data);

// 读取
int data2;
while ((data2 = bais.read()) != -1) {
    System.out.print((char) data2);
}

// 使用ByteArrayInputStream转换数据
byte[] rawData = getDataFromNetwork();  // 假设这是从网络获取的数据
try (ByteArrayInputStream bais = new ByteArrayInputStream(rawData)) {
    DataInputStream dis = new DataInputStream(bais);
    int id = dis.readInt();
    String name = dis.readUTF();
}
```

### 2.5.2 ByteArrayOutputStream

```java
// ByteArrayOutputStream：写入到字节数组

ByteArrayOutputStream baos = new ByteArrayOutputStream();

// 写入数据
baos.write("Hello".getBytes());
baos.write(65);

// 获取写入的数据
byte[] data = baos.toByteArray();

// 转换为字符串
String content = baos.toString();  // 使用默认编码
String contentUTF = baos.toString("UTF-8");

// 写入到另一个输出流
baos.writeTo(new FileOutputStream("output.txt"));

// 追加模式（使用ByteArrayOutputStream本身）
ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
baos2.write("First".getBytes());
baos2.write("Second".getBytes());
// baos2内容："FirstSecond"
```

---

## 2.6 DataInputStream / DataOutputStream

### 2.6.1 DataInputStream

```java
// DataInputStream：读取Java基本数据类型

ByteArrayOutputStream baos = new ByteArrayOutputStream();
DataOutputStream dos = new DataOutputStream(baos);
dos.writeInt(12345);
dos.writeDouble(3.14159);
dos.writeBoolean(true);
dos.writeUTF("Hello");

// 读取
byte[] data = baos.toByteArray();
ByteArrayInputStream bais = new ByteArrayInputStream(data);
DataInputStream dis = new DataInputStream(bais);

int num = dis.readInt();        // 12345
double pi = dis.readDouble();   // 3.14159
boolean flag = dis.readBoolean();  // true
String str = dis.readUTF();     // "Hello"
```

### 2.6.2 DataOutputStream

```java
// DataOutputStream：写入Java基本数据类型

// 支持的数据类型：
writeInt(int)
writeLong(long)
writeFloat(float)
writeDouble(double)
writeBoolean(boolean)
writeChar(char)
writeUTF(String)
writeBytes(String)  // 写入字符串的字节

// 示例：写入多种数据类型
try (FileOutputStream fos = new FileOutputStream("data.bin");
     DataOutputStream dos = new DataOutputStream(fos)) {

    dos.writeInt(42);
    dos.writeDouble(2.718);
    dos.writeUTF("Answer");
    dos.writeBoolean(true);
}
```

---

## 2.7 PipedInputStream / PipedOutputStream

### 2.7.1 基本概念

```java
// PipedInputStream/PipedOutputStream：管道流
// 用于线程间数据传输

// 一个线程写入管道，另一个线程从管道读取
// 类似于Unix的管道概念
```

### 2.7.2 使用示例

```java
// 示例：生产者-消费者模式

class Producer implements Runnable {
    private PipedOutputStream out;

    public Producer(PipedOutputStream out) {
        this.out = out;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                out.write(("Message " + i + "\n").getBytes());
                Thread.sleep(100);
            }
            out.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private PipedInputStream in;

    public Consumer(PipedInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received: " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 使用
PipedOutputStream out = new PipedOutputStream();
PipedInputStream in = new PipedInputStream(out);

new Thread(new Producer(out)).start();
new Thread(new Consumer(in)).start();
```

---

## 2.8 练习题

```java
// 1. 说明read()返回int而不是byte的原因

// 2. 使用字节流复制文件（带缓冲）

// 3. 实现一个方法，将int[]数组写入文件，再用DataInputStream读取

// 4. FileInputStream和ByteArrayInputStream有什么区别？

// 5. 使用管道流实现两个线程间的数据传输
```

---

## 2.9 参考答案

```java
// 1. read()返回int而不是byte
// 因为返回-1表示到达流的末尾
// byte范围是-128到127，无法表示-1
// int范围是-2147483648到2147483647

// 2. 文件复制（带缓冲）
public static void copyFile(String src, String dest) throws IOException {
    try (FileInputStream fis = new FileInputStream(src);
         BufferedInputStream bis = new BufferedInputStream(fis);
         FileOutputStream fos = new FileOutputStream(dest);
         BufferedOutputStream bos = new BufferedOutputStream(fos)) {

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
    }
}

// 3. int[]数组写入和读取
public static void writeInts(String file, int[] arr) throws IOException {
    try (DataOutputStream dos = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(file)))) {
        dos.writeInt(arr.length);
        for (int n : arr) {
            dos.writeInt(n);
        }
    }
}

public static int[] readInts(String file) throws IOException {
    try (DataInputStream dis = new DataInputStream(
            new BufferedInputStream(new FileInputStream(file)))) {
        int len = dis.readInt();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = dis.readInt();
        }
        return arr;
    }
}

// 4. FileInputStream vs ByteArrayInputStream
// FileInputStream: 从文件读取数据
// ByteArrayInputStream: 从内存字节数组读取数据
// 两者都是InputStream的实现，用法类似

// 5. 管道流实现线程间传输
PipedOutputStream pos = new PipedOutputStream();
PipedInputStream pis = new PipedInputStream(pos);

// 生产者线程
new Thread(() -> {
    try {
        for (int i = 0; i < 100; i++) {
            pos.write(i);
        }
        pos.close();
    } catch (IOException e) {}
}).start();

// 消费者线程
new Thread(() -> {
    try {
        int data;
        while ((data = pis.read()) != -1) {
            System.out.println(data);
        }
        pis.close();
    } catch (IOException e) {}
}).start();
```

---

[返回目录](./README.md) | [下一步：字符流](./Step03_CharStream.md)
