package com.ssitao.code.thread.code31.core;


import com.ssitao.code.thread.code31.task.TaskValidator;
import com.ssitao.code.thread.code31.task.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 代码31：使用 invokeAny() 实现多线程验证模式
 *
 * 【核心概念】
 * invokeAny() 方法：提交一组任务给线程池，返回第一个成功完成的任务结果
 * - 只要任意一个任务成功（正常返回），就取消其他正在执行的任务
 * - 如果所有任务都失败（抛异常），则抛出 ExecutionException
 *
 * 【应用场景】
 * 想象你有多个人工智能模型可以验证用户：
 * - 只要第一个说”验证通过”就认为用户合法
 * - 其他模型可以停止工作了
 * 这在需要”快速响应”的场景下很有用
 *
 * 【设计模式】
 * 这种模式叫做”第一个成功者获胜”（First Success Wins）
 */
public class Main {
    public static void main(String[] args) {
        // ========== 1. 准备验证数据 ==========
        String username = "test";
        String password = "test";

        // ========== 2. 创建多个验证器（模拟不同的验证来源） ==========
        // 场景：系统同时支持 LDAP 和数据库两种验证方式
        // 只要任意一个验证通过，用户就认为是合法的
        UserValidator ldapValidator = new UserValidator("LDAP");
        UserValidator dbValidator = new UserValidator("DataBase");

        // ========== 3. 包装成 Callable 任务 ==========
        // TaskValidator 是适配器：
        // - 持有 UserValidator（业务逻辑）
        // - 实现 Callable 接口（可以被线程池执行）
        // - 返回验证器名称作为成功标记
        TaskValidator ldapTask = new TaskValidator(ldapValidator, username, password);
        TaskValidator dbTask = new TaskValidator(dbValidator, username, password);

        // ========== 4. 添加到任务列表 ==========
        // 注意：invokeAny() 接收的是 Collection，不是单个任务
        List<TaskValidator> taskList = new ArrayList<>();
        taskList.add(ldapTask);
        taskList.add(dbTask);

        // ========== 5. 使用缓存线程池执行 ==========
        // newCachedThreadPool() 特点：
        // - 会重用空闲线程
        // - 可以按需创建新线程（最多 Integer.MAX_VALUE）
        ExecutorService executor = Executors.newCachedThreadPool();
        String result;

        try {
            // ========== 6. 关键调用：invokeAny() ==========
            // 执行逻辑：
            // 1. 同时启动所有任务
            // 2. 等待任意一个任务成功完成
            // 3. 立即取消其他任务
            // 4. 返回成功任务的返回值
            //
            // 【为什么用 invokeAny 而不是分别执行？】
            // - invokeAny 是并行的：LDAP和数据库同时验证
            // - 如果LDAP要10秒，数据库要1秒，那么1秒就能返回结果
            // - 比串行验证（先LDAP再数据库）快得多
            result = executor.invokeAny(taskList);
            System.out.printf("Main: Result: %s\n", result);
            // 输出示例：Main: Result: DataBase （取决于哪个先完成）
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // 如果所有任务都失败，会抛出 ExecutionException
            // 异常信息包含所有任务的失败原因
            e.printStackTrace();
        }

        // ========== 7. 关闭线程池 ==========
        executor.shutdown();
        System.out.printf("Main: End of the Execution\n");
    }
}
