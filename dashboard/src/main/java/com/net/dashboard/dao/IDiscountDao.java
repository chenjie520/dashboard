package com.net.dashboard.dao;

import com.net.dashboard.pojo.Discount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/29
 **/
@Mapper
public interface IDiscountDao {

    @Select("select id,number,type,due_date as dueDate,dc_id as dcId,create_date as createDate ,use_date as useDate from discount where number=#{number}")
    List<Discount> selectDiscountByNumber(Discount discount);

    @Insert("insert into discount(id,number,type,due_date,dc_id,create_date,use_date)value(#{id},#{number},#{type},#{dueDate},#{dcId},#{createDate},#{useDate})")
    int insertDiscount(Discount discount);
}
