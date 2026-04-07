package com.ssitao.code.effectivejava.ch02.item09;

import java.util.HashMap;
import java.util.Map;

/**
 * 条目9：重写equals时必须重写hashCode
 *
 * hashCode的通用约定：
 * 1. 相等对象的哈希码必须相同
 * 2. 不相等的对象不一定有不同的哈希码（但可以提高性能）
 *
 * hashCode和equals的契约：
 * - 如果 a.equals(b) 为true，则 a.hashCode() == b.hashCode()
 * - 如果 a.equals(b) 为false，hashCode可以相同也可以不同
 * - 同一个对象多次调用hashCode()应返回相同结果（除非对象被修改）
 *
 * 如果不遵守这个契约，会导致HashMap、HashSet等集合行为异常
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

    /**
     * 正确的hashCode实现
     * 使用31是因为：31*i = (i<<5) - i，JVM会优化乘法
     */
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
        System.out.println("=== hashCode契约示例 ===\n");

        PhoneNumberWithHash pn1 = new PhoneNumberWithHash((short) 707, (short) 867, (short) 5309);
        PhoneNumberWithHash pn2 = new PhoneNumberWithHash((short) 707, (short) 867, (short) 5309);

        // equals相等的对象，hashCode必须相等
        System.out.println("pn1.equals(pn2): " + pn1.equals(pn2));
        System.out.println("pn1.hashCode() == pn2.hashCode(): " + (pn1.hashCode() == pn2.hashCode()));

        // 正确的hashCode使HashMap正常工作
        Map<PhoneNumberWithHash, String> map = new HashMap<>();
        map.put(pn1, "alice");

        // 现在能正确获取！
        String result = map.get(pn2);
        System.out.println("\nmap.get(pn2)（pn2.equals(pn1)为true）: " + result);

        // 不同对象的哈希码可能相同（哈希冲突）
        PhoneNumberWithHash pn3 = new PhoneNumberWithHash((short) 212, (short) 555, (short) 1212);
        System.out.println("\npn1.hashCode(): " + pn1.hashCode());
        System.out.println("pn3.hashCode(): " + pn3.hashCode());
        System.out.println("pn1.hashCode() == pn3.hashCode(): " + (pn1.hashCode() == pn3.hashCode()));
    }
}
