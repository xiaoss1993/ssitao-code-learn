package com.ssitao.code.effectivejava.ch02.item12;

import java.util.Arrays;
import java.util.Comparator;
import java.math.BigDecimal;

/**
 * Item 12: Consider implementing Comparable
 *
 * Demonstrates proper compareTo() implementation
 */
public class WordList implements Comparable<WordList> {
    private final String[] words;

    public WordList(String... words) {
        this.words = words;
    }

    @Override
    public int compareTo(WordList o) {
        // Compare by length first, then alphabetically
        return Comparator
            .comparing((WordList w) -> w.words.length)
            .thenComparing(w -> w.words[0])
            .compare(this, o);
    }

    @Override
    public String toString() {
        return Arrays.toString(words);
    }

    public static void main(String[] args) {
        System.out.println("=== Comparable Demo ===\n");

        WordList[] lists = {
            new WordList("hello", "world"),
            new WordList("hi"),
            new WordList("good", "morning")
        };

        System.out.println("Before sorting:");
        for (WordList list : lists) {
            System.out.println("  " + list);
        }

        Arrays.sort(lists);

        System.out.println("\nAfter sorting:");
        for (WordList list : lists) {
            System.out.println("  " + list);
        }

        // Demonstrate compareTo vs equals
        System.out.println("\n--- compareTo vs equals ---");
        BigDecimal one = new BigDecimal("1.0");
        BigDecimal two = new BigDecimal("1.00");
        System.out.println("one.equals(two): " + one.equals(two));
        System.out.println("one.compareTo(two) == 0: " + (one.compareTo(two) == 0));
    }
}
