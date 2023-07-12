package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {
    // 定义数据库地址、表名等常量
    private static final String DB_URL = "jdbc:sqlite:shopping.db";
    private static final String ADMIN_TABLE = "admin";
    private static final String USER_TABLE = "user";
    private static final String PRODUCT_TABLE = "product";
    private static final String CART_TABLE = "cart";
    private static final String HISTORY_TABLE = "history";

    // 定义数据库连接、语句和结果集等变量
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    // 构造方法，用来加载驱动、创建连接和创建表
    public DBHelper() {
        try {
            // 加载驱动类
            Class.forName("org.sqlite.JDBC");
            // 创建连接对象
            conn = DriverManager.getConnection(DB_URL);
            // 创建语句对象
            stmt = conn.createStatement();
            // 调用创建表的方法
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 创建表的方法，如果表不存在则创建，否则不做操作
    private void createTables() throws SQLException {
        String sql1 = "CREATE TABLE IF NOT EXISTS " + ADMIN_TABLE + " ("
                + "id INTEGER PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ");";
        String sql2 = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
                + "id INTEGER PRIMARY KEY,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL"
                + ");";
        String sql3 = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE + " ("
                + "id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL UNIQUE,"
                + "price REAL NOT NULL,"
                + "stock INTEGER NOT NULL"
                + ");";
        String sql4 = "CREATE TABLE IF NOT EXISTS " + CART_TABLE + " ("
                + "user_id INTEGER NOT NULL,"
                + "product_id INTEGER NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES user(id),"
                + "FOREIGN KEY (product_id) REFERENCES product(id),"
                + "PRIMARY KEY (user_id, product_id)"
                + ");";
        String sql5 = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE + " ("
                + "user_id INTEGER NOT NULL,"
                + "product_id INTEGER NOT NULL,"
                + "quantity INTEGER NOT NULL,"
                + "total REAL NOT NULL,"
                + "date TEXT NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES user(id),"
                + "FOREIGN KEY (product_id) REFERENCES product(id)"
                + ");";
        stmt.executeUpdate(sql1);
        stmt.executeUpdate(sql2);stmt.executeUpdate(sql3);stmt.executeUpdate(sql4);stmt.executeUpdate(sql5);
    }

    // 获取连接的方法，用来返回数据库连接对象
    public Connection getConnection() {return conn;}

    // 关闭资源的方法，用来关闭数据库连接、语句和结果集
    public void close() {try {if (rs != null) {rs.close();}if (stmt != null) {stmt.close();}
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}