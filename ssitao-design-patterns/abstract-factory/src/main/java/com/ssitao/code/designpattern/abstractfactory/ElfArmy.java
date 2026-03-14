package com.ssitao.code.designpattern.abstractfactory;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/2/27 17:49
 */
public class ElfArmy implements Army{
    static final String DESCRIPTION = "This is the Elven Army!";
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
