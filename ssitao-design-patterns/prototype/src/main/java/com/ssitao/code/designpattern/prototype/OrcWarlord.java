package com.ssitao.code.designpattern.prototype;

/**
 * 
 * OrcWarlord
 *
 */
public class OrcWarlord extends Warlord {

  public OrcWarlord() {}

  @Override
  public Warlord clone() throws CloneNotSupportedException {
    return new OrcWarlord();
  }

  @Override
  public String toString() {
    return "Orcish warlord";
  }

}
