# 步骤2：String家族 - 不可变字符串、可变字符串、字符串拼接

---

## 2.1 String类的特性

### 2.1.1 String是不可变类

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    // JDK 8及之前
    private final char[] value;

    // JDK 9+ 使用byte数组存储,更节省内存
    private final byte[] value;
    private final boolean isLatin1;
}
```

**String不可变的原因**：
1. `final`类 - 不能被继承
2. `final` char[]/byte[] - 字符数组不能被修改
3. 没有提供setter方法修改内部值

### 2.1.2 String的创建方式

```java
// 1. 字面量方式 - 放在字符串常量池中
String s1 = "hello";

// 2. new String()方式 - 放在堆内存中
String s2 = new String("hello");

// 3. 字符串拼接
String s3 = "he" + "llo";  // 编译器优化为"hello"

// 4. 从字符数组创建
char[] chars = {'h', 'e', 'l', 'l', 'o'};
String s4 = new String(chars);
```

### 2.1.3 字符串常量池

```
字符串常量池(String Constant Pool)是方法区的一部分

字面量创建:
  String s1 = "hello";  // 检查常量池,没有则创建并放入

new String创建:
  String s2 = new String("hello");
  // 1. 在常量池中创建"hello"
  // 2. 在堆中创建String对象,引用常量池的"hello"

intern():
  s2.intern();  // 如果常量池中有"hello",返回常量池的引用
               // 如果没有,添加到常量池并返回引用
```

---

## 2.2 String常用方法

### 2.2.1 基础操作

```java
String str = "Hello World";

// 长度
str.length();  // 11

// 判空
str.isEmpty();  // false
"".isEmpty();   // true

// 获取字符
str.charAt(0);  // 'H'
str.charAt(6);  // 'W'

// 获取字符编码
str.codePointAt(0);  // 72 (Unicode)

// 转换为字符数组
char[] chars = str.toCharArray();

// 格式化
String.format("Hello %s", "World");  // "Hello World"
```

### 2.2.2 字符串比较

```java
String s1 = "hello";
String s2 = "hello";
String s3 = "Hello";
String s4 = new String("hello");

// == 比较引用地址
s1 == s2;              // true (字面量,同一引用)
s1 == s4;              // false (new String,不同引用)

// equals 比较内容
s1.equals(s2);         // true
s1.equals(s3);         // false (区分大小写)
s1.equalsIgnoreCase(s3); // true (忽略大小写)

// compareTo 比较字典顺序
s1.compareTo(s3);       // 返回正数 ('h' > 'H')
"abc".compareTo("abd"); // -1 ('c' < 'd')
```

### 2.2.3 字符串查找与判断

```java
String str = "Hello World, Hello Java";

// 包含判断
str.contains("World");        // true
str.contains("world");        // false (区分大小写)

// 开头/结尾判断
str.startsWith("Hello");      // true
str.startsWith("World", 6);   // true (从索引6开始)
str.endsWith("Java");         // true

// 索引查找
str.indexOf("Hello");         // 0 (首次出现)
str.indexOf("Hello", 7);      // 13 (从索引7开始查找)
str.lastIndexOf("Hello");      // 13 (最后一次出现)

// 判断正则
str.matches("\\w+");          // false (包含逗号和空格)
"hello".matches("[a-z]+");    // true
```

### 2.2.4 字符串截取与分割

```java
String str = "Hello World";

// 截取子串
str.substring(6);           // "World" (从索引6到末尾)
str.substring(6, 11);       // "World" (左闭右开)

// 分割
str.split(" ");             // ["Hello", "World"]
str.split("\\s+");          // ["Hello", "World"] (按空白符分割)
str.split(",", 2);          // ["Hello World", " Hello Java"] (限制分割次数)

// 拼接
String.join("-", "a", "b", "c");  // "a-b-c"
String.join("-", Arrays.asList("a", "b", "c"));  // "a-b-c"
```

### 2.2.5 字符串替换

```java
String str = "Hello World";

// 替换第一个
str.replaceFirst("l", "L");   // "HeLlo World"

// 替换所有
str.replace("l", "L");        // "HeLLo WorLd"

// 替换字符
str.replace('o', 'O');        // "HellO WOrld"

// 替换字符序列
str.replaceAll("\\s+", "-");  // "Hello-World"
str.replaceFirst("\\s+", "-"); // "Hello-World"

// JDK 12+ 简化版
str.transform(s -> s.toUpperCase());  // "HELLO WORLD"
```

### 2.2.6 大小写转换

```java
String str = "Hello World";

