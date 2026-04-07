package com.ssitao.code.effectivejava.ch09.item79;

import java.util.concurrent.*;
import java.util.*;

/**
 * 条目79：避免过度同步
 *
 * 过度同步的问题：
 * 1. 性能下降：锁竞争
 * 2. 死锁风险
 * 3. 活跃性问题
 *
 * 原则：
 * - 在同步块外做耗时操作
 * - 使用并发集合减少锁粒度
 * - 考虑使用CopyOnWriteArrayList等线程安全集合
 */
public class ExecutorServiceExamples {

    // ==================== 问题示例 ====================
    /**
     * 在同步块内调用外部方法 - 危险！
     */
    static class ObservableSet<E> {
        private final Set<E> set = new HashSet<>();
        private final List<Observer<E>> observers = new ArrayList<>();

        public synchronized void add(E e) {
            set.add(e);
            notifyObservers(e);
        }

        public void addObserver(Observer<E> observer) {
            synchronized (set) {
                observers.add(observer);
            }
        }

        // 危险：在同步块内遍历可能修改的列表
        public void notifyObservers(E e) {
            synchronized (set) {
                for (Observer<E> observer : observers) {
                    observer.added(e);  // 回调可能修改observers
                }
            }
        }
    }

    interface Observer<E> {
        void added(E element);
    }

    // ==================== 正确示例 ====================
    /**
     * 使用CopyOnWriteArrayList - 迭代时无需同步
     */
    static class SafeObservableSet<E> {
        private final Set<E> set = Collections.synchronizedSet(new HashSet<>());
        private final List<Observer<E>> observers = new CopyOnWriteArrayList<>();

        public void add(E e) {
            set.add(e);
            notifyObservers(e);
        }

        public void addObserver(Observer<E> observer) {
            observers.add(observer);
        }

        public void removeObserver(Observer<E> observer) {
            observers.remove(observer);
        }

        // 安全：CopyOnWriteArrayList迭代时不需要同步
        private void notifyObservers(E e) {
            for (Observer<E> observer : observers) {
                observer.added(e);
            }
        }
    }

    // ==================== 执行器示例 ====================
    /**
     * 使用ExecutorService而不是直接创建Thread
     */
    static class TaskExecutor {
        private final ExecutorService executor = Executors.newFixedThreadPool(10);

        public void executeTasks(List<Runnable> tasks) throws InterruptedException {
            List<Future<?>> futures = new ArrayList<>();

            for (Runnable task : tasks) {
                futures.add(executor.submit(task));
            }

            // 等待所有任务完成
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause();
                    System.out.println("Task failed: " + cause);
                }
            }
        }

        public void shutdown() {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    // ==================== 生产者-消费者 ====================
    /**
     * 使用BlockingQueue实现生产者-消费者
     */
    static class ProducerConsumer {
        private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(100);

        public void producer() throws InterruptedException {
            for (int i = 0; i < 100; i++) {
                queue.put(i);  // 队列满时会阻塞
                System.out.println("Produced: " + i);
            }
            queue.put(-1);  // 发送结束信号
        }

        public void consumer() throws InterruptedException {
            while (true) {
                Integer value = queue.take();  // 队列空时会阻塞
                if (value == -1) break;
                System.out.println("Consumed: " + value);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ExecutorService示例 ===\n");

        // 使用固定大小线程池
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        System.out.println("所有任务完成");

        System.out.println("\n=== 生产者-消费者示例 ===\n");

        ProducerConsumer pc = new ProducerConsumer();

        Thread producerThread = new Thread(() -> {
            try {
                pc.producer();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                pc.consumer();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        Thread.sleep(100);  // 让生产者先生产一些
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        System.out.println("\n=== 最佳实践 ===\n");
        System.out.println("1. 使用线程池而不是直接创建Thread");
        System.out.println("2. 使用BlockingQueue实现生产者-消费者");
        System.out.println("3. 使用CopyOnWriteArrayList处理观察者模式");
        System.out.println("4. 避免在同步块内调用外来方法");
    }
}
