package com.ssitao.codelearn.mybatis.test;

import com.ssitao.codelearn.mybatis.binding.MapperProxyFactory;
import com.ssitao.codelearn.mybatis.binding.MapperRegistry;
import com.ssitao.codelearn.mybatis.session.SqlSession;
import com.ssitao.codelearn.mybatis.session.SqlSessionFactory;
import com.ssitao.codelearn.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.ssitao.codelearn.mybatis.test.dao.IUserDao;
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
        // 1、注册Mapper
        MapperRegistry  registry = new MapperRegistry();
        registry.addMappers("com.ssitao.codelearn.mybatis.test.dao");

        // 2、从SqlSession 工厂获取Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3、获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4、测试方法执行
        String  result  = userDao.queryUserNameById("10001");
        logger.info("result:{}",result);
    }

}
