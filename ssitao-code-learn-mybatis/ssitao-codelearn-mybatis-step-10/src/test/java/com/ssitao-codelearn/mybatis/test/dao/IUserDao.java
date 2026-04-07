package com.ssitao.code.learn.mybatis.test.dao;

import com.ssitao.code.learn.mybatis.test.po.User;

public interface IUserDao {

    User queryUserInfoById(Long id);

    User queryUserInfo(User req);

}
