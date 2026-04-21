package com.ssitao.code.designpattern.decorator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志装饰器 - 具体装饰器
 */
public class LoggingDecorator extends DataSourceDecorator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LoggingDecorator(DataSource source) {
        super(source);
    }

    @Override
    public void writeData(String data) {
        String log = String.format("[%s] 写入数据: %s", LocalDateTime.now().format(FORMATTER), data);
        System.out.println(log);
        super.writeData(data);
    }

    @Override
    public String readData() {
        String data = super.readData();
        String log = String.format("[%s] 读取数据: %s", LocalDateTime.now().format(FORMATTER), data);
        System.out.println(log);
        return data;
    }
}
