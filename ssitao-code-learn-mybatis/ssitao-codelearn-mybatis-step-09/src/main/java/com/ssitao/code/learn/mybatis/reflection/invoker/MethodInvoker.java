package com.ssitao.code.learn.mybatis.reflection.invoker;

import java.lang.reflect.Method;

/**
 * @author sizt
 * @description: 方法调用者
 * @date 2026/2/10 19:56
 */
public class MethodInvoker implements Invoker {

    private Class<?> type;
    private Method method;

    public MethodInvoker(Method method) {
        this.method = method;

        if (method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        } else {
            this.type = method.getReturnType();
        }

    }

    @Override
    public Object invoke(Object target, Object[] args) throws Throwable {
        return method.invoke(target, args);
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
