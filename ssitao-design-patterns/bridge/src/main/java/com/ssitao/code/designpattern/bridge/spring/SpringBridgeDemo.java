package com.ssitao.code.designpattern.bridge.spring;

/**
 * Spring框架中的桥接模式示例
 *
 * Spring中使用桥接模式的场景：
 * 1. TransactionManager - 事务管理抽象与具体实现
 * 2. ViewResolver - 视图解析抽象与具体实现
 * 3. Resource - 资源访问抽象与具体实现
 */
public class SpringBridgeDemo {

    public static void main(String[] args) {
        System.out.println("=== Spring Bridge Pattern Demo ===\n");

        // 1. TransactionManager
        demonstrateTransactionManager();

        // 2. ViewResolver
        demonstrateViewResolver();

        // 3. Resource
        demonstrateResource();
    }

    /**
     * 1. TransactionManager
     * PlatformTransactionManager(抽象) <--------> Transaction(实现)
     */
    private static void demonstrateTransactionManager() {
        System.out.println("--- 1. TransactionManager (事务管理) ---\n");

        System.out.println("Spring使用桥接模式分离事务管理和具体事务实现：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - PlatformTransactionManager: 事务管理抽象");
        System.out.println("    - getTransaction(): 获取事务");
        System.out.println("    - commit(): 提交事务");
        System.out.println("    - rollback(): 回滚事务");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - Transaction接口: 定义事务状态");
        System.out.println("    - DataSourceTransactionManager: JDBC事务实现");
        System.out.println("    - JpaTransactionManager: JPA事务实现");
        System.out.println("    - HibernateTransactionManager: Hibernate事务实现");
        System.out.println("    - JtaTransactionManager: JTA分布式事务实现");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    PlatformTransactionManager持有Transaction引用");
        System.out.println("    不同的事务管理器管理不同类型的Transaction");
        System.out.println("    应用程序只需使用PlatformTransactionManager接口");
        System.out.println();

        // 模拟使用
        System.out.println("TransactionManager使用示例：");
        System.out.println("  @Autowired");
        System.out.println("  private PlatformTransactionManager transactionManager;");
        System.out.println();
        System.out.println("  public void transferMoney() {");
        System.out.println("    DefaultTransactionDefinition def = new DefaultTransactionDefinition();");
        System.out.println("    TransactionStatus status = transactionManager.getTransaction(def);");
        System.out.println("    try {");
        System.out.println("        // 业务逻辑");
        System.out.println("        transactionManager.commit(status);");
        System.out.println("    } catch (Exception e) {");
        System.out.println("        transactionManager.rollback(status);");
        System.out.println("    }");
        System.out.println("  }");
        System.out.println();
        System.out.println("  注意：PlatformTransactionManager可以是");
        System.out.println("  DataSourceTransactionManager或JpaTransactionManager等");
        System.out.println("  客户端代码无需改变，只需配置不同的Bean");
        System.out.println();
    }

    /**
     * 2. ViewResolver
     * ViewResolver(抽象) <--------> View(实现)
     */
    private static void demonstrateViewResolver() {
        System.out.println("--- 2. ViewResolver (视图解析) ---\n");

        System.out.println("Spring MVC使用桥接模式分离视图解析和视图渲染：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - ViewResolver接口: 解析视图名");
        System.out.println("    - resolveViewName(): 根据视图名返回View对象");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - View接口: 定义渲染方法");
        System.out.println("    - InternalResourceView: 渲染JSP/HTML");
        System.out.println("    - FreeMarkerView: 渲染FreeMarker模板");
        System.out.println("    - ThymeleafView: 渲染Thymeleaf模板");
        System.out.println("    - MappingJackson2JsonView: 渲染JSON");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    ViewResolver持有View的引用");
        System.out.println("    控制器返回视图名，由ViewResolver选择View");
        System.out.println("    View负责具体的渲染逻辑");
        System.out.println();

        // 模拟使用
        System.out.println("ViewResolver配置示例：");
        System.out.println("  @Bean");
        System.out.println("  public ViewResolver internalResourceViewResolver() {");
        System.out.println("      InternalResourceViewResolver resolver = new InternalResourceViewResolver();");
        System.out.println("      resolver.setPrefix(\"/WEB-INF/views/\");");
        System.out.println("      resolver.setSuffix(\".jsp\");");
        System.out.println("      return resolver;");
        System.out.println("  }");
        System.out.println();
        System.out.println("  @Bean");
        System.out.println("  public ViewResolver freemarkerViewResolver() {");
        System.out.println("      FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();");
        System.out.println("      resolver.setPrefix(\"/templates/\");");
        System.out.println("      resolver.setSuffix(\".ftl\");");
        System.out.println("      return resolver;");
        System.out.println("  }");
        System.out.println();
        System.out.println("  控制器：");
        System.out.println("  @RequestMapping(\"/hello\")");
        System.out.println("  public String hello() {");
        System.out.println("      return \"hello\";  // 返回视图名");
        System.out.println("  }");
        System.out.println("  // ViewResolver根据配置选择InternalResourceView或FreeMarkerView");
        System.out.println();
    }

    /**
     * 3. Resource
     * Resource(抽象) <--------> InputStreamSource(实现)
     */
    private static void demonstrateResource() {
        System.out.println("--- 3. Resource (资源访问) ---\n");

        System.out.println("Spring使用桥接模式分离资源抽象和具体实现：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - Resource接口: 定义资源操作");
        System.out.println("    - exists(): 检查资源是否存在");
        System.out.println("    - isReadable(): 检查是否可读");
        System.out.println("    - getInputStream(): 获取输入流");
        System.out.println("    - getDescription(): 获取描述");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - UrlResource: URL资源");
        System.out.println("    - ClassPathResource: 类路径资源");
        System.out.println("    - FileSystemResource: 文件系统资源");
        System.out.println("    - ServletContextResource: Web应用资源");
        System.out.println("    - ByteArrayResource: 字节数组资源");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    Resource接口定义统一的资源操作");
        System.out.println("    不同实现类处理不同来源的资源");
        System.out.println("    应用程序可以使用统一的Resource接口访问不同资源");
        System.out.println();

        // 模拟使用
        System.out.println("Resource使用示例：");
        System.out.println("  @Autowired");
        System.out.println("  private ResourceLoader resourceLoader;");
        System.out.println();
        System.out.println("  public void readResource() {");
        System.out.println("      // 加载类路径资源");
        System.out.println("      Resource classPathResource = resourceLoader.getResource(\"classpath:config.properties\");");
        System.out.println("      InputStream is = classPathResource.getInputStream();");
        System.out.println();
        System.out.println("      // 加载文件资源");
        System.out.println("      Resource fileResource = resourceLoader.getResource(\"file:/data/users.txt\");");
        System.out.println("      InputStream is2 = fileResource.getInputStream();");
        System.out.println();
        System.out.println("      // 加载URL资源");
        System.out.println("      Resource urlResource = resourceLoader.getResource(\"https://example.com/data.json\");");
        System.out.println("      InputStream is3 = urlResource.getInputStream();");
        System.out.println("  }");
        System.out.println();
        System.out.println("  注意：不同的Resource实现处理不同的资源来源");
        System.out.println("  客户端代码使用统一的Resource接口");
        System.out.println();
    }
}
