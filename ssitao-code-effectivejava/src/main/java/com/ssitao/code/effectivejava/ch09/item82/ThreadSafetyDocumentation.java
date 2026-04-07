package com.ssitao.code.effectivejava.ch09.item82;

/**
 * 条目82：线程安全程度的文档化
 *
 * 线程安全级别（从高到低）：
 * 1. 不可变 (immutable) - 不需要同步，如 String, BigInteger
 * 2. 无条件线程安全 (unconditionally thread-safe) - 如 ConcurrentHashMap
 * 3. 有条件线程安全 (conditionally thread-safe) - 部分方法需要同步
 * 4. 非线程安全 (not thread-safe) - 如 ArrayList, HashMap
 * 5. 线程对立 (thread-hostile) - 在并发时会导致错误
 *
 * 使用@ThreadSafe和@NotThreadSafe注解（JSR 305）
 */
public class ThreadSafetyDocumentation {

    // ==================== 1. 不可变 ====================
    /**
     * 不可变类 - 不需要同步
     * 所有字段都是final，状态不会改变
     */
    public static final class Point {
        private final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }
    }

    // ==================== 2. 无条件线程安全 ====================
    /**
     * 无条件线程安全 - 所有方法都是线程安全的
     * 使用者不需要额外同步
     */
    public static class SafeCounter {
        private final java.util.concurrent.atomic.AtomicLong count = new java.util.concurrent.atomic.AtomicLong(0);

        public void increment() {
            count.incrementAndGet();
        }

        public long get() {
            return count.get();
        }
    }

    // ==================== 3. 有条件线程安全 ====================
    /**
     * 有条件线程安全 - 部分方法需要同步
     * 文档应说明哪些方法需要同步
     */
    public static class SomeList {
        private final java.util.List<Object> list = java.util.Collections.synchronizedList(new java.util.ArrayList<>());

        /**
         * 线程安全：内部已同步
         */
        public synchronized void add(Object o) {
            list.add(o);
        }

        /**
         * 线程安全：内部已同步
         */
        public synchronized Object get(int index) {
            return list.get(index);
        }

        /**
         * 需要外部同步：迭代时需要同步
         * 用法示例：
         * synchronized (someList) {
         *     for (Object o : someList) {
         *         // 处理o
         *     }
         * }
         */
        public java.util.Iterator<Object> iterator() {
            return list.iterator();  // 调用者必须同步
        }
    }

    // ==================== 4. 非线程安全 ====================
    /**
     * 非线程安全 - 如ArrayList
     * 使用者必须外部同步
     */
    public static class UnsafeList<E> {
        private final java.util.ArrayList<E> list = new java.util.ArrayList<>();

        public void add(E e) {
            list.add(e);  // 非线程安全
        }

        public E get(int index) {
            return list.get(index);  // 非线程安全
        }
    }

    // ==================== 线程安全标注 ====================
    /**
     * @ThreadSafe - 这个类是线程安全的
     * 使用示例：Collections.synchronizedList返回的类
     */
    // @ThreadSafe
    public static class ThreadSafeClass {
        private final java.util.concurrent.atomic.AtomicInteger counter = new java.util.concurrent.atomic.AtomicInteger();

        public void increment() {
            counter.incrementAndGet();
        }
    }

    /**
     * @NotThreadSafe - 这个类不是线程安全的
     * 使用示例：ArrayList, HashMap等
     */
    // @NotThreadSafe
    public static class NotThreadSafeClass {
        private final java.util.ArrayList<String> list = new java.util.ArrayList<>();

        public void add(String s) {
            list.add(s);  // 非线程安全
        }
    }

    // ==================== 最佳实践 ====================
    public static void main(String[] args) {
        System.out.println("=== 线程安全级别 ===\n");

        System.out.println("1. 不可变 (immutable)");
        System.out.println("   - 不需要同步");
        System.out.println("   - 示例：String, BigInteger, enum");
        System.out.println();

        System.out.println("2. 无条件线程安全");
        System.out.println("   - 所有方法都线程安全");
        System.out.println("   - 示例：ConcurrentHashMap, AtomicLong");
        System.out.println();

        System.out.println("3. 有条件线程安全");
        System.out.println("   - 部分方法需要外部同步");
        System.out.println("   - 示例：Collections.synchronizedList包装的集合");
        System.out.println();

        System.out.println("4. 非线程安全");
        System.out.println("   - 使用者必须外部同步");
        System.out.println("   - 示例：ArrayList, HashMap");
        System.out.println();

        System.out.println("5. 线程对立");
        System.out.println("   - 并发使用会导致错误");
        System.out.println("   - 通常是设计缺陷");
        System.out.println();

        System.out.println("=== 文档注释示例 ===\n");
        System.out.println("/**");
        System.out.println(" * 此类是线程安全的。");
        System.out.println(" * 不需要外部同步。");
        System.out.println(" *");
        System.out.println(" * @author xxx");
        System.out.println(" * @see java.util.concurrent");
        System.out.println(" */");
    }
}
