package com.ssitao.code.learn.mybatis.scripting.defaults;

import com.ssitao.code.learn.mybatis.builder.SqlSourceBuilder;
import com.ssitao.code.learn.mybatis.mapping.BoundSql;
import com.ssitao.code.learn.mybatis.mapping.SqlSource;
import com.ssitao.code.learn.mybatis.scripting.xmltags.DynamicContext;
import com.ssitao.code.learn.mybatis.scripting.xmltags.SqlNode;
import com.ssitao.code.learn.mybatis.session.Configuration;

import java.util.HashMap;

/**
 * @author sizt
 * @description: 原始SQL源码，比 DynamicSqlSource 动态SQL处理快
 * @date 2026/2/11 10:07
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

}
