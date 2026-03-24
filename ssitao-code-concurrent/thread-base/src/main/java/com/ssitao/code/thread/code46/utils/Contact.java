package com.ssitao.code.thread.code46.utils;

/**
 * 联系人类 - 数据实体
 *
 * 作为 ConcurrentSkipListMap 的值类型存储
 * 设计简洁，仅包含姓名和电话两个属性
 *
 * 注意：
 * - name 通常是单个字母（A-Y），代表创建该联系人的任务ID
 * - phone 是字符串形式的数字（1000-1999）
 */
public class Contact {

    /**
     * 联系人姓名/名称
     * 在本例中，对应任务的任务ID（字母前缀）
     */
    private String name;

    /**
     * 联系人电话
     * 字符串类型，便于拼接作为map的key
     */
    private String phone;

    /**
     * 构造函数
     *
     * @param name  联系人姓名
     * @param phone 联系人电话
     */
    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    /**
     * 获取联系人姓名
     *
     * @return 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取联系人电话
     *
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }
}
