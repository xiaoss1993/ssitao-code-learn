package com.ssitao.codelearn.mybatis.builder;

import com.ssitao.codelearn.mybatis.mapping.BoundSql;
import com.ssitao.codelearn.mybatis.mapping.ParameterMapping;
import com.ssitao.codelearn.mybatis.mapping.SqlSource;
import com.ssitao.codelearn.mybatis.session.Configuration;

import java.util.List;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/2/11 10:04
 */
public class StaticSqlSource implements SqlSource {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }

}
