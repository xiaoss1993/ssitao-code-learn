package com.ssitao.codelearn.mybatis.executor;

import com.ssitao.codelearn.mybatis.executor.statement.StatementHandler;
import com.ssitao.codelearn.mybatis.mapping.BoundSql;
import com.ssitao.codelearn.mybatis.mapping.MappedStatement;
import com.ssitao.codelearn.mybatis.session.Configuration;
import com.ssitao.codelearn.mybatis.session.ResultHandler;
import com.ssitao.codelearn.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author sizt
 * @description:
 * 简单执行器
 * @date 2026/2/6 17:35
 */
public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, resultHandler, boundSql);
            Connection connection = transaction.getConnection();
            Statement stmt = handler.prepare(connection);
            handler.parameterize(stmt);
            return handler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
