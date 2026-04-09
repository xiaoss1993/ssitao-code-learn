package com.ssitao.code.designpattern.bridge.mybatis;

/**
 * MyBatis中的桥接模式示例
 *
 * MyBatis中使用桥接模式的场景：
 * 1. Executor - SQL执行抽象与具体执行实现
 * 2. ResultSetHandler - 结果集处理抽象与具体实现
 * 3. StatementHandler - SQL语句处理抽象与具体实现
 */
public class MyBatisBridgeDemo {

    public static void main(String[] args) {
        System.out.println("=== MyBatis Bridge Pattern Demo ===\n");

        // 1. Executor
        demonstrateExecutor();

        // 2. ResultSetHandler
        demonstrateResultSetHandler();

        // 3. StatementHandler
        demonstrateStatementHandler();
    }

    /**
     * 1. Executor
     * Executor(抽象) <--------> Transaction(实现)
     */
    private static void demonstrateExecutor() {
        System.out.println("--- 1. Executor (SQL执行) ---\n");

        System.out.println("MyBatis使用桥接模式分离Executor和Transaction：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - Executor接口: 定义SQL执行方法");
        System.out.println("    - query(): 执行查询");
        System.out.println("    - update(): 执行更新");
        System.out.println("    - flushStatements(): 刷新语句");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - Transaction接口: 定义事务方法");
        System.out.println("    - JdbcTransaction: JDBC本地事务实现");
        System.out.println("    - ManagedTransaction: 容器的托管事务");
        System.out.println("    - SpringManagedTransaction: Spring托管事务");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    Executor持有Transaction引用");
        System.out.println("    Executor通过Transaction获取数据库连接");
        System.out.println("    不同的Executor实现使用不同的查询优化策略");
        System.out.println();

        // 模拟使用
        System.out.println("Executor使用示例：");
        System.out.println("  // SimpleExecutor - 每次执行创建新的语句");
        System.out.println("  SimpleExecutor simpleExecutor = new SimpleExecutor(");
        System.out.println("      new JdbcTransaction(dataSource));");
        System.out.println();
        System.out.println("  // ReuseExecutor - 重用预编译语句");
        System.out.println("  ReuseExecutor reuseExecutor = new ReuseExecutor(");
        System.out.println("      new JdbcTransaction(dataSource));");
        System.out.println();
        System.out.println("  // BatchExecutor - 批量执行");
        System.out.println("  BatchExecutor batchExecutor = new BatchExecutor(");
        System.out.println("      new JdbcTransaction(dataSource));");
        System.out.println();
        System.out.println("  // CachingExecutor - 带缓存的执行器（装饰器模式）");
        System.out.println("  CachingExecutor cachingExecutor = new CachingExecutor(");
        System.out.println("      new SimpleExecutor(new JdbcTransaction(dataSource)));");
        System.out.println();
        System.out.println("  注意：Transaction实现可以切换（如SpringManagedTransaction）");
        System.out.println("  而不改变Executor的使用方式");
        System.out.println();
    }

