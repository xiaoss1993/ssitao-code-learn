package com.ssitao.code.designpattern.composite;

/**
 * 开发者 - 叶子节点
 */
public class Developer implements Employee {

    private String name;
    private double salary;

    public Developer(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getSalary() {
        return salary;
    }

    @Override
    public void print() {
        System.out.println("  开发者: " + name + ", 工资: " + salary);
    }
}
