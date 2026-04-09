package com.concurrency.juc.core;

import com.concurrency.juc.atomic.AtomicIntegerSourceAnalysis;
import com.concurrency.juc.atomic.AtomicReferenceSourceAnalysis;
import com.concurrency.juc.locks.*;
import com.concurrency.juc.collections.*;
import com.concurrency.juc.executors.ThreadPoolExecutorSourceAnalysis;

/**
 * JUC 源码学习主入口
 */
public class JUCSourceLearning {

    public static void main(String[] args) throws Exception {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║         JUC (java.util.concurrent) 源码学习                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        int choice = 0;
        if (args.length > 0) {
            try {
                choice = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
            }
        }

        if (choice == 0) {
            printMenu();
        } else {
            runExample(choice);
        }
    }

    private static void printMenu() {
        System.out.println("请选择要运行的示例:");
        System.out.println();
        System.out.println("【Atomic 原子类】");
        System.out.println("  1. AtomicInteger 源码分析");
        System.out.println("  2. AtomicReference 源码分析");
        System.out.println();
        System.out.println("【Locks 锁】");
        System.out.println("  3. AQS 源码分析");
        System.out.println("  4. ReentrantLock 源码分析");
        System.out.println("  5. ReentrantReadWriteLock 源码分析");
        System.out.println("  6. CountDownLatch 源码分析");
        System.out.println("  7. Semaphore 源码分析");
        System.out.println();
        System.out.println("【Collections 集合】");
        System.out.println("  8. ConcurrentHashMap 源码分析");
        System.out.println("  9. CopyOnWriteArrayList 源码分析");
        System.out.println();
        System.out.println("【Executors 执行器】");
        System.out.println(" 10. ThreadPoolExecutor 源码分析");
        System.out.println();
        System.out.println("用法: java JUCSourceLearning [选项编号]");
        System.out.println("示例: java JUCSourceLearning 1");
    }

    private static void runExample(int choice) throws Exception {
        switch (choice) {
            case 1:
                AtomicIntegerSourceAnalysis.main(new String[]{});
                break;
            case 2:
                AtomicReferenceSourceAnalysis.main(new String[]{});
                break;
            case 3:
                AbstractQueuedSynchronizerSourceAnalysis.main(new String[]{});
                break;
            case 4:
                ReentrantLockSourceAnalysis.main(new String[]{});
                break;
            case 5:
                ReentrantReadWriteLockSourceAnalysis.main(new String[]{});
                break;
            case 6:
                CountDownLatchSourceAnalysis.main(new String[]{});
                break;
            case 7:
                SemaphoreSourceAnalysis.main(new String[]{});
                break;
            case 8:
                ConcurrentHashMapSourceAnalysis.main(new String[]{});
                break;
            case 9:
                CopyOnWriteArrayListSourceAnalysis.main(new String[]{});
                break;
            case 10:
                ThreadPoolExecutorSourceAnalysis.main(new String[]{});
                break;
            default:
                System.out.println("无效的选项!");
        }
    }
}
