package com.ssitao.code.designpattern.adapter.mybatis;

/**
 * MyBatis中的适配器模式示例
 *
 * MyBatis中使用适配器模式的场景：
 * 1. ResultSetHandler - JDBC ResultSet到Java对象的适配
 * 2. StatementHandler - SQL语句到PreparedStatement的适配
 * 3. TypeHandler - JDBC类型到Java类型的适配
 * 4. Log - 日志框架适配（统一不同日志库的接口）
 */
public class MyBatisAdapterDemo {

    public static void main(String[] args) {
        System.out.println("=== MyBatis Adapter Pattern Demo ===\n");

        // 1. TypeHandler - JDBC类型到Java类型的适配
        demonstrateTypeHandler();

        // 2. ResultSetHandler - ResultSet到对象的适配
        demonstrateResultSetHandler();

        // 3. Log适配 - 统一日志接口
        demonstrateLogAdapter();
    }

    /**
     * 1. TypeHandler示例
     * MyBatis使用TypeHandler将JDBC类型（如VARCHAR、DATE）适配为Java类型
     */
    private static void demonstrateTypeHandler() {
        System.out.println("--- 1. TypeHandler (JDBC类型 -> Java类型) ---");

        // MyBatis中的TypeHandler接口
        // 用于在PreparedStatement设置参数和从ResultSet读取结果时进行类型转换

        // 示例：StringTypeHandler将String适配为JDBC VARCHAR
        // String javaType = "Hello";
        // PreparedStatement.setString(paramIndex, javaType); // JDBC VARCHAR

        // 示例：DateTypeHandler将java.util.Date适配为JDBC DATE
        // java.util.Date date = new Date();
        // PreparedStatement.setDate(paramIndex, new java.sql.Date(date.getTime())); // JDBC DATE

        System.out.println("TypeHandler负责：");
        System.out.println("  1. PreparedStatement.setXxx() - Java对象转JDBC类型");
        System.out.println("  2. ResultSet.getXxx() - JDBC类型转Java对象");
        System.out.println("  3. ResultSet.getObject() - 自动类型转换");

        System.out.println();
    }

    /**
     * 2. ResultSetHandler示例
     * MyBatis使用ResultSetHandler将ResultSet（JDBC）适配为Java对象
     */
    private static void demonstrateResultSetHandler() {
        System.out.println("--- 2. ResultSetHandler (ResultSet -> Java对象) ---");

        // MyBatis的ResultSetHandler接口有多个实现：
        // - DefaultResultSetHandler: 默认实现
        // - NestedResultSetHandler: 嵌套结果集处理
        // - NestedLoopResultSetHandler: 嵌套循环结果集处理

        // 模拟ResultSet到对象的映射过程
        System.out.println("ResultSetHandler的工作流程：");
        System.out.println("  1. ResultSet.next() - 移动到下一行");
        System.out.println("  2. ResultSet.getXxx() - 读取列值");
        System.out.println("  3. TypeHandler.getXxx() - 转换为Java类型");
        System.out.println("  4. 创建Java对象并设置属性");

        // 示例SQL结果
        // SELECT id, name, age FROM user WHERE id = 1
        // ResultSet: [1, "Tom", 25]
        // User对象: User{id=1, name="Tom", age=25}

        System.out.println();
    }

    /**
     * 3. Log适配器示例
     * MyBatis使用Log适配不同日志框架，统一为Log接口
     */
    private static void demonstrateLogAdapter() {
        System.out.println("--- 3. Log Adapter (统一日志接口) ---");

        // MyBatis定义了自己的Log接口
        // 内部使用适配器模式来适配不同的日志框架：
        // - Log4jLoggerImpl -> 适配 Log4j
        // - Log4j2LoggerImpl -> 适配 Log4j2
        // - Slf4jLoggerImpl -> 适配 Slf4j
        // - Jdk14LoggerImpl -> 适配 JDK Logger
        // - NoOpImpl -> 无日志实现

        System.out.println("MyBatis日志适配器：");
        System.out.println("  - 定义统一的Log接口");
        System.out.println("  - 内部自动检测并适配具体的日志框架");
        System.out.println("  - 应用层代码只需要使用MyBatis的Log接口");

        System.out.println("  MyBatis使用的日志框架：");
        System.out.println("    - SLF4J");
        System.out.println("    - Log4j 2");
        System.out.println("    - Log4j");
        System.out.println("    - JDK Logging");
        System.out.println("    - No logging");

        System.out.println();
    }

    /**
     * 模拟MyBatis的TypeHandler接口
     */
    interface TypeHandler<T> {
        T getResult(ResultSetMock rs, String columnName) throws Exception;
        void setParameter(PreparedStatementMock ps, int position, T parameter) throws Exception;
    }

    /**
     * 模拟MyBatis的Log接口
     */
    interface Log {
        void debug(String message);
        void info(String message);
        void warn(String message);
        void error(String message);
        boolean isDebugEnabled();
    }

    /**
     * 模拟SLF4J到MyBatis Log的适配器
     */
    static class Slf4jToMyBatisLogAdapter implements Log {
        // 注意：实际MyBatis中使用的是org.slf4j.Logger
        // 这里只是演示适配器模式的结构
        private final String loggerName;

        public Slf4jToMyBatisLogAdapter(String loggerName) {
            this.loggerName = loggerName;
        }

        @Override
        public void debug(String message) {
            System.out.println("[DEBUG] " + loggerName + ": " + message);
        }

        @Override
        public void info(String message) {
            System.out.println("[INFO] " + loggerName + ": " + message);
        }

        @Override
        public void warn(String message) {
            System.out.println("[WARN] " + loggerName + ": " + message);
        }

        @Override
        public void error(String message) {
            System.out.println("[ERROR] " + loggerName + ": " + message);
        }

        @Override
        public boolean isDebugEnabled() {
            return true;
        }
    }

    /**
     * 模拟ResultSet
     */
    static class ResultSetMock {
        public boolean next() { return false; }
        public String getString(String column) { return null; }
        public int getInt(String column) { return 0; }
    }

    /**
     * 模拟PreparedStatement
     */
    static class PreparedStatementMock {
        public void setString(int position, String value) { }
        public void setInt(int position, int value) { }
    }
}
