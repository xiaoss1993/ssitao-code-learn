package com.ssitao.code.datastruct.hashtable;

/**
 * 哈希表测试类 - 演示哈希表的基本操作
 */
public class HashMapTest {

    public static void main(String[] args) {
        System.out.println("========== 哈希表学习 ==========\n");

        HashMap<String, Integer> map = new HashMap<>(4);  // 小容量方便观察扩容

        // 基本操作
        testBasicOperations(map);

        // 扩容演示
        System.out.println("\n========== 扩容演示 ==========\n");
        HashMap<String, Integer> map2 = new HashMap<>(4);
        testResize(map2);

        // 哈希表 vs 链表性能对比
        System.out.println("\n========== 复杂度对比 ==========\n");
        printComplexity();
    }

    // 测试哈希表基本操作
    private static void testBasicOperations(HashMap<String, Integer> map) {
        System.out.println("--- 基本操作 ---");
        System.out.println("isEmpty: " + map.isEmpty() + ", size: " + map.size());

        // 添加键值对
        System.out.println("\n--- 添加 put ---");
        map.put("apple", 1);
        System.out.println("put(\"apple\", 1): " + map);

        map.put("banana", 2);
        System.out.println("put(\"banana\", 2): " + map);

        map.put("cherry", 3);
        System.out.println("put(\"cherry\", 3): " + map);

        map.put("date", 4);
        System.out.println("put(\"date\", 4): " + map);

        // 更新已存在的键
        System.out.println("\n--- 更新已存在键 ---");
        map.put("apple", 10);
        System.out.println("put(\"apple\", 10) [更新]: " + map);
        System.out.println("get(\"apple\"): " + map.get("apple"));

        // 获取
        System.out.println("\n--- 获取 get ---");
        System.out.println("get(\"banana\"): " + map.get("banana"));
        System.out.println("get(\"不存在\"): " + map.get("不存在"));

        // 包含判断
        System.out.println("\n--- 包含判断 ---");
        System.out.println("containsKey(\"cherry\"): " + map.containsKey("cherry"));
        System.out.println("containsKey(\"不存在\"): " + map.containsKey("不存在"));
        System.out.println("containsValue(10): " + map.containsValue(10));
        System.out.println("containsValue(99): " + map.containsValue(99));

        // 删除
        System.out.println("\n--- 删除 remove ---");
        System.out.println("remove(\"banana\"): " + map.remove("banana"));
        System.out.println("remove后: " + map);

        System.out.println("remove(\"不存在\"): " + map.remove("不存在"));
        System.out.println("remove后: " + map);

        // 清空
        System.out.println("\n--- 清空 clear ---");
        map.clear();
        System.out.println("clear后: " + map);
        System.out.println("isEmpty: " + map.isEmpty());
    }

    // 扩容演示
    private static void testResize(HashMap<String, Integer> map) {
        System.out.println("初始容量: " + map.getCapacity());
        System.out.println("负载因子阈值: " + map.getLoadFactor());

        System.out.println("\n依次添加元素:");
        String[] keys = {"a", "b", "c", "d", "e", "f", "g"};
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], i + 1);
            System.out.println("put(\"" + keys[i] + "\", " + (i + 1) + "): " + map);
        }

        System.out.println("\n最终状态:");
        System.out.println("size = " + map.size());
        System.out.println("capacity = " + map.getCapacity());
        System.out.println("实际负载因子 = " + map.getLoadFactor());
    }

    // 打印复杂度对比
    private static void printComplexity() {
        System.out.println("哈希表操作复杂度（平均情况）:");
        System.out.println(repeat("-", 35));
        System.out.println("| 操作      | 数组   | 链表   | 哈希表 |");
        System.out.println(repeat("-", 35));
        System.out.println("| 查找      | O(n)   | O(n)   | O(1)   |");
        System.out.println("| 插入      | O(n)   | O(1)   | O(1)   |");
        System.out.println("| 删除      | O(n)   | O(n)   | O(1)   |");
        System.out.println(repeat("-", 35));

        System.out.println("\n哈希表最坏情况 O(n):");
        System.out.println("- 所有键都散列到同一个桶（哈希冲突严重）");
        System.out.println("- 解决：使用好的哈希函数 + 扩容机制");

        System.out.println("\n哈希表适用场景:");
        System.out.println("- 快速查找需求");
        System.out.println("- 键值对存储");
        System.out.println("- 去重");
        System.out.println("- 缓存实现");
    }

    // 字符串重复工具方法（兼容Java 8）
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}