package com.ssitao.code.designpattern.decorator;

/**
 * 数据源装饰器基类 - 抽象装饰器
 */
public abstract class DataSourceDecorator implements DataSource {

    protected DataSource wrappedSource;

    public DataSourceDecorator(DataSource source) {
        this.wrappedSource = source;
    }

    @Override
    public void writeData(String data) {
        wrappedSource.writeData(data);
    }

    @Override
    public String readData() {
        return wrappedSource.readData();
    }
}
