# 模板方法模式 (Template Method Pattern)

## 模式定义

模板方法模式定义一个算法的骨架，将某些步骤延迟到子类中实现，使子类可以不改变算法结构即可重写该算法的某些特定步骤。

## UML结构

```
┌─────────────────────────────┐
│      AbstractClass          │
├─────────────────────────────┤
│ + templateMethod()         │  // 模板方法，定义骨架
│ # step1()                  │  // 基本方法，子类实现
│ # step2()                  │  // 基本方法，子类实现
└─────────────────────────────┘
              △
              │
┌─────────────┴─────────────┐
│    ConcreteClass         │
├─────────────────────────────┤
│ # step1()                │  // 实现父类抽象方法
│ # step2()                │  // 实现父类抽象方法
└─────────────────────────────┘
```

## 核心思想

```
模板方法模式 = 抽象类 + 模板方法 + 基本方法 + 钩子方法

关键点：
1. 模板方法 - 定义算法骨架，调用各个步骤方法
2. 基本方法 - 抽象方法，子类必须实现
3. 钩子方法 - 可选重写，提供灵活性

与策略模式的区别：
- 模板方法：继承 + 方法调用
- 策略模式：组合 + 接口委托
```

---

## 示例代码

### 基础示例：偷窃

```java
// 抽象类 - 定义偷窃算法骨架
public abstract class StealingMethod {

    // 模板方法 - 定义偷窃步骤
    public void steal() {
        String target = pickTarget();      // 选择目标
        System.out.println("目标已选定: " + target);
        confuseTarget(target);            // 迷惑目标
        stealTheItem(target);              // 偷取物品
    }

    // 基本方法 - 子类必须实现
    protected abstract String pickTarget();
    protected abstract void confuseTarget(String target);
    protected abstract void stealTheItem(String target);
}

// 具体实现：突袭偷窃
public class HitAndRunMethod extends StealingMethod {
    @Override
    protected String pickTarget() {
        return "街边的路人";
    }

    @Override
    protected void confuseTarget(String target) {
        System.out.println("快速接近 " + target);
    }

    @Override
    protected void stealTheItem(String target) {
        System.out.println("抢走物品后迅速逃离");
    }
}

// 具体实现：悄悄偷窃
public class SubtleMethod extends StealingMethod {
    @Override
    protected String pickTarget() {
        return "博物馆的展品";
    }

    @Override
    protected void confuseTarget(String target) {
        System.out.println("假装参观者接近 " + target);
    }

    @Override
    protected void stealTheItem(String target) {
        System.out.println("巧妙地取走 " + target);
    }
}

// 使用
HalflingThief thief = new HalflingThief(new HitAndRunMethod());
thief.steal();
```

---

## JDK中的应用

### 1. InputStream

```java
// InputStream的read()方法是模板方法
public abstract class InputStream implements Closeable {
    // 模板方法
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        // 调用抽象方法读取单个字节
        int c = read();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        // 读取剩余字节
        int i = 1;
        try {
            for (; i < len; i++) {
                c = read();
                if (c == -1) {
                    break;
                }
                b[off + i] = (byte) c;
            }
        } catch (IOException e) {}
        return i;
    }

    // 抽象方法 - 子类必须实现
    public abstract int read() throws IOException;
}

// 具体实现：FileInputStream
public class FileInputStream extends InputStream {
    private native int read() throws IOException;
}

// 具体实现：ByteArrayInputStream
public class ByteArrayInputStream extends InputStream {
    public int read() {
        return (pos < count) ? (buf[pos++] & 0xff) : -1;
    }
}
```

### 2. AbstractList/AbstractSet

```java
// AbstractList定义模板方法
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    // 模板方法：indexOf
    public int indexOf(Object o) {
        ListIterator<E> it = listIterator();
        if (o == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return it.previousIndex();
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    return it.previousIndex();
                }
            }
        }
        return -1;
    }

    // 抽象方法 - 子类实现
    public abstract E get(int index);
    public abstract int size();
}

// 具体实现
public class ArrayList<E> extends AbstractList<E> implements List<E> {
    public E get(int index) {
        rangeCheck(index);
        return elementData(index);
    }

    public int size() {
        return size;
    }
}
```

