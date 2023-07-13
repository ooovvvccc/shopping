package org.example;

import java.util.*;
import java.sql.*;

public class ShoppingApp {
    Scanner scanner; // 输入扫描器
    private DBHelper dbHelper; // 数据库操作对象

    ShoppingApp() {
        scanner = new Scanner(System.in);
        try {
            // 创建一个DBHelper对象
            dbHelper = new DBHelper();
            // 测试数据库操作
            testDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 测试数据库操作的方法
    private void testDB() throws SQLException {
        // 使用DBHelper对象获取数据库连接对象
        Connection connection = dbHelper.getConnection();
        // 使用Statement或PreparedStatement对象来执行SQL语句
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT EMPNAME FROM EMPLOYEEDETAILS");
        // 使用ResultSet对象来获取查询结果
        while (resultSet.next()) {
            System.out.println("EMPLOYEE NAME:" + resultSet.getString("EMPNAME"));
        }
        // 关闭资源
        resultSet.close();
        statement.close();
    }

    // 开始程序的方法，显示欢迎信息和选项菜单
    public void start() {
        System.out.println("****欢迎使用购物平台小程序****");
        System.out.println("*******当前在第一级界面*******");
        System.out.println("请选择你的身份：");
        System.out.println("1. 管理员");
        System.out.println("2. 用户");
        System.out.println("3. 退出");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        if(choice==1) 
                adminMenu();
        else if(choice==2)
                useMenu();
        else if(choice==3)
                exit(); // 退出程序
        else{
            System.out.println("无效的输入，请重新输入");
            start(); // 重新开始
        }
    }

    public void adminMenu() {
        System.out.println("*********管理员系统**********");
        System.out.println("*******当前在第二级界面*******");
        System.out.println("1. 登录");
        System.out.println("2. 返回上一级");
        System.out.println("3. 退出");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                System.out.println("请输入管理员名：");
                String adminName = scanner.nextLine();
                System.out.println("请输入密码：");
                String adminPassword = scanner.nextLine();
                Admin admin = new Admin(adminName, adminPassword);
                if (admin.adminLogin()) {
                    admin.adminMenu();
                }
                break;
            case 2:
                start();
                break;
            case 3:
                exit(); // 退出程序
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminMenu();// 重新开始
                break;
        }
    }

    public void useMenu() {
        System.out.println("**********用户系统***********");
        System.out.println("*******当前在第二级界面*******");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.println("3. 返回上一级");
        System.out.println("4. 退出");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                System.out.println("请输入用户名：");
                String userName = scanner.nextLine();
                System.out.println("请输入用户密码：");
                String password = scanner.nextLine();
                User user1 = new User(userName, password);
                if (user1.login()) {
                    user1.userMenu();
                }
                break;
            case 2:
                System.out.println("请输入用户名：");
                String userName2 = scanner.nextLine();
                System.out.println("请输入用户密码：");
                String password2 = scanner.nextLine();
                User user2 = new User(userName2, password2);
                if (user2.register()) {
                    user2.userMenu();
                }
                break;
            case 3:
                start();
                break;
            case 4:
                exit(); // 退出程序
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminMenu();// 重新开始
                break;
        }
    }

    // 退出程序的方法，显示结束信息并关闭输入扫描器
    public void exit() {
        scanner.close();
        System.out.println("感谢使用购物平台小程序，再见！");
        
    }
}