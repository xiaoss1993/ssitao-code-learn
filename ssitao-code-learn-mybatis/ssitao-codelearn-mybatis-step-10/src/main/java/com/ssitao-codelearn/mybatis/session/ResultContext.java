package com.ssitao.codelearn.mybatis.session;

/**
 * @author sizt
 * @description: 结果上下文
 * @date 2026/2/12 19:02
 */
public interface ResultContext {

    /**
     * 获取结果
     */
    Object getResultObject();

    /**
     * 获取记录数
     */
    int getResultCount();

}