### 3. HttpServlet

```java
// HttpServlet的service()方法是模板方法
public void service(ServletRequest req, ServletResponse res)
        throws ServletException, IOException {
    HttpServletRequest request;
    HttpServletRequest response;

    if (!(req instanceof HttpServletRequest && res instanceof HttpServletResponse)) {
        throw new ServletException("non-HTTP request or response");
    }

    request = (HttpServletRequest) req;
    response = (HttpServletResponse) res;

    service(request, response);  // 调用另一个模板方法
}

protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    String method = req.getMethod();

    if (method.equals(METHOD_GET)) {
        doGet(req, resp);  // 调用HTTP方法
    } else if (method.equals(METHOD_HEAD)) {
        doHead(req, resp);
    } else if (method.equals(METHOD_POST)) {
        doPost(req, resp);
    }
    // ...
}

// 具体子类只需重写doGet/doPost等方法
public class MyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 处理GET请求
    }
}
```

---

## Spring框架中的应用

### 1. JdbcTemplate

```java
// JdbcTemplate是模板方法模式的经典应用

public class JdbcTemplate {
    // 模板方法：execute
    public <T> T execute(ConnectionCallback<T> action) {
        Connection con = null;
        try {
            con = getDataSource().getConnection();
            return action.doInConnection(con);
        } finally {
            closeConnection(con);
        }
    }

    // 模板方法：query
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return execute(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List<T> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rowMapper.mapRow(rs, rs.getRow()));
            }
            closeResultSet(rs);
            closeStatement(ps);
            return result;
        });
    }
}

// 使用
jdbcTemplate.query("SELECT * FROM users WHERE id = ?",
    (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("name")));
```

### 2. HibernateTemplate

```java
// Spring ORM中的模板方法
public class HibernateTemplate {
    // 模板方法：execute
    public <T> T execute(HibernateCallback<T> action) {
        return doExecute(action, true);
    }

    private <T> T doExecute(HibernateCallback<T> action, boolean allowCreate) {
        Session session = getSession(allowCreate);
        Transaction tx = null;
        try {
            if (enableTransaction) {
                tx = session.beginTransaction();
            }
            T result = action.doInHibernate(session);
            if (tx != null) {
                tx.commit();
            }
            return result;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            closeSession();
        }
    }
}
```

### 3. TransactionSynchronizationManager

```java
// Spring事务管理的模板方法
public abstract class AbstractPlatformTransactionManager {
    // 模板方法
    public final void commit(TransactionStatus status) {
        if (status.isCompleted()) {
            throw new IllegalStateException("Transaction already completed");
        }
        if (status.isRollbackOnly()) {
            rollback(status);
            return;
        }
        processCommit(def, status);
    }

    // 抽象方法 - 子类实现
    protected abstract void doCommit(DefaultTransactionStatus status);
    protected abstract void doRollback(DefaultTransactionStatus status);
    protected abstract boolean isExistingTransaction(Object transaction);
    protected abstract void doBegin(Object transaction, TransactionDefinition definition);
}

// 具体实现
public class DataSourceTransactionManager extends AbstractPlatformTransactionManager {
    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        // JDBC提交实现
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        // JDBC回滚实现
    }
}
```

---

## MyBatis中的应用

### 1. BaseExecutor

