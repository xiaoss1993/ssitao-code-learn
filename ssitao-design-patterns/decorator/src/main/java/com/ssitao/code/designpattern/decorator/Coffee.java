package com.ssitao.code.designpattern.decorator;

/**
 * 咖啡组件接口 - 装饰器模式
 */
public interface Coffee {

    // 获取描述
    String getDescription();

    // 获取成本
    double getCost();
}
