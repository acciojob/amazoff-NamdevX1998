package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    static Map<String,Order> orderMapping=new HashMap<>();
    static Map<String,DeliveryPartner>partnerMap=new HashMap<>();
    static Map<DeliveryPartner,List<Order>>orderDeliveryPartnerMap=new HashMap<>();

    public void addOrder(Order order) {
        String ID=order.getId();
        orderMapping.put(ID,order);
    }

    public void partnerId(String partnerId) {
        DeliveryPartner dp=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,dp);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMapping.containsKey(orderId)==false)
            return;
        if(partnerMap.containsKey(partnerId)==false)
            return;
        DeliveryPartner dp=partnerMap.get(partnerId);
        Order order=orderMapping.get(orderId);
        if(orderDeliveryPartnerMap.containsKey(dp)==false){
            List<Order>list=new ArrayList<>();
            list.add(order);
            orderDeliveryPartnerMap.put(dp,list);
        }
        else{
            List<Order>list=orderDeliveryPartnerMap.get(dp);
            list.add(order);
            orderDeliveryPartnerMap.put(dp,list);
        }
    }

    public Order getOrderById(String orderId) {
        if(orderMapping.containsKey(orderId))
            return orderMapping.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId))
            return partnerMap.get(partnerId);
        return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if(partnerMap.containsKey(partnerId)==false)
            return 0;
        DeliveryPartner dp=partnerMap.get(partnerId);
        if(orderDeliveryPartnerMap.containsKey(dp)==false)
            return 0;
        List<Order>list=orderDeliveryPartnerMap.get(dp);
        return list.size();
    }
    public static List<String> getOrdersByPartnerId(String partnerId) {
        if(partnerMap.containsKey(partnerId)==false)
            return new ArrayList<>();
        DeliveryPartner dp=partnerMap.get(partnerId);
        if(orderDeliveryPartnerMap.containsKey(dp)==false)
            return new ArrayList<>();
        List<Order>list=orderDeliveryPartnerMap.get(dp);
        List<String>ans=new ArrayList<>();
        for(Order o:list){
            ans.add(o.getId());
        }
        return ans;
    }
    public static List<String> getAllOrders() {
        List<String>ans=new ArrayList<>();
        for(String s:orderMapping.keySet()){
            ans.add(s);
        }
        return ans;
    }

    public static Integer getCountOfUnassignedOrders() {
        int a=orderMapping.size();
        int count=0;
        for(DeliveryPartner dp:orderDeliveryPartnerMap.keySet()){
            int b=orderDeliveryPartnerMap.get(dp).size();
            count+=b;
        }
        return a-count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        if(orderDeliveryPartnerMap.containsKey(partnerId)==false){
            return 0;
        }
        List<Order>list=orderDeliveryPartnerMap.get(partnerId);
        int count=0;

        for(Order order:list){
            int ideal_time=order.getDeliveryTime();
            int actual_time=Integer.valueOf(time);
            if(actual_time>ideal_time)
                count++;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        if(orderDeliveryPartnerMap.containsKey(partnerId)==false)
            return "";
        List<Order>list=orderDeliveryPartnerMap.get(partnerId);
        int n=list.size();
        Order order=list.get(n-1);
        int ans=order.getDeliveryTime();
        return Integer.toString(ans);
    }

    public void deletePartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId)==false)
            return;
        if(orderDeliveryPartnerMap.containsKey(partnerId)==false)
            return;
        partnerMap.remove(partnerId);
        orderDeliveryPartnerMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        if(orderMapping.containsKey(orderId)==false)
            return;
        Order order=orderMapping.get(orderId);
        for(DeliveryPartner dp:orderDeliveryPartnerMap.keySet()){
            List<Order>list=orderDeliveryPartnerMap.get(dp);
            for(Order o:list){
                if(o.getId().equalsIgnoreCase(orderId)){
                    list.remove(o);
                }
            }
        }
    }
}
