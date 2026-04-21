package com.ssitao.code.designpattern.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * JDK动态代理 - 调用处理器
 *
 * 特点：
 * 1. 必须实现InvocationHandler接口
 * 2. 通过反射调用目标方法
 * 3. 可以在方法前后添加额外逻辑
 */
public class PerformanceInvocationHandler implements InvocationHandler {

    // 目标对象
    private final Object target;

    public PerformanceInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 方法执行前
        long startTime = System.currentTimeMillis();
        String methodName = method.getName();
        System.out.println("[JDK代理] 开始执行: " + methodName + ", 参数: " + Arrays.toString(args));

        // 调用目标方法
        Object result = method.invoke(target, args);

        // 方法执行后
        long endTime = System.currentTimeMillis();
        System.out.println("[JDK代理] 执行完成: " + methodName + ", 耗时: " + (endTime - startTime) + "ms");

        return result;
    }
}
