package com.learn.crazyjava.lesson10_io;

import java.io.*;

/**
 * 第10课：I/O流 - 对象序列化
 */
public class ObjectSerializationDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Person person = new Person("张三", 25, "123456");

        // 序列化
        String file = "person.dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            oos.writeObject(person);
            System.out.println("序列化完成");
        }

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            Person restored = (Person) ois.readObject();
            System.out.println("反序列化：" + restored);
        }
    }
}

class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    // transient不参与序列化
    private transient String password;

    public Person(String name, int age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", password='" + password + "'}";
    }
}
