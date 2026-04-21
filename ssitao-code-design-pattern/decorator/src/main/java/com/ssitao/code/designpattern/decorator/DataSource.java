package com.ssitao.code.designpattern.decorator;

/**
 * 数据源接口 - 装饰器模式
 */
public interface DataSource {

    // 写入数据
    void writeData(String data);

    // 读取数据
    String readData();
}
