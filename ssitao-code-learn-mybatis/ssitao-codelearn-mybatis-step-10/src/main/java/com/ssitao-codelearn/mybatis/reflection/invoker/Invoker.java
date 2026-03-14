package com.ssitao.codelearn.mybatis.reflection.invoker;

/**
 * 调用者
 * 关于对象类中的属性值获取和设置可以分为 Field 字段的 get/set 还有普通的 Method 的调用，
 * 为了减少使用方的过多的处理，这里可以把集中调用者的实现包装成调用策略，统一接口不同策略不同的实现类。
 */
public interface Invoker {
    Object invoke(Object target, Object[] args) throws Throwable;

    Class<?> getType();
}
