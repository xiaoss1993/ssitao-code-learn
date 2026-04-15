package com.learn.crazyjava.lesson13_jdbc;

import java.sql.*;

/**
 * 第13课：JDBC - 基本操作
 */
public class JDBCDemo {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        try {
            // 加载驱动
            Class.forName(DRIVER);

            // 获取连接
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("连接成功");

                // 查询
                queryUsers(conn);

                // 插入
                // insertUser(conn, "张三", "zhangsan@example.com");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void queryUsers(Connection conn) throws SQLException {
        String sql = "SELECT id, name, email FROM users LIMIT 10";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Email: %s%n",
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"));
            }
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
}
