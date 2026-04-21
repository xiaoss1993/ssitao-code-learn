package com.ssitao.code.designpattern.proxy;

/**
 * 代理模式 - 抽象主题接口
 *
 * 代理模式(Proxy Pattern)：
 * 为其他对象提供一种代理以控制对这个对象的访问
 */
public interface Image {

    // 显示图片
    void display();

    // 获取文件名
    String getFileName();
}
