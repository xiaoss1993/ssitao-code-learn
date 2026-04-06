package com.ssitao.code.effectivejava.ch03.item18.composition;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Item 18: Favor composition over inheritance
 *
 * Demonstrates why composition is better than inheritance
 */

// ==================== WRONG: Inheritance ====================
class InstrumentedHashSet<E> extends HashSet<E> {
    private int addCount = 0;

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}

// ==================== CORRECT: Composition ====================
class InstrumentedSet<E> {
    private final Set<E> inner;
    private int addCount = 0;

    public InstrumentedSet(Set<E> inner) {
        this.inner = inner;
    }

    public boolean add(E e) {
        addCount++;
        return inner.add(e);
    }

    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return inner.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }

    // Delegate other methods
    public boolean contains(Object o) {
        return inner.contains(o);
    }

    public int size() {
        return inner.size();
    }

    public void clear() {
        inner.clear();
    }
}

public class InstrumentedSetDemo {
    public static void main(String[] args) {
        System.out.println("=== Composition vs Inheritance Demo ===\n");

        // Problem with inheritance
        System.out.println("--- Inheritance (WRONG) ---");
        InstrumentedHashSet<String> inheritanceSet = new InstrumentedHashSet<>();
        inheritanceSet.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
        System.out.println("Expected addCount: 3");
        System.out.println("Actual addCount: " + inheritanceSet.getAddCount());
        // Problem: addCount is 6 because addAll() calls add() internally!

        // Correct with composition
        System.out.println("\n--- Composition (CORRECT) ---");
        InstrumentedSet<String> compositionSet = new InstrumentedSet<>(new HashSet<>());
        compositionSet.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
        System.out.println("Expected addCount: 3");
        System.out.println("Actual addCount: " + compositionSet.getAddCount());
        // Correct: 3 because we control the implementation
    }
}
