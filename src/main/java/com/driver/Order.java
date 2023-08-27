package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        int DeliveryTime=Integer.valueOf(deliveryTime);
        int a=DeliveryTime/10;
        int b=DeliveryTime%10;
        DeliveryTime=(a*60)+b;
        this.id=id;
        this.deliveryTime=DeliveryTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
