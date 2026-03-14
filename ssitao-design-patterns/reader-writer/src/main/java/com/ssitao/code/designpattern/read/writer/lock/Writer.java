package com.ssitao.code.designpattern.read.writer.lock;

import java.util.concurrent.locks.Lock;

public class Writer implements Runnable {

    private Lock writeLock;

    private String name;

    public Writer(String name, Lock writeLock) {
        this.name = name;
        this.writeLock = writeLock;
    }


    @Override
    public void run() {
        writeLock.lock();
        try {
            write();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public void write() throws InterruptedException {
        System.out.printf(name + " begin");
        Thread.sleep(500);
        System.out.printf(name + " finish ");
    }
}
