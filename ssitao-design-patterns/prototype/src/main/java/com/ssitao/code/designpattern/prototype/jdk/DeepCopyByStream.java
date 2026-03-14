package com.ssitao.code.designpattern.prototype.jdk;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JDK中原型模式示例 - 深拷贝 - Java 8+ Stream API
 *
 * 使用Stream API简化集合的深拷贝
 *
 * 优点：
 * 1. 代码简洁现代
 * 2. 函数式编程风格
 * 3. 易于理解和维护
 *
 * 缺点：
 * 1. 只适合集合类型
 * 2. 需要对象本身支持克隆
 */
public class DeepCopyByStream {

    /**
     * 使用Stream API深拷贝List
     */
    public static <T extends Cloneable> List<T> deepCopyList(List<T> original) {
        if (original == null) {
            return null;
        }
        return original.stream()
                .map(item -> {
                    try {
                        @SuppressWarnings("unchecked")
                        T cloned = (T) item.getClass().getMethod("clone").invoke(item);
                        return cloned;
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to clone: " + item.getClass(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 使用Stream API深拷贝Map
     * 注意：key保持原引用（假设String等不可变），value进行深拷贝
     */
    public static <V extends Cloneable> Map<String, V> deepCopyMap(Map<String, V> original) {
        if (original == null) {
            return null;
        }
        return original.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> cloneObject(entry.getValue())
                ));
    }

    /**
     * 通用克隆方法
     */
    @SuppressWarnings("unchecked")
    private static <T> T cloneObject(T obj) {
        if (obj == null) {
            return null;
        }

        // 基本类型直接返回
        if (isPrimitiveOrString(obj)) {
            return obj;
        }

        // 调用clone方法
        try {
            return (T) obj.getClass().getMethod("clone").invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clone: " + obj.getClass(), e);
        }
    }

    private static boolean isPrimitiveOrString(Object obj) {
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() ||
               clazz == String.class ||
               clazz == Integer.class ||
               clazz == Long.class ||
               clazz == Double.class ||
               clazz == Float.class ||
               clazz == Boolean.class;
    }

    public static void main(String[] args) {
        // 创建测试数据
        List<City> originalCities = new ArrayList<>();
        originalCities.add(new City("北京", "华北"));
        originalCities.add(new City("上海", "华东"));
        originalCities.add(new City("广州", "华南"));

        Map<String, City> originalMap = new HashMap<>();
        originalMap.put("首都", new City("北京", "华北"));
        originalMap.put("魔都", new City("上海", "华东"));

        // 深拷贝List
        List<City> clonedCities = deepCopyList(originalCities);

        // 深拷贝Map
        Map<String, City> clonedMap = deepCopyMap(originalMap);

        System.out.println("=== List深拷贝 ===");
        System.out.println("原始List: " + originalCities);
        System.out.println("克隆List: " + clonedCities);
        System.out.println("引用相同? " + (originalCities == clonedCities));
        System.out.println("元素引用相同? " + (originalCities.get(0) == clonedCities.get(0)));

        System.out.println("\n=== Map深拷贝 ===");
        System.out.println("原始Map: " + originalMap);
        System.out.println("克隆Map: " + clonedMap);
        System.out.println("引用相同? " + (originalMap == clonedMap));
        System.out.println("值引用相同? " + (originalMap.get("首都") == clonedMap.get("首都")));

        // 修改原始对象
        originalCities.get(0).setName("北京-修改");
        originalMap.get("首都").setName("北京-修改");

        System.out.println("\n=== 修改原始对象后 ===");
        System.out.println("原始List: " + originalCities);
        System.out.println("克隆List: " + clonedCities);
        System.out.println("原始Map: " + originalMap);
        System.out.println("克隆Map: " + clonedMap);
    }

    /**
     * 测试用类 - 必须实现Cloneable
     */
    public static class City implements Cloneable {
        private String name;
        private String region;

        public City(String name, String region) {
            this.name = name;
            this.region = region;
        }

        @Override
        public City clone() throws CloneNotSupportedException {
            return (City) super.clone();
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        @Override
        public String toString() {
            return "City{name='" + name + "', region='" + region + "'}";
        }
    }
}
