package com.ssitao.code.jdk.phase08.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * LocalDateTime 示例
 */
public class LocalDateTimeDemo {

    public static void main(String[] args) {
        System.out.println("=== LocalDateTime Demo ===\n");

        // 1. LocalDate 基本操作
        demonstrateLocalDate();

        // 2. LocalTime 基本操作
        demonstrateLocalTime();

        // 3. LocalDateTime 基本操作
        demonstrateLocalDateTime();

        // 4. TemporalAdjusters 使用
        demonstrateTemporalAdjusters();

        // 5. 日期计算
        demonstrateDateCalculation();
    }

    private static void demonstrateLocalDate() {
        System.out.println("--- LocalDate ---");

        // 获取当前日期
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today);

        // 指定日期
        LocalDate date = LocalDate.of(2024, 1, 15);
        System.out.println("指定日期: " + date);

        // 解析
        LocalDate parsed = LocalDate.parse("2024-01-15");
        System.out.println("解析字符串: " + parsed);

        // 获取各字段
        System.out.println("\n获取字段:");
        System.out.println("  年: " + date.getYear());
        System.out.println("  月: " + date.getMonthValue());
        System.out.println("  日: " + date.getDayOfMonth());
        System.out.println("  星期: " + date.getDayOfWeek());
        System.out.println("  是否闰年: " + date.isLeapYear());

        System.out.println();
    }

    private static void demonstrateLocalTime() {
        System.out.println("--- LocalTime ---");

        // 获取当前时间
        LocalTime now = LocalTime.now();
        System.out.println("Now: " + now);

        // 指定时间
        LocalTime time = LocalTime.of(10, 30, 45);
        System.out.println("指定时间: " + time);

        // 解析
        LocalTime parsed = LocalTime.parse("10:30:45");
        System.out.println("解析字符串: " + parsed);

        // 获取各字段
        System.out.println("\n获取字段:");
        System.out.println("  时: " + time.getHour());
        System.out.println("  分: " + time.getMinute());
        System.out.println("  秒: " + time.getSecond());

        System.out.println();
    }

    private static void demonstrateLocalDateTime() {
        System.out.println("--- LocalDateTime ---");

        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Now: " + now);

        // 指定日期时间
        LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        System.out.println("指定日期时间: " + dt);

        // 解析
        LocalDateTime parsed = LocalDateTime.parse("2024-01-15T10:30:45");
        System.out.println("解析字符串: " + parsed);

        // 格式化为字符串
        String formatted = dt.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        System.out.println("格式化: " + formatted);

        System.out.println();
    }

    private static void demonstrateTemporalAdjusters() {
        System.out.println("--- TemporalAdjusters ---");

        LocalDate date = LocalDate.of(2024, 1, 15);  // 周一

        System.out.println("原始日期: " + date);
        System.out.println("本月第一天: " + date.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.println("本月最后一天: " + date.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println("本年第一天: " + date.with(TemporalAdjusters.firstDayOfYear()));
        System.out.println("本年最后一天: " + date.with(TemporalAdjusters.lastDayOfYear()));

        System.out.println("下一个周一: " + date.with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
        System.out.println("本月第一个周一: " + date.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));
        System.out.println("本月最后一个周五: " + date.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));

        System.out.println();
    }

    private static void demonstrateDateCalculation() {
        System.out.println("--- Date Calculation ---");

        LocalDate date = LocalDate.of(2024, 1, 15);

        // 加减操作
        System.out.println("加1天: " + date.plusDays(1));
        System.out.println("加1周: " + date.plusWeeks(1));
        System.out.println("加1月: " + date.plusMonths(1));
        System.out.println("减1年: " + date.minusYears(1));

        // 比较
        LocalDate other = LocalDate.of(2024, 1, 20);
        System.out.println("\n比较 " + date + " 和 " + other + ":");
        System.out.println("  isAfter: " + date.isAfter(other));
        System.out.println("  isBefore: " + date.isBefore(other));

        // 计算天数差
        long daysBetween = ChronoUnit.DAYS.between(date, other);
        System.out.println("  天数差: " + daysBetween);

        // 链式操作
        LocalDate result = LocalDate.now()
            .plusDays(1)
            .plusMonths(1)
            .withDayOfMonth(1);
        System.out.println("\n链式操作结果: " + result);

        // YearMonth 计算月份天数
        YearMonth yearMonth = YearMonth.of(2024, 2);
        System.out.println("\n2024年2月有 " + yearMonth.lengthOfMonth() + " 天");
        System.out.println("2024年2月最后一天: " + yearMonth.atEndOfMonth());

        System.out.println();
    }
}
