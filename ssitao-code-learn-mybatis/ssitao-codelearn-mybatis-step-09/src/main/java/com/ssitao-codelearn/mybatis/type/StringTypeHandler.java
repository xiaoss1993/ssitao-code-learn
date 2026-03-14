package com.ssitao.codelearn.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author sizt
 * @description: String类型处理器
 * @date 2026/2/11 09:50
 */
public class StringTypeHandler extends BaseTypeHandler<String>{

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

}
