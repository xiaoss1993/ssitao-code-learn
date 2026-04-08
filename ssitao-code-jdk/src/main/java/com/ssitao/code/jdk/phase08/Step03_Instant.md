# 步骤3：Instant与Duration - 时间戳与时间段

---

## 3.1 Instant概述

### 3.1.1 什么是Instant

```java
// Instant：瞬时点，代表一个时间戳
// 类似于 java.util.Date，但更精确

// 概念：
// - 基于 Unix 时间纪元（1970-01-01T00:00:00Z）开始的秒数
// - Z 表示 UTC 时区
// - 可以精确到纳秒

Instant now = Instant.now();
System.out.println(now);  // 2024-01-15T02:30:45.123456789Z

// 1970年开始的秒数
long epochSecond = now.getEpochSecond();  // 1705284645
// 纳秒部分
int nano = now.getNano();  // 123456789
```

### 3.1.2 创建Instant

```java
// 1. 获取当前时间戳
Instant now = Instant.now();

// 2. 从Unix纪元开始
Instant epoch = Instant.EPOCH;  // 1970-01-01T00:00:00Z

// 3. 从秒数创建
Instant ofEpochSecond = Instant.ofEpochSecond(1705284645);
Instant ofEpochMilli = Instant.ofEpochMilli(1705284645123L);
Instant ofEpochNano = Instant.ofEpochSecond(1705284645, 123456789);

// 4. 从LocalDateTime转换
LocalDateTime ldt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
Instant fromLocal = ldt.toInstant(ZoneOffset.UTC);
Instant fromZoned = ldt.atZone(ZoneId.systemDefault()).toInstant();

// 5. 解析字符串
Instant parsed = Instant.parse("2024-01-15T10:30:45Z");
```

---

## 3.2 Instant操作

### 3.2.1 基本操作

```java
Instant now = Instant.now();

// 加减操作
Instant plus1 = now.plusSeconds(1);
Instant minus1 = now.minusSeconds(1);
Instant plusDay = now.plus(1, ChronoUnit.DAYS);
Instant minusDay = now.minus(1, ChronoUnit.DAYS);

// 比较
boolean isAfter = now.isAfter(other);
boolean isBefore = now.isBefore(other);

// 计算差值
long secondsBetween = ChronoUnit.SECONDS.between(instant1, instant2);
long millisBetween = ChronoUnit.MILLIS.between(instant1, instant2);

// 截断
Instant truncated = now.truncatedTo(ChronoUnit.SECONDS);  // 去掉毫秒和纳秒
```

### 3.2.2 与Date/LocalDateTime转换

```java
// Instant -> Date
Date date = Date.from(instant);

// Date -> Instant
Instant instant = date.toInstant();

// Instant -> LocalDateTime (需要指定时区)
LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
LocalDateTime ldt2 = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

// LocalDateTime -> Instant (需要指定时区)
Instant toInstant = ldt.atZone(ZoneId.systemDefault()).toInstant();
```

---

## 3.3 Duration

### 3.3.1 什么是Duration

```java
// Duration：时间段，用于计量时间（基于时间单位）
// - 可以表示秒和纳秒
// - 适用于测量代码执行时间、缓存过期等

// Duration vs Period
// Duration: 基于时间 (秒、分、小时)
// Period: 基于日期 (天、月、年)
```

### 3.3.2 创建Duration

```java
// 1. 从两个Instant创建
Instant start = Instant.now();
// ... 执行某些操作
Instant end = Instant.now();
Duration duration = Duration.between(start, end);

// 2. 从时间单位创建
Duration ofSeconds = Duration.ofSeconds(5);
Duration ofMinutes = Duration.ofMinutes(5);
Duration ofHours = Duration.ofHours(1);
Duration ofDays = Duration.ofDays(1);

// 3. 从毫秒/微秒/纳秒创建
Duration ofMillis = Duration.ofMillis(1000);
Duration ofNanos = Duration.ofNanos(1_000_000_000);

// 4. 从字符串创建
Duration parsed = Duration.parse("PT1H30M");  // 1小时30分钟
// PT = Period Time，H = Hour，M = Minute
```

### 3.3.3 Duration操作

```java
Duration duration = Duration.ofHours(1).plusMinutes(30).plusSeconds(45);

// 获取时间段值
long seconds = duration.getSeconds();     // 获取秒
long millis = duration.toMillis();       // 转换为毫秒
long micros = duration.toMicros();        // 转换为微秒
long nanos = duration.toNanos();          // 转换为纳秒
long minutes = duration.toMinutes();      // 转换为分钟
long hours = duration.toHours();          // 转换为小时
long days = duration.toDays();            // 转换为天

// 判断是否为空或负数
boolean isNegative = duration.isNegative();
boolean isZero = duration.isZero();

// 加减操作
Duration plus = duration.plusMinutes(10);
Duration minus = duration.minusMinutes(10);

// 乘除操作
Duration multiplied = duration.multipliedBy(2);  // 乘以2
Duration divided = duration.dividedBy(2);         // 除以2
```

---

## 3.4 Period

### 3.4.1 什么是Period

```java
// Period：时间段，用于计量日期（基于日历年单位）
// - 表示年、月、天的组合
// - 适用于计算日期差异、生日、会员有效期等
```

### 3.4.2 创建Period

```java
// 1. 从年、月、天创建
Period period = Period.of(1, 2, 3);  // 1年2个月3天

// 2. 单独创建
Period ofYears = Period.ofYears(1);
Period ofMonths = Period.ofMonths(2);
Period ofDays = Period.ofDays(3);

// 3. 从两个日期创建
LocalDate start = LocalDate.of(2024, 1, 1);
LocalDate end = LocalDate.of(2024, 3, 15);
Period between = Period.between(start, end);

// 4. 从字符串创建
Period parsed = Period.parse("P1Y2M3D");  // 1年2个月3天
```

