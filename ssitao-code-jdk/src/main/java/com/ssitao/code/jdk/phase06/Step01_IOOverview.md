# 步骤1：IO概述 - 流与装饰器模式

---

## 1.1 IO简介

### 1.1.1 什么是IO

```java
// IO = Input/Output（输入/输出）
// 程序与外部世界交换数据的方式

// 输入：读取数据（从文件、网络、键盘等）
// 输出：写入数据（到文件、网络、屏幕等）

// Java IO支持：
// - 文件IO
// - 网络IO
// - 内存IO
// - 管道IO
```

### 1.1.2 IO分类

```
按数据单位分类：
├── 字节流（Byte Stream）
│   - 以字节为单位（8位）
│   - 用于二进制数据（图片、音频、视频）
│   - InputStream / OutputStream
│
└── 字符流（Character Stream）
    - 以字符为单位（16位Unicode）
    - 用于文本数据
    - Reader / Writer

按流向分类：
├── 输入流（Input Stream）
└── 输出流（Output Stream）

按功能分类：
├── 节点流（Node Stream）
│   - 直接与数据源连接
│   - 如：FileInputStream
│
└── 装饰流（Decorator Stream）
    - 包装节点流，增强功能
    - 如：BufferedInputStream
```

---

## 1.2 IO架构

### 1.2.1 类继承关系

```java
// 字节流
java.io.InputStream (抽象类)
├── FileInputStream
├── ByteArrayInputStream
├── PipedInputStream
├── BufferedInputStream (装饰器)
├── DataInputStream
├── ObjectInputStream
└── ZipInputStream

java.io.OutputStream (抽象类)
├── FileOutputStream
├── ByteArrayOutputStream
├── PipedOutputStream
├── BufferedOutputStream (装饰器)
├── DataOutputStream
├── ObjectOutputStream
└── ZipOutputStream

// 字符流
java.io.Reader (抽象类)
├── FileReader
├── CharArrayReader
├── PipedReader
├── BufferedReader (装饰器)
├── InputStreamReader
│   └── FileReader
└── StringReader

java.io.Writer (抽象类)
├── FileWriter
├── CharArrayWriter
├── PipedWriter
├── BufferedWriter (装饰器)
├── OutputStreamWriter
│   └── FileWriter
└── StringWriter
```

### 1.2.2 装饰器模式

```java
// Java IO使用装饰器模式（Decorator Pattern）

// 核心思想：
// - 包装已有的流，增加新功能
// - 可以多层包装
// - 运行时动态组合

// 示例：多层包装
FileInputStream fis = new FileInputStream("file.txt");
BufferedInputStream bis = new BufferedInputStream(fis);
DataInputStream dis = new DataInputStream(bis);

// 等价于
DataInputStream dis = new DataInputStream(
    new BufferedInputStream(
        new FileInputStream("file.txt")
    )
);

// 读取数据（带缓冲和数据类型解析）
int value = dis.readInt();
```

---

## 1.3 File类

### 1.3.1 File类概述

```java
// File类：代表文件或目录的路径名
// 不是IO流，用于操作文件/目录本身

File file = new File("test.txt");

// 文件操作
file.exists();           // 文件是否存在
file.isFile();           // 是否是文件
file.isDirectory();      // 是否是目录
file.createNewFile();    // 创建新文件
file.delete();           // 删除文件
file.length();           // 文件大小（字节）

// 目录操作
file.mkdir();            // 创建单层目录
file.mkdirs();          // 创建多层目录
String[] list = file.list();  // 列出目录内容

// 路径操作
file.getAbsolutePath();   // 获取绝对路径
file.getName();          // 获取文件名
file.getParent();        // 获取父目录
file.renameTo(new File("new.txt"));  // 重命名
```

### 1.3.2 遍历目录

```java
// 遍历目录示例
File dir = new File("/path/to/dir");

// 列出所有文件/目录
String[] names = dir.list();
for (String name : names) {
    System.out.println(name);
}

// 列出所有文件（带过滤）
File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
for (File f : files) {
    System.out.println(f.getName());
}

// 递归遍历
public void walkDirectory(File dir) {
    File[] children = dir.listFiles();
    if (children == null) return;

    for (File child : children) {
        if (child.isDirectory()) {
            walkDirectory(child);
        } else {
            System.out.println(child.getAbsolutePath());
        }
    }
}
```

---

## 1.4 关闭资源

### 1.4.1 传统方式

```java
// 传统方式：finally中关闭
FileInputStream fis = null;
try {
    fis = new FileInputStream("file.txt");
    // 读取数据
    int data = fis.read();
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (fis != null) {
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 1.4.2 try-with-resources（JDK 7+）

```java
// try-with-resources：自动关闭资源
try (FileInputStream fis = new FileInputStream("file.txt")) {
    int data = fis.read();
} catch (IOException e) {
    e.printStackTrace();
}
// fis自动关闭，不需要finally

