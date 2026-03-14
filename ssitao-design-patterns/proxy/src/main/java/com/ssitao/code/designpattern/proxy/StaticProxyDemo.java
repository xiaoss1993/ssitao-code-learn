package com.ssitao.code.designpattern.proxy;

/**
 * 静态代理模式演示
 */
public class StaticProxyDemo {

    public static void main(String[] args) {
        System.out.println("========== 静态代理模式示例 ==========\n");

        // 直接访问真实对象
        System.out.println("--- 直接访问 ---");
        Image realImage = new RealImage("photo.jpg");
        realImage.display();

        // 通过代理访问
        System.out.println("\n--- 通过代理访问 ---");
        Image proxyImage = new ImageProxy("photo.jpg");
        proxyImage.display();

        // 演示延迟加载
        System.out.println("\n--- 演示延迟加载 ---");
        Image lazyProxy = new ImageProxy("lazy_photo.jpg");
        System.out.println("代理对象已创建，但图片未加载");
        lazyProxy.display();
        System.out.println("再次显示:");
        lazyProxy.display();
    }
}
