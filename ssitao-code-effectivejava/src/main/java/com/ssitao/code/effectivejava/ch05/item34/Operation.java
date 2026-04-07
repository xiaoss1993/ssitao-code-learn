package com.ssitao.code.effectivejava.ch05.item34;

/**
 * 带行为的枚举 - 每个枚举常量可以有自己的实现
 *
 * 枚举可以实现抽象方法，每个常量提供自己的实现
 * 这是实现策略模式的优雅方式
 */
public enum Operation {
    PLUS("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) { return x / y; }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public String symbol() { return symbol; }

    @Override
    public String toString() { return symbol; }

    /**
     * 抽象方法 - 每个枚举常量必须实现
     */
    public abstract double apply(double x, double y);

    public static void main(String[] args) {
        System.out.println("=== 运算枚举示例 ===\n");

        double x = 10.0;
        double y = 2.0;

        for (Operation op : Operation.values()) {
            System.out.printf("%.1f %s %.1f = %.1f%n", x, op, y, op.apply(x, y));
        }
    }
}
