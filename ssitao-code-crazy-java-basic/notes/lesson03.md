# 第3课：字符串处理

## 核心概念

### 3.1 String特性
- **不可变类**：内部char数组被final修饰
- 字符串常量池节省内存
- 常用构造方式：`String s = "hello";` vs `new String("hello");`

### 3.2 StringBuilder vs StringBuffer
| 类 | 线程安全 | 性能 | 使用场景 |
|----|---------|------|----------|
| String | - | 低（每次产生新对象） | 字符串常量 |
| StringBuilder | 不安全 | 高 | 单线程字符串拼接 |
| StringBuffer | 安全 | 中 | 多线程字符串操作 |

### 3.3 正则表达式
- `String.matches()`
- `Pattern` 和 `Matcher` 类
- 常用符号：\d, \w, \s, [], {}, (), ^, $, *, +, ?

## 代码示例

### 示例1：String常用操作
```java
public class StringDemo {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = new String("hello");

        // 常用方法
        System.out.println(s1.length());           // 5
        System.out.println(s1.charAt(0));          // h
        System.out.println(s1.substring(1, 3));    // el
        System.out.println(s1.indexOf("ll"));      // 2
        System.out.println(s1.replace("l", "L"));   // heLLo
        System.out.println(s1.toUpperCase());      // HELLO
        System.out.println("  abc  ".trim());      // abc

        // 比较
        System.out.println(s1.equals(s2));          // true（内容比较）
        System.out.println(s1 == s2);               // false（引用比较）

        // 分割
        String str = "apple,banana,orange";
        String[] fruits = str.split(",");
    }
}
```

### 示例2：StringBuilder高效拼接
```java
public class StringBuilderDemo {
    public static void main(String[] args) {
        // 低效：每次创建新对象
        String bad = "";
        for (int i = 0; i < 100; i++) {
            bad += "test";  // 创建100个新String对象
        }

        // 高效：使用StringBuilder
        StringBuilder good = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            good.append("test");
        }
        System.out.println(good.toString());
    }
}
```

### 示例3：正则表达式
```java
public class RegexDemo {
    public static void main(String[] args) {
        // 验证手机号
        String phone = "13812345678";
        System.out.println(phone.matches("1[3-9]\\d{9}"));

        // 验证邮箱
        String email = "test@example.com";
        System.out.println(email.matches("\\w+@\\w+\\.\\w+"));

        // 使用Pattern和Matcher
        String text = "我的手机号是13812345678，另一个是13987654321";
        Pattern pattern = Pattern.compile("1[3-9]\\d{9}");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("找到：" + matcher.group());
        }

        // 替换
        System.out.println(text.replaceAll("1[3-9]\\d{9}", "***********"));
    }
}
```

### 示例4：字符串格式化
```java
public class StringFormatDemo {
    public static void main(String[] args) {
        String name = "张三";
        int age = 25;
        double height = 1.75;

        // 使用format方法
        String msg = String.format("姓名：%s，年龄：%d，身高：%.2f", name, age, height);
        System.out.println(msg);

        // System.out.printf 直接格式化输出
        System.out.printf("姓名：%s，年龄：%d，身高：%.2f%n", name, age, height);
    }
}
```

## 常见面试题

1. **String s = new String("abc")创建了几个对象？**
   - 可能1个或2个：常量池中"abc"，堆中new String()

2. **String为什么是不可变的？**
   - final修饰的char数组
   - 没有提供修改数组的方法

3. **==和equals的区别？**
   - ==比较引用地址
   - equals默认比较引用，可重写比较内容

## 练习题

1. 实现字符串反转（不能使用reverse）
2. 统计字符串中每个字符出现的次数
3. 验证IP地址格式是否合法
4. 提取字符串中的所有数字

## 要点总结

- String是不可变字符串
- 频繁拼接用StringBuilder
- 正则表达式处理复杂字符串匹配
- 字符串比较用equals
