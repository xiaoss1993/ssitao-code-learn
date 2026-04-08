# 步骤4：缓冲流 - 高效IO

---

## 4.1 缓冲流概述

### 4.1.1 为什么需要缓冲

```java
// 缓冲流原理：
// - 不直接操作底层IO
// - 先将数据写入缓冲区
// - 缓冲区满或手动刷新时，才进行实际IO
// - 减少系统调用次数，提高性能

// 无缓冲：每读/写一次，都可能触发一次系统调用
// 有缓冲：先操作内存缓冲区，减少系统调用

// 默认缓冲区大小：
// - BufferedInputStream: 8192字节
// - BufferedOutputStream: 8192字节
// - BufferedReader: 8192字符
// - BufferedWriter: 8192字符
```

### 4.1.2 缓冲流类

```
缓冲流（装饰器类）
├── BufferedInputStream    - 缓冲字节输入
├── BufferedOutputStream   - 缓冲字节输出
├── BufferedReader         - 缓冲字符输入
└── BufferedWriter        - 缓冲字符输出
```

---

## 4.2 BufferedInputStream

### 4.2.1 基本使用

```java
// BufferedInputStream：为InputStream增加缓冲

// 构造方式
BufferedInputStream bis1 = new BufferedInputStream(new FileInputStream("file.txt"));
BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream("file.txt"), 16384);  // 自定义缓冲

// 使用方式：与普通InputStream相同
try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("file.txt"))) {
    int data;
    while ((data = bis.read()) != -1) {
        // 处理数据
    }
}

// 按数组读取
byte[] buffer = new byte[8192];
int bytesRead;
while ((bytesRead = bis.read(buffer)) != -1) {
    // 处理 buffer[0..bytesRead-1]
}
```

### 4.2.2 mark和reset

```java
// BufferedInputStream支持mark/reset

try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("file.txt"))) {
    bis.mark(100);  // 标记当前位置，最多读100字节
    bis.skip(50);  // 跳过50字节
    // ... 处理一些数据
    bis.reset();   // 重置到标记位置

    // 重新读取
}
```

---

## 4.3 BufferedOutputStream

### 4.3.1 基本使用

```java
// BufferedOutputStream：为OutputStream增加缓冲

// 构造方式
BufferedOutputStream bos1 = new BufferedOutputStream(new FileOutputStream("file.txt"));
BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream("file.txt"), 16384);

// 使用方式：与普通OutputStream相同
try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("file.txt"))) {
    bos.write("Hello".getBytes());
    bos.flush();  // 手动刷新
}

// 不需要每次写入都刷新
try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("file.txt"))) {
    for (int i = 0; i < 1000; i++) {
        bos.write(("Line " + i + "\n").getBytes());
        // 数据先写入缓冲区，不立即写入磁盘
    }
    // 关闭时自动flush
}
```

---

## 4.4 BufferedReader

### 4.4.1 基本使用

```java
// BufferedReader：为Reader增加缓冲

// 构造方式
BufferedReader br1 = new BufferedReader(new FileReader("file.txt"));
BufferedReader br2 = new BufferedReader(new InputStreamReader(
    new FileInputStream("file.txt"), StandardCharsets.UTF_8), 16384);

// 使用方式
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    int ch;
    while ((ch = br.read()) != -1) {
        System.out.print((char) ch);
    }
}

// readLine() - 读取一行（JDK 1.1引入）
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
}
```

### 4.4.2 readLine详解

```java
// readLine()特点：
// - 读取一行，不包含换行符
// - 返回null表示到达末尾
// - 不处理不同平台的换行符差异

// 不同换行符：
// Windows: \r\n
// Unix/Linux: \n
// Old Mac: \r

// 处理换行符差异
String line;
while ((line = br.readLine()) != null) {
    // Unix系统：直接添加换行
    System.out.println(line);  // println自带换行
}

// 如果需要保留原始换行符
String line;
while ((line = br.readLine()) != null) {
    if (line.contains("\r\n")) {
        // Windows格式
    } else if (line.endsWith("\r")) {
        // Old Mac格式
    }
}
```

### 4.4.3 lines方法（JDK 8+）

```java
// lines() - 返回Stream<String>

try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    // 统计行数
    long lineCount = br.lines().count();

    // 处理每一行
    br.lines()
        .filter(line -> !line.isEmpty())
        .map(String::toUpperCase)
        .forEach(System.out::println);

    // 按行号处理
    AtomicInteger lineNum = new AtomicInteger();
    br.lines()
        .forEach(line ->
            System.out.println(lineNum.incrementAndGet() + ": " + line));
}
```

---

## 4.5 BufferedWriter

### 4.5.1 基本使用

```java
// BufferedWriter：为Writer增加缓冲

// 构造方式
BufferedWriter bw1 = new BufferedWriter(new FileWriter("file.txt"));
BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(
    new FileOutputStream("file.txt"), StandardCharsets.UTF_8), 16384);

// 基本使用
try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"))) {
    bw.write("Hello");
    bw.newLine();  // 写入换行符
    bw.write("World");
    bw.flush();     // 手动刷新
}
```

