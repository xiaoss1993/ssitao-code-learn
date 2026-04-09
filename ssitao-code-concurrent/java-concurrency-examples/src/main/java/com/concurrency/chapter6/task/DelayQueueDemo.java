package com.concurrency.chapter6.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * DelayQueue延迟队列示例
 */
public class DelayQueueDemo {

    public static void demo() throws InterruptedException {
        BlockingQueue<DelayedItem> queue = new DelayQueue<>();

        // 添加延迟项目（延迟时间递减）
        queue.put(new DelayedItem("项目A", 3000)); // 3秒后过期
        queue.put(new DelayedItem("项目B", 1000)); // 1秒后过期
        queue.put(new DelayedItem("项目C", 2000)); // 2秒后过期

        System.out.println("按过期时间顺序取出（先过期的先出）：");

        long start = System.currentTimeMillis();
        while (!queue.isEmpty()) {
            DelayedItem item = queue.take();
            long elapsed = System.currentTimeMillis() - start;
            System.out.println("取出: " + item.name + " (已过期" + elapsed + "ms)");
        }
    }

    static class DelayedItem implements Delayed {
        String name;
        long expireTime; // 过期时间（毫秒）

        DelayedItem(String name, long delayMillis) {
            this.name = name;
            this.expireTime = System.currentTimeMillis() + delayMillis;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return expireTime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            DelayedItem other = (DelayedItem) o;
            return Long.compare(this.expireTime, other.expireTime);
        }
    }
}
