package com.ssitao.code.designpattern.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * CGLIB代理演示
 *
 * 原理：
 * 使用Enhancer创建代理对象
 * 通过继承目标类来实现代理
 * 不需要目标类实现接口
 */
public class CglibProxyDemo {

    public static void main(String[] args) {
        System.out.println("========== CGLIB代理示例 ==========\n");

        // 1. 创建Enhancer
        Enhancer enhancer = new Enhancer();

        // 2. 设置父类（目标类）
        enhancer.setSuperclass(UserDao.class);

        // 3. 设置方法拦截器
        enhancer.setCallback(new LogMethodInterceptor());

        // 4. 创建代理对象
        UserDao proxy = (UserDao) enhancer.create();

        // 5. 调用方法
        System.out.println("--- 调用insert ---");
        proxy.insert("张三");

        System.out.println("\n--- 调用select ---");
        String result = proxy.select("001");
        System.out.println("返回结果: " + result);

        System.out.println("\n--- 调用delete ---");
        proxy.delete("李四");

        // 验证代理类型
        System.out.println("\n--- 代理类型信息 ---");
        System.out.println("代理类: " + proxy.getClass().getName());
        System.out.println("父类: " + proxy.getClass().getSuperclass().getName());
    }
}
