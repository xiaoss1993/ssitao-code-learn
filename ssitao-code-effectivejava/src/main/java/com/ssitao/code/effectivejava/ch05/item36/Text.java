package com.ssitao.code.effectivejava.ch05.item36;

import java.util.EnumSet;
import java.util.Set;

/**
 * 条目36：用EnumSet代替位域
 *
 * 位域的问题：
 * 1. 不是类型安全：可以用任意int值
 * 2. 难以调试：打印出来是数字，不直观
 * 3. 需要手动处理位运算
 *
 * EnumSet的优点：
 * 1. 类型安全：只能是指定枚举类型的值
 * 2. 性能好：内部用位向量实现
 * 3. API简洁：addAll()、complementOf()等方法
 * 4. 可读性好：打印出来是枚举名称
 */
public class Text {
    public enum Style { BOLD, ITALIC, UNDERLINE, STRIKETHROUGH }

    // EnumSet - 类型安全且高效
    public void applyStyles(Set<Style> styles) {
        System.out.println("应用的样式: " + styles);
    }

    public static void main(String[] args) {
        System.out.println("=== EnumSet示例 ===\n");

        Text text = new Text();

        // 轻松添加样式
        text.applyStyles(EnumSet.of(Style.BOLD));

        // 组合多个样式
        text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));

        // 所有样式
        text.applyStyles(EnumSet.allOf(Style.class));

        // 范围内的样式
        text.applyStyles(EnumSet.range(Style.BOLD, Style.STRIKETHROUGH));

        // 补集（除了...以外的所有）
        text.applyStyles(EnumSet.complementOf(EnumSet.of(Style.BOLD)));

        System.out.println("\n=== 位域 vs EnumSet ===\n");

        // 旧方式 - 位域（错误）
        // public static final int STYLE_BOLD = 1 << 0;     // 1
        // public static final int STYLE_ITALIC = 1 << 1;   // 2
        // private int styles;  // 位域

        // EnumSet方式（正确）
        EnumSet<Style> styles = EnumSet.noneOf(Style.class);
        styles.add(Style.BOLD);
        styles.add(Style.ITALIC);
        System.out.println("EnumSet: " + styles);

        // 集合操作很简单
        EnumSet<Style> set1 = EnumSet.of(Style.BOLD, Style.ITALIC);
        EnumSet<Style> set2 = EnumSet.of(Style.UNDERLINE, Style.STRIKETHROUGH);
        EnumSet<Style> union = EnumSet.copyOf(set1);
        union.addAll(set2);
        System.out.println("并集: " + union);
    }
}
