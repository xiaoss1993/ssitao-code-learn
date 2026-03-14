package com.ssitao.code.designpattern.composite;

/**
 * 组织架构组合模式演示
 */
public class OrganizationDemo {

    public static void main(String[] args) {
        System.out.println("========== 组合模式 - 组织架构示例 ==========\n");

        // 创建员工
        Developer dev1 = new Developer("张三", 15000);
        Developer dev2 = new Developer("李四", 12000);
        Developer dev3 = new Developer("王五", 14000);
        Developer dev4 = new Developer("赵六", 11000);

        // 创建经理
        Manager techManager = new Manager("技术总监", 30000);
        techManager.addSubordinate(dev1);
        techManager.addSubordinate(dev2);

        Manager productManager = new Manager("产品经理", 25000);
        productManager.addSubordinate(dev3);

        Manager hrManager = new Manager("HR经理", 22000);
        hrManager.addSubordinate(dev4);

        // 创建CEO
        Manager ceo = new Manager("CEO", 50000);
        ceo.addSubordinate(techManager);
        ceo.addSubordinate(productManager);
        ceo.addSubordinate(hrManager);

        // 打印组织架构
        System.out.println("组织架构:");
        ceo.print();

        // 计算团队总工资
        System.out.println("\n--- 团队总工资 ---");
        System.out.println("CEO团队总工资: " + ceo.getTotalSalary());
        System.out.println("技术团队总工资: " + techManager.getTotalSalary());
    }
}
