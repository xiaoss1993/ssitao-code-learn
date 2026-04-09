package com.learn.crazyjava.lesson07_exception;

/**
 * 第7课：异常处理 - 基本使用
 */
public class ExceptionDemo {
    public static void main(String[] args) {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("除数不能为0");
            e.printStackTrace();
        } finally {
            System.out.println("必定执行");
        }

        // 测试自定义异常
        Person p = new Person();
        try {
            p.setAge(200);
        } catch (AgeException e) {
            System.out.println("异常：" + e.getMessage());
        }
    }
}

class Person {
    private int age;

    public void setAge(int age) throws AgeException {
        if (age < 0 || age > 150) {
            throw new AgeException("年龄必须在0-150之间：" + age);
        }
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
