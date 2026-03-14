package com.ssitao.code.designpattern.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * 容器单例模式
 *
 * 优点：
 * 1. 可以管理多个单例
 * 2. 统一管理
 *
 * 缺点：
 * 1. 依赖注入框架
 */
public class ContainerSingleton {

    private static final Map<String, Object> SINGLETON_MAP = new HashMap<>();

    private ContainerSingleton() {
    }

    public static void register(String key, Object instance) {
        if (!SINGLETON_MAP.containsKey(key)) {
            SINGLETON_MAP.put(key, instance);
        }
    }

    public static Object getInstance(String key) {
        return SINGLETON_MAP.get(key);
    }
}
