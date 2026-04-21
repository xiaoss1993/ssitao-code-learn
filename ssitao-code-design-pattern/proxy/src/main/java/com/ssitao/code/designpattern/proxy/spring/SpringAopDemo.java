package com.ssitao.code.designpattern.proxy.spring;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Spring AOP代理演示
 *
 * Spring AOP使用两种代理：
 * 1. JDK动态代理 - 目标类实现接口
 * 2. CGLIB代理 - 目标类未实现接口（默认）
 */
public class SpringAopDemo {

    public static void main(String[] args) {
        System.out.println("========== Spring AOP代理示例 ==========\n");

        // 1. 创建目标对象
        UserService target = new UserServiceImpl();

        // 2. 创建代理工厂
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // 3. 添加增强通知 (使用 MethodInterceptor)
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                String methodName = invocation.getMethod().getName();
                Object[] args = invocation.getArguments();

                // 前置通知
                System.out.println("[AOP前置] 方法: " + methodName + ", 参数: " + java.util.Arrays.toString(args));

                // 执行目标方法
                long startTime = System.currentTimeMillis();
                Object result = invocation.proceed();
                long endTime = System.currentTimeMillis();

                // 后置通知
                System.out.println("[AOP后置] 方法: " + methodName + ", 返回: " + result);
                System.out.println("[AOP性能] 方法: " + methodName + ", 耗时: " + (endTime - startTime) + "ms");

                return result;
            }
        });

        // 4. 获取代理对象
        UserService proxy = (UserService) proxyFactory.getProxy();

        // 5. 调用方法
        System.out.println("--- 调用 createOrder ---");
        proxy.createOrder("iPhone 15");

        System.out.println("\n--- 调用 queryOrder ---");
        String result = proxy.queryOrder("12345");
        System.out.println("返回结果: " + result);
    }
}
