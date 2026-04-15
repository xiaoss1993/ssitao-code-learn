package com.learn.crazyjava.lesson09_enum;

/**
 * 第9课：枚举 - 基本枚举
 */
public enum SeasonEnum {
    SPRING("春"),
    SUMMER("夏"),
    AUTUMN("秋"),
    WINTER("冬");

    private final String chineseName;

    SeasonEnum(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public static void main(String[] args) {
        // 遍历枚举
        for (SeasonEnum s : SeasonEnum.values()) {
            System.out.println(s.ordinal() + ": " + s.name() + " = " + s.getChineseName());
        }

        // 根据名称获取枚举值
        SeasonEnum s = SeasonEnum.valueOf("SPRING");
        System.out.println("SPRING: " + s.getChineseName());

        // switch使用枚举
        printSeason(SeasonEnum.SUMMER);
    }

    public static void printSeason(SeasonEnum season) {
        switch (season) {
            case SPRING:
                System.out.println("春天来了");
                break;
            case SUMMER:
                System.out.println("夏天真热");
                break;
            case AUTUMN:
                System.out.println("秋天丰收");
                break;
            case WINTER:
                System.out.println("冬天很冷");
                break;
        }
    }
}
