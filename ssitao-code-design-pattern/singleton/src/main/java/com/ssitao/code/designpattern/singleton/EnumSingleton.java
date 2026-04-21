package com.ssitao.code.designpattern.singleton;

/**
 * 枚举单例模式
 *
 * 优点：
 * 1. 线程安全(JVM保证)
 * 2. 防止反射和序列化攻击
 * 3. 代码简洁
 *
 * 缺点：
 * 1. 无法延迟加载
 * 2. 无法继承
 */
public enum EnumSingleton {

    INSTANCE;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void showMessage() {
        System.out.println("枚举单例: " + this.hashCode());
    }

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }
}
