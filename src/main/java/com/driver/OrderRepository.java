package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    static Map<String,Order> orderMap =new HashMap<>();
    static Map<String,DeliveryPartner>partnerMap=new HashMap<>();
    static Map<DeliveryPartner,List<Order>> Partner_OrdersMap =new HashMap<>();
    static Map<Order,DeliveryPartner> order_PartnerMap =new HashMap<>();

    public void addOrder(Order order) {
        String ID=order.getId();
        orderMap.put(ID,order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner dp=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,dp);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            DeliveryPartner dp=partnerMap.get(partnerId);
            Order order= orderMap.get(orderId);
            if(Partner_OrdersMap.containsKey(dp)==false){
                List<Order>list=new ArrayList<>();
                list.add(order);
                Partner_OrdersMap.put(dp,list);
            }
            else{
                List<Order>list= Partner_OrdersMap.get(dp);
                list.add(order);
                Partner_OrdersMap.put(dp,list);
            }
            //now increase the count of orders of respective delivery partner.
            dp.setNumberOfOrders(Partner_OrdersMap.size());  // or   dp.setNumberOfOrders(dp.getNumberOfOrders()+1);
            order_PartnerMap.put(order,dp);
        }
    }

    public Order getOrderById(String orderId) {
        if(orderMap.containsKey(orderId))
            return orderMap.get(orderId);
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
        if(Partner_OrdersMap.containsKey(dp)==false)
            return 0;
        List<Order>list= Partner_OrdersMap.get(dp);
        return list.size();
    }
    public static List<String> getOrdersByPartnerId(String partnerId) {
        if(partnerMap.containsKey(partnerId)==false)
            return new ArrayList<>();
        DeliveryPartner dp=partnerMap.get(partnerId);
        if(Partner_OrdersMap.containsKey(dp)==false)
            return new ArrayList<>();
        List<Order>list= Partner_OrdersMap.get(dp);
        List<String>ans=new ArrayList<>();
        for(Order o:list){
            ans.add(o.getId());
        }
        return ans;
    }
    public static List<String> getAllOrders() {
        List<String>ans=new ArrayList<>();
        for(String s: orderMap.keySet()){
            ans.add(s);
        }
        return ans;
    }

    public static Integer getCountOfUnassignedOrders() {
        int a= orderMap.size();
        int b=order_PartnerMap.size();
        return a-b;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int ideal_time, String partnerId) {
        if(Partner_OrdersMap.containsKey(partnerId)==false){
            return 0;
        }
        List<Order>list= Partner_OrdersMap.get(partnerId);
        int count=0;

        for(Order order:list){
            int actual_time=order.getDeliveryTime();
            if(actual_time>ideal_time)
                count++;
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        if(Partner_OrdersMap.containsKey(partnerId)==false)
            return 0;
        DeliveryPartner dp=new DeliveryPartner(partnerId);
        if(Partner_OrdersMap.containsKey(dp)==false)
            return 0;
        List<Order>list= Partner_OrdersMap.get(dp);
        int maxi=0;
        for(Order o:list){
            maxi=Math.max(maxi,o.getDeliveryTime());
        }
        return maxi;
    }

    public void deletePartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId)==false)
            return;
        DeliveryPartner dp=partnerMap.get(partnerId);
        partnerMap.remove(partnerId);

        if(Partner_OrdersMap.containsKey(dp)==false)
            return;

        //delete entry of partner from Partner_OrdersMap
        List<Order>delete=new ArrayList<>();
        for(Order order:Partner_OrdersMap.get(dp)){
            delete.add(order);
        }
        Partner_OrdersMap.remove(dp);

        //delete orders from order_PartnerMap
        for(Order o:delete){
           if(order_PartnerMap.containsKey((o))){
               order_PartnerMap.remove(o);
           }
        }
    }

    public void deleteOrderById(String orderId) {
        if(orderMap.containsKey(orderId)==false)
            return;
        Order order= orderMap.get(orderId);
        if(order_PartnerMap.containsKey(order)==false)
            return;
        DeliveryPartner dp=order_PartnerMap.get(order);

        //remove from order-partner map
        order_PartnerMap.remove(order);

        //remove from order map
        orderMap.remove(order);

        //remove order from partner-orders map
        Partner_OrdersMap.get(dp).remove(order);   //imp
    }
}
