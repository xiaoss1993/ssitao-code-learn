package com.ssitao.code.learn.mybatis.scripting.xmltags;

import com.ssitao.code.learn.mybatis.executor.parameter.ParameterHandler;
import com.ssitao.code.learn.mybatis.mapping.BoundSql;
import com.ssitao.code.learn.mybatis.mapping.MappedStatement;
import com.ssitao.code.learn.mybatis.mapping.SqlSource;
import com.ssitao.code.learn.mybatis.scripting.LanguageDriver;
import com.ssitao.code.learn.mybatis.scripting.defaults.DefaultParameterHandler;
import com.ssitao.code.learn.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * @author sizt
 * @description: XML语言驱动器
 * @date 2026/2/11 10:01
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用XML脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

}
