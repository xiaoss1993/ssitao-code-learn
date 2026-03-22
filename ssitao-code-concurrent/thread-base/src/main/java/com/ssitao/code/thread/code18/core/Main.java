package com.ssitao.code.thread.code18.core;


import com.ssitao.code.thread.code18.task.PricesInfo;
import com.ssitao.code.thread.code18.task.Reader;
import com.ssitao.code.thread.code18.task.Writer;

/**
 * 主程序入口
 * 演示读写锁(ReadWriteLock)的使用：多个读者可以同时读取数据，但写者需要独占访问
 */
public class Main {
    public static void main(String[] args) {
        // 创建共享的价格信息对象
        PricesInfo pricesInfo = new PricesInfo();

        // 创建5个读者线程数组
        Reader readers[] = new Reader[5];
        Thread threadsReader[] = new Thread[5];

        // 创建5个读者线程，每个读者都会读取10次价格信息
        for (int i = 0; i < 5; i++) {
            readers[i] = new Reader(pricesInfo);
            threadsReader[i] = new Thread(readers[i], "Reader-" + i);
        }

        // 创建一个写者线程，写者会修改3次价格信息
        Writer writer = new Writer(pricesInfo);
        Thread threadWriter = new Thread(writer, "Writer");

        // 启动所有读者线程（同时运行，提高并发读取演示效果）
        for (int i = 0; i < 5; i++) {
            threadsReader[i].start();
        }
        // 启动写者线程
        threadWriter.start();
    }
}
