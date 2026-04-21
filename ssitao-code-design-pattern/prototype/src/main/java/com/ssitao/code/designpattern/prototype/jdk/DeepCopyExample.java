package com.ssitao.code.designpattern.prototype.jdk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK中原型模式示例 - 深拷贝
 */
public class DeepCopyExample implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private List<String> hobbies;
    private Address address;

    public DeepCopyExample(String name, int age, List<String> hobbies, Address address) {
        this.name = name;
        this.age = age;
        this.hobbies = hobbies;
        this.address = address;
    }

    /**
     * 使用序列化实现深拷贝
     */
    public DeepCopyExample deepClone() throws IOException, ClassNotFoundException {
        // 序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        // 反序列化
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (DeepCopyExample) ois.readObject();
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

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "DeepCopyExample{name='" + name + "', age=" + age + ", hobbies=" + hobbies + ", address=" + address + "}";
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Address address = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");

        DeepCopyExample original = new DeepCopyExample("张三", 25, hobbies, address);
        DeepCopyExample cloned = original.deepClone();

        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
        System.out.println("hobbies引用相同? " + (original.getHobbies() == cloned.getHobbies()));
        System.out.println("address引用相同? " + (original.getAddress() == cloned.getAddress()));

        // 修改原始对象的引用类型属性，不会影响克隆对象
        original.getHobbies().add("游泳");
        original.getAddress().setCity("上海");

        System.out.println("\n修改原始对象的属性后:");
        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
    }

    /**
     * 内部类Address也需要实现Serializable
     */
    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;

        private String city;
        private String district;

        public Address(String city, String district) {
            this.city = city;
            this.district = district;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        @Override
        public String toString() {
            return "Address{city='" + city + "', district='" + district + "'}";
        }
    }
}
