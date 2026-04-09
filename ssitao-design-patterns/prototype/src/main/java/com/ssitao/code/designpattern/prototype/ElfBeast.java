
package com.ssitao.code.designpattern.prototype;

/**
 * 
 * ElfBeast
 *
 */
public class ElfBeast extends Beast {

  public ElfBeast() {}

  @Override
  public Beast clone() throws CloneNotSupportedException {
    return new ElfBeast();
  }

  @Override
  public String toString() {
    return "Elven eagle";
  }

}
