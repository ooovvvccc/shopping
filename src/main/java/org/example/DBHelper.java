package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;

public class DBHelper {
    // 定义数据库地址、表名等常量
    public static final String DB_URL = "jdbc:sqlite:shopping.db";
    public static final String ADMIN_TABLE = "admin";
    public static final String USER_TABLE = "user";
    public static final String PRODUCT_TABLE = "product";
    public static final String CART_TABLE = "cart";
    public static final String HISTORY_TABLE = "history";

    // 定义数据库连接、语句和结果集等变量
    public Connection conn;
    public Statement stmt;
    public ResultSet rs;

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
                + "username TEXT NOT NULL,"
                + "password TEXT NOT NULL"
                + ");";
        String sql2 = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " ("
                + "id INTEGER PRIMARY KEY,"
                + "username TEXT NOT NULL,"
                + "password TEXT NOT NULL"
                + ");";
        String sql3 = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE + " ("
                + "id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
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
        stmt.executeUpdate(sql2);
        stmt.executeUpdate(sql3);
        stmt.executeUpdate(sql4);
        stmt.executeUpdate(sql5);
    }

    // 获取连接的方法，用来返回数据库连接对象
    public Connection getConnection() {
        return conn;
    }

    // 关闭资源的方法，用来关闭数据库连接、语句和结果集
    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 在DBHelper类中添加一个getAllItems()方法
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>(); // 创建一个空的List对象
        String sql = "SELECT * FROM " + PRODUCT_TABLE; // 定义查询语句
        try {
            PreparedStatement ps = conn.prepareStatement(sql); // 创建PreparedStatement对象
            rs = ps.executeQuery(); // 执行查询语句，返回ResultSet对象
            while (rs.next()) { // 遍历ResultSet对象
                int id = rs.getInt("id"); // 获取商品id
                String name = rs.getString("name"); // 获取商品名称
                double price = rs.getDouble("price"); // 获取商品价格
                int stock = rs.getInt("stock"); // 获取商品库存
                Item item = new Item(id, name, price, stock); // 创建Item对象，quantity默认为0
                items.add(item); // 将Item对象添加到List中
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items; // 返回List对象
    }

    public void insertAdmin(Admin admin) {
        String sql = "INSERT INTO " + ADMIN_TABLE + "(username, password) VALUES (?, ?)"; // 定义插入语句
        try {
            PreparedStatement ps = conn.prepareStatement(sql); // 创建PreparedStatement对象
            ps.setString(1, admin.getUsername()); // 设置第一个参数为管理员用户名
            ps.setString(2, admin.getPassword()); // 设置第二个参数为管理员密码
            ps.executeUpdate(); // 执行插入语句
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
