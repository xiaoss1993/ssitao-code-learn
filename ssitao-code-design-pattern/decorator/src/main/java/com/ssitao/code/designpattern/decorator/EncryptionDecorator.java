package com.ssitao.code.designpattern.decorator;

import java.util.Base64;

/**
 * 加密装饰器 - 具体装饰器
 */
public class EncryptionDecorator extends DataSourceDecorator {

    public EncryptionDecorator(DataSource source) {
        super(source);
    }

    @Override
    public void writeData(String data) {
        // 简单加密: Base64编码
        String encrypted = Base64.getEncoder().encodeToString(data.getBytes());
        super.writeData(encrypted);
        System.out.println("[加密] 数据已加密写入");
    }

    @Override
    public String readData() {
        String data = super.readData();
        // 解密
        String decrypted = new String(Base64.getDecoder().decode(data));
        System.out.println("[加密] 数据已解密读取");
        return decrypted;
    }
}
