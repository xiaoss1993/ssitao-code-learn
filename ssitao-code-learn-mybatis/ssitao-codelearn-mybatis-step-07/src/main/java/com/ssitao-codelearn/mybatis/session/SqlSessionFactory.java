package com.ssitao.codelearn.mybatis.session;

/**
 * 工厂模式接口，构建SqlSession的工厂
 */
public interface SqlSessionFactory {
    /**
     * 打开一个session
     *
     * @return
     */
    SqlSession openSession();
}
