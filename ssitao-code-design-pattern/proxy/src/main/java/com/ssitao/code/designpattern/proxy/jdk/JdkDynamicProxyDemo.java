package com.ssitao.code.designpattern.proxy.jdk;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理演示
 *
 * 原理：
 * 使用Proxy.newProxyInstance()创建代理对象
 * 代理对象会在运行时生成，需要目标类实现接口
 */
public class JdkDynamicProxyDemo {

    public static void main(String[] args) {
        System.out.println("========== JDK动态代理示例 ==========\n");

        // 1. 创建目标对象
        UserService target = new UserServiceImpl();

        // 2. 创建调用处理器
        PerformanceInvocationHandler handler = new PerformanceInvocationHandler(target);

        // 3. 创建代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),  // 类加载器
            target.getClass().getInterfaces(),   // 目标类实现的接口
            handler                             // 调用处理器
        );

        // 4. 通过代理调用方法
        System.out.println("--- 调用addUser ---");
        proxy.addUser("张三");

        System.out.println("\n--- 调用getUserName ---");
        String name = proxy.getUserName("001");
        System.out.println("返回结果: " + name);

        System.out.println("\n--- 调用deleteUser ---");
        proxy.deleteUser("李四");

        // 验证代理类型
        System.out.println("\n--- 代理类型信息 ---");
        System.out.println("代理类: " + proxy.getClass().getName());
        System.out.println("父类: " + proxy.getClass().getSuperclass().getName());
        System.out.println("实现的接口: " + Arrays.toString(proxy.getClass().getInterfaces()));
    }
}
