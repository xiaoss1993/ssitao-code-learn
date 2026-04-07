package com.ssitao.code.effectivejava.ch03.item22;

/**
 * 条目22：类层次优于标签类
 *
 * 标签类的问题：
 * - 多个不相关的字段堆砌在一个类中
 * - switch语句散布，难以维护
 * - 新增类型需要修改所有switch
 * - 无法添加与特定类型相关的字段
 *
 * 本例演示：标签类（有shape字段判断类型）vs 类层次（继承）
 */

// ==================== 错误：标签类 ====================
/**
 * 标签类 - 用shape字段区分类型
 * 这种方式难以维护，应避免使用
 */
class TaggedFigure {
    enum Shape { CIRCLE, RECTANGLE; }

    Shape shape;
    double radius;       // 用于圆形
    double length, width; // 用于矩形

    // 构造器、area()方法中包含switch...
    // 标签类难以维护
}

// ==================== 正确：类层次 ====================
/**
 * 抽象基类 - 定义公共接口
 */
abstract class Figure {
    abstract double area();  // 每个子类必须实现
}

/**
 * 圆形
 */
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

/**
 * 矩形
 */
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

/**
 * 正方形 - 是矩形的特殊情况
 */
class Square extends Rectangle {
    Square(double side) {
        super(side, side);  // 边长相同的矩形
    }
}

public class FigureDemo {
    public static void main(String[] args) {
        System.out.println("=== 类层次示例 ===\n");

        // 使用多态
        Figure[] figures = {
            new Circle(5.0),
            new Rectangle(4.0, 5.0),
            new Square(3.0)
        };

        for (Figure f : figures) {
            System.out.println(f.getClass().getSimpleName() + " 面积: " + f.area());
        }

        System.out.println("\n--- 优点 ---");
        System.out.println("1. 无需switch语句");
        System.out.println("2. 每个类只有相关字段");
        System.out.println("3. 编译器确保所有子类实现area()");
        System.out.println("4. 易于添加新图形类型");
    }
}
