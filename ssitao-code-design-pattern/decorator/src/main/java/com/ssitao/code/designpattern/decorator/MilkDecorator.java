package com.ssitao.code.designpattern.decorator;

/**
 * 牛奶装饰器 - 具体装饰器
 */
public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", 加牛奶";
    }

    @Override
    public double getCost() {
        return super.getCost() + 3.0;
    }
}
