package com.ssitao.code.designpattern.decorator;

/**
 * 咖啡装饰器基类 - 抽象装饰器
 * 装饰器模式中的Decorator(装饰器)角色
 */
public abstract class CoffeeDecorator implements Coffee {

    // 持有被装饰对象的引用
    protected Coffee decoratedCoffee;

    public CoffeeDecorator(Coffee coffee) {
        this.decoratedCoffee = coffee;
    }

    @Override
    public String getDescription() {
        return decoratedCoffee.getDescription();
    }

    @Override
    public double getCost() {
        return decoratedCoffee.getCost();
    }
}
