package com.ssitao.code.effectivejava.ch01.item02;

/**
 * Item 2: Builder pattern when constructor has multiple parameters
 */
public class NutritionFacts {
    private final int servingSize;    // required
    private final int servings;       // required
    private final int calories;       // optional
    private final int fat;            // optional
    private final int sodium;         // optional
    private final int carbohydrate;    // optional

    public static class Builder {
        private final int servingSize;
        private final int servings;

        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int val) {
            this.calories = val;
            return this;
        }

        public Builder fat(int val) {
            this.fat = val;
            return this;
        }

        public Builder sodium(int val) {
            this.sodium = val;
            return this;
        }

        public Builder carbohydrate(int val) {
            this.carbohydrate = val;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

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
        System.out.println("=== Builder Pattern Demo ===\n");

        NutritionFacts cocaCola = new Builder(240, 8)
            .calories(100)
            .fat(0)
            .sodium(35)
            .carbohydrate(27)
            .build();

        NutritionFacts orangeJuice = new Builder(240, 8)
            .calories(110)
            .fat(0)
            .sodium(0)
            .carbohydrate(26)
            .build();

        System.out.println("Coca Cola: " + cocaCola);
        System.out.println("Orange Juice: " + orangeJuice);
    }
}