    /**
     * 2. ResultSetHandler
     * ResultSetHandler(抽象) <--------> TypeHandler(实现)
     */
    private static void demonstrateResultSetHandler() {
        System.out.println("--- 2. ResultSetHandler (结果集处理) ---\n");

        System.out.println("MyBatis使用桥接模式分离ResultSetHandler和TypeHandler：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - ResultSetHandler接口: 定义结果集处理");
        System.out.println("    - handleResultSets(): 处理结果集");
        System.out.println("    - handleOutputParameters(): 处理输出参数");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - TypeHandler接口: 定义类型转换");
        System.out.println("    - StringTypeHandler: String <-> VARCHAR");
        System.out.println("    - IntegerTypeHandler: Integer <-> INTEGER");
        System.out.println("    - DateTypeHandler: Date <-> TIMESTAMP");
        System.out.println("    - BooleanTypeHandler: Boolean <-> BIT");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    ResultSetHandler持有TypeHandlerRegistry引用");
        System.out.println("    处理每列数据时调用TypeHandler进行类型转换");
        System.out.println("    可以注册自定义TypeHandler处理特殊类型");
        System.out.println();

        // 模拟使用
        System.out.println("ResultSetHandler使用示例：");
        System.out.println("  // 处理流程");
        System.out.println("  ResultSet rs = statement.executeQuery(sql);");
        System.out.println("  ResultSetWrapper rsw = new ResultSetWrapper(rs);");
        System.out.println();
        System.out.println("  while (rs.next()) {");
        System.out.println("      // 1. 获取列值（原始JDBC类型）");
        System.out.println("      Object rawValue = rsw.getResultSet().getObject(column);");
        System.out.println();
        System.out.println("      // 2. 使用TypeHandler转换为Java类型");
        System.out.println("      TypeHandler handler = typeHandlerRegistry.getTypeHandler(propertyType);");
        System.out.println("      Object javaValue = handler.getResult(rs, column);");
        System.out.println();
        System.out.println("      // 3. 设置到目标对象");
        System.out.println("      metaObject.setValue(propertyName, javaValue);");
        System.out.println("  }");
        System.out.println();

        // 自定义TypeHandler
        System.out.println("自定义TypeHandler示例：");
        System.out.println("  @MappedTypes(MyEnum.class)");
        System.out.println("  public class MyEnumTypeHandler implements TypeHandler<MyEnum> {");
        System.out.println("      @Override");
        System.out.println("      public void setParameter(PreparedStatement ps, int i, MyEnum parameter, JdbcType jdbcType) {");
        System.out.println("          ps.setString(i, parameter.name());");
        System.out.println("      }");
        System.out.println();
        System.out.println("      @Override");
        System.out.println("      public MyEnum getResult(ResultSet rs, String columnName) {");
        System.out.println("          return MyEnum.valueOf(rs.getString(columnName));");
        System.out.println("      }");
        System.out.println("  }");
        System.out.println();
    }

    /**
     * 3. StatementHandler
     * StatementHandler(抽象) <--------> ParameterHandler(实现)
     */
    private static void demonstrateStatementHandler() {
        System.out.println("--- 3. StatementHandler (SQL语句处理) ---\n");

        System.out.println("MyBatis使用桥接模式分离StatementHandler和ParameterHandler：");
        System.out.println();
        System.out.println("  抽象部分 (Abstraction)");
        System.out.println("    - StatementHandler接口: 定义语句处理");
        System.out.println("    - prepare(): 预编译SQL语句");
        System.out.println("    - parameterize(): 设置参数");
        System.out.println("    - batch(): 批量操作");
        System.out.println("    - update()/query(): 执行操作");
        System.out.println();
        System.out.println("  实现部分 (Implementor)");
        System.out.println("    - ParameterHandler接口: 定义参数处理");
        System.out.println("    - DefaultParameterHandler: 默认参数处理");
        System.out.println("    - TypeHandlerParameterHandler: 使用TypeHandler处理参数");
        System.out.println();
        System.out.println("  桥接方式：");
        System.out.println("    StatementHandler持有ParameterHandler引用");
        System.out.println("    StatementHandler通过ParameterHandler设置参数");
        System.out.println("    不同的StatementHandler实现不同的SQL处理策略");
        System.out.println();

        // 模拟使用
        System.out.println("StatementHandler使用示例：");
        System.out.println("  // SimpleStatementHandler - 处理普通SQL");
        System.out.println("  SimpleStatementHandler simpleHandler = new SimpleStatementHandler(");
        System.out.println("      mappedStatement, parameter, statementFactory);");
        System.out.println();
        System.out.println("  // PreparedStatementHandler - 处理预编译SQL");
        System.out.println("  PreparedStatementHandler preparedHandler = new PreparedStatementHandler(");
        System.out.println("      mappedStatement, parameter, statementFactory);");
        System.out.println();
        System.out.println("  // CallableStatementHandler - 处理存储过程");
        System.out.println("  CallableStatementHandler callableHandler = new CallableStatementHandler(");
        System.out.println("      mappedStatement, parameter, statementFactory);");
        System.out.println();
        System.out.println("  // RoutingStatementHandler - 路由到具体的Handler");
        System.out.println("  RoutingStatementHandler routingHandler = new RoutingStatementHandler(");
        System.out.println("      mappedStatement);");
        System.out.println("  // 根据mappedStatement.type选择具体的Handler");
        System.out.println();
        System.out.println("  // 执行流程");
        System.out.println("  Statement stmt = handler.prepare(connection);");
        System.out.println("  handler.parameterize(stmt);");
        System.out.println("  handler.batch(stmt);");
        System.out.println("  ResultSet rs = handler.query(stmt, resultHandler);");
        System.out.println();
    }
}
