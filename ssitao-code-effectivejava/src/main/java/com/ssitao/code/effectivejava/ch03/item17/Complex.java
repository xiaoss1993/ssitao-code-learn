package com.ssitao.code.effectivejava.ch03.item17;

import java.util.Objects;

/**
 * 条目17：最小化可变性
 *
 * 不可变类的五大原则：
 * 1. 不提供修改对象状态的方法（如setter）
 * 2. 确保类不会被继承（用final修饰或私有构造器）
 * 3. 所有字段用final修饰
 * 4. 所有字段用private修饰
 * 5. 确保对可变组件的访问是独占的（如防御性拷贝）
 *
 * 不可变类的优点：
 * - 线程安全：无需同步
 * - 可自由共享：无需拷贝防御
 * - 失败原子性：状态不会被异常破坏
 * - 易于设计：状态不会变化
 */
public final class Complex {
    private final double re;  // 实部
    private final double im;  // 虚部

    /**
     * 不可变类 - 没有setter，所有操作返回新实例
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // 静态工厂方法
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }

    public static Complex of(double re, double im) {
        return new Complex(re, im);
    }

    // Getter（没有setter！）
    public double getReal() { return re; }
    public double getImaginary() { return im; }

    /**
     * 算术运算 - 返回新的实例，原对象不变
     */
    public Complex add(Complex other) {
        return new Complex(re + other.re, im + other.im);
    }

    public Complex subtract(Complex other) {
        return new Complex(re - other.re, im - other.im);
    }

    public Complex multiply(Complex other) {
        return new Complex(
            re * other.re - im * other.im,
            re * other.im + im * other.re
        );
    }

    public Complex divide(Complex other) {
        double denominator = other.re * other.re + other.im * other.im;
        return new Complex(
            (re * other.re + im * other.im) / denominator,
            (im * other.re - re * other.im) / denominator
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return Double.compare(complex.re, re) == 0 &&
               Double.compare(complex.im, im) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(re, im);
    }

    @Override
    public String toString() {
        if (im >= 0) {
            return String.format("%.1f + %.1fi", re, im);
        } else {
            return String.format("%.1f - %.1fi", re, -im);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 不可变类示例 ===\n");

        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Complex c = a.add(b);
        System.out.println("a.add(b) = " + c);
        System.out.println("a 仍然是 = " + a);  // a未改变（不可变！）

        Complex d = a.multiply(b);
        System.out.println("a.multiply(b) = " + d);

        // 演示线程安全性
        System.out.println("\n--- 线程安全性 ---");
        System.out.println("不可变对象天生线程安全：");
        System.out.println("- 无需同步");
        System.out.println("- 不可能出现不一致的状态");
        System.out.println("- 可以自由共享");
    }
}
