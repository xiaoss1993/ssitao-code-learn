# 步骤2：DateTimeFormatter - 日期时间格式化

---

## 2.1 格式化概述

### 2.1.1 什么是格式化

```java
// 格式化：将日期时间转换为字符串
// 解析：将字符串转换为日期时间

// 旧API问题
// SimpleDateFormat 不是线程安全的！
// 每次使用都要创建新实例

// 新API优势
// DateTimeFormatter 是线程安全的
// 可以静态final共享使用
```

### 2.1.2 预定义格式

```java
import java.time.format.DateTimeFormatter;

// 常用预定义格式
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);

String s1 = dt.format(DateTimeFormatter.ISO_LOCAL_DATE);          // "2024-01-15"
String s2 = dt.format(DateTimeFormatter.ISO_LOCAL_TIME);          // "10:30:45"
String s3 = dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);    // "2024-01-15T10:30:45"

String s4 = dt.format(DateTimeFormatter.BASIC_ISO_DATE);         // "20240115"
String s5 = dt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);   // "2024-01-15T10:30:45+08:00"

// 中文格式
String s6 = dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL));   // 2024年1月15日 星期一 上午10:30:45
String s7 = dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG));    // 2024年1月15日 上午10:30:45
String s8 = dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)); // 2024-1-15 10:30:45
String s9 = dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));  // 2024/1/15 上午10:30
```

---

## 2.2 自定义格式

### 2.2.1 格式模式

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
LocalDate date = LocalDate.of(2024, 1, 15);
LocalTime time = LocalTime.of(10, 30, 45);

// 年月日
date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));    // "2024-01-15"
date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));    // "2024/01/15"
date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")); // "2024年01月15日"
date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));       // "20240115"

// 时分秒
time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));       // "10:30:45"
time.format(DateTimeFormatter.ofPattern("HH:mm"));           // "10:30"
time.format(DateTimeFormatter.ofPattern("hh:mm:ss"));       // "10:30:45" (12小时制)

// 日期时间组合
dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // "2024-01-15 10:30:45"
dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")); // "2024-01-15T10:30:45"
```

### 2.2.2 格式符号说明

```java
/*
格式符号说明：

年:
  yyyy  - 4位年份 (2024)
  yy    - 2位年份 (24)

月:
  MM    - 2位月份 (01-12)
  M     - 1-2位月份 (1-12)
  MMM   - 缩写的月份名 (Jan)
  MMMM  - 完整的月份名 (January)

日:
  dd    - 2位日期 (01-31)
  d     - 1-2位日期 (1-31)
  D     - 一年中的第几天 (1-366)

时:
  HH    - 24小时制，2位 (00-23)
  hh    - 12小时制，2位 (01-12)
  H     - 24小时制 (0-23)
  h     - 12小时制 (1-12)

分:
  mm    - 2位分钟 (00-59)

秒:
  ss    - 2位秒 (00-59)

毫秒:
  SSS   - 3位毫秒 (000-999)

周:
  E     - 星期几缩写 (Mon)
  EE/EEEE - 完整星期名 (Monday)

上午/下午:
  a     - 上午/下午 (AM/PM)

时区:
  XXX   - +08:00 格式
  xxxx  - Asia/Shanghai 格式
*/
```

---

## 2.3 解析字符串

### 2.3.1 解析日期时间

```java
import java.time.format.DateTimeFormatter;

