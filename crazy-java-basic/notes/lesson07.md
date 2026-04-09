# 第7课：异常处理

## 核心概念

### 7.1 异常体系
```
Throwable
  ├── Error（错误） - 程序无法处理
  │     ├── OutOfMemoryError
  │     └── StackOverflowError
  └── Exception（异常）
        ├── RuntimeException（运行时异常） - 可处理可不处理
        │     ├── NullPointerException
        │     ├── ArrayIndexOutOfBoundsException
        │     └── ClassCastException
        └── 非运行时异常（受检异常）- 必须处理
              ├── IOException
              ├── SQLException
              └── FileNotFoundException
```

### 7.2 关键字
- `try` - 监控代码块
- `catch` - 捕获异常
- `finally` - 必定执行的代码块
- `throw` - 抛出异常
- `throws` - 声明异常

## 代码示例

### 示例1：基本异常处理
```java
public class ExceptionDemo1 {
    public static void main(String[] args) {
        try {
            int result = 10 / 0;  // 抛出ArithmeticException
        } catch (ArithmeticException e) {
            System.out.println("除数不能为0");
            e.printStackTrace();
        } finally {
            System.out.println("必定执行");
        }
    }
}
```

### 示例2：多重catch
```java
public class MultiCatchDemo {
    public static void main(String[] args) {
        try {
            String s = null;
            System.out.println(s.length());  // NullPointerException

            int[] arr = {1, 2};
            System.out.println(arr[5]);        // ArrayIndexOutOfBoundsException

        } catch (NullPointerException e) {
            System.out.println("空指针异常");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界异常");
        } catch (Exception e) {
            System.out.println("其他异常");
        }
    }
}
```

### 示例3：throws声明
```java
import java.io.*;

public class ThrowsDemo {
    // 方法声明可能抛出的异常
    public static void readFile() throws IOException {
        FileReader fr = new FileReader("test.txt");
        BufferedReader br = new BufferedReader(fr);
        System.out.println(br.readLine());
        br.close();
        fr.close();
    }

    public static void main(String[] args) {
        try {
            readFile();
        } catch (IOException e) {
            System.out.println("文件读取失败");
        }
    }
}
```

### 示例4：自定义异常
```java
// 自定义异常类
public class AgeException extends Exception {
    public AgeException() {
        super("年龄异常");
    }

    public AgeException(String message) {
        super(message);
    }
}

public class Person {
    private int age;

    public void setAge(int age) throws AgeException {
        if (age < 0 || age > 150) {
            throw new AgeException("年龄必须在0-150之间：" + age);
        }
        this.age = age;
    }

    public static void main(String[] args) {
        Person p = new Person();
        try {
            p.setAge(200);
        } catch (AgeException e) {
            System.out.println(e.getMessage());
        }
    }
}
```

### 示例5：try-with-resources（JDK 7+）
```java
import java.io.*;

public class TryWithResourcesDemo {
    public static void main(String[] args) {
        // 自动关闭资源
        try (FileReader fr = new FileReader("test.txt");
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 示例6：异常链
```java
public class ExceptionChainDemo {
    public static void main(String[] args) {
        try {
            methodA();
        } catch (Exception e) {
            // 打印原始异常
            System.out.println("原始异常：" + e.getMessage());
            // 获取被包装的异常
            Throwable cause = e.getCause();
            if (cause != null) {
                System.out.println("根本原因：" + cause.getMessage());
            }
        }
    }

    public static void methodA() throws Exception {
        try {
            methodB();
        } catch (Exception e) {
            throw new Exception("A方法异常", e);  // 包装原始异常
        }
    }

    public static void methodB() throws Exception {
        throw new Exception("B方法异常");
    }
}
```

## 异常处理原则

| 原则 | 说明 |
|------|------|
| 早抛出，晚捕获 | 检测到异常立即抛出，在合适层级捕获 |
| 精确捕获 | 尽量精确匹配异常类型 |
| 不要压制 | 不要空的catch块 |
| 不要吞掉异常 | 捕获后要处理或重新抛出 |

## 常见面试题

1. **final、finally、finalize的区别？**
   - final：修饰符，表示不可变
   - finally：异常处理块，必定执行
   - finalize：Object方法，GC前调用

2. **try-catch-finally中，如果finally中有return会怎样？**
   - finally的return会覆盖try/catch中的return
   - 不要在finally中return

3. **CheckedException和RuntimeException的区别？**
   - CheckedException：受检异常，编译器检查，必须处理
   - RuntimeException：运行时异常，可处理可不处理

## 练习题

1. 写一个方法验证年龄，超过范围抛出自定义异常
2. 使用异常链传递底层异常信息
3. 实现try-with-resources关闭多个资源

## 要点总结

- Throwable是异常基类
- Error无法处理，Exception可处理
- 异常处理保证程序不崩溃
- 自定义异常用于特定业务场景
- finally总是执行（除System.exit外）
