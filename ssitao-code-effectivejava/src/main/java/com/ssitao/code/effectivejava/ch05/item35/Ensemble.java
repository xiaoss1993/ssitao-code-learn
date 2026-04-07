package com.ssitao.code.effectivejava.ch05.item35;

/**
 * 条目35：使用实例字段代替序数值
 *
 * 永远不要用 ordinal() 来获取关联值！
 *
 * ordinal()的问题：
 * 1. 枚举顺序改变时，ordinal()返回值也会改变
 * 2. 无法表示重复值（如DOUBLE_QUARTET和OCTET都是8人）
 * 3. 代码难以理解和维护
 *
 * 正确做法：使用实例字段存储关联值
 */
public enum Ensemble {
    SOLO(1),           // 使用实例字段，不用 ordinal()
    DUET(2),
    TRIO(3),
    QUARTET(4),
    QUINTET(5),
    SEXTET(6),
    SEPTET(7),
    OCTET(8),
    DOUBLE_QUARTET(8),  // 重复值：也是8人
    TRIPLE_QUARTET(12);

    private final int numberOfMusicians;  // 实例字段 - 可靠！

    Ensemble(int size) {
        this.numberOfMusicians = size;
    }

    public int numberOfMusicians() {
        return numberOfMusicians;
    }

    /**
     * 错误示例 - 不要这样做！
     * 使用 ordinal() 是脆弱的，容易出错：
     *
     * public int numberOfMusicians() {
     *     return ordinal() + 1;  // 错误！顺序改变就坏了
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== 乐团枚举示例（实例字段） ===\n");

        for (Ensemble e : Ensemble.values()) {
            System.out.printf("%s: %d 位音乐家%n", e, e.numberOfMusicians());
        }
    }
}
