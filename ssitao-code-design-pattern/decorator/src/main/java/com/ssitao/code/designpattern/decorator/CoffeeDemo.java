package com.ssitao.code.designpattern.decorator;

/**
 * 咖啡店装饰器模式演示
 */
public class CoffeeDemo {

    public static void main(String[] args) {
        System.out.println("========== 装饰器模式 - 咖啡店示例 ==========\n");

        // 1. 简单咖啡
        Coffee coffee = new SimpleCoffee();
        System.out.println("订单: " + coffee.getDescription());
        System.out.println("价格: " + coffee.getCost());

        // 2. 咖啡 + 牛奶
        Coffee coffeeWithMilk = new MilkDecorator(new SimpleCoffee());
        System.out.println("\n订单: " + coffeeWithMilk.getDescription());
        System.out.println("价格: " + coffeeWithMilk.getCost());

        // 3. 咖啡 + 牛奶 + 糖
        Coffee coffeeWithMilkAndSugar = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        System.out.println("\n订单: " + coffeeWithMilkAndSugar.getDescription());
        System.out.println("价格: " + coffeeWithMilkAndSugar.getCost());

        // 4. 咖啡 + 牛奶 + 糖 + 奶油
        Coffee fancyCoffee = new WhipDecorator(new SugarDecorator(new MilkDecorator(new SimpleCoffee())));
        System.out.println("\n订单: " + fancyCoffee.getDescription());
        System.out.println("价格: " + fancyCoffee.getCost());

        // 5. 双份牛奶 + 糖
        Coffee doubleMilk = new MilkDecorator(new MilkDecorator(new SugarDecorator(new SimpleCoffee())));
        System.out.println("\n订单: " + doubleMilk.getDescription());
        System.out.println("价格: " + doubleMilk.getCost());
    }
}
