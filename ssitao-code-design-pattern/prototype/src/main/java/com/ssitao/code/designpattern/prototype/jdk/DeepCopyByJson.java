package com.ssitao.code.designpattern.prototype.jdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;
import java.util.List;

/**
 * JDK中原型模式示例 - 深拷贝 - 使用JSON序列化
 *
 * 优点：
 * 1. 简单易用，代码量少
 * 2. 不需要类实现Serializable接口
 * 3. 支持任意复杂对象
 *
 * 缺点：
 * 1. 需要引入Jackson库
 * 2. 性能比二进制序列化稍差
 * 3. 需要无参构造函数
 */
public class DeepCopyByJson {

    private String name;
    private int age;
    private Address address;
    private List<String> hobbies;

    // 必须有无参构造函数用于反序列化
    public DeepCopyByJson() {
    }

    public DeepCopyByJson(String name, int age, Address address, List<String> hobbies) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies;
    }

    /**
     * 使用Jackson进行深拷贝
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public DeepCopyByJson deepClone() {
        try {
            String json = objectMapper.writeValueAsString(this);
            return objectMapper.readValue(json, DeepCopyByJson.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON processing error", e);
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
        return "DeepCopyByJson{name='" + name + "', age=" + age +
               ", address=" + address + ", hobbies=" + hobbies + "}";
    }

    public static void main(String[] args) {
        Address address = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");

        DeepCopyByJson original = new DeepCopyByJson("张三", 25, address, hobbies);
        DeepCopyByJson cloned = original.deepClone();

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
