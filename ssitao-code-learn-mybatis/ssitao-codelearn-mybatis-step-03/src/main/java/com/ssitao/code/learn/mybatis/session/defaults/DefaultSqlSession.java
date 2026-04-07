package com.ssitao.code.learn.mybatis.session.defaults;

import com.ssitao.code.learn.mybatis.binding.MapperRegistry;
import com.ssitao.code.learn.mybatis.mapping.MappedStatement;
import com.ssitao.code.learn.mybatis.session.Configuration;
import com.ssitao.code.learn.mybatis.session.SqlSession;

/**
 * @author sizt
 * @description:
 * 这里并不直接去依赖MapperRegistry，而是依赖通过依赖Configuration间接去依赖MapperRegistry
 * @date 2026/1/17 21:02
 */
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你被代理了！" + "\n方法：" + statement + "\n入参：" + parameter + "\n待执行SQL：" + mappedStatement.getSql());
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}
