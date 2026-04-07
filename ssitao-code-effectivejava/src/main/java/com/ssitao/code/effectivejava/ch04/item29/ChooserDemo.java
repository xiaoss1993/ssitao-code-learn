package com.ssitao.code.effectivejava.ch04.item29;

/**
 * 条目29：优先使用泛型类型
 *
 * 泛型的优点：
 * 1. 类型安全：编译时检查，避免 ClassCastException
 * 2. 无需类型转换：代码更简洁
 * 3. 可读性更好：代码意图更清晰
 *
 * 本例演示：将原始类型改为泛型类型
 */
import java.util.Random;

// ==================== 错误：原始类型 ====================
/**
 * 使用Object数组 - 需要类型转换，不安全
 */
class ChooserRaw {
    private final Object[] choices;

    public ChooserRaw(Object[] choices) {
        this.choices = choices;
    }

    public Object choose() {
        Random rand = new Random();
        return choices[rand.nextInt(choices.length)];
    }
}

// ==================== 正确：泛型类型 ====================
/**
 * 使用泛型 - 类型安全，无需转换
 */
class Chooser<T> {
    private final T[] choices;

    @SuppressWarnings("unchecked")
    public Chooser(T[] choices) {
        this.choices = choices;
    }

    public T choose() {
        Random rand = new Random();
        return choices[rand.nextInt(choices.length)];
    }
}

public class ChooserDemo {
    public static void main(String[] args) {
        System.out.println("=== 泛型类型示例 ===\n");

        // 错误：原始类型 - 需要类型转换
        String[] names = {"Alice", "Bob", "Charlie"};
        ChooserRaw wrongChooser = new ChooserRaw(names);
        String name = (String) wrongChooser.choose();  // 危险的类型转换！
        System.out.println("原始类型（需要转换）: " + name);

        // 正确：泛型类型 - 类型安全
        Chooser<String> correctChooser = new Chooser<>(names);
        String safeName = correctChooser.choose();  // 无需转换！
        System.out.println("泛型类型（类型安全）: " + safeName);

        // 类型错误在编译时捕获
        // Integer num = correctChooser.choose();  // 编译错误！

        System.out.println("\n--- 优点 ---");
        System.out.println("1. 运行时没有 ClassCastException");
        System.out.println("2. 不需要显式类型转换");
        System.out.println("3. 编译器可以验证类型安全");
    }
}
