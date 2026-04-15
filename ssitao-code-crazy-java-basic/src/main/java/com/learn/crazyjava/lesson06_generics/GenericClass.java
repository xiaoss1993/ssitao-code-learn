package com.learn.crazyjava.lesson06_generics;

/**
 * 第6课：泛型 - 泛型类
 */
public class GenericClass<T> {
    private T value;

    public GenericClass(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static void main(String[] args) {
        GenericClass<String> obj1 = new GenericClass<>("hello");
        GenericClass<Integer> obj2 = new GenericClass<>(100);

        System.out.println(obj1.getValue());
        System.out.println(obj2.getValue());

        // 类型安全
        // obj1.setValue(123);  // 编译错误
    }
}
