package com.ssitao.code.effectivejava.ch03.item22;

/**
 * Item 22: Prefer class hierarchies to tagged classes
 *
 * Demonstrates proper use of class hierarchy
 */

// ==================== WRONG: Tagged Class ====================
class TaggedFigure {
    enum Shape { CIRCLE, RECTANGLE; }

    Shape shape;
    double radius;       // for circle
    double length, width; // for rectangle

    // constructors, area() with switch... tagged classes are hard to maintain
}

// ==================== CORRECT: Class Hierarchy ====================
abstract class Figure {
    abstract double area();
}

class Circle extends Figure {
    private final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Figure {
    private final double length;
    private final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }
}

class Square extends Rectangle {
    Square(double side) {
        super(side, side);
    }
}

public class FigureDemo {
    public static void main(String[] args) {
        System.out.println("=== Class Hierarchy Demo ===\n");

        // Using polymorphism
        Figure[] figures = {
            new Circle(5.0),
            new Rectangle(4.0, 5.0),
            new Square(3.0)
        };

        for (Figure f : figures) {
            System.out.println(f.getClass().getSimpleName() + " area: " + f.area());
        }

        System.out.println("\n--- Advantages ---");
        System.out.println("1. No switch statements needed");
        System.out.println("2. Each class has only relevant fields");
        System.out.println("3. Compiler ensures all subclasses implement area()");
        System.out.println("4. Easy to add new figure types");
    }
}
