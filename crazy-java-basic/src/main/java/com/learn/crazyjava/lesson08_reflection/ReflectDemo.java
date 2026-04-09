package com.learn.crazyjava.lesson08_reflection;

import java.lang.reflect.*;

/**
 * 第8课：反射 - 反射操作
 */
public class ReflectDemo {
    public static void main(String[] args) throws Exception {
        Class<Person> clazz = Person.class;

        // 创建对象
        Person p = clazz.newInstance();

        // 获取方法
        Method setName = clazz.getMethod("setName", String.class);
        Method getName = clazz.getMethod("getName");
        Method speak = clazz.getMethod("speak");

        // 调用方法
        setName.invoke(p, "张三");
        String name = (String) getName.invoke(p);
        speak.invoke(p);

        // 获取并操作私有属性
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        System.out.println("私有属性值：" + nameField.get(p));

        // 获取构造器
        Constructor<Person> constructor = clazz.getConstructor(String.class, int.class);
        Person p2 = constructor.newInstance("李四", 25);
        System.out.println("通过构造器创建：" + p2.getName() + "，" + p2.getAge());
    }
}
