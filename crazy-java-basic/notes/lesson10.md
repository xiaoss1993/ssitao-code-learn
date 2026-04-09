# 第10课：I/O流

## 核心概念

### 10.1 I/O分类
```
I/O流
  ├── 字节流
  │     ├── InputStream（输入）
  │     │     └── FileInputStream, BufferedInputStream, ObjectInputStream
  │     └── OutputStream（输出）
  │           └── FileOutputStream, BufferedOutputStream, ObjectOutputStream
  └── 字符流
        ├── Reader（输入）
        │     └── FileReader, BufferedReader, InputStreamReader
        └── Writer（输出）
              └── FileWriter, BufferedWriter, OutputStreamWriter
```

### 10.2 装饰器模式
- 字节流与字符流转换：InputStreamReader / OutputStreamWriter
- 缓冲流：BufferedInputStream / BufferedReader
- 对象流：ObjectInputStream / ObjectOutputStream

### 10.3 序列化
- 实现Serializable接口
- transient关键字排除不需要序列化的属性
- 序列化版本号：serialVersionUID

## 代码示例

### 示例1：文件字节流复制
```java
import java.io.*;

public class FileCopyDemo {
    public static void main(String[] args) throws IOException {
        try (FileInputStream fis = new FileInputStream("source.txt");
             FileOutputStream fos = new FileOutputStream("target.txt")) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }
    }
}
```

### 示例2：字符流读写
```java
import java.io.*;

public class CharStreamDemo {
    public static void main(String[] args) throws IOException {
        // 使用FileReader/FileWriter（自动字符编码转换）
        try (FileReader fr = new FileReader("input.txt");
             FileWriter fw = new FileWriter("output.txt")) {

            char[] buffer = new char[1024];
            int len;
            while ((len = fr.read(buffer)) != -1) {
                fw.write(buffer, 0, len);
            }
        }
    }
}
```

### 示例3：缓冲流高效读写
```java
import java.io.*;

public class BufferedIODemo {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        // 不使用缓冲流
        try (FileInputStream fis = new FileInputStream("source.txt");
             FileOutputStream fos = new FileOutputStream("target.txt")) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + "ms");
    }
}
```

### 示例4：对象序列化
```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjectSerializationDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Person> people = new ArrayList<>();
        people.add(new Person("张三", 25));
        people.add(new Person("李四", 30));

        // 序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("people.dat"))) {
            oos.writeObject(people);
        }

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("people.dat"))) {
            List<Person> restored = (List<Person>) ois.readObject();
            System.out.println(restored);
        }
    }
}

class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    // transient不参与序列化
    private transient String password;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}
```

### 示例5：Scanner和Console
```java
import java.io.*;
import java.util.Scanner;

public class InputDemo {
    public static void main(String[] args) {
        // 使用Scanner读取各种类型
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入姓名：");
        String name = scanner.nextLine();
        System.out.print("请输入年龄：");
        int age = scanner.nextInt();
        System.out.print("请输入身高：");
        double height = scanner.nextDouble();

        System.out.println("姓名：" + name + "，年龄：" + age + "，身高：" + height);

        // 使用Console（密码输入）
        Console console = System.console();
        if (console != null) {
            String username = console.readLine("用户名：");
            char[] password = console.readPassword("密码：");
            console.printf("欢迎，%s%n", username);
        }
    }
}
```

### 示例6：NIO文件操作
```java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class NIODemo {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("test.txt");

        // 写入
        Files.write(path, "Hello NIO".getBytes(StandardCharsets.UTF_8));

        // 读取
        String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        System.out.println(content);

        // 按行读取
        Files.lines(path, StandardCharsets.UTF_8)
             .forEach(System.out::println);

        // 复制文件
        Files.copy(path, Paths.get("test_copy.txt"));

        // 删除文件
        // Files.deleteIfExists(Paths.get("test_copy.txt"));
    }
}
```

## 常见面试题

1. **字节流和字符流的区别？**
   - 字节流处理二进制数据
   - 字符流处理文本字符，自动编码转换
   - 文本文件用字符流，二进制文件用字节流

2. **BufferedReader的readLine()为什么比普通Reader高效？**
   - BufferedReader有内部缓冲区
   - 减少底层读取次数

3. **什么是序列化？什么场景需要序列化？**
   - 对象转换为字节流
   - 网络传输、对象持久化、缓存

## 练习题

1. 实现文件复制功能（使用不同流）
2. 序列化一个学生对象，包含姓名、年龄、课程列表
3. 使用NIO读取大文件并统计行数

## 要点总结

- I/O流分字节流和字符流
- 使用缓冲流提高效率
- 对象序列化需实现Serializable
- NIO提供了更高效的IO操作
- 注意资源关闭（try-with-resources）
