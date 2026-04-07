package com.ssitao.code.learn.mybatis.builder;

import com.ssitao.code.learn.mybatis.session.Configuration;
import com.ssitao.code.learn.mybatis.type.TypeAliasRegistry;
import com.ssitao.code.learn.mybatis.type.TypeHandlerRegistry;

/**
 * @author sizt
 * @description:
 * 构造器的基类，建造者模式
 * @date 2026/1/18 11:57
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;
    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
