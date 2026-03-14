package com.ssitao.code.designpattern.abstractfactory;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/2/27 17:47
 */
public class OrcKingdomFactory implements KingdomFactory {

    public Castle createCastle() {
        return new OrcCastle();
    }

    public King createKing() {
        return new OrcKing();
    }

    public Army createArmy() {
        return new OrcArmy();
    }
}
