# 步骤3：包装类 - 基本类型与对象之间的桥梁

---

## 3.1 包装类概述

### 3.1.1 Java的8种基本类型和对应的包装类

| 基本类型 | 包装类 | 字节数 | 取值范围 |
|---------|--------|--------|----------|
| byte | Byte | 1 | -128 ~ 127 |
| short | Short | 2 | -32768 ~ 32767 |
| int | Integer | 4 | -2^31 ~ 2^31-1 |
| long | Long | 8 | -2^63 ~ 2^63-1 |
| float | Float | 4 | IEEE 754 |
| double | Double | 8 | IEEE 754 |
| char | Character | 2 | 0 ~ 65535 |
| boolean | Boolean | 1 | true / false |

### 3.1.2 包装类的层次结构

```java
// 所有数值型包装类继承自Number
public abstract class Number implements java.io.Serializable {
    public abstract int intValue();
    public abstract long longValue();
    public abstract float floatValue();
    public abstract double doubleValue();
    // ...
}

// Boolean和Character直接继承Object
public final class Boolean implements Serializable, Comparable<Boolean> {
    private final boolean value;
}

public final class Character implements Serializable, Comparable<Character> {
    private final char value;
}
```

---

## 3.2 自动装箱与拆箱

### 3.2.1 概念

```java
// 自动装箱: 基本类型 -> 包装类型
Integer i = 10;  // 编译后: Integer i = Integer.valueOf(10);

// 自动拆箱: 包装类型 -> 基本类型
int j = i;       // 编译后: int j = i.intValue();
```

### 3.2.2 装箱与拆箱的原理

```java
// valueOf() 使用缓存,避免频繁创建对象
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}

// IntegerCache缓存范围: -128 ~ 127 (可配置)
static {
    int low = -128;
    int high = 127;
    // 预创建Integer对象缓存
}
```

### 3.2.3 装箱的陷阱

```java
// 陷阱1: 装箱类型比较要用equals
Integer a = 127;
Integer b = 127;
System.out.println(a == b);  // true (缓存范围内,同一引用)

Integer c = 128;
Integer d = 128;
System.out.println(c == d);  // false (超出缓存,不同对象)

// 陷阱2: 混合类型比较
Integer e = 1;
Double f = 1.0;
System.out.println(e == f);  // 编译错误! 不能用==

// 陷阱3: 装箱类型参与运算
Integer g = 0;
g++;  // 实际: g = Integer.valueOf(g.intValue() + 1);
```

---

## 3.3 Integer类详解

### 3.3.1 核心常量

```java
public final class Integer extends Number implements Comparable<Integer> {
    // 基本值
    private final int value;

    // 常用常量
    public static final int MIN_VALUE = 0x80000000;  // -2147483648
    public static final int MAX_VALUE = 0x7fffffff;   // 2147483647

    // 字节数
    public static final int SIZE = 32;  // 32位

    // 字节数组长度
    public static final int BYTES = SIZE / Byte.SIZE;  // 4

    // 类型
    public static final Class<Integer> TYPE = int.class;
}
```

### 3.3.2 常用方法

```java
// 构造方法 (已废弃,建议使用valueOf)
Integer i = new Integer(10);  // 不推荐,每次创建新对象

// 推荐使用valueOf
Integer i = Integer.valueOf(10);

// 转换为基本类型
i.intValue();          // int
i.longValue();         // long
i.doubleValue();       // double

// 字符串转换
Integer.parseInt("123");        // "123" -> 123
Integer.parseInt("FF", 16);      // 十六进制"FF" -> 255
Integer.parseInt("1010", 2);     // 二进制"1010" -> 10

Integer.toString(123);          // 123 -> "123"
Integer.toBinaryString(10);     // 10 -> "1010"
Integer.toHexString(255);      // 255 -> "ff"
Integer.toOctalString(8);      // 8 -> "10"

// 其他方法
Integer.bitCount(10);           // 10的二进制中1的个数 -> 2
Integer.compare(10, 20);        // 比较 -> -1
Integer.compareUnsigned(10, 20); // 无符号比较 -> -1
Integer.divideExact(10, 3);      // 精确除法,不支持返回小数
Integer.max(10, 20);             // 最大值 -> 20
Integer.min(10, 20);             // 最小值 -> 10
Integer.sum(10, 20);             // 求和 -> 30
Integer.highestOneBit(10);       // 最高位1 -> 8
Integer.lowestOneBit(10);        // 最低位1 -> 2
```

### 3.3.3 位运算方法

```java
// 位旋转
Integer.rotateLeft(10, 2);   // 00001010 -> 00101000 = 40
Integer.rotateRight(10, 2); // 00001010 -> 10000010 = 130

// 位反转
Integer.reverse(10);         // 00001010 -> 01010000 = 80

// 前导零/尾随零
Integer.numberOfLeadingZeros(10);   // 10前有多少个0 -> 28
Integer.numberOfTrailingZeros(10); // 10后有多少个0 -> 1
```

---

## 3.4 Long类详解

```java
// Long类的特殊方法
Long.hashCode(10L);              // long的hashCode
Long.numberOfLeadingZeros(10L);  // 前导零
Long.numberOfTrailingZeros(10L); // 尾随零
Long.bitCount(10L);              // 1的个数

// 无符号运算
Long.compareUnsigned(10L, 20L);  // 无符号比较
Long.divideUnsigned(10L, 3L);   // 无符号除法
Long.remainderUnsigned(10L, 3L);// 无符号取余

// 字符串转换
Long.parseLong("123");
Long.parseLong("FF", 16);
Long.toBinaryString(10L);
Long.toUnsignedString(10L);
```

