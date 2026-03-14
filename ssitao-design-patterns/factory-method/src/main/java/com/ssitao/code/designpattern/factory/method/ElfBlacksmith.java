package com.ssitao.code.designpattern.factory.method;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/3/11 09:20
 */
public class ElfBlacksmith implements Blacksmith{
    @Override
    public Weapon manufactureWeapon(WeaponType weaponType) {
        return new ElfWeapon(weaponType);
    }
}
