package com.ssitao.code.jdk.phase06.object;

import java.io.*;
import java.util.*;

/**
 * 对象序列化示例
 */
public class ObjectStreamDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Object Serialization Demo ===\n");

        // 1. 基本序列化
        demonstrateBasicSerialization();

        // 2. 自定义序列化
        demonstrateCustomSerialization();

        // 3. 集合序列化
        demonstrateCollectionSerialization();
    }

    private static void demonstrateBasicSerialization() throws Exception {
        System.out.println("--- Basic Serialization ---");

        String file = "person.ser";

        // 序列化
        Person person = new Person("Alice", 25, "secret123");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(person);
        }
        System.out.println("Serialized Person: " + person);

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            Person restored = (Person) ois.readObject();
            System.out.println("Deserialized Person: " + restored);
            System.out.println("Password (transient): " + restored.getPassword());
        }

        // 清理
        new File(file).delete();
        System.out.println();
    }

    private static void demonstrateCustomSerialization() throws Exception {
        System.out.println("--- Custom Serialization ---");

        String file = "account.ser";

        // 序列化（密码会被加密）
        Account account = new Account("user123", "mypassword");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(account);
        }
        System.out.println("Serialized Account: username=" + account.getUsername() +
                           ", password=" + account.getPassword());

        // 反序列化（密码会被解密）
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            Account restored = (Account) ois.readObject();
            System.out.println("Deserialized Account: username=" + restored.getUsername() +
                               ", password=" + restored.getPassword());
        }

        // 清理
        new File(file).delete();
        System.out.println();
    }

    private static void demonstrateCollectionSerialization() throws Exception {
        System.out.println("--- Collection Serialization ---");

        String file = "collection.ser";

        // 创建包含各种对象的集合
        List<Object> list = new ArrayList<>();
        list.add(new Person("Alice", 25, null));
        list.add(new Person("Bob", 30, null));
        list.add(Arrays.asList(1, 2, 3, 4, 5));
        list.add(new HashMap<String, Integer>() {{
            put("one", 1);
            put("two", 2);
        }});

        // 序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {
            oos.writeObject(list);
        }
        System.out.println("Serialized collection with " + list.size() + " elements");

        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            @SuppressWarnings("unchecked")
            List<Object> restored = (List<Object>) ois.readObject();
            System.out.println("Deserialized collection:");
            for (Object item : restored) {
                System.out.println("  - " + item.getClass().getSimpleName() + ": " + item);
            }
        }

        // 清理
        new File(file).delete();
        System.out.println();
    }

    // ===== 可序列化的类 =====

    static class Person implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int age;
        private transient String password;  // 不序列化

        public Person(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public String getPassword() { return password; }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    // ===== 自定义序列化的类 =====

    static class Account implements Serializable {
        private static final long serialVersionUID = 1L;

        private String username;
        private transient String password;  // 自定义序列化

        public Account(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }

        // 自定义序列化：加密密码
        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.defaultWriteObject();
            // 简单加密：每个字符+1
            String encrypted = encrypt(password);
            oos.writeUTF(encrypted);
        }

        // 自定义反序列化：解密密码
        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject();
            // 读取加密的密码并解密
            String encrypted = ois.readUTF();
            this.password = decrypt(encrypted);
        }

        private String encrypt(String s) {
            if (s == null) return null;
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                sb.append((char)(c + 1));
            }
            return sb.toString();
        }

        private String decrypt(String s) {
            if (s == null) return null;
            StringBuilder sb = new StringBuilder();
            for (char c : s.toCharArray()) {
                sb.append((char)(c - 1));
            }
            return sb.toString();
        }
    }
}
