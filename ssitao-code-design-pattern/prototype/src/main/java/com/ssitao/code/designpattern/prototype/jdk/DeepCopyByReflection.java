package com.ssitao.code.designpattern.prototype.jdk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDK中原型模式示例 - 深拷贝 - 反射通用实现
 *
 * 使用反射实现通用的深拷贝工具方法
 *
 * 优点：
 * 1. 通用性强，一次实现可用于所有类
 * 2. 无需修改原类
 *
 * 缺点：
 * 1. 性能较差
 * 2. 无法处理循环引用
 * 3. 只能处理基本类型和常见集合
 */
public class DeepCopyByReflection {

    /**
     * 使用反射进行深拷贝
     */
    public static <T> T deepCopy(T object) {
        if (object == null) {
            return null;
        }

        Class<?> clazz = object.getClass();

        // 处理基本类型和String
        if (isPrimitiveOrString(clazz)) {
            return object;
        }

        // 处理数组
        if (clazz.isArray()) {
            return deepCopyArray(object);
        }

        // 处理集合
        if (List.class.isAssignableFrom(clazz)) {
            return (T) deepCopyList((List<?>) object);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return (T) deepCopyMap((Map<?, ?>) object);
        }

        // 处理普通对象
        return deepCopyObject(object);
    }

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
               clazz == Short.class;
    }

    private static <T> T deepCopyArray(T array) {
        Class<?> componentType = array.getClass().getComponentType();
        if (componentType.isPrimitive()) {
            return array; // 基本类型数组直接返回引用
        }

        Object[] original = (Object[]) array;
        Object[] cloned = original.clone();
        for (int i = 0; i < original.length; i++) {
            cloned[i] = deepCopy(original[i]);
        }
        return (T) cloned;
    }

    private static List<?> deepCopyList(List<?> original) {
        List<Object> cloned = new ArrayList<>();
        for (Object item : original) {
            cloned.add(deepCopy(item));
        }
        return cloned;
    }

    private static Map<?, ?> deepCopyMap(Map<?, ?> original) {
        Map<Object, Object> cloned = new HashMap<>();
        for (Map.Entry<?, ?> entry : original.entrySet()) {
            cloned.put(deepCopy(entry.getKey()), deepCopy(entry.getValue()));
        }
        return cloned;
    }

    @SuppressWarnings("unchecked")
    private static <T> T deepCopyObject(T object) {
        try {
            Class<?> clazz = object.getClass();
            T cloned = (T) clazz.getDeclaredConstructor().newInstance();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                // 静态字段跳过
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                Object value = field.get(object);
                if (value != null) {
                    Object copiedValue = deepCopy(value);
                    field.set(cloned, copiedValue);
                }
            }

            return cloned;
        } catch (Exception e) {
            throw new RuntimeException("Deep copy failed for: " + object.getClass(), e);
        }
    }

    public static void main(String[] args) {
        // 创建测试对象
        Address addr = new Address("北京", "朝阳区");
        List<String> hobbies = new ArrayList<>();
        hobbies.add("篮球");
        hobbies.add("足球");
        Map<String, String> attrs = new HashMap<>();
        attrs.put("职业", "工程师");

        Person original = new Person("张三", 25, addr, hobbies, attrs);

        // 深拷贝
        Person cloned = deepCopy(original);

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
    public static class Person {
        private String name;
        private int age;
        private Address address;
        private List<String> hobbies;
        private Map<String, String> attributes;

        public Person(String name, int age, Address address,
                     List<String> hobbies, Map<String, String> attributes) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.hobbies = hobbies;
            this.attributes = attributes;
        }

        public String getName() { return name; }
        public int getAge() { return age; }
        public Address getAddress() { return address; }
        public List<String> getHobbies() { return hobbies; }
        public Map<String, String> getAttributes() { return attributes; }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age +
                   ", address=" + address + ", hobbies=" + hobbies +
                   ", attributes=" + attributes + "}";
        }
    }

    public static class Address {
        private String city;
        private String district;

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
