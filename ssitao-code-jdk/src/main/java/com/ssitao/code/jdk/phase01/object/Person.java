package com.ssitao.code.jdk.phase01.object;

/**
 * 第一阶段步骤1: Object类 - 演示equals()、hashCode()、toString()的用法
 */
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 重写equals方法
     * 步骤:
     * 1. 检查是否为同一个引用 (this == obj)
     * 2. 检查是否为null
     * 3. 检查是否为同一个类 (getClass() == obj.getClass())
     * 4. 比较关键字段
     */
    @Override
    public boolean equals(Object obj) {
        // 1. 检查是否为同一个引用
        if (this == obj) {
            return true;
        }
        // 2. 检查是否为null
        if (obj == null) {
            return false;
        }
        // 3. 检查是否为同一个类
        if (getClass() != obj.getClass()) {
            return false;
        }
        // 4. 比较关键字段
        Person other = (Person) obj;
        return age == other.age && (name == null ? other.name == null : name.equals(other.name));
    }

    /**
     * 重写hashCode方法
     * 黄金法则: equals()返回true的两个对象, hashCode()必须返回相同的值
     *
     * 公式: result = 31 * result + 字段hashCode
     */
    @Override
    public int hashCode() {
        int result = 17;  // 初始质数
        result = 31 * result + age;
        result = 31 * result + (name == null ? 0 : name.hashCode());
        return result;
    }

    /**
     * 重写toString方法, 方便调试和日志输出
     */
    @Override
    public String toString() {
        return "Person{name='" + name + '\'' + ", age=" + age + '}';
    }

    // Getter方法
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // 测试方法
    public static void main(String[] args) {
        System.out.println("=== Object类方法演示 ===\n");

        Person p1 = new Person("Alice", 25);
        Person p2 = new Person("Alice", 25);
        Person p3 = new Person("Bob", 30);

        // 测试toString
        System.out.println("--- toString() ---");
        System.out.println("p1.toString() = " + p1);
        System.out.println("默认Object.toString() = " + new Object());
        System.out.println();

        // 测试equals
        System.out.println("--- equals() ---");
        System.out.println("p1.equals(p2) = " + p1.equals(p2) + " (相同属性)");
        System.out.println("p1.equals(p3) = " + p1.equals(p3) + " (不同属性)");
        System.out.println("p1.equals(null) = " + p1.equals(null));
        System.out.println();

        // 测试hashCode
        System.out.println("--- hashCode() ---");
        System.out.println("p1.hashCode() = " + p1.hashCode());
        System.out.println("p2.hashCode() = " + p2.hashCode() + " (与p1相等,验证hashCode一致性)");
        System.out.println("p3.hashCode() = " + p3.hashCode());
        System.out.println("p1.hashCode() == p2.hashCode() : " + (p1.hashCode() == p2.hashCode()));
        System.out.println();

        // getClass演示
        System.out.println("--- getClass() ---");
        System.out.println("p1.getClass() = " + p1.getClass());
        System.out.println("\"hello\".getClass() = " + "hello".getClass());
        System.out.println("123.getClass() = " + ((Object)123).getClass());
    }
}
