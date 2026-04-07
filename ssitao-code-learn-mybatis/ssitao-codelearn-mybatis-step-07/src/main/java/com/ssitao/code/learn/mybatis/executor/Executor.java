package com.ssitao.code.learn.mybatis.executor;

import com.ssitao.code.learn.mybatis.mapping.BoundSql;
import com.ssitao.code.learn.mybatis.mapping.MappedStatement;
import com.ssitao.code.learn.mybatis.session.ResultHandler;
import com.ssitao.code.learn.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sizt
 * @description: 执行器
 * @date 2026/1/17 21:02
 */
public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);
}
