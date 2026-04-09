package com.learn.crazyjava.lesson08_reflection;

import java.lang.reflect.*;

/**
 * 第8课：反射 - 动态代理
 */
public class DynamicProxyDemo {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        UserService proxy = (UserService) Proxy.newProxyInstance(
            userService.getClass().getClassLoader(),
            userService.getClass().getInterfaces(),
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("方法 " + method.getName() + " 开始执行");
                    Object result = method.invoke(userService, args);
                    System.out.println("方法 " + method.getName() + " 执行结束");
                    return result;
                }
            }
        );

        proxy.addUser("张三");
        proxy.deleteUser(1);
    }
}

interface UserService {
    void addUser(String name);
    void deleteUser(int id);
}

class UserServiceImpl implements UserService {
    @Override
    public void addUser(String name) {
        System.out.println("添加用户：" + name);
    }

    @Override
    public void deleteUser(int id) {
        System.out.println("删除用户：" + id);
    }
}
