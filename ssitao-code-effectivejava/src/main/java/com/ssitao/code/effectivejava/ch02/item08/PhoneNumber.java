package com.ssitao.code.effectivejava.ch02.item08;

/**
 * 条目8：重写equals时遵守通用约定
 *
 * equals必须满足的契约：
 * 1. 自反性：x.equals(x) 必须返回true
 * 2. 对称性：x.equals(y) 和 y.equals(x) 结果相同
 * 3. 传递性：x.equals(y)且y.equals(z)，则x.equals(z)
 * 4. 一致性：多次调用结果相同（除非对象被修改）
 * 5. 非空性：x.equals(null) 必须返回false
 *
 * 注意：继承时可能违反对称性和传递性
 */
public class PhoneNumber {
    private final short areaCode;  // 区号
    private final short prefix;    // 前缀
    private final short lineNum;   // 号码

    public PhoneNumber(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    public short getAreaCode() { return areaCode; }
    public short getPrefix() { return prefix; }
    public short getLineNum() { return lineNum; }

    /**
     * 正确的equals实现
     */
    @Override
    public boolean equals(Object obj) {
        // 1. 检查是否同一引用（自反性）
        if (this == obj) {
            return true;
        }

        // 2. 检查是否为null（非空性）
        if (obj == null) {
            return false;
        }

        // 3. 检查是否是同一个类（对称性）
        if (getClass() != obj.getClass()) {
            return false;
        }

        // 4. 转型并比较字段
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
        System.out.println("=== 正确的equals()示例 ===\n");

        PhoneNumber pn1 = new PhoneNumber((short) 707, (short) 867, (short) 5309);
        PhoneNumber pn2 = new PhoneNumber((short) 707, (short) 867, (short) 5309);
        PhoneNumber pn3 = new PhoneNumber((short) 212, (short) 555, (short) 1212);

        // 自反性：x.equals(x) 必须为true
        System.out.println("自反性: pn1.equals(pn1) = " + pn1.equals(pn1));

        // 对称性：x.equals(y) == y.equals(x)
        System.out.println("对称性: pn1.equals(pn2) = " + pn1.equals(pn2));
        System.out.println("对称性: pn2.equals(pn1) = " + pn2.equals(pn1));

        // 值相同但不同引用的对象应该相等
        System.out.println("pn1 == pn2: " + (pn1 == pn2) + " (不同引用)");
        System.out.println("pn1.equals(pn2): " + pn1.equals(pn2) + " (equals返回true)");

        // 非空性
        System.out.println("pn1.equals(null): " + pn1.equals(null));
    }
}
