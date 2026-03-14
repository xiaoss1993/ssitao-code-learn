package com.ssitao.code.designpattern.proxy.jdk;

/**
 * JDK动态代理示例 - 真实业务实现
 */
public class UserServiceImpl implements UserService {

    @Override
    public void addUser(String name) {
        System.out.println("添加用户: " + name);
    }

    @Override
    public void deleteUser(String name) {
        System.out.println("删除用户: " + name);
    }

    @Override
    public String getUserName(String id) {
        return "User_" + id;
    }
}