// 可以同时管理多个资源
try (
    FileInputStream fis = new FileInputStream("input.txt");
    FileOutputStream fos = new FileOutputStream("output.txt")
) {
    int data;
    while ((data = fis.read()) != -1) {
        fos.write(data);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### 1.4.3 需要关闭的资源

```java
// 需要关闭的资源：
// - FileInputStream/FileOutputStream
// - FileReader/FileWriter
// - BufferedInputStream/BufferedOutputStream
// - BufferedReader/BufferedWriter
// - Socket/ServerSocket
// - Connection/Statement/ResultSet (JDBC)

// 不需要关闭的资源：
// - ByteArrayInputStream/ByteArrayOutputStream
// - CharArrayReader/CharArrayWriter
// - StringReader/StringWriter
// - System.in/System.out/System.err
```

---

## 1.5 常见IO操作

### 1.5.1 文件复制

```java
// 基本文件复制（字节流）
public static void copyFile(String src, String dest) throws IOException {
    try (FileInputStream fis = new FileInputStream(src);
         FileOutputStream fos = new FileOutputStream(dest)) {

        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead);
        }
    }
}

// 文件复制（字符流）
public static void copyTextFile(String src, String dest) throws IOException {
    try (FileReader fr = new FileReader(src);
         FileWriter fw = new FileWriter(dest)) {

        char[] buffer = new char[8192];
        int charsRead;
        while ((charsRead = fr.read(buffer)) != -1) {
            fw.write(buffer, 0, charsRead);
        }
    }
}
```

### 1.5.2 读取文件内容

```java
// 方式1：按字节读取
try (FileInputStream fis = new FileInputStream("file.txt")) {
    int data;
    StringBuilder sb = new StringBuilder();
    while ((data = fis.read()) != -1) {
        sb.append((char) data);
    }
    String content = sb.toString();
}

// 方式2：按字节数组读取
try (FileInputStream fis = new FileInputStream("file.txt")) {
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fis.read(buffer)) != -1) {
        // 处理 buffer[0..bytesRead]
    }
}

// 方式3：使用BufferedReader（推荐文本）
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}

// 方式4：使用Files工具类（JDK 7+）
String content = Files.readString(Path.of("file.txt"));
List<String> lines = Files.readAllLines(Path.of("file.txt"));
```

### 1.5.3 写入文件

```java
// 方式1：字节流
try (FileOutputStream fos = new FileOutputStream("file.txt")) {
    fos.write("Hello".getBytes());
}

// 方式2：字符流
try (FileWriter fw = new FileWriter("file.txt"))) {
    fw.write("Hello World");
}

// 方式3：BufferedWriter
try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"))) {
    bw.write("Line 1");
    bw.newLine();
    bw.write("Line 2");
}

// 方式4：使用Files工具类（JDK 7+）
Files.writeString(Path.of("file.txt"), "Hello World");
Files.write(Path.of("file.txt"), "Hello".getBytes());
Files.writeLines(Path.of("file.txt"), List.of("Line 1", "Line 2"));
```

---

## 1.6 练习题

```java
// 1. 说明字节流和字符流的区别

// 2. Java IO中装饰器模式是如何应用的？

// 3. 使用try-with-resources有什么好处？

// 4. 实现一个方法，统计文件的行数

// 5. 说明为什么要使用缓冲流
```

---

## 1.7 参考答案

```java
// 1. 字节流 vs 字符流
// 字节流：以字节(8位)为单位，用于二进制数据
// 字符流：以字符(16位Unicode)为单位，用于文本数据
// 字符流内部使用字节流 + 编码转换

// 2. 装饰器模式应用
// - 通过包装已有流来增强功能
// - 可以多层包装
// BufferedInputStream = new BufferedInputStream(new FileInputStream(...))

// 3. try-with-resources好处
// - 自动关闭资源
// - 代码更简洁
// - 避免忘记关闭
// - 自动处理异常情况

// 4. 统计文件行数
public static long countLines(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        return br.lines().count();
    }
}

// 或
public static long countLines2(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        long count = 0;
        while (br.readLine() != null) {
            count++;
        }
        return count;
    }
}

// 5. 使用缓冲流的原因
// - 减少系统调用次数
// - 缓冲流一次读取大量数据到内存
// - 然后从缓冲读取，避免频繁IO操作
// - 大幅提高IO效率
```

---

[返回目录](./README.md) | [下一步：字节流](./Step02_ByteStream.md)
