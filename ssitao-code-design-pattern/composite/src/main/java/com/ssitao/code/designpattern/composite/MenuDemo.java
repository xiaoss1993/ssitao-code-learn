package com.ssitao.code.designpattern.composite;

/**
 * 菜单组合模式演示
 */
public class MenuDemo {

    public static void main(String[] args) {
        System.out.println("========== 组合模式 - 菜单系统示例 ==========\n");

        // 创建菜品
        MenuItemExample burger = new MenuItemExample("汉堡", "牛肉汉堡", 15.0, false);
        MenuItemExample salad = new MenuItemExample("沙拉", "蔬菜沙拉", 10.0, true);
        MenuItemExample pizza = new MenuItemExample("披萨", "意大利披萨", 25.0, false);
        MenuItemExample pasta = new MenuItemExample("意面", "番茄意面", 18.0, true);
        MenuItemExample coffee = new MenuItemExample("咖啡", "美式咖啡", 8.0, true);
        MenuItemExample beer = new MenuItemExample("啤酒", "青岛啤酒", 12.0, false);

        // 创建子菜单
        MenuExample breakfastMenu = new MenuExample("早餐菜单", "美味的早餐");
        breakfastMenu.add(burger);
        breakfastMenu.add(salad);

        MenuExample lunchMenu = new MenuExample("午餐菜单", "丰盛的午餐");
        lunchMenu.add(pizza);
        lunchMenu.add(pasta);

        MenuExample drinkMenu = new MenuExample("饮料菜单", "各种饮品");
        drinkMenu.add(coffee);
        drinkMenu.add(beer);

        // 创建主菜单
        MenuExample mainMenu = new MenuExample("主菜单", "全部菜单");
        mainMenu.add(breakfastMenu);
        mainMenu.add(lunchMenu);
        mainMenu.add(drinkMenu);

        // 打印所有菜单
        mainMenu.print();

        // 统计信息
        System.out.println("\n--- 菜单统计 ---");
        System.out.println("早餐总价: " + breakfastMenu.getTotalPrice());
        System.out.println("午餐总价: " + lunchMenu.getTotalPrice());
        System.out.println("饮料总价: " + drinkMenu.getTotalPrice());
        System.out.println("素食菜品数: " + mainMenu.getVegetarianCount());
    }
}
