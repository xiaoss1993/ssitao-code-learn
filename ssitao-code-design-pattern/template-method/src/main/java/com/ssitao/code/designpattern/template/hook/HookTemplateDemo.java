package com.ssitao.code.designpattern.template.hook;

import java.util.ArrayList;
import java.util.List;

/**
 * 钩子方法示例
 *
 * 钩子方法特点：
 * 1. 提供默认实现，子类可选重写
 * 2. 用于控制父类行为
 * 3. 体现"好莱坞原则"：别调用我，我来调用你
 */
public class HookTemplateDemo {

    public static void main(String[] args) {
        System.out.println("=== 钩子方法示例 ===\n");

        // 制作经典咖啡
        System.out.println("--- 经典咖啡 ---");
        CoffeeWithHook classicCoffee = new ClassicCoffee();
        classicCoffee.prepareRecipe();

        System.out.println("\n--- 脱咖啡因咖啡 ---");
        CoffeeWithHook decafCoffee = new DecafCoffee();
        decafCoffee.prepareRecipe();

        System.out.println("\n--- 用户自定义咖啡 ---");
        CustomCoffee customCoffee = new CustomCoffee();
        customCoffee.setAddMilk(true);
        customCoffee.setAddSugar(true);
        customCoffee.prepareRecipe();
    }
}

/**
 * 咖啡抽象类 - 带钩子
 */
abstract class CoffeeWithHook {
    /**
     * 模板方法
     */
    public final void prepareRecipe() {
        boilWater();
        brewCoffeeGrounds();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }

    // 烧水
    private void boilWater() {
        System.out.println("烧水");
    }

    // 冲泡
    protected abstract void brewCoffeeGrounds();

    // 倒入杯中
    private void pourInCup() {
        System.out.println("倒入杯中");
    }

    // 添加调料
    protected abstract void addCondiments();

    /**
     * 钩子方法 - 默认返回true
     * 子类可以重写
     */
    protected boolean customerWantsCondiments() {
        return true;
    }
}

/**
 * 经典咖啡
 */
class ClassicCoffee extends CoffeeWithHook {

    @Override
    protected void brewCoffeeGrounds() {
        System.out.println("冲泡经典咖啡");
    }

    @Override
    protected void addCondiments() {
        System.out.println("添加牛奶和糖");
    }
}

/**
 * 脱咖啡因咖啡
 */
class DecafCoffee extends CoffeeWithHook {

    @Override
    protected void brewCoffeeGrounds() {
        System.out.println("冲泡脱咖啡因咖啡");
    }

    @Override
    protected void addCondiments() {
        System.out.println("添加豆浆");
    }

    @Override
    protected boolean customerWantsCondiments() {
        return false; // 脱咖啡因默认不加调料
    }
}

/**
 * 自定义咖啡 - 用户选择
 */
class CustomCoffee extends CoffeeWithHook {
    private boolean addMilk = false;
    private boolean addSugar = false;

    public void setAddMilk(boolean addMilk) {
        this.addMilk = addMilk;
    }

    public void setAddSugar(boolean addSugar) {
        this.addSugar = addSugar;
    }

    @Override
    protected void brewCoffeeGrounds() {
        System.out.println("冲泡现磨咖啡");
    }

    @Override
    protected void addCondiments() {
        List<String> condiments = new ArrayList<>();
        if (addMilk) {
            condiments.add("牛奶");
        }
        if (addSugar) {
            condiments.add("糖");
        }
        if (!condiments.isEmpty()) {
            System.out.println("添加: " + String.join(",", condiments));
        }
    }

    @Override
    protected boolean customerWantsCondiments() {
        return addMilk || addSugar;
    }
}
