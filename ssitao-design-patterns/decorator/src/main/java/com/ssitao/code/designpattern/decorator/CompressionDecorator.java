package com.ssitao.code.designpattern.decorator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * 压缩装饰器 - 具体装饰器
 */
public class CompressionDecorator extends DataSourceDecorator {

    public CompressionDecorator(DataSource source) {
        super(source);
    }

    @Override
    public void writeData(String data) {
        // 压缩数据
        byte[] input = data.getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            bos.write(buffer, 0, count);
        }
        byte[] compressed = bos.toByteArray();
        super.writeData(new String(compressed));
        System.out.println("[压缩] 数据已压缩, 原始大小: " + input.length + ", 压缩后: " + compressed.length);
    }

    @Override
    public String readData() {
        String data = super.readData();
        try {
            // 解压数据
            byte[] input = data.getBytes();
            Inflater inflater = new Inflater();
            inflater.setInput(input);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                bos.write(buffer, 0, count);
            }
            String decompressed = bos.toString();
            System.out.println("[压缩] 数据已解压");
            return decompressed;
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }
}
