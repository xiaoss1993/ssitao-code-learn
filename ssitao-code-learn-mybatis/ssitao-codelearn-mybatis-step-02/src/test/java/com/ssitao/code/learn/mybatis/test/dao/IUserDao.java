package com.ssitao.code.learn.mybatis.test.dao;

public interface IUserDao {
    String queryUserNameById(String userId);
    Integer queryUserAgeById(String userId);
}