### 3.4.3 Period操作

```java
Period period = Period.of(1, 2, 3);

// 获取各部分值
int years = period.getYears();    // 1
int months = period.getMonths();  // 2
int days = period.getDays();      // 3

// 转换为总天数
long totalDays = period.toTotalDays();

// 加减操作
Period plus = period.plusMonths(1);
Period minus = period.minusDays(1);

// 乘除操作
Period multiplied = period.multipliedBy(2);
Period normalized = period.normalized();  // 归一化月份（如18个月变为1年6个月）
```

---

## 3.5 实战应用

### 3.5.1 计算代码执行时间

```java
// 方法1：Instant
Instant start = Instant.now();
try {
    // 执行某些操作
    Thread.sleep(100);
} finally {
    Instant end = Instant.now();
    Duration duration = Duration.between(start, end);
    System.out.println("执行时间: " + duration.toMillis() + "ms");
}

// 方法2：System.nanoTime
long startNano = System.nanoTime();
// 执行操作
long endNano = System.nanoTime();
System.out.println("执行时间: " + (endNano - startNano) / 1_000_000 + "ms");
```

### 3.5.2 缓存过期

```java
public class CacheEntry<T> {
    private final T value;
    private final Instant expiresAt;

    public CacheEntry(T value, Duration ttl) {
        this.value = value;
        this.expiresAt = Instant.now().plus(ttl);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public T getValue() {
        if (isExpired()) {
            throw new IllegalStateException("Cache entry expired");
        }
        return value;
    }
}

// 使用
CacheEntry<String> cache = new CacheEntry<>("data", Duration.ofMinutes(30));
if (!cache.isExpired()) {
    String data = cache.getValue();
}
```

### 3.5.3 计算年龄

```java
public static int calculateAge(LocalDate birthDate) {
    LocalDate today = LocalDate.now();
    return Period.between(birthDate, today).getYears();
}

// 更精确的年龄（考虑生日）
public static String calculateDetailedAge(LocalDate birthDate) {
    LocalDate today = LocalDate.now();
    Period period = Period.between(birthDate, today);
    return period.getYears() + "岁" + period.getMonths() + "月" + period.getDays() + "天";
}
```

### 3.5.4 会员有效期

```java
public class Member {
    private final String name;
    private final LocalDate startDate;
    private final Period membershipPeriod;

    public Member(String name, LocalDate startDate, Period membershipPeriod) {
        this.name = name;
        this.startDate = startDate;
        this.membershipPeriod = membershipPeriod;
    }

    public LocalDate getExpirationDate() {
        return startDate.plus(membershipPeriod);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(getExpirationDate());
    }

    public long getRemainingDays() {
        if (isExpired()) return 0;
        return Period.between(LocalDate.now(), getExpirationDate()).getDays();
    }
}

// 使用
Member member = new Member("Alice", LocalDate.now(), Period.ofYears(1));
System.out.println("到期日: " + member.getExpirationDate());
System.out.println("剩余天数: " + member.getRemainingDays());
```

---

## 3.6 练习题

```java
// 1. 计算两个Instant之间相差多少毫秒

// 2. 将Duration.ofHours(2).plusMinutes(30)转换为总分钟数

// 3. 计算2024年1月1日到2024年12月31日相差多少天

// 4. 写一个方法，判断2024年是闰年还是平年，并计算该年有多少天

// 5. 实现一个简单的缓存，过期时间为30秒
```

---

## 3.7 参考答案

```java
// 1. 计算毫秒差
long millisBetween = Duration.between(instant1, instant2).toMillis();

// 2. 总分钟数
int totalMinutes = (int) Duration.ofHours(2).plusMinutes(30).toMinutes();

// 3. 计算天数差
LocalDate start = LocalDate.of(2024, 1, 1);
LocalDate end = LocalDate.of(2024, 12, 31);
long days = ChronoUnit.DAYS.between(start, end);
// 或
long days2 = Period.between(start, end).getDays();

// 4. 判断平年/闰年
LocalDate date = YearMonth.of(2024, 1).atDay(1);
long daysInYear = date.lengthOfYear();  // 366或365
boolean isLeap = date.isLeapYear();

// 5. 30秒缓存
public class SimpleCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new HashMap<>();

    public void put(K key, V value, Duration ttl) {
        cache.put(key, new CacheEntry<>(value, ttl));
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return entry.getValue();
    }

    private class CacheEntry<V> {
        private final V value;
        private final Instant expiresAt;

        CacheEntry(V value, Duration ttl) {
            this.value = value;
            this.expiresAt = Instant.now().plus(ttl);
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }

        V getValue() {
            return value;
        }
    }
}
```

---

[返回目录](./README.md)

## 第八阶段总结

### 核心知识点

| 类 | 用途 |
|----|------|
| LocalDate | 仅日期 |
| LocalTime | 仅时间 |
| LocalDateTime | 日期时间 |
| Instant | 时间戳 |
| Duration | 时间段（基于时间） |
| Period | 时间段（基于日期） |
| DateTimeFormatter | 格式化/解析 |

### 关键区别

| 类别 | 类 | 说明 |
|------|-----|------|
| 时间点 | Instant | 瞬时点，类似Date |
| 本地时间 | LocalDateTime | 本地日期时间，无时区 |
| 时区时间 | ZonedDateTime | 带时区的日期时间 |
| 时间段 | Duration | 秒/纳秒级时间段 |
| 日期段 | Period | 年/月/日时间段 |
