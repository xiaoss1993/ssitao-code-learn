# MySQL 学习指南

## 目录
1. [MySQL 架构](#1-mysql-架构)
2. [存储引擎](#2-存储引擎)
3. [索引详解](#3-索引详解)
4. [事务与锁](#4-事务与锁)
5. [SQL 优化](#5-sql-优化)
6. [运维实战](#6-运维实战)

---

## 1. MySQL 架构

### 1.1 整体架构图

```
┌──────────────────────────────────────────────────────────────┐
│                         MySQL Server                          │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐  │
│  │                    连接层                                │  │
│  │  ┌─────────┐  ┌─────────┐  ┌─────────┐                 │  │
│  │  │连接池   │  │连接池   │  │连接池   │  ...           │  │
│  │  └────┬────┘  └────┬────┘  └────┬────┘                 │  │
│  └───────┼────────────┼────────────┼──────────────────────┘  │
│          └────────────┼────────────┘                         │
│                       ▼                                       │
│  ┌────────────────────────────────────────────────────────┐  │
│  │                   Server 层                            │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐             │  │
│  │  │ SQL Parser│ │ 优化器   │  │ 执行器   │             │  │
│  │  └──────────┘  └──────────┘  └──────────┘             │  │
│  └──────────────────────┬───────────────────────────────┘  │
│                          ▼                                   │
│  ┌────────────────────────────────────────────────────────┐  │
│  │              存储引擎层 (Plugin)                        │  │
│  │  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐         │  │
│  │  │ InnoDB │ │MyISAM │ │Memory  │ │Archive │         │  │
│  │  └────────┘ └────────┘ └────────┘ └────────┘         │  │
│  └────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────┘
                              ↓
                    ┌─────────────────┐
                    │     磁盘文件     │
                    │  .frm .ibd .MYD │
                    └─────────────────┘
```

### 1.2 连接层

**职责**：
- 连接管理（连接/断开）
- 认证（用户名密码验证）
- 线程处理（创建/回收线程）
- 连接池（复用连接）

**配置参数**：
```ini
[mysqld]
max_connections=2000           # 最大连接数
wait_timeout=28800             # 空闲连接超时
interactive_timeout=28800       # 交互超时
```

### 1.3 Server 层

**组件**：
1. **SQL Parser**：解析 SQL 语句
2. **Preprocessor**：语义检查
3. **Optimizer**：生成执行计划
4. **Executor**：执行计划

**工作流程**：
```
SQL → Parser → Preprocessor → Optimizer → Executor → 存储引擎
```

### 1.4 存储引擎层

| 引擎 | 特点 | 适用场景 |
|------|------|----------|
| InnoDB | 事务支持、行锁、外键 | 默认，OLTP |
| MyISAM | 表锁、无事务 | OLAP，读多写少 |
| Memory | 内存存储 | 临时表、缓存 |
| Archive | 压缩存储 | 日志、审计 |

---

## 2. 存储引擎

### 2.1 InnoDB 存储结构

```
Tablespace (表空间)
│
├── Segment (段) - 数据段/索引段/回滚段
│   │
│   ├── Extent (区) - 1MB = 64 Page
│   │   │
│   │   └── Page (页) - 16KB
│   │       │
│   │       └── Row (行)
│   │           ├── 行头信息 (6 bytes)
│   │           ├── 列数据
│   │           ├── 列信息
│   │           ├── trx_id (事务ID)
│   │           └── roll_pointer (回滚指针)
│   │
└── 回滚段
```

### 2.2 InnoDB vs MyISAM

| 特性 | InnoDB | MyISAM |
|------|--------|--------|
| 事务 | ✅ ACID | ❌ |
| 锁粒度 | 行锁 | 表锁 |
| 外键 | ✅ | ❌ |
| 全文索引 | ✅ (5.6+) | ✅ |
| 崩溃恢复 | 自动 | 手动 |
| 存储文件 | .ibd (表空间) | .frm .MYD .MYI |
| 计数 | 实时准确 | 缓存值 |
| 索引 | 聚簇索引 | 非聚簇索引 |

### 2.3 聚簇索引 vs 非聚簇索引

**聚簇索引**（InnoDB）：
```
叶子节点存储完整行数据

         ┌─────────────────────────┐
         │        索引页           │
         │   [id=1] [id=5] [id=9]   │
         └────────┬────────────────┘
                  │
    ┌─────────────┼─────────────┐
    ▼             ▼             ▼
┌────────┐   ┌────────┐   ┌────────┐
│ id=1   │   │ id=5   │   │ id=9   │
│行数据   │   │行数据   │   │行数据   │
│name=张 │   │name=李 │   │name=王 │
└────────┘   └────────┘   └────────┘
```

**非聚簇索引**（MyISAM）：
```
叶子节点存储行地址

         ┌─────────────────────────┐
         │        索引页           │
         │   [name=张] [name=李]     │
         └────────┬────────────────┘
                  │
         ┌────────┴────────┐
         ▼                 ▼
    ┌─────────┐       ┌─────────┐
    │行地址: 0x│       │行地址: 0x│
    └─────────┘       └─────────┘
         │                 │
         ▼                 ▼
    ┌─────────────────────────┐
    │        数据页           │
    │ [id=1, name=张, ...]    │
    └─────────────────────────┘
```

---

## 3. 索引详解

### 3.1 B+ 树结构

```
                    ┌─────────┐
                    │  根节点  │
                    │ 50 │ 100 │
                    └──┬──┴──┬─┘
                       ▼    ▼
        ┌─────────────┐     ┌─────────────┐
        │   索引页     │     │   索引页     │
        │ 20 | 35 | 48│     │ 60 | 80 | 95│
        └──────┬──────┘     └──────┬──────┘
               │                   │
    ┌──────────┼──────────┐ ┌──────┼──────┐
    ▼          ▼          ▼ ▼      ▼      ▼
┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐ ┌───────┐
│ 10-20 │ │ 20-35 │ │ 35-48 │ │ 48-60 │ │ 60-100│
│       │ │       │ │       │ │       │ │       │
└───────┘ └───────┘ └───────┘ └───────┘ └───────┘
    └───────────────┴───────────┘
              │
              ▼
    ┌─────────────────────────┐
    │        叶子节点          │
    │  [10] [20] [35] [48] [60] [80] [95] [100]
    │   │    │    │    │    │    │    │    │
    └───┼────┼────┼────┼────┼────┼────┼────┼────
        │    │    │    │    │    │    │    │
        ▼    ▼    ▼    ▼    ▼    ▼    ▼    ▼
    所有数据行，通过链表连接
```

### 3.2 索引分类

```sql
-- 主键索引（唯一且非空）
CREATE TABLE t1 (
    id INT PRIMARY KEY,
    ...
);

-- 唯一索引
CREATE UNIQUE INDEX idx_email ON user(email);

-- 普通索引
CREATE INDEX idx_name ON user(name);

-- 全文索引
CREATE FULLTEXT INDEX idx_content ON article(content);

-- 复合索引
CREATE INDEX idx_name_age ON user(name, age);
```

### 3.3 最左前缀原则

```sql
-- 创建复合索引
CREATE INDEX idx_name_age_gender ON user(name, age, gender);

-- 有效查询
SELECT * FROM user WHERE name = '张三';                    -- ✅ 使用
SELECT * FROM user WHERE name = '张三' AND age = 20;       -- ✅ 使用
SELECT * FROM user WHERE name = '张三' AND age = 20 AND gender = 1;  -- ✅ 使用

-- 无效查询
SELECT * FROM user WHERE age = 20;                        -- ❌ 不使用
SELECT * FROM user WHERE gender = 1;                      -- ❌ 不使用
SELECT * FROM user WHERE name LIKE '%张%';                 -- ❌ 不使用（开头通配符）
```

### 3.4 索引失效场景

```sql
-- 1. 使用函数
WHERE YEAR(create_time) = 2024          -- ❌
WHERE create_time + 1 = 100              -- ❌

-- 2. 类型转换
WHERE phone = 13800138000  -- phone 是 varchar  -- ❌

-- 3. OR 条件
WHERE name = '张三' OR age = 20           -- ❌（age 索引失效）

-- 4. NOT 操作
WHERE name NOT IN ('张三', '李四')       -- ❌
WHERE id NOT BETWEEN 1 AND 100          -- ❌

-- 5. LIKE 开头通配符
WHERE name LIKE '%张%'                   -- ❌

-- 6. 隐式转换
WHERE CAST(id AS CHAR) = '123'           -- ❌
```

### 3.5 覆盖索引

```sql
-- 覆盖索引：查询字段都在索引中，无需回表

-- 创建索引 (name, age)
CREATE INDEX idx_name_age ON user(name, age);

-- 只需要查询 name 和 age，使用覆盖索引
SELECT name, age FROM user WHERE name = '张三';  -- Using index

-- 需要查询所有字段，需要回表
SELECT * FROM user WHERE name = '张三';          -- Using index & using MRR
```

---

## 4. 事务与锁

### 4.1 事务控制

```sql
-- 开启事务
START TRANSACTION;
BEGIN;

-- 提交
COMMIT;

-- 回滚
ROLLBACK;

-- 设置保存点
SAVEPOINT sp1;

-- 回滚到保存点
ROLLBACK TO sp1;

-- 自动提交
SET autocommit = 0;  -- 关闭
SET autocommit = 1;  -- 开启
```

### 4.2 隔离级别

| 级别 | 脏读 | 不可重复读 | 幻读 |
|------|------|-----------|------|
| READ UNCOMMITTED | ✅ | ✅ | ✅ |
| READ COMMITTED | ❌ | ✅ | ✅ |
| REPEATABLE READ | ❌ | ❌ | ✅ |
| SERIALIZABLE | ❌ | ❌ | ❌ |

```sql
-- 查看当前隔离级别
SELECT @@tx_isolation;
SELECT @@transaction_isolation;

-- 设置隔离级别
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
```

### 4.3 MVCC 原理

```sql
-- 隐藏列
DESCRIBE user;
-- +---------------------+----------------------+------+-----+----------------+
-- | Field               | Type                 | Null | Key | Extra          |
-- +---------------------+----------------------+------+-----+----------------+
-- | id                  | int                  | NO   | PRI |                |
-- | name                | varchar(50)          | YES  |     |                |
-- | trx_id              | bigint unsigned      | NO   |     | DB_ROW_TRX_ID  |
-- | roll_pointer        | bigint unsigned      | NO   |     | DB_ROLL_PTR    |
-- +---------------------+----------------------+------+-----+----------------+
```

**快照读 vs 当前读**：
```sql
-- 快照读（普通 SELECT）
SELECT * FROM user WHERE id = 1;  -- MVCC 快照

-- 当前读（加锁）
SELECT * FROM user WHERE id = 1 FOR UPDATE;  -- 读取最新数据
INSERT INTO user VALUES(...);
UPDATE user SET ... WHERE id = 1;
DELETE FROM user WHERE id = 1;
```

### 4.4 锁类型

```sql
-- 共享锁 (S)
SELECT * FROM user WHERE id = 1 LOCK IN SHARE MODE;

-- 排他锁 (X)
SELECT * FROM user WHERE id = 1 FOR UPDATE;

-- 记录锁 (Record Lock)
SELECT * FROM user WHERE id = 1 FOR UPDATE;  -- 锁住 id=1

-- 间隙锁 (Gap Lock)
SELECT * FROM user WHERE id > 1 AND id < 10 FOR UPDATE;  -- 锁住 (1, 10)

-- Next-Key Lock
SELECT * FROM user WHERE id >= 1 AND id <= 10 FOR UPDATE;  -- 锁住 [1, 10]
```

### 4.5 死锁处理

```sql
-- 查看死锁日志
SHOW ENGINE INNODB STATUS;

-- 设置锁等待超时
SET innodb_lock_wait_timeout = 50;

-- 查看当前锁
SELECT * FROM information_schema.INNODB_LOCKS;
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
```

### 4.6 乐观锁实现

```sql
-- 版本号方式
UPDATE user
SET name = '张三', version = version + 1
WHERE id = 1 AND version = 1;

-- 时间戳方式
UPDATE user
SET name = '张三', update_time = NOW()
WHERE id = 1 AND update_time < '2024-01-01 00:00:00';
```

---

## 5. SQL 优化

### 5.1 EXPLAIN 分析

```sql
EXPLAIN SELECT * FROM user WHERE name = '张三';

-- 关键字段
-- type: const > eq_ref > ref > range > index > ALL
-- key: 实际使用的索引
-- rows: 预计扫描行数
-- extra: Using filesort / Using index / Using temporary
```

### 5.2 SQL 执行顺序

```
FROM → ON → JOIN → WHERE → GROUP BY → HAVING → SELECT → DISTINCT → ORDER BY → LIMIT
```

### 5.3 慢查询优化

```sql
-- 开启慢查询日志
SET slow_query_log = ON;
SET long_query_time = 1;

-- 查看慢查询
SHOW VARIABLES LIKE 'slow_query_log%';
SHOW VARIABLES LIKE 'long_query_time';

-- 分析慢查询日志
mysqldumpslow -s c -t 10 /var/log/mysql/slow.log
```

### 5.4 分页优化

```sql
-- 低效：偏移量过大会变慢
SELECT * FROM orders ORDER BY id LIMIT 100000, 10;

-- 优化1：基于 ID 游标
SELECT * FROM orders WHERE id > 100000 ORDER BY id LIMIT 10;

-- 优化2：延迟关联
SELECT o.* FROM orders o
INNER JOIN (SELECT id FROM orders ORDER BY id LIMIT 100000, 10) t
ON o.id = t.id;
```

### 5.5 索引优化

```sql
-- 1. 选择性高的列放前面
CREATE INDEX idx_status_created ON orders(status, created_at);

-- 2. 覆盖索引减少回表
CREATE INDEX idx_name_age ON user(name, age);

-- 3. 前缀索引（字符串列）
CREATE INDEX idx_phone ON user(phone(6));

-- 4. 联合索引覆盖查询
CREATE INDEX idx_name_age_gender ON user(name, age, gender);
SELECT name, age, gender FROM user WHERE name = '张三';  -- 覆盖索引
```

### 5.6 表设计优化

```sql
-- 1. 适当反范式化（冗余字段）
-- 订单表冗余用户名称，避免 JOIN

-- 2. 避免 select *
SELECT id, name FROM user WHERE id = 1;

-- 3. 批量操作
INSERT INTO user(id, name) VALUES (1, 'A'), (2, 'B'), (3, 'C');

-- 4. 使用 LIMIT 合理
SELECT * FROM user LIMIT 1000;  -- 限制单次查询量
```

---

## 6. 运维实战

### 6.1 主从复制配置

**Master 配置**：
```ini
[mysqld]
server-id = 1
log-bin = mysql-bin
sync-binlog = 1
binlog-do-db = mydb
```

**Slave 配置**：
```ini
[mysqld]
server-id = 2
relay-log = relay-bin
read-only = 1
```

**复制命令**：
```sql
-- Master: 创建复制账号
CREATE USER 'repl'@'%' IDENTIFIED BY 'password';
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';

-- Slave: 启动复制
CHANGE MASTER TO
    MASTER_HOST='master_host',
    MASTER_USER='repl',
    MASTER_PASSWORD='password',
    MASTER_LOG_FILE='mysql-bin.000001',
    MASTER_LOG_POS=123;

START SLAVE;
SHOW SLAVE STATUS\G
```

### 6.2 备份恢复

```bash
# 全量备份
mysqldump -u root -p --all-databases > backup.sql

# 备份指定库
mysqldump -u root -p dbname > dbname.sql

# 恢复
mysql -u root -p < backup.sql

# 基于时间点恢复
mysqlbinlog --stop-datetime='2024-01-01 12:00:00' binlog.000001 | mysql -u root -p
```

### 6.3 表空间清理

```sql
-- 重建表释放空间
OPTIMIZE TABLE user;

-- 收缩 InnoDB 表空间（5.6+）
ALTER TABLE user ENGINE = InnoDB;

-- 删除无用的索引
ALTER TABLE user DROP INDEX idx_unused;
```

### 6.4 连接池配置（HikariCP）

```java
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("root");
config.setPassword("password");
config.setMaximumPoolSize(20);
config.setMinimumIdle(5);
config.setConnectionTimeout(30000);
config.setIdleTimeout(600000);
config.setMaxLifetime(1800000);
```

### 6.5 常见问题处理

```sql
-- 连接数过多
SHOW STATUS LIKE 'Threads_connected';
SHOW PROCESSLIST;
KILL [process_id];

-- 锁等待
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
SELECT * FROM information_schema.INNODB_LOCKS;

-- 临时表/文件排序占用过多
SHOW STATUS LIKE 'Created_tmp%';
```

---

## 附录：SQL 练习题

### 练习1：部门工资最高的员工

```sql
-- 表结构
CREATE TABLE employee (
    id INT PRIMARY KEY,
    name VARCHAR(50),
    salary DECIMAL(10,2),
    dept_id INT
);

CREATE TABLE department (
    id INT PRIMARY KEY,
    name VARCHAR(50)
);

-- 答案
SELECT d.name AS dept, e.name, e.salary
FROM employee e
JOIN department d ON e.dept_id = d.id
WHERE (e.dept_id, e.salary) IN (
    SELECT dept_id, MAX(salary)
    FROM employee
    GROUP BY dept_id
);
```

### 练习2：连续登录天数

```sql
-- 表结构
CREATE TABLE user_login (
    user_id INT,
    login_date DATE
);

-- 答案（去除连续重复的日期）
SELECT user_id, COUNT(*) AS consecutive_days
FROM (
    SELECT user_id, login_date,
           DATE_SUB(login_date, INTERVAL rn DAY) AS grp
    FROM (
        SELECT user_id, login_date,
               ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) AS rn
        FROM (
            SELECT DISTINCT user_id, DATE(login_time) AS login_date
            FROM user_login
        ) t1
    ) t2
) t3
GROUP BY user_id, grp;
```

### 练习3：排名（窗口函数）

```sql
-- 排名（不跳过并列）
SELECT name, score,
       ROW_NUMBER() OVER (ORDER BY score DESC) AS rank
FROM student;

-- 排名（跳过并列）
SELECT name, score,
       RANK() OVER (ORDER BY score DESC) AS rank
FROM student;

-- 排名（并列，下一个不跳过）
SELECT name, score,
       DENSE_RANK() OVER (ORDER BY score DESC) AS rank
FROM student;
```