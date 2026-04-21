package com.ssitao.code.designpattern.prototype.jdk;

import net.sf.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * JDK中原型模式示例 - 深拷贝 - 使用Cglib BeanCopier
 *
 * Cglib的BeanCopier是高性能的bean拷贝工具
 * 但默认只支持浅拷贝，这里扩展实现深拷贝
 *
 * 优点：
 * 1. 性能极高，比反射快很多
 * 2. 代码简洁
 *
 * 缺点：
 * 1. 需要引入Cglib库
 * 2. 深拷贝需要额外处理
 */
public class DeepCopyByCglib {

    // 缓存BeanCopier实例
    private static final Map<String, BeanCopier> COPIER_CACHE = new HashMap<>();

    /**
     * 使用Cglib进行浅拷贝
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        String key = sourceClass.getName() + "->" + targetClass.getName();

        BeanCopier copier = COPIER_CACHE.get(key);
        if (copier == null) {
            copier = BeanCopier.create(sourceClass, targetClass, false);
            COPIER_CACHE.put(key, copier);
        }

        copier.copy(source, target, null);
    }

    /**
     * 使用Cglib进行深拷贝（需要目标类有无参构造函数）
     */
    public static <T> T deepCopy(T source) {
        if (source == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        Class<T> sourceClass = (Class<T>) source.getClass();

        T target;
        try {
            target = sourceClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Cannot create instance of: " + sourceClass, e);
        }

        // 浅拷贝所有属性
        copyProperties(source, target);

        // 对引用类型进行递归深拷贝
        deepCopyReferences(source, target, sourceClass);

        return target;
    }

    /**
     * 对引用类型属性进行递归深拷贝
     */
    private static void deepCopyReferences(Object source, Object target, Class<?> clazz) {
        try {
            PropertyDescriptor[] pds = getPropertyDescriptors(clazz);

            for (PropertyDescriptor pd : pds) {
                Method readMethod = pd.getReadMethod();
                Method writeMethod = pd.getWriteMethod();

                if (readMethod == null || writeMethod == null) {
                    continue;
                }

                Object value = readMethod.invoke(source);
                if (value != null && !isPrimitiveOrString(value.getClass())) {
                    // 递归深拷贝
                    Object copiedValue = deepCopy(value);
                    writeMethod.invoke(target, copiedValue);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deep copy property", e);
        }
    }

    /**
     * 获取属性描述符（兼容JDK8）
     */
    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws Exception {
        Method[] methods = clazz.getDeclaredMethods();
        Map<String, PropertyDescriptor> pdMap = new HashMap<>();

        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") && name.length() > 3 && method.getParameterCount() == 0) {
                String propertyName = name.substring(3, 4).toLowerCase() + name.substring(4);
                String setterName = "set" + name.substring(3);
                try {
                    Method setter = clazz.getMethod(setterName, method.getReturnType());
                    pdMap.put(propertyName, new PropertyDescriptor(propertyName, method, setter));
                } catch (NoSuchMethodException e) {
                    // no setter, skip
                }
            }
        }

        return pdMap.values().toArray(new PropertyDescriptor[0]);
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
               clazz == Short.class ||
               clazz.isEnum();
    }

    public static void main(String[] args) {
        // 创建测试对象
        Address addr = new Address("北京", "朝阳区");
        Map<String, String> attrs = new HashMap<>();
        attrs.put("职业", "工程师");

        Person original = new Person("张三", 25, addr, attrs);

        // 深拷贝
        Person cloned = deepCopy(original);

        System.out.println("原始对象: " + original);
        System.out.println("克隆对象: " + cloned);

        System.out.println("\n--- 验证引用独立性 ---");
        System.out.println("地址引用相同? " + (original.getAddress() == cloned.getAddress()));
        System.out.println("Map引用相同? " + (original.getAttributes() == cloned.getAttributes()));

        // 修改原始对象
        original.getAddress().setCity("上海");
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
        private Map<String, String> attributes;

        public Person() {
        }

        public Person(String name, int age, Address address, Map<String, String> attributes) {
            this.name = name;
            this.age = age;
            this.address = address;
            this.attributes = attributes;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public Address getAddress() { return address; }
        public void setAddress(Address address) { this.address = address; }
        public Map<String, String> getAttributes() { return attributes; }
        public void setAttributes(Map<String, String> attributes) { this.attributes = attributes; }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age +
                   ", address=" + address + ", attributes=" + attributes + "}";
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
