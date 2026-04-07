package com.ssitao.code.effectivejava.ch01.item02;

/**
 * 条目2：当构造器参数过多时使用Builder模式
 *
 * Builder模式的优点：
 * 1. 代码可读性强：链式调用，如 new Builder(240, 8).calories(100).fat(0)
 * 2. 参数较多时不会像重叠构造器模式那样难以阅读
 * 3. 可以给参数命名，比索引更清晰
 * 4. 可以设置默认值
 * 5. 可以验证参数的合法性
 */
public class NutritionFacts {
    // 必须设置的参数
    private final int servingSize;    // 每份大小（ml）- 必须
    private final int servings;       // 份数 - 必须

    // 可选参数，有默认值
    private final int calories;       // 卡路里 - 可选，默认0
    private final int fat;           // 脂肪(g) - 可选，默认0
    private final int sodium;        // 钠(mg) - 可选，默认0
    private final int carbohydrate;  // 碳水化合物(g) - 可选，默认0

    /**
     * Builder内部类 - 负责构建NutritionFacts对象
     */
    public static class Builder {
        // 必须参数 - final，必须赋值
        private final int servingSize;
        private final int servings;

        // 可选参数 - 有默认值0
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        /**
         * 构造器 - 必须传入必须参数
         */
        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        /**
         * 设置卡路里
         */
        public Builder calories(int val) {
            this.calories = val;
            return this;  // 返回this支持链式调用
        }

        /**
         * 设置脂肪
         */
        public Builder fat(int val) {
            this.fat = val;
            return this;
        }

        /**
         * 设置钠
         */
        public Builder sodium(int val) {
            this.sodium = val;
            return this;
        }

        /**
         * 设置碳水化合物
         */
        public Builder carbohydrate(int val) {
            this.carbohydrate = val;
            return this;
        }

        /**
         * 构建NutritionFacts对象
         */
        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    /**
     * 私有构造器 - 只能通过Builder构建
     */
    private NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
    }

    @Override
    public String toString() {
        return String.format(
            "NutritionFacts{servingSize=%dml, servings=%d, calories=%d, fat=%dg, sodium=%dmg, carbohydrate=%dg}",
            servingSize, servings, calories, fat, sodium, carbohydrate
        );
    }

    public static void main(String[] args) {
        System.out.println("=== Builder模式示例 ===\n");

        // 使用Builder创建可口可乐营养成分
        NutritionFacts cocaCola = new Builder(240, 8)
            .calories(100)
            .fat(0)
            .sodium(35)
            .carbohydrate(27)
            .build();

        // 使用Builder创建橙汁营养成分
        NutritionFacts orangeJuice = new Builder(240, 8)
            .calories(110)
            .fat(0)
            .sodium(0)
            .carbohydrate(26)
            .build();

        System.out.println("可口可乐: " + cocaCola);
        System.out.println("橙汁: " + orangeJuice);
    }
}
