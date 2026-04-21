package com.ssitao.code.designpattern.template.basic;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板方法模式基础示例
 *
 * 模板方法模式特点：
 * 1. 定义算法骨架，将某些步骤延迟到子类
 * 2. 父类定义骨架，子类实现具体步骤
 * 3. 不改变算法结构即可重定义某些步骤
 *
 * 与策略模式区别：
 * - 策略模式：整个算法都可以替换
 * - 模板方法模式：算法骨架不变，部分步骤可变
 *
 * 组成：
 * - AbstractClass: 抽象类，定义模板方法和抽象方法
 * - ConcreteClass: 具体类，实现抽象方法
 */
public class BasicTemplateDemo {

    public static void main(String[] args) {
        System.out.println("=== 模板方法模式基础示例 ===\n");

        // 制作绿茶
        System.out.println("--- 制作绿茶 ---");
        Beverage greenTea = new GreenTea();
        greenTea.prepare();

        System.out.println("\n--- 制作红茶 ---");
        Beverage blackTea = new BlackTea();
        blackTea.prepare();

        System.out.println("\n--- 制作咖啡 ---");
        Beverage coffee = new Coffee();
        coffee.prepare();
    }
}

/**
 * 饮料抽象类 - 模板方法
 */
abstract class Beverage {
    /**
     * 模板方法 - 定义算法骨架
     * final防止子类重写
     */
    public final void prepare() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }

    // 烧水 - 通用实现
    private void boilWater() {
        System.out.println("烧水");
    }

    // 冲泡 - 子类实现
    protected abstract void brew();

    // 倒入杯中 - 通用实现
    private void pourInCup() {
        System.out.println("倒入杯中");
    }

    // 添加调料 - 子类实现
    protected abstract void addCondiments();
}

/**
 * 绿茶
 */
class GreenTea extends Beverage {

    @Override
    protected void brew() {
        System.out.println("冲泡绿茶");
    }

    @Override
    protected void addCondiments() {
        System.out.println("添加柠檬");
    }
}

/**
 * 红茶
 */
class BlackTea extends Beverage {

    @Override
    protected void brew() {
        System.out.println("冲泡红茶");
    }

    @Override
    protected void addCondiments() {
        System.out.println("添加牛奶和糖");
    }
}

/**
 * 咖啡
 */
class Coffee extends Beverage {

    @Override
    protected void brew() {
        System.out.println("冲泡咖啡");
    }

    @Override
    protected void addCondiments() {
        System.out.println("添加糖和牛奶");
    }
}
