
package com.ssitao.code.designpattern.decorator;

/**
 * 
 * Troll implements {@link Hostile} interface directly.
 *
 */
public class Troll implements Hostile {

  @Override
  public void attack() {
    System.out.println("The troll swings at you with a club!");
  }

  @Override
  public int getAttackPower() {
    return 10;
  }

  @Override
  public void fleeBattle() {
    System.out.println("The troll shrieks in horror and runs away!");
  }
}
