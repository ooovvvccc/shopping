package org.example;
import java.util.*;
import java.sql.*;

public class ShoppingApp {
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
    ShoppingApp() {
        scanner = new Scanner(System.in);
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            createTables(); //创建数据库表
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //开始程序的方法，显示欢迎信息和选项菜单
    public void start() {
        System.out.println("欢迎使用购物平台小程序");
        System.out.println("请选择你的身份：");
        System.out.println("1. 管理员");
        System.out.println("2. 用户");
        System.out.println("3. 退出");
        int choice = scanner.nextInt();
        scanner.nextLine(); //消除回车
        switch (choice) {
            case 1:
                adminLogin(); //管理员登录
                break;
            case 2:
                userLogin(); //用户登录
                break;
            case 3:
                exit(); //退出程序
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                start(); //重新开始
                break;
        }
    }

    //管理员登录的方法，验证用户名和密码，如果成功则进入管理员界面，否则返回开始界面
    public void adminLogin() {
        System.out.println("请输入管理员用户名：");
        String username = scanner.nextLine();
        System.out.println("请输入管理员密码：");
        String password = scanner.nextLine();
        try {
            String sql = "SELECT * FROM " + ADMIN_TABLE + " WHERE username = '" + username + "' AND password = '" + password + "';";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                System.out.println("登录成功，欢迎你，" + username);
                adminMenu(); //进入管理员界面
            } else {
                System.out.println("登录失败，用户名或密码错误");
                start(); //返回开始界面
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //用户登录的方法，验证用户名和密码，如果成功则进入用户界面，否则返回开始界面
    public void userLogin() {
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 注册");
        System.out.println("2. 登录");
        System.out.println("3. 返回");
        int choice = scanner.nextInt();
        scanner.nextLine(); //消除回车
        switch (choice) {
            case 1:
                userRegister(); //用户注册
                break;
            case 2:
            userSignIn(); //用户登录
            break;
        case 3:
            start(); //返回开始界面
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            userLogin(); //重新登录
            break;
    }
}

//用户注册的方法，输入用户名和密码，如果用户名不存在则创建新用户，否则提示用户名已存在
public void userRegister() {
    System.out.println("请输入你要注册的用户名：");
    String username = scanner.nextLine();
    System.out.println("请输入你要注册的密码：");
    String password = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + username + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            System.out.println("用户名已存在，请重新输入");
            userRegister(); //重新注册
        } else {
            sql = "INSERT INTO " + USER_TABLE + " (username, password) VALUES ('" + username + "', '" + password + "');";
            stmt.executeUpdate(sql);
            System.out.println("注册成功，欢迎你，" + username);
            userMenu(username); //进入用户界面
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//用户登录的方法，输入用户名和密码，如果正确则进入用户界面，否则提示错误信息
public void userSignIn() {
    System.out.println("请输入你的用户名：");
    //String username.println("请选择你要进行的操作："); //这一行是错误的
    String username = scanner.nextLine(); //获取用户输入的用户名
    System.out.println("请选择你要进行的操作："); //打印出菜单选项
    System.out.println("1. 密码管理");
    System.out.println("2. 客户管理");
    System.out.println("3. 商品管理");
    System.out.println("4. 退出登录");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            adminPassword(); //密码管理
            break;
        case 2:
            adminCustomer(); //客户管理
            break;
        case 3:
            adminProduct(); //商品管理
            break;
        case 4:
            start(); //退出登录
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            adminMenu(); //重新显示菜单
            break;
    }
}

//用户界面的方法，显示用户选项菜单，并根据选择执行相应操作
public void userMenu(String username) {
    System.out.println("请选择你要进行的操作：");
    System.out.println("1. 密码管理");
    System.out.println("2. 购物");
    System.out.println("3. 退出登录");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            userPassword(username); //密码管理
            break;
        case 2:
            userShopping(username); //购物
            break;
        case 3:
            start(); //退出登录
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            userMenu(username); //重新显示菜单
            break;
    }
}

//管理员密码管理的方法，显示密码管理选项菜单，并根据选择执行相应操作
public void adminPassword() {
    System.out.println("请选择你要进行的操作：");
    System.out.println("1. 修改自身密码");
    System.out.println("2. 重置用户密码");
    System.out.println("3. 返回");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
        changeAdminPassword(); //修改自身密码
        break;
    case 2:
        resetUserPassword(); //重置用户密码
        break;
    case 3:
        adminMenu(); //返回
        break;
    default:
        System.out.println("无效的输入，请重新输入");
        adminPassword(); //重新显示菜单
        break;
}
}
public void exit() {
    System.out.println("感谢使用购物平台小程序，再见！");
    try {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
        if (scanner != null) scanner.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//管理员界面的方法，显示管理员选项菜单，并根据选择执行相应操作
public void adminMenu() {
    System.out
            .println("请选择你要进行的操作：");
    System.out.println("1. 密码管理");
    System.out.println("2. 客户管理");
    System.out.println("3. 商品管理");
    System.out.println("4. 退出登录");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            adminPassword(); //密码管理
            break;
        case 2:
            adminCustomer(); //客户管理
            break;
        case 3:
            adminProduct(); //商品管理
            break;
        case 4:
            exit(); //退出登录
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            adminMenu(); //重新显示菜单
            break;
    }
}
//管理员客户管理的方法，显示客户管理选项菜单，并根据选择执行相应操作
public void adminCustomer() {
    System.out.println("请选择你要进行的操作：");
    System.out.println("1. 列出所有客户信息");
    System.out.println("2. 删除客户信息");
    System.out.println("3. 查询客户信息");
    System.out.println("4. 返回");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            listAllCustomers(); //列出所有客户信息
            break;
        case 2:
            deleteCustomer(); //删除客户信息
            break;
        case 3:
            queryCustomer(); //查询客户信息
            break;
        case 4:
            adminMenu(); //返回
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            adminCustomer(); //重新显示菜单
            break;
    }
}

//管理员商品管理的方法，显示商品管理选项菜单，并根据选择执行相应操作
public void adminProduct() {
    System.out.println("请选择你要进行的操作：");
    System.out.println("1. 列出所有商品信息");
    System.out.println("2. 添加商品信息");
    System.out.println("3. 修改商品信息");
    System.out.println("4. 删除商品信息");
    System.out.println("5. 查询商品信息");
    System.out.println("6. 返回");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            listAllProducts(); //列出所有商品信息
            break;
        case 2:
            addProduct(); //添加商品信息
            break;
        case 3:
            updateProduct(); //修改商品信息
            break;
        case 4:
            deleteProduct(); //删除商品信息
            break;
        case 5:
            queryProduct(); //查询商品信息
            break;
        case 6:
            adminMenu(); //返回
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            adminProduct(); //重新显示菜单
            break;
    }
}

//用户密码管理的方法，显示密码管理选项菜单，并根据选择执行相应操作
public void userPassword(String username) {
    System.out.println("请选择你要进行的操作：");
    System.out.println("1. 修改密码");
    System.out.println("2. 重置密码");
    System.out.println("3. 返回");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            changeUserPassword(username); //修改密码
            break;
        case 2:
            resetUserPassword(username); //重置密码
            break;
        case 3:
            userMenu(username); //返回
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            userPassword(username); //重新显示菜单
            break;
    }
}

