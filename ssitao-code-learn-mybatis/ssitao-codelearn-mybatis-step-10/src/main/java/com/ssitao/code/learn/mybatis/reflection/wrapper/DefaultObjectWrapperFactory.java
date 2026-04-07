package com.ssitao.code.learn.mybatis.reflection.wrapper;

import com.ssitao.code.learn.mybatis.reflection.MetaObject;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/2/10 21:28
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }

}
