package com.ssitao.code.effectivejava.ch04.item33;

import java.util.HashMap;
import java.util.Map;

/**
 * 条目33：考虑使用类型安全的异构容器
 *
 * 传统容器的限制：
 * - Map<String, Object> 可以存储不同类型，但取值时需要类型转换
 * - 类型信息在运行时丢失
 *
 * 异构容器的优点：
 * - 用 Class<T> 作为 key，保留类型信息
 * - 可以存储不同类型的值，同时保持类型安全
 * - 取值时无需类型转换
 */
public class Favorites {
    // 异构容器：key可以有不同的类型参数
    private final Map<Class<?>, Object> favorites = new HashMap<>();

    /**
     * 存储任意类型的收藏
     * @param type Class类型
     * @param instance 该类型的实例
     */
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(type, type.cast(instance));
    }

    /**
     * 获取任意类型的收藏
     * @param type Class类型
     * @return 该类型的实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getFavorite(Class<T> type) {
        return (T) favorites.get(type);  // 由于putFavorite，转换是安全的
    }

    public static void main(String[] args) {
        System.out.println("=== 类型安全的异构容器示例 ===\n");

        Favorites f = new Favorites();

        // 在同一个容器中存储不同类型
        f.putFavorite(String.class, "hello");
        f.putFavorite(Integer.class, 123);
        f.putFavorite(Double.class, 3.14);

        // 取值时保持类型安全
        String s = f.getFavorite(String.class);
        Integer i = f.getFavorite(Integer.class);
        Double d = f.getFavorite(Double.class);

        System.out.println("String: " + s);
        System.out.println("Integer: " + i);
        System.out.println("Double: " + d);

        // 传统Map的限制
        System.out.println("\n--- 传统Map的限制 ---");
        Map<String, String> stringMap = new HashMap<>();
        // stringMap只能存储String类型的值

        System.out.println("传统 Map<String, String> 只能存储一种类型");
        System.out.println("异构容器可以存储多种类型！");

        // 演示类型安全
        System.out.println("\n--- 类型安全 ---");
        System.out.println("f.getFavorite(String.class) 返回 String: " + s.getClass().getSimpleName());
        System.out.println("f.getFavorite(Integer.class) 返回 Integer: " + i.getClass().getSimpleName());
    }
}
