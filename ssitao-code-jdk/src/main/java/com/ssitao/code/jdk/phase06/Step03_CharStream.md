# 步骤3：字符流 - 文本数据处理

---

## 3.1 Reader

### 3.1.1 Reader概述

```java
// Reader：字符输入流的抽象基类

public abstract class Reader
    implements Readable, Closeable {

    // 读取单个字符
    public abstract int read() throws IOException;

    // 读取字符数组
    public int read(char[] cbuf) throws IOException;
    public abstract int read(char[] cbuf, int off, int len) throws IOException;

    // 读取到String
    public String readLine() throws IOException;  // 已废弃，使用BufferedReader

    // 跳过字符
    public long skip(long n) throws IOException;

    // 标记和重置
    public boolean markSupported();
    public void mark(int readAheadLimit) throws IOException;
    public void reset() throws IOException;

    // 关闭流
    public abstract void close() throws IOException;
}
```

### 3.1.2 read方法详解

```java
// read() - 读取单个字符
// 返回值：字符(0-65535)，-1（到达末尾）
Reader reader = new FileReader("file.txt");
int ch;
while ((ch = reader.read()) != -1) {
    System.out.print((char) ch);
}

// read(char[]) - 读取到字符数组
Reader reader = new FileReader("file.txt");
char[] buffer = new char[1024];
int charsRead;
while ((charsRead = reader.read(buffer)) != -1) {
    // 处理 buffer[0..charsRead-1]
}

// read(char[], int off, int len) - 读取到指定范围
charsRead = reader.read(buffer, 0, buffer.length);
```

---

## 3.2 Writer

### 3.2.1 Writer概述

```java
// Writer：字符输出流的抽象基类

public abstract class Writer
    implements Appendable, Closeable, Flushable {

    // 写入单个字符
    public abstract void write(int c) throws IOException;

    // 写入字符数组
    public void write(char[] cbuf) throws IOException;
    public abstract void write(char[] cbuf, int off, int len) throws IOException;

    // 写入字符串
    public void write(String str) throws IOException;
    public void write(String str, int off, int len) throws IOException;

    // 追加
    public Writer append(CharSequence csq) throws IOException;
    public Writer append(CharSequence csq, int start, int end) throws IOException;
    public Writer append(char c) throws IOException;

    // 刷新和关闭
    public abstract void flush() throws IOException;
    public abstract void close() throws IOException;
}
```

### 3.2.2 write方法详解

```java
// write(int) - 写入单个字符
Writer writer = new FileWriter("file.txt");
writer.write(65);  // 写入'A'

// write(char[]) - 写入字符数组
char[] chars = {'H', 'e', 'l', 'l', 'o'};
writer.write(chars);

// write(String) - 写入字符串
writer.write("Hello World");

// write(String, int off, int len) - 写入字符串的一部分
writer.write("Hello World", 0, 5);  // 写入"Hello"

// append - 追加字符/字符序列
Writer writer = new FileWriter("file.txt", true);  // 追加模式
writer.append("Appended text");
writer.append("\n");
writer.append("Line 2", 0, 6);
```

---

## 3.3 FileReader

### 3.3.1 基本使用

```java
// FileReader：用于读取字符文件的便捷类
// 默认使用平台默认编码

// 构造方式
FileReader fr1 = new FileReader("file.txt");
FileReader fr2 = new FileReader(new File("file.txt"));
FileReader fr3 = new FileReader("file.txt", Charset.forName("UTF-8"));

// 读取所有字符
try (FileReader fr = new FileReader("file.txt")) {
    char[] allChars = new char[(int) new File("file.txt").length()];
    fr.read(allChars);
    String content = new String(allChars);
}

// 按字符数组读取
try (FileReader fr = new FileReader("file.txt")) {
    char[] buffer = new char[8192];
    int charsRead;
    StringBuilder sb = new StringBuilder();
    while ((charsRead = fr.read(buffer)) != -1) {
        sb.append(buffer, 0, charsRead);
    }
}
```

### 3.3.2 编码问题

