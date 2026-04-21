# 责任链模式 (Chain of Responsibility Pattern)

## 模式定义

责任链模式使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有一个对象处理它为止。

## UML结构

```
┌─────────────────────────┐
│    Handler             │
│   <<interface>>        │
├─────────────────────────┤
│ + handleRequest()      │
│ + setNext(Handler)     │
└───────────┬─────────────┘
            △
            │
┌───────────┴─────────────┐
│   AbstractHandler       │
├─────────────────────────┤
│ - next: Handler         │
│ + handleRequest()      │
│ + setNext(Handler)     │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│    ConcreteHandlerA    │
├─────────────────────────┤
│ + handleRequest()       │
│   if (canHandle)        │
│     doHandle()          │
│   else                  │
│     super.handle()      │
└─────────────────────────┘
```

## 核心思想

```
责任链模式 = Handler接口 + ConcreteHandler + 链式传递

关键点：
1. Handler定义处理请求的接口
2. ConcreteHandler决定是否处理请求
3. 如果不能处理，将请求传递给下一个处理器
4. 客户端可以决定链的顺序

应用场景：
- Servlet Filter
- Spring Interceptor
- 日志级别处理
- 审批流程
```

---

## 示例代码

### 基础示例：兽人军队

```java
// 请求类型
public enum RequestType {
    DEFEND_CASTLE,
    TORTURE_PRISONER,
    COLLECT_TAX
}

// 请求
public class Request {
    private final RequestType requestType;
    private final String requestDescription;

    public Request(RequestType requestType, String requestDescription) {
        this.requestType = requestType;
        this.requestDescription = requestDescription;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getRequestDescription() {
        return requestDescription;
    }
}

// 处理器抽象类
public abstract class RequestHandler {
    private RequestHandler next;

    public RequestHandler(RequestHandler next) {
        this.next = next;
    }

    public void handleRequest(Request req) {
        if (next != null) {
            next.handleRequest(req);
        }
    }
}

// 具体处理器：士兵
public class OrcSoldier extends RequestHandler {
    public OrcSoldier(RequestHandler next) {
        super(next);
    }

    @Override
    public void handleRequest(Request req) {
        if (RequestType.DEFEND_CASTLE.equals(req.getRequestType())) {
            printHandling(req);
            req.markHandled();
        } else {
            super.handleRequest(req);
        }
    }

    @Override
    public String toString() {
        return "Orc Soldier";
    }
}

// 具体处理器：军官
public class OrcOfficer extends RequestHandler {
    public OrcOfficer(RequestHandler next) {
        super(next);
    }

    @Override
    public void handleRequest(Request req) {
        if (RequestType.TORTURE_PRISONER.equals(req.getRequestType())) {
            printHandling(req);
            req.markHandled();
        } else {
            super.handleRequest(req);
        }
    }

    @Override
    public String toString() {
        return "Orc Officer";
    }
}

// 具体处理器：国王
public class OrcKing extends RequestHandler {
    public OrcKing(RequestHandler next) {
        super(next);
    }

    @Override
    public void handleRequest(Request req) {
        printHandling(req);
        req.markHandled();
    }

    @Override
    public String toString() {
        return "Orc King";
    }
}

// 使用
RequestHandler chain = new OrcKing(
    new OrcOfficer(
        new OrcSoldier(null)
    )
);

chain.handleRequest(new Request(RequestType.DEFEND_CASTLE, " defend the castle"));
```

---

## JDK中的应用

### 1. Servlet Filter

```java
// Servlet Filter使用责任链模式
public interface Filter {
    void doFilter(ServletRequest request, ServletResponse response,
                  FilterChain chain) throws IOException, ServletException;
}

// FilterChain是责任链
public interface FilterChain {
    void doFilter(ServletRequest request, ServletResponse response)
        throws IOException, ServletException;
}

// FilterDispatcher包含多个Filter
public class ApplicationFilterChain implements FilterChain {
    private List<Filter> filters = new ArrayList<>();
    private int pos = 0;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) {
        if (pos < filters.size()) {
            Filter filter = filters.get(pos++);
            filter.doFilter(request, response, this);
        }
        // 最终调用Servlet
    }
}

// 使用
// CharacterEncodingFilter -> SecurityFilter -> LoggingFilter -> Servlet
```

