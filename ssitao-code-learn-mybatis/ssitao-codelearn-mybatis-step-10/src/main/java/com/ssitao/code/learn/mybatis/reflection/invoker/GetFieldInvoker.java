package com.ssitao.code.learn.mybatis.reflection.invoker;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

/**
 * @author sizt
 * @description: getter 调用者
 * getter 方法的调用者处理，因为get是有返回值的，所以直接对 Field 字段操作完后直接返回结果
 * @date 2026/2/10 19:56
 */
public class GetFieldInvoker implements Invoker{

    private Field field;

    public GetFieldInvoker(Field field){
        this.field = field;
    }
    @Override
    public Object invoke(Object target, Object[] args) throws Throwable {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
