package com.ssitao.code.designpattern.decorator;

/**
 * 糖装饰器 - 具体装饰器
 */
public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", 加糖";
    }

    @Override
    public double getCost() {
        return super.getCost() + 1.5;
    }
}
