package com.ssitao.codelearn.mybatis.test.dao;

import com.ssitao.codelearn.mybatis.test.po.User;

public interface IUserDao {

    User queryUserInfoById(Long id);

    User queryUserInfo(User req);

    int insertUser(User user);

    int updateUser(User user);

    int deleteUser(Long id);

}
