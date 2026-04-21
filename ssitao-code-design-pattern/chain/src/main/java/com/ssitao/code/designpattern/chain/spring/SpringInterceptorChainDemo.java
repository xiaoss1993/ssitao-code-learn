package com.ssitao.code.designpattern.chain.spring;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Spring 责任链模式示例
 *
 * Spring中责任链模式的应用：
 * 1. HandlerInterceptorChain - Web请求拦截器链
 * 2. MethodInterceptor - AOP方法拦截器链
 * 3. FilterChain - Servlet过滤器链
 *
 * 本示例演示Spring AOP的拦截器链
 */
public class SpringInterceptorChainDemo {

    public static void main(String[] args) {
        // 使用Spring AOP创建代理
        AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);

        UserService userService = context.getBean(UserService.class);

        System.out.println("=== 测试调用 saveUser ===");
        userService.saveUser("张三");

        System.out.println("\n=== 测试调用 getUser ===");
        userService.getUser(1L);

        System.out.println("\n=== 测试调用 deleteUser ===");
        userService.deleteUser(1L);

        context.close();
    }
}

/**
 * 目标服务类
 */
class UserService {

    public void saveUser(String name) {
        System.out.println("[UserService] 保存用户: " + name);
    }

    public String getUser(Long id) {
        System.out.println("[UserService] 获取用户ID: " + id);
        return "User-" + id;
    }

    public void deleteUser(Long id) {
        System.out.println("[UserService] 删除用户ID: " + id);
    }
}

/**
 * Spring配置类
 */
@Configuration
class AppConfig {

    @Bean
    public UserService userService() {
        // 创建一个简单的代理工厂
        ProxyFactory factory = new ProxyFactory(new UserService());

        // 添加多个拦截器（形成拦截器链）
        factory.addAdvisor(new DefaultPointcutAdvisor(new LoggingInterceptor()));
        factory.addAdvisor(new DefaultPointcutAdvisor(new PerformanceInterceptor()));
        factory.addAdvisor(new DefaultPointcutAdvisor(new AuthInterceptor()));

        return (UserService) factory.getProxy();
    }
}

/**
 * 日志拦截器
 */
class LoggingInterceptor implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        String methodName = method.getName();
        String className = target.getClass().getSimpleName();
        System.out.println("[LoggingInterceptor] >>> 进入方法: " + className + "." + methodName);
        if (args.length > 0) {
            System.out.println("[LoggingInterceptor] 参数: " + Arrays.toString(args));
        }
    }
}

/**
 * 性能监控拦截器
 */
class PerformanceInterceptor implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 将开始时间绑定到线程变量
        PerformanceTracker.setStartTime(startTime);
        System.out.println("[PerformanceInterceptor] 开始计时: " + startTime);
    }
}

/**
 * 权限校验拦截器
 */
class AuthInterceptor implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        String methodName = method.getName();

        // 模拟权限校验 - delete操作需要管理员权限
        if (methodName.startsWith("delete")) {
            System.out.println("[AuthInterceptor] 权限校验: 需要管理员权限");
            // 实际应用中会检查用户权限
        } else {
            System.out.println("[AuthInterceptor] 权限校验: 通过");
        }
    }
}

/**
 * 性能跟踪工具类
 */
class PerformanceTracker {
    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    public static void setStartTime(Long time) {
        START_TIME.set(time);
    }

    public static long getStartTime() {
        return START_TIME.get();
    }

    public static void clear() {
        START_TIME.remove();
    }
}

/**
 * 模拟Spring的HandlerInterceptorChain
 *
 * Spring MVC中，请求会经过一系列拦截器：
 * 1. DispatcherServlet 接收到请求
 * 2. HandlerMapping 找到处理器
 * 3. HandlerExecutionChain 执行拦截器链（preHandle）
 * 4. 调用处理器
 * 5. 执行拦截器链（postHandle）
 * 6. 处理视图
 * 7. 执行拦截器链（afterCompletion）
 */
class HandlerExecutionChainDemo {

    public static void main(String[] args) {
        // 模拟Spring MVC的拦截器链执行
        System.out.println("=== Spring MVC 请求处理流程 ===\n");

        // 创建拦截器链
        HandlerInterceptorChain chain = new HandlerInterceptorChain();
        chain.addInterceptor(new LoggingHandlerInterceptor());
        chain.addInterceptor(new AuthHandlerInterceptor());

        // 模拟请求
        MockHttpRequest request = new MockHttpRequest("/api/users", "DELETE");
        MockHttpResponse response = new MockHttpResponse();

        // 执行 preHandle
        boolean proceed = chain.applyPreHandle(request, response);
        System.out.println("preHandle结果: " + (proceed ? "继续执行" : "中断执行"));

        if (proceed) {
            // 调用实际的处理器
            System.out.println("\n--- 执行控制器方法 ---\n");
            System.out.println("Controller: 处理 DELETE /api/users 请求\n");

            // 执行 postHandle
            chain.applyPostHandle(request, response);
        }

        // 执行 afterCompletion（无论成功与否都会执行）
        chain.triggerAfterCompletion(request, response, null);
    }
}

/**
 * 模拟HTTP请求
 */
class MockHttpRequest {
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
 * 模拟HTTP响应
 */
class MockHttpResponse {
    private int status = 200;

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}

/**
 * Spring风格的处理器拦截器接口
 */
interface HandlerInterceptor {
    default boolean preHandle(MockHttpRequest request, MockHttpResponse response) {
        return true;
    }

    default void postHandle(MockHttpRequest request, MockHttpResponse response) {
    }

    default void afterCompletion(MockHttpRequest request, MockHttpResponse response, Exception ex) {
    }
}

/**
 * 拦截器链
 */
class HandlerInterceptorChain {
    private final java.util.List<HandlerInterceptor> interceptors = new java.util.ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public boolean applyPreHandle(MockHttpRequest request, MockHttpResponse response) {
        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandle(request, response)) {
                triggerAfterCompletion(request, response, null);
                return false;
            }
        }
        return true;
    }

    public void applyPostHandle(MockHttpRequest request, MockHttpResponse response) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).postHandle(request, response);
        }
    }

    public void triggerAfterCompletion(MockHttpRequest request, MockHttpResponse response, Exception ex) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            try {
                interceptors.get(i).afterCompletion(request, response, ex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 日志处理器拦截器
 */
class LoggingHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(MockHttpRequest request, MockHttpResponse response) {
        System.out.println("[LoggingInterceptor] 请求: " + request.getMethod() + " " + request.getUri());
        return true;
    }

    @Override
    public void postHandle(MockHttpRequest request, MockHttpResponse response) {
        System.out.println("[LoggingInterceptor] 响应状态: " + response.getStatus());
    }

    @Override
    public void afterCompletion(MockHttpRequest request, MockHttpResponse response, Exception ex) {
        System.out.println("[LoggingInterceptor] 请求完成");
    }
}

/**
 * 权限处理器拦截器
 */
class AuthHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(MockHttpRequest request, MockHttpResponse response) {
        System.out.println("[AuthInterceptor] 检查用户权限...");

        // 模拟权限检查
        if ("DELETE".equals(request.getMethod())) {
            System.out.println("[AuthInterceptor] DELETE操作需要管理员权限");
            // 实际会检查token/session
        }

        System.out.println("[AuthInterceptor] 权限检查通过");
        return true;
    }

    @Override
    public void afterCompletion(MockHttpRequest request, MockHttpResponse response, Exception ex) {
        if (ex != null) {
            System.out.println("[AuthInterceptor] 请求异常: " + ex.getMessage());
        }
    }
}
