package com.ssitao.code.effectivejava.ch05.item37;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 条目37：用EnumMap代替序数索引
 *
 * 用 ordinal() 索引数组的问题：
 * 1. 需要魔法数字（+1/-1偏移）
 * 2. 不是类型安全，可以存储错误的相位
 * 3. ordinal() 顺序改变就坏了
 *
 * EnumMap的优点：
 * 1. 类型安全：只能是Phase枚举
 * 2. 性能好：内部用数组，O(1)访问
 * 3. 可读性好：无需处理ordinal偏移
 *
 * 本例：物质相变状态机
 */
public enum Phase {
    SOLID,   // 固态
    LIQUID,  // 液态
    GAS;     // 气态

    /**
     * 相变枚举
     */
    public enum Transition {
        MELT(SOLID, LIQUID),      // 熔化
        FREEZE(LIQUID, SOLID),   // 凝固
        BOIL(LIQUID, GAS),        // 沸腾
        CONDENSE(GAS, LIQUID),    // 凝结
        SUBLIME(SOLID, GAS),      // 升华
        DEPOSIT(GAS, SOLID);      // 凝华

        private final Phase from;
        private final Phase to;

        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        // 使用EnumMap缓存所有相变
        private static final Map<Phase, Map<Phase, Transition>> TRANSITION_MAP;

        static {
            TRANSITION_MAP = Stream.of(values())
                .collect(Collectors.groupingBy(
                    t -> t.from,
                    () -> new EnumMap<>(Phase.class),
                    Collectors.toMap(
                        t -> t.to,
                        t -> t,
                        (a, b) -> b,
                        () -> new EnumMap<>(Phase.class)
                    )
                ));
        }

        /**
         * 根据起始和目标相位获取相变
         */
        public static Transition from(Phase from, Phase to) {
            return TRANSITION_MAP.get(from).get(to);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== EnumMap示例 - 相变 ===\n");

        // 演示相变
        Phase[] phases = Phase.values();
        for (Phase from : phases) {
            for (Phase to : phases) {
                Transition transition = Transition.from(from, to);
                if (transition != null) {
                    System.out.printf("%s -> %s: %s%n", from, to, transition);
                }
            }
        }

        System.out.println("\n=== 为什么不使用ordinal数组？ ===\n");
        System.out.println("错误: Phase[][] transitions = new Phase[3][3];");
        System.out.println("     - 需要 +1/-1 偏移计算");
        System.out.println("     - 不是类型安全，可能存储错误的相位");
        System.out.println("     - ordinal() 顺序改变会坏");
        System.out.println();
        System.out.println("正确: EnumMap<Phase, EnumMap<Phase, Transition>>");
        System.out.println("     - 类型安全");
        System.out.println("     - O(1)查找性能");
        System.out.println("     - 无需ordinal魔法数字");
    }
}
