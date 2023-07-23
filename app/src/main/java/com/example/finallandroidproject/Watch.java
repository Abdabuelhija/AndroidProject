package com.example.finallandroidproject;

public class Watch {
public String name;
public String Model;
public int Price ;

public int indexIMG;

public int quantity;
public Watch (String name,String Model,int Price,int quantity,int indexIMG){
    this.name=name;
    this.Model=Model;
    this.Price=Price;
    this.quantity=quantity;
    this.indexIMG=indexIMG;
}
    public Watch() {
        // Required for Firebase
    }

}
