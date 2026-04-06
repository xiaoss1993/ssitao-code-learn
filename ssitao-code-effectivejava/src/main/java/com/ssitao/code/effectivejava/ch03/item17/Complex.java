package com.ssitao.code.effectivejava.ch03.item17;

import java.util.Objects;

/**
 * Item 17: Minimize mutability
 *
 * Demonstrates immutable class design
 */
public final class Complex {
    private final double re;
    private final double im;

    // Immutable class - no setters, returns new instances
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // Static factory methods
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }

    public static Complex of(double re, double im) {
        return new Complex(re, im);
    }

    // Getters (no setters - immutable!)
    public double getReal() { return re; }
    public double getImaginary() { return im; }

    // Arithmetic operations - return NEW instances
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
        System.out.println("=== Immutable Class Demo ===\n");

        Complex a = new Complex(1.0, 2.0);
        Complex b = new Complex(3.0, 4.0);

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        Complex c = a.add(b);
        System.out.println("a.add(b) = " + c);
        System.out.println("a is still = " + a);  // a unchanged (immutable!)

        Complex d = a.multiply(b);
        System.out.println("a.multiply(b) = " + d);

        // Demonstrate thread safety
        System.out.println("\n--- Thread Safety ---");
        System.out.println("Immutable objects are inherently thread-safe:");
        System.out.println("- No synchronization needed");
        System.out.println("- No possibility of inconsistent state");
        System.out.println("- Can be freely shared");
    }
}
