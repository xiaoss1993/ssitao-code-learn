package com.ssitao.code.jdk.phase02.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * ArrayList 示例代码
 * 演示ArrayList的常用操作和底层原理
 */
public class ArrayListDemo {

    public static void main(String[] args) {
        System.out.println("=== ArrayList Demo ===\n");

        // 1. 基本创建和添加
        ArrayList<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("orange");
        list.add(1, "grape");  // 指定位置插入
        System.out.println("After add: " + list);

        // 2. 访问元素
        String first = list.get(0);
        int size = list.size();
        int index = list.indexOf("banana");
        System.out.println("First: " + first + ", Size: " + size + ", Index: " + index);

        // 3. 修改元素
        list.set(2, "peach");
        System.out.println("After set: " + list);

        // 4. 删除元素
        list.remove(0);               // 按索引删除
        list.remove("banana");       // 按对象删除
        System.out.println("After remove: " + list);

        // 5. 子列表
        list.addAll(Arrays.asList("a", "b", "c", "d"));
        List<String> sub = list.subList(1, 3);
        System.out.println("Sub list: " + sub);

        // 6. 遍历方式
        System.out.println("\n--- 遍历方式 ---");

        // 6.1 普通for循环
        System.out.print("For loop: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        // 6.2 for-each
        System.out.print("For-each: ");
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();

        // 6.3 Iterator
        System.out.print("Iterator: ");
        for (java.util.Iterator<String> it = list.iterator(); it.hasNext(); ) {
            System.out.print(it.next() + " ");
        }
        System.out.println();

        // 6.4 ListIterator（正向）
        System.out.print("ListIterator forward: ");
        ListIterator<String> lit = list.listIterator();
        while (lit.hasNext()) {
            System.out.print(lit.nextIndex() + ":" + lit.next() + " ");
        }
        System.out.println();

        // 6.5 ListIterator（反向）
        System.out.print("ListIterator backward: ");
        while (lit.hasPrevious()) {
            System.out.print(lit.previousIndex() + ":" + lit.previous() + " ");
        }
        System.out.println();

        // 6.6 forEach (JDK 8+)
        System.out.print("forEach: ");
        list.forEach(s -> System.out.print(s + " "));
        System.out.println();

        // 6.7 Stream
        System.out.print("Stream: ");
        list.stream().forEach(s -> System.out.print(s + " "));
        System.out.println();

        // 7. 使用Iterator安全删除
        System.out.println("\n--- 安全删除（Iterator）---");
        ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("Before: " + numbers);
        java.util.Iterator<Integer> it = numbers.iterator();
        while (it.hasNext()) {
            Integer n = it.next();
            if (n % 2 == 0) {
                it.remove();  // 安全删除，不会ConcurrentModificationException
            }
        }
        System.out.println("After removing evens: " + numbers);

        // 8. 容量相关
        System.out.println("\n--- 容量信息 ---");
        ArrayList<String> capacityList = new ArrayList<>(20);
        System.out.println("Initial capacity: " + 20);
        System.out.println("Size: " + capacityList.size());

        // 9. 线程安全演示
        System.out.println("\n--- 线程安全 ---");
        System.out.println("ArrayList is NOT thread-safe.");
        System.out.println("Use Collections.synchronizedList() or CopyOnWriteArrayList for thread safety.");

        // 10. 数组转换
        System.out.println("\n--- 数组转换 ---");
        List<String> list2 = Arrays.asList("x", "y", "z");
        String[] array = list2.toArray(new String[0]);
        System.out.println("To array: " + Arrays.toString(array));
    }
}
