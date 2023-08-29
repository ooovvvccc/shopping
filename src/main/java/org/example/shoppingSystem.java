package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class shoppingSystem<Workbook, HSSFWorkbook, HSSFSheet> {
    List<Administrator> administrators = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Product> products = new ArrayList<>();

    public void run() {
        
        Scanner scanner = new Scanner(System.in);
        int order = 0;
        int number = 0;//循环标识
        while (number == 0) {
            menu();
            try {
                order = scanner.nextInt();//指令值
            } catch (InputMismatchException e) {
                System.out.println("输入有误！");
                order = 6;
                scanner.nextLine();
            }
            switch (order) {
                case 1:
                    administratorLogin();
                    break;
                case 2:
                    userLogin();
                    break;
                case 3:
                    addUser();
                    break;
                case 4:
                    number = 1;
                    System.out.println("感谢使用本系统，再见！");
                    break;
                default:
                    System.out.println("请输入正确指令！");
                    break;
            }
        }

    }

    public void menu() {
        System.out.println("*********************************************************");
        System.out.println("                   欢迎使用购物管理系统！");
        System.out.println("                   请选择管理员或用户登录");
        System.out.println("         1.管理员   2.用户登录    3. 用户注册   4.退出系统");
        System.out.println("*********************************************************");
        System.out.print("输入指令：");
    }

    public void addUser() {
        Scanner scanner = new Scanner(System.in);
        boolean num = true;
        System.out.println("请输入新用户名：");
        String newName = scanner.nextLine();
        for (User user : users) {
            if (user.name.equals(newName)) {
                System.out.println("该用户已存在！");
                num = false;
                break;
            }
        }
        if (num) {
            System.out.println("请输入密码：");
            String newPassword1 = scanner.nextLine();
            System.out.println("请再次输入密码：");
            String newPassword2 = scanner.nextLine();
            if (newPassword1.equals(newPassword2)) {
                users.add(new User(newName, encryptPassword(newPassword2)));
                System.out.println("注册成功！");
            } else {
                System.out.println("注册失败，两次密码不一致！");
            }
        }
    }

    public void initializeAdministrator() {
        administrators.add(new Administrator("admin", "ynuinfo#777"));
    }

    public void initializeUser() {
        users.add(new User("Annnn", "Cxy237237."));
    }

    public void initializeProduct() {
        products.add(new Product("牛奶", 3.0, "2023/8/1", "2025/8/1", 50));
        products.add(new Product("面包", 2.0, "2023/9/10", "2025/7/10", 50));
    }

    public void administratorLogin() {
        Scanner scanner = new Scanner(System.in);
        int num = 3;//登录次数
        System.out.print("请输入管理者名:");
        String newName = scanner.nextLine();//键盘获取用户名
        while (num > 0) {
            for (Administrator administrator : administrators) {
                if (administrator.getName().equals(newName)) {
                    System.out.print("请输入密码:");
                    String newPassword = scanner.nextLine();//键盘获取密码
                    if (administrator.getPassword().equals(encryptPassword(newPassword))) {
                        System.out.println("登录成功");
                        administratorMenu(administrator);
                        num = 0;
                        break;
                    } else if (num < 2) {
                        System.out.println("输入次数已达上限，请五分钟后再试");
                        num = 0;
                        break;
                    } else {
                        System.out.println("登录失败，你还有" + --num + "次机会");
                    }
                } else {
                    System.out.println("管理员不存在！");
                    num = 0;
                }
            }
        }
    }

    public void userLogin() {
        Scanner scanner = new Scanner(System.in);
        int num = 0;
        System.out.print("请输入用户名:");
        String newName = scanner.nextLine();//键盘获取用户名
        System.out.print("请输入密码:");
        String newPassword = scanner.nextLine();//键盘获取密码
        for (User user : users) {
            if (user.getName().equals(newName) && user.getPassword().equals(encryptPassword(newPassword))) {
                System.out.println("登录成功");
                userMenu(user);
                num = 1;
                break;
            }
        }
        if (num == 0) {
            System.out.println("用户名或密码错误，请重新登录！");
        }
    }

    public void administratorMenu(Administrator administrator) {
        Scanner scanner = new Scanner(System.in);
        int number = 1;//循环标识
        while (number == 1) {
            System.out.println("****************管理员系统*************************");
            System.out.println("1.修改自身密码  2.重置用户密码  3.列出所有客户信息");
            System.out.println("4.删除客户信息  5.查询客户信息  6.列出所有商品信息");
            System.out.println("7.添加商品信息  8.修改商品信息  9.删除商品信息");
            System.out.println("10.查询商品信息 11.退出登录");
            System.out.println("***************************************************");
            System.out.print("请输入指令：");
            int order = 0;//指令值
            try {
                order = scanner.nextInt();//指令值
            } catch (InputMismatchException e) {
                System.out.println("输入有误！");
                scanner.nextLine();
            }
            switch (order) {
                case 1:
                    number = administrator.changePassword(administrator, number);
                    break;
                case 2:
                    administrator.resetUserPassword(users);
                    break;
                case 3:
                    administrator.listUserInform(users);
                    break;
                case 4:
                    administrator.deleteUserInform(users);
                    break;
                case 5:
                    administrator.searchUserInform(users);
                    break;
                case 6:
                    administrator.listProduct(products);
                    break;
                case 7:
                    administrator.addProduct(products);
                    break;
                case 8:
                    administrator.changeProduct(products);
                    break;
                case 9:
                    administrator.deleteProduct(products);
                    break;
                case 10:
                    administrator.searchProduct(products);
                    break;
                case 11:
                    number = 0;
                    System.out.println("已退出登录！");
                    break;
                default:
                    System.out.println("请输入正确指令！");
                    break;
            }
        }
    }

    public void userMenu(User user) {
        Scanner scanner = new Scanner(System.in);
        int number = 1;//循环标识
        while (number == 1) {
            System.out.println("*****************用户系统*****************************");
            System.out.println("1.修改密码      2.将商品加入购物车  3.从购物车中移除商品");
            System.out.println("4.展示商品      5.展示购物车        6.模拟结账  ");
            System.out.println("7.查看购物历史  8.退出登录");
            System.out.println("*********************************************************");
            System.out.print("请输入指令：");
            int order = 0;//指令值
            try {
                order = scanner.nextInt();//指令值
            } catch (InputMismatchException e) {
                System.out.println("输入有误！");
                scanner.nextLine();
            }
            switch (order) {
                case 1:
                    number = user.changePassword(user, number);
                    break;
                case 2:
                    user.addProduct(products);
                    break;
                case 3:
                    user.deleteProduct();
                    break;
                case 4:
                    user.listProduct(products);
                    break;
                case 5:
                    user.printProduct();
                    break;
                case 6:
                    user.checkout(products);
                    break;
                case 7:
                    user.searchShopping(users);
                    break;
                case 8:
                    number = 0;
                    System.out.println("已退出登录！");
                    break;
                default:
                    System.out.println("请输入正确指令！");
                    break;
            }
        }
    }
    //使用TXT
    public void saveFile(){
             try {
                 FileWriter userWriter = new FileWriter("users.xlsx");
                 for (User user : users) {
                     userWriter.write(user.getName() + "," + user.getPassword() + "\n");
                 }
                 userWriter.close();

                 FileWriter historyWriter = new FileWriter("histories.xlsx");
                 for (User user : users) {
                     for (ShoppingCart history : user.histories) {
                         historyWriter.write(history.userName+","+history.price + "," + history.time + "," + history.payWay + "\n");
                     }
                 }
                     historyWriter.close();

                     FileWriter productWriter = new FileWriter("products.xlsx");
                     for (Product product : products) {
                         productWriter.write(product.getName() + "," + product.getPrice() + "," + product.getProDate() + "," +
                                 product.getExpDate() + "," + product.getNumber() + "\n");
                     }
                     productWriter.close();

                     FileWriter administratorWriter = new FileWriter("administrators.xlsx");
                     for (Administrator administrator : administrators) {
                         administratorWriter.write(administrator.getName() + "," + administrator.getPassword() + "\n");
                     }
                     administratorWriter.close();
             } catch(IOException e){
                     e.printStackTrace();
                 }
             }
            public void loadFile(){
            try {
                FileReader userReader = new FileReader("users.xlsx");
                Scanner userScanner = new Scanner(userReader);
                while (userScanner.hasNextLine()) {
                    String[] userInfo = userScanner.nextLine().split(",");
                    User user = new User(userInfo[0], userInfo[1]);
                    users.add(user);
                }
                userReader.close();
                //读txt历史
                for (User user : users) {
                    user.readTxt();
                }

                FileReader administratorReader = new FileReader("administrators.xlsx");
                Scanner administratorScanner= new Scanner(administratorReader);
                while ( administratorScanner.hasNextLine()) {
                    String[] administratorInfo =  administratorScanner.nextLine().split(",");
                    Administrator administrator = new Administrator(administratorInfo[0], administratorInfo[1]);
                    administrators.add(administrator);
                }
                administratorReader.close();

                FileReader productReader = new FileReader("products.xlsx");
                Scanner productScanner = new Scanner(productReader);
                while (productScanner.hasNextLine()) {
                    String[] productInfo = productScanner.nextLine().split(",");
                    Product product = new Product(productInfo[0],Double.parseDouble(productInfo[1]),productInfo[2],
                            productInfo[3], Integer.parseInt(productInfo[4]));
                    products.add(product);
                }
                productReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}

