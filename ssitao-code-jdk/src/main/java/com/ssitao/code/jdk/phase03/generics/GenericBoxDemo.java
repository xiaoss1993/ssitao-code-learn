package com.ssitao.code.jdk.phase03.generics;

import java.util.*;

/**
 * 泛型示例代码
 * 演示泛型类的使用
 */
public class GenericBoxDemo {

    public static void main(String[] args) {
        System.out.println("=== Generic Box Demo ===\n");

        // 1. 泛型类基本使用
        System.out.println("--- 泛型类基本使用 ---");
        Box<String> stringBox = new Box<>();
        stringBox.set("Hello");
        System.out.println("StringBox: " + stringBox.get());

        Box<Integer> intBox = new Box<>();
        intBox.set(123);
        System.out.println("IntBox: " + intBox.get());

        // 2. 多类型参数
        System.out.println("\n--- 多类型参数 ---");
        Pair<String, Integer> pair = new Pair<>("age", 25);
        System.out.println("Pair: " + pair.getKey() + " = " + pair.getValue());

        // 3. 泛型方法
        System.out.println("\n--- 泛型方法 ---");
        System.out.println("Max of 1,2,3: " + max(1, 2, 3));
        System.out.println("Max of a,b,c: " + max("a", "b", "c"));

        // 4. 泛型接口
        System.out.println("\n--- 泛型接口 ---");
        StringContainer container = new StringContainer();
        container.add("apple");
        container.add("banana");
        System.out.println("Container[0]: " + container.get(0));
        System.out.println("Container size: " + container.size());

        // 5. 通配符
        System.out.println("\n--- 通配符 ---");
        List<Integer> intList = Arrays.asList(1, 2, 3);
        List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
        System.out.println("Sum of intList: " + sumOfList(intList));
        System.out.println("Sum of doubleList: " + sumOfList(doubleList));

        // 6. 上界通配符 (extends)
        System.out.println("\n--- 上界通配符 (extends) ---");
        printNumbers(intList);
        printNumbers(doubleList);

        // 7. 下界通配符 (super)
        System.out.println("\n--- 下界通配符 (super) ---");
        List<Number> numberList = new ArrayList<>();
        addNumbers(numberList);
        System.out.println("numberList: " + numberList);

        // 8. 类型擦除演示
        System.out.println("\n--- 类型擦除 ---");
        System.out.println("Box<String>和Box<Integer>的Class对象是相同的");
        System.out.println("stringBox.getClass(): " + stringBox.getClass());
        System.out.println("intBox.getClass(): " + intBox.getClass());
        System.out.println("相同: " + stringBox.getClass().equals(intBox.getClass()));

        // 9. PECS原则
        System.out.println("\n--- PECS原则 ---");
        System.out.println("Producer Extends, Consumer Super");
        System.out.println("读取数据用extends，写入数据用super");

        // 10. 泛型限制
        System.out.println("\n--- 泛型限制 ---");
        System.out.println("1. 不能创建泛型数组");
        System.out.println("2. 不能实例化类型参数");
        System.out.println("3. 不能使用基本类型");
        System.out.println("4. 不能使用instanceof检查泛型类型");
    }

    // 泛型类
    static class Box<T> {
        private T value;

        public void set(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }

        @Override
        public String toString() {
            return "Box{" + value + "}";
        }
    }

    // 多类型参数
    static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }

    // 泛型方法
    public static <T extends Comparable<T>> T max(T... items) {
        if (items == null || items.length == 0) return null;
        T max = items[0];
        for (int i = 1; i < items.length; i++) {
            if (items[i].compareTo(max) > 0) {
                max = items[i];
            }
        }
        return max;
    }

    // 泛型接口
    interface Container<T> {
        void add(T item);
        T get(int index);
        int size();
    }

    static class StringContainer implements Container<String> {
        private List<String> list = new ArrayList<>();

        @Override
        public void add(String item) {
            list.add(item);
        }

        @Override
        public String get(int index) {
            return list.get(index);
        }

        @Override
        public int size() {
            return list.size();
        }
    }

    // 无界通配符
    public static double sumOfList(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) {
            sum += n.doubleValue();
        }
        return sum;
    }

    // 上界通配符
    public static void printNumbers(List<? extends Number> list) {
        System.out.print("Numbers: ");
        for (Number n : list) {
            System.out.print(n + " ");
        }
        System.out.println();
        // 不能写入
        // list.add(1); // 编译错误！
    }

    // 下界通配符
    public static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
        // 读取只能当作Object
        System.out.println("First as Object: " + list.get(0).getClass());
    }
}
