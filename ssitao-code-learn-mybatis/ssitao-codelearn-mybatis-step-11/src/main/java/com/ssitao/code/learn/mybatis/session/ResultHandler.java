package com.ssitao.code.learn.mybatis.session;

/**
 * @author sizt
 * @description:
 * 结果处理器
 * @date 2026/1/17 21:02
 */
public interface ResultHandler {

    /**
     * 处理结果
     */
    void handleResult(ResultContext context);

}
