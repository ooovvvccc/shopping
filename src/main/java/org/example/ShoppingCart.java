package org.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShoppingCart {
     double price;
     String time;
     String userName;
     String payWay;
     List<Product> products=new ArrayList<>();
     ShoppingCart(){}
     ShoppingCart(String userName,double price,String time,String payWay){
          this.userName=userName;
          this.price=price;
          this.time=time;
          this.payWay=payWay;
     }

     public ShoppingCart(String userName, double price, Date time, String payWay) {
     }

     public void setProduct(Product product){
          products.add(product);
     }
     public void deleteProduct(Product product){
          products.remove(product);
     }
     public ShoppingCart clone(String userName,double price,String time,String payWay,ShoppingCart cart){
          ShoppingCart tempCart=new ShoppingCart(userName,price,time,payWay);
          for(Product product:cart.products){
               Product product1=new Product(product.name,product.price,product.count);
                    tempCart.products.add(product1);
          }
          return tempCart;
     }


}
