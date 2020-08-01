package com.net.dashboard.controller;

import com.net.dashboard.dao.IOrderDao;
import com.net.dashboard.pojo.Order;
import com.net.dashboard.pojo.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private IOrderDao orderDao;
    @RequestMapping("getOrders")
    public @ResponseBody Object getOrder(@RequestParam("dcId")String dcId){
        if(dcId==null||"".equals(dcId)){
            return new Response(false,"dcId is not found");
        }
        return new Response(true,orderDao.selectOrderByDcId(new Order(){
            @Override
            public String getDcId(){
                return dcId;
            }
        }).toString());
    }
}
