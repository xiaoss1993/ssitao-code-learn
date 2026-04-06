package com.ssitao.code.effectivejava.ch01.item05;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Item 5: Prefer dependency injection
 */
public class SpellChecker {
    private final Dictionary dictionary;

    // Dependency injection via constructor
    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public static void main(String[] args) {
        System.out.println("=== Dependency Injection Demo ===\n");

        // Inject English dictionary
        SpellChecker englishChecker = new SpellChecker(new EnglishDictionary());
        System.out.println("Using English dictionary:");
        System.out.println("\"hello\" is valid: " + englishChecker.isValid("hello"));
        System.out.println("\"helo\" is valid: " + englishChecker.isValid("helo"));

        // Inject French dictionary - same code, different behavior
        SpellChecker frenchChecker = new SpellChecker(new FrenchDictionary());
        System.out.println("\nUsing French dictionary:");
        System.out.println("\"bonjour\" is valid: " + frenchChecker.isValid("bonjour"));
    }
}

interface Dictionary {
    boolean contains(String word);
}

class EnglishDictionary implements Dictionary {
    private static final Set<String> WORDS = new HashSet<>(Arrays.asList(
        "hello", "world", "java", "programming"
    ));

    @Override
    public boolean contains(String word) {
        return WORDS.contains(word.toLowerCase());
    }
}

class FrenchDictionary implements Dictionary {
    private static final Set<String> WORDS = new HashSet<>(Arrays.asList(
        "bonjour", "merci", "france", "paris"
    ));

    @Override
    public boolean contains(String word) {
        return WORDS.contains(word.toLowerCase());
    }
}
