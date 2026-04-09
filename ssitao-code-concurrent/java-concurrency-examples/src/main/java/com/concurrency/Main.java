package com.concurrency;

import com.concurrency.chapter1.core.Chapter1Examples;
import com.concurrency.chapter2.core.Chapter2Examples;
import com.concurrency.chapter3.core.Chapter3Examples;
import com.concurrency.chapter4.core.Chapter4Examples;
import com.concurrency.chapter5.core.Chapter5Examples;
import com.concurrency.chapter6.core.Chapter6Examples;
import com.concurrency.chapter7.core.Chapter7Examples;

/**
 * Java并发编程实战 - 示例代码入口
 *
 * 本项目包含《Java 7 并发编程实战手册》各章节的核心示例代码
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       Java 并发编程实战 - 示例代码                           ║");
        System.out.println("║       基于《Java 7 并发编程实战手册》                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // 选择要运行的章节示例
        if (args.length == 0) {
            runAllChapters();
        } else {
            int chapter = Integer.parseInt(args[0]);
            runChapter(chapter);
        }
    }

    private static void runAllChapters() throws Exception {
        Chapter1Examples.main(new String[]{});
        Chapter2Examples.main(new String[]{});
        Chapter3Examples.main(new String[]{});
        Chapter4Examples.main(new String[]{});
        Chapter5Examples.main(new String[]{});
        Chapter6Examples.main(new String[]{});
        Chapter7Examples.main(new String[]{});

        System.out.println("\n══════════════════════════════════════════════════════════");
        System.out.println("所有章节示例执行完成!");
        System.out.println("══════════════════════════════════════════════════════════");
    }

    private static void runChapter(int chapter) throws Exception {
        switch (chapter) {
            case 1:
                Chapter1Examples.main(new String[]{});
                break;
            case 2:
                Chapter2Examples.main(new String[]{});
                break;
            case 3:
                Chapter3Examples.main(new String[]{});
                break;
            case 4:
                Chapter4Examples.main(new String[]{});
                break;
            case 5:
                Chapter5Examples.main(new String[]{});
                break;
            case 6:
                Chapter6Examples.main(new String[]{});
                break;
            case 7:
                Chapter7Examples.main(new String[]{});
                break;
            default:
                System.out.println("无效的章节号，请输入1-7之间的数字");
        }
    }
}
