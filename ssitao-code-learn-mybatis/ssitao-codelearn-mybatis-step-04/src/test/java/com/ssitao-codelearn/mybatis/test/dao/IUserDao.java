package com.ssitao.codelearn.mybatis.test.dao;

import com.ssitao.codelearn.mybatis.test.po.User;

public interface IUserDao {

    User queryUserInfoById(String uId);

}