### 2. Logging Handler

```java
// java.util.logging使用责任链模式
public abstract class Handler {
    private Handler next;

    public void setNext(Handler handler) {
        this.next = handler;
    }

    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            doPublish(record);
        }
        if (next != null) {
            next.publish(record);
        }
    }

    protected abstract void doPublish(LogRecord record);
}

// ConsoleHandler -> FileHandler -> SocketHandler
```

---

## Spring框架中的应用

### 1. HandlerInterceptor

```java
// Spring MVC的拦截器使用责任链模式
public interface HandlerInterceptor {
    default boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        return true;
    }

    default void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler,
                                 Exception ex) throws Exception {
    }
}

// HandlerInterceptorChain管理拦截器
public class HandlerInterceptorChain {
    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    public void applyPreHandle(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(request, response, handler)) {
                return;
            }
        }
    }

    public void applyPostHandle(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                ModelAndView modelAndView) throws Exception {
        for (HandlerInterceptor interceptor : interceptors) {
            interceptor.postHandle(request, response, handler, modelAndView);
        }
    }
}

// 使用
// LoginInterceptor -> AuthInterceptor -> LoggingInterceptor -> Controller
```

### 2. TransactionSynchronization

```java
// Spring使用责任链管理事务同步
public interface TransactionSynchronization {
    void suspend();
    void resume();
    void beforeCommit(boolean readOnly);
    void beforeCompletion();
    void afterCommit();
    void afterCompletion(int status);
}

public class TransactionSynchronizationManager {
    private static final ThreadLocal<List<TransactionSynchronization>> synchronizations =
        new ThreadLocal<>();

    public static void registerSynchronization(TransactionSynchronization synchronization) {
        synchronizations.get().add(synchronization);
    }

    public static void invokeBeforeCommit(int readOnly) {
        for (TransactionSynchronization sync : synchronizations.get()) {
            sync.beforeCommit(readOnly);
        }
    }
}
```

---

## MyBatis中的应用

### 1. Interceptor Chain

```java
// MyBatis使用责任链模式管理拦截器
public interface Interceptor {
    Object intercept(Invocation invocation) throws Throwable;
}

// Plugin实现责任链
@Intercepts({
    @Signature(type = Executor.class, method = "query",
               args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class Plugin implements Interceptor {
    private Object target;
    private Interceptor interceptor;
    private Map<Class<?>, Set<Method>> signatureMap;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 执行拦截逻辑
        return interceptor.intercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}

// InterceptorChain管理拦截器链
public class InterceptorChain {
    private final List<Interceptor> interceptors = new ArrayList<>();

    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
}

// 使用
// 实际执行: queryInterceptor -> cacheInterceptor -> loggingInterceptor -> Executor
```

---

## 适用场景

1. **Servlet Filter**：请求/响应过滤
2. **Spring Interceptor**：Web请求拦截
3. **日志级别处理**：不同级别日志处理器
4. **审批流程**：多级审批
5. **异常处理**：多级异常捕获

## 优点

- **解耦**：发送者和接收者解耦
- **灵活性**：可以动态改变链的顺序
- **单一职责**：每个处理器只关注一个职责

## 缺点

- **链过长**：可能导致性能问题
- **调试困难**：链的顺序难以追踪
- **空请求**：如果没有处理器处理请求

## 对比其他模式

| 模式 | 关系 |
|------|------|
| 责任链模式 | 链式传递，请求被动处理 |
| 命令模式 | 请求封装，主动执行 |
| 观察者模式 | 一对多通知 |
| 装饰器模式 | 动态增加功能 |

---

## 代码示例

### 运行基础示例

```bash
cd ssitao-design-patterns/chain
mvn compile
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.chain.App"
```

### 运行基本责任链示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.chain.basic.BasicChainDemo"
```

### 运行订单处理链示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.chain.order.OrderProcessingChainDemo"
```

### 运行返回值链示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.chain.returnvalue.ChainWithReturnValueDemo"
```

### 运行Spring拦截器链示例

```bash
mvn exec:java -Dexec.mainClass="com.ssitao.code.designpattern.chain.spring.SpringInterceptorChainDemo"
```
