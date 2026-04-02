package com.ssitao.code.datastruct.hashtable;

/**
 * Map接口 - 键值对映射
 *
 * 哈希表核心思想：
 * - 通过哈希函数将键映射到数组索引
 * - 实现 O(1) 平均时间复杂度的查找、插入、删除
 *
 * 典型应用：
 * - 缓存
 * - 字典映射
 * - 去重
 */
public interface Map<K, V> {

    // 获取映射中键值对数量
    int size();

    // 判断映射是否为空
    boolean isEmpty();

    // 添加键值对，如果键已存在则覆盖值
    void put(K key, V value);

    // 根据键获取值，如果键不存在返回null
    V get(K key);

    // 根据键删除键值对，返回被删除的值
    V remove(K key);

    // 判断是否包含指定的键
    boolean containsKey(K key);

    // 判断是否包含指定的值
    boolean containsValue(V value);

    // 清空映射
    void clear();
}