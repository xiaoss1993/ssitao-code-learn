package com.ssitao.code.learn.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.ssitao.code.learn.mybatis.binding.MapperRegistry;
import com.ssitao.code.learn.mybatis.io.Resources;
import com.ssitao.code.learn.mybatis.session.SqlSession;
import com.ssitao.code.learn.mybatis.session.SqlSessionFactory;
import com.ssitao.code.learn.mybatis.session.SqlSessionFactoryBuilder;
import com.ssitao.code.learn.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.ssitao.code.learn.mybatis.test.dao.IUserDao;
import com.ssitao.code.learn.mybatis.test.po.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * @author sizt
 * @description: TODO
 * @date 2026/1/17 20:17
 */
public class ApiTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void test_SqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        User user = userDao.queryUserInfoById("1000");
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

}
