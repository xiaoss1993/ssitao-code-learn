package com.ssitao.codelearn.mybatis.test;

import com.alibaba.fastjson.JSON;
import com.ssitao.codelearn.mybatis.binding.MapperRegistry;
import com.ssitao.codelearn.mybatis.io.Resources;
import com.ssitao.codelearn.mybatis.session.SqlSession;
import com.ssitao.codelearn.mybatis.session.SqlSessionFactory;
import com.ssitao.codelearn.mybatis.session.SqlSessionFactoryBuilder;
import com.ssitao.codelearn.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.ssitao.codelearn.mybatis.test.dao.IUserDao;
import com.ssitao.codelearn.mybatis.test.po.User;
import org.junit.Before;
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

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    private SqlSession sqlSession;

    @Before
    public void init() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void test_queryUserInfoById() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：基本参数
        User user = userDao.queryUserInfoById(1L);
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

    @Test
    public void test_queryUserInfo() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 测试验证：对象参数
        User user = userDao.queryUserInfo(new User(1L, "10001"));
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

    @Test
    public void test_insertUser() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 创建新用户
        User user = new User();
        user.setUserId("10009");
        user.setUserName("测试用户");
        user.setUserHead("1A");

        // 3. 执行插入操作
        int count = userDao.insertUser(user);
        logger.info("插入成功，影响行数：{}", count);

        // 4. 通过 userId 查询验证插入结果
        User queryReq = new User();
        queryReq.setUserId("10009");
        User insertedUser = userDao.queryUserInfo(queryReq);
        logger.info("插入后的用户信息：{}", JSON.toJSONString(insertedUser));
    }

    @Test
    public void test_updateUser() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 先查询用户
        User user = userDao.queryUserInfoById(1L);
        logger.info("更新前的用户信息：{}", JSON.toJSONString(user));

        // 3. 更新用户信息
        user.setUserName("更新后的用户名");
        user.setUserHead("2B");

        // 4. 执行更新操作
        int count = userDao.updateUser(user);
        logger.info("更新成功，影响行数：{}", count);

        // 5. 查询验证
        User updatedUser = userDao.queryUserInfoById(1L);
        logger.info("更新后的用户信息：{}", JSON.toJSONString(updatedUser));
    }

    @Test
    public void test_deleteUser() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 2. 先插入一个测试用户
        User user = new User();
        user.setUserId("10010");
        user.setUserName("待删除用户");
        user.setUserHead("3C");
        userDao.insertUser(user);

        // 3. 查询确认用户存在
        User queryReq = new User();
        queryReq.setUserId("10010");
        User userToDelete = userDao.queryUserInfo(queryReq);
        Long userId = userToDelete.getId();
        logger.info("待删除的用户信息：{}", JSON.toJSONString(userToDelete));

        // 4. 执行删除操作
        int count = userDao.deleteUser(userId);
        logger.info("删除成功，影响行数：{}", count);

        // 5. 查询验证（应该返回null）
        User deletedUser = userDao.queryUserInfoById(userId);
        logger.info("删除后的查询结果（应为null）：{}", JSON.toJSONString(deletedUser));
    }

}