//用户购物的方法，显示购物选项菜单，并根据选择执行相应操作
public void userShopping(String username) {
    System.out.println("请选择你要进行的操作：");
    System.out.println("1. 将商品加入购物车");
    System.out.println("2. 从购物车中移除商品");
    System.out.println("3. 修改购物车中的商品");
    System.out.println("4. 模拟结账");
    System.out.println("5. 查看购物历史");
    System.out.println("6. 返回");
    int choice = scanner.nextInt();
    scanner.nextLine(); //消除回车
    switch (choice) {
        case 1:
            addProductToCart(username); //将商品加入购物车
            break;
        case 2:
            removeProductFromCart(username); //从购物车中移除商品
            break;
        case 3:
            updateProductInCart(username); //修改购物车中的商品
            break;
        case 4:
            checkout(username); //模拟结账
            break;
        case 5:
            viewShoppingHistory(username); //查看购物历史
            break;
        case 6:
            userMenu(username); //返回
            break;
        default:
            System.out.println("无效的输入，请重新输入");
            userShopping(username); //重新显示菜单
            break;
    }
}

//管理员修改自身密码的方法，输入旧密码和新密码，如果旧密码正确则更新密码，否则提示错误信息
public void changeAdminPassword() {
    System.out.println("请输入你的旧密码：");
    String oldPassword = scanner.nextLine();
    System.out.println("请输入你的新密码：");
    String newPassword = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + ADMIN_TABLE + " WHERE password = '" + oldPassword + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "UPDATE " + ADMIN_TABLE + " SET password = '" + newPassword + "' WHERE password = '" + oldPassword + "';";
            stmt.executeUpdate(sql);
            System.out.println("修改密码成功");
        } else {
            System.out.println("修改密码失败，旧密码错误");
        }
        adminPassword(); //返回密码管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员重置用户密码的方法，输入用户名和新密码，如果用户名存在则更新密码，否则提示错误信息
