package com.ssitao.codelearn.mybatis.builder;

import com.ssitao.codelearn.mybatis.session.Configuration;
import com.ssitao.codelearn.mybatis.type.TypeAliasRegistry;

/**
 * @author sizt
 * @description:
 * 构造器的基类，建造者模式
 * @date 2026/1/18 11:57
 */
public abstract class BaseBuilder {
    protected Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;


    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
