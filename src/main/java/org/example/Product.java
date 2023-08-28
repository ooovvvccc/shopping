package org.example;
import java.util.Date;

public class Product {
    double price;
    String name;
    String proDate;
    String expDate;
    int number;//库存
    int count;//购物车中商品的数量
    Product(String name,double price,String proDate,String expDate,int number){
        this.name=name;
        this.price=price;
        this.proDate=proDate;
        this.expDate=expDate;
        this.number=number;
    }
    Product(String name,double price,int count){
        this.name=name;
        this.price=price;
        this.count=count;
    }

    public Product(String name, double price, Date proDate, Date expDate, int number) {
    }

    public void setPrice(double price){this.price=price;}
    public void setName(String name){this.name=name;}
    public void setProDate(String proDate){this.proDate=proDate;}
    public void setExpDate(String expDate){this.expDate=expDate;}
    public void setNumber(int number){this.number=number;}
    public void setCount(int count){this.count=count;}
    public double getPrice() {return this.price;}
    public String getName() {return this.name;}
    public String getExpDate() {return this.expDate;}
    public String getProDate() {return this.proDate;}
    public int getNumber(){return this.number;}

}
