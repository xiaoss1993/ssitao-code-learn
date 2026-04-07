package com.ssitao.code.effectivejava.ch03.item18.composition;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 条目18：组合优于继承
 *
 * 继承的问题：
 * - 父类的内部实现可能变化，破坏子类
 * - 父类新增方法可能与子类意图不符
 * - 可能违反"is-a"关系
 *
 * 本例演示：继承HashSet时，addAll内部调用add，导致计数错误
 */

// ==================== 错误：继承 ====================
/**
 * 通过继承实现的计数Set
 * 问题：HashSet的addAll内部调用add，导致计数翻倍！
 */
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

// ==================== 正确：组合 ====================
/**
 * 通过组合实现的计数Set
 * 组合：将需要功能的对象作为字段，通过委托实现
 * 这样我们完全控制实现，不会被父类影响
 */
class InstrumentedSet<E> {
    private final Set<E> inner;  // 组合：持有另一个Set
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

    // 委托其他方法
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
        System.out.println("=== 组合 vs 继承示例 ===\n");

        // 继承的问题
        System.out.println("--- 继承（错误） ---");
        InstrumentedHashSet<String> inheritanceSet = new InstrumentedHashSet<>();
        inheritanceSet.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
        System.out.println("期望的addCount: 3");
        System.out.println("实际的addCount: " + inheritanceSet.getAddCount());
        // 问题：addCount是6，因为addAll()内部调用了add()！

        // 组合的正确做法
        System.out.println("\n--- 组合（正确） ---");
        InstrumentedSet<String> compositionSet = new InstrumentedSet<>(new HashSet<>());
        compositionSet.addAll(Arrays.asList("Snap", "Crackle", "Pop"));
        System.out.println("期望的addCount: 3");
        System.out.println("实际的addCount: " + compositionSet.getAddCount());
        // 正确：3，因为我们控制了实现
    }
}
