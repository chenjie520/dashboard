package com.net.dashboard.test;

import com.net.dashboard.dao.IDiscountDao;
import com.net.dashboard.pojo.Discount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/29
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan("com.net.dashboard.dao")
public class DiscountTest {
    @Autowired
    IDiscountDao discountDao;
    @Test
    public void test1(){
        List<String> list=new ArrayList<>();
        for(int i=0;i<5;i++){
            Discount discount=new Discount();
            discount.setNumber(generateDiscount());
            discount.setType("0.9");
            discount.setCreateDate(new Date());
            try {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                discount.setDueDate(sdf.parse("2020-01-11"));
                discountDao.insertDiscount(discount);
                list.add(discount.getNumber());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.println(list.toString());
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
}
