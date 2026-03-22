package com.ssitao.code.thread.code07.task;

import com.ssitao.code.thread.code07.event.Event;

import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * 写事件的类，每一秒钟产生一个事件对象
 */
public class WriterTask implements Runnable{
    /**
     * 用于存储事件对象的队列
     */
    Deque<Event> deque;

    /**
     * 构造函数
     *
     * @param deque 存储事件对象的队列
     */
    public WriterTask(Deque<Event> deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        for(int i= 1;i<100;i++){
            Event event = new Event();
            event.setDate(new Date());
            event.setEvent(String.format("The thread %s has generated an event", Thread.currentThread().getId()));
            // 将事件添加对队列头部
            deque.addFirst(event);
            try {
                // 休眠一秒种
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
