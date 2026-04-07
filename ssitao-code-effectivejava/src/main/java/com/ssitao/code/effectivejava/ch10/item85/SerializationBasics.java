package com.ssitao.code.effectivejava.ch10.item85;

import java.io.*;

/**
 * 条目85：优先使用Java序列化替代其他选择
 *
 * 序列化的风险：
 * 1. 安全问题：反序列化可能执行任意代码
 * 2. 兼容性：版本兼容性难以维护
 * 3. 性能：序列化开销大
 *
 * 原则：
 * - 避免序列化不可信数据
 * - 使用自定义序列化格式更安全可控
 */
public class SerializationBasics {

    // ==================== 基本序列化 ====================
    /**
     * 实现Serializable接口即可序列化
     */
    public static class Person implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int age;
        // transient字段不会被序列化
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

    // ==================== 序列化到文件 ====================
    public static void serializeToFile(Person person, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            out.writeObject(person);
            System.out.println("序列化成功: " + person);
        }
    }

    public static Person deserializeFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(filename))) {
            return (Person) in.readObject();
        }
    }

    // ==================== 关键问题：反序列化漏洞 ====================
    /**
     * 反序列化攻击示例（危险代码，仅演示）
     *
     * 恶意序列化数据可以执行任意代码，这就是为什么：
     * 1. 不要反序列化不可信数据
     * 2. 使用白名单机制
     * 3. 考虑使用JSON等更安全的替代方案
     */
    public static void demonstrateDanger() {
        System.out.println("\n=== 序列化安全风险 ===");
        System.out.println("反序列化可以执行构造函数和getter/setter");
        System.out.println("恶意构造的序列化数据可能导致：");
        System.out.println("1. 远程代码执行");
        System.out.println("2. 拒绝服务攻击(DoS)");
        System.out.println("3. 绕过安全检查");
    }

    public static void main(String[] args) {
        System.out.println("=== 基本序列化示例 ===\n");

        Person person = new Person("张三", 30, "secret123");

        // transient字段不会被序列化
        System.out.println("原始对象: " + person);

        try {
            // 序列化
            serializeToFile(person, "person.ser");

            // 反序列化
            Person deserialized = deserializeFromFile("person.ser");
            System.out.println("反序列化后: " + deserialized);
            // 注意：password变成null，因为它是transient的

            demonstrateDanger();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
