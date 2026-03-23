package com.ssitao.code.thread.code31.task;


import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 用户检验类，实现用户验证过程
 *
 * 【设计模式】
 * 这个类模拟一个外部验证系统（如LDAP、数据库等）
 * 它是"不可靠"的：随机返回成功/失败，模拟真实世界中验证可能失败的场景
 */
public class UserValidator {
    /**
     * 验证系统名称，如 "LDAP"、"DataBase"、"OAuth" 等
     * 用于标识是哪个验证系统返回了结果
     */
    private String name;

    /**
     * 构造函数，初始化验证系统名称
     *
     * @param name 验证系统名称
     */
    public UserValidator(String name) {
        this.name = name;
    }

    /**
     * 验证方法，根据用户名和密码进行验证
     *
     * 【模拟说明】
     * 真实场景中，这里会调用实际的验证服务：
     * - LDAP：查询目录服务
     * - 数据库：查询用户表
     * - OAuth：调用第三方认证
     *
     * @param name     用户名
     * @param password 密码
     * @return true验证通过，false验证失败
     */
    public boolean validate(String name, String password) {
        Random random = new Random();

        // 模拟验证过程的延迟（0-10秒随机）
        // 真实场景中这是网络请求或数据库查询的耗时
        try {
            Long duration = (long) (Math.random() * 10);
            System.out.printf("Validator %s: Validating a user during %d seconds\n",
                this.name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            // 如果线程被中断，说明其他验证器已经成功了
            // 当前验证器可以放弃
            return false;
        }

        // 【模拟】随机返回验证结果
        // 真实场景中，这里会根据实际验证逻辑返回确定的结果
        // random.nextBoolean() 50%概率返回true，50%返回false
        return random.nextBoolean();
    }

    /**
     * 返回验证系统名称
     *
     * @return name属性值
     */
    public String getName() {
        return name;
    }
}