// 解析日期
LocalDate date = LocalDate.parse("2024-01-15");
LocalDate date2 = LocalDate.parse("2024/01/15", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
LocalDate date3 = LocalDate.parse("2024年01月15日", DateTimeFormatter.ofPattern("yyyy年MM月dd日"));

// 解析时间
LocalTime time = LocalTime.parse("10:30:45");
LocalTime time2 = LocalTime.parse("10-30-45", DateTimeFormatter.ofPattern("HH-mm-ss"));

// 解析日期时间
LocalDateTime dt = LocalDateTime.parse("2024-01-15T10:30:45");
LocalDateTime dt2 = LocalDateTime.parse("2024-01-15 10:30:45", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
```

### 2.3.2 解析注意事项

```java
// 1. 格式必须匹配
try {
    LocalDate.parse("2024-01-15", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
} catch (DateTimeParseException e) {
    // 格式不匹配会抛异常
}

// 2. 不严格解析（宽松解析）
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
formatter = formatter.withResolverStyle(ResolverStyle.LENIENT);

// 3. 处理不同语言的月份名
DateTimeFormatter chineseFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.CHINESE);
LocalDate date = LocalDate.parse("2024年1月15日", chineseFormatter);
```

---

## 2.4 中文格式化

### 2.4.1 中文日期格式

```java
LocalDate date = LocalDate.of(2024, 1, 15);

// 中文年月日
date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));  // "2024年01月15日"
date.format(DateTimeFormatter.ofPattern("yyyy年M月d日"));    // "2024年1月15日"

// 中文星期
date.format(DateTimeFormatter.ofPattern("EEEE", Locale.CHINESE));  // "星期一"
date.format(DateTimeFormatter.ofPattern("E", Locale.CHINESE));    // "周一"

// 中文前后缀
DateTimeFormatter dtf = new DateTimeFormatterBuilder()
    .appendValue(ChronoField.YEAR)
    .appendLiteral("年")
    .appendValue(ChronoField.MONTH_OF_YEAR)
    .appendLiteral("月")
    .appendValue(ChronoField.DAY_OF_MONTH)
    .appendLiteral("日")
    .toFormatter(Locale.CHINESE);
```

### 2.4.2 中文时间格式

```java
LocalTime time = LocalTime.of(10, 30, 45);

// 中文时间格式
time.format(DateTimeFormatter.ofPattern("HH时mm分ss秒"));  // "10时30分45秒"
time.format(DateTimeFormatter.ofPattern("上午HH:mm:ss"));  // "上午10:30:45"
```

---

## 2.5 格式化本地化

### 2.5.1 FormatStyle

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);

// FULL: 2024年1月15日 星期一 上午10:30:45 中国标准时间
// LONG: 2024年1月15日 上午10:30:45
// MEDIUM: 2024-1-15 10:30:45
// SHORT: 2024/1/15 上午10:30

System.out.println(dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)));
System.out.println(dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
System.out.println(dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
System.out.println(dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));

// 日期单独格式化
dt.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
```

### 2.5.2 不同地区格式

```java
LocalDate date = LocalDate.of(2024, 1, 15);

// 美国格式
date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US));  // "01/15/2024"

// 欧洲格式
date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.GERMAN));  // "15/01/2024"

// 日本格式
date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.JAPANESE));  // "2024/01/15"
```

---

## 2.6 练习题

```java
// 1. 将 "2024-01-15 10:30:45" 解析为 LocalDateTime

// 2. 将 LocalDateTime 格式化为 "2024年01月15日 10时30分45秒"

// 3. 将 "15/01/2024" (欧洲格式) 解析为 LocalDate

// 4. 格式化当前日期为中文格式

// 5. 实现一个方法，解析 "2024-01-15T10:30:45+08:00" 格式的字符串
```

---

## 2.7 参考答案

```java
// 1. 解析
LocalDateTime dt = LocalDateTime.parse("2024-01-15 10:30:45",
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

// 2. 格式化中文
String result = dt.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"));
// "2024年01月15日 10时30分45秒"

// 3. 解析欧洲格式
LocalDate date = LocalDate.parse("15/01/2024",
    DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.GERMAN));

// 4. 格式化当前日期为中文
LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.CHINESE));

// 5. 解析带时区的字符串
ZonedDateTime zdt = ZonedDateTime.parse("2024-01-15T10:30:45+08:00",
    DateTimeFormatter.ISO_OFFSET_DATE_TIME);
// 或
OffsetDateTime odt = OffsetDateTime.parse("2024-01-15T10:30:45+08:00");
```

---

[返回目录](./README.md) | [下一步：Instant与Duration](./Step03_Instant.md)
