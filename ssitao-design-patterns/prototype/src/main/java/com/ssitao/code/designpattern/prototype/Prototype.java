package com.ssitao.code.designpattern.prototype;

/**
 * 原型是一种创建型设计模式， 使你能够复制对象， 甚至是复杂对象， 而又无需使代码依赖它们所属的类
 * 使用示例： Java 的 Cloneable  （可克隆） 接口就是立即可用的原型模式。
 * 任何类都可通过实现该接口来实现可被克隆的性质。
 */
public abstract class Prototype implements Cloneable{
    @Override
    public abstract Object clone() throws CloneNotSupportedException;
}
