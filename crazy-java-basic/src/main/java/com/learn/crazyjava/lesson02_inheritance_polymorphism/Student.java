package com.learn.crazyjava.lesson02_inheritance_polymorphism;

/**
 * 第2课：继承与多态 - 子类
 */
public class Student extends Person {
    private double score;

    public Student() {
    }

    public Student(String name, int age, double score) {
        super(name, age);
        this.score = score;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("成绩：" + score);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public static void main(String[] args) {
        // 向上转型
        Person p = new Student("张三", 20, 95.5);
        p.display();  // 调用Student的display

        // 向下转型
        if (p instanceof Student) {
            Student s = (Student) p;
            System.out.println("成绩是：" + s.getScore());
        }
    }
}
