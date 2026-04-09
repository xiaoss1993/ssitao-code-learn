# 第4课：包装类

## 核心概念

### 4.1 包装类分类
| 基本类型 | 包装类 |
|----------|--------|
| byte | Byte |
| short | Short |
| int | Integer |
| long | Long |
| float | Float |
| double | Double |
| char | Character |
| boolean | Boolean |

### 4.2 自动装箱与拆箱
```java
// 装箱：基本类型 -> 包装类
Integer i = 100;  // 自动装箱

// 拆箱：包装类 -> 基本类型
int n = i;        // 自动拆箱
```

### 4.3 缓存机制
- IntegerCache：默认缓存-128~127
- 其他包装类也有类似机制
- 超出范围会创建新对象

### 4.4 常用方法
```java
Integer.parseInt("123");     // 字符串转int
Integer.valueOf("123");      // 字符串转Integer
Integer.toBinaryString(10);  // 转二进制
Integer.toHexString(10);     // 转十六进制
```

## 代码示例

### 示例1：装箱与拆箱
```java
public class BoxingDemo {
    public static void main(String[] args) {
        // 自动装箱
        Integer i1 = 100;
        Integer i2 = 100;
        System.out.println(i1 == i2);  // true，命中缓存

        Integer i3 = 200;
        Integer i4 = 200;
        System.out.println(i3 == i4);  // false，超出缓存范围

        // 自动拆箱
        int n = i1;  // i1自动拆箱为int
        System.out.println(n);

        // 运算时自动拆箱
        System.out.println(i1 + i2);  // 200，自动拆箱后运算
    }
}
```

### 示例2：类型转换
```java
public class TypeConversionDemo {
    public static void main(String[] args) {
        // String -> 基本类型
        int a = Integer.parseInt("123");
        double b = Double.parseDouble("3.14");
        boolean c = Boolean.parseBoolean("true");

        // 基本类型 -> String
        String s1 = String.valueOf(123);
        String s2 = Integer.toString(123);
        String s3 = 123 + "";  // 利用字符串拼接

        // 进制转换
        System.out.println(Integer.toBinaryString(10));   // 1010
        System.out.println(Integer.toOctalString(10));   // 12
        System.out.println(Integer.toHexString(10));     // a
        System.out.println(Integer.parseInt("1010", 2)); // 10
    }
}
```

### 示例3：处理边界情况
```java
public class BorderCaseDemo {
    public static void main(String[] args) {
        // null值处理
        Integer i = null;
        // i + 1;  // NullPointerException！

        // 字符串转换异常
        try {
            int n = Integer.parseInt("abc");  // NumberFormatException
        } catch (NumberFormatException e) {
            System.out.println("转换失败");
        }

        // 科学计数法
        Double d = Double.parseDouble("1.5e-3");
        System.out.println(d);  // 0.0015
    }
}
```

## 包装类 vs 基本类型

| 方面 | 基本类型 | 包装类 |
|------|----------|--------|
| 值 | 实际值 | 对象 |
| 默认值 | 0/false | null |
| 存储位置 | 栈 | 堆 |
| 泛型支持 | 不支持 | 支持 |
| 运算 | 直接运算 | 自动拆箱后运算 |

## 常见面试题

1. **Integer i = new Integer(128)和Integer i = 128一样吗？**
   - 不同，newInteger总是创建新对象，==比较引用
   - 自动装箱在-128~127范围内返回缓存对象

2. **为什么需要包装类？**
   - Java是面向对象的语言，需要一切皆对象
   - 集合框架不支持基本类型
   - 提供实用方法

3. **下面的代码输出什么？**
   ```java
   Integer a = 1;
   Integer b = 1;
   Integer c = 128;
   Integer d = 128;
   System.out.println(a == b);  // true
   System.out.println(c == d);  // false
   ```

## 练习题

1. 实现一个方法，将字符串数组转换为int数组
2. 统计字符串中数字字符的数量
3. 验证一个字符串是否可以转换为合法数字

## 要点总结

- 包装类将基本类型封装为对象
- 自动装箱/拆箱简化代码
- 注意null值导致的NPE
- 整数包装类有缓存机制
