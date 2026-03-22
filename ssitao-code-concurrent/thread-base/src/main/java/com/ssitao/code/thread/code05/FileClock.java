package com.ssitao.code.thread.code05;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileClock implements Runnable{
    @Override
    public void run() {
        for(int i =0 ;i<10;i++){
            System.out.printf("%s\n", new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (InterruptedException e){
                // 当线程被中断时，释放或者关闭线程正在使用的资源。
                System.out.printf("The FileClock has been interrupted");
                return; // 发生异常就跳出
            }
        }
    }
}
