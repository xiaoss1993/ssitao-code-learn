package com.ssitao.code.effectivejava.ch02.item10;

import java.util.Objects;

/**
 * 条目10：始终重写toString
 *
 * toString的好处：
 * 1. 调试更方便：打印对象时能看到有意义的描述
 * 2. 日志输出更清晰：便于排查问题
 * 3. 便于开发：快速了解对象内容
 *
 * 建议：
 * - 返回有意义的格式
 * - 包含对象的关键信息
 * - 文档中说明格式
 */
public class PhoneNumberWithToString {
    private final short areaCode;
    private final short prefix;
    private final short lineNum;

    public PhoneNumberWithToString(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhoneNumberWithToString that = (PhoneNumberWithToString) obj;
        return areaCode == that.areaCode
            && prefix == that.prefix
            && lineNum == that.lineNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, prefix, lineNum);
    }

    /**
     * 好的toString实现
     * 返回格式：707-867-5309
     */
    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
    }

    public static void main(String[] args) {
        System.out.println("=== toString()示例 ===\n");

        PhoneNumberWithToString pn = new PhoneNumberWithToString((short) 707, (short) 867, (short) 5309);

        // 没有toString: PhoneNumberWithToString@4554617c（无意义）
        // 有toString: 707-867-5309（有意义）
        System.out.println("电话号码: " + pn);
        System.out.println("直接打印: " + pn.toString());
    }
}
