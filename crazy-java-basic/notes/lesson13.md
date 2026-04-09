# 第13课：JDBC

## 核心概念

### 13.1 JDBC架构
```
Java App
  └── JDBC API
        └── JDBC Driver Manager
              └── Database Driver (如MySQL Driver)
                    └── Database
```

### 13.2 JDBC操作步骤
1. 加载驱动
2. 获取连接
3. 创建Statement/PreparedStatement
4. 执行SQL
5. 处理ResultSet
6. 关闭资源

### 13.3 事务管理
- setAutoCommit(false)
- commit()
- rollback()

## 代码示例

### 示例1：JDBC基本操作
```java
import java.sql.*;

public class JDBBDemo {
    // JDBC配置
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. 加载驱动
            Class.forName(DRIVER);

            // 2. 获取连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("连接成功");

            // 3. 查询操作
            String sql = "SELECT * FROM users WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 1);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // 4. 关闭资源（注意顺序）
            close(rs, pstmt, conn);
        }
    }

    private static void close(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
```

### 示例2：增删改操作
```java
import java.sql.*;

public class JDBDModifyDemo {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            // INSERT
            insertUser(conn, "张三", "zhangsan@example.com");

            // UPDATE
            updateUser(conn, 1, "newemail@example.com");

            // DELETE
            deleteUser(conn, 2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertUser(Connection conn, String name, String email) throws SQLException {
        String sql = "INSERT INTO users(name, email) VALUES(?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            int rows = pstmt.executeUpdate();
            System.out.println("插入" + rows + "行");
        }
    }

    private static void updateUser(Connection conn, int id, String email) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            System.out.println("更新" + rows + "行");
        }
    }

    private static void deleteUser(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println("删除" + rows + "行");
        }
    }
}
```

### 示例3：事务管理
```java
import java.sql.*;

public class TransactionDemo {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "password";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            // 关闭自动提交
            conn.setAutoCommit(false);

            // 操作1：扣款
            String sql1 = "UPDATE account SET balance = balance - 100 WHERE id = 1";
            conn.prepareStatement(sql1).executeUpdate();

            // 操作2：存款
            String sql2 = "UPDATE account SET balance = balance + 100 WHERE id = 2";
            conn.prepareStatement(sql2).executeUpdate();

            // 提交事务
            conn.commit();
            System.out.println("转账成功");

        } catch (SQLException e) {
            // 回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("事务回滚");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

### 示例4：连接池（Druid）
```java
import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DruidDemo {
    private static DataSource dataSource;

    static {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC");
        ds.setUsername("root");
        ds.setPassword("password");
        ds.setInitialSize(5);
        ds.setMaxActive(10);
        dataSource = ds;
    }

    public static void main(String[] args) throws SQLException {
        // 从连接池获取连接
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
            }
        }
    }
}
```

### 示例5：DBUtils简化操作
```java
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DBUtilsDemo {
    public static void main(String[] args) throws SQLException {
        QueryRunner runner = new QueryRunner(DBConfig.getDataSource());

        // 插入
        int rows = runner.update("INSERT INTO users(name, email) VALUES(?, ?)",
            "张三", "zhangsan@example.com");
        System.out.println("插入：" + rows + "行");

        // 查询
        List<Map<String, Object>> results = runner.query(
            "SELECT * FROM users WHERE id > ?",
            new MapListHandler(),
            0
        );
        for (Map<String, Object> row : results) {
            System.out.println(row);
        }
    }
}
```

## JDBC重要接口

| 接口 | 说明 |
|------|------|
| DriverManager | 驱动管理，获取连接 |
| Connection | 数据库连接 |
| Statement | 执行SQL |
| PreparedStatement | 预编译SQL，防止SQL注入 |
| ResultSet | 结果集 |
| CallableStatement | 调用存储过程 |

## 常见面试题

1. **Statement和PreparedStatement的区别？**
   - PreparedStatement预编译，性能更好
   - PreparedStatement防止SQL注入
   - 推荐使用PreparedStatement

2. **JDBC事务特性（ACID）？**
   - Atomicity（原子性）
   - Consistency（一致性）
   - Isolation（隔离性）
   - Durability（持久性）

3. **连接池的作用？**
   - 复用连接，提高性能
   - 统一管理连接资源

## 练习题

1. 实现用户表的增删改查
2. 实现分页查询
3. 使用事务实现转账功能

## 要点总结

- JDBC是Java操作数据库的标准API
- PreparedStatement防SQL注入
- 事务确保数据一致性
- 连接池提高系统性能
- 记得关闭资源
