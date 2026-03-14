package com.ssitao.code.designpattern.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * CGLIB方法拦截器
 *
 * 特点：
 * 1. 实现MethodInterceptor接口
 * 2. 不需要接口
 * 3. 通过继承目标类创建代理
 */
public class LogMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // 方法执行前
        String methodName = method.getName();
        System.out.println("[CGLIB代理] 开始执行: " + methodName + ", 参数: " + Arrays.toString(args));

        long startTime = System.currentTimeMillis();

        // 调用父类的方法（最终调用目标方法）
        Object result = proxy.invokeSuper(obj, args);

        // 方法执行后
        long endTime = System.currentTimeMillis();
        System.out.println("[CGLIB代理] 执行完成: " + methodName + ", 耗时: " + (endTime - startTime) + "ms");

        return result;
    }
}
