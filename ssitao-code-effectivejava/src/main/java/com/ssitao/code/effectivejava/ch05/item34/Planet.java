package com.ssitao.code.effectivejava.ch05.item34;

/**
 * 条目34：用枚举代替int常量
 *
 * 枚举的优点：
 * 1. 编译时类型安全：编译器检查
 * 2. 单例模式天然实现：每个枚举常量唯一实例
 * 3. 线程安全：JVM保证
 * 4. 可添加方法和字段：支持丰富的行为
 * 5. 可实现接口：灵活扩展
 *
 * 枚举天然是final的，构造器是私有的
 */
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),   // 水星
    VENUS(4.869e+24, 6.0518e6),     // 金星
    EARTH(5.976e+24, 6.37814e6),   // 地球
    MARS(6.421e+23, 3.3972e6),     // 火星
    JUPITER(1.9e+27, 7.1492e7),    // 木星
    SATURN(5.688e+26, 6.0268e7),   // 土星
    URANUS(8.686e+25, 2.5559e7),   // 天王星
    NEPTUNE(1.024e+26, 2.4746e7);  // 海王星

    private final double mass;    // 质量（千克）
    private final double radius;  // 半径（米）

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    public double mass() { return mass; }
    public double radius() { return radius; }

    // 万有引力常数
    public static final double G = 6.67300E-11;

    /**
     * 计算表面重力
     */
    public double surfaceGravity() {
        return G * mass / (radius * radius);
    }

    /**
     * 计算在当前行星上的重量
     */
    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
}
