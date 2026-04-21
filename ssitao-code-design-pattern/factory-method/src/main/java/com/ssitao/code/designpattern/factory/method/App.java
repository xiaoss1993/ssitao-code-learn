package com.ssitao.code.designpattern.factory.method;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/3/11 09:23
 */
public class App {
    private final Blacksmith blacksmith;

    public App(Blacksmith blacksmith){
        this.blacksmith = blacksmith;
    }

    public static void main(String[] args) {
        App app = new App(new OrcBlacksmith());
        app.manufactureWeapons();
        app = new App(new ElfBlacksmith());
        app.manufactureWeapons();
    }

    private void manufactureWeapons() {
        Weapon weapon ;
        weapon = blacksmith.manufactureWeapon(WeaponType.SPEAR);
        System.out.println(weapon);
        weapon = blacksmith.manufactureWeapon(WeaponType.AXE);
        System.out.println(weapon);
    }
}
