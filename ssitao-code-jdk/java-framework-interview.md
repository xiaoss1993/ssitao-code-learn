# Java 常用框架面试题

> 内容整理自 GitHub 高星项目，涵盖 Spring、MyBatis、Spring Cloud、消息队列等

## 一、Spring

### 1. ⭐ Spring 框架特点

- **轻量级**：Spring 核心包很小
- **控制反转（IOC）**：将对象创建权交给容器
- **面向切面编程（AOP）**：分离业务逻辑和通用功能
- **容器**：管理对象的生命周期和配置
- **框架集成**：集成多种优秀框架（ Hibernate、MyBatis 等）

### 2. ⭐ IOC 原理

**IOC（控制反转）**：将对象的创建权反转给 Spring 容器，由容器负责依赖注入。

**实现原理**：
1. 解析配置文件或注解，获取 bean 定义信息
2. 使用反射（Class.forName、Constructor.newInstance）创建对象
3. 通过反射注入依赖

```java
// 简单 IOC 容器实现
public class SimpleIOC {
    private Map<String, Object> beans = new HashMap<>();

    public void loadBeans(String location) {
        // 解析 XML 或注解，获取 bean 定义
        // 使用反射创建 bean 实例
        // 注入依赖
    }
}
```

### 3. ⭐ Bean 生命周期

1. **实例化**（Instantiation）：调用构造方法创建 Bean 实例
2. **属性填充**（Populate）：注入依赖（@Autowired、@Value）
3. **Aware 接口**：执行 BeanNameAware、BeanFactoryAware 等
4. **BeanPostProcessor 前置处理**（Before initialization）
5. **初始化**（Initialization）：
   - @PostConstruct 注解方法
   - InitializingBean 接口的 afterPropertiesSet()
   - 自定义 init-method
6. **BeanPostProcessor 后置处理**（After initialization）
7. **销毁**（Destruction）：
   - @PreDestroy 注解方法
   - DisposableBean 接口的 destroy()
   - 自定义 destroy-method

### 4. ⭐ 依赖注入方式

| 方式 | 说明 |
|------|------|
| setter 注入 | 通过 set 方法注入 |
| 构造器注入 | 通过构造方法注入 |
| 字段注入 | 直接在字段上 @Autowired（JDK 17+ 禁止） |

**推荐构造器注入**，原因：
- 便于单元测试
- 不可变对象
- 避免循环依赖检测不到
- 明确必需依赖

```java
// 构造器注入（推荐）
@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }
}
```

### 5. ⭐ AOP 原理

**AOP（面向切面编程）**：将通用逻辑（日志、事务、权限）与业务逻辑分离。

**实现方式**：
- **静态代理**：编译时织入（AspectJ）
- **动态代理**：
  - JDK 动态代理（基于接口）
  - CGLIB 动态代理（基于继承）

```java
// JDK 动态代理示例
public class JdkDynamicProxy implements InvocationHandler {
    private Object target;

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 前置增强
        System.out.println("before method");
        Object result = method.invoke(target, args);
        // 后置增强
        System.out.println("after method");
        return result;
    }
}
```

### 6. ⭐ Spring 事务传播行为

| 传播行为 | 说明 |
|----------|------|
| REQUIRED（默认） | 有事务加入，无则创建新事务 |
| REQUIRES_NEW | 挂起当前事务，创建新事务 |
| NESTED | 嵌套事务（savepoint） |
| SUPPORTS | 有事务则加入，无则非事务执行 |
| NOT_SUPPORTED | 以非事务执行，挂起事务 |
| MANDATORY | 必须有事务，否则抛异常 |
| NEVER | 必须无事务，否则抛异常 |

```java
@Service
public class OrderService {
    @Transactional
    public void createOrder() {
        // 创建订单
        paymentService.pay();  // REQUIRES_NEW 会开启新事务
    }
}

@Service
public class PaymentService {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void pay() {
        // 独立事务
    }
}
```

### 7. ⭐ @Transactional 失效场景

| 场景 | 说明 |
|------|------|
| 方法内部调用 | 不走代理对象，直接调用 |
| 非 public 方法 | 代理无法拦截 |
| 异常被 catch | 异常被吞掉，未抛出 |
| 异常类型不匹配 | 默认只回滚 RuntimeException |
|  rollbackFor 配置错误 | 需要指定异常类型 |
| 事务传播问题 | 内部方法事务传播行为导致 |
| 多数据源 | 未正确配置事务管理器 |

