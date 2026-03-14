package com.ssitao.code.designpattern.prototype.jdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDK中原型模式示例 - 深拷贝 - 手动递归拷贝
 *
 * 优点：
 * 1. 无需引入额外依赖
 * 2. 可精确控制哪些字段需要深拷贝
 * 3. 适合复杂对象图
 *
 * 缺点：
 * 1. 代码量大，需要为每个类型编写处理逻辑
 * 2. 当类结构变化时需要同步更新
 */
public class DeepCopyManual {

    private String name;
    private int age;
    private Address address;
    private List<String> hobbies;
    private Map<String, String> attributes;

    public DeepCopyManual(String name, int age, Address address,
                          List<String> hobbies, Map<String, String> attributes) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hobbies = hobbies;
        this.attributes = attributes;
    }

    /**
     * 手动递归深拷贝
     */
    public DeepCopyManual deepClone() {
        // 创建新对象
        DeepCopyManual cloned = new DeepCopyManual();

        // 基本类型直接复制
        cloned.name = this.name;
        cloned.age = this.age;

        // 引用类型递归拷贝
        cloned.address = this.address != null ? this.address.deepClone() : null;

        // List深拷贝
        if (this.hobbies != null) {
            cloned.hobbies = new ArrayList<>(this.hobbies);
        }

        // Map深拷贝
        if (this.attributes != null) {
            cloned.attributes = new HashMap<>(this.attributes);
        }

        return cloned;
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
    public Map<String, String> getAttributes() { return attributes; }
    public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

    @Override
    public String toString() {
        return "DeepCopyManual{name='" + name + "', age=" + age +
               ", address=" + address + ", hobbies=" + hobbies +
               ", attributes=" + attributes + "}";
    }

    public static void main(String[] args) {
        Address address = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");
        Map<String, String> attributes = new HashMap<>();
        attributes.put("职业", "工程师");
        attributes.put("等级", "高级");

        DeepCopyManual original = new DeepCopyManual("张三", 25, address, hobbies, attributes);
        DeepCopyManual cloned = original.deepClone();

        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);

        System.out.println("\n--- 验证引用独立性 ---");
        System.out.println("地址引用相同? " + (original.getAddress() == cloned.getAddress()));
        System.out.println("列表引用相同? " + (original.getHobbies() == cloned.getHobbies()));
        System.out.println("Map引用相同? " + (original.getAttributes() == cloned.getAttributes()));

        // 修改原始对象
        original.getAddress().setCity("上海");
        original.getHobbies().add("游泳");
        original.getAttributes().put("等级", "资深");

        System.out.println("\n--- 修改原始对象后 ---");
        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
    }

    public DeepCopyManual() {
    }

    /**
     * 内部类Address也需要实现深拷贝
     */
    public static class Address {
        private String city;
        private String district;

        public Address(String city, String district) {
            this.city = city;
            this.district = district;
        }

        public Address() {
        }

        /**
         * Address深拷贝
         */
        public Address deepClone() {
            Address cloned = new Address();
            cloned.city = this.city;
            cloned.district = this.district;
            return cloned;
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
