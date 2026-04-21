
package com.ssitao.code.designpattern.proxy;

/**
 * 
 * Wizard
 *
 */
public class Wizard {

  private String name;

  public Wizard(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }

}
