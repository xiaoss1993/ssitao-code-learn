package com.ssitao.code.effectivejava.ch02.item10;

import java.util.Objects;

/**
 * Item 10: Always override toString
 *
 * Demonstrates importance of toString()
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

    // GOOD toString implementation
    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
    }

    public static void main(String[] args) {
        System.out.println("=== toString() Demo ===\n");

        PhoneNumberWithToString pn = new PhoneNumberWithToString((short) 707, (short) 867, (short) 5309);

        // Without toString: PhoneNumberWithToString@4554617c (unhelpful)
        // With toString: 707-867-5309 (meaningful)
        System.out.println("Phone number: " + pn);
        System.out.println("Direct print: " + pn.toString());
    }
}
