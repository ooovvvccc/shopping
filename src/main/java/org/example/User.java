package org.example;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class User {
    // 属性
    private int id; // 用户id
    private String username; // 用户名
    private String password; // 密码
    private boolean loggedIn; // 登录状态
    private DBHelper dbHelper; // 数据库操作对象
    private List<Item> cart; // 购物车列表
    private Scanner scanner; // 输入扫描器

    // 构造方法，接收用户名和密码作为参数，并创建数据库操作对象和购物车列表对象和输入扫描器对象
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.loggedIn = false;
        this.dbHelper = new DBHelper();
        this.cart = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // 注册方法，返回布尔值表示是否注册成功
    public boolean register() {
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造插入语句，使用占位符防止SQL注入攻击
            String sql = "INSERT INTO " + userTable + " (username, password) VALUES (?, ?);";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            // 执行插入语句，获取影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                return true; // 注册成功，返回true
            } else {
                return false; // 注册失败，返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 发生异常，返回false
        }
    }

    // 登录方法，返回布尔值表示是否登录成功
    public boolean login() {
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造查询语句，使用占位符防止SQL注入攻击
            String sql = "SELECT * FROM " + userTable + " WHERE username = ? AND password = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            // 执行查询语句，获取结果集对象
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id"); // 获取用户id并赋值给属性id
                loggedIn = true; // 设置登录状态为true
                return true; // 登录成功，返回true
            } else {
                return false; // 登录失败，返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 发生异常，返回false
        }
    }

    // 修改密码的方法，接收旧密码和新密码作为参数，返回布尔值表示是否修改成功
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!oldPassword.equals(password)) {
            return false; // 旧密码错误，返回false
        }
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 构造更新语句，使用占位符防止SQL注入攻击
            String sql = "UPDATE " + userTable + " SET password = ? WHERE id = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, id);
            // 执行更新语句，获取影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                password = newPassword; // 更新密码属性
                return true; // 修改成功，返回true
            } else {
                return false; // 修改失败，返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 发生异常，返回false
        }
    }

    // 重置密码的方法，返回布尔值表示是否重置成功
    public boolean resetPassword() {
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String userTable = dbHelper.USER_TABLE;
            // 生成一个随机的六位数作为新密码
            Random random = new Random();
            int num = random.nextInt(900000) + 100000;
            String newPassword = String.valueOf(num);
            // 构造更新语句，使用占位符防止SQL注入攻击
            String sql = "UPDATE " + userTable + " SET password = ? WHERE id = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, id);
            // 执行更新语句，获取影响的行数
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                password = newPassword; // 更新密码属性
                System.out.println("你的新密码是：" + newPassword); // 显示新密码
                return true; // 重置成功，返回true
            } else {
                return false; // 重置失败，返回false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 发生异常，返回false
        }
    }

    // 将商品添加到购物车的方法，接收一个商品对象作为参数，无返回值
    public void addToCart(Item item) {
        cart.add(item); // 将商品对象添加到购物车列表中
    }

    // 从购物车中移除商品的方法，接收一个商品对象作为参数，无返回值
    public void removeFromCart(Item item) {
        cart.remove(item); // 将商品对象从购物车列表中移除
    }

    // 修改购物车中的商品数量的方法，接收一个商品编号和一个新的数量作为参数，无返回值
    public void modifyCart(int id, int quantity) {
        for (Item item : cart) {
            if (item.getId() == id) {
                item.setQuantity(quantity); // 设置商品对象的数量属性为新的数量
                break;
            }
        }
    }

    // 模拟结账的方法，返回布尔值表示是否结账成功
    public boolean checkout() {
        if (cart.isEmpty()) {
            return false; // 购物车为空，结账失败，返回false
        }
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String cartTable = dbHelper.CART_TABLE;
            String historyTable = dbHelper.HISTORY_TABLE;
            String productTable = dbHelper.PRODUCT_TABLE;
            // 获取当前日期并格式化为字符串
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(date);
            // 遍历购物车列表中的每个商品对象
            for (Item item : cart) {
                // 获取商品对象的属性值
                int productId = item.getId();
                String productName = item.getName();
                double productPrice = item.getPrice();
                int productStock = item.getStock();
                int quantity = item.getQuantity();
                double total = productPrice * quantity;
                // 构造插入语句，将购物车中的商品信息插入到历史记录表中
                String sql1 = "INSERT INTO " + historyTable + " (user_id, product_id, quantity, total, date) VALUES (?, ?, ?, ?, ?);";
                // 使用连接对象创建预编译语句对象，并设置参数值
                PreparedStatement pstmt1 = connection.prepareStatement(sql1);
                pstmt1.setInt(1, id);
                pstmt1.setInt(2, productId);
                pstmt1.setInt(3, quantity);
                pstmt1.setDouble(4, total);
                pstmt1.setString(5, dateString);
                // 执行插入语句，获取影响的行数
                int rows1 = pstmt1.executeUpdate();
                if (rows1 > 0) {
                    // 插入成功，构造更新语句，将商品表中的商品库存减去购买数量
                    String sql2 = "UPDATE " + productTable + " SET stock = stock - ? WHERE id = ?;";
                    // 使用连接对象创建预编译语句对象，并设置参数值
                    PreparedStatement pstmt2 = connection.prepareStatement(sql2);
                    pstmt2.setInt(1, quantity);
                    pstmt2.setInt(2, productId);
                    // 执行更新语句，获取影响的行数
                    int rows2 = pstmt2.executeUpdate();
                    if (rows2 > 0) {
                        // 更新成功，打印结账信息
                        System.out.println("你已购买" + quantity + "件" + productName + "，共花费" + total + "元");
                    } else {
                        // 更新失败，打印错误信息
                        System.out.println("更新商品库存失败，请联系管理员");
                    }
                } else {
                    // 插入失败，打印错误信息
                    System.out.println("插入历史记录失败，请联系管理员");
                }
            }
            // 清空购物车列表
            cart.clear();
            return true; // 结账成功，返回true
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 发生异常，结账失败，返回false
        }
    }

    // 查看购物历史的方法，无返回值
    public void viewHistory() {
        try {
            // 使用DBHelper对象获取数据库连接对象和表名
            Connection connection = dbHelper.getConnection();
            String historyTable = dbHelper.HISTORY_TABLE;
            String productTable = dbHelper.PRODUCT_TABLE;
            // 构造查询语句，使用连接操作符和子查询来查询历史记录表和商品表中的相关信息
            String sql = "SELECT h.quantity, h.total, h.date, p.name FROM " + historyTable + " h JOIN " + productTable + " p ON h.product_id = p.id WHERE h.user_id = ?;";
            // 使用连接对象创建预编译语句对象，并设置参数值
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            // 执行查询语句，获取结果集对象
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 有历史记录，显示历史记录信息
                System.out.println("以下是你的购物历史：");
                do {
                    // 获取结果集中的字段值并打印
                    int quantity = rs.getInt("quantity");
                    double total = rs.getDouble("total");
                    String date = rs.getString("date");
                    String name = rs.getString("name");
                    System.out.println(date + " 购买了" + quantity + "件" + name + "，共花费" + total + "元");
                } while (rs.next());
            } else {
                // 没有历史记录，显示提示信息
                System.out.println("你还没有购物历史，请先购买商品");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库操作异常，请稍后重试");
        }
    }

    // 退出登录的方法，无返回值
    public void logout() {
        loggedIn = false; // 设置登录状态为false
        System.out.println("你已退出登录，感谢你的使用");
    }

    // 获取用户id的方法，返回用户id
    public int getId() {
        return id;
    }

    // 获取用户名的方法，返回用户名
    public String getUsername() {
        return username;
    }

    // 获取密码的方法，返回密码
    public String getPassword() {
        return password;
    }

    // 获取登录状态的方法，返回登录状态
    public boolean isLoggedIn() {
        return loggedIn;
    }

    // 获取购物车列表的方法，返回购物车列表
    public List<Item> getCart() {
        return cart;
    }

    // 重写toString方法，返回用户信息的字符串表示
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", loggedIn=" + loggedIn +
                '}';
            }

            // 显示用户菜单的方法，无返回值
            public void userMenu() {
                System.out.println("欢迎你，" + username);
                System.out.println("请选择你要进行的操作：");
                System.out.println("1. 密码管理");
                System.out.println("2. 购物");
                System.out.println("3. 查看购物历史");
                System.out.println("4. 退出登录");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消除回车
                switch (choice) {
                    case 1:
                        passwordManage(); // 密码管理
                        break;
                    case 2:
                        shopping(); // 购物
                        break;
                    case 3:
                        viewHistory(); // 查看购物历史
                        break;
                    case 4:
                        logout(); // 退出登录
                        break;
                    default:
                        System.out.println("无效的输入，请重新输入");
                        userMenu(); // 重新显示用户菜单
                        break;
                }
            }
        
            // 密码管理的方法，无返回值
            public void passwordManage() {
                System.out.println("请选择你要进行的操作：");
                System.out.println("1. 修改密码");
                System.out.println("2. 重置密码");
                System.out.println("3. 返回上一级");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消除回车
                switch (choice) {
                    case 1:
                        changePasswordMenu(); // 修改密码菜单
                        break;
                    case 2:
                        resetPasswordMenu(); // 重置密码菜单
                        break;
                    case 3:
                        userMenu(); // 返回用户菜单
                        break;
                    default:
                        System.out.println("无效的输入，请重新输入");
                        passwordManage(); // 重新显示密码管理界面
                        break;
                }
            }
        
            // 修改密码菜单的方法，无返回值
            public void changePasswordMenu() {
                System.out.println("请输入你的旧密码：");
                String oldPassword = scanner.nextLine();
                System.out.println("请输入你的新密码：");
                String newPassword = scanner.nextLine();
                
               if (changePassword(oldPassword, newPassword)) {
                   // 修改成功，显示提示信息并返回密码管理界面
                   System.out.println("你已成功修改密码，请牢记你的新密码");
                   passwordManage();
               } else {
                   // 修改失败，显示提示信息并返回修改密码菜单
                   System.out.println("你输入的旧密码错误，请重新输入");
                   changePasswordMenu();
               }
                
            }
        
            // 重置密码菜单的方法，无返回值
            public void resetPasswordMenu() {
                
               if (resetPassword()) {
                   // 重置成功，显示提示信息并返回密码管理界面
                   System.out.println("你已成功重置密码，请牢记你的新密码");
                   passwordManage();
               } else {
                   // 重置失败，显示提示信息并返回重置密码菜单
                   System.out.println("重置密码失败，请稍后重试或联系管理员");
                   resetPasswordMenu();
               }
                
            }
        
            // 购物的方法，无返回值
            public void shopping() {
                System.out.println("请选择你要进行的操作：");
                System.out.println("1. 浏览商品");
                System.out.println("2. 查看购物车");
                System.out.println("3. 结账");
                System.out.println("4. 返回上一级");
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消除回车
                switch (choice) {
                    case 1:
                        browseItems(); // 浏览商品
                        break;
                    case 2:
                        viewCart(); // 查看购物车
                        break;
                    case 3:
                        checkout(); // 结账
                        break;
                    case 4:
                        userMenu(); // 返回用户菜单
                        break;
                    default:
                        System.out.println("无效的输入，请重新输入");
                        shopping(); // 重新显示购物界面
                        break;
                }
            }
        
            // 浏览商品的方法，从数据库中获取所有商品信息，并显示在控制台，让用户选择是否添加到购物车或返回上一级
            public void browseItems() {
                
               try {
                   List<Item> items = dbHelper.getAllItems(); // 获取所有商品信息
                   if (items.isEmpty()) {
                       // 商品列表为空，显示提示信息
                       System.out.println("暂无商品信息，请联系管理员添加商品");
                   } else {
                       // 商品列表不为空，显示商品信息
                       System.out.println("以下是所有商品信息：");
                       for (Item item : items) {
                           System.out.println(item);
                       }
                       // 让用户选择是否添加到购物车或返回上一级
                       System.out.println("请输入你要添加到购物车的商品编号，或输入0返回上一级：");
                       int id = scanner.nextInt();
                       scanner.nextLine(); // 消除回车
                       if (id == 0) {
                           // 返回上一级
                           shopping();
                       } else {
                           // 查找商品对象
                           Item item = null;
                           for (Item i : items) {
                               if (i.getId() == id) {
                                   // 找到商品对象，跳出循环
                                   item = i;
                                   break;
                               }
                           }
                           if (item == null) {
                               // 没有找到商品对象，显示提示信息
                               System.out.println("无效的商品编号，请重新输入");
                               browseItems(); // 重新浏览商品
                           } else {
                               // 找到商品对象，让用户输入购买数量
                               System.out.println("请输入你要购买的数量：");
                               int quantity = scanner.nextInt();
                               scanner.nextLine(); // 消除回车
                               if (quantity <= 0) {
                                   // 购买数量不合法，显示提示信息
                                   System.out.println("无效的购买数量，请重新输入");
                                   browseItems(); // 重新浏览商品
                               } else {
                                   // 购买数量合法，设置商品对象的数量属性
                                   item.setQuantity(quantity);
                                   // 调用用户对象的添加到购物车方法
                                   addToCart(item);
                                   // 显示添加成功信息
                                   System.out.println("已将" + item.getName() + "添加到购物车");
                                   browseItems(); // 重新浏览商品
                               }
                           }
                       }
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
                   System.out.println("数据库操作异常，请稍后重试");
                   shopping(); // 返回购物界面
               }
                
            }
        
            // 查看购物车的方法，获取用户对象的购物车列表，并显示在控制台，让用户选择是否修改或删除购物车中的商品或返回上一级
            public void viewCart() {
                List<Item> cart = getCart(); // 获取用户对象的购物车列表
                if (cart.isEmpty()) {
                    // 购物车列表为空，显示提示信息
                    System.out.println("你的购物车为空，请先添加商品");
                    shopping(); // 返回购物界面
                } else {
                    // 购物车列表不为空，显示购物车信息
                    System.out.println("以下是你的购物车信息：");
                    for (Item item : cart) {
                        System.out.println(item);
                    }
                    // 让用户选择是否修改或删除购物车中的商品
                    System.out.println("请输入你要修改或删除的商品编号，或输入0返回上一级：");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // 消除回车
                    if (id == 0) {
                        // 返回上一级
                        shopping();
                    } else {
                        // 查找商品对象
                        Item item = null;
                        for (Item i : cart) {
                            if (i.getId() == id) {
                                // 找到商品对象，跳出循环
                                item = i;
                                break;
                            }
                        }
                        if (item == null) {
                            // 没有找到商品对象，显示提示信息
                            System.out.println("无效的商品编号，请重新输入");
                            viewCart(); // 重新查看购物车
                        } else {
                            // 找到商品对象，让用户选择是修改还是删除
                            System.out.println("请选择你要进行的操作：");
                            System.out.println("1. 修改数量");
                            System.out.println("2. 删除商品");
                            int choice = scanner.nextInt();
                            scanner.nextLine(); // 消除回车
                            switch (choice) {
                                case 1:
                                    // 修改数量，让用户输入新的数量
                                    System.out.println("请输入新的数量：");
                                    int quantity = scanner.nextInt();
                                    scanner.nextLine(); // 消除回车
                                    if (quantity <= 0) {
                                        // 新的数量不合法，显示提示信息
                                        System.out.println("无效的数量，请重新输入");
                                        viewCart(); // 重新查看购物车
                                    } else {
                                        // 新的数量合法，调用用户对象的修改购物车方法
                                        modifyCart(id, quantity);
                                        // 显示修改成功信息
                                        System.out.println("已将" + item.getName() + "的数量修改为" + quantity);
                                        viewCart(); // 重新查看购物车
                                    }
                                    break;
                                case 2:
                                    // 删除商品，调用用户对象的从购物车中移除方法
                                    removeFromCart(item);
                                    // 显示删除成功信息
                                    System.out.println("已将" + item.getName() + "从购物车中移除");
                                    viewCart(); // 重新查看购物车
                                    break;
                                default:
                                    System.out.println("无效的输入，请重新输入");
                                    viewCart(); // 重新查看购物车
                                    break;
                            }
                        }
                    }
                }
            }
        
            // 结账的方法，调用用户对象的模拟结账方法，并显示结账结果
            public void checkoutMenu() {
                
               if (checkout()) {
                   // 结账成功，显示结账结果
                   System.out.println("结账成功，感谢你的购买");
               } else {
                   // 结账失败，显示结账结果
                   System.out.println("结账失败，你的购物车为空或数据库操作异常");
               }
                
                shopping(); // 返回购物界面
            }
        }
    
