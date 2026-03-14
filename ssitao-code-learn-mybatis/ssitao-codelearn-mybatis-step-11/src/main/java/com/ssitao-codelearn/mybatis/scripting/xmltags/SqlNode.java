package com.ssitao.codelearn.mybatis.scripting.xmltags;

/**
 * SQL 节点
 */
public interface SqlNode {
    boolean apply(DynamicContext context);
}
