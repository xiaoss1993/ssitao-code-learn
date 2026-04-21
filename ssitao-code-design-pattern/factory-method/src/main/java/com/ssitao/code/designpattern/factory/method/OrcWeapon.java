package com.ssitao.code.designpattern.factory.method;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/3/11 09:17
 */
public class OrcWeapon implements Weapon{

    private WeaponType  weaponType;
    public OrcWeapon(WeaponType weaponType){
        this.weaponType = weaponType;
    }

    @Override
    public String toString() {
        return "OrcWeapon{" + "weaponType=" + weaponType + '}';
    }

    @Override
    public WeaponType getWeaponType() {
        return weaponType;
    }
}
