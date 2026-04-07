package com.ssitao.code.effectivejava.ch01.item03;

/**
 * 条目3：使用枚举类型实现单例模式（最佳方式）
 *
 * 枚举单例的优点：
 * 1. 线程安全：JVM保证枚举实例唯一
 * 2. 防止反射攻击：枚举构造器被调用多次会抛异常
 * 3. 防止反序列化创建新实例：枚举实例不可被反序列化重新创建
 * 4. 简洁：无需编写复杂的双重检查锁定或延迟初始化
 *
 * 这是实现单例模式的最佳方式
 */
public enum Elvis {
    INSTANCE;  // 唯一实例

    private String name = "Elvis Presley";

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm out of here!");
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        System.out.println("=== 枚举单例模式示例 ===\n");

        // 获取单例实例
        Elvis elvis1 = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;

        // 验证是同一个实例
        System.out.println("elvis1 == elvis2: " + (elvis1 == elvis2));
        // 结果为 true，说明两个引用指向同一个实例

        System.out.println("elvis1.getName(): " + elvis1.getName());
        elvis1.leaveTheBuilding();
    }
}
