package com.ssitao.code.designpattern.prototype.jdk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK中原型模式示例 - 浅拷贝
 */
public class ShallowCopyExample implements Cloneable {

    private String name;
    private int age;
    private List<String> hobbies;

    public ShallowCopyExample(String name, int age, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
    }

    @Override
    public ShallowCopyExample clone() throws CloneNotSupportedException {
        return (ShallowCopyExample) super.clone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "ShallowCopyExample{name='" + name + "', age=" + age + ", hobbies=" + hobbies + "}";
    }

    public static void main(String[] args) throws CloneNotSupportedException {
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");

        ShallowCopyExample original = new ShallowCopyExample("张三", 25, hobbies);
        ShallowCopyExample cloned = original.clone();

        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
        System.out.println("引用相同? " + (original.getHobbies() == cloned.getHobbies()));

        // 修改原始对象的引用类型属性，会影响克隆对象
        original.getHobbies().add("游泳");
        System.out.println("\n修改原始对象的hobbies后:");
        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
    }
}
