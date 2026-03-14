package com.ssitao.code.designpattern.read.writer.lock;

import java.util.concurrent.locks.Lock;

public class Reader implements Runnable {

    private Lock readLock;

    private String name;

    public Reader(String name, Lock readLock) {
        this.name = name;
        this.readLock = readLock;
    }


    @Override
    public void run() {
        readLock.lock();
        try {
            read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Simulate the read operation
     */
    public void read() throws InterruptedException {
        System.out.println(name + " begin");
        Thread.sleep(250);
        System.out.println(name + " finish");
    }
}
