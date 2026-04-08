package com.ssitao.code.jdk.phase03.reflection;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射示例代码
 * 演示反射的基本用法
 */
public class ReflectionDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Reflection Demo ===\n");

        // 1. 获取Class对象
        System.out.println("--- 获取Class对象 ---");
        Class<?> clazz1 = Class.forName("com.ssitao.code.jdk.phase03.reflection.Person");
        Class<?> clazz2 = Person.class;
        Person person = new Person("Alice", 25);
        Class<?> clazz3 = person.getClass();

        System.out.println("Class name: " + clazz1.getName());
        System.out.println("Simple name: " + clazz1.getSimpleName());
        System.out.println("Package: " + clazz1.getPackage());
        System.out.println("Same class: " + (clazz1 == clazz2 && clazz2 == clazz3));

        // 2. 获取类修饰符
        System.out.println("\n--- 类修饰符 ---");
        int modifiers = clazz1.getModifiers();
        System.out.println("Is public: " + Modifier.isPublic(modifiers));
        System.out.println("Is final: " + Modifier.isFinal(modifiers));
        System.out.println("Modifiers: " + Modifier.toString(modifiers));

        // 3. 获取父类和接口
        System.out.println("\n--- 父类和接口 ---");
        System.out.println("Superclass: " + clazz1.getSuperclass());
        System.out.println("Interfaces: " + Arrays.toString(clazz1.getInterfaces()));

        // 4. 获取构造方法
        System.out.println("\n--- 构造方法 ---");
        Constructor<?>[] constructors = clazz1.getConstructors();
        for (Constructor<?> c : constructors) {
            System.out.println("Constructor: " + c.getName() + " - " + Arrays.toString(c.getParameterTypes()));
        }

        // 5. 获取成员变量
        System.out.println("\n--- 成员变量 ---");
        Field[] fields = clazz1.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("Field: " + Modifier.toString(f.getModifiers()) + " " +
                             f.getType().getSimpleName() + " " + f.getName());
        }

        // 6. 获取成员方法
        System.out.println("\n--- 成员方法 ---");
        Method[] methods = clazz1.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("Method: " + Modifier.toString(m.getModifiers()) + " " +
                             m.getReturnType().getSimpleName() + " " + m.getName() +
                             Arrays.toString(m.getParameterTypes()));
        }

        // 7. 动态创建对象
        System.out.println("\n--- 动态创建对象 ---");
        Constructor<?> constructor = clazz1.getConstructor(String.class, int.class);
        Person p1 = (Person) constructor.newInstance("Bob", 30);
        System.out.println("Created: " + p1);

        // 8. 动态调用方法
        System.out.println("\n--- 动态调用方法 ---");
        Method getNameMethod = clazz1.getMethod("getName");
        Method setNameMethod = clazz1.getMethod("setName", String.class);

        System.out.println("getName: " + getNameMethod.invoke(p1));
        setNameMethod.invoke(p1, "Charlie");
        System.out.println("After setName: " + p1.getName());

        // 9. 访问private成员
        System.out.println("\n--- 访问private成员 ---");
        Field privateField = clazz1.getDeclaredField("id");
        privateField.setAccessible(true);  // 打破封装
        System.out.println("Private field id: " + privateField.get(p1));

        Method privateMethod = clazz1.getDeclaredMethod("privateMethod");
        privateMethod.setAccessible(true);
        privateMethod.invoke(p1);

        // 10. 动态操作字段
        System.out.println("\n--- 动态操作字段 ---");
        Field nameField = clazz1.getDeclaredField("name");
        nameField.setAccessible(true);
        System.out.println("Before: " + nameField.get(p1));
        nameField.set(p1, "David");
        System.out.println("After: " + nameField.get(p1));

        // 11. 静态成员
        System.out.println("\n--- 静态成员 ---");
        Field countField = clazz1.getDeclaredField("count");
        countField.setAccessible(true);
        System.out.println("Static count: " + countField.get(null));

        Method staticMethod = clazz1.getDeclaredMethod("staticMethod");
        staticMethod.invoke(null);

        // 12. 通用toString实现
        System.out.println("\n--- 通用toString ---");
        System.out.println("Using reflection: " + reflectionToString(p1));
    }

    // 通用toString实现
    public static String reflectionToString(Object obj) {
        if (obj == null) return "null";
        StringBuilder sb = new StringBuilder();
        Class<?> clazz = obj.getClass();
        sb.append(clazz.getSimpleName()).append("{");
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            sb.append(field.getName()).append("=");
            try {
                Object value = field.get(obj);
                sb.append(value);
            } catch (IllegalAccessException e) {
                sb.append("?");
            }
            sb.append(", ");
        }
        if (sb.length() > clazz.getSimpleName().length() + 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    // 测试类
    static class Person {
        private static int count = 0;
        private long id;
        public String name;
        protected int age;

        public Person() {
            this.id = ++count;
        }

        public Person(String name, int age) {
            this.id = ++count;
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
            this.age = age;
        }

        private void privateMethod() {
            System.out.println("  [Private method called]");
        }

        public static void staticMethod() {
            System.out.println("  [Static method called]");
        }

        @Override
        public String toString() {
            return "Person{id=" + id + ", name='" + name + "', age=" + age + '}';
        }
    }
}
