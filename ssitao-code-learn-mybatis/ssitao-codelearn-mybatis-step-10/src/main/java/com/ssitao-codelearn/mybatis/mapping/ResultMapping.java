package com.ssitao.codelearn.mybatis.mapping;

import com.ssitao.codelearn.mybatis.session.Configuration;
import com.ssitao.codelearn.mybatis.type.JdbcType;
import com.ssitao.codelearn.mybatis.type.TypeHandler;

/**
 * @author sizt
 * @description: 结果映射
 * @date 2026/2/12 18:50
 */
public class ResultMapping {

    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;

    ResultMapping() {
    }

    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();


    }

}