```java
// 正确用法
@Transactional(rollbackFor = Exception.class)
public void method() {
    try {
        // 业务逻辑
    } catch (Exception e) {
        throw e;  // 重新抛出异常
    }
}

// 方法内部调用（失效示例）
@Service
public class TestService {
    public void outer() {
        this.inner();  // 不会生效，走的是 this 不是代理
    }

    @Transactional
    public void inner() {
        // 事务不生效
    }
}
```

### 8. Spring MVC 工作流程

```
请求 → DispatcherServlet
         ↓
    HandlerMapping（映射到 Controller）
         ↓
    HandlerAdapter 执行 Handler
         ↓
    返回 ModelAndView
         ↓
    ViewResolver 解析视图
         ↓
    渲染视图 → 响应
```

### 9. Spring Boot 自动配置原理

**@SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan**

1. Spring Boot 启动时扫描 `META-INF/spring.factories`
2. 加载 EnableAutoConfiguration 实现类
3. 按条件（@Conditional*）进行配置
4. 将 Bean 注册到容器中

```java
// spring.factories 示例
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,\
org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
```

### 10. Spring 中的设计模式

| 模式 | 应用场景 |
|------|----------|
| 单例模式 | Bean 默认作用域 |
| 工厂模式 | BeanFactory |
| 代理模式 | AOP |
| 模板方法模式 | JdbcTemplate |
| 观察者模式 | 事件监听机制 |
| 适配器模式 | HandlerAdapter |

---

## 二、Spring Cloud

### 11. ⭐ 微服务架构特点

| 特点 | 说明 |
|------|------|
| 微小 | 每个服务专注单一功能 |
| 独立 | 独立部署、升级、扩展 |
| 解耦 | 服务间松耦合 |
| 技术多样 | 可选不同技术栈 |

### 12. ⭐ 服务注册与发现

**Eureka 工作原理**：
- 服务提供者向 Eureka 注册
- 服务消费者从 Eureka 获取服务列表
- 心跳机制保持服务活性

| 组件 | 作用 |
|------|------|
| Eureka Server | 注册中心 |
| Eureka Client | 服务提供者/消费者 |
| 服务注册 | provider.register() |
| 服务续约 | 每30秒续约 |
| 服务剔除 | 90秒未续约则剔除 |

### 13. ⭐ Nacos 与 Eureka 区别

| 对比 | Nacos | Eureka |
|------|-------|--------|
| CAP 原则 | CP + AP 可切换 | AP |
| 健康检查 | 支持 | 不支持 |
| 配置中心 | 支持 | 不支持 |
| 界面 | 有管理界面 | 无 |

### 14. ⭐ Ribbon 负载均衡策略

| 策略 | 说明 |
|------|------|
| RoundRobinRule | 轮询（默认） |
| RandomRule | 随机 |
| RetryRule | 重试 |
| WeightedResponseTimeRule | 响应时间加权 |
| BestAvailableRule | 最小并发 |

### 15. ⭐ Feign 工作原理

1. 启动时扫描 @FeignClient 注解
2. 通过动态代理创建代理对象
3. 请求时生成 HTTP 请求模板
4. 使用 Ribbon 负载均衡

```java
@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    User getUser(@PathVariable Long id);
}

// 降级处理
@Component
public class UserClientFallback implements UserClient {
    @Override
    public User getUser(Long id) {
        return new User();
    }
}
```

### 16. ⭐ Hystrix / Sentinel 熔断降级

**Hystrix 熔断器三种状态**：
- **Closed**：正常状态，统计失败率
- **Open**：熔断状态，快速失败
- **Half-Open**：尝试恢复

**Sentinel 与 Hystrix 对比**：

| 对比 | Sentinel | Hystrix |
|------|----------|---------|
| 实时统计 | 是 | 是 |
| 流量控制 | 支持 | 不支持 |
| 系统自适应保护 | 支持 | 不支持 |
| 隔离策略 | 线程池/信号量 | 线程池隔离 |

### 17. ⭐ Gateway 路由网关

