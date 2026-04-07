package com.ssitao.code.learn.mybatis.session.defaults;

import com.ssitao.code.learn.mybatis.executor.Executor;
import com.ssitao.code.learn.mybatis.mapping.Environment;
import com.ssitao.code.learn.mybatis.session.Configuration;
import com.ssitao.code.learn.mybatis.session.SqlSession;
import com.ssitao.code.learn.mybatis.session.SqlSessionFactory;
import com.ssitao.code.learn.mybatis.session.TransactionIsolationLevel;
import com.ssitao.code.learn.mybatis.transaction.Transaction;
import com.ssitao.code.learn.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * @author sizt
 * @description:
 * 默认的DefaultSqlSessionFactory实现
 * @date 2026/1/17 21:02
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            // 创建DefaultSqlSession
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }

}
