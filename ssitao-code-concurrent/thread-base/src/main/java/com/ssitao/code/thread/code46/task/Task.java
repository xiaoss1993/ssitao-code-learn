package com.ssitao.code.thread.code46.task;


import com.ssitao.code.thread.code46.utils.Contact;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 任务类 - Contact 生产者
 *
 * 每个 Task 实例在独立线程中运行
 * 负责向 ConcurrentSkipListMap 中插入联系人数据
 *
 * 设计特点：
 * 1. 25个线程并发插入数据，验证 ConcurrentSkipListMap 的并发安全性
 * 2. 每个任务生成1000个联系人
 * 3. key格式：前缀（字母）+ 电话号码（1000-1999）
 *    - 例如 Task "A" 创建的联系人 key 为 "A1000", "A1001", ..., "A1999"
 *
 * ConcurrentSkipListMap 保证：
 * - 线程安全，多线程可同时插入
 * - 按key字典序排序存储
 * - 无需额外同步措施
 */
public class Task implements Runnable {

    /**
     * 共享的并发跳表映射
     * 用于存储所有任务创建的联系人
     */
    private ConcurrentSkipListMap<String, Contact> map;

    /**
     * 任务标识（也作为联系人名称的前缀）
     * 例如 "A", "B", "C" ... "Y"
     */
    private String id;

    /**
     * 构造函数，初始化任务属性
     *
     * @param map 共享的并发跳表映射
     * @param id  任务标识
     */
    public Task(ConcurrentSkipListMap<String, Contact> map, String id) {
        this.id = id;
        this.map = map;
    }

    /**
     * 任务执行入口
     *
     * 核心逻辑：
     * 1. 循环1000次，每次创建一个Contact
     * 2. Contact的name为当前任务id（字母前缀）
     * 3. Contact的phone为 i + 1000（1000-1999）
     * 4. map的key格式：id + phone，例如 "A1000", "A1001"
     *
     * 最终结果：
     * - 25个任务共创建 25 * 1000 = 25000 个联系人
     * - 所有按key字典序排列
     */
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            // 创建联系人对象
            Contact contact = new Contact(id, String.valueOf(i + 1000));
            // 构建key：前缀 + 电话号码，如 "A1000", "B1500" 等
            // put操作是线程安全的，无需额外同步
            map.put(id + contact.getPhone(), contact);
        }
    }
}
