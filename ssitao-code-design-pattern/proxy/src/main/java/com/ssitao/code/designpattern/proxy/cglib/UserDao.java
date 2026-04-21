package com.ssitao.code.designpattern.proxy.cglib;

/**
 * CGLIB代理示例 - 不需要接口的类
 */
public class UserDao {

    // 添加用户
    public void insert(String name) {
        System.out.println("插入用户: " + name);
    }

    // 删除用户
    public void delete(String name) {
        System.out.println("删除用户: " + name);
    }

    // 获取用户
    public String select(String id) {
        System.out.println("查询用户: " + id);
        return "User_" + id;
    }
}
