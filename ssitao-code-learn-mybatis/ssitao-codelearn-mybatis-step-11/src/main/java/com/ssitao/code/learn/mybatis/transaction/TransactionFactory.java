package com.ssitao.code.learn.mybatis.transaction;

import com.ssitao.code.learn.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author sizt
 * @description:
 * 事务工厂
 * @date 2026/1/21 19:58
 */
public interface TransactionFactory {
    /**
     * 根据 Connection 创建 Transaction
     * @param connection
     * @return
     */
    Transaction newTransaction(Connection connection);

    /**
     * 根据数据源和事务隔离级别创建 Transaction
     * @param dataSource DataSource to take the connection from
     * @param level Desired isolation level
     * @param autoCommit Desired autocommit
     * @return Transaction
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level,boolean autoCommit);
}
