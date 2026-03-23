package com.ssitao.code.thread.code39.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品生成器类 - 用于生成测试数据
 *
 * 根据指定数量生成Product对象列表，每个产品：
 * - 名称格式： "Product " + 索引号（如 "Product 0", "Product 1"）
 * - 初始价格： 10
 *
 * 主要用途：
 * - 为Fork/Join示例生成大量测试产品
 * - 确保所有产品的初始状态一致，便于验证并行处理结果
 */
public class ProductListGenerator {

    /**
     * 生成产品列表
     *
     * @param size 产品数量
     * @return 包含指定数量产品的列表
     */
    public List<Product> generate(int size) {
        // 使用ArrayList存储产品
        List<Product> ret = new ArrayList<>();

        // 循环创建指定数量的产品
        for (int i = 0; i < size; i++) {
            Product product = new Product();
            // 设置产品名称：Product 0, Product 1, Product 2, ...
            product.setName("Product " + i);
            // 设置初始价格：所有产品初始价格都是10
            product.setPrice(10);
            ret.add(product);
        }

        return ret;
    }
}
