package com.ssitao.code.learn.mybatis.transaction.jdbc;

import com.ssitao.code.learn.mybatis.session.TransactionIsolationLevel;
import com.ssitao.code.learn.mybatis.transaction.Transaction;
import com.ssitao.code.learn.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/1/21 20:05
 */
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
