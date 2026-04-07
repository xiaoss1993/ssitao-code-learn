package com.ssitao.code.effectivejava.ch01.item05;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 条目5：优先使用依赖注入
 *
 * 依赖注入的优点：
 * 1. 解耦：类不需要自己创建依赖对象
 * 2. 可测试性：可以注入mock对象进行单元测试
 * 3. 灵活性：可以在运行时替换不同的实现
 * 4. 清晰性：依赖关系一目了然
 *
 * 本例演示如何通过构造器注入不同的字典实现
 */
public class SpellChecker {
    private final Dictionary dictionary;

    /**
     * 通过构造器注入依赖
     */
    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public static void main(String[] args) {
        System.out.println("=== 依赖注入示例 ===\n");

        // 注入英语字典
        SpellChecker englishChecker = new SpellChecker(new EnglishDictionary());
        System.out.println("使用英语字典:");
        System.out.println("\"hello\" 是否有效: " + englishChecker.isValid("hello"));
        System.out.println("\"helo\" 是否有效: " + englishChecker.isValid("helo"));

        // 注入法语字典 - 相同代码，不同行为
        SpellChecker frenchChecker = new SpellChecker(new FrenchDictionary());
        System.out.println("\n使用法语字典:");
        System.out.println("\"bonjour\" 是否有效: " + frenchChecker.isValid("bonjour"));
    }
}

/**
 * 字典接口 - 定义拼写检查的契约
 */
interface Dictionary {
    boolean contains(String word);
}

/**
 * 英语字典实现
 */
class EnglishDictionary implements Dictionary {
    private static final Set<String> WORDS = new HashSet<>(Arrays.asList(
        "hello", "world", "java", "programming"
    ));

    @Override
    public boolean contains(String word) {
        return WORDS.contains(word.toLowerCase());
    }
}

/**
 * 法语字典实现
 */
class FrenchDictionary implements Dictionary {
    private static final Set<String> WORDS = new HashSet<>(Arrays.asList(
        "bonjour", "merci", "france", "paris"
    ));

    @Override
    public boolean contains(String word) {
        return WORDS.contains(word.toLowerCase());
    }
}
