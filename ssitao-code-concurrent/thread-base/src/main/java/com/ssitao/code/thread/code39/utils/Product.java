package com.ssitao.code.thread.code39.utils;

/**
 * 产品实体类 - 用于Fork/Join示例的数据模型
 *
 * 存储产品的基本信息：
 * - name: 产品名称
 * - price: 产品价格
 *
 * 注意：由于多个线程会并发修改产品价格，
 * 这个类被设计为可变类，由调用者确保线程安全
 */
public class Product {
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品价格
     */
    private double price;

    /**
     * 获取产品名称
     *
     * @return 产品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置产品名称
     *
     * @param name 产品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取产品价格
     *
     * @return 产品价格
     */
    public double getPrice() {
        return price;
    }

    /**
     * 设置产品价格
     *
     * @param price 产品价格
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
