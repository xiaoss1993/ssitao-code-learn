package com.ssitao.codelearn.mybatis.session;

/**
 * SqlSession 用于执行SQL,获取映射器，管理事务。
 */
public interface SqlSession {
    /**
     * 根据指定的SqlID 获取一条记录的封装对象
     *
     * @param <T>       封装之后的对象类型
     * @param statement sqlID
     * @return Mapperd Object 封装之后的对象
     */
    <T> T selectOne(String statement);

    /**
     * 根据指定的SqlID 获取一条记录的封装对象，允许传入参数
     *
     * @param <T>       the  returned object  type
     * @param statement Unique identifier matching the statement to use
     * @param parameter A parameter object to pass to the statement
     * @return Mapperd Object
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 获取指定接口的代理对象
     *
     * @param type 接口类型
     * @param <T>  接口类型
     * @return 代理对象
     */
    <T> T getMapper(Class<T> type);

}
