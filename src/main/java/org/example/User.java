package org.example;

public class User {
    // 用户注册的方法，输入用户名和密码，如果用户名不存在则创建新用户，否则提示用户名已存在
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
                userRegister(); // 重新注册
            } else {
                sql = "INSERT INTO " + USER_TABLE + " (username, password) VALUES ('" + username + "', '" + password
                        + "');";
                stmt.executeUpdate(sql);
                System.out.println("注册成功，欢迎你，" + username);
                userMenu(username); // 进入用户界面
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 用户登录的方法，输入用户名和密码，如果正确则进入用户界面，否则提示错误信息
    public void userSignIn() {
        System.out.println("请输入你的用户名：");
        // String username.println("请选择你要进行的操作："); //这一行是错误的
        String username = scanner.nextLine(); // 获取用户输入的用户名
        System.out.println("请选择你要进行的操作："); // 打印出菜单选项
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
                start(); // 退出登录
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                adminMenu(); // 重新显示菜单
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
        // 用户密码管理的方法，显示密码管理选项菜单，并根据选择执行相应操作
    public void userPassword(String username) {
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 修改密码");
        System.out.println("2. 重置密码");
        System.out.println("3. 返回");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                changeUserPassword(username); // 修改密码
                break;
            case 2:
                resetUserPassword(username); // 重置密码
                break;
            case 3:
                userMenu(username); // 返回
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                userPassword(username); // 重新显示菜单
                break;
        }
    }
    
    // 用户购物的方法，显示购物选项菜单，并根据选择执行相应操作
    public void userShopping(String username) {
        System.out.println("请选择你要进行的操作：");
        System.out.println("1. 将商品加入购物车");
        System.out.println("2. 从购物车中移除商品");
        System.out.println("3. 修改购物车中的商品");
        System.out.println("4. 模拟结账");
        System.out.println("5. 查看购物历史");
        System.out.println("6. 返回");
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消除回车
        switch (choice) {
            case 1:
                addProductToCart(username); // 将商品加入购物车
                break;
            case 2:
                removeProductFromCart(username); // 从购物车中移除商品
                break;
            case 3:
                updateProductInCart(username); // 修改购物车中的商品
                break;
            case 4:
                checkout(username); // 模拟结账
                break;
            case 5:
                viewShoppingHistory(username); // 查看购物历史
                break;
            case 6:
                userMenu(username); // 返回
                break;
            default:
                System.out.println("无效的输入，请重新输入");
                userShopping(username); // 重新显示菜单
                break;
        }
    }

    // 管理员修改自身密码的方法，输入旧密码和新密码，如果旧密码正确则更新密码，否则提示错误信息
    public void changeAdminPassword() {
        System.out.println("请输入你的旧密码：");
        String oldPassword = scanner.nextLine();
        System.out.println("请输入你的新密码：");
        String newPassword = scanner.nextLine();
        try {
            String sql = "SELECT * FROM " + ADMIN_TABLE + " WHERE password = '" + oldPassword + "';";
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                sql = "UPDATE " + ADMIN_TABLE + " SET password = '" + newPassword + "' WHERE password = '" + oldPassword
                        + "';";
                stmt.executeUpdate(sql);
                System.out.println("修改密码成功");
            } else {
                System.out.println("修改密码失败，旧密码错误");
            }
            adminPassword(); // 返回密码管理界面
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