### 4.5.2 newLine方法

```java
// newLine() - 写入平台默认的换行符
try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"))) {
    bw.write("Line 1");
    bw.newLine();  // 写入\n（Unix）或\r\n（Windows）
    bw.write("Line 2");
    bw.newLine();
}

// 手动指定换行符
bw.write("Line 1");
bw.write(System.lineSeparator());  // JDK 1.7+
bw.write("Line 2");
```

---

## 4.6 缓冲流性能对比

### 4.6.1 性能差异

```java
// 性能测试：文件复制

// 无缓冲
long start = System.nanoTime();
try (FileInputStream fis = new FileInputStream("input.txt");
     FileOutputStream fos = new FileOutputStream("output.txt")) {
    int data;
    while ((data = fis.read()) != -1) {
        fos.write(data);
    }
}
long unbufferedTime = System.nanoTime() - start;

// 有缓冲
start = System.nanoTime();
try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("input.txt"));
     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("output.txt"))) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
    }
}
long bufferedTime = System.nanoTime() - start;

// 缓冲流通常快10-100倍
System.out.println("Unbuffered: " + unbufferedTime / 1_000_000 + "ms");
System.out.println("Buffered: " + bufferedTime / 1_000_000 + "ms");
```

### 4.6.2 最佳实践

```java
// 最佳实践：总是使用缓冲流

// 字节流
try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("file.txt"));
     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("output.txt"))) {
    byte[] buffer = new byte[8192];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, bytesRead);
    }
}

// 字符流
try (BufferedReader br = new BufferedReader(new FileReader("input.txt"));
     BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    char[] buffer = new char[8192];
    int charsRead;
    while ((charsRead = br.read(buffer)) != -1) {
        bw.write(buffer, 0, charsRead);
    }
}

// 文本读写（使用readLine）
try (BufferedReader br = new BufferedReader(new FileReader("input.txt"));
     BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    String line;
    while ((line = br.readLine()) != null) {
        bw.write(line);
        bw.newLine();
    }
}
```

---

## 4.7 缓冲流与flush

### 4.7.1 flush时机

```java
// 什么时候需要flush

// 1. 缓冲区满时自动flush

// 2. 关闭流时自动flush
try (BufferedWriter bw = new BufferedWriter(new FileWriter("file.txt"))) {
    bw.write("Hello");
    // close时自动flush
}

// 3. 重要数据需要立即写出
try (BufferedWriter bw = new BufferedWriter(new FileWriter("log.txt"))) {
    bw.write("Starting process");
    bw.flush();  // 立即写出，保证崩溃时不丢失
    // ... 执行操作
    bw.write("Process completed");
    bw.flush();
}

// 4. 网络IO，实时传输
try (BufferedWriter bw = new BufferedWriter(
        new OutputStreamWriter(socket.getOutputStream()))) {
    bw.write("data1");
    bw.flush();  // 立即发送
    // ...
}
```

### 4.7.2 自动刷新

```java
// PrintWriter支持自动刷新
PrintWriter pw = new PrintWriter(
    new BufferedWriter(new FileWriter("output.txt")),
    true  // autoFlush
);

pw.println("This will be auto flushed");
// 等价于
pw.print("This will be auto flushed\n");
pw.flush();
```

---

## 4.8 练习题

```java
// 1. 缓冲流的工作原理是什么？有什么优势？

// 2. 使用BufferedReader按行读取文件，统计单词数

// 3. 实现一个方法，读取文件并反转每一行后写入另一个文件

// 4. flush()和close()有什么区别？

// 5. BufferedReader的readLine()有什么需要注意的地方？
```

---

## 4.9 参考答案

```java
// 1. 缓冲流原理和优势
// 原理：先操作内存缓冲区，减少系统调用
// 优势：
//   - 减少系统调用次数
//   - 提高IO效率
//   - 批量读写更快

// 2. 统计单词数
public static long countWords(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        return br.lines()
            .flatMap(line -> Arrays.stream(line.split("\\s+")))
            .filter(word -> !word.isEmpty())
            .count();
    }
}

// 或
public static long countWords2(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        long count = 0;
        String line;
        while ((line = br.readLine()) != null) {
            count += line.split("\\s+").length;
        }
        return count;
    }
}

// 3. 反转每行并写入
public static void reverseLines(String input, String output) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(input));
         BufferedWriter bw = new BufferedWriter(new FileWriter(output))) {
        String line;
        while ((line = br.readLine()) != null) {
            bw.write(new StringBuilder(line).reverse().toString());
            bw.newLine();
        }
    }
}

// 4. flush vs close
// flush: 只刷新缓冲区，强制写出数据，流仍然打开
// close: 关闭流，关闭前会先flush，通常只调用一次

// 5. readLine注意事项
// - 返回null表示到达末尾
// - 不包含换行符
// - 不区分不同平台的换行符（都当作\n处理，\r被忽略或单独处理）
```

---

[返回目录](./README.md) | [下一步：对象序列化](./Step05_ObjectStream.md)
