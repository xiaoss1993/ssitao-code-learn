package com.ssitao.codelearn.mybatis.test.dao;

public interface IUserDao {
    String queryUserNameById(String userId);
    Integer queryUserAgeById(String userId);
}
