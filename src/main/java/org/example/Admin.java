package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin {

    // 属性
    private String username; // 用户名
    private String password; // 密码
    private boolean loggedIn; // 登录状态
    private DBHelper dbHelper; // 数据库操作对象
    private Scanner scanner; // 输入扫描器

    // 构造方法，接收用户名和密码作为参数，并创建数据库操作对象和输入扫描器对象
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.dbHelper = new DBHelper();
        this.scanner = new Scanner(System.in);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean adminLogin() {
        try {
            Connection connection = dbHelper.getConnection();
            String adminTable = dbHelper.ADMIN_TABLE;
            String sql = "SELECT * FROM " + adminTable + " WHERE username = ? AND password = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                System.out.println("用户名或密码错误，请重新输入");
                ShoppingApp shopping = new ShoppingApp();
                shopping.start();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 管理员界面的方法，显示管理员选项菜单，并根据选择执行相应操作
    public void adminMenu() {
        System.out.println("*********管理员系统**********");
        System.out.println("*******当前在第三级界面*******");
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 密码管理");
        System.out.println("2. 客户管理");
        System.out.println("3. 商品管理");
        System.out.println("4. 退出登录");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                adminPassword(); // 密码管理
                break;
            case 2:
                adminCustomer(); // 客户管理
                break;
            case 3:
                adminProduct(); // 商品管理
                break;
            case 4:
                exit(); // 退出登录
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminMenu(); // 重新显示菜单
                break;
        }
    }

    // 密码管理的方法，显示密码管理选项菜单，并根据选择执行相应操作
    public void adminPassword() {
        System.out.println("*********管理员系统**********");
        System.out.println("*******当前在第四级界面*******");
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 修改自身密码");
        System.out.println("2. 重置用户密码");
        System.out.println("3. 返回上一级菜单");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                changePassword(); // 修改自身密码
                break;
            case 2:
                resetPassword(); // 重置用户密码
                break;
            case 3:
                adminMenu(); // 返回上一级菜单
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminPassword(); // 重新显示菜单
                break;
        }
    }

    // 修改自身密码的方法，接收旧密码和新密码，并更新数据库中的密码
    public void changePassword() {
        System.out.println("请输入旧密码：");
        String oldPassword = scanner.nextLine();
        if (oldPassword.equals(this.password)) { // 验证旧密码是否正确
            System.out.println("请输入新密码：");
            String newPassword = scanner.nextLine();
            try {
                // 使用DBHelper对象获取数据库连接对象和表名
                Connection connection = dbHelper.getConnection();
                String adminTable = dbHelper.ADMIN_TABLE;
                // 构造更新语句，使用占位符防止SQL注入攻击
                String sql = "UPDATE " + adminTable + " SET password = ? WHERE username = ?;";
                // 使用连接对象创建预编译语句对象，并设置参数值
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, this.username);
                // 执行更新语句，返回受影响的行数
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("修改密码成功");
                    this.password = newPassword; // 更新属性值
                    adminPassword(); // 返回密码管理菜单
                } else {
                    System.out.println("修改密码失败");
                    changePassword(); // 重新修改密码
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("旧密码错误，请重新输入");
            changePassword(); // 重新修改密码
        }
    }

    // 重置用户密码的方法，接收用户名和新密码，并更新数据库中的密码
    public void resetPassword() {
        System.out.println("请输入要重置密码的用户名：");
        String username = scanner.nextLine();
        System.out.println("请输入新密码：");
        String newPassword = scanner.nextLine();
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造更新语句，使用占位符防止SQL注入攻击
            String sql = "UPDATE " + userTable + " SET password = ? WHERE username = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            // 执行更新语句，返回受影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("重置用户" + username + "的密码成功");
                adminPassword(); // 返回密码管理菜单
            } else {
                System.out.println("重置用户" + username + "的密码失败，可能不存在这个用户");
                resetPassword(); // 重新重置密码
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 客户管理的方法，显示客户管理选项菜单，并根据选择执行相应操作
    public void adminCustomer() {
        System.out.println("*********管理员系统**********");
        System.out.println("*******当前在第四级界面*******");
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 列出所有客户信息");
        System.out.println("2. 删除客户信息");
        System.out.println("3. 查询客户信息");
        System.out.println("4. 返回上一级菜单");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                listCustomers(); // 列出所有客户信息
                break;
            case 2:
                deleteCustomer(); // 删除客户信息
                break;
            case 3:
                queryCustomer(); // 查询客户信息
                break;
            case 4:
                adminMenu(); // 返回上一级菜单
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminCustomer(); // 重新显示菜单
                break;
        }
    }

    // 列出所有客户信息的方法，从数据库中查询所有用户表的数据，并打印出来
    public void listCustomers() {
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造查询语句，查询所有用户表的数据
            String sql = "SELECT * FROM " + userTable + ";";
            // 使用连接对象创建语句对象
            Statement stmt = connection.createStatement();
            // 执行查询语句，获取结果集对象
            ResultSet rs = stmt.executeQuery(sql);
            // 打印表头
            System.out.println("用户ID\t用户名\t密码");
            // 遍历结果集，打印每一行数据
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                System.out.println(id + "\t" + username + "\t" + password);
            }
            // 关闭资源
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adminCustomer(); // 返回客户管理菜单
    }

    // 删除客户信息的方法，接收用户名，并从数据库中删除对应的用户数据
    public void deleteCustomer() {
        System.out.println("请输入要删除的客户用户名：");
        String username = scanner.nextLine();
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造删除语句，使用占位符防止SQL注入攻击
            String sql = "DELETE FROM " + userTable + " WHERE username = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            // 执行删除语句，返回受影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("删除客户" + username + "成功");
                adminCustomer(); // 返回客户管理菜单
            } else {
                System.out.println("删除客户" + username + "失败，可能不存在这个客户");
                deleteCustomer(); // 重新删除客户
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询客户信息的方法，接收用户名，并从数据库中查询对应的用户数据，并打印出来
    public void queryCustomer() {
        System.out.println("请输入要查询的客户用户名：");
        String username = scanner.nextLine();
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造查询语句，使用占位符防止SQL注入攻击
            String sql = "SELECT * FROM " + userTable + " WHERE username = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            // 执行查询语句，获取结果集对象
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // 如果有结果，打印出来
                int id = rs.getInt("id");
                String password = rs.getString("password");
                System.out.println("用户ID：" + id);
                System.out.println("用户名：" + username);
                System.out.println("密码：" + password);
                adminCustomer(); // 返回客户管理菜单
            } else { // 如果没有结果，提示用户不存在
                System.out.println("不存在用户名为" + username + "的客户");
                queryCustomer(); // 重新查询客户
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 商品管理的方法，显示商品管理选项菜单，并根据选择执行相应操作
    public void adminProduct() {
        System.out.println("*********管理员系统**********");
        System.out.println("*******当前在第四级界面*******");
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 列出所有商品信息");
        System.out.println("2. 添加商品信息");
        System.out.println("3. 修改商品信息");
        System.out.println("4. 删除商品信息");
        System.out.println("5. 查询商品信息");
        System.out.println("6. 返回上一级菜单");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                listProducts(); // 列出所有商品信息
                break;
            case 2:
                addProduct(); // 添加商品信息
                break;
            case 3:
                updateProduct(); // 修改商品信息
                break;
            case 4:
                deleteProduct(); // 删除商品信息
                break;
            case 5:
                queryProduct(); // 查询商品信息
                break;
            case 6:
                adminMenu(); // 返回上一级菜单
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminProduct(); // 重新显示菜单
                break;
        }
    }

    // 列出所有商品信息的方法，从数据库中查询所有商品表的数据，并打印出来
    public void listProducts() {
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String productTable = dbHelper.PRODUCT_TABLE;
            // 构造查询语句，查询所有商品表的数据
            String sql = "SELECT * FROM " + productTable + ";";
            // 使用连接对象创建语句对象
            Statement stmt = connection.createStatement();
            // 执行查询语句，获取结果集对象
            ResultSet rs = stmt.executeQuery(sql);
            // 打印表头
            System.out.println("商品ID\t商品名称\t商品价格\t商品库存");
            // 遍历结果集，打印每一行数据
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                System.out.println(id + "\t" + name + "\t\t" + price + "\t\t" + stock);
            }
            // 关闭资源
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        adminProduct(); // 返回商品管理菜单
    }

    // 添加商品信息的方法，接收商品名称、价格和库存，并插入到数据库中
    public void addProduct() {
        System.out.println("请输入要添加的商品名称：");
        String name = scanner.nextLine();
        System.out.println("请输入要添加的商品价格：");
        double price = scanner.nextDouble();
        scanner.nextLine(); // 消除回车
        System.out.println("请输入要添加的商品库存：");
        int stock = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String productTable = dbHelper.PRODUCT_TABLE;
            // 构造插入语句，使用占位符防止SQL注入攻击
            String sql = "INSERT INTO " + productTable + " (name, price, stock) VALUES (?, ?, ?);";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stock);
            // 执行插入语句，返回受影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("添加商品" + name + "成功");
                adminProduct(); // 返回商品管理菜单
            } else {
                System.out.println("添加商品" + name + "失败");
                addProduct(); // 重新添加商品
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改商品信息的方法，接收商品ID和新的价格或库存，并更新数据库中的数据
    public void updateProduct() {
        System.out.println("请输入要修改的商品ID：");
        int id = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        System.out.println("请选择要修改的属性：");
        System.out.println("1. 价格");
        System.out.println("2. 库存");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1: // 修改价格
                System.out.println("请输入新的价格：");
                double price = scanner.nextDouble();
                scanner.nextLine(); // 消除回车
                try {
                    // 使用DBHelper对象获取数据库连接对象和表名
                    Connection connection = dbHelper.getConnection();
                    String productTable = dbHelper.PRODUCT_TABLE;
                    // 构造更新语句，使用占位符防止SQL注入攻击
                    String sql = "UPDATE " + productTable + " SET price = ? WHERE id = ?;";
                    // 使用连接对象创建预编译语句对象，并设置参数值
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setDouble(1, price);
                    pstmt.setInt(2, id);
                    // 执行更新语句，返回受影响的行数
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("修改商品" + id + "的价格成功");
                        adminProduct(); // 返回商品管理菜单
                    } else {
                        System.out.println("修改商品" + id + "的价格失败，可能不存在这个商品");
                        updateProduct(); // 重新修改商品
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2: // 修改库存
                System.out.println("请输入新的库存：");
                int stock = scanner.nextInt();
                scanner.nextLine(); // 消除回车
                try {
                    // 使用DBHelper对象获取数据库连接对象和表名
                    Connection connection = dbHelper.getConnection();
                    String productTable = dbHelper.PRODUCT_TABLE;
                    // 构造更新语句，使用占位符防止SQL注入攻击
                    String sql = "UPDATE " + productTable + " SET stock = ? WHERE id = ?;";
                    // 使用连接对象创建预编译语句对象，并设置参数值
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setInt(1, stock);
                    pstmt.setInt(2, id);
                    // 执行更新语句，返回受影响的行数
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("修改商品" + id + "的库存成功");
                        adminProduct(); // 返回商品管理菜单
                    } else {
                        System.out.println("修改商品" + id + "的库存失败，可能不存在这个商品");
                        updateProduct(); // 重新修改商品
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                updateProduct(); // 重新修改商品
                break;
        }
    }

    // 删除商品信息的方法，接收商品ID，并从数据库中删除对应的商品数据
    public void deleteProduct() {
        System.out.println("请输入要删除的商品ID：");
        int id = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String productTable = dbHelper.PRODUCT_TABLE;
            // 构造删除语句，使用占位符防止SQL注入攻击
            String sql = "DELETE FROM " + productTable + " WHERE id = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            // 执行删除语句，返回受影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("删除商品" + id + "成功");
                adminProduct(); // 返回商品管理菜单
            } else {
                System.out.println("删除商品" + id + "失败，可能不存在这个商品");
                deleteProduct(); // 重新删除商品
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询商品信息的方法，接收商品名称，并从数据库中查询对应的商品数据，并打印出来
    public void queryProduct() {
        System.out.println("请输入要查询的商品名称：");
        String name = scanner.nextLine();
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String productTable = dbHelper.PRODUCT_TABLE;
            // 构造查询语句，使用占位符防止SQL注入攻击
            String sql = "SELECT * FROM " + productTable + " WHERE name = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            // 执行查询语句，获取结果集对象
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // 如果有结果，打印出来
                int id = rs.getInt("id");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                System.out.println("商品ID：" + id);
                System.out.println("商品名称：" + name);
                System.out.println("商品价格：" + price);
                System.out.println("商品库存：" + stock);
                adminProduct(); // 返回商品管理菜单
            } else { // 如果没有结果，提示商品不存在
                System.out.println("不存在名称为" + name + "的商品");
                queryProduct(); // 重新查询商品
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 退出登录的方法，修改登录状态为false，并返回开始界面
    public void exit() {
        this.loggedIn = false;
        System.out.println("退出登录成功");
        ShoppingApp shopping = new ShoppingApp();
        shopping.start(); // 返回开始界面
    }

    // 其他代码省略

}
