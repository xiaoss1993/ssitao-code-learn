package com.ssitao.code.designpattern.prototype.spring;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring中原型模式示例 - 深拷贝 - 使用Spring BeanUtils
 *
 * BeanUtils.copyProperties() 默认是浅拷贝
 * 这里提供一个深拷贝的扩展实现
 *
 * 优点：
 * 1. 利用Spring现有工具
 * 2. 代码简洁
 * 3. 自动匹配属性
 *
 * 缺点：
 * 1. 需要处理循环引用
 * 2. 性能相对较差
 */
public class DeepCopyBySpringBeanUtils {

    /**
     * Spring BeanUtils 深拷贝
     * 基于Spring的BeanUtils扩展实现深拷贝
     */
    public static <T> T deepCopy(T source) {
        if (source == null) {
            return null;
        }

        Class<?> sourceClass = source.getClass();
        T target;

        try {
            // 创建目标对象实例
            target = (T) sourceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new FatalBeanException("Could not create instance of " + sourceClass, e);
        }

        // 复制属性
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));

        // 对引用类型进行递归深拷贝
        deepCopyReferences(source, target, sourceClass);

        return target;
    }

    /**
     * 对引用类型属性进行递归深拷贝
     */
    private static void deepCopyReferences(Object source, Object target, Class<?> clazz) {
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(clazz);

        for (PropertyDescriptor pd : pds) {
            Method readMethod = pd.getReadMethod();
            Method writeMethod = pd.getWriteMethod();

            if (readMethod == null || writeMethod == null) {
                continue;
            }

            try {
                Object value = readMethod.invoke(source);
                if (value != null && !isPrimitiveOrString(value.getClass())) {
                    // 递归深拷贝引用类型
                    Object copiedValue = deepCopy(value);
                    writeMethod.invoke(target, copiedValue);
                }
            } catch (Exception e) {
                throw new FatalBeanException("Failed to deep copy property: " + pd.getName(), e);
            }
        }
    }

    /**
     * 获取空值属性名，用于BeanUtils.copyProperties时跳过null值
     */
    private static String[] getNullPropertyNames(Object source) {
        List<String> nullNames = new ArrayList<>();
        Class<?> clazz = source.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(source) == null) {
                    nullNames.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                // ignore
            }
        }

        return nullNames.toArray(new String[0]);
    }

    /**
     * 判断是否为基本类型或String
     */
    private static boolean isPrimitiveOrString(Class<?> clazz) {
        return clazz.isPrimitive() ||
               clazz == String.class ||
               clazz == Integer.class ||
               clazz == Long.class ||
               clazz == Double.class ||
               clazz == Float.class ||
               clazz == Boolean.class ||
               clazz == Character.class ||
               clazz == Byte.class ||
               clazz == Short.class ||
               clazz.isEnum();
    }

    public static void main(String[] args) {
        // 创建测试对象
        Address addr = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");
        Map<String, String> attrs = new HashMap<>();
        attrs.put("职业", "工程师");

        User original = new User("张三", 25, addr, hobbies, attrs);

        // 深拷贝
        User cloned = deepCopy(original);

        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);

        System.out.println("\n--- 验证引用独立性 ---");
        System.out.println("地址引用相同? " + (original.getAddress() == cloned.getAddress()));
        System.out.println("列表引用相同? " + (original.getHobbies() == cloned.getHobbies()));
        System.out.println("Map引用相同? " + (original.getAttributes() == cloned.getAttributes()));

        // 修改原始对象
        original.getAddress().setCity("上海");
        original.getHobbies().add("游泳");
        original.getAttributes().put("等级", "高级");

        System.out.println("\n--- 修改原始对象后 ---");
        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);
    }

    // 测试用类
    public static class User {
        private String name;
        private int age;
        private Address address;
        private List<String> hobbies;
        private Map<String, String> attributes;

        public User(String name, int age, Address address,
                    List<String> hobbies, Map<String, String> attributes) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.hobbies = hobbies;
            this.attributes = attributes;
        }

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
            return "User{name='" + name + "', age=" + age +
                   ", address=" + address + ", hobbies=" + hobbies +
                   ", attributes=" + attributes + "}";
        }
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
