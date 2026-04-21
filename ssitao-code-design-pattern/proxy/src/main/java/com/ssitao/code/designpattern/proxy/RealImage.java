package com.ssitao.code.designpattern.proxy;

/**
 * 真实主题 - 真实图片
 * 代理模式中的RealSubject角色
 */
public class RealImage implements Image {

    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("加载图片: " + fileName);
    }

    @Override
    public void display() {
        System.out.println("显示图片: " + fileName);
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
