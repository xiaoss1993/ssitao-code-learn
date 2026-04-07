package com.ssitao.code.learn.mybatis.executor.statement;

import com.ssitao.code.learn.mybatis.executor.Executor;
import com.ssitao.code.learn.mybatis.mapping.BoundSql;
import com.ssitao.code.learn.mybatis.mapping.MappedStatement;
import com.ssitao.code.learn.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/2/6 17:50
 */
public class PreparedStatementHandler extends BaseStatementHandler{

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultHandler, boundSql);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        return super.prepare(connection);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        if (parameterObject != null) {
            Object param = parameterObject;
            if (parameterObject instanceof Object[]) {
                Object[] params = (Object[]) parameterObject;
                if (params.length > 0 && params[0] != null) {
                    param = params[0];
                } else {
                    param = null;
                }
            }
            if (param != null) {
                ps.setLong(1, Long.parseLong(param.toString()));
            }
        }
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.<E> handleResultSets(ps);
    }

}
