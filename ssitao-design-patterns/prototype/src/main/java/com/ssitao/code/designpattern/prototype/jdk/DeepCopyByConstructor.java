package com.ssitao.code.designpattern.prototype.jdk;

import java.util.ArrayList;
import java.util.List;

/**
 * JDK中原型模式示例 - 深拷贝 - 拷贝构造器模式
 *
 * 优点：
 * 1. 不需要实现Cloneable或Serializable接口
 * 2. 代码清晰直观
 * 3. 可以控制哪些字段需要深拷贝，哪些需要浅拷贝
 *
 * 缺点：
 * 1. 需要为每个类编写拷贝构造器
 * 2. 当类结构变化时需要维护拷贝构造器
 */
public class DeepCopyByConstructor {

    private String name;
    private int age;
    private Address address;
    private List<String> hobbies;

    public DeepCopyByConstructor(String name, int age, Address address, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies;
    }

    /**
     * 拷贝构造器 - 实现深拷贝
     */
    public DeepCopyByConstructor(DeepCopyByConstructor other) {
        this.name = other.name;
        this.age = other.age;
        // 深拷贝：创建新的Address对象
        this.address = new Address(other.address.getCity(), other.address.getDistrict());
        // 深拷贝：创建新的List并复制元素
        this.hobbies = new ArrayList<>(other.hobbies);
    }

    /**
     * 浅拷贝构造器
     */
    public DeepCopyByConstructor shallowCopy(DeepCopyByConstructor other) {
        this.name = other.name;
        this.age = other.age;
        // 浅拷贝：直接引用同一对象
        this.address = other.address;
        this.hobbies = other.hobbies;
        return this;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public List<String> getHobbies() { return hobbies; }
    public void setHobbies(List<String> hobbies) { this.hobbies = hobbies; }

    @Override
    public String toString() {
        return "DeepCopyByConstructor{name='" + name + "', age=" + age +
               ", address=" + address + ", hobbies=" + hobbies + "}";
    }

    public static void main(String[] args) {
        Address address = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");

        // 深拷贝测试
        System.out.println("=== 深拷贝测试 ===");
        DeepCopyByConstructor original = new DeepCopyByConstructor("张三", 25, address, hobbies);
        DeepCopyByConstructor cloned = new DeepCopyByConstructor(original);

        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
        System.out.println("地址引用相同? " + (original.getAddress() == cloned.getAddress()));
        System.out.println("列表引用相同? " + (original.getHobbies() == cloned.getHobbies()));

        // 修改原始对象
        original.getAddress().setCity("上海");
        original.getHobbies().add("游泳");

        System.out.println("\n修改原始对象后:");
        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);

        // 浅拷贝测试
        System.out.println("\n=== 浅拷贝测试 ===");
        DeepCopyByConstructor original2 = new DeepCopyByConstructor("李四", 30, new Address("广州", "天河区"), new ArrayList<>());
        DeepCopyByConstructor shallowCloned = new DeepCopyByConstructor();
        shallowCloned.shallowCopy(original2);

        System.out.println("原始对象: " + original2);
        System.out.println("浅克隆对象: " + shallowCloned);
        System.out.println("地址引用相同? " + (original2.getAddress() == shallowCloned.getAddress()));

        // 修改原始对象会影响浅拷贝对象
        original2.getAddress().setCity("深圳");
        System.out.println("\n修改原始对象后:");
        System.out.println("原始对象: " + original2);
        System.out.println("浅克隆对象: " + shallowCloned);
    }

    public DeepCopyByConstructor() {
    }

    public static class Address {
        private String city;
        private String district;

        public Address() {
        }

        public Address(String city, String district) {
            this.city = city;
            this.district = district;
        }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }

        @Override
        public String toString() {
            return "Address{city='" + city + "', district='" + district + "'}";
        }
    }
}
