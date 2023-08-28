
package org.example;


public class Main {
    public static void main(String[] args) {
        DBHelper dbHelper = new DBHelper(); // 创建一个DBHelper对象
        Admin admin = new Admin("admin", "ynuinfo#777"); 
        dbHelper.insertAdmin(admin); // 调用insertAdmin方法将Admin对象插入到数据库中
        ShoppingApp app = new ShoppingApp();
        app.start(); // 开始程序
    }
}