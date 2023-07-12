package org.example;
import java.util.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
     public static final String DB_URL = "jdbc:sqlite:shopping.db"; //数据库地址
     public static final String ADMIN_TABLE = "admin"; //管理员表名
     public static final String USER_TABLE = "user"; //用户表名
     public static final String PRODUCT_TABLE = "product"; //商品表名
     public static final String CART_TABLE = "cart"; //购物车表名
     public static final String HISTORY_TABLE = "history"; //购物历史表名
 ShoppingApp ShoppingApp=new ShoppingApp();
     //定义一些变量
     private Scanner scanner; //输入扫描器
     private Connection conn; //数据库连接
     private Statement stmt; //数据库语句
     private ResultSet rs; //数据库结果集
 
     
 
     //创建数据库表的方法，如果表不存在则创建，否则不做操作
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
         stmt.executeUpdate(sql2);
         stmt.executeUpdate(sql3);
         stmt.executeUpdate(sql4);
         stmt.executeUpdate(sql5);
     }
}
