package com.ssitao.code.learn.mybatis.datasource.unpooled;

import com.ssitao.code.learn.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/1/24 22:56
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties  props;
    @Override
    public void setProperties(Properties properties) {
        this.props = properties;
    }

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(props.getProperty("driver"));
        unpooledDataSource.setUrl(props.getProperty("url"));
        unpooledDataSource.setUsername(props.getProperty("username"));
        unpooledDataSource.setPassword(props.getProperty("password"));
        return unpooledDataSource;
    }
}
