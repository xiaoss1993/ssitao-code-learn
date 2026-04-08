package com.ssitao.code.jdk.phase08.datetime;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Instant 和 Duration 示例
 */
public class InstantDemo {

    public static void main(String[] args) {
        System.out.println("=== Instant & Duration Demo ===\n");

        // 1. Instant 基本操作
        demonstrateInstant();

        // 2. Duration 基本操作
        demonstrateDuration();

        // 3. Period 基本操作
        demonstratePeriod();

        // 4. 实战应用
        demonstratePracticalUse();
    }

    private static void demonstrateInstant() {
        System.out.println("--- Instant ---");

        // 获取当前时间戳
        Instant now = Instant.now();
        System.out.println("当前时间戳: " + now);

        // 从纪元开始创建
        Instant epoch = Instant.EPOCH;
        System.out.println("Unix纪元: " + epoch);

        // 从秒/毫秒创建
        Instant ofSecond = Instant.ofEpochSecond(1705284645);
        Instant ofMilli = Instant.ofEpochMilli(1705284645123L);
        System.out.println("从秒创建: " + ofSecond);
        System.out.println("从毫秒创建: " + ofMilli);

        // 获取时间戳值
        System.out.println("\n获取值:");
        System.out.println("  Epoch秒: " + now.getEpochSecond());
        System.out.println("  纳秒: " + now.getNano());

        // 加减操作
        Instant plus1 = now.plusSeconds(1);
        Instant minus1 = now.minusSeconds(1);
        System.out.println("\n加减1秒:");
        System.out.println("  加1秒: " + plus1);
        System.out.println("  减1秒: " + minus1);

        // 与LocalDateTime转换
        System.out.println("\n与LocalDateTime转换:");
        LocalDateTime ldt = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        System.out.println("Instant -> LocalDateTime: " + ldt);
        Instant fromLdt = ldt.atZone(ZoneId.systemDefault()).toInstant();
        System.out.println("LocalDateTime -> Instant: " + fromLdt);

        System.out.println();
    }

    private static void demonstrateDuration() {
        System.out.println("--- Duration ---");

        // 创建Duration
        Duration duration = Duration.ofHours(1).plusMinutes(30).plusSeconds(45);
        System.out.println("Duration: " + duration);

        // 从两个Instant创建
        Instant start = Instant.now();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();
        Duration between = Duration.between(start, end);
        System.out.println("两Instant之间: " + between.toMillis() + "ms");

        // 获取值
        System.out.println("\n获取值:");
        System.out.println("  秒: " + duration.getSeconds());
        System.out.println("  毫秒: " + duration.toMillis());
        System.out.println("  分钟: " + duration.toMinutes());
        System.out.println("  小时: " + duration.toHours());
        System.out.println("  天: " + duration.toDays());

        // Duration.parse 格式: PTnHnMnS
        Duration parsed = Duration.parse("PT1H30M45S");
        System.out.println("\n解析 'PT1H30M45S': " + parsed);

        // 运算
        Duration multiplied = duration.multipliedBy(2);
        Duration divided = duration.dividedBy(2);
        System.out.println("\n运算:");
        System.out.println("  乘以2: " + multiplied);
        System.out.println("  除以2: " + divided);

        System.out.println();
    }

    private static void demonstratePeriod() {
        System.out.println("--- Period ---");

        // 创建Period
        Period period = Period.of(1, 2, 3);  // 1年2个月3天
        System.out.println("Period: " + period);

        // 从两个日期创建
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        Period between = Period.between(start, end);
        System.out.println("两日期间: " + between);

        // 获取值
        System.out.println("\n获取值:");
        System.out.println("  年: " + period.getYears());
        System.out.println("  月: " + period.getMonths());
        System.out.println("  日: " + period.getDays());
        System.out.println("  归一化总天数: " + period.normalized().getMonths() + "个月"
            + period.normalized().getDays() + "天");

        // Period.parse 格式: PnYnMnD
        Period parsed = Period.parse("P1Y2M3D");
        System.out.println("\n解析 'P1Y2M3D': " + parsed);

        // 运算
        Period plus = period.plusMonths(1);
        Period multiplied = period.multipliedBy(2);
        System.out.println("\n运算:");
        System.out.println("  加1月: " + plus);
        System.out.println("  乘以2: " + multiplied);

        System.out.println();
    }

    private static void demonstratePracticalUse() {
        System.out.println("--- Practical Use ---");

        // 1. 计算年龄
        System.out.println("计算年龄:");
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        System.out.println("  出生日期: " + birthDate);
        System.out.println("  年龄: " + age + " 岁");

        // 2. 会员有效期
        System.out.println("\n会员有效期:");
        LocalDate memberSince = LocalDate.of(2024, 1, 1);
        Period membershipPeriod = Period.ofYears(1);
        LocalDate expiration = memberSince.plus(membershipPeriod);
        long remainingDays = ChronoUnit.DAYS.between(LocalDate.now(), expiration);
        System.out.println("  入会日期: " + memberSince);
        System.out.println("  到期日期: " + expiration);
        System.out.println("  剩余天数: " + remainingDays);

        // 3. 计算工作日
        System.out.println("\n计算工作日:");
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        long workdays = calculateWorkdays(start, end);
        System.out.println("  " + start + " 到 " + end + " 工作日: " + workdays);

        // 4. 计算代码执行时间
        System.out.println("\n计算执行时间:");
        Instant startTime = Instant.now();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant endTime = Instant.now();
        Duration executionTime = Duration.between(startTime, endTime);
        System.out.println("  执行时间: " + executionTime.toMillis() + "ms");

        System.out.println();
    }

    // 计算两个日期之间的工作日数量
    private static long calculateWorkdays(LocalDate start, LocalDate end) {
        long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
        long weeks = totalDays / 7;
        long workdays = weeks * 5;

        // 计算剩余天数
        long remainder = totalDays % 7;
        for (long i = 0; i < remainder; i++) {
            LocalDate date = start.plusDays(weeks * 7 + i);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workdays++;
            }
        }
        return workdays;
    }
}
