package com.ssitao.code.effectivejava.ch02.item08;

/**
 * Item 8: Obey the general contract when overriding equals
 *
 * Demonstrates proper equals() implementation
 */
public class PhoneNumber {
    private final short areaCode;
    private final short prefix;
    private final short lineNum;

    public PhoneNumber(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    public short getAreaCode() { return areaCode; }
    public short getPrefix() { return prefix; }
    public short getLineNum() { return lineNum; }

    // CORRECT equals implementation
    @Override
    public boolean equals(Object obj) {
        // 1. Check if same reference
        if (this == obj) {
            return true;
        }

        // 2. Check if null
        if (obj == null) {
            return false;
        }

        // 3. Check if same class
        if (getClass() != obj.getClass()) {
            return false;
        }

        // 4. Cast and compare fields
        PhoneNumber other = (PhoneNumber) obj;
        return areaCode == other.areaCode
            && prefix == other.prefix
            && lineNum == other.lineNum;
    }

    @Override
    public String toString() {
        return String.format("%03d-%03d-%04d", areaCode, prefix, lineNum);
    }

    public static void main(String[] args) {
        System.out.println("=== Proper equals() Demo ===\n");

        PhoneNumber pn1 = new PhoneNumber((short) 707, (short) 867, (short) 5309);
        PhoneNumber pn2 = new PhoneNumber((short) 707, (short) 867, (short) 5309);
        PhoneNumber pn3 = new PhoneNumber((short) 212, (short) 555, (short) 1212);

        // Reflexive: x.equals(x) must be true
        System.out.println("Reflexive: pn1.equals(pn1) = " + pn1.equals(pn1));

        // Symmetric: x.equals(y) == y.equals(x)
        System.out.println("Symmetric: pn1.equals(pn2) = " + pn1.equals(pn2));
        System.out.println("Symmetric: pn2.equals(pn1) = " + pn2.equals(pn1));

        // Different objects with same values should be equal
        System.out.println("pn1 == pn2: " + (pn1 == pn2) + " (different references)");
        System.out.println("pn1.equals(pn2): " + pn1.equals(pn2) + " (but equal by equals())");

        // Null check
        System.out.println("pn1.equals(null): " + pn1.equals(null));
    }
}
