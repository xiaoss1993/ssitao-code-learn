# 命令模式 (Command Pattern)

## 模式定义

命令模式将请求封装成对象，从而允许你参数化客户端对象、排队请求、记录日志，以及支持可撤销的操作。

## UML结构

```
┌─────────────────┐       ┌─────────────────┐
│     Client     │       │    Command     │
│                 │       │  <<interface>> │
└────────┬────────┘       └────────┬────────┘
         │                        │
         │                        │
┌────────▼────────┐       ┌────────┴────────┐
│    Invoker     │──────▶│ ConcreteCommand│
│                │       │                │
│ + setCommand() │       │ - receiver    │
│ + execute()   │       │ + execute()   │
└────────────────┘       └────────────────┘
                                △
                                │
                       ┌────────┴────────┐
                       │    Receiver     │
                       │                 │
                       │ + action()      │
                       └─────────────────┘
```

## 核心思想

```
命令模式 = 命令接口 + 具体命令 + 调用者 + 接收者

关键点：
1. Command (命令接口) - 定义执行方法
2. ConcreteCommand (具体命令) - 绑定接收者
3. Invoker (调用者) - 触发命令
4. Receiver (接收者) - 执行实际操作

应用场景：
- 请求排队、日志记录
- 撤销/重做功能
- 宏命令
- 请求延迟执行
```

---

## 示例代码

### 基础示例：法师施法

```java
// 命令接口
public interface Command {
    void execute(Target target);
    void undo();
    void redo();
}

// 接收者：目标
public class Target {
    private Size size;
    private Visibility visibility;

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility visibility) { this.visibility = visibility; }
}

// 具体命令：隐身术
public class InvisibilitySpell extends Command {
    private Target target;
    private Size previousSize;

    @Override
    public void execute(Target target) {
        this.target = target;
        previousSize = target.getSize();
        target.setVisibility(Visibility.INVISIBLE);
    }

    @Override
    public void undo() {
        target.setVisibility(Visibility.VISIBLE);
    }

    @Override
    public void redo() {
        execute(target);
    }
}

// 调用者：法师
public class Wizard {
    private List<Command> commands = new ArrayList<>();

    public void castSpell(Command command, Target target) {
        command.execute(target);
        commands.add(command);
    }

    public void undoLastSpell() {
        if (!commands.isEmpty()) {
            commands.remove(commands.size() - 1).undo();
        }
    }
}

// 使用
Wizard wizard = new Wizard();
Target goblin = new Goblin();
wizard.castSpell(new InvisibilitySpell(), goblin);
```

---

## JDK中的应用

### 1. Runnable

```java
// Runnable是命令接口
public interface Runnable {
    void run();
}

// Thread接收Runnable命令
Thread thread = new Thread(() -> {
    // 执行任务
    System.out.println("任务执行");
});
thread.start();

// ExecutorService也是命令模式的应用
ExecutorService executor = Executors.newFixedThreadPool(10);
executor.submit(() -> {
    // 执行任务
});
```

### 2. Swing Action

```java
// Swing使用命令模式
public interface Action extends ActionListener {
    void addPropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);
    boolean isEnabled();
    void setEnabled(boolean b);
}

// AbstractAction提供基本实现
public abstract class AbstractAction implements Action {
    protected boolean enabled = true;
    private Map<Object, Object> properties = new HashMap<>();

    @Override
    public void actionPerformed(ActionEvent e) {
        // 调用execute方法
        execute(e);
    }

    // 抽象方法，子类实现
    public abstract void execute(ActionEvent e);
}

// 使用
JButton button = new JButton("点击");
button.addActionListener(e -> {
    // 执行命令
    System.out.println("按钮点击");
});
```

### 3. Timer

```java
// java.util.Timer使用命令模式
Timer timer = new Timer();
timer.schedule(new TimerTask() {
    @Override
    public void run() {
        // 执行任务
        System.out.println("定时任务执行");
    }
}, 1000, 1000);  // 延迟1秒，周期1秒
```

---

## Spring框架中的应用

### 1. JdbcTemplate

```java
// JdbcTemplate封装命令模式
public class JdbcTemplate {
    // 命令接口
    public interface StatementCallback<T> {
        T doInStatement(Statement stmt) throws SQLException;
    }

    public <T> T execute(StatementCallback<T> action) {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            return action.doInStatement(stmt);
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }

    // 使用示例
    public void update(String sql) {
        jdbcTemplate.execute(stmt -> {
            stmt.executeUpdate(sql);
            return null;
        });
    }
}
```

### 2. Spring MVC命令对象

```java
// Spring MVC使用命令模式处理表单
@Controller
public class UserController {
    @PostMapping("/user")
    public String saveUser(@ModelAttribute UserCommand command) {
        userService.save(command);
        return "redirect:/user/list";
    }
}

// 命令对象
public class UserCommand {
    private String name;
    private String email;
    private Integer age;
    // getters and setters
}
```

