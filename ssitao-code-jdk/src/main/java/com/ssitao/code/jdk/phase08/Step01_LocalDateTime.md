# 步骤1：LocalDateTime - 本地日期时间

---

## 1.1 旧API的问题

### 1.1.1 java.util.Date的问题

```java
// 旧API的坑

// 1. 年份从1900开始
Date date1 = new Date(100, 1, 1);  // 实际是2000年
// 正确做法
Date date2 = new Date(2024 - 1900, 1, 1);

// 2. 月份从1开始但内部是0-11
date1.setMonth(0);   // 实际是1月
date1.setMonth(11);  // 实际是12月

// 3. 可变对象，线程不安全
Date date = new Date();
date.setTime(0);  // 改变了原来的对象！

// 4. toString不直观
System.out.println(date);
// 输出: Fri Jan 01 00:00:00 CST 2026
// CST是北京时区，不是字符串"CST"
```

### 1.1.2 java.util.Calendar的问题

```java
// Calendar的坑

Calendar cal = Calendar.getInstance();

// 月份是0-11（反人类！）
cal.set(2024, Calendar.JANUARY, 1);  // 设置为1月
// 或
cal.set(2024, 0, 1);  // 0代表1月，容易出错

// 年份也有类似问题
cal.set(100, 0, 1);  // 实际是2000年

// 可变对象
Calendar cal2 = cal;
cal2.add(Calendar.DAY_OF_MONTH, 10);  // cal也被改变了！
```

---

## 1.2 LocalDateTime概述

### 1.2.1 什么是LocalDateTime

```java
// LocalDateTime = LocalDate + LocalTime
// 表示本地日期时间，不带时区信息

// 优点：
// - 不可变对象，线程安全
// - API清晰易懂
// - 年月日时分秒从1/正常开始
```

### 1.2.2 获取实例

```java
// 1. 获取当前日期时间
LocalDateTime now = LocalDateTime.now();
System.out.println(now);  // 2024-01-15T10:30:45

// 2. 指定日期时间
LocalDateTime dt1 = LocalDateTime.of(2024, 1, 15, 10, 30);
LocalDateTime dt2 = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
LocalDateTime dt3 = LocalDateTime.of(2024, 1, 15, 10, 30, 45, 123456789);

// 3. 从LocalDate和LocalTime组合
LocalDate date = LocalDate.of(2024, 1, 15);
LocalTime time = LocalTime.of(10, 30, 45);
LocalDateTime dt4 = LocalDateTime.of(date, time);

// 4. 解析字符串
LocalDateTime dt5 = LocalDateTime.parse("2024-01-15T10:30:45");
```

---

## 1.3 LocalDate

### 1.3.1 基本使用

```java
// 获取当前日期
LocalDate today = LocalDate.now();
System.out.println(today);  // 2024-01-15

// 指定日期
LocalDate date = LocalDate.of(2024, 1, 15);
LocalDate date2 = LocalDate.of(2024, Month.JANUARY, 15);

// 解析字符串
LocalDate parsed = LocalDate.parse("2024-01-15");

// 获取年月日
int year = date.getYear();           // 2024
int month = date.getMonthValue();    // 1
int day = date.getDayOfMonth();      // 15
DayOfWeek dow = date.getDayOfWeek(); // MONDAY

// 也可以用枚举
Month month2 = date.getMonth();      // JANUARY
```

### 1.3.2 日期计算

```java
LocalDate date = LocalDate.of(2024, 1, 15);

// 加减日期
LocalDate tomorrow = date.plusDays(1);
LocalDate nextWeek = date.plusWeeks(1);
LocalDate nextMonth = date.plusMonths(1);
LocalDate nextYear = date.plusYears(1);

LocalDate yesterday = date.minusDays(1);

// 快捷方法
LocalDate firstDayOfMonth = date.withDayOfMonth(1);
LocalDate lastDayOfMonth = date.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

// 比较日期
boolean isAfter = date.isAfter(otherDate);
boolean isBefore = date.isBefore(otherDate);
boolean isEqual = date.isEqual(otherDate);

// 是否是闰年
boolean isLeap = date.isLeapYear();  // 2024是闰年
```

---

## 1.4 LocalTime

### 1.4.1 基本使用

```java
// 获取当前时间
LocalTime now = LocalTime.now();
System.out.println(now);  // 10:30:45.123456789

// 指定时间
LocalTime time = LocalTime.of(10, 30);
LocalTime time2 = LocalTime.of(10, 30, 45);
LocalTime time3 = LocalTime.of(10, 30, 45, 123456789);

// 解析字符串
LocalTime parsed = LocalTime.parse("10:30:45");

// 获取时分秒
int hour = time.getHour();      // 10
int minute = time.getMinute();   // 30
int second = time.getSecond();   // 45
int nano = time.getNano();      // 123456789
```

