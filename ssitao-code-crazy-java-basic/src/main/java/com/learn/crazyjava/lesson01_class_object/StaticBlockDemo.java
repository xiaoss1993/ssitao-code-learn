package com.learn.crazyjava.lesson01_class_object;

/**
 * 静态代码块演示
 */
public class StaticBlockDemo {
    static {
        System.out.println("静态代码块执行");
    }

    public StaticBlockDemo() {
        System.out.println("构造器执行");
    }

    public static void main(String[] args) {
        System.out.println("--- 第一次创建对象 ---");
        new StaticBlockDemo();
        System.out.println("--- 第二次创建对象 ---");
        new StaticBlockDemo();
    }
}
