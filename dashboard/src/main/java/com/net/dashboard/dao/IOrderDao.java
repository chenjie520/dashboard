package com.net.dashboard.dao;

import com.net.dashboard.pojo.Order;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IOrderDao {
    @Insert("insert into `order`(dc_id,buy_date,due_date,buy_price,discount,buy_type) value(#{dcId},#{buyDate},#{dueDate},#{buyPrice},#{discount},#{buyType})")
    int insertOrder(Order order);
    @Select("select id, dc_id as dcId,buy_date as buyDate,due_date as dueDate,buy_price as buyPrice,discount,buy_type as buyType from `order` where dc_id=#{dcId} and due_date >=sysdate()")
    List<Order> selectOrderByDcId(Order order);
    @Select("select id, dc_id as dcId,buy_date as buyDate,due_date as dueDate,buy_price as buyPrice,discount,buy_type as buyType from `order` where dc_id=#{dcId}")
    List<Order> selectAllOrder(Order order);

}
