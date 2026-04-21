package com.ssitao.code.designpattern.factory.kit;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/3/11 14:02
 */
public class App {

    public static void main(String[] args) {
        WeaponFactory factory = WeaponFactory.factory(
            builder -> {
                builder.add(WeaponType.SWORD, Sword::new);
                builder.add(WeaponType.AXE, Axe::new);
                builder.add(WeaponType.SPEAR, Spear::new);
                builder.add(WeaponType.BOW, Bow::new);
            }
        );
        Weapon axe = factory.create(WeaponType.AXE);
        System.out.println(axe);
    }
}
