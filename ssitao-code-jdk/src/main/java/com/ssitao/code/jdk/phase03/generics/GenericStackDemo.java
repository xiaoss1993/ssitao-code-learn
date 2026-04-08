package com.ssitao.code.jdk.phase03.generics;

import java.util.LinkedList;

/**
 * 泛型栈示例
 * 实现一个泛型栈
 */
public class GenericStackDemo {

    public static void main(String[] args) {
        System.out.println("=== Generic Stack Demo ===\n");

        // 1. Integer栈
        System.out.println("--- Integer Stack ---");
        GenericStack<Integer> intStack = new GenericStack<>();
        intStack.push(1);
        intStack.push(2);
        intStack.push(3);
        System.out.println("Stack: " + intStack);
        System.out.println("Peek: " + intStack.peek());
        System.out.println("Pop: " + intStack.pop());
        System.out.println("Pop: " + intStack.pop());
        System.out.println("After pops: " + intStack);

        // 2. String栈
        System.out.println("\n--- String Stack ---");
        GenericStack<String> strStack = new GenericStack<>();
        strStack.push("Hello");
        strStack.push("World");
        System.out.println("Peek: " + strStack.peek());
        System.out.println("Pop: " + strStack.pop());
        System.out.println("Is empty: " + strStack.isEmpty());

        // 3. 自定义对象栈
        System.out.println("\n--- Custom Object Stack ---");
        GenericStack<Person> personStack = new GenericStack<>();
        personStack.push(new Person("Alice", 25));
        personStack.push(new Person("Bob", 30));
        System.out.println("Peek: " + personStack.peek());
    }

    // 泛型栈实现
    static class GenericStack<E> {
        private LinkedList<E> list = new LinkedList<>();

        public void push(E item) {
            list.addFirst(item);
        }

        public E pop() {
            if (list.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return list.removeFirst();
        }

        public E peek() {
            if (list.isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return list.peekFirst();
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public int size() {
            return list.size();
        }

        @Override
        public String toString() {
            return list.toString();
        }
    }

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }
}
