package com.example.finallandroidproject;
import java.util.ArrayList;

public class Cart {
    public String userPhone;
    public ArrayList<Watch> watches;
    public ArrayList<Integer> quantities;
    public int price;

    public Cart() {}

    public Cart(String userPhone){
        this.userPhone = userPhone;
        this.watches = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.price = 0;
    }

    public boolean AddWatch(Watch watch){
        this.watches.add(watch);
        this.price += watch.Price;
        watch.quantity--;
        return true;
    }
    public boolean DeleteWatch(Watch watch){
        if (this.watches.remove(watch)) {
            this.price -= watch.Price;
            watch.quantity++;
            return true;
        }
        return false;
    }

    public boolean Empty(){
        this.watches.clear();
        this.quantities.clear();
        this.price = 0;
        return true;
    }


}