### 3. Spring Shell

```java
// Spring Shell使用命令模式
@ShellComponent
public class HelloCommands {
    @ShellMethod("Say hello")
    public String hello(@ShellOption String name) {
        return "Hello, " + name + "!";
    }

    @ShellMethod("Exit the shell")
    public void exit() {
        System.exit(0);
    }
}

// 命令注册和执行机制
public class SimpleCommandRegistry implements CommandRegistry {
    private Map<String, Method> commands = new HashMap<>();

    public void register(Object bean) {
        // 扫描bean上的@ShellMethod注解
        // 注册命令
    }

    public String execute(String commandName, Object... args) {
        Method method = commands.get(commandName);
        return method.invoke(bean, args);
    }
}
```

### 4. Spring Batch

```java
// Spring Batch使用命令模式执行任务
public interface Job {
    void execute(JobExecution execution);
}

public class JobLauncher {
    public void run(Job job, JobParameters params) {
        JobExecution execution = new JobExecution(params);
        job.execute(execution);
    }
}

// 具体任务
public class ImportUserJob implements Job {
    @Override
    public void execute(JobExecution execution) {
        // 读取数据
        execution.setStatus(BatchStatus.STARTED);
        // 处理数据
        // 写入数据
    }
}
```

### 5. Spring Security Filter

```java
// Spring Security使用命令模式（FilterChain）
public interface Filter {
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain);
}

// FilterChain持有多个Filter
public interface FilterChain {
    void doFilter(ServletRequest request, ServletResponse response);
}

// 使用
FilterChain chain = new SecurityFilterChain();
chain.addFilter(new UsernamePasswordAuthenticationFilter());
chain.addFilter(new JwtAuthenticationFilter());
chain.doFilter(request, response);
```

---

## MyBatis中的应用

### 1. SqlSession

```java
// MyBatis的SqlSession方法可以看作命令模式
public interface SqlSession {
    <T> T selectOne(String statement, Object parameter);
    int insert(String statement, Object parameter);
    int update(String statement, Object parameter);
    int delete(String statement, Object parameter);
    void commit();
    void rollback();
    void close();
}

// 每个方法都是一个命令
// SqlSession实现类负责执行这些命令
```

### 2. Executor

```java
// Executor的update/query方法是命令模式
public interface Executor {
    int update(MappedStatement ms, Object parameter) throws SQLException;
    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds,
                      ResultHandler resultHandler) throws SQLException;
    <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds);
    void close(boolean forceRollback);
}

// 命令执行过程
public class SimpleExecutor implements Executor {
    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        // 1. 获取SQL
        BoundSql boundSql = ms.getBoundSql(parameter);

        // 2. 创建命令
        Statement statement = prepareStatement(ms, parameter, boundSql);

        // 3. 执行命令
        return statement.execute();
    }
}
```

---

## 宏命令

宏命令是命令模式的扩展，可以批量执行多个命令。

```java
// 宏命令
public class MacroCommand implements Command {
    private List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void removeCommand(Command command) {
        commands.remove(command);
    }

    @Override
    public void execute(Target target) {
        for (Command command : commands) {
            command.execute(target);
        }
    }

    @Override
    public void undo() {
        for (Command command : commands) {
            command.undo();
        }
    }

    @Override
    public void redo() {
        execute(null);
    }
}

// 使用
MacroCommand macro = new MacroCommand();
macro.addCommand(new InvisibilitySpell());
macro.addCommand(new ShrinkSpell());
macro.execute(goblin);  // 批量执行
```

---

## 适用场景

1. **请求排队**：异步执行请求
2. **日志记录**：记录操作历史
3. **撤销/重做**：支持回滚操作
4. **宏命令**：批量执行命令
5. **事务管理**：确保操作原子性

## 优点

- **松耦合**：调用者和接收者解耦
- **可扩展**：新增命令不影响现有代码
- **可组合**：宏命令可以组合多个命令
- **支持撤销**：命令可以存储历史记录

## 缺点

- **类数量增加**：每个操作需要一个命令类
- **复杂性**：简单场景可能过度设计
- **内存消耗**：大量命令可能导致内存问题

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 命令模式 | 请求封装，可撤销 |
| 策略模式 | 算法替换，委托执行 |
| 观察者模式 | 一对多通知 |
| 模板方法 | 算法骨架，部分重写 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/command
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.command.App"
```

### 运行基本命令示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.command.basic.BasicCommandDemo"
```

### 运行撤销重做示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.command.undo.UndoRedoDemo"
```

### 运行宏命令示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.command.macro.MacroCommandDemo"
```

### 运行Spring命令示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.command.web.SpringCommandDemo"
```