```java
// MyBatis的BaseExecutor定义模板方法
public abstract class BaseExecutor implements Executor {
    // 模板方法：update
    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        ErrorContext.instance().activity("executing update").object(ms.getId());
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        clearLocalCache();
        doUpdate(ms, parameter);
        return updateCount;
    }

    // 模板方法：query
    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds,
                             ResultHandler resultHandler) throws SQLException {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        CacheKey key = createCacheKey(ms, parameter, rowBounds, noValue);
        return query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }

    // 抽象方法 - 子类实现
    protected abstract int doUpdate(MappedStatement ms, Object parameter);
    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter,
                                           RowBounds rowBounds, ResultHandler resultHandler,
                                           BoundSql boundSql);
    protected abstract void doFlushStatements(boolean is小野);
    protected abstract <E> List<E> queryFromDatabase(MappedStatement ms, Object parameter,
                                                      RowBounds rowBounds, ResultHandler resultHandler,
                                                      CacheKey key, BoundSql boundSql);
}

// 具体实现：SimpleExecutor
public class SimpleExecutor extends BaseExecutor {
    @Override
    protected int doUpdate(MappedStatement ms, Object parameter) {
        PreparedStatement ps = prepareStatement(ms, parameter);
        return ps.executeUpdate();
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter,
                                  RowBounds rowBounds, ResultHandler resultHandler,
                                  BoundSql boundSql) {
        PreparedStatement ps = prepareStatement(ms, parameter);
        return ps.getResultSet().unwrap(ResultSet.class);
    }
}
```

### 2. SqlSessionTemplate

```java
// MyBatis的SqlSessionTemplate是模板方法模式的应用
public class SqlSessionTemplate implements SqlSession {
    private final SqlSessionFactory sqlSessionFactory;
    private final ExecutorType executorType;

    // 模板方法：selectOne
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return this.sqlSessionFactory.openSession(executorType)
            .selectOne(statement, parameter);
    }

    // 模板方法：insert/update/delete
    @Override
    public int insert(String statement, Object parameter) {
        return update(statement, parameter);
    }

    @Override
    public int update(String statement, Object parameter) {
        return this.sqlSessionFactory.openSession(executorType)
            .update(statement, parameter);
    }
}
```

---

## 钩子方法

钩子方法是模板方法模式的重要组成部分，提供可选的重写点。

```java
public abstract class DataProcessor {
    // 模板方法
    public void process() {
        readData();
        validateData();
        if (shouldTransform()) {  // 钩子方法
            transformData();
        }
        writeData();
    }

    // 钩子方法 - 子类可选择重写
    protected boolean shouldTransform() {
        return true;  // 默认行为
    }

    // 基本方法 - 子类必须实现
    protected abstract void readData();
    protected abstract void validateData();
    protected abstract void transformData();
    protected abstract void writeData();
}

// 子类可以重写钩子方法改变行为
public class CsvDataProcessor extends DataProcessor {
    @Override
    protected boolean shouldTransform() {
        return false;  // CSV不需要转换
    }
}
```

---

## 适用场景

1. **算法骨架固定**：多个类实现相同算法结构
2. **步骤可扩展**：某些步骤需要子类实现，其他步骤固定
3. **控制扩展点**：通过钩子方法提供可选扩展
4. **框架设计**：如JDBC Template、HibernateTemplate

## 优点

- **代码复用**：复用父类代码，只重写差异部分
- **单一职责**：父类关注算法骨架，子类关注具体实现
- **灵活扩展**：通过钩子方法提供可选重写

## 缺点

- **类数量增加**：每个算法变体需要新类
- **继承限制**：Java单继承，可能导致继承层次复杂
- **违反开闭原则**：修改父类可能影响所有子类

## 对比策略模式

| 特性 | 模板方法 | 策略模式 |
|------|---------|----------|
| 机制 | 继承 | 组合 |
| 扩展 | 重写方法 | 替换对象 |
| 时间 | 编译时 | 运行时 |
| 灵活性 | 低 | 高 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/template-method
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.template.App"
```

### 运行基本模板方法示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.template.basic.BasicTemplateDemo"
```

### 运行钩子方法示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.template.hook.HookTemplateDemo"
```

### 运行业务模板方法示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.template.biz.BizTemplateDemo"
```

### 运行Web模板方法示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.template.web.WebTemplateDemo"
```
