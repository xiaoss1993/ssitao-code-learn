package com.ssitao.code.learn.mybatis.builder;

import com.ssitao.code.learn.mybatis.session.Configuration;

/**
 * @author sizt
 * @description:
 * 构造器的基类，建造者模式
 * @date 2026/1/18 11:57
 */
public abstract class BaseBuilder {
    protected Configuration configuration;

    public BaseBuilder(Configuration configuration){
        this.configuration = configuration;
    }
    public Configuration getConfiguration(){
        return configuration;
    }
}
