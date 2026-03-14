package com.ssitao.code.designpattern.prototype.jdk;

import java.util.List;

/**
 * JDK中原型模式示例 - 深拷贝 - clone()方法重写
 *
 * 实现要点：
 * 1. 类实现Cloneable接口
 * 2. 重写clone()方法，访问修饰符改为public
 * 3. 对引用类型字段进行单独克隆
 */
public class DeepCopyByClone implements Cloneable {

    private String name;
    private int age;
    private Address address;
    private List<String> hobbies;

    public DeepCopyByClone(String name, int age, Address address, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies;
    }

    /**
     * 深拷贝实现 - 重写clone方法
     */
    @Override
    public DeepCopyByClone clone() {
        try {
            DeepCopyByClone cloned = (DeepCopyByClone) super.clone();
            // 单独克隆引用类型字段
            cloned.address = this.address.clone();
            // 对于不可变对象或需要独立修改的List，创建新的实例
            cloned.hobbies = new java.util.ArrayList<>(this.hobbies);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
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
        return "DeepCopyByClone{name='" + name + "', age=" + age +
               ", address=" + address + ", hobbies=" + hobbies + "}";
    }

    public static void main(String[] args) {
        Address address = new Address("北京", "朝阳区");
        List<String> hobbies = new java.util.ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");

        DeepCopyByClone original = new DeepCopyByClone("张三", 25, address, hobbies);
        DeepCopyByClone cloned = original.clone();

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
    }

    /**
     * 内部类Address也需要实现Cloneable
     */
    public static class Address implements Cloneable {
        private String city;
        private String district;

        public Address(String city, String district) {
            this.city = city;
            this.district = district;
        }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Clone not supported", e);
            }
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
