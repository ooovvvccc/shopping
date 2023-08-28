package org.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    String name;
    String password;
    ShoppingCart cart=new ShoppingCart();//购物车
    List<ShoppingCart> histories = new ArrayList<>();//购物历史
    User(){}
    User(String name,String password){
        this.name=name;
        this.password=password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCart(ShoppingCart cart){this.cart=cart;}
    public String getName(){return this.name;}
    public String getPassword(){return this.password;}
    public ShoppingCart getCart(){return this.cart;}
    public int changePassword(User user,int number){
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入新密码： ");
        String newPassword1 = scanner.nextLine();//键盘获取新密码
        System.out.println("请再次输入密码：");
        String newPassword2 = scanner.nextLine();//键盘再次获取新密码
        if (newPassword1.equals(newPassword2)) {
            user.setPassword(encryptPassword(newPassword1));
            System.out.println("密码修改成功！");
            number = 0;
        } else {
            System.out.println("密码修改失败！");
        }
        return number;
    }

    /* public void resetPassword(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入新密码：");
        String newPassword=scanner.nextLine();//键盘获取新密码
        setPassword(newPassword);
        System.out.println("用户密码重置成功！");
    }*/

    public void addProduct(List<Product> products){
        Scanner scanner=new Scanner(System.in);
        boolean index=false;//标识符
        System.out.println("请输入要加入的商品名称：");
        String name=scanner.nextLine();
        for (Product product : products) {
            if (product.name.equals(name)) {
                System.out.println("请输入要加入的商品数量：");
                int count = scanner.nextInt();
                for (Product product1 : cart.products) {
                   if(product1.name.equals(product.name)){
                       if(product1.count+count<=product.number) {
                           product1.count += count;
                           System.out.println("商品已加入购物车！");
                           index = true;
                       }else{
                           System.out.println("库存不足！");
                       }
                       return;
                   }
                }
                if(count<=product.number) {
                    product.count = count;
                    cart.setProduct(product);
                    System.out.println("商品已加入购物车！");
                }else{
                    System.out.println("库存不足！");
                    return;
                }
                index = true;
            }
        }
        if(!index){
            System.out.println("未找到此商品！");
        }
    }
    public void deleteProduct(){
        Scanner scanner=new Scanner(System.in);
        boolean number=false;
        System.out.println("请输入要删除的商品名称：");
        String name=scanner.nextLine();
        for (Product product : cart.products) {
            if (product.name.equals(name)) {
                cart.deleteProduct(product);
                System.out.println("商品已从购物车中删除！");
                number=true;
                break;
            }
        }
        if(!number){
            System.out.println("购物车未找到此商品！");
        }
    }
    public void listProduct(List<Product> products){
        int i=1;
        for (Product product : products) {
            System.out.println("商品"+i+"：商品名称："+product.name+" 价格："+product.price+"元 生产日期："
                    +product.proDate+" 到期日期："+product.expDate+" 库存："+product.number);
            i++;
        }
        if(i==1){
            System.out.println("无商品信息！");
        }
    }

    public void changeProduct(List<Product> products){
        boolean index=false;
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入要修改的商品名称：");
        String name=scanner.nextLine();
        for (Product product : cart.products) {
            if (product.name.equals(name)) {
                for (Product product1 : products) {
                    System.out.println("请输入要修改的商品数量：");
                    int count = scanner.nextInt();
                    if (count <= product1.number) {
                        product.count = count;
                        System.out.println("商品数量已经修改！");
                    } else {
                        System.out.println("库存不足！");
                    }
                    return;
                }
                index=true;
            }
        }
        if(!index){
            System.out.println("未找到此商品！");
        }
    }
    public void checkout(List<Product> products){
        Scanner scanner=new Scanner(System.in);
        double price=0;
        for (Product product : cart.products) {
            price+=product.count*product.price;
        }
        if(price==0){
            System.out.println("购物车无商品！");
            return;
        }
        System.out.println("购物车商品总价格为："+price+"元");
        System.out.println("请选择支付方式：1.微信支付   2.支付宝支付");
        int number=scanner.nextInt();
        switch (number){
            case 1:
                cart.payWay="微信支付";
                break;
            case 2:
                cart.payWay="支付宝支付";
                break;
            default:
                System.out.println("无效支付！");
                return;
        }
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(System.currentTimeMillis());
        String time=formatter.format(date);
        System.out.println("支付成功！ 时间："+time+" 总金额："+price+"元 支付方式："+cart.payWay);
        cart.price=price;
        cart.time=time;
        histories.add(cart.clone(this.name,cart.price,cart.time,cart.payWay,cart));
        for (Product product : cart.products) {
            for (Product product1 : products) {
                  if(product.name.equals(product1.name)){
                      product1.number-=product.count;
                  }
            }
        }
        cart.products.clear();
    }
    public void searchShopping(List<User> users){
     System.out.println("购物历史如下：");
     int i=1;
        for (ShoppingCart history : histories) {
           // for(User user:users) {
                //if (user.equals(history.userName)) {
                    System.out.println("第" + i + "次订单如下：");
                    /*for (Product product : history.products) {
                        System.out.println("商品名称：" + product.name + " 商品单价：" + product.price +
                                "元 购买商品数量：" + product.count);
                    }*/
                    i++;
                    System.out.println("总金额：" + history.price + "元 支付方式：" + history.payWay + " 时间：" + history.time);
                //}
            //}
        }
        if(i==1){
            System.out.println("无购物历史！");
        }
    }

    public void printProduct(){
        System.out.println("购物车如下：");
        int i=0;
        for (Product product : cart.products) {
                System.out.println("商品名称："+product.name+" 商品单价："+product.price+
                        "元 购物车商品数量："+product.count);
                i++;
        }
        if(i==0){
            System.out.println("购物车为空！");
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

    public void readTxt() {
        try {
            FileReader historyReader = new FileReader("histories.txt");
            Scanner historyScanner = new Scanner(historyReader);
            while (historyScanner.hasNextLine()) {
                String[] historyInfo = historyScanner.nextLine().split(",");
                if (this.name.equals(historyInfo[0])) {
                    ShoppingCart history = new ShoppingCart(historyInfo[0], Double.parseDouble(historyInfo[1]),
                            historyInfo[2], historyInfo[3]);
                    histories.add(history);
                }
            }
            historyReader.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
