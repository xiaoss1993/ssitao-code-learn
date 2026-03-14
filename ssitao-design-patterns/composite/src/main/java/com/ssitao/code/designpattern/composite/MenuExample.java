package com.ssitao.code.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单 - 组合节点
 */
public class MenuExample implements MenuComponent {

    private String name;
    private String description;
    private List<MenuComponent> components = new ArrayList<>();

    public MenuExample(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void add(MenuComponent component) {
        components.add(component);
    }

    public void remove(MenuComponent component) {
        components.remove(component);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void print() {
        System.out.println("\n" + name + ", " + description);
        System.out.println("============================");
        for (MenuComponent component : components) {
            component.print();
        }
    }

    // 获取所有菜品的价格
    public double getTotalPrice() {
        double total = 0;
        for (MenuComponent component : components) {
            total += component.getPrice();
        }
        return total;
    }

    // 获取素食菜品数量
    public int getVegetarianCount() {
        int count = 0;
        for (MenuComponent component : components) {
            if (component.isVegetarian()) {
                count++;
            }
        }
        return count;
    }
}
