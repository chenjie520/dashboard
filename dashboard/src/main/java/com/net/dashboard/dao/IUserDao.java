package com.net.dashboard.dao;

import com.net.dashboard.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/
@Mapper
public interface IUserDao {

    @Select("select id from user")
    List<User> getIds();
}
