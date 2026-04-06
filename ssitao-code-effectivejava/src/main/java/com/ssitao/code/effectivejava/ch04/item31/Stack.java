package com.ssitao.code.effectivejava.ch04.item31;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Item 31: Use bounded wildcards to increase API flexibility
 *
 * Demonstrates PECS: Producer Extends, Consumer Super
 */
public class Stack<E> {
    private final List<E> elements = new ArrayList<>();

    public void push(E item) {
        elements.add(item);
    }

    public E pop() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return elements.remove(elements.size() - 1);
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    // Producer: reads elements from source - use extends
    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    // Consumer: writes elements to destination - use super
    public void popAll(Collection<? super E> dest) {
        while (!isEmpty()) {
            dest.add(pop());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== PECS Demo (Producer Extends, Consumer Super) ===\n");

        Stack<Number> numberStack = new Stack<>();

        // pushAll with Iterable<? extends Number> - Producer
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);

        System.out.println("Pushing integers...");
        numberStack.pushAll(integers);  // OK: Integer is a subtype of Number

        // popAll with Collection<? super Number> - Consumer
        List<Object> objects = new ArrayList<>();
        System.out.println("Popping to Object list...");
        numberStack.popAll(objects);  // OK: Number is a subtype of Object

        System.out.println("Objects collected: " + objects);

        System.out.println("\n--- PECS Explained ---");
        System.out.println("pushAll(src): src produces E -> use extends");
        System.out.println("popAll(dest): dest consumes E -> use super");
    }
}
