package com.ssitao.code.effectivejava.ch04.item33;

import java.util.HashMap;
import java.util.Map;

/**
 * Item 33: Consider typesafe heterogeneous containers
 *
 * Demonstrates storing different types in a single container
 */
public class Favorites {
    // Heterogeneous container: keys have different type parameters
    private final Map<Class<?>, Object> favorites = new HashMap<>();

    // Store a favorite of any type
    public <T> void putFavorite(Class<T> type, T instance) {
        favorites.put(type, type.cast(instance));
    }

    // Retrieve a favorite of any type
    @SuppressWarnings("unchecked")
    public <T> T getFavorite(Class<T> type) {
        return (T) favorites.get(type);  // Cast is safe due to putFavorite
    }

    public static void main(String[] args) {
        System.out.println("=== Typesafe Heterogeneous Container Demo ===\n");

        Favorites f = new Favorites();

        // Store different types in the same container
        f.putFavorite(String.class, "hello");
        f.putFavorite(Integer.class, 123);
        f.putFavorite(Double.class, 3.14);

        // Retrieve with type safety
        String s = f.getFavorite(String.class);
        Integer i = f.getFavorite(Integer.class);
        Double d = f.getFavorite(Double.class);

        System.out.println("String: " + s);
        System.out.println("Integer: " + i);
        System.out.println("Double: " + d);

        // Traditional Map limitation
        System.out.println("\n--- Traditional Map Limitation ---");
        Map<String, String> stringMap = new HashMap<>();
        // stringMap can only store String values

        System.out.println("Traditional Map<String, String> can only store one type");
        System.out.println("Heterogeneous container can store multiple types!");

        // Demonstrate type safety
        System.out.println("\n--- Type Safety ---");
        System.out.println("f.getFavorite(String.class) returns String: " + s.getClass().getSimpleName());
        System.out.println("f.getFavorite(Integer.class) returns Integer: " + i.getClass().getSimpleName());
    }
}
