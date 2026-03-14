package com.ssitao.codelearn.mybatis.executor;

import com.ssitao.codelearn.mybatis.mapping.BoundSql;
import com.ssitao.codelearn.mybatis.mapping.MappedStatement;
import com.ssitao.codelearn.mybatis.session.ResultHandler;
import com.ssitao.codelearn.mybatis.session.RowBounds;
import com.ssitao.codelearn.mybatis.transaction.Transaction;

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
