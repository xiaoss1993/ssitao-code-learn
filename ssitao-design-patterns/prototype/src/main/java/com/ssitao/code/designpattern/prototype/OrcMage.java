package com.ssitao.code.designpattern.prototype;

/**
 * 
 * OrcMage
 *
 */
public class OrcMage extends Mage {

  public OrcMage() {}

  @Override
  public Mage clone() throws CloneNotSupportedException {
    return new OrcMage();
  }

  @Override
  public String toString() {
    return "Orcish mage";
  }

}
