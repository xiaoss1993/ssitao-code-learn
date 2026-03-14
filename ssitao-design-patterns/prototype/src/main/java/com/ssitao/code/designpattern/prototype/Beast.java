package com.ssitao.code.designpattern.prototype;

/**
 * 
 * Beast
 *
 */
public abstract class Beast extends Prototype {

  @Override
  public abstract Beast clone() throws CloneNotSupportedException;

}
