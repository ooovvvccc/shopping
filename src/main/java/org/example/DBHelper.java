package org.example;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.File;

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

    // 在DBHelper类中添加一个getAllItems()方法
    public List<Item> getAllItems() {
        // 创建一个LinkedList对象，用于存储商品信息
        LinkedList<Item> items = new LinkedList<>();
        // 创建一个Scanner对象，用于读取文本文件
        Scanner scanner = null;
        try {
            // 打开文件
            scanner = new Scanner(new File("products.txt"));
            // 循环读取每一行
            while (scanner.hasNextLine()) {
                // 读取一行，并用逗号分隔
                String[] data = scanner.nextLine().split(",");
                // 根据数据创建一个Item对象
                Item item = new Item(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2]),
                        Integer.parseInt(data[3]));
                // 将Item对象添加到LinkedList中
                items.add(item);
            }
        } catch (FileNotFoundException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (scanner != null) {
                scanner.close();
            }
        }
        // 返回LinkedList对象
        return items;
    }

    public void insertAdmin(Admin admin) {
        // 创建一个PrintWriter对象，用于写入文本文件
        PrintWriter writer = null;
        try {
            // 打开文件，并设置为追加模式
            writer = new PrintWriter(new FileOutputStream(new File("admins.txt"), true));
            // 将Admin对象的信息转换为字符串，并用逗号分隔
            String data = admin.toString();
            // 将字符串写入到文本文件中
            writer.println(data);
        } catch (FileNotFoundException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (writer != null) {
                writer.close();
            }
        }
    }
}