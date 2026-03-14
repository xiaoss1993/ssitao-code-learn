package com.ssitao.code.designpattern.decorator;

/**
 * 简单咖啡 - 具体组件
 * 装饰器模式中的Component(组件)角色
 */
public class SimpleCoffee implements Coffee {

    @Override
    public String getDescription() {
        return "简单咖啡";
    }

    @Override
    public double getCost() {
        return 10.0;
    }
}
