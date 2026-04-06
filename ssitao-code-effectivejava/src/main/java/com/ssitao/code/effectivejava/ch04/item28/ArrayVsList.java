package com.ssitao.code.effectivejava.ch04.item28;

/**
 * Item 28: Prefer lists to arrays
 *
 * Demonstrates why arrays are less safe than lists
 */
import java.util.ArrayList;
import java.util.List;

public class ArrayVsList {
    public static void main(String[] args) {
        System.out.println("=== Arrays vs Lists ===\n");

        // Problem 1: Arrays are covariant (unsafe)
        System.out.println("--- Problem 1: Covariance ---");

        // String[] is a subtype of Object[]
        Object[] objArr = new String[3];
        System.out.println("String[] is a subtype of Object[]: OK at compile time");

        try {
            objArr[0] = 123;  // But runtime ArrayStoreException!
            System.out.println("Stored Integer in String[] - should not reach here");
        } catch (ArrayStoreException e) {
            System.out.println("ArrayStoreException: " + e.getMessage());
        }

        // Problem 2: Generic types are invariant (safe)
        System.out.println("\n--- Problem 2: Invariance ---");
        // List<String> is NOT a subtype of List<Object>
        // List<String> stringList = new ArrayList<Object>();  // Compile error!
        System.out.println("List<String> is NOT a subtype of List<Object>: compile error");

        // Generic list catches type errors at compile time
        List<String> stringList = new ArrayList<>();
        // stringList.add(123);  // Compile error!
        System.out.println("Adding Integer to List<String>: compile error (caught at compile time)");

        // Problem 3: Arrays don't support generics
        System.out.println("\n--- Problem 3: Arrays don't support generics ---");
        // new List<E>() - compile error, can't create generic arrays
        // new E[] - compile error, can't create generic arrays

        List<String>[] stringLists = (List<String>[]) new ArrayList<?>[3];  // Unchecked cast
        System.out.println("Must use unchecked cast to create generic array: (List<String>[]) new ArrayList<?>[3]");

        System.out.println("\n--- Conclusion ---");
        System.out.println("Lists are safer than arrays:");
        System.out.println("1. Type errors caught at compile time, not runtime");
        System.out.println("2. No ClassCastException or ArrayStoreException");
        System.out.println("3. Full generic type support");
    }
}
