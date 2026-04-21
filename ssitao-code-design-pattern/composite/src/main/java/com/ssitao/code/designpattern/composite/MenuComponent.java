package com.ssitao.code.designpattern.composite;

/**
 * 菜单组件接口 - 组合模式
 */
public interface MenuComponent {

    // 获取名称
    String getName();

    // 获取描述
    String getDescription();

    // 获取价格
    default double getPrice() {
        return 0;
    }

    // 获取是否素食
    default boolean isVegetarian() {
        return false;
    }

    // 打印
    void print();

    // 获取深度（用于缩进）
    default int getDepth() {
        return 0;
    }
}
