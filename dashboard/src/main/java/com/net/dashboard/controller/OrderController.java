package com.net.dashboard.controller;

import com.net.dashboard.dao.IOrderDao;
import com.net.dashboard.pojo.Order;
import com.net.dashboard.pojo.Response;
import org.apache.ibatis.annotations.Param;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private IOrderDao orderDao;
    @RequestMapping("getOrders")
    public @ResponseBody Object getOrders(@Param("dcId")String dcId){
        if(dcId==null||"".equals(dcId)){
            return new Response(false,"dcId is not found");
        }
        return new Response(true,orderDao.selectAllOrder(new Order(){
            @Override
            public String getDcId(){
                return dcId;
            }
        }).toString());
    }
    @RequestMapping("getOrder")
    public @ResponseBody Object getOrder(@Param("dcId")String dcId){
        if(dcId==null||"".equals(dcId)){
            return new Response(false,"dcId is not found");
        }
        List<Order>  list=orderDao.selectAllOrder(new Order(){
            @Override
            public String getDcId(){
                return dcId;
            }
        });
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        List<HashMap<String,Object>> list1=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            HashMap<String,Object> map=new HashMap<>();
            map.put("id",list.get(i).getDcId());
            map.put("dcId",list.get(i).getDcId());
            map.put("buyDate",sdf.format(list.get(i).getBuyDate()));
            map.put("dueDate",sdf.format(list.get(i).getDueDate()));
            map.put("discount",list.get(i).getDiscount());
            map.put("buyPrice",list.get(i).getBuyPrice());
            map.put("buyType",list.get(i).getBuyType());
            list1.add(map);
        }
        return new Response(true,new JSONArray(list1).toString());
    }
}
