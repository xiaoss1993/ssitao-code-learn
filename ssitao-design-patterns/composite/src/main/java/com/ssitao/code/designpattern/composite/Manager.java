package com.ssitao.code.designpattern.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * 经理 - 组合节点
 * 可以管理下属员工
 */
public class Manager implements Employee {

    private String name;
    private double salary;
    private List<Employee> subordinates = new ArrayList<>();

    public Manager(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    // 添加下属
    public void addSubordinate(Employee employee) {
        subordinates.add(employee);
    }

    // 移除下属
    public void removeSubordinate(Employee employee) {
        subordinates.remove(employee);
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
    public int getSubordinateCount() {
        return subordinates.size();
    }

    @Override
    public void print() {
        System.out.println("经理: " + name + ", 工资: " + salary);
        for (Employee employee : subordinates) {
            employee.print();
        }
    }

    // 获取团队总工资
    public double getTotalSalary() {
        double total = salary;
        for (Employee employee : subordinates) {
            if (employee instanceof Manager) {
                total += ((Manager) employee).getTotalSalary();
            } else {
                total += employee.getSalary();
            }
        }
        return total;
    }
}
