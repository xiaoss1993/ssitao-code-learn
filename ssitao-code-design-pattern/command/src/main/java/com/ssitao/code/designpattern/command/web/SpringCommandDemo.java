package com.ssitao.code.designpattern.command.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Spring框架中的命令模式应用示例
 *
 * Spring中命令模式的使用：
 * 1. Spring MVC - @Controller参数绑定
 * 2. Spring Shell - 命令行应用
 * 3. Spring Batch - 任务命令
 * 4. Spring Integration - 消息网关
 * 5. Spring Security - SecurityCommandMetadata
 * 6. JdbcTemplate - 命令模式封装
 */
public class SpringCommandDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring命令模式应用 ===\n");

        // 1. 模拟Spring MVC命令对象
        System.out.println("1. Spring MVC命令对象");
        springMvcDemo();

        // 2. 模拟Spring Shell命令
        System.out.println("\n2. Spring Shell命令");
        springShellDemo();

        // 3. 模拟Spring Batch任务
        System.out.println("\n3. Spring Batch任务命令");
        springBatchDemo();

        // 4. 模拟JdbcTemplate命令封装
        System.out.println("\n4. JdbcTemplate命令封装");
        jdbcTemplateDemo();

        // 5. 模拟Spring Security命令
        System.out.println("\n5. Spring Security命令");
        springSecurityDemo();
    }

    // ============================================
    // 1. Spring MVC命令对象
    // ============================================

    private static void springMvcDemo() {
        // 模拟用户提交的表单（命令对象）
        UserCommand userCmd = new UserCommand();
        userCmd.setName("张三");
        userCmd.setEmail("zhangsan@example.com");
        userCmd.setAge(25);

        // 模拟Controller处理命令对象
        UserControllerV2 controller = new UserControllerV2();
        controller.saveUser(userCmd);
    }

    /**
     * 命令对象 - 用户命令
     */
    static class UserCommand {
        private String name;
        private String email;
        private Integer age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }

    /**
     * Controller - 处理命令对象
     */
    static class UserControllerV2 {
        private UserServiceV2 userService = new UserServiceV2();

        public String saveUser(UserCommand command) {
            // 命令对象被传递给Service
            userService.create(command);
            return "redirect:/users";
        }
    }

    /**
     * Service
     */
    static class UserServiceV2 {
        public void create(UserCommand command) {
            System.out.println("创建用户: " + command.getName() +
                ", Email: " + command.getEmail() +
                ", Age: " + command.getAge());
        }
    }

    // ============================================
    // 2. Spring Shell命令
    // ============================================

    private static void springShellDemo() {
        // 模拟Shell命令注册表
        CommandRegistry registry = new CommandRegistry();

        // 注册命令
        registry.register(new HelloCommand());
        registry.register(new GreetCommand());
        registry.register(new ExitCommand());

        // 执行命令
        System.out.println("--- 执行 hello 命令 ---");
        registry.execute("hello", "World");

        System.out.println("\n--- 执行 greet 命令 ---");
        registry.execute("greet", "张三");

        System.out.println("\n--- 执行 exit 命令 ---");
        registry.execute("exit", null);
    }

    /**
     * Shell命令接口
     */
    interface ShellCommand {
        String getName();
        String execute(String args);
    }

    /**
     * Shell命令基类
     */
    static abstract class AbstractShellCommand implements ShellCommand {
        protected String description;

        public String getDescription() {
            return description;
        }
    }

    /**
     * Hello命令
     */
    static class HelloCommand extends AbstractShellCommand {
        public HelloCommand() {
            this.description = "打印Hello";
        }

        @Override
        public String getName() {
            return "hello";
        }

        @Override
        public String execute(String args) {
            if (args != null && !args.isEmpty()) {
                return "Hello, " + args + "!";
            }
            return "Hello!";
        }
    }

    /**
     * Greet命令
     */
    static class GreetCommand extends AbstractShellCommand {
        public GreetCommand() {
            this.description = "礼貌问候";
        }

        @Override
        public String getName() {
            return "greet";
        }

        @Override
        public String execute(String args) {
            return "您好，" + (args != null ? args : "访客") + "！";
        }
    }

    /**
     * Exit命令
     */
    static class ExitCommand extends AbstractShellCommand {
        public ExitCommand() {
            this.description = "退出程序";
        }

        @Override
        public String getName() {
            return "exit";
        }

        @Override
        public String execute(String args) {
            return "程序退出";
        }
    }

    /**
     * 命令注册表
     */
    static class CommandRegistry {
        private Map<String, ShellCommand> commands = new HashMap<>();

        public void register(ShellCommand command) {
            commands.put(command.getName(), command);
        }

        public String execute(String name, String args) {
            ShellCommand command = commands.get(name);
            if (command != null) {
                String result = command.execute(args);
                System.out.println("输出: " + result);
                return result;
            }
            System.out.println("未知命令: " + name);
            return null;
        }
    }

    // ============================================
    // 3. Spring Batch任务命令
    // ============================================

    private static void springBatchDemo() {
        // 创建JobLauncher
        JobLauncher launcher = new JobLauncher();

        // 创建任务
        ImportUserJob job = new ImportUserJob();

        // 执行任务
        launcher.run(job);
    }

    /**
     * Batch Job接口（命令）
     */
    interface BatchJob {
        void execute();
        String getName();
    }

    /**
     * Job启动器（调用者）
     */
    static class JobLauncher {
        public void run(BatchJob job) {
            System.out.println("开始执行Job: " + job.getName());
            job.execute();
            System.out.println("Job执行完成: " + job.getName());
        }
    }

    /**
     * 导入用户任务
     */
    static class ImportUserJob implements BatchJob {
        @Override
        public String getName() {
            return "importUserJob";
        }

        @Override
        public void execute() {
            // 读取数据
            System.out.println("[Step1] 读取CSV文件");
            // 处理数据
            System.out.println("[Step2] 处理数据");
            // 写入数据库
            System.out.println("[Step3] 写入数据库");
        }
    }

    // ============================================
    // 4. JdbcTemplate命令封装
    // ============================================

    private static void jdbcTemplateDemo() {
        // 模拟JdbcTemplate
        JdbcTemplate template = new JdbcTemplate();

        // 执行查询命令
        System.out.println("--- 执行查询 ---");
        String querySql = "SELECT * FROM users WHERE id = ?";
        template.query(querySql, new Object[]{1});

        // 执行更新命令
        System.out.println("\n--- 执行更新 ---");
        String updateSql = "UPDATE users SET name = ? WHERE id = ?";
        template.update(updateSql, new Object[]{"新名字", 1});

        // 执行插入命令
        System.out.println("\n--- 执行插入 ---");
        String insertSql = "INSERT INTO users(name, email) VALUES(?, ?)";
        template.update(insertSql, new Object[]{"张三", "zhangsan@example.com"});
    }

    /**
     * 模拟JdbcTemplate - 命令模式封装
     */
    static class JdbcTemplate {

        public void query(String sql, Object[] args) {
            System.out.println("执行查询: " + sql);
            System.out.println("参数: " + java.util.Arrays.toString(args));
        }

        public void update(String sql, Object[] args) {
            System.out.println("执行更新: " + sql);
            System.out.println("参数: " + java.util.Arrays.toString(args));
        }
    }

    // ============================================
    // 5. Spring Security命令
    // ============================================

    private static void springSecurityDemo() {
        // 模拟SecurityFilterChain
        SecurityFilterChain chain = new SecurityFilterChain();

        // 添加过滤器
        chain.addFilter(new UsernamePasswordAuthenticationFilter());
        chain.addFilter(new JwtAuthenticationFilter());
        chain.addFilter(new AuthorizationFilter());

        // 处理请求
        System.out.println("--- 处理登录请求 ---");
        SecurityContext context = new SecurityContext();
        chain.doFilter(new MockHttpRequest("/login", "POST"), context);

        System.out.println("\n--- 处理API请求 ---");
        chain.doFilter(new MockHttpRequest("/api/users", "GET"), context);
    }

    /**
     * 安全请求
     */
    static class MockHttpRequest {
        private String uri;
        private String method;

        public MockHttpRequest(String uri, String method) {
            this.uri = uri;
            this.method = method;
        }

        public String getUri() { return uri; }
        public String getMethod() { return method; }
    }

    /**
     * 安全上下文
     */
    static class SecurityContext {
        private Object authentication;

        public Object getAuthentication() { return authentication; }
        public void setAuthentication(Object auth) { this.authentication = auth; }
    }

    /**
     * 安全过滤器接口（命令）
     */
    interface SecurityFilter {
        void doFilter(MockHttpRequest request, SecurityContext context);
    }

    /**
     * 用户名密码认证过滤器
     */
    static class UsernamePasswordAuthenticationFilter implements SecurityFilter {
        @Override
        public void doFilter(MockHttpRequest request, SecurityContext context) {
            if ("/login".equals(request.getUri()) && "POST".equals(request.getMethod())) {
                System.out.println("[UsernamePasswordAuthenticationFilter] 执行用户名密码认证");
                context.setAuthentication("USER_AUTH");
            }
        }
    }

    /**
     * JWT认证过滤器
     */
    static class JwtAuthenticationFilter implements SecurityFilter {
        @Override
        public void doFilter(MockHttpRequest request, SecurityContext context) {
            if (request.getUri().startsWith("/api/")) {
                System.out.println("[JwtAuthenticationFilter] 执行JWT认证");
                context.setAuthentication("JWT_AUTH");
            }
        }
    }

    /**
     * 授权过滤器
     */
    static class AuthorizationFilter implements SecurityFilter {
        @Override
        public void doFilter(MockHttpRequest request, SecurityContext context) {
            if (request.getUri().startsWith("/api/")) {
                System.out.println("[AuthorizationFilter] 执行授权检查");
            }
        }
    }

    /**
     * 过滤器链
     */
    static class SecurityFilterChain {
        private List<SecurityFilter> filters = new ArrayList<>();

        public void addFilter(SecurityFilter filter) {
            filters.add(filter);
        }

        public void doFilter(MockHttpRequest request, SecurityContext context) {
            for (SecurityFilter filter : filters) {
                filter.doFilter(request, context);
            }
            System.out.println("请求处理完成: " + request.getUri());
        }
    }
}
