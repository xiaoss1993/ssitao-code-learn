package com.concurrency.chapter3.task;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier循环屏障示例 - 在集合点同步
 */
public class CyclicBarrierDemo {

    private static final int PARTY_COUNT = 3;

    public static void demo() throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(PARTY_COUNT, () -> {
            System.out.println("所有玩家已就位，游戏开始!");
        });

        System.out.println("模拟" + PARTY_COUNT + "个玩家加载游戏...");

        for (int i = 1; i <= PARTY_COUNT; i++) {
            final int playerId = i;
            new Thread(() -> {
                try {
                    System.out.println("玩家" + playerId + " 正在加载...");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("玩家" + playerId + " 加载完成，等待其他人...");
                    barrier.await(); // 等待其他玩家
                    System.out.println("玩家" + playerId + " 开始游戏!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Player-" + playerId).start();
        }

        Thread.sleep(5000);
    }
}
