package com.ssitao.codelearn.mybatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * 对象工厂接口
 */
public interface ObjectFactory {

    /**
     * 设置属性
     * @param properties
     */
    void setProperties(Properties properties);

    /**
     * 生产对象
     * @param type
     * @return
     * @param <T>
     */
    <T> T create(Class<T> type);

    /**
     * 生产对象，使用指定的构造函数和构造函数参数
     * @param type
     * @param constructorArgTypes
     * @param constructorArgs
     * @return
     * @param <T>
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * 返回这个对象是否是集合，为了支持 Scala collections
     * @param type
     * @return
     * @param <T>
     */
    <T> boolean isCollection(Class<T> type);

}
