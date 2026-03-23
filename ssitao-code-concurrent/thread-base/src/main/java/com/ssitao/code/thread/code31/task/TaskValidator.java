package com.ssitao.code.thread.code31.task;

import java.util.concurrent.Callable;


/**
 * 任务验证类，作为 Callable 任务提交给线程池执行
 *
 * 【设计说明】
 * TaskValidator 是 UserValidator 的包装器
 * - UserValidator：业务逻辑类，负责实际的验证操作
 * - TaskValidator：适配器类，实现 Callable 接口，使其能被线程池执行
 *
 * 【关键设计】
 * validate() 返回 false 时，call() 会抛出 Exception
 * 这是必须的！因为 invokeAny() 只认"成功完成"的任务
 * - 返回值 = 成功
 * - 抛异常 = 失败
 */
public class TaskValidator implements Callable<String> {
    /**
     * 底层的用户验证器（可以是LDAP、数据库、SSO等）
     */
    private UserValidator validator;
    /**
     * 要验证的用户名
     */
    private String user;
    /**
     * 要验证的密码
     */
    private String password;

    /**
     * 构造函数，初始化验证任务
     *
     * @param validator 用户验证对象
     * @param user      用户名
     * @param password  用户密码
     */
    public TaskValidator(UserValidator validator, String user, String password) {
        this.validator = validator;
        this.user = user;
        this.password = password;
    }

    /**
     * 核心方法：执行验证并返回结果
     *
     * 【返回值设计】
     * - 验证成功：返回 validator.getName()，即验证系统名称（如 "LDAP"）
     * - 验证失败：抛出 Exception
     *
     * 【为什么失败要抛异常？】
     * invokeAny() 判断任务成功的条件是：任务正常返回（未抛异常）
     * 如果 validate() 返回 false 但 call() 返回 null 或空字符串
     * invokeAny() 仍会认为这是一个有效结果
     * 所以必须抛异常来表示"验证失败"
     *
     * @return 验证器的名称（表示验证成功）
     * @throws Exception 验证不通过时抛出异常
     */
    @Override
    public String call() throws Exception {
        // 调用底层验证器进行验证
        if (!validator.validate(user, password)) {
            System.out.printf("%s: The user has not been found\n", validator.getName());
            // 【关键】验证失败时抛出异常，而不是返回false
            // 这样 invokeAny() 才能正确识别这是"失败"的任务
            throw new Exception("Error validating user");
        }

        // 验证成功，返回验证器名称
        System.out.printf("%s: The user has been found\n", validator.getName());
        return validator.getName();
    }
}