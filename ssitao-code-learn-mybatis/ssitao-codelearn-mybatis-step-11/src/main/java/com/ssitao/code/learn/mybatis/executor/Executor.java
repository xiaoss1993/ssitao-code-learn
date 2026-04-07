package com.ssitao.code.learn.mybatis.executor;

import com.ssitao.code.learn.mybatis.mapping.BoundSql;
import com.ssitao.code.learn.mybatis.mapping.MappedStatement;
import com.ssitao.code.learn.mybatis.session.ResultHandler;
import com.ssitao.code.learn.mybatis.session.RowBounds;
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

    int update(MappedStatement ms, Object parameter) throws SQLException;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
