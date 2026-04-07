package com.ssitao.code.effectivejava.ch10.item86;

import java.io.*;
import java.util.Date;

/**
 * 条目86：自定义序列化形式
 *
 * 实现readObject和writeObject自定义序列化逻辑
 *
 * 原则：
 * 1. 提供custom serialized form当默认形式不合适时
 * 2. 使用writeObject/readObject自定义逻辑
 * 3. 确保与默认构造器的关系一致
 */
public class CustomSerialization {

    // ==================== 问题：不合理的默认形式 ====================
    /**
     * 默认序列化形式不合适的情况：
     * - 包含派生字段
     * - 包含与序列化形式不一致的字段
     * - 需要验证或转换数据
     */
    public static class Name implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String lastName;
        private final String middleName;
        private final String firstName;

        // 派生字段：fullName可以从其他字段计算
        private transient String fullName;

        public Name(String lastName, String middleName, String firstName) {
            this.lastName = lastName;
            this.middleName = middleName;
            this.firstName = firstName;
            this.fullName = computeFullName();
        }

        private String computeFullName() {
            return firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
        }

        // 问题：fullName在序列化时被忽略，但反序列化后需要重新计算
        // 这时需要自定义readObject

        @Override
        public String toString() {
            return fullName;
        }
    }

    // ==================== 正确：自定义readObject ====================
    /**
     * 自定义序列化的类
     */
    public static class SafeName implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String lastName;
        private final String middleName;
        private final String firstName;

        // transient：不需要序列化
        private transient String fullName;

        public SafeName(String lastName, String middleName, String firstName) {
            this.lastName = lastName;
            this.middleName = middleName;
            this.firstName = firstName;
            this.fullName = computeFullName();
        }

        private String computeFullName() {
            return firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
        }

        /**
         * 自定义序列化：写入时做什么
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();  // 先写入默认字段
            // 可以额外写入额外数据
        }

        /**
         * 自定义反序列化：读取时做什么
         */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();  // 先读取默认字段
            // 重新计算派生字段
            this.fullName = computeFullName();
        }

        @Override
        public String toString() {
            return fullName;
        }
    }

    // ==================== 防御性拷贝 ====================
    /**
     * 包含可变对象的类需要特别注意
     */
    public static class Period implements Serializable {
        private static final long serialVersionUID = 1L;

        private Date start;
        private Date end;

        public Period(Date start, Date end) {
            this.start = new Date(start.getTime());  // 防御性拷贝
            this.end = new Date(end.getTime());
        }

        /**
         * 序列化时：防御性拷贝
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            out.defaultWriteObject();
            // Date是可变对象，需要确保写入的是副本
        }

        /**
         * 反序列化时：防御性拷贝
         */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            // 重新做防御性拷贝，防止对象被篡改
            this.start = new Date(start.getTime());
            this.end = new Date(end.getTime());
        }

        @Override
        public String toString() {
            return "Period{start=" + start + ", end=" + end + "}";
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("=== 自定义序列化示例 ===\n");

        SafeName name = new SafeName("李", "明", "张");
        System.out.println("原始: " + name);

        // 序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(name);
        byte[] data = baos.toByteArray();
        out.close();

        // 反序列化
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        SafeName deserialized = (SafeName) in.readObject();
        in.close();

        System.out.println("反序列化: " + deserialized);

        System.out.println("\n=== 可变对象的防御性拷贝 ===\n");

        Date start = new Date();
        Date end = new Date(start.getTime() + 10000);
        Period period = new Period(start, end);

        System.out.println("Period: " + period);

        // 修改原Date对象不应该影响Period
        start.setTime(0);
        System.out.println("修改原Date后: " + period);

        System.out.println("\n=== 序列化最佳实践 ===\n");
        System.out.println("1. 谨慎序列化：数据可能被攻击者利用");
        System.out.println("2. 使用transient排除不需要序列化的字段");
        System.out.println("3. 可变对象字段要防御性拷贝");
        System.out.println("4. 考虑使用JSON等更安全的替代方案");
    }
}
