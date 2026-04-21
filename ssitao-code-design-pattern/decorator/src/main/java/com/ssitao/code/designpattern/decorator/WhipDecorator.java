package com.ssitao.code.designpattern.decorator;

/**
 * 奶油装饰器 - 具体装饰器
 */
public class WhipDecorator extends CoffeeDecorator {

    public WhipDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", 加奶油";
    }

    @Override
    public double getCost() {
        return super.getCost() + 2.5;
    }
}