// 转大写
str.toUpperCase();           // "HELLO WORLD"

// 转小写
str.toLowerCase();           // "hello world"

// 首字母大写
str.substring(0, 1).toUpperCase() + str.substring(1);  // "Hello world"

// 单词首字母大写
Arrays.stream(str.split("\\s+"))
      .map(s -> s.substring(0,1).toUpperCase() + s.substring(1))
      .collect(Collectors.joining(" "));  // "Hello World"
```

### 2.2.7 去空白与对齐

```java
String str = "  Hello World  ";

// 去空白
str.trim();                  // "Hello World" (去两端ASCII空白)
str.strip();                 // "Hello World" (去两端Unicode空白,JDK 11+)
str.stripLeading();          // "Hello World  " (只去前端)
str.stripTrailing();         // "  Hello World" (只去后端)

// 对齐
str.repeat(3);               // "  Hello World  Hello World  Hello World  " (JDK 11+)
"hello".padStart(10, '-');   // "-----hello"
"hello".padEnd(10, '-');     // "hello-----"
```

### 2.2.8 字符串与其他类型转换

```java
// int -> String
String.valueOf(123);         // "123"
Integer.toString(123);        // "123"
"" + 123;                    // "123" (不推荐)

// String -> int
Integer.parseInt("123");     // 123
Integer.valueOf("123");      // 123 (返回Integer对象)

// String -> byte[]
"hello".getBytes();          // UTF-8编码的byte数组
"hello".getBytes(StandardCharsets.UTF_8);

// byte[] -> String
new String(bytes, StandardCharsets.UTF_8);

// String -> char[]
"hello".toCharArray();       // ['h','e','l','l','o']

// String -> boolean
Boolean.parseBoolean("true"); // true
Boolean.parseBoolean("FALSE"); // true (不区分大小写)
```

---

## 2.3 StringBuilder vs StringBuffer

### 2.3.1 核心区别

| 特性 | StringBuilder | StringBuffer |
|------|---------------|--------------|
| 线程安全 | 否 | 是 |
| 性能 | 高 | 低(有synchronized) |
| JDK版本 | JDK 5+ | JDK 1.0+ |
| 推荐场景 | 单线程环境 | 多线程环境 |

### 2.3.2 StringBuilder源码分析

```java
public final class StringBuilder
    extends AbstractStringBuilder
    implements java.io.Serializable, Comparable<StringBuilder>, CharSequence {

    // 继承自AbstractStringBuilder
    char[] value;  // 存储字符的数组
    int count;     // 已使用字符数量

    // 默认容量
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    // 构造方法
    public StringBuilder() {
        super(16);  // 默认容量16
    }

    public StringBuilder(String str) {
        super(str.length() + 16);  // 容量 = 字符串长度 + 16
    }

    public StringBuilder(int capacity) {
        super(capacity);
    }

    // 追加 - 核心方法
    @Override
    public StringBuilder append(String str) {
        super.append(str);
        return this;  // 支持链式调用
    }

    // toString
    @Override
    public String toString() {
        return new String(value, 0, count);
    }
}
```

### 2.3.3 AbstractStringBuilder

```java
abstract class AbstractStringBuilder {
    char[] value;
    int count;

    // 扩容机制
    private int newCapacity(int minCapacity) {
        int newCapacity = (value.length << 1) + 2;  // 2倍+2
        if (newCapacity < 0) {
            return Integer.MAX_VALUE;
        }
        return Math.max(minCapacity, newCapacity);
    }

    // 追加实现
    public AbstractStringBuilder append(String str) {
        if (str == null) {
            appendNull();
        } else {
            int len = str.length();
            ensureCapacityInternal(count + len);
            str.getChars(0, len, value, count);
            count += len;
        }
        return this;
    }

    // 容量检查
    private void ensureCapacityInternal(int minimumCapacity) {
        if (minimumCapacity > value.length) {
            grow(minimumCapacity);
        }
    }

    // 扩容
    void grow(int minCapacity) {
        int oldCapacity = value.length;
        int newCapacity = (oldCapacity << 1) + 2;
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        value = Arrays.copyOf(value, newCapacity);
    }
}
```

### 2.3.4 扩容机制

```
初始容量: 16
第一次扩容: 16 * 2 + 2 = 34
第二次扩容: 34 * 2 + 2 = 70
...

