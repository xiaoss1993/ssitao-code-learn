package com.ssitao.codelearn.mybatis.session.defaults;

import com.ssitao.codelearn.mybatis.binding.MapperRegistry;
import com.ssitao.codelearn.mybatis.session.SqlSession;

/**
 * @author sizt
 * @description: 默认的SqlSession实现
 * @date 2026/1/17 21:02
 */
public class DefaultSqlSession implements SqlSession {
    /**
     * 映射器注册机
     */
    private MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了!" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你被代理了! + 方法：" + statement + " 参数：" + parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }
}
