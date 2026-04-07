package com.ssitao.code.learn.mybatis.session;

import com.ssitao.code.learn.mybatis.builder.xml.XMLConfigBuilder;
import com.ssitao.code.learn.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 * @author sizt
 * @description:
 * 构建 SqlSessionFactory的工厂
 * @date 2026/1/18 11:51
 */
public class SqlSessionFactoryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(SqlSessionFactoryBuilder.class);

    public SqlSessionFactory build(Reader reader){
        XMLConfigBuilder xmlConfiguration = new XMLConfigBuilder(reader);
        return build(xmlConfiguration.parse());
    }

    public SqlSessionFactory build(Configuration config){
        return new DefaultSqlSessionFactory(config);
    }
}