### 1.4.2 时间计算

```java
LocalTime time = LocalTime.of(10, 30, 45);

// 加减时间
LocalTime later = time.plusHours(2);
LocalTime earlier = time.minusMinutes(30);

// 比较时间
boolean isAfter = time.isAfter(otherTime);
boolean isBefore = time.isBefore(otherTime);

// 特殊时间
LocalTime midnight = LocalTime.MIDNIGHT;    // 00:00
LocalTime noon = LocalTime.NOON;             // 12:00
```

---

## 1.5 LocalDateTime日期时间计算

### 1.5.1 基本操作

```java
LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);

// 获取各字段
int year = dt.getYear();                    // 2024
int month = dt.getMonthValue();             // 1
int day = dt.getDayOfMonth();               // 15
int hour = dt.getHour();                    // 10
int minute = dt.getMinute();                // 30
int second = dt.getSecond();                // 45

// 加减操作
LocalDateTime tomorrow = dt.plusDays(1);
LocalDateTime nextMonth = dt.plusMonths(1);
LocalDateTime nextYear = dt.plusYears(1);
LocalDateTime nextHour = dt.plusHours(2);

// 快捷方法
LocalDateTime startOfDay = dt.toLocalDate().atStartOfDay();
LocalDateTime withHour = dt.withHour(0);
LocalDateTime withMinute = dt.withMinute(0);
```

### 1.5.2 链式操作

```java
LocalDateTime result = LocalDateTime.now()
    .plusDays(1)           // 加1天
    .plusHours(2)          // 加2小时
    .withMinute(0)         // 设置分钟为0
    .withSecond(0);         // 设置秒为0
```

---

## 1.6 TemporalAdjusters工具类

### 1.6.1 常用调整器

```java
LocalDate date = LocalDate.of(2024, 1, 15);  // 周一

// 获取各种日期
date.with(TemporalAdjusters.firstDayOfMonth());       // 2024-01-01
date.with(TemporalAdjusters.lastDayOfMonth());        // 2024-01-31
date.with(TemporalAdjusters.firstDayOfYear());        // 2024-01-01
date.with(TemporalAdjusters.lastDayOfYear());         // 2024-12-31
date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));  // 2024-01-22
date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)); // 2024-01-15（如果是周一则不变）
date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)); // 2024-01-12
date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)); // 2024-01-01（本月第一个周一）
date.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));  // 2024-01-26（本月最后一个周五）
```

### 1.6.2 自定义调整器

```java
LocalDate date = LocalDate.of(2024, 1, 15);

// 计算下一个工作日（周一到周五）
LocalDate nextWorkday = date.with(temporal -> {
    LocalDate d = (LocalDate) temporal;
    do {
        d = d.plusDays(1);
    } while (d.getDayOfWeek() == DayOfWeek.SATURDAY ||
             d.getDayOfWeek() == DayOfWeek.SUNDAY);
    return d;
});
```

---

## 1.7 练习题

```java
// 1. 计算2024年2月的最后一天是几号

// 2. 判断2024年是否是闰年

// 3. 计算今天到春节（2024年2月10日）还有多少天

// 4. 写一个方法，判断指定日期是否是周末

// 5. 计算下个月第三个周五是几号
```

---

## 1.8 参考答案

```java
// 1. 2月最后一天
LocalDate date = YearMonth.of(2024, 2).atEndOfMonth();
System.out.println(date);  // 2024-02-29

// 2. 判断闰年
boolean isLeap = Year.of(2024).isLeap();  // true
boolean isLeap2 = LocalDate.now().isLeapYear();  // 使用LocalDate

// 3. 计算天数差
LocalDate today = LocalDate.now();
LocalDate spring = LocalDate.of(2024, 2, 10);
long days = ChronoUnit.DAYS.between(today, spring);
System.out.println("还有 " + days + " 天");

// 4. 判断是否是周末
public static boolean isWeekend(LocalDate date) {
    DayOfWeek dow = date.getDayOfWeek();
    return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
}

// 5. 下个月第三个周五
LocalDate thirdFriday = LocalDate.of(2024, 2, 1)
    .with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY))  // 第一个周五
    .plusWeeks(2);  // 再加2周 = 第三个周五
System.out.println(thirdFriday);  // 2024-02-16
```

---

[返回目录](./README.md) | [下一步：格式化与解析](./Step02_DateTimeFormatter.md)
