package com.ssitao.code.designpattern.factory.method.jdk;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * JDK 工厂方法模式示例 - Calendar.getInstance()
 *
 * Calendar 是一个抽象类，getInstance() 是工厂方法，
 * 根据系统时区和Locale返回不同的 Calendar 子类实例
 *
 * 工厂方法模式体现：
 * - 产品：Calendar（抽象产品）
 * - 具体产品：GregorianCalendar, BuddhistCalendar 等
 * - 工厂方法：getInstance()
 *
 * 注意：Java 8 之后推荐使用 java.time 包 (如 LocalDateTime.now())
 */
public class CalendarDemo {

    public static void main(String[] args) {
        System.out.println("=== Calendar.getInstance() 工厂方法示例 ===\n");

        // 默认工厂方法 - 根据系统时区返回 GregorianCalendar
        Calendar defaultCalendar = Calendar.getInstance();
        System.out.println("默认 Calendar: " + defaultCalendar.getClass().getSimpleName());
        System.out.println("  时间: " + defaultCalendar.getTime());

        // 带时区的工厂方法
        TimeZone tokyoZone = TimeZone.getTimeZone("Asia/Tokyo");
        Calendar tokyoCalendar = Calendar.getInstance(tokyoZone);
        System.out.println("\n东京 Calendar: " + tokyoCalendar.getClass().getSimpleName());
        System.out.println("  时区: " + tokyoCalendar.getTimeZone().getID());
        System.out.println("  时间: " + tokyoCalendar.getTime());

        // 带Locale的工厂方法 - 泰国返回 BuddhistCalendar
        java.util.Locale thaiLocale = new java.util.Locale("th", "TH");
        Calendar thaiCalendar = Calendar.getInstance(java.util.Locale.forLanguageTag("th-TH"));
        System.out.println("\n泰国 Calendar: " + thaiCalendar.getClass().getSimpleName());
        System.out.println("  Locale: " + thaiLocale);
        System.out.println("  时间: " + thaiCalendar.getTime());

        // 同时指定时区和Locale
        Calendar usCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"), java.util.Locale.US);
        System.out.println("\n美国 Calendar: " + usCalendar.getClass().getSimpleName());
        System.out.println("  时区: " + usCalendar.getTimeZone().getID());
        System.out.println("  Locale: " + java.util.Locale.US);
    }
}
