package com.ssitao.code.effectivejava.ch04.item31;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 条目31：使用有界通配符增加API灵活性
 *
 * PECS原则：Producer Extends, Consumer Super
 * - 如果参数是"生产者"（从参数读取数据），使用 ? extends E
 * - 如果参数是"消费者"（向参数写入数据），使用 ? super E
 *
 * 示例：
 * - pushAll(Iterable<? extends E>)：从src读取E，使用extends
 * - popAll(Collection<? super E>)：向dest写入E，使用super
 */
public class Stack<E> {
    private final List<E> elements = new ArrayList<>();

    public void push(E item) {
        elements.add(item);
    }

    public E pop() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("栈为空");
        }
        return elements.remove(elements.size() - 1);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * 生产者：从源读取元素 - 使用 extends
     */
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    /**
     * 消费者：向目标写入元素 - 使用 super
     */
    public void popAll(Collection<? super E> dest) {
        while (!isEmpty()) {
            dest.add(pop());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PECS示例 (Producer Extends, Consumer Super) ===\n");

        Stack<Number> numberStack = new Stack<>();

        // pushAll 传入 Iterable<? extends Number> - 生产者
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        System.out.println("推入整数...");
        numberStack.pushAll(integers);  // 正确：Integer 是 Number 的子类型

        // popAll 传入 Collection<? super Number> - 消费者
        List<Object> objects = new ArrayList<>();
        System.out.println("弹出到Object列表...");
        numberStack.popAll(objects);  // 正确：Number 是 Object 的子类型

        System.out.println("收集的对象: " + objects);

        System.out.println("\n--- PECS解释 ---");
        System.out.println("pushAll(src): src产生E -> 使用extends");
        System.out.println("popAll(dest): dest消费E -> 使用super");
    }
}
