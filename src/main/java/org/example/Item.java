package org.example;

public class Item {
    // 属性
    private int id; // 商品编号
    private String name; // 商品名称
    private double price; // 商品价格
    private int stock; // 商品库存
    private int quantity; // 商品数量

    // 构造方法，接收编号，名称，价格和库存作为参数，并设置数量为0
    public Item(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.quantity = 0;
    }

    // 获取商品编号的方法，返回商品编号
    public int getId() {
        return id;
    }

    // 获取商品名称的方法，返回商品名称
    public String getName() {
        return name;
    }

    // 获取商品价格的方法，返回商品价格
    public double getPrice() {
        return price;
    }

    // 获取商品库存的方法，返回商品库存
    public int getStock() {
        return stock;
    }

    // 获取商品数量的方法，返回商品数量
    public int getQuantity() {
        return quantity;
    }

    // 设置商品数量的方法，接收一个新的数量作为参数，无返回值
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // 重写toString方法，返回商品信息的字符串表示
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", quantity=" + quantity +
                '}';
    }
}