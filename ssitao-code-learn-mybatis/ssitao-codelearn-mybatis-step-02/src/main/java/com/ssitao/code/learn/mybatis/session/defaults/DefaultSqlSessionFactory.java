package com.ssitao.code.learn.mybatis.session.defaults;

import com.ssitao.code.learn.mybatis.binding.MapperRegistry;
import com.ssitao.code.learn.mybatis.session.SqlSession;
import com.ssitao.code.learn.mybatis.session.SqlSessionFactory;

/**
 * @author sizt
 * @description:
 * 默认的DefaultSqlSessionFactory实现
 * @date 2026/1/17 21:02
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry   mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