public void resetUserPassword() {
    System.out.println("请输入你要重置密码的用户的用户名：");
    String username = scanner.nextLine();
    System.out.println("请输入你要重置密码的用户的新密码：");
    String newPassword = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + username + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "UPDATE " + USER_TABLE + " SET password = '" + newPassword + "' WHERE username = '" + username + "';";
            stmt.executeUpdate(sql);
            System.out.println("重置密码成功");
        } else {
            System.out.println("重置密码失败，用户名不存在");
        }
        adminPassword(); //返回密码管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//管理员列出所有客户信息的方法，查询并显示用户表中的所有记录
public void listAllCustomers() {
    try {
        String sql = "SELECT * FROM " + USER_TABLE + ";";
        rs = stmt.executeQuery(sql);
        System.out.println("以下是所有客户的信息：");
        System.out.println("编号\t用户名\t密码");
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            System.out.println(id + "\t" + username + "\t" + password);
        }
        adminCustomer(); //返回客户管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员删除客户信息的方法，输入用户编号，如果存在则删除该用户的记录，否则提示错误信息
public void deleteCustomer() {
    System.out.println("请输入你要删除的客户的编号：");
    int id = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE id = " + id + ";";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "DELETE FROM " + USER_TABLE + " WHERE id = " + id + ";";
            stmt.executeUpdate(sql);
            System.out.println("删除客户成功");
        } else {
            System.out.println("删除客户失败，编号不存在");
        }
        adminCustomer(); //返回客户管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员查询客户信息的方法，输入用户名，如果存在则显示该用户的记录，否则提示错误信息
public void queryCustomer() {
    System.out.println("请输入你要查询的客户的用户名：");
    String username = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + username + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int id = rs.getInt("id");
            String password = rs.getString("password");
            System.out.println("以下是该客户的信息：");
            System.out.println("编号\t用户名\t密码");
            System.out.println(id + "\t" + username + "\t" + password);
        } else {
            System.out.println("查询客户失败，用户名不存在");
        }
        adminCustomer(); //返回客户管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员列出所有商品信息的方法，查询并显示商品表中的所有记录
public void listAllProducts() {
    try {
        String sql = "SELECT * FROM " + PRODUCT_TABLE + ";";
        rs = stmt.executeQuery(sql);
        System.out.println("以下是所有商品的信息：");
        System.out.println("编号\t名称\t价格\t库存");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            int stock = rs.getInt("stock");
            System.out.println(id + "\t" + name + "\t" + price + "\t" + stock);
        }
        adminProduct(); //返回商品管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员添加商品信息的方法，输入商品名称，价格和库存，如果名称不存在则创建新商品，否则提示名称已存在
public void addProduct() {
    System.out.println("请输入你要添加的商品的名称：");
    String name = scanner.nextLine();
    System.out.println("请输入你要添加的商品的价格：");
    double price = scanner.nextDouble();
    scanner.nextLine(); //消除回车
    System.out.println("请输入你要添加的商品的库存：");
    int stock = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE name = '" + name + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            System.out.println("名称已存在，请重新输入");
            addProduct(); //重新添加
        } else {
            sql = "INSERT INTO " + PRODUCT_TABLE + " (name, price, stock) VALUES ('" + name + "', " + price + ", " + stock + ");";
            stmt.executeUpdate(sql);
            System.out.println("添加商品成功");
            adminProduct(); //返回商品管理界面
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员修改商品信息的方法，输入商品编号，如果存在则显示该商品的记录，并输入新的价格和库存，否则提示错误信息
public void updateProduct() {
    System.out.println("请输入你要修改的商品的编号：");
    int id = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + id + ";";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            int stock = rs.getInt("stock");
            System.out.println("以下是该商品的信息：");
            System.out.println("编号\t名称\t价格\t库存");
            System.out.println(id + "\t" + name + "\t" + price + "\t" + stock);
            System.out.println("请输入你要修改的商品的新价格：");
            double newPrice = scanner.nextDouble();
            scanner.nextLine(); //消除回车
            System.out.println("请输入你要修改的商品的新库存：");
            int newStock = scanner.nextInt();
            scanner.nextLine(); //消除回车
            sql = "UPDATE " + PRODUCT_TABLE + " SET price = " + newPrice + ", stock = " + newStock + " WHERE id = " + id + ";";
            stmt.executeUpdate(sql);
            System.out.println("修改商品成功");
        } else {
            System.out.println("修改商品失败，编号不存在");
        }
        adminProduct(); //返回商品管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员删除商品信息的方法，输入商品编号，如果存在则删除该商品的记录，否则提示错误信息
public void deleteProduct() {
    System.out.println("请输入你要删除的商品的编号：");
    int id = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + id + ";";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "DELETE FROM " + PRODUCT_TABLE + " WHERE id = " + id + ";";
            stmt.executeUpdate(sql);
            System.out.println("删除商品成功");
        } else {
            System.out.println("删除商品失败，编号不存在");
        }
        adminProduct(); //返回商品管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//管理员查询商品信息的方法，输入商品名称，如果存在则显示该商品的记录，否则提示错误信息
public void queryProduct() {
    System.out.println("请输入你要查询的商品的名称：");
    String name = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE name = '" + name + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int id = rs.getInt("id");
            double price = rs.getDouble("price");
            int stock = rs.getInt("stock");
            System.out.println("以下是该商品的信息：");
            System.out.println("编号\t名称\t价格\t库存");
            System.out.println(id + "\t" + name + "\t" + price + "\t" + stock);
        } else {
            System.out.println("查询商品失败，名称不存在");
        }
        adminProduct(); //返回商品管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//用户修改密码的方法，输入旧密码和新密码，如果旧密码正确则更新密码，否则提示错误信息
public void changeUserPassword(String username) {
    System.out.println("请输入你的旧密码：");
    String oldPassword = scanner.nextLine();
    System.out.println("请输入你的新密码：");
    String newPassword = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + username + "' AND password = '" + oldPassword + "';";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "UPDATE " + USER_TABLE + " SET password = '" + newPassword + "' WHERE username = '" 
            + username + "' AND password = '" + oldPassword + "';";
            stmt.executeUpdate(sql);
            System.out.println("修改密码成功");
        } else {
            System.out.println("修改密码失败，旧密码错误");
        }
        userPassword(username); //返回密码管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//用户重置密码的方法，输入用户名和新密码，如果用户名存在则更新密码，否则提示错误信息
public void resetUserPassword(String username) {
    System.out.println("请输入你要重置密码的用户的用户名：");
    //String username = scanner.nextLine(); //这一行是错误的
    String targetUsername = scanner.nextLine(); //用一个不同的变量名来存储用户输入的用户名
    System.out.println("请输入你要重置密码的用户的新密码：");
    String newPassword = scanner.nextLine();
    try {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE username = '" + targetUsername + "';"; //用targetUsername来进行数据库操作
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            sql = "UPDATE " + USER_TABLE + " SET password = '" + newPassword + "' WHERE username = '" + targetUsername + "';";
            stmt.executeUpdate(sql);
            System.out.println("重置密码成功");
        } else {
            System.out.println("重置密码失败，用户名不存在");
        }
        userPassword(username); //返回密码管理界面
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//用户将商品加入购物车的方法，输入商品编号和数量，如果编号存在且库存足够则添加到购物车表中，否则提示错误信息
public void addProductToCart(String username) {
    System.out.println("请输入你要加入购物车的商品的编号：");
    int productId = scanner.nextInt();
    scanner.nextLine(); //消除回车
    System.out.println("请输入你要加入购物车的商品的数量：");
    int quantity = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + productId + ";";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int stock = rs.getInt("stock");
            if (stock >= quantity) {
                sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE + " WHERE username = '" + username + "') AND product_id = " + productId + ";";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int oldQuantity = rs.getInt("quantity");
                    int newQuantity = oldQuantity + quantity;
                    sql = "UPDATE " + CART_TABLE + " SET quantity = " + newQuantity + " WHERE user_id = (SELECT id FROM "
                            + USER_TABLE + " WHERE username = '" + username
                            + "') AND product_id = " + productId + ";";
                    stmt.executeUpdate(sql);
                    System.out.println("添加商品成功");
                } else {
                    //如果购物车中没有该商品，则插入一条新记录
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
                //如果库存不足，则提示错误信息
                System.out.println("对不起，该商品的库存不足，请重新选择");
            }
        } else {
            //如果商品编号不存在，则提示错误信息
            System.out.println("对不起，该商品不存在，请重新输入");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//从购物车中移除商品的方法，输入商品编号和数量，如果编号存在且数量合理则从购物车表中删除或更新记录，否则提示错误信息
public void removeProductFromCart(String username) {
    System.out.println("请输入你要从购物车中移除的商品的编号：");
    int productId = scanner.nextInt();
    scanner.nextLine(); //消除回车
    System.out.println("请输入你要从购物车中移除的商品的数量：");
    int quantity = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE + " WHERE username = '" + username + "') AND product_id = " + productId + ";";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int oldQuantity = rs.getInt("quantity");
            if (oldQuantity >= quantity) {
                int newQuantity = oldQuantity - quantity;
                if (newQuantity == 0) {
                    //如果移除后数量为0，则删除该记录
                    sql = "DELETE FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM "
                            + USER_TABLE + " WHERE username = '" + username
                            + "') AND product_id = " + productId + ";";
                    stmt.executeUpdate(sql);
                    System.out.println("移除商品成功");
                } else {
                    //如果移除后数量不为0，则更新该记录
                    sql = "UPDATE " + CART_TABLE + " SET quantity = " + newQuantity + " WHERE user_id = (SELECT id FROM "
                            + USER_TABLE + " WHERE username = '" + username
                            + "') AND product_id = " + productId + ";";
                    stmt.executeUpdate(sql);
                    System.out.println("移除商品成功");
                }
            } else {
                //如果移除的数量大于购物车中的数量，则提示错误信息
                System.out.println("对不起，你不能移除超过购物车中的数量，请重新输入");
            }
        } else {
            //如果购物车中没有该商品，则提示错误信息
            System.out.println("对不起，你的购物车中没有该商品，请重新输入");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//修改购物车中的商品的方法，输入商品编号和新的数量，如果编号存在且数量合理则更新购物车表中的记录，否则提示错误信息
public void updateProductInCart(String username) {
    System.out.println("请输入你要修改的购物车中的商品的编号：");
    int productId = scanner.nextInt();
    scanner.nextLine(); //消除回车
    System.out.println("请输入你要修改的购物车中的商品的新的数量：");
    int newQuantity = scanner.nextInt();
    scanner.nextLine(); //消除回车
    try {
        String sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE + " WHERE username = '" + username + "') AND product_id = " + productId + ";";
        rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int oldQuantity = rs.getInt("quantity");
            if (newQuantity > 0) {
                sql = "UPDATE " + CART_TABLE + " SET quantity = " + newQuantity + " WHERE user_id = (SELECT id FROM "
                        + USER_TABLE + " WHERE username = '" + username
                        + "') AND product_id = " + productId + ";";
                stmt.executeUpdate(sql);
                System.out.println("修改商品成功");
            } else {
                //如果新的数量为0或负数，则提示错误信息
                System.out.println("对不起，你不能将购物车中的商品数量修改为0或负数，请重新输入");
            }
        } else {
            //如果购物车中没有该商品，则提示错误信息
            System.out.println("对不起，你的购物车中没有该商品，请重新输入");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//模拟结账的方法，输入用户名，计算购物车中的商品总价，清空购物车表，并将购物记录插入到历史表中
public void checkout(String username) {
    double totalPrice = 0.0;
    try {
        String sql = "SELECT * FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE + " WHERE username = '" + username + "');";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int productId = rs.getInt("product_id");
            int quantity = rs.getInt("quantity");
            sql = "SELECT * FROM " + PRODUCT_TABLE + " WHERE id = " + productId + ";";
            ResultSet rs2 = stmt.executeQuery(sql);
            if (rs2.next()) {
                double price = rs2.getDouble("price");
                totalPrice += price * quantity;
                //将购物记录插入到历史表中
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
        //清空购物车表
        sql = "DELETE FROM " + CART_TABLE + " WHERE user_id = (SELECT id FROM "
                + USER_TABLE + " WHERE username = '" + username
                + "');";
        stmt.executeUpdate(sql);
        System.out.println("结账成功，你的购物车总价为：" + totalPrice);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//查看购物历史的方法，输入用户名，查询历史表中的所有记录，并显示出来
public void viewShoppingHistory(String username) {
    try {
        String sql = "SELECT * FROM " + HISTORY_TABLE + " WHERE user_id = (SELECT id FROM " + USER_TABLE + " WHERE username = '" + username + "') ORDER BY date DESC;";
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