---

## 3.5 Boolean类

```java
// Boolean常量
Boolean.TRUE;
Boolean.FALSE;

// 字符串转换
Boolean.parseBoolean("true");     // true (不区分大小写)
Boolean.parseBoolean("TRUE");     // true
Boolean.parseBoolean("false");    // false
Boolean.parseBoolean("anything"); // false

// 值为true/false的对象
Boolean.valueOf(true);
Boolean.valueOf("true");

// 逻辑运算
Boolean.logicalAnd(true, false);  // false
Boolean.logicalOr(true, false);   // true
Boolean.logicalXor(true, false);  // true
```

---

## 3.6 Character类

### 3.6.1 字符判断

```java
Character.isLetter('a');        // 是否为字母 -> true
Character.isDigit('5');         // 是否为数字 -> true
Character.isLetterOrDigit('a'); // 是否为字母或数字 -> true
Character.isWhitespace(' ');    // 是否为空格 -> true
Character.isUpperCase('A');     // 是否为大写 -> true
Character.isLowerCase('a');     // 是否为小写 -> true

// JDK 8+
Character.isAlphabetic('a');    // 是否为字母
Character.isIdeographic('中');  // 是否为表意文字
```

### 3.6.2 字符转换

```java
Character.toUpperCase('a');     // 'A'
Character.toLowerCase('A');     // 'a'
Character.toTitleCase('a');     // 'A' (Title Case用于首字母大写)

Character.reverseBytes('a');    // 字节反转
```

### 3.6.3 Unicode相关

```java
Character.codePointAt("hello", 0);  // 字符的Unicode码点
Character.codePointBefore("hello", 1); // 前一个字符的码点
Character.codePointCount("hello", 0, 3); // 子串的码点数量

// Unicode区块判断
Character.isIdeographic('\u4E00'); // 中文字符
Character.isMirrored('(');         // 是否为镜像字符
```

---

## 3.7 自动装箱的坑

### 3.7.1 性能问题

```java
// 反例: 循环中使用装箱类型
Long sum = 0L;  // 使用基本类型long sum = 0;
for (Long i = 0L; i < 1000000L; i++) {
    sum += i;  // 每次循环都创建新Long对象!
}
// 正例: 使用基本类型
long sum = 0L;
for (long i = 0; i < 1000000L; i++) {
    sum += i;
}
```

### 3.7.2 空指针问题

```java
Integer i = null;
int j = i;  // 自动拆箱: j = i.intValue()
            // 抛出 NullPointerException!

// 防御代码
if (i != null) {
    int j = i;
}
```

### 3.7.3 equals和==混淆

```java
Integer a = 100;
Integer b = 100;
Integer c = 200;
Integer d = 200;

a == b;  // false (超出缓存范围-128~127)
a.equals(b); // true

c == d;  // false
c.equals(d); // true

// 最佳实践: 始终使用equals比较包装类型
Objects.equals(a, b);  // true
```

---

## 3.8 数值计算工具类

### 3.8.1 Math类

```java
// 常用常量
Math.PI;  // 3.141592653589793
Math.E;   // 2.718281828459045

// 取整
Math.floor(3.7);   // 3.0 (向下取整)
Math.ceil(3.2);    // 4.0 (向上取整)
Math.round(3.5);   // 4 (四舍五入)
Math.rint(3.5);    // 4.0 (返回最接近的double)

 // 绝对值
Math.abs(-10);     // 10
Math.absExact(-10); // 10,溢出时抛异常

// 最大最小值
Math.max(10, 20);  // 20
Math.min(10, 20);  // 10

// 幂和开方
Math.pow(2, 3);    // 8.0 (2的3次方)
Math.sqrt(16);     // 4.0 (开平方)
Math.cbrt(27);     // 3.0 (开立方)

// 对数
Math.log(10);      // 自然对数
Math.log10(100);   // 以10为底的对数
Math.log1p(10);    // ln(1+x),高精度

// 三角函数
Math.sin(Math.PI / 2);  // 1.0
Math.cos(Math.PI);      // -1.0
Math.tan(Math.PI / 4);  // 1.0

// 弧度角度转换
Math.toDegrees(Math.PI);    // 180.0
Math.toRadians(180);       // PI

// 随机数
Math.random();  // [0, 1) 的double

// 符号函数
Math.signum(-10);  // -1.0
Math.signum(10);   // 1.0
Math.signum(0);    // 0.0
```

### 3.8.2 StrictMath类

```java
// StrictMath保证跨平台结果一致
// Math可能在不同平台优化,结果略有差异
// StrictMath始终返回相同结果
StrictMath.sin(Math.PI);
StrictMath.cos(Math.PI);
```

---

## 3.9 练习题

```java
// 1. 实现一个方法,将Integer列表转换为int数组

// 2. 实现一个方法,找出整数数组中的最大值(不能使用排序)

// 3. 判断一个字符串是否为有效的整数表示

// 4. 实现精确的浮点数计算 (不能用double)

// 5. BigDecimal实现加减乘除
```

---

## 3.10 参考答案

```java
// 1. Integer列表转int数组
public static int[] toIntArray(List<Integer> list) {
    return list.stream().mapToInt(Integer::intValue).toArray();
}

// 2. 找最大值
public static int findMax(int[] arr) {
    int max = arr[0];
    for (int i = 1; i < arr.length; i++) {
        if (arr[i] > max) {
            max = arr[i];
        }
    }
    return max;
}

// 3. 判断有效整数
public static boolean isValidInteger(String s) {
    try {
        Integer.parseInt(s);
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
```

---

[返回目录](./README.md) | [下一步：常用工具类](./Step04_Utils.md)