**核心概念**：
- **Route**：路由规则
- **Predicate**：判断条件
- **Filter**：过滤处理

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/user/**
          filters:
            - StripPrefix=1
```

### 18. ⭐ 分布式事务解决方案

| 方案 | 说明 | 适用场景 |
|------|------|----------|
| 2PC | 两阶段提交 | 强一致性 |
| TCC | Try-Confirm-Cancel | 性能要求高 |
| 本地消息表 | 异步确保 | 可接受最终一致 |
| Seata | AT 模式 | 通用场景 |
| SAGA | 长事务 | 业务流程长 |

**Seata 三种模式**：
- AT 模式：自动补偿
- TCC 模式：手动补偿
- SAGAS 模式：编排服务

### 19. ⭐ 配置中心 Config

**Config 工作流程**：
1. 配置中心存储配置文件（Git）
2. Config Server 对外提供接口
3. Config Client 启动时拉取配置
4. 支持动态刷新 @RefreshScope

---

## 三、消息队列（MQ）

### 20. ⭐ 消息队列应用场景

| 场景 | 说明 |
|------|------|
| 异步处理 | 非核心流程异步化，提升响应速度 |
| 流量削峰 | 缓解并发压力，平滑处理高峰流量 |
| 应用解耦 | 降低服务间依赖 |
| 日志处理 | 异步收集日志 |
| 消息通讯 | 点对点、发布订阅 |

### 21. ⭐ Kafka 保证消息不丢失

| 环节 | 保证措施 |
|------|----------|
| 生产者 | acks=all + retries |
| Broker | replication.factor>=3 + min.insync.replicas>=2 |
| 消费者 | 手动提交 offset |

```properties
# 生产者配置
acks=all
retries=MAX
enforce.idempotence=true

# Broker 配置
replication.factor=3
min.insync.replicas=2

# 消费者配置
enable.auto.commit=false
auto.offset.reset=earliest
```

### 22. ⭐ Kafka 保证消息不重复消费

**原因**：消费者重启或处理超时导致重复消费

**解决方案**：
- 幂等处理（Redis/MongoDB 去重）
- 业务表唯一键
- 分布式事务

```java
// 幂等示例
if (redis.setIfAbsent("msg:" + msgId, "1", 24 * 3600)) {
    // 处理消息
} else {
    // 重复消息，跳过
}
```

### 23. ⭐ Kafka 保证消息顺序

**问题**：分区并行消费无法保证顺序

**解决方案**：
- 单分区：所有消息发到同一分区
- 业务层：相同 Key 发到同一分区

```java
// 按用户 ID 路由到同一分区
kafkaTemplate.send("topic", userId, message);
```

### 24. ⭐ RabbitMQ 消息模型

| 组件 | 说明 |
|------|------|
| Producer | 生产者 |
| Consumer | 消费者 |
| Exchange | 交换机（路由） |
| Queue | 队列 |
| Binding | 绑定关系 |
| Virtual Host | 虚拟主机，隔离资源 |

**Exchange 类型**：
- **Direct**：完全匹配
- **Fanout**：广播
- **Topic**：通配符匹配
- **Headers**：属性匹配

### 25. ⭐ RabbitMQ 持久化

| 设置 | 说明 |
|------|------|
| Exchange durable=true | 交换机持久化 |
| Queue durable=true | 队列持久化 |
| Message persistenceMode= true | 消息持久化 |

### 26. ⭐ 消息堆积处理

**原因**：消费者处理速度 < 生产者发送速度

**解决方案**：
1. 扩容消费者（集群）
2. 增加消费者线程
3. 批量消费优化
4. 丢弃旧消息（优先级队列）
5. 迁移到离线处理

### 27. ⭐ 消息事务

**RocketMQ 事务消息流程**：
```
1. 发送半消息（prepare）
2. 执行本地事务
3. 确认消息（commit/rollback）
```

```java
// RocketMQ 事务消息示例
@Transactional
public void placeOrder() {
    // 1. 发送半消息
    transactionProducer.sendMessageInTransaction("order-topic", order, user);

    // 2. 本地事务（创建订单）
    orderService.createOrder(order);
}

// TransactionListener 实现
@Override
public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
    try {
        orderService.createOrder((Order) arg);
        return LocalTransactionState.COMMIT_MESSAGE;
    } catch (Exception e) {
        return LocalTransactionState.ROLLBACK_MESSAGE;
    }
}
```

### 28. ⭐ Kafka 和 RabbitMQ 对比

| 对比 | Kafka | RabbitMQ |
|------|-------|----------|
| 吞吐量 | 极高（百万级） | 万级 |
| 延迟 | 低 | 低 |
| 可靠性 | at-least-once | at-least-once |
| 事务 | 支持 | 不支持（仅支持确认） |
| 顺序消息 | 单分区有序 | 支持 |
| 负载均衡 | 分区副本 | 多节点镜像 |

---

## 四、MyBatis

### 29. ⭐ MyBatis 工作流程

```
1. 加载配置（XML 或注解）
2. 创建 SqlSessionFactory
3. 创建 SqlSession
4. 获取 Mapper 代理对象
5. 执行 Mapper 方法
6. 返回结果
```

### 30. ⭐ #{} 和 ${} 的区别

| 符号 | 说明 | 安全性 |
|------|------|--------|
| #{} | 预编译，参数替换为占位符 | 防 SQL 注入 |
| ${} | 直接拼接 | 有 SQL 注入风险 |

```sql
-- #{} 生成 PreparedStatement
SELECT * FROM user WHERE id = ?

-- ${} 直接拼接
SELECT * FROM user WHERE name = 'zhangsan'
```

**使用场景**：
- #{}：大多数场景
- ${}：表名/列名动态确定时（如分表）

### 31. ⭐ MyBatis 缓存

| 缓存 | 说明 |
|------|------|
| 一级缓存 | SqlSession 级，本地缓存 |
| 二级缓存 | Mapper 级，跨 SqlSession |

**一级缓存**：
- 默认开启
- 作用域为 SqlSession
- 执行 update/close/clearCache 会清空

**二级缓存**：
- 需要开启 `<cache/>`
- 作用域为 Mapper namespace
- 可配合 Redis 使用

```xml
<!-- 开启二级缓存 -->
<cache eviction="LRU" flushInterval="60000" size="512" readOnly="true"/>
```

### 32. ⭐ MyBatis 延迟加载

**原理**：使用 CGLIB 创建代理对象，调用 getter 时才真正查询

```xml
<!-- 全局配置 -->
<settings>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="aggressiveLazyLoading" value="false"/>
</settings>

<!-- association 延迟加载 -->
<association property="dept" column="dept_id" select="findDeptById"/>
```

### 33. ⭐ MyBatis 插件原理

**四大对象**：
- Executor（执行器）
- StatementHandler（SQL 语句处理）
- ParameterHandler（参数处理）
- ResultSetHandler（结果处理）

**插件机制**：通过拦截以上对象，包装目标对象

```java
// 分页插件示例
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
})
public class PageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截逻辑
        return invocation.proceed();
    }
}
```

### 34. ⭐ Mapper 扫描原理

1. 解析配置，获取 Mapper 接口位置
2. 使用 JDK 动态代理创建 Mapper 代理对象
3. 注册到 Spring 容器

```java
@MapperScan("com.example.mapper")
public class MyBatisConfig {
}
```

### 35. MyBatis 一对一、一对多映射

```xml
<!-- 一对一 association -->
<resultMap id="OrderDetailMap" type="Order">
    <association property="user" javaType="User">
        <id property="id" column="user_id"/>
        <result property="name" column="user_name"/>
    </association>
</resultMap>

<!-- 一对多 collection -->
<resultMap id="UserMap" type="User">
    <collection property="orders" ofType="Order">
        <id property="id" column="order_id"/>
        <result property="amount" column="amount"/>
    </collection>
</resultMap>
```

---

## 五、MySQL

### 36. ⭐ InnoDB vs MyISAM

| 对比 | InnoDB | MyISAM |
|------|--------|--------|
| 事务 | 支持 | 不支持 |
| 外键 | 支持 | 不支持 |
| 锁粒度 | 行锁 | 表锁 |
| 崩溃恢复 | 自动恢复 | 不支持 |
| 全文索引 | 5.6+ 支持 | 原生支持 |
| 适用场景 | 写入多、并发 | 读取多 |

### 37. ⭐ 索引数据结构

| 类型 | 说明 |
|------|------|
| B+ Tree | InnoDB 默认，多路平衡查找树 |
| Hash | Memory 引擎，精确查询快 |
| Full-text | 全文索引 |

**B+ Tree 特点**：
- 所有数据在叶子节点
- 叶子节点链表连接，范围查询快
- 树高稳定（3-4 层可存千万数据）

### 38. ⭐ 索引失效情况

| 场景 | 说明 |
|------|------|
| %开头 LIKE | 无法利用 B+ 树 |
| 函数/运算 | 列参与运算 |
| 类型转换 | 类型不匹配 |
| OR | 部分情况失效 |
| 最左前缀 | 复合索引不满足最左前缀 |
| NULL/NOT NULL | 可能导致全表扫描 |

```sql
-- 索引 idx(name, age)
-- 失效
SELECT * FROM user WHERE age = 20;
SELECT * FROM user WHERE name LIKE '%zhang';

-- 有效
SELECT * FROM user WHERE name = 'zhang';
SELECT * FROM user WHERE name = 'zhang' AND age = 20;
```

### 39. ⭐ MVCC 原理

**MVCC（多版本并发控制）**：通过版本链 + ReadView 实现。

**隐藏列**：
- DB_TRX_ID：最近修改的事务 ID
- DB_ROLL_PTR：回滚指针
- DB_ROW_ID：隐含主键

**ReadView**：
- m_ids：活跃事务列表
- min_trx_id：最小活跃事务 ID
- max_trx_id：创建 ReadView 时最大事务 ID

**快照读 vs 当前读**：
- 快照读：读取历史版本（SELECT）
- 当前读：读取最新版本（SELECT...FOR UPDATE）

### 40. ⭐ 事务隔离级别

| 级别 | 脏读 | 不可重复读 | 幻读 |
|------|------|------------|------|
| READ UNCOMMITTED | 可能 | 可能 | 可能 |
| READ COMMITTED | 不可能 | 可能 | 可能 |
| REPEATABLE READ（默认） | 不可能 | 不可能 | 可能 |
| SERIALIZABLE | 不可能 | 不可能 | 不可能 |

### 41. ⭐ InnoDB 锁

| 锁类型 | 说明 |
|--------|------|
| 共享锁（S） | 允许读，阻塞写 |
| 排他锁（X） | 允许写，阻塞读写 |
| 记录锁 | 锁单条记录 |
| 间隙锁 | 锁记录间隙，防幻读 |
| Next-Key Lock | 记录锁+间隙锁 |

**死锁处理**：
- 超时自动回滚
- 等待图检测，主动回滚

### 42. ⭐ SQL 优化方法

1. **避免 SELECT ***
2. **合理创建索引**：区分度高、适量
3. **分页优化**：延迟关联
4. **批量操作**：减少数据库交互
5. **避免子查询**：用 JOIN 替代
6. **EXPLAIN 分析**：检查执行计划

```sql
-- 分页优化
-- 低效
SELECT * FROM user LIMIT 1000000, 10;

-- 高效（延迟关联）
SELECT * FROM user u
INNER JOIN (
    SELECT id FROM user ORDER BY id LIMIT 1000000, 10
) t ON u.id = t.id;
```

---

## 六、Redis

### 43. ⭐ Redis 数据类型

| 类型 | 命令示例 | 应用场景 |
|------|----------|----------|
| String | SET/GET | 缓存、计数器 |
| Hash | HSET/HGET | 对象存储 |
| List | LPUSH/RPOP | 队列、列表 |
| Set | SADD/SMEMBERS | 去重、标签 |
| ZSet | ZADD/ZRANGE | 排行榜 |
| Bitmap | SETBIT | 签到、状态 |
| HyperLogLog | PFADD | UV 统计 |

### 44. ⭐ Redis 持久化

| 方式 | 说明 |
|------|------|
| RDB | 定时快照，fork 子进程 |
| AOF | 记录每次写命令 |

**对比**：

| 对比 | RDB | AOF |
|------|-----|-----|
| 文件大小 | 小 | 大 |
| 恢复速度 | 快 | 慢 |
| 数据完整性 | 可能丢失数据 | 可配置 |
| 性能 | 好 | 需要 fsync |

### 45. ⭐ Redis 缓存问题

| 问题 | 描述 | 解决方案 |
|------|------|----------|
| 穿透 | 查询不存在的数据 | 布隆过滤器、缓存空值 |
| 击穿 | 热点 key 过期瞬间大量请求 | 互斥锁、热点永不过期 |
| 雪崩 | 大量 key 同时过期 | 随机过期时间、分布式锁 |

### 46. ⭐ Redis 集群方案

| 方案 | 说明 |
|------|------|
| 主从复制 | 一主多从，读写分离 |
| Sentinel | 主从 + 自动故障转移 |
| Cluster | 数据分片（16384 槽） |

### 47. ⭐ Redis 分布式锁

```java
// SETNX 方式（不推荐，有死锁风险）
String result = jedis.set(lockKey, value, "NX", "PX", expireTime);

// Redisson 方式（推荐）
RLock lock = redisson.getLock(lockKey);
try {
    lock.lock(10, TimeUnit.SECONDS);
    // 业务逻辑
} finally {
    lock.unlock();
}
```

### 48. ⭐ Redis 缓存与数据库一致性

| 策略 | 说明 |
|------|------|
| Cache Aside | 读：先缓存后数据库；写：先数据库后缓存 |
| Read Through | 缓存负责读写数据库 |
| Write Through | 同步写缓存和数据库 |
| Write Behind | 异步批量写数据库 |

---

## 七、分布式

### 49. ⭐ CAP 理论

分布式系统最多只能同时满足：
- **C（Consistency）**：一致性
- **A（Availability）**：可用性
- **P（Partition tolerance）**：分区容错性

**常见选择**：
- CP：Zookeeper、Nacos（CP 模式）
- AP：Eureka、Nacos（AP 模式）

### 50. ⭐ BASE 理论

- **Basically Available**：基本可用
- **Soft State**：软状态
- **Eventually Consistent**：最终一致

### 51. ⭐ 负载均衡算法

| 算法 | 说明 |
|------|------|
| 轮询 | 逐个分配 |
| 加权轮询 | 按权重分配 |
| 随机 | 随机选择 |
| 哈希 | 相同 key 同一节点 |
| 最小连接 | 选择连接数最少 |

### 52. ⭐ 一致性哈希

**问题**：普通哈希扩容时大量数据需要迁移

**解决**：环形哈希空间 + 虚拟节点

```
         hash(node1)
            ↓
hash(node3) ← hash(0~2^32)
            ↑
         hash(node2)
```

### 53. ⭐ 接口幂等性

**场景**：重复提交、网络重试

**解决方案**：
1. **唯一索引**：数据库唯一键
2. **Token 机制**：提交前获取 token，提交时校验
3. **分布式锁**：Redis 锁
4. **状态机**：订单状态流转

```java
// Token 方式
if (!redisTemplate.delete("order:token:" + token)) {
    throw new BizException("重复提交");
}
```

### 54. ⭐ 分布式 ID 生成

| 方案 | 说明 |
|------|------|
| UUID | 字符串，无序 |
| 数据库自增 | 需要独立 DB |
| Redis INCR | 依赖 Redis |
| 雪花算法（Snowflake） | 时间 + 机器 + 序列号 |

```java
// 雪花算法结构
// 1bit(符号) + 41bit(时间戳) + 10bit(机器ID) + 12bit(序列号)
public class SnowflakeIdGenerator {
    private final long twepoch = 1609459200000L;
    private final long workerIdBits = 10L;
    private final long sequenceBits = 12L;

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis() - twepoch;
        return (timestamp << 22) | (workerId << 12) | sequence++;
    }
}
```

---

## 八、常用工具

### 55. Git 常用命令

```bash
# 基本操作
git add .                    # 暂存
git commit -m "message"      # 提交
git push                     # 推送
git pull                     # 拉取

# 分支操作
git branch                   # 查看分支
git checkout -b feature      # 创建并切换
git merge feature            # 合并分支
git rebase feature           # 变基

# 撤销操作
git reset --soft HEAD~1     # 撤销 commit（保留更改）
git reset --hard HEAD~1     # 撤销 commit（删除更改）
git checkout -- file         # 撤销文件更改
```

### 56. Docker 常用命令

```bash
# 镜像操作
docker images               # 列出镜像
docker pull nginx           # 拉取镜像
docker build -t myapp .     # 构建镜像

# 容器操作
docker ps                   # 运行中容器
docker run -d -p 8080:80 nginx  # 运行容器
docker stop container_id     # 停止容器
docker exec -it container_id bash  # 进入容器

# 日志
docker logs -f container_id  # 查看日志
```

### 57. Linux 常用命令

```bash
# 文件操作
ls -la                      # 列出文件
cd /path                    # 切换目录
tail -f app.log             # 实时查看日志

# 进程/网络
ps -ef | grep java          # 查看进程
netstat -tlnp | grep 8080   # 查看端口占用
curl http://localhost:8080  # 测试接口

# 性能监控
top                         # 查看 CPU/内存
df -h                       # 磁盘使用
free -h                     # 内存使用
```
