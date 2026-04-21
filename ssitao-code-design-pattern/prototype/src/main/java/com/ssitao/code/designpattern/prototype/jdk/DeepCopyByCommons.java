package com.ssitao.code.designpattern.prototype.jdk;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * JDK中原型模式示例 - 深拷贝 - 使用Apache Commons Lang3
 *
 * SerializationUtils.serializableClone() 提供简洁的序列化深拷贝方式
 *
 * 优点：
 * 1. 代码简洁，一行代码完成深拷贝
 * 2. 支持任意Serializable对象
 *
 * 缺点：
 * 1. 所有类必须实现Serializable接口
 * 2. 性能比手写clone方法差
 */
public class DeepCopyByCommons implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private Address address;
    private List<String> hobbies;

    public DeepCopyByCommons(String name, int age, Address address, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies;
    }

    /**
     * 使用Apache Commons SerializationUtils进行深拷贝
     */
    public DeepCopyByCommons deepClone() {
        return SerializationUtils.clone(this);
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
        return "DeepCopyByCommons{name='" + name + "', age=" + age +
               ", address=" + address + ", hobbies=" + hobbies + "}";
    }

    public static void main(String[] args) {
        Address address = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");

        DeepCopyByCommons original = new DeepCopyByCommons("张三", 25, address, hobbies);
        DeepCopyByCommons cloned = original.deepClone();

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
     * 内部类必须实现Serializable
     */
    public static class Address implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

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
