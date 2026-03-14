package com.ssitao.code.designpattern.proxy.jdk;

/**
 * JDK动态代理示例 - 业务接口
 */
public interface UserService {

    // 添加用户
    void addUser(String name);

    // 删除用户
    void deleteUser(String name);

    // 获取用户名
    String getUserName(String id);
}
