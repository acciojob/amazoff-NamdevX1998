package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository=new OrderRepository();

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.getOrderById(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return orderRepository.getPartnerById(partnerId);
    }
    public Integer getOrderCountByPartnerId(String partnerId){
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }
    public static List<String> getOrdersByPartnerId(String partnerId) {
        return OrderRepository.getOrdersByPartnerId(partnerId);
    }
    public static List<String> getAllOrders() {
        return  OrderRepository.getAllOrders();
    }

    public static Integer getCountOfUnassignedOrders() {
        return OrderRepository.getCountOfUnassignedOrders();
    }


    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String Time[]=time.split(":");
        int newTime=Integer.parseInt(Time[0])*60+Integer.parseInt(Time[1]);
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(newTime,partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time=orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
        String HH=Integer.toString(time/60);
        String MM=Integer.toString(time%60);
        if(HH.length()<2)
            HH='0'+HH;
        if(MM.length()<2)
            MM='0'+MM;
        return HH+":"+MM;
    }

    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }
}
