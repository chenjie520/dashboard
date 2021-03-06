package com.net.dashboard.controller;

import com.net.dashboard.dao.IDiscountDao;
import com.net.dashboard.pojo.Discount;
import com.net.dashboard.pojo.Response;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/29
 **/
@Controller
@RequestMapping("discount")
public class DiscountController {
    @Autowired
    private IDiscountDao discountDao;

    //number:数量，type:折扣(是小于1的小数，比如0.9，date:有效日期(2020-02-01)
    @RequestMapping("/add")
    public @ResponseBody
    Response  add(HttpServletResponse response, @RequestParam("number")String number, @RequestParam("type")String type, @RequestParam("date")String date){
        if(number==null||number.equals("")||number.equals("0")||(!isNumber(number))){
            return new Response(true, "error for number");
        }
        if(type==null||type.equals("0")||type.equals("")||type.equals("1")){
            return new Response(true, "error for type");
        }
        if(date==null||"".equals(date)){
            return new Response(true, "error for date");
        }
        List<String> list=new ArrayList<>();
        for(int i=0;i<new Integer(number);i++){
            Discount discount=new Discount();
            discount.setNumber(generateDiscount());
            discount.setType(type);
            discount.setCreateDate(new Date());
            try {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                discount.setDueDate(sdf.parse(date));
                discountDao.insertDiscount(discount);
                list.add(discount.getNumber());
            } catch (ParseException e) {
                return new Response(true, "error for date");
            }
        }
        return new Response(true, list.toString());
    }

    private static boolean isNumber(String str){
        for(int i=str.length();--i>=0;){
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
    private static String generateDiscount(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<8;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @RequestMapping("/judgeDiscount")
    public void judgeDiscount(HttpServletResponse response,@RequestParam("discount")String discount){
        Discount discount1=new Discount();
        discount1.setNumber(discount);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        discount1=discountDao.selectDiscountByNumber(discount1).get(0);
        Map<String,Object> hashMap=new HashMap<>();
        if(discount1!=null&&discount1.getId()!=0){
            hashMap.put("id",discount1.getId());
            hashMap.put("number",discount1.getNumber());
            hashMap.put("type",discount1.getType());
            hashMap.put("dueDate",sdf.format(discount1.getDueDate()));
            hashMap.put("dcId",discount1.getDcId());
            hashMap.put("createDate",sdf.format(discount1.getCreateDate()));
            if(discount1.getUseDate()!=null){
                hashMap.put("useDate",sdf.format(discount1.getUseDate()));
            }else{
                hashMap.put("useDate","");
            }
            com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
            jsonObject.put("result",hashMap);
            try {
                response.getWriter().write(jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
