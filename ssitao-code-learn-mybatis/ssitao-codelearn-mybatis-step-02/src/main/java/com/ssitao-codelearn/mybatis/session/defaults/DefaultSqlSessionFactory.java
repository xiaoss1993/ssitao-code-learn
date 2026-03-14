package com.ssitao.codelearn.mybatis.session.defaults;

import com.ssitao.codelearn.mybatis.binding.MapperRegistry;
import com.ssitao.codelearn.mybatis.session.SqlSession;
import com.ssitao.codelearn.mybatis.session.SqlSessionFactory;

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
