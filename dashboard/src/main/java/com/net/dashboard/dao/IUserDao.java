package com.net.dashboard.dao;

import com.net.dashboard.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/
@Mapper
public interface IUserDao {

    @Select("select id,dc_id as dcId,is_in_dc as isInDc,create_date as createDate,netnut_customer_id as netnutCustomerId ,cheap_user_name as cheapUserName,smart_user_name as smartUserName,smart_user_password as smartUserPassword   from user where dc_id=#{dcId}")
    User selectUserByDcId(User user);

    @Insert("insert into user(id,dc_id,is_in_dc,create_date)value(#{id},#{dcId},#{isInDc},#{createDate})")
    int insertUser(User user);
    @Update("update user set is_in_dc=#{isInDc} where dc_id=#{dcId}")
    int updateUserIsInDc(User user);
    @Update("update user set netnut_customer_id =#{netnutCustomerId},cheap_user_name=#{cheapUserName},smart_user_name=#{smartUserName},smart_user_password=#{smartUserPassword} where dc_id =#{dcId}")
    int updateUserProxy(User user);
}
