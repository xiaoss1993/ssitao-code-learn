package com.learn.crazyjava.lesson01_class_object;

/**
 * 第1课：类和对象 - 示例类
 */
public class Person {
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
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
        if (age >= 0 && age <= 150) {
            this.age = age;
        }
    }

    public void speak() {
        System.out.println(name + "说：我今年" + age + "岁");
    }

    public static void main(String[] args) {
        Person p = new Person("张三", 25);
        p.speak();

        Person p2 = new Person();
        p2.setName("李四");
        p2.setAge(30);
        p2.speak();
    }
}
