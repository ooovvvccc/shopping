package org.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;
public class Administrator {
    String name;
    String password;
    boolean num=false;//标识
    Administrator(String name,String password){
        this.name=name;
        this.password=password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {this.name = name;}
    public String getName(){return this.name;}
    public String getPassword(){return this.password;}
    public int changePassword(Administrator administrator,int index){
        Scanner scanner=new Scanner(System.in);
        index = 1;//循环标识
        System.out.println("请输入新密码：");
        String newPassword1 = scanner.nextLine();//键盘获取新密码
        System.out.println("请再次输入密码：");
        String newPassword2 = scanner.nextLine();//键盘再次获取新密码
        if (newPassword1.equals(newPassword2)) {
            administrator.setPassword(encryptPassword(newPassword1));
            System.out.println("密码修改成功！");
            index = 0;
        } else {
            System.out.println("密码修改失败！");
        }
        return index;
    }
    public void resetUserPassword(List<User> users){
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入要重置密码的用户的用户名：");
        String username = scanner.nextLine();
        for (User user : users) {
            if (user.getName().equals(username)) {
                System.out.println("请输入新密码：");
                String newPassword = scanner.nextLine();
                user.setPassword(encryptPassword(newPassword));
                System.out.println("密码重置成功！");
                return;
            }
        }
        System.out.println("未找到该用户！");
    }
    public void listUserInform(List<User> users){
        int i=1;
        for (User user : users) {
            System.out.println("用户"+i+"：用户名："+user.name+" 密码："+user.password);
            i++;
        }
        if(i==1){
            System.out.println("无用户信息！");
        }
    }

    public void deleteUserInform(List<User> users){
        Scanner scanner=new Scanner(System.in);
        num=false;
        System.out.println("请输入要删除用户的用户名：");
        String name = scanner.nextLine();
        for (User user : users) {
             if(user.name.equals(name)){
                 users.remove(user);
                 System.out.println("用户删除成功！");
                 num=true;
                 return;
             }
        }
        if(!num){
            System.out.println("不存在该用户！");
        }
    }
    public void searchUserInform(List<User> users){
        Scanner scanner=new Scanner(System.in);
        num=false;
        System.out.println("请输入要查询用户的用户名：");
        String name = scanner.nextLine();
        for (User user : users) {
            if(user.name.equals(name)){
                System.out.println("用户信息：用户名："+user.name+" 密码："+user.password);
                num=true;
                return;
            }
        }
        if(!num){
            System.out.println("不存在该用户！");
        }
    }
    public void listProduct(List<Product> products){
        int i=1;
        for (Product product : products) {
            System.out.println("商品"+i+"：商品名称："+product.name+" 价格："+product.price+"元 生产日期："
                    +product.proDate+" 到期日期："+product.expDate);
            i++;
        }
        if(i==1){
            System.out.println("无商品信息！");
        }
    }
    public void addProduct(List<Product> products){
        Scanner scanner=new Scanner(System.in);
        num=true;
        while(num) {
            System.out.println("请输入需要添加商品的名称：");
            String name = scanner.nextLine();
            for (Product product : products) {
                if (product.name.equals(name)) {
                    System.out.println("该商品已存在！");
                    num = true;
                    break;
                }
            }
            num=false;
            if(!num){
                System.out.println("请输入需要添加商品的价格：");
                double price=scanner.nextDouble();
                System.out.println("请输入需要添加商品的生产日期：");
                scanner.nextLine();
                String proData=scanner.nextLine();
                System.out.println("请输入需要添加商品的到期日期：");
                String expData=scanner.nextLine();
                System.out.println("请输入需要添加商品的库存：");
                int number=scanner.nextInt();
                products.add(new Product(name,price,proData,expData,number));
                System.out.println("商品添加成功！");
            }
        }
    }
    public void changeProduct(List<Product> products){
        Scanner scanner=new Scanner(System.in);
        num=false;
        System.out.println("请输入需要修改商品的名称：");
        String name = scanner.nextLine();
        for (Product product : products) {
            if (product.name.equals(name)) {
                System.out.println("请输入修改后商品的名称：");
                String newName = scanner.nextLine();
                System.out.println("请输入修改后商品的价格：");
                double price=scanner.nextDouble();
                System.out.println("请输入修改后商品的生产日期：");
                scanner.nextLine();
                String proData=scanner.nextLine();
                System.out.println("请输入修改后商品的到期日期：");
                String expData=scanner.nextLine();
                System.out.println("请输入修改后商品的库存：");
                int number=scanner.nextInt();
                product.setName(newName);
                product.setPrice(price);
                product.setProDate(proData);
                product.setExpDate(expData);
                product.setNumber(number);
                System.out.println("商品修改成功！");
                num=true;
                break;
            }
        }
        if(!num){
            System.out.println("未找到该商品！");
        }
    }
    public void deleteProduct(List<Product> products){
        Scanner scanner=new Scanner(System.in);
        num=false;
        System.out.println("请输入要删除商品的名称：");
        String name = scanner.nextLine();
        for (Product product : products) {
            if(product.name.equals(name)){
                products.remove(product);
                System.out.println("商品删除成功！");
                num=true;
                return;
            }
        }
        if(!num){
            System.out.println("不存在该商品！");
        }
    }
    public void searchProduct(List<Product> products){
        Scanner scanner=new Scanner(System.in);
        num=false;
        System.out.println("请输入要查询商品的名称：");
        String name = scanner.nextLine();
        for (Product product : products) {
            if(product.name.equals(name)){
                System.out.println("商品信息"+"：商品名称："+product.name+" 价格："+product.price+"元 生产日期："
                        +product.proDate+" 到期日期："+product.expDate+" 库存："+product.number);
                num=true;
                return;
            }
        }
        if(!num){
            System.out.println("不存在该商品！");
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
