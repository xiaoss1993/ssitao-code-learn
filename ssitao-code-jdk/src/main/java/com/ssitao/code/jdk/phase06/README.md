# IO专题：输入输出

## 学习目标

掌握Java IO流体系，理解字节流、字符流、缓冲流、NIO的原理和使用场景。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | IO概述 | [Step01_IOOverview.md](./Step01_IOOverview.md) | - |
| 2 | 字节流 | [Step02_ByteStream.md](./Step02_ByteStream.md) | [basic/*.java](./basic/) |
| 3 | 字符流 | [Step03_CharStream.md](./Step03_CharStream.md) | [basic/*.java](./basic/) |
| 4 | 缓冲流 | [Step04_BufferedStream.md](./Step04_BufferedStream.md) | [basic/*.java](./basic/) |
| 5 | 对象序列化 | [Step05_ObjectStream.md](./Step05_ObjectStream.md) | [object/*.java](./object/) |
| 6 | NIO | [Step06_NIO.md](./Step06_NIO.md) | [nio/*.java](./nio/) |

---

## 核心概念概览

### IO分类

```
IO流
├── 字节流
│   ├── InputStream
│   │    ├── FileInputStream
│   │    ├── BufferedInputStream
│   │    └── ByteArrayInputStream
│   └── OutputStream
│        ├── FileOutputStream
│        ├── BufferedOutputStream
│        └── ByteArrayOutputStream
│
├── 字符流
│   ├── Reader
│   │    ├── FileReader
│   │    ├── BufferedReader
│   │    └── InputStreamReader
│   └── Writer
│        ├── FileWriter
│        ├── BufferedWriter
│        └── OutputStreamWriter
│
└── NIO
    ├── Channel
    ├── Buffer
    └── Selector
```

### 核心类

```
FileInputStream/FileOutputStream    - 文件字节流
FileReader/FileWriter              - 文件字符流
BufferedInputStream/BufferedOutputStream - 缓冲字节流
BufferedReader/BufferedWriter     - 缓冲字符流
InputStreamReader/OutputStreamWriter - 转换流
ObjectInputStream/ObjectOutputStream - 对象流
ByteArrayInputStream/ByteArrayOutputStream - 字节数组流
```

---

## 学习建议

1. **IO概述**: 理解IO分类和装饰器模式
2. **字节流/字符流**: 掌握区别和使用场景
3. **缓冲流**: 理解为什么要缓冲
4. **NIO**: 理解Channel/Buffer模型

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase06.basic.ByteStreamDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase06.basic.CharStreamDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase06.nio.NIODemo"
```
