package com.ssitao.code.jdk.phase08.format;

import java.time.*;
import java.time.format.*;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * DateTimeFormatter 示例
 */
public class DateTimeFormatterDemo {

    public static void main(String[] args) {
        System.out.println("=== DateTimeFormatter Demo ===\n");

        LocalDateTime dt = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
        LocalDate date = LocalDate.of(2024, 1, 15);
        LocalTime time = LocalTime.of(10, 30, 45);

        // 1. 预定义格式
        demonstratePredefinedFormats(dt);

        // 2. 自定义格式
        demonstrateCustomFormats(dt);

        // 3. 中文格式
        demonstrateChineseFormats(dt, date, time);

        // 4. 解析
        demonstrateParsing();

        // 5. 本地化格式
        demonstrateLocalizedFormats(dt);
    }

    private static void demonstratePredefinedFormats(LocalDateTime dt) {
        System.out.println("--- Predefined Formats ---");

        System.out.println("ISO_LOCAL_DATE: " + dt.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("ISO_LOCAL_TIME: " + dt.format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.out.println("ISO_LOCAL_DATE_TIME: " + dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("BASIC_ISO_DATE: " + dt.format(DateTimeFormatter.BASIC_ISO_DATE));

        System.out.println();
    }

    private static void demonstrateCustomFormats(LocalDateTime dt) {
        System.out.println("--- Custom Formats ---");

        // 日期格式
        System.out.println("日期格式:");
        System.out.println("  yyyy-MM-dd: " + dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        System.out.println("  yyyy/MM/dd: " + dt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        System.out.println("  yyyy年MM月dd日: " + dt.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        System.out.println("  yyyyMMdd: " + dt.format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        // 时间格式
        System.out.println("\n时间格式:");
        System.out.println("  HH:mm:ss: " + dt.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        System.out.println("  HH:mm: " + dt.format(DateTimeFormatter.ofPattern("HH:mm")));
        System.out.println("  HH时mm分ss秒: " + dt.format(DateTimeFormatter.ofPattern("HH时mm分ss秒")));

        // 日期时间组合
        System.out.println("\n日期时间组合:");
        System.out.println("  yyyy-MM-dd HH:mm:ss: " + dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("  yyyy-MM-dd'T'HH:mm:ss: " + dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        System.out.println();
    }

    private static void demonstrateChineseFormats(LocalDateTime dt, LocalDate date, LocalTime time) {
        System.out.println("--- Chinese Formats ---");

        // 中文日期
        System.out.println("中文日期:");
        System.out.println("  yyyy年MM月dd日: " + date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
        System.out.println("  yyyy年M月d日: " + date.format(DateTimeFormatter.ofPattern("yyyy年M月d日")));

        // 中文星期
        System.out.println("\n中文星期:");
        System.out.println("  EEEE: " + date.format(DateTimeFormatter.ofPattern("EEEE", Locale.CHINESE)));
        System.out.println("  E: " + date.format(DateTimeFormatter.ofPattern("E", Locale.CHINESE)));

        // 中文时间
        System.out.println("\n中文时间:");
        System.out.println("  HH时mm分ss秒: " + time.format(DateTimeFormatter.ofPattern("HH时mm分ss秒")));
        System.out.println("  上午HH:mm:ss: " + time.format(DateTimeFormatter.ofPattern("上午HH:mm:ss")));

        // 完整中文日期时间
        System.out.println("\n完整中文日期时间:");
        DateTimeFormatter chineseFormatter = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR)
            .appendLiteral("年")
            .appendValue(ChronoField.MONTH_OF_YEAR, 2)
            .appendLiteral("月")
            .appendValue(ChronoField.DAY_OF_MONTH, 2)
            .appendLiteral("日 ")
            .appendValue(ChronoField.HOUR_OF_DAY, 2)
            .appendLiteral(":")
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .appendLiteral(":")
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .toFormatter(Locale.CHINESE);
        System.out.println(dt.format(chineseFormatter));

        System.out.println();
    }

    private static void demonstrateParsing() {
        System.out.println("--- Parsing ---");

        // 解析日期
        System.out.println("解析日期:");
        LocalDate date1 = LocalDate.parse("2024-01-15");
        LocalDate date2 = LocalDate.parse("2024/01/15", DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate date3 = LocalDate.parse("2024年01月15日", DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        System.out.println("  2024-01-15 -> " + date1);
        System.out.println("  2024/01/15 -> " + date2);
        System.out.println("  2024年01月15日 -> " + date3);

        // 解析时间
        System.out.println("\n解析时间:");
        LocalTime time1 = LocalTime.parse("10:30:45");
        LocalTime time2 = LocalTime.parse("10-30-45", DateTimeFormatter.ofPattern("HH-mm-ss"));
        System.out.println("  10:30:45 -> " + time1);
        System.out.println("  10-30-45 -> " + time2);

        // 解析日期时间
        System.out.println("\n解析日期时间:");
        LocalDateTime dt1 = LocalDateTime.parse("2024-01-15T10:30:45");
        LocalDateTime dt2 = LocalDateTime.parse("2024-01-15 10:30:45",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("  2024-01-15T10:30:45 -> " + dt1);
        System.out.println("  2024-01-15 10:30:45 -> " + dt2);

        // 解析带时区的
        System.out.println("\n解析带时区:");
        ZonedDateTime zdt = ZonedDateTime.parse("2024-01-15T10:30:45+08:00",
            DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println("  带时区字符串 -> " + zdt);

        System.out.println();
    }

    private static void demonstrateLocalizedFormats(LocalDateTime dt) {
        System.out.println("--- Localized Formats ---");

        System.out.println("FULL: " + dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)));
        System.out.println("LONG: " + dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
        System.out.println("MEDIUM: " + dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        System.out.println("SHORT: " + dt.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));

        // 不同地区
        System.out.println("\n不同地区格式:");
        System.out.println("  美国 (US): " + dt.format(DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US)));
        System.out.println("  德国 (GERMAN): " + dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN)));
        System.out.println("  日本 (JAPANESE): " + dt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.JAPANESE)));

        System.out.println();
    }
}
