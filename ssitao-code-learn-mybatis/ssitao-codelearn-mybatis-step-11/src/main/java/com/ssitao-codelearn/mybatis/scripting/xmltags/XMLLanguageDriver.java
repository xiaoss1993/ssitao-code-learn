package com.ssitao.codelearn.mybatis.scripting.xmltags;

import com.ssitao.codelearn.mybatis.executor.parameter.ParameterHandler;
import com.ssitao.codelearn.mybatis.mapping.BoundSql;
import com.ssitao.codelearn.mybatis.mapping.MappedStatement;
import com.ssitao.codelearn.mybatis.mapping.SqlSource;
import com.ssitao.codelearn.mybatis.scripting.LanguageDriver;
import com.ssitao.codelearn.mybatis.scripting.defaults.DefaultParameterHandler;
import com.ssitao.codelearn.mybatis.session.Configuration;
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
