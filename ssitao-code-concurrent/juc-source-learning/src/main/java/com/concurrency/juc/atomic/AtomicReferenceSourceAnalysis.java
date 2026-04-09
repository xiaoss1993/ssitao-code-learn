package com.concurrency.juc.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference 源码分析
 *
 * AtomicReference是对普通对象的原子化封装
 * 使用CAS保证引用更新的原子性
 */
public class AtomicReferenceSourceAnalysis {

    public static void main(String[] args) {
        System.out.println("=== AtomicReference 源码分析 ===\n");

        AtomicReference<User> ref = new AtomicReference<>(new User("Alice", 20));

        // 1. compareAndSet 源码
        System.out.println("1. compareAndSet() 核心源码:");
        System.out.println("   public boolean compareAndSet(V expect, V update) {");
        System.out.println("       return unsafe.compareAndSwapObject(this, valueOffset, expect, update);");
        System.out.println("   }");
        System.out.println();

        User expect = ref.get();
        User update = new User("Bob", 25);
        boolean success = ref.compareAndSet(expect, update);
        System.out.println("   compareAndSet(Alice, Bob) = " + success);
        System.out.println("   当前值: " + ref.get());
        System.out.println();

        // 2. getAndSet 源码
        System.out.println("2. getAndSet() 核心源码:");
        System.out.println("   public final V getAndSet(V newValue) {");
        System.out.println("       return (V)unsafe.getAndSetObject(this, valueOffset, newValue);");
        System.out.println("   }");
        System.out.println();

        User oldUser = ref.getAndSet(new User("Charlie", 30));
        System.out.println("   getAndSet(Charlie) 返回旧值: " + oldUser);
        System.out.println("   当前值: " + ref.get());
        System.out.println();

        // 3. weakCompareAndSet 源码
        System.out.println("3. weakCompareAndSet() 特点:");
        System.out.println("   不保证可见性立即同步，可能失败后重试");
        System.out.println("   性能更高，适用于高竞争场景");
        System.out.println();

        // 4. 模拟CAS ABA问题
        System.out.println("4. ABA问题演示:");
        AtomicReference<Integer> abaRef = new AtomicReference<>(1);
        System.out.println("   初始值: " + abaRef.get());

        // 线程A: 1 -> 2
        new Thread(() -> {
            abaRef.compareAndSet(1, 2);
            System.out.println("   线程A: 1 -> 2");
        }).start();

        // 线程B: 2 -> 1 (ABA)
        new Thread(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            abaRef.compareAndSet(2, 1);
            System.out.println("   线程B: 2 -> 1 (ABA)");
        }).start();

        // 线程C: 检查发现是1，以为没变过
        new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException e) {}
            boolean result = abaRef.compareAndSet(1, 3);
            System.out.println("   线程C: compareAndSet(1, 3) = " + result);
            System.out.println("   最终值: " + abaRef.get());
        }).start();
    }

    static class User {
        private String name;
        private int age;
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }
}
