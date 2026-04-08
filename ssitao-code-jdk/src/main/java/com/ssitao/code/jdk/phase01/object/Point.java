package com.ssitao.code.jdk.phase01.object;

/**
 * 第一阶段步骤1: Object类 - 练习题答案
 *
 * 题目: 实现一个Point类,重写equals()、hashCode()和toString()方法
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * equals: 比较两个点的坐标是否相同
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }

    /**
     * hashCode: 与equals保持一致
     * 公式: result = 31 * result + 字段hashCode
     */
    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    /**
     * toString: 返回可读的字符串表示
     */
    @Override
    public String toString() {
        return String.format("Point{x=%d, y=%d}", x, y);
    }

    // Getter方法
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("=== Point类练习 ===\n");

        Point p1 = new Point(3, 4);
        Point p2 = new Point(3, 4);
        Point p3 = new Point(5, 6);

        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p3 = " + p3);
        System.out.println();

        System.out.println("p1.equals(p2) = " + p1.equals(p2) + " (应为true)");
        System.out.println("p1.equals(p3) = " + p1.equals(p3) + " (应为false)");
        System.out.println();

        System.out.println("p1.hashCode() = " + p1.hashCode());
        System.out.println("p2.hashCode() = " + p2.hashCode());
        System.out.println("p1.hashCode() == p2.hashCode() : " + (p1.hashCode() == p2.hashCode()) + " (应为true)");
    }
}
