package com.ssitao.code.designpattern.facade.spring;

/**
 * Spring框架中的外观模式示例
 *
 * Spring中使用外观模式的场景：
 * 1. ConfigurationClassParser - 统一处理配置类
 * 2. WebMvcConfigurer - 统一配置MVC
 * 3. TransactionSynchronizationManager - 统一事务管理
 * 4. ApplicationContext - 统一Bean管理
 */
public class SpringFacadeDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Facade Pattern Demo ===\n");

        // 1. ApplicationContext - 统一Bean管理
        demonstrateApplicationContext();

        // 2. WebMvcConfigurer - 统一MVC配置
        demonstrateWebMvcConfigurer();

        // 3. TransactionSynchronizationManager - 统一事务管理
        demonstrateTransactionManager();
    }

    /**
     * 1. ApplicationContext - Spring核心容器
     * 外观模式：统一管理所有Bean的创建和获取
     */
    private static void demonstrateApplicationContext() {
        System.out.println("--- 1. ApplicationContext (统一Bean管理) ---");

        // ApplicationContext隐藏了：
        // 1. Bean的创建和生命周期管理
        // 2. 依赖注入
        // 3. 各种BeanFactory实现
        // 4. 资源加载

        System.out.println("ApplicationContext是Spring容器的核心接口：");
        System.out.println("  - 统一管理所有Bean");
        System.out.println("  - 提供依赖注入");
        System.out.println("  - 处理资源加载");
        System.out.println("  - 支持国际化和事件机制");

        // 常见的ApplicationContext实现
        // - ClassPathXmlApplicationContext: 从类路径加载XML
        // - FileSystemXmlApplicationContext: 从文件系统加载XML
        // - AnnotationConfigApplicationContext: 注解配置
        // - WebApplicationContext: Web应用上下文

        System.out.println();
    }

    /**
     * 2. WebMvcConfigurer - 统一MVC配置
     * WebMvcConfigurer提供统一的MVC配置接口
     */
    private static void demonstrateWebMvcConfigurer() {
        System.out.println("--- 2. WebMvcConfigurer (统一MVC配置) ---");

        // WebMvcConfigurer是外观模式
        // 统一了各种MVC配置：
        // - 视图解析器
        // - 静态资源处理
        // - 拦截器配置
        // - 消息转换器
        // - CORS配置

        System.out.println("WebMvcConfigurer的配置项：");
        System.out.println("  - configureViewResolvers() - 视图解析器");
        System.out.println("  - addInterceptors() - 拦截器");
        System.out.println("  - addResourceHandlers() - 静态资源");
        System.out.println("  - configureMessageConverters() - 消息转换");
        System.out.println("  - addCorsMappings() - CORS跨域");

        // 示例代码结构
        System.out.println("\n示例：");
        System.out.println("@Configuration");
        System.out.println("public class WebConfig implements WebMvcConfigurer {");
        System.out.println("    @Override");
        System.out.println("    public void addCorsMappings(CorsRegistry registry) {");
        System.out.println("        registry.addMapping(\"/api/**\")");
        System.out.println("            .allowedOrigins(\"*\");");
        System.out.println("    }");
        System.out.println("}");

        System.out.println();
    }

    /**
     * 3. TransactionSynchronizationManager - 事务同步管理
     * 外观模式：统一管理线程绑定的事务资源
     */
    private static void demonstrateTransactionManager() {
        System.out.println("--- 3. TransactionSynchronizationManager (事务管理) ---");

        // TransactionSynchronizationManager隐藏了：
        // 1. 线程本地变量管理
        // 2. 资源绑定和解绑
        // 3. 事务同步回调

        System.out.println("TransactionSynchronizationManager：");
        System.out.println("  - bindResource() - 绑定资源到当前线程");
        System.out.println("  - getResource() - 获取当前线程的资源");
        System.out.println("  - unbindResource() - 解绑资源");
        System.out.println("  - registerSynchronization() - 注册事务同步回调");

        // 使用场景
        System.out.println("\n典型使用场景：");
        System.out.println("  1. DataSourceTransactionManager绑定数据库连接");
        System.out.println("  2. HibernateTransactionManager绑定Session");
        System.out.println("  3. 事务提交后的回调处理");

        System.out.println();
    }
}

/**
 * 模拟Spring的WebMvcConfigurer
 */
interface WebMvcConfigurer {
    // 视图解析器配置
    default void configureViewResolvers(ViewResolverRegistry registry) {
        // 默认实现
    }

    // 拦截器配置
    default void addInterceptors(InterceptorRegistry registry) {
        // 默认实现
    }

    // 静态资源处理
    default void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 默认实现
    }

    // CORS跨域配置
    default void addCorsMappings(CorsRegistry registry) {
        // 默认实现
    }
}

// 模拟配置类
class ViewResolverRegistry {
    public void beanNameViewResolver() {
        System.out.println("配置BeanNameViewResolver");
    }
}

class InterceptorRegistry {
    public void addInterceptor(Object handler) {
        System.out.println("添加拦截器");
    }
}

class ResourceHandlerRegistry {
    public void addResourceHandler(String path) {
        System.out.println("添加资源处理器: " + path);
    }
}

class CorsRegistry {
    public void addMapping(String path) {
        System.out.println("添加CORS映射: " + path);
    }
}

/**
 * 模拟配置类
 */
class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**");
        System.out.println("已配置CORS跨域");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**");
        System.out.println("已配置静态资源");
    }
}
