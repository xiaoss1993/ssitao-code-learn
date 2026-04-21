package com.ssitao.code.designpattern.composite;

/**
 * 员工接口 - 组合模式组件
 */
public interface Employee {

    // 获取姓名
    String getName();

    // 获取工资
    double getSalary();

    // 打印员工信息
    void print();

    // 获取下属数量
    default int getSubordinateCount() {
        return 0;
    }
}
