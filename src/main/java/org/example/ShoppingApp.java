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
        System.out.println("欢迎使用购物平台小程序");
        System.out.println("请选择你的身份：");
        System.out.println("1. 管理员");
        System.out.println("2. 用户");
        System.out.println("3. 退出");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                adminLogin(); // 管理员登录
                break;
            case 2:
                userLogin(); // 用户登录
                break;
            case 3:
                exit(); // 退出程序
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                start(); // 重新开始
                break;
        }
    }

    // 用户界面的方法，显示用户选项菜单，并根据选择执行相应操作
    public void userMenu(String username) {
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 密码管理");
        System.out.println("2. 购物");
        System.out.println("3. 退出登录");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                userPassword(username); // 密码管理
                break;
            case 2:
                userShopping(username); // 购物
                break;
            case 3:
                start(); // 退出登录
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                userMenu(username); // 重新显示菜单
                break;
        }
    }

    // 管理员密码管理的方法，显示密码管理选项菜单，并根据选择执行相应操作
    public void adminPassword() {
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 修改自身密码");
        System.out.println("2. 重置用户密码");
        System.out.println("3. 返回");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                changeAdminPassword(); // 修改自身密码
                break;
            case 2:
                resetUserPassword(); // 重置用户密码
                break;
            case 3:
                adminMenu(); // 返回
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminPassword(); // 重新显示菜单
                break;
        }
    }

    public void exit() {
        System.out.println("感谢使用购物平台小程序，再见！");
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
            if (scanner != null)
                scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   

    // 管理员重置用户密码的方法，输入用户名和新密码，如果用户名存在则更新密码，否则提示错误信息
    public void resetUserPassword() {
        System.out.println("请输入你要重置密码的用户的用户名：");
        String username = scanner.nextLine();
        System.out.println("请输入你要重置密码的用户的新密码：");
        String newPassword = scanner.nextLine();
        try {
            String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + username + "';";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                sql = "UPDATE " + USER_TABLE + " SET password = '" + newPassword + "' WHERE username = '" + username
                        + "';";
                stmt.executeUpdate(sql);
                System.out.println("重置密码成功");
            } else {
                System.out.println("重置密码失败，用户名不存在");
            }
            adminPassword(); // 返回密码管理界面
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    // 用户重置密码的方法，输入用户名和新密码，如果用户名存在则更新密码，否则提示错误信息
    public void resetUserPassword(String username) {
        System.out.println("请输入你要重置密码的用户的用户名：");
        // String username = scanner.nextLine(); //这一行是错误的
        String targetUsername = scanner.nextLine(); // 用一个不同的变量名来存储用户输入的用户名
        System.out.println("请输入你要重置密码的用户的新密码：");
        String newPassword = scanner.nextLine();
        try {
            String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + targetUsername + "';"; // 用targetUsername来进行数据库操作
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                sql = "UPDATE " + USER_TABLE + " SET password = '" + newPassword + "' WHERE username = '"
                        + targetUsername + "';";
                stmt.executeUpdate(sql);
                System.out.println("重置密码成功");
            } else {
                System.out.println("重置密码失败，用户名不存在");
            }
            userPassword(username); // 返回密码管理界面
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 用户将商品加入购物车的方法，输入商品编号和数量，如果编号存在且库存足够则添加到购物车表中，否则提示错误信息
    public void addProductToCart(String username) {
        System.out.println("请输入你要加入购物车的商品的编号：");
        int productId = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        System.out.println("请输入你要加入购物车的商品的数量：");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        try {
            String sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + productId + ";";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int stock = rs.getInt("stock");
                if (stock >= quantity) {
                    sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE
                            + " WHERE username = '" + username + "') AND product_id = " + productId + ";";
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        int oldQuantity = rs.getInt("quantity");
                        int newQuantity = oldQuantity + quantity;
                        sql = "UPDATE " + CART_TABLE + " SET quantity = " + newQuantity
                                + " WHERE user_id = (SELECT id FROM "
                                + USER_TABLE + " WHERE username = '" + username
                                + "') AND product_id = " + productId + ";";
                        stmt.executeUpdate(sql);
                        System.out.println("添加商品成功");
                    } else {
                        // 如果购物车中没有该商品，则插入一条新记录
                        sql = "INSERT INTO " + CART_TABLE
                                + " (user_id, product_id, quantity) VALUES ((SELECT id FROM "
                                + USER_TABLE
                                + " WHERE username = '"
                                + username
                                + "'), "
                                + productId
                                + ", "
                                + quantity
                                + ");";
                        stmt.executeUpdate(sql);
                        System.out.println("添加商品成功");
                    }
                } else {
                    // 如果库存不足，则提示错误信息
                    System.out.println("对不起，该商品的库存不足，请重新选择");
                }
            } else {
                // 如果商品编号不存在，则提示错误信息
                System.out.println("对不起，该商品不存在，请重新输入");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 从购物车中移除商品的方法，输入商品编号和数量，如果编号存在且数量合理则从购物车表中删除或更新记录，否则提示错误信息
    public void removeProductFromCart(String username) {
        System.out.println("请输入你要从购物车中移除的商品的编号：");
        int productId = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        System.out.println("请输入你要从购物车中移除的商品的数量：");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        try {
            String sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE
                    + " WHERE username = '" + username + "') AND product_id = " + productId + ";";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int oldQuantity = rs.getInt("quantity");
                if (oldQuantity >= quantity) {
                    int newQuantity = oldQuantity - quantity;
                    if (newQuantity == 0) {
                        // 如果移除后数量为0，则删除该记录
                        sql = "DELETE FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM "
                                + USER_TABLE + " WHERE username = '" + username
                                + "') AND product_id = " + productId + ";";
                        stmt.executeUpdate(sql);
                        System.out.println("移除商品成功");
                    } else {
                        // 如果移除后数量不为0，则更新该记录
                        sql = "UPDATE " + CART_TABLE + " SET quantity = " + newQuantity
                                + " WHERE user_id = (SELECT id FROM "
                                + USER_TABLE + " WHERE username = '" + username
                                + "') AND product_id = " + productId + ";";
                        stmt.executeUpdate(sql);
                        System.out.println("移除商品成功");
                    }
                } else {
                    // 如果移除的数量大于购物车中的数量，则提示错误信息
                    System.out.println("对不起，你不能移除超过购物车中的数量，请重新输入");
                }
            } else {
                // 如果购物车中没有该商品，则提示错误信息
                System.out.println("对不起，你的购物车中没有该商品，请重新输入");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改购物车中的商品的方法，输入商品编号和新的数量，如果编号存在且数量合理则更新购物车表中的记录，否则提示错误信息
    public void updateProductInCart(String username) {
        System.out.println("请输入你要修改的购物车中的商品的编号：");
        int productId = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        System.out.println("请输入你要修改的购物车中的商品的新的数量：");
        int newQuantity = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        try {
            String sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE
                    + " WHERE username = '" + username + "') AND product_id = " + productId + ";";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int oldQuantity = rs.getInt("quantity");
                if (newQuantity > 0) {
                    sql = "UPDATE " + CART_TABLE + " SET quantity = " + newQuantity
                            + " WHERE user_id = (SELECT id FROM "
                            + USER_TABLE + " WHERE username = '" + username
                            + "') AND product_id = " + productId + ";";
                    stmt.executeUpdate(sql);
                    System.out.println("修改商品成功");
                } else {
                    // 如果新的数量为0或负数，则提示错误信息
                    System.out.println("对不起，你不能将购物车中的商品数量修改为0或负数，请重新输入");
                }
            } else {
                // 如果购物车中没有该商品，则提示错误信息
                System.out.println("对不起，你的购物车中没有该商品，请重新输入");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 模拟结账的方法，输入用户名，计算购物车中的商品总价，清空购物车表，并将购物记录插入到历史表中
    public void checkout(String username) {
        double totalPrice = 0.0;
        try {
            String sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE
                    + " WHERE username = '" + username + "');";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + productId + ";";
                ResultSet rs2 = stmt.executeQuery(sql);
                if (rs2.next()) {
                    double price = rs2.getDouble("price");
                    totalPrice += price * quantity;
                    // 将购物记录插入到历史表中
                    sql = "INSERT INTO " + HISTORY_TABLE
                            + " (user_id, product_id, quantity, price, date) VALUES ((SELECT id FROM "
                            + USER_TABLE
                            + " WHERE username = '"
                            + username
                            + "'), "
                            + productId
                            + ", "
                            + quantity
                            + ", "
                            + price
                            + ", datetime('now', 'localtime'));";
                    stmt.executeUpdate(sql);
                }
            }
            // 清空购物车表
            sql = "DELETE FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM "
                    + USER_TABLE + " WHERE username = '" + username
                    + "');";
            stmt.executeUpdate(sql);
            System.out.println("结账成功，你的购物车总价为：" + totalPrice);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查看购物历史的方法，输入用户名，查询历史表中的所有记录，并显示出来
    public void viewShoppingHistory(String username) {
        try {
            String sql = "SELECT * FROM " + HISTORY_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE
                    + " WHERE username = '" + username + "') ORDER BY date DESC;";
            rs = stmt.executeQuery(sql);
            System.out.println("你的购物历史如下：");
            System.out.println("商品编号\t商品名称\t商品数量\t商品单价\t购买日期");
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String date = rs.getString("date");
                sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + productId + ";";
                ResultSet rs2 = stmt.executeQuery(sql);
                if (rs2.next()) {
                    String productName = rs2.getString("name");
                    System.out.println(productId + "\t" + productName + "\t" + quantity
                            + "\t" + price + "\t" + date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}