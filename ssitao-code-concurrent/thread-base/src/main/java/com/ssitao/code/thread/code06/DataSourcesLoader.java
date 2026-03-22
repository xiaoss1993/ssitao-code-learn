package com.ssitao.code.thread.code06;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DataSourcesLoader implements Runnable{
    @Override
    public void run() {
        // 输出一条消息
        System.out.printf("Beginning data sources loading: %s\n",new Date());

        // 休眠10s
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 输出一条消息
        System.out.printf("Data sources loading has finished: %s\n",new Date());
    }
}
