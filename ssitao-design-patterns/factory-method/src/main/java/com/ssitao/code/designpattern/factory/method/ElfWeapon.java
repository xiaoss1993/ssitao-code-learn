package com.ssitao.code.designpattern.factory.method;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/3/11 09:20
 */
public class ElfWeapon implements Weapon{
    private WeaponType weaponType;

    public ElfWeapon(WeaponType weaponType){
        this.weaponType = weaponType;
    }

    @Override
    public String toString() {
        return "ElfWeapon{" +
                "weaponType=" + weaponType +
                '}';
    }

    @Override
    public WeaponType getWeaponType() {
        return weaponType;
    }
}
