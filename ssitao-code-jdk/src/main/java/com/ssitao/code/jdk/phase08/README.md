# 第八阶段：日期时间API

## 学习目标

掌握Java 8全新的日期时间API，理解旧API的问题，学会使用LocalDateTime、Instant等类处理日期时间。

---

## 步骤列表

| 步骤 | 主题 | 文档 | 代码 |
|------|------|------|------|
| 1 | LocalDateTime | [Step01_LocalDateTime.md](./Step01_LocalDateTime.md) | [datetime/*.java](./datetime/) |
| 2 | 格式化与解析 | [Step02_DateTimeFormatter.md](./Step02_DateTimeFormatter.md) | [format/*.java](./format/) |
| 3 | Instant与Duration | [Step03_Instant.md](./Step03_Instant.md) | [datetime/*.java](./datetime/) |

---

## 核心概念概览

### 旧API的问题

```
java.util.Date
- 年份从1900开始
- 月份从1开始
- 可变对象，线程不安全
- 时区处理麻烦

java.util.Calendar
- 也从1900开始
- 月份0-11（反人类）
- 可变对象，线程不安全

java.time包 (JDK 8+)
- 不可变对象，线程安全
- 清晰的API设计
- 完美的时区支持
```

### 新API核心类

```
LocalDateTime    - 本地日期时间（无时区）
LocalDate        - 仅日期
LocalTime        - 仅时间
ZonedDateTime    - 带时区的日期时间
Instant          - 时间戳
Duration         - 时间段（基于时间）
Period           - 时间段（基于日期）
DateTimeFormatter - 日期时间格式化
```

---

## 学习建议

1. **LocalDateTime**: 重点理解创建、解析、计算
2. **格式化**: 掌握DateTimeFormatter的使用
3. **Instant**: 理解时间戳与日期时间的转换

---

## 运行代码

```bash
cd ssitao-code-jdk
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase08.datetime.LocalDateTimeDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase08.format.DateTimeFormatterDemo"
mvn exec:java -Dexec.mainClass="com.ssitao.code.jdk.phase08.datetime.InstantDemo"
```
