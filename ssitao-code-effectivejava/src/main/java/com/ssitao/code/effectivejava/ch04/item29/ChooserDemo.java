package com.ssitao.code.effectivejava.ch04.item29;

/**
 * Item 29: Favor generic types
 *
 * Demonstrates converting raw type to generic type
 */
import java.util.Random;

// ==================== WRONG: Raw types ====================
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

// ==================== CORRECT: Generic type ====================
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
        System.out.println("=== Generic Types Demo ===\n");

        // WRONG: Raw type - requires casting
        String[] names = {"Alice", "Bob", "Charlie"};
        ChooserRaw wrongChooser = new ChooserRaw(names);
        String name = (String) wrongChooser.choose();  // Dangerous cast!
        System.out.println("Raw type (needs cast): " + name);

        // CORRECT: Generic type - type safe
        Chooser<String> correctChooser = new Chooser<>(names);
        String safeName = correctChooser.choose();  // No cast needed!
        System.out.println("Generic type (type safe): " + safeName);

        // Compile error with wrong type
        // Integer num = correctChooser.choose();  // Compile error!

        System.out.println("\n--- Advantages ---");
        System.out.println("1. No ClassCastException at runtime");
        System.out.println("2. No explicit casts needed");
        System.out.println("3. Compiler can verify type safety");
    }
}