公式: newCapacity = oldCapacity * 2 + 2
```

---

## 2.4 字符串拼接的细节

### 2.4.1 编译期优化

```java
// 编译期优化 - 常量折叠
String s1 = "a" + "b" + "c";  // 编译后变成 "abc"
String s2 = 1 + 2 + "c";      // 编译后变成 "3c"

// 变量拼接 - 不能优化
String a = "a";
String s3 = a + "b";         // 编译后使用 StringBuilder
```

### 2.4.2 编译后 bytecode

```java
// 源代码
String s = "a" + "b";

// 编译后 (近似)
String s = new StringBuilder().append("a").append("b").toString();
```

### 2.4.3 字符串拼接对比

```java
// 效率: StringBuilder > StringBuffer > String

// 1. 少量拼接 - StringBuilder
String result = new StringBuilder()
    .append("hello")
    .append(" ")
    .append("world")
    .toString();

// 2. 循环拼接 - 必须用StringBuilder
String[] arr = {"a", "b", "c", "d", "e"};
StringBuilder sb = new StringBuilder();
for (String s : arr) {
    sb.append(s).append(",");
}
String result2 = sb.toString();

// 3. 避免循环中使用+
for (int i = 0; i < 1000; i++) {
    s = s + i;  // 每次循环创建新的StringBuilder,效率极低!
}
```

### 2.4.4 StringJoiner (JDK 8+)

```java
import java.util.StringJoiner;

// 基本用法
StringJoiner sj = new StringJoiner(", ");
sj.add("a").add("b").add("c");
System.out.println(sj.toString());  // "a, b, c"

// 带前缀后缀
StringJoiner sj2 = new StringJoiner(", ", "[", "]");
sj2.add("a").add("b").add("c");
System.out.println(sj2.toString());  // "[a, b, c]"

// 与Stream配合
List<String> list = Arrays.asList("a", "b", "c");
String result = list.stream().collect(Collectors.joining(", "));
```

---

## 2.5 字符串常见面试题

### 2.5.1 String s = new String("abc") 创建了几个对象？

```java
String s = new String("abc");

// 可能创建1个或2个对象:
// 1. 如果常量池中已有"abc",只创建1个(堆中)
// 2. 如果常量池中没有"abc",创建2个(常量池+堆中)
```

### 2.5.2 String为什么是不可变的？

```java
// 1. final class - 不能被继承修改
public final class String { ... }

// 2. private final char[] value - 字符数组不可修改
private final char[] value;

// 3. 没有提供修改value的方法
//    只有substring, replace, toUpperCase 等返回新String的方法
```

### 2.5.3 String、StringBuilder、StringBuffer选哪个？

| 场景 | 推荐 |
|------|------|
| 字符串不可变场景 | String |
| 单线程大量字符串拼接 | StringBuilder |
| 多线程字符串拼接 | StringBuffer |
| 列表元素拼接 | StringJoiner/Collectors.joining |

---

## 2.6 练习题

```java
// 1. 反转字符串 "hello" -> "olleh"

// 2. 判断是否为回文 "abcba" -> true, "abc" -> false

// 3. 统计字符串中每个字符出现的次数

// 4. 实现字符串压缩 "aaabbbcc" -> "a3b3c2"

// 5. String s1 = "abc"; String s2 = new String("abc");
//    s1 == s2 ? s1.equals(s2) ? s2 == s2.intern() ?
```

---

## 2.7 参考答案

```java
// 1. 反转字符串
public static String reverse(String str) {
    return new StringBuilder(str).reverse().toString();
}

// 2. 判断回文
public static boolean isPalindrome(String str) {
    int left = 0, right = str.length() - 1;
    while (left < right) {
        if (str.charAt(left++) != str.charAt(right--)) {
            return false;
        }
    }
    return true;
}

// 3. 统计字符出现次数
public static Map<Character, Long> countChars(String str) {
    return str.chars()
              .mapToObj(c -> (char) c)
              .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
}

// 4. 字符串压缩
public static String compress(String str) {
    StringBuilder sb = new StringBuilder();
    int count = 1;
    for (int i = 1; i < str.length(); i++) {
        if (str.charAt(i) == str.charAt(i - 1)) {
            count++;
        } else {
            sb.append(str.charAt(i - 1)).append(count);
            count = 1;
        }
    }
    sb.append(str.charAt(str.length() - 1)).append(count);
    return sb.toString();
}
```

---

[返回目录](./README.md) | [下一步：包装类](./Step03_Wrapper.md)
