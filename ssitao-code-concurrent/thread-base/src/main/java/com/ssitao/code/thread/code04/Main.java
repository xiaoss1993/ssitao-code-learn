package com.ssitao.code.thread.code04;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        // 创建一个运行对象和一个运行它的线程
        FileSearch searcher = new FileSearch("/Users/ssitao", "readme.txt");
        Thread thread = new Thread(searcher);

        thread.start(); // 启动线程

        try {
            TimeUnit.SECONDS.sleep(10); // 主线程休眠10s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt(); // 中断线程
    }
}
