package org.example;

public class Main {
    public static void main(String[] args){
        shoppingSystem shopping=new shoppingSystem();
        shopping.loadFile();
        shopping.run();
        shopping.saveFile();
    }
}
