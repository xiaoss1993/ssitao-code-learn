package com.ssitao.code.designpattern.factory.method;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/3/11 09:22
 */
public class OrcBlacksmith implements Blacksmith{
    @Override
    public Weapon manufactureWeapon(WeaponType weaponType) {
        return new OrcWeapon(weaponType);
    }
}
