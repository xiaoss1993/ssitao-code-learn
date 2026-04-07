package com.ssitao.code.learn.mybatis.executor.statement;

import com.ssitao.code.learn.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author sizt
 * @description: 语句处理器
 * @date 2026/1/17 21:02
 */
public interface StatementHandler {

    Statement   prepare(Connection connection) throws SQLException;

    void    parameterize(Statement statement) throws SQLException;

    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;
}
