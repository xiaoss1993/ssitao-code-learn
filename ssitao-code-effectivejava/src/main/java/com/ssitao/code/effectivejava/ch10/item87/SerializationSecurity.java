package com.ssitao.code.effectivejava.ch10.item87;

import java.io.*;

/**
 * 条目87：考虑使用自定义序列化方式替代保护性拷贝
 *
 * 本条目强调：
 * 1. 序列化可能导致对象被意外修改
 * 2. readObject需要做防御性拷贝
 * 3. 枚举单例是序列化单例的最佳方式
 */
public class SerializationSecurity {

    // ==================== 枚举单例（最佳方式） ====================
    /**
     * 枚举单例 - 序列化安全，无需额外保护
     * JVM保证枚举实例唯一，即使在反序列化时
     */
    public enum Elvis {
        INSTANCE;

        private String name = "Elvis Presley";

        public void leaveTheBuilding() {
            System.out.println("Whoa baby, I'm out of here!");
        }

        public String getName() {
            return name;
        }

        // 枚举的readResolve确保返回同一个实例
        private Object readResolve() {
            return INSTANCE;  // 返回单例，不是新对象
        }
    }

    // ==================== 问题：可被反序列化创建多个实例 ====================
    /**
     * 普通单例类（如果 implements Serializable就有问题）
     * 问题：反序列化会创建新对象
     */
    public static class BrokenSingleton implements Serializable {
        private static final BrokenSingleton INSTANCE = new BrokenSingleton();

        private BrokenSingleton() {
            // 构造器私有
        }

        public static BrokenSingleton getInstance() {
            return INSTANCE;
        }

        // 问题：反序列化会创建新实例！
        // 解决方案：实现readResolve方法
        private Object readResolve() {
            return INSTANCE;
        }
    }

    // ==================== 完整示例 ====================
    public static class ElvisImpersonator implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String name;
        private final int age;

        public ElvisImpersonator(String name, int age) {
            this.name = name;
            this.age = age;
        }

        // 如果这个对象被反序列化，会创建"假的Elvis"
        // 这就是为什么不应该序列化单例
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("=== 枚举单例 vs 普通单例 ===\n");

        // 枚举单例
        Elvis elvis1 = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;

        System.out.println("枚举单例:");
        System.out.println("elvis1 == elvis2: " + (elvis1 == elvis2));
        System.out.println("elvis1.getName(): " + elvis1.getName());

        // 序列化枚举
        System.out.println("\n序列化枚举...");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(Elvis.INSTANCE);
        byte[] data = baos.toByteArray();
        out.close();

        // 反序列化枚举
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        Elvis elvis3 = (Elvis) in.readObject();
        in.close();

        System.out.println("反序列化后 elvis3 == elvis1: " + (elvis3 == elvis1));
        // JVM保证枚举反序列化后是同一个实例！

        System.out.println("\n=== 序列化安全总结 ===\n");
        System.out.println("1. 枚举单例是序列化的最佳方式");
        System.out.println("2. 普通单例如果可序列化，需要readResolve方法");
        System.out.println("3. 可变对象需要防御性拷贝");
        System.out.println("4. 永远不要序列化不可信对象");
    }
}
