package com.ssitao.code.designpattern.abstractfactory;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/2/27 17:48
 */
public class ElfKingdomFactory implements KingdomFactory {

    public Castle createCastle() {
        return new ElfCastle();
    }

    public King createKing() {
        return new ElfKing();
    }

    public Army createArmy() {
        return new ElfArmy();
    }

}
