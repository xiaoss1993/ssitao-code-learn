package com.ssitao.codelearn.mybatis.scripting;

import com.ssitao.codelearn.mybatis.executor.parameter.ParameterHandler;
import com.ssitao.codelearn.mybatis.mapping.BoundSql;
import com.ssitao.codelearn.mybatis.mapping.MappedStatement;
import com.ssitao.codelearn.mybatis.mapping.SqlSource;
import com.ssitao.codelearn.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * 脚本语言驱动
 */
public interface LanguageDriver {

    /**
     * 创建SQL源码(mapper xml方式)
     */
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

    /**
     * 创建参数处理器
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

}
