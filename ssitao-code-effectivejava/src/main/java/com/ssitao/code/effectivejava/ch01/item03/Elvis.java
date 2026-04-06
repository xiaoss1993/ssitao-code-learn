package com.ssitao.code.effectivejava.ch01.item03;

/**
 * Item 3: Enforce singleton with enum type (best approach)
 */
public enum Elvis {
    INSTANCE;

    private String name = "Elvis Presley";

    public void leaveTheBuilding() {
        System.out.println("Whoa baby, I'm out of here!");
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        System.out.println("=== Enum Singleton Demo ===\n");

        Elvis elvis1 = Elvis.INSTANCE;
        Elvis elvis2 = Elvis.INSTANCE;

        System.out.println("elvis1 == elvis2: " + (elvis1 == elvis2));
        System.out.println("elvis1.getName(): " + elvis1.getName());
        elvis1.leaveTheBuilding();
    }
}
