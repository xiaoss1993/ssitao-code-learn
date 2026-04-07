package com.ssitao.code.effectivejava.ch08.item48;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 条目48：需要精确答案时避免使用float和double
 *
 * float和double是二进制浮点数，不适合：
 * - 财务计算
 * - 货币计算
 * - 需要精确结果的计算
 *
 * 原因：二进制浮点数无法精确表示0.1等小数
 */
public class PrimitiveTypes {
    public static void main(String[] args) {
        System.out.println("=== Float/Double精度问题 ===\n");

        // 错误：用double计算money - 演示精度问题
        double funds = 1.00;
        double price = 0.10;
        int count = 0;
        // 固定循环次数10次，演示精度误差
        for (int i = 0; i < 10; i++) {
            funds -= price;
            count++;
        }
        System.out.println("购买物品数: " + count);
        System.out.println("剩余金额: $" + funds);
        // 问题：由于精度误差，结果不是预期的 $0.00
        // 结果：错误！应该是0.8，但实际是0.7999999999999999

        System.out.println("\n=== 正确：使用BigDecimal ===\n");

        // 正确：用BigDecimal计算money
        BigDecimal price2 = new BigDecimal("0.10");
        BigDecimal funds2 = new BigDecimal("1.00");
        int count2 = 0;
        // 固定循环10次，正确计算
        for (int i = 0; i < 10; i++) {
            funds2 = funds2.subtract(price2);
            count2++;
        }
        System.out.println("购买物品数: " + count2);
        System.out.println("剩余金额: $" + funds2);

        System.out.println("\n=== 原始类型总结 ===\n");

        System.out.println("| 类型   | 位宽 | 精度   | 用途 |");
        System.out.println("|--------|------|--------|------|");
        System.out.println("| byte   | 8    | 精确   | 二进制数据 |");
        System.out.println("| short  | 16   | 精确   | 很少使用 |");
        System.out.println("| int    | 32   | 精确   | 整数运算默认 |");
        System.out.println("| long   | 64   | 精确   | 大整数 |");
        System.out.println("| float  | 32   | 二进制浮点 | 很少用，图形 |");
        System.out.println("| double | 64   | 二进制浮点 | 科学计算 |");
        System.out.println("| BigDecimal | 任意 | 精确 | 货币 |");

        System.out.println("\n=== 何时使用哪种类型 ===\n");

        // int: 默认整数运算
        int i = 1 + 2;  // OK

        // long: int不够用时
        long bigNumber = 1_000_000_000_000L;

        // double: 科学计算、图形
        double pi = 3.14159265359;
        double area = Math.PI * 2 * 2;

        // BigDecimal: 货币、任何需要精确计算
        BigDecimal itemPrice = new BigDecimal("19.99");
        BigDecimal tax = itemPrice.multiply(new BigDecimal("0.08")).setScale(2, RoundingMode.HALF_UP);
        System.out.println("含税价格: $" + tax);
    }
}
