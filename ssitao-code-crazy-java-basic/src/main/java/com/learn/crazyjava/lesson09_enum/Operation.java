package com.learn.crazyjava.lesson09_enum;

/**
 * 第9课：枚举 - 枚举实现接口
 */
public enum Operation implements Calculate {
    PLUS("+") {
        @Override
        public double apply(double x, double y) {
            return x + y;
        }
    },
    MINUS("-") {
        @Override
        public double apply(double x, double y) {
            return x - y;
        }
    },
    TIMES("*") {
        @Override
        public double apply(double x, double y) {
            return x * y;
        }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) {
            if (y == 0) throw new ArithmeticException("除数不能为0");
            return x / y;
        }
    };

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static void main(String[] args) {
        double x = 10, y = 3;
        for (Operation op : Operation.values()) {
            System.out.printf("%.2f %s %.2f = %.2f%n", x, op, y, op.apply(x, y));
        }
    }
}

interface Calculate {
    double apply(double x, double y);
}
