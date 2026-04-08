package com.ssitao.code.learn.mybatis.test;

import com.ssitao.code.learn.mybatis.binding.MapperProxyFactory;
import com.ssitao.code.learn.mybatis.test.dao.IUserDao;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/1/17 20:17
 */
public class ApiTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void test_MapperProxyFactory(){
        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);

        Map<String,String>  sqlSession = new HashMap<>();
        sqlSession.put("com.ssitao.code.learn.mybatis.test.dao.IUserDao.queryUserNameById","模拟执行 Mapper.xml 中的SQL操作，查询用户姓名");
        sqlSession.put("com.ssitao.code.learn.mybatis.test.dao.IUserDao.queryUserAgeById","模拟执行 Mapper.xml 中的SQL操作，查询用户年龄");
        IUserDao userDao = factory.newInstance(sqlSession);
        String userName  = userDao.queryUserNameById("10001");
        logger.info("测试结果: userName:{}",userName);
    }

    @Test
    public void test_proxy_class(){
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class},(proxy,method,args) ->"你被代理了");

        String result = userDao.queryUserNameById("10001");

        logger.info("测试结果: result:{}",result);
    }
}
