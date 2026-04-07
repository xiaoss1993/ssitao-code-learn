package com.ssitao.code.effectivejava.ch02.item12;

import java.util.Arrays;
import java.util.Comparator;
import java.math.BigDecimal;

/**
 * 条目12：考虑实现Comparable接口
 *
 * compareTo的约定：
 * 1. 返回负数表示当前对象小于目标对象
 * 2. 返回零表示相等
 * 3. 返回正数表示当前对象大于目标对象
 * 4. 必须是自反的：x.compareTo(x) == 0
 * 5. 必须是对称的：x.compareTo(y) == -y.compareTo(x)
 * 6. 必须是传递的：x.compareTo(y) > 0 且 y.compareTo(z) > 0，则 x.compareTo(z) > 0
 *
 * 实现Comparable后，该类的对象可以：
 * - 被Arrays.sort()和Collections.sort()排序
 * - 在TreeSet和TreeMap中作为键使用
 * - 在PriorityQueue中作为元素使用
 */
public class WordList implements Comparable<WordList> {
    private final String[] words;

    public WordList(String... words) {
        this.words = words;
    }

    /**
     * compareTo实现：先按长度排序，再按首字母排序
     */
    @Override
    public int compareTo(WordList o) {
        // 先比较长度，再比较首字母
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
        System.out.println("=== Comparable示例 ===\n");

        WordList[] lists = {
            new WordList("hello", "world"),
            new WordList("hi"),
            new WordList("good", "morning")
        };

        System.out.println("排序前:");
        for (WordList list : lists) {
            System.out.println("  " + list);
        }

        Arrays.sort(lists);  // 使用compareTo排序

        System.out.println("\n排序后:");
        for (WordList list : lists) {
            System.out.println("  " + list);
        }

        // 演示compareTo vs equals
        System.out.println("\n--- compareTo vs equals ---");
        BigDecimal one = new BigDecimal("1.0");
        BigDecimal two = new BigDecimal("1.00");
        System.out.println("one.equals(two): " + one.equals(two));  // false，精度不同
        System.out.println("one.compareTo(two) == 0: " + (one.compareTo(two) == 0));  // true，数值相等
    }
}