```java
// FileReader使用平台默认编码，可能导致乱码
// 推荐使用InputStreamReader并指定编码

// 错误示例（中文平台可能乱码）
try (FileReader fr = new FileReader("chinese.txt")) {
    // ...
}

// 正确做法：指定UTF-8编码
try (InputStreamReader isr = new InputStreamReader(
        new FileInputStream("chinese.txt"), StandardCharsets.UTF_8)) {
    // ...
}

// 或使用Files工具类（JDK 7+）
String content = Files.readString(Path.of("chinese.txt"), StandardCharsets.UTF_8);
```

---

## 3.4 FileWriter

### 3.4.1 基本使用

```java
// FileWriter：用于写入字符文件的便捷类
// 默认使用平台默认编码

// 构造方式
FileWriter fw1 = new FileWriter("file.txt");        // 覆盖
FileWriter fw2 = new FileWriter("file.txt", false); // 覆盖
FileWriter fw3 = new FileWriter("file.txt", true);  // 追加
FileWriter fw4 = new FileWriter(new File("file.txt"));
FileWriter fw5 = new FileWriter("file.txt", StandardCharsets.UTF_8);

// 写入
try (FileWriter fw = new FileWriter("output.txt"))) {
    fw.write("Hello");
    fw.write(" World");
    fw.write('\n');
    fw.write("新的一行");
}

// 追加模式
try (FileWriter fw = new FileWriter("log.txt", true)) {
    fw.write("New log entry\n");
}
```

---

## 3.5 InputStreamReader / OutputStreamWriter

### 3.5.1 转换流概念

```java
// 转换流：字节流和字符流之间的桥梁
// InputStreamReader: 字节流 -> 字符流
// OutputStreamWriter: 字符流 -> 字节流

// 重要：可以指定编码
```

### 3.5.2 InputStreamReader

```java
// InputStreamReader：将InputStream转换为Reader

// 基本使用
InputStream is = new FileInputStream("file.txt");
Reader reader = new InputStreamReader(is);

// 指定编码
InputStream is = new FileInputStream("file.txt");
Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);

// 常见用法：包装其他字节流
Reader reader = new InputStreamReader(
    new BufferedInputStream(new FileInputStream("file.txt")),
    StandardCharsets.UTF_8);
```

### 3.5.3 OutputStreamWriter

```java
// OutputStreamWriter：将Writer转换为OutputStream

// 基本使用
OutputStream os = new FileOutputStream("file.txt");
Writer writer = new OutputStreamWriter(os);

// 指定编码
OutputStream os = new FileOutputStream("file.txt");
Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);

// 常见用法：支持各种编码的写入
try (Writer writer = new OutputStreamWriter(
        new FileOutputStream("output.txt"), "GBK")) {
    writer.write("中文内容");
}
```

---

## 3.6 CharArrayReader / CharArrayWriter

### 3.6.1 CharArrayReader

```java
// CharArrayReader：从字符数组读取

char[] data = "Hello World".toCharArray();
CharArrayReader car = new CharArrayReader(data);

// 读取
int ch;
while ((ch = car.read()) != -1) {
    System.out.print((char) ch);
}

// 使用Reader的mark/reset功能
char[] buffer = "Hello".toCharArray();
CharArrayReader reader = new CharArrayReader(buffer);
reader.mark(10);
reader.skip(2);
reader.reset();  // 重置到mark位置
```

### 3.6.2 CharArrayWriter

```java
// CharArrayWriter：写入到字符数组

CharArrayWriter caw = new CharArrayWriter();

// 写入
caw.write("Hello");
caw.write(" World");
caw.append("!!!");

// 获取字符数组
char[] chars = caw.toCharArray();

// 转换为字符串
String content = caw.toString();

// 写入到另一个Writer
caw.writeTo(new FileWriter("output.txt"));

// 追加模式（通过构造函数指定）
CharArrayWriter caw2 = new CharArrayWriter();
caw2.write("First");
caw2.write("Second");
```

---

## 3.7 StringReader / StringWriter

### 3.7.1 StringReader

```java
// StringReader：从String读取

String text = "Hello\nWorld";
StringReader sr = new StringReader(text);

// 读取
int ch;
while ((ch = sr.read()) != -1) {
    System.out.print((char) ch);
}

// 使用readLine
BufferedReader br = new BufferedReader(new StringReader(text));
String line;
while ((line = br.readLine()) != null) {
    System.out.println(line);
}

// 使用mark/reset
StringReader reader = new StringReader("Hello");
reader.mark(10);
reader.skip(2);
reader.reset();  // 重置到开始
```

