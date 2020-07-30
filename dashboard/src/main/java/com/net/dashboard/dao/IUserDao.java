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

    @Select("select id,dc_id as dcId,is_in_dc as isInDc from user where dc_id=#{dcId}")
    User selectUserByDcId(User user);

    @Insert("insert into user(id,dc_id,is_in_dc,create_date)value(#{id},#{dcId},#{isInDc},#{createDate})")
    int insertUser(User user);
    @Update("update user set is_in_dc=#{isInDc} where dc_id=#{dcId}")
    int updateUserIsInDc(User user);
}
