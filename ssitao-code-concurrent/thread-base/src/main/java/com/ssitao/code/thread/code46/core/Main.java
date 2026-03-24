package com.ssitao.code.thread.code46.core;


import com.ssitao.code.thread.code46.task.Task;
import com.ssitao.code.thread.code46.utils.Contact;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 主程序 - ConcurrentSkipListMap 并发示例
 *
 * 演示使用 ConcurrentSkipListMap 实现高并发的有序映射
 * 创建25个线程并发插入数据，验证其线程安全性和有序性
 *
 * 核心概念：
 * 1. ConcurrentSkipListMap - 基于跳表实现的并发有序映射
 *    - 线程安全，无需外部同步
 *    - 按key字典序排序（这里是字符串排序）
 *    - 支持范围查询（subMap）
 *
 * 2. 跳表特性：
 *    - 多层链表结构，查找效率 O(log n)
 *    - 无锁设计，高并发性能优秀
 */
public class Main {
    public static void main(String[] args) {

        // 创建并发跳表映射，key为String类型，value为Contact类型
        // String类型的key会按字典序排列，这是理解输出顺序的关键
        ConcurrentSkipListMap<String, Contact> map = new ConcurrentSkipListMap<>();

        // 创建25个线程，对应25个任务（A到Y，每个代表一个字母开头）
        Thread threads[] = new Thread[25];
        int counter = 0;

        // 启动25个并发任务
        // Task A: 生成1000个联系人，key前缀为 "A"
        // Task B: 生成1000个联系人，key前缀为 "B"
        // ...
        // Task Y: 生成1000个联系人，key前缀为 "Y"
        for (char i = 'A'; i < 'Z'; i++) {
            Task task = new Task(map, String.valueOf(i));
            threads[counter] = new Thread(task);
            threads[counter].start();
            counter++;
        }

        // 等待所有任务执行完成
        // join() 确保主线程在读取map之前，所有生产者线程都已完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 此时map中应该有 25 * 1000 = 25000 个联系人
        System.out.printf("Main: Size of the map: %d\n", map.size());

        // 获取map中的第一个条目（key字典序最小）
        // 由于key格式是 "字母+电话号码"，字典序最小的应该是 "A" 开头的
        Map.Entry<String, Contact> element;
        Contact contact;

        element = map.firstEntry();
        contact = element.getValue();
        System.out.printf("Main: First Entry: %s: %s\n", contact.getName(), contact.getPhone());

        // 获取map中的最后一个条目（key字典序最大）
        // 应该是 "Y" 开头的
        element = map.lastEntry();
        contact = element.getValue();
        System.out.printf("Main: Last Entry: %s: %s\n", contact.getName(), contact.getPhone());

        // 演示范围查询：获取子map [A1996, B1002)
        // subMap 返回的是一个并发可遍历的子视图
        // 会包含所有 A1996 <= key < B1002 的条目
        System.out.printf("Main: Submap from A1996 to B1002: \n");
        ConcurrentNavigableMap<String, Contact> submap = map.subMap("A1996", "B1002");

        // 使用 pollFirstEntry 迭代获取子map中的所有条目
        // pollFirstEntry 会删除并返回第一个条目
        do {
            element = submap.pollFirstEntry();
            if (element != null) {
                contact = element.getValue();
                System.out.printf("%s: %s\n", contact.getName(), contact.getPhone());
            }
        } while (element != null);
    }
}
