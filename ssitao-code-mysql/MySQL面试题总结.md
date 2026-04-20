# MySQL 面试题总结

## 目录
1. [基础概念](#1-基础概念)
2. [索引](#2-索引)
3. [事务](#3-事务)
4. [锁](#4-锁)
5. [SQL 优化](#5-sql-优化)
6. [存储引擎](#6-存储引擎)
7. [高可用与集群](#7-高可用与集群)

---

## 1. 基础概念

### Q1: MySQL 的整体架构是怎样的？

```
┌─────────────────────────────────────────────────────────┐
│                    MySQL 架构                            │
├─────────────────────────────────────────────────────────┤
│  连接层                                                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │ Connection│  │ Connection│  │ Connection│  ...      │
│  │  Pool    │  │  Pool    │  │  Pool    │              │
│  └──────┬───┘  └──────┬───┘  └──────┬───┘              │
│         └────────────┼────────────┘                    │
│                      ▼                                   │
│  ┌────────────────────────────────────────┐             │
│  │           Server Layer                  │             │
│  │  ┌─────────┐ ┌─────────┐ ┌─────────┐  │             │
│  │  │   SQL   │ │ 优化器  │ │  执行器 │  │             │
│  │  │  Parser │ │         │ │         │  │             │
│  │  └─────────┘ └─────────┘ └─────────┘  │             │
│  └──────────────────┬────────────────────┘             │
│                     ▼                                   │
│  ┌────────────────────────────────────────┐             │
│  │           Storage Engine Layer         │             │
│  │  ┌────────┐  ┌────────┐  ┌────────┐   │             │
│  │  │ InnoDB │  │ MyISAM │  │ Memory │   │             │
│  │  └────────┘  └────────┘  └────────┘   │             │
│  └────────────────────────────────────────┘             │
└─────────────────────────────────────────────────────────┘
```

**组件说明**：
- **连接层**：连接池、认证、线程处理
- **Server 层**：SQL 解析、优化、执行
- **存储引擎层**：数据存储和读取

### Q2: char 和 varchar 的区别？

| 特性 | char | varchar |
|------|------|---------|
| 存储方式 | 固定长度 | 可变长度 |
| 存储效率 | 高 | 低（有额外开销） |
| 存储空间 | 固定 | 实际长度 + 1~2 字节 |
| 尾部空格 | 补空格存储 | 保留 |
| 适用场景 | 固定长度（密码、身份证） | 可变长度（姓名、地址） |

```sql
char(10)  -- 始终占用 10 字节
varchar(10) -- 占用 1-10 字节 + 1-2 字节长度标识
```

### Q3: int(10) 和 int(11) 的区别？

**没有区别**。括号内的数字只是显示宽度，不影响存储范围。

```sql
int(1)   -- 存储范围：-2147483648 ~ 2147483647
int(10)  -- 存储范围：-2147483648 ~ 2147483647
int(11)  -- 存储范围：-2147483648 ~ 2147483647
```

### Q4: datetime 和 timestamp 的区别？

| 特性 | datetime | timestamp |
|------|----------|-----------|
| 存储范围 | 1000-01-01 ~ 9999-12-31 | 1970-01-01 ~ 2038-01-19 |
| 时区处理 | 不转换 | 自动转换时区 |
| 存储大小 | 8 字节 | 4 字节 |
| 显示格式 | YYYY-MM-DD HH:MM:SS | YYYY-MM-DD HH:MM:SS |

### Q5: drop、delete、truncate 的区别？

| 操作 | 类型 | 回滚 | 空间释放 | 速度 |
|------|------|------|----------|------|
| drop | DDL | ❌ 不可回滚 | 立即释放 | 快 |
| truncate | DDL | ❌ 不可回滚 | 释放，保留结构 | 快 |
| delete | DML | ✅ 可回滚 | 不立即释放 | 慢 |

```sql
-- delete 删除数据可以回滚，支持 where 条件
DELETE FROM table WHERE id = 1;

-- truncate 删除所有数据，不可回滚
TRUNCATE TABLE table;

-- drop 删除整个表，不可回滚
DROP TABLE table;
```

---

## 2. 索引

### Q6: 索引是什么？有什么作用？

**索引**是对数据库表中一列或多列的值进行排序的数据结构，用于加速数据检索。

**优点**：
- 加快数据查询速度
- 减少磁盘 I/O
- 保证数据唯一性

**缺点**：
- 占用磁盘空间
- 增加写操作成本
- 降低更新效率

### Q7: MySQL 有哪些索引类型？

```
索引类型
├── 主键索引 (Primary Key)
│   └── 唯一且非空，每个表只能有一个
├── 唯一索引 (Unique)
│   └── 值唯一，可为空
├── 普通索引 (Index)
│   └── 无限制
├── 全文索引 (Full Text)
│   └── 文本搜索
└── 组合索引 (Composite)
    └── 多列组合
```

### Q8: 主键索引和唯一索引的区别？

| 特性 | 主键索引 | 唯一索引 |
|------|----------|----------|
| 数量 | 每个表只能一个 | 可以有多个 |
| 值唯一 | 必须唯一 | 必须唯一 |
| 空值 | 不允许 | 允许（多个 null） |
| 用途 | 唯一标识记录 | 保证数据唯一性 |
| 自动创建 | 是（若未显式定义） | 否 |

### Q9: 什么情况下索引会失效？

```sql
-- 1. 使用函数或运算
SELECT * FROM user WHERE YEAR(birthday) = 2000;  -- 失效
SELECT * FROM user WHERE age + 1 = 20;            -- 失效

-- 2. 类型转换
SELECT * FROM user WHERE name = 123;              -- name 是 varchar

-- 3. like 开头使用通配符
SELECT * FROM user WHERE name LIKE '%张%';        -- 失效
SELECT * FROM user WHERE name LIKE '张%';         -- 有效

-- 4. OR 连接不同字段
SELECT * FROM user WHERE name = '张三' OR age = 20;  -- 部分失效

-- 5. 最左前缀原则
CREATE INDEX idx_name_age ON user(name, age);
SELECT * FROM user WHERE age = 20;                -- 失效
SELECT * FROM user WHERE name = '张三';           -- 有效

-- 6. 使用 NOT IN、NOT EXISTS
SELECT * FROM user WHERE id NOT IN (1, 2, 3);     -- 可能失效

-- 7. IS NULL
SELECT * FROM user WHERE name IS NULL;            -- 可能失效
```

### Q10: 最左前缀原则是什么？

组合索引 `idx(name, age, gender)` 的最左前缀匹配：

| 查询条件 | 是否使用索引 |
|----------|-------------|
| WHERE name = '张三' | ✅ 使用 |
| WHERE name = '张三' AND age = 20 | ✅ 使用 |
| WHERE name = '张三' AND age = 20 AND gender = 1 | ✅ 使用 |
| WHERE age = 20 | ❌ 不使用 |
| WHERE gender = 1 | ❌ 不使用 |
| WHERE age = 20 AND gender = 1 | ❌ 不使用 |

### Q11: 索引为什么要用 B+ 树而不是 B 树？

```
B 树（每个节点都存储数据）
┌─────────────────────────────────┐
│  [10 20] │ [30 40] │ [50 60]    │  ← 节点存储数据
│    ↓        ↓        ↓          │
└─────────────────────────────────┘
         ↓           ↓
    [15 18]     [35 38]

B+ 树（只有叶子节点存储数据）
                    ┌─────────┐
                    │ 20 │ 40 │
                    └──┬──┴──┬─┘
                       ↓    ↓
        ┌─────────┐  ┌─────────┐
        │ 10 │ 15 │  │ 30 │ 35 │  ← 中间节点不存数据
        └────┬────┘  └────┬────┘
             ↓           ↓
        ┌─────────────────────────┐
        │ 10 │ 15 │ 20 │ 30 │ 35 │  ← 叶子节点存储全部数据
        └─────────────────────────┘
```

**B+ 树的优点**：
1. **查询稳定**：所有查询都要到叶子节点
2. **范围查询快**：叶子节点用链表连接
3. **磁盘读写少**：非叶子节点不存数据，可以容纳更多索引
4. **查询效率高**：IO 次数 = 树的高度

### Q12: 索引在什么情况下会被触发？

```sql
-- 使用 EXPLAIN 分析
EXPLAIN SELECT * FROM user WHERE name = '张三';

-- type 列显示查询类型
-- const > eq_ref > ref > range > index > ALL
-- const: 主键/唯一索引等值查询
-- ref: 非唯一索引等值查询
-- range: 索引范围查询
-- ALL: 全表扫描（索引失效）
```

---

## 3. 事务

### Q13: 什么是事务？有哪些特性？

**事务**是数据库操作的最小工作单元，要么全部成功，要么全部失败。

**ACID 特性**：

| 特性 | 说明 |
|------|------|
| **A**tomic（原子性） | 事务是不可分割的操作 |
| **C**onsistent（一致性） | 事务执行前后数据保持一致 |
| **I**solated（隔离性） | 并发执行时相互隔离 |
| **D**urable（持久性） | 提交后数据永久保存 |

### Q14: 事务的隔离级别有哪些？

| 隔离级别 | 脏读 | 不可重复读 | 幻读 | 实现方式 |
|----------|------|-----------|------|----------|
| READ UNCOMMITTED | ✅ 可能 | ✅ 可能 | ✅ 可能 | 无 |
| READ COMMITTED | ❌ 不可能 | ✅ 可能 | ✅ 可能 | MVCC |
| REPEATABLE READ | ❌ 不可能 | ❌ 不可能 | ✅ 可能 | MVCC + 间隙锁 |
| SERIALIZABLE | ❌ 不可能 | ❌ 不可能 | ❌ 不可能 | 锁 |

**MySQL 默认隔离级别**：REPEATABLE READ

### Q15: 脏读、不可重复读、幻读的区别？

```
脏读：读取到其他事务未提交的数据
T1: 开启事务，修改数据（未提交）
T2: 读取数据 → 读取到 T1 修改的数据
T1: 回滚事务
T2: 读取的数据是错误的（脏读）

不可重复读：同一事务两次读取结果不同
T1: 开启事务，读取数据
T2: 另一个事务修改并提交
T1: 再次读取 → 数据不同（不可重复读）

幻读：同一事务两次查询结果记录数不同
T1: 开启事务，查询全表共 10 条
T2: 另一个事务插入一条新记录并提交
T1: 再次查询 → 共 11 条（幻读）
```

### Q16: MVCC 是什么？

**MVCC (Multi-Version Concurrency Control)** 多版本并发控制。

```
原理：每个事务看到的是数据库的某个快照

事务 A (id=100, name='张三')  ──┐
事务 B (id=100, name='李四')  ──┼── 读取到的是不同版本
事务 C (提交)                  ──┘

隐藏列：
- trx_id: 事务ID
- roll_pointer: 回滚指针
- DB_ROW_ID: 行ID
```

**READ COMMITTED**：每次 SELECT 都生成新快照
**REPEATABLE READ**：同一事务中所有 SELECT 使用同一快照

### Q17: 如何开启和关闭事务？

```sql
-- 开启事务
START TRANSACTION;
BEGIN;

-- 提交事务
COMMIT;

-- 回滚事务
ROLLBACK;

-- 设置保存点
SAVEPOINT savepoint_name;

-- 回滚到保存点
ROLLBACK TO savepoint_name;

-- 开启自动提交（MySQL 默认）
SET autocommit = 1;

-- 关闭自动提交
SET autocommit = 0;
```

---

## 4. 锁

### Q18: MySQL 有哪些锁？

```
锁类型
├── 按操作分类
│   ├── 读锁（共享锁）
│   └── 写锁（排他锁）
├── 按粒度分类
│   ├── 表锁
│   ├── 行锁
│   └── 页锁
└── 按特性分类
    ├── 意向锁
    └── 自增锁
```

### Q19: 表锁和行锁的区别？

| 特性 | 表锁 | 行锁 |
|------|------|------|
| 粒度 | 大 | 小 |
| 开销 | 小 | 大（需要维护） |
| 并发度 | 低 | 高 |
| 冲突 | 多 | 少 |
| 加锁速度 | 快 | 慢 |

### Q20: 什么是意向锁？

**意向锁**是表级锁，表示事务要在表中某行加锁。

| 锁 | 作用 |
|---|------|
| IS | 意图获取共享锁 |
| IX | 意图获取排他锁 |

**作用**：快速判断表是否有行锁，避免全表扫描。

### Q21: InnoDB 行锁的实现方式？

```sql
-- 记录锁（Record Lock）
SELECT * FROM user WHERE id = 1 FOR UPDATE;  -- 锁定 id=1 的行

-- 间隙锁（Gap Lock）
SELECT * FROM user WHERE id > 1 AND id < 10 FOR UPDATE;  -- 锁定 (1, 10) 区间

-- Next-Key Lock（记录锁 + 间隙锁）
SELECT * FROM user WHERE id >= 1 AND id <= 10 FOR UPDATE;  -- 锁定 [1, 10] 区间
```

### Q22: 死锁是什么？如何避免？

**死锁**：两个或多个事务相互持有对方需要的锁。

```sql
-- 死锁示例
事务 A: SELECT * FROM user WHERE id=1 FOR UPDATE;  -- 锁住 id=1
事务 B: SELECT * FROM user WHERE id=2 FOR UPDATE;  -- 锁住 id=2
事务 A: SELECT * FROM user WHERE id=2 FOR UPDATE;  -- 等待 id=2
事务 B: SELECT * FROM user WHERE id=1 FOR UPDATE;  -- 等待 id=1 → 死锁
```

**避免死锁的方法**：
1. 按固定顺序访问表
2. 减少锁的持有时间
3. 使用低隔离级别
4. 设置锁等待超时

```sql
-- 查看死锁日志
SHOW ENGINE INNODB STATUS;

-- 设置锁等待超时（毫秒）
SET innodb_lock_wait_timeout = 50;
```

### Q23: 什么是乐观锁和悲观锁？

```sql
-- 乐观锁：版本号控制
UPDATE user SET name='张三', version=version+1
WHERE id=1 AND version=1;  -- 失败则重试

-- 悲观锁：先加锁
SELECT * FROM user WHERE id=1 FOR UPDATE;  -- 锁定这行
-- 业务处理
UPDATE user SET name='张三' WHERE id=1;
```

| 特性 | 乐观锁 | 悲观锁 |
|------|--------|--------|
| 实现方式 | 版本号/CAS | 数据库锁 |
| 适用场景 | 冲突少 | 冲突多 |
| 性能 | 好（无锁） | 差（有锁） |
| 实现复杂度 | 高 | 低 |

---

## 5. SQL 优化

### Q24: 如何分析 SQL 性能？

```sql
-- EXPLAIN 分析执行计划
EXPLAIN SELECT * FROM user WHERE name = '张三';

-- 分析结果字段
-- type: 查询类型（const, ref, range, ALL）
-- key: 使用的索引
-- rows: 扫描行数
-- extra: 额外信息（Using filesort, Using index）

-- PROFILING 分析
SET profiling = 1;
SELECT * FROM user WHERE name = '张三';
SHOW PROFILES;
```

### Q25: SQL 执行顺序是什么？

```sql
SELECT DISTINCT
       field1, field2, field3
FROM   table1
JOIN   table2 ON condition
WHERE  condition
GROUP BY field1
HAVING count(*) > 1
ORDER BY field2 DESC
LIMIT 10;
```

**执行顺序**：
```
1. FROM          -- 确定表
2. JOIN          -- 连接表
3. WHERE         -- 过滤条件
4. GROUP BY      -- 分组
5. HAVING        -- 分组后过滤
6. SELECT        -- 选择字段（包括 DISTINCT）
7. ORDER BY      -- 排序
8. LIMIT         -- 限制条数
```

### Q26: 如何优化慢查询？

1. **使用索引**
```sql
-- 添加索引
CREATE INDEX idx_name ON user(name);

-- 复合索引
CREATE INDEX idx_name_age ON user(name, age);
```

2. **避免全表扫描**
```sql
-- 避免
SELECT * FROM user WHERE name IS NULL;

-- 使用索引
SELECT * FROM user WHERE id = 1;
```

3. **优化 LIMIT**
```sql
-- 低效
SELECT * FROM user ORDER BY id LIMIT 100000, 10;

-- 高效（游标方式）
SELECT * FROM user WHERE id > 100000 ORDER BY id LIMIT 10;
```

4. **避免子查询**
```sql
-- 低效
SELECT * FROM user WHERE id IN (SELECT user_id FROM orders);

-- 高效（JOIN）
SELECT u.* FROM user u JOIN orders o ON u.id = o.user_id;
```

### Q27: 分页查询优化

```sql
-- 传统分页
SELECT * FROM user ORDER BY id LIMIT 100000, 10;

-- 优化方案1：基于 ID
SELECT * FROM user WHERE id > 100000 ORDER BY id LIMIT 10;

-- 优化方案2：延迟关联
SELECT u.* FROM user u
JOIN (SELECT id FROM user ORDER BY id LIMIT 100000, 10) t
ON u.id = t.id;

-- 优化方案3：游标分页
SELECT * FROM user WHERE id > #{lastId} ORDER BY id LIMIT 10;
```

### Q28: 如何优化大表？

1. **分表**
```sql
-- 水平分表：按 ID 范围
-- user_0: id 1-100万
-- user_1: id 100万-200万

-- 垂直分表：按字段
-- user_basic: id, name, email
-- user_detail: id, age, address, ...
```

2. **分区**
```sql
CREATE TABLE orders (
    id BIGINT,
    create_time DATETIME,
    ...
) PARTITION BY RANGE (YEAR(create_time)) (
    PARTITION p2020 VALUES LESS THAN (2020),
    PARTITION p2021 VALUES LESS THAN (2021),
    PARTITION p2022 VALUES LESS THAN (2022),
    PARTITION pmax VALUES LESS THAN MAXVALUE
);
```

3. **归档**
```sql
-- 归档历史数据到归档表
INSERT INTO orders_archive SELECT * FROM orders WHERE create_time < '2021-01-01';
DELETE FROM orders WHERE create_time < '2021-01-01';
```

---

## 6. 存储引擎

### Q29: MySQL 有哪些存储引擎？

| 引擎 | 事务 | 锁粒度 | 外键 | 索引 | 使用场景 |
|------|------|--------|------|------|----------|
| InnoDB | ✅ | 行锁 | ✅ | B+树 | 默认，通用场景 |
| MyISAM | ❌ | 表锁 | ❌ | B+树 | 读多写少 |
| Memory | ❌ | 表锁 | ❌ | Hash | 临时表，缓存 |
| Archive | ❌ | 行锁 | ❌ | B+树 | 日志，审计 |

### Q30: InnoDB 和 MyISAM 的区别？

| 特性 | InnoDB | MyISAM |
|------|--------|--------|
| 事务 | ✅ 支持 | ❌ 不支持 |
| 锁 | 行锁 | 表锁 |
| 外键 | ✅ 支持 | ❌ 不支持 |
| 全文索引 | ✅ 5.6+ 支持 | ✅ 原生支持 |
| 崩溃恢复 | ✅ 自动恢复 | ❌ 需手动 |
| 存储结构 | 表空间 | 三个文件 |
| 计数方式 | 实时（行锁） | 缓存（快） |

### Q31: InnoDB 的存储结构？

```
表空间 (TableSpace)
├── 段 (Segment)
│   ├── 数据段 (Leaf Segment)
│   ├── 索引段 (Non-Leaf Segment)
│   └── 回滚段 (Rollback Segment)
├── 区 (Extent) - 1MB (64个16KB页面)
│   └── 64 页面 (Page) - 16KB
└── 页面 (Page) - 16KB
    └── 行 (Row)
        ├── 记录头信息
        ├── 字段数据
        ├── 字段信息
        └── 事务ID、回滚指针
```

### Q32: 什么是 Change Buffer？

**Change Buffer** 是 InnoDB 将更新操作缓存在内存中，稍后再合并到磁盘。

适用场景：
- 非唯一索引的插入/更新
- 辅助索引的修改

```
用户更新索引页 → 内存缓冲区 → 后台线程合并 → 磁盘
```

### Q33: Redo Log 和 Binlog 的区别？

| 特性 | Redo Log | Binlog |
|------|----------|--------|
| 作用 | 保证持久性 | 主从复制、恢复 |
| 位置 | InnoDB 特有 | Server 层 |
| 内容 | 物理修改 | 逻辑修改（SQL） |
| 格式 | 物理日志 | 逻辑日志（statement/row/mixed） |
| 写入时机 | 事务提交前 | 事务提交时 |
| 格式 | 循环写入 | 追加写入 |

---

## 7. 高可用与集群

### Q34: 主从复制原理？

```
┌──────────┐                    ┌──────────┐
│   Master │ ─── Binlog ────▶ │   Slave  │
│          │   异步复制         │          │
│  写入    │                    │  读取    │
└──────────┘                    └──────────┘
```

**流程**：
1. Master 写数据到 Binlog
2. Slave IO Thread 读取 Binlog 到 Relay Log
3. Slave SQL Thread 重放 Relay Log

### Q35: 主从延迟的原因？

| 原因 | 解决方案 |
|------|----------|
| 大事务 | 拆分为小事务 |
| 网络延迟 | 优化网络 |
| 大量并发 | 减少并发，架构优化 |
| 慢 SQL | 优化 SQL |
| 硬件差异 | 保持一致 |

```sql
-- 查看从库延迟
SHOW SLAVE STATUS\G
```

### Q36: 如何保证高可用？

1. **主从切换**
2. **读写分离**
3. **分库分表**
4. **连接池**（HikariCP）

---

## 附录：面试常考 SQL

### Q37: 用 SQL 显示每个部门工资最高的员工

```sql
-- 方法1：子查询
SELECT d.name, e.name, e.salary
FROM employee e
JOIN department d ON e.dept_id = d.id
WHERE e.salary = (SELECT MAX(salary) FROM employee WHERE dept_id = e.dept_id);

-- 方法2：窗口函数
SELECT name, dept, salary
FROM (
    SELECT name, dept, salary,
           ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC) as rn
    FROM employee
) t
WHERE rn = 1;

-- 方法3：JOIN
SELECT d.name, e.name, e.salary
FROM employee e
JOIN department d ON e.dept_id = d.id
JOIN (SELECT dept_id, MAX(salary) as max_sal FROM employee GROUP BY dept_id) m
ON e.dept_id = m.dept_id AND e.salary = m.max_sal;
```

### Q38: 用 SQL 计算连续登录天数

```sql
-- 用户连续登录天数
SELECT user_id, COUNT(*) as days
FROM (
    SELECT user_id, login_date,
           DATE_SUB(login_date, INTERVAL ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) DAY) as grp
    FROM (
        SELECT DISTINCT user_id, DATE(login_time) as login_date
        FROM user_login
    ) t1
) t2
GROUP BY user_id, grp;
```

### Q39: 用 SQL 找出第二高的工资

```sql
-- 方法1：子查询
SELECT DISTINCT salary
FROM employee
ORDER BY salary DESC
LIMIT 1 OFFSET 1;

-- 方法2：MAX
SELECT MAX(salary)
FROM employee
WHERE salary < (SELECT MAX(salary) FROM employee);
```