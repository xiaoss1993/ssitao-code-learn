package com.ssitao.code.designpattern.proxy;

/**
 * 代理类 - 图片代理
 * 代理模式中的Proxy角色
 *
 * 特点：
 * 1. 保存真实对象的引用
 * 2. 控制对真实对象的访问
 * 3. 可以在访问前后添加额外操作
 */
public class ImageProxy implements Image {

    private RealImage realImage;
    private String fileName;

    public ImageProxy(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        // 延迟加载：只有第一次显示时才创建真实对象
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
