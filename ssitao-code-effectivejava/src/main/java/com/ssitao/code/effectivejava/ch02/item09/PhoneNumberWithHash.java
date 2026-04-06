package com.ssitao.code.effectivejava.ch02.item09;

import java.util.HashMap;
import java.util.Map;

/**
 * Item 9: Always override hashCode when you override equals
 *
 * Demonstrates the contract between equals() and hashCode()
 */
public class PhoneNumberWithHash {
    private final short areaCode;
    private final short prefix;
    private final short lineNum;

    public PhoneNumberWithHash(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhoneNumberWithHash that = (PhoneNumberWithHash) obj;
        return areaCode == that.areaCode
            && prefix == that.prefix
            && lineNum == that.lineNum;
    }

    // CORRECT hashCode implementation
    @Override
    public int hashCode() {
        int result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
    }

    public static void main(String[] args) {
        System.out.println("=== hashCode Contract Demo ===\n");

        PhoneNumberWithHash pn1 = new PhoneNumberWithHash((short) 707, (short) 867, (short) 5309);
        PhoneNumberWithHash pn2 = new PhoneNumberWithHash((short) 707, (short) 867, (short) 5309);

        // Equal objects MUST have equal hashCodes
        System.out.println("pn1.equals(pn2): " + pn1.equals(pn2));
        System.out.println("pn1.hashCode() == pn2.hashCode(): " + (pn1.hashCode() == pn2.hashCode()));

        // HashMap behavior with proper hashCode
        Map<PhoneNumberWithHash, String> map = new HashMap<>();
        map.put(pn1, "alice");

        // Now this works correctly!
        String result = map.get(pn2);
        System.out.println("\nmap.get(pn2) where pn2.equals(pn1): " + result);

        // Different objects (hopefully) have different hashCodes
        PhoneNumberWithHash pn3 = new PhoneNumberWithHash((short) 212, (short) 555, (short) 1212);
        System.out.println("pn1.hashCode(): " + pn1.hashCode());
        System.out.println("pn3.hashCode(): " + pn3.hashCode());
        System.out.println("pn1.hashCode() == pn3.hashCode(): " + (pn1.hashCode() == pn3.hashCode()));
    }
}