### 3.7.2 StringWriter

```java
// StringWriter：写入到StringBuilder

StringWriter sw = new StringWriter();

// 写入
sw.write("Hello");
sw.write(" World");
sw.append("!!!");

// 获取结果
String content = sw.toString();

// 获取StringBuilder（如果需要修改）
StringBuilder sb = sw.getBuffer();

// StringWriter自动扩大缓冲区
StringWriter sw2 = new StringWriter(100);  // 初始容量100
```

---

## 3.8 PrintWriter

### 3.8.1 基本使用

```java
// PrintWriter：格式化输出，自动刷新

// 构造方式
PrintWriter pw1 = new PrintWriter("file.txt");
PrintWriter pw2 = new PrintWriter(new FileWriter("file.txt"));
PrintWriter pw3 = new PrintWriter(new OutputStreamWriter(
    new FileOutputStream("file.txt"), StandardCharsets.UTF_8), true);  // autoFlush

// 写入方法（自动转换）
pw.print(65);         // 65
pw.println(65);        // 65\n
pw.println("Hello");   // Hello\n
pw.printf("%s: %d", "Answer", 42);  // Answer: 42\n

// 支持各种类型
pw.print(true);
pw.print(3.14159);
pw.print('A');
pw.print(new int[]{1, 2, 3});

// 刷新和关闭
pw.flush();
pw.close();
```

### 3.8.2 与PrintStream对比

```java
// PrintStream vs PrintWriter
// PrintStream: 字节流，可以写字节/字符，OutputStream的包装
// PrintWriter: 字符流，只写字符，Writer的包装

// System.out就是PrintStream
PrintStream out = System.out;
out.println("Hello");

// 推荐使用PrintWriter（更好的国际化支持）
PrintWriter pw = new PrintWriter(new OutputStreamWriter(
    new FileOutputStream("file.txt"), StandardCharsets.UTF_8));
pw.println("Hello World");
```

---

## 3.9 练习题

```java
// 1. 说明字符流和字节流的区别，为什么需要字符流？

// 2. 如何指定字符流的编码？

// 3. 实现一个方法，统计文本文件的字符数、单词数、行数

// 4. PrintWriter相比FileWriter有什么优势？

// 5. 使用字符流实现文件复制
```

---

## 3.10 参考答案

```java
// 1. 字符流 vs 字节流
// 字节流：以字节为单位，适用于二进制数据
// 字符流：以字符为单位，适用于文本数据
// 字符流内部使用字节流 + 编码转换
// 字符流可以正确处理Unicode字符

// 2. 指定编码
// 使用InputStreamReader/OutputStreamWriter
InputStreamReader isr = new InputStreamReader(
    new FileInputStream("file.txt"), StandardCharsets.UTF_8);
OutputStreamWriter osw = new OutputStreamWriter(
    new FileOutputStream("file.txt"), StandardCharsets.UTF_8);

// JDK 10+
Reader r = new FileReader("file.txt", StandardCharsets.UTF_8);

// 3. 统计文件
public static class TextStats {
    long chars, words, lines;
}

public static TextStats analyzeFile(String path) throws IOException {
    TextStats stats = new TextStats();
    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
        String line;
        while ((line = br.readLine()) != null) {
            stats.lines++;
            stats.chars += line.length();
            stats.words += line.split("\\s+").length;
        }
    }
    return stats;
}

// 4. PrintWriter优势
// - print/println/printf格式化输出
// - 自动类型转换（任何对象转字符串）
// - 可指定编码
// - 可设置自动刷新

// 5. 字符流复制文件
public static void copyFile(String src, String dest) throws IOException {
    try (BufferedReader br = new BufferedReader(
            new InputStreamReader(new FileInputStream(src), StandardCharsets.UTF_8));
         BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(dest), StandardCharsets.UTF_8))) {

        char[] buffer = new char[8192];
        int charsRead;
        while ((charsRead = br.read(buffer)) != -1) {
            bw.write(buffer, 0, charsRead);
        }
    }
}
```

---

[返回目录](./README.md) | [下一步：缓冲流](./Step04_BufferedStream.md)
