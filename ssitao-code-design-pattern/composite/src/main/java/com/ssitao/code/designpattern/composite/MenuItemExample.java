package com.ssitao.code.designpattern.composite;

/**
 * 菜单项 - 叶子节点
 */
public class MenuItemExample implements MenuComponent {

    private String name;
    private String description;
    private double price;
    private boolean vegetarian;

    public MenuItemExample(String name, String description, double price, boolean vegetarian) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.vegetarian = vegetarian;
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
    public double getPrice() {
        return price;
    }

    @Override
    public boolean isVegetarian() {
        return vegetarian;
    }

    @Override
    public void print() {
        String veg = vegetarian ? "(素食)" : "";
        System.out.println("  " + name + veg + " -- " + price);
        System.out.println("    -- " + description);
    }
}
