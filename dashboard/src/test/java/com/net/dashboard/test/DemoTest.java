package com.net.dashboard.test;

import com.net.dashboard.config.ContentKey;
import com.net.dashboard.dao.IUserDao;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 * 单元测试
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan("com.net.dashboard.dao")
public class DemoTest {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private Environment environment;
    @Test
    public void test1(){
        userDao.getIds().stream().forEach(n->{
            System.out.println(n.getId());
        });
    }

    @Test
    public void test2(){
        System.out.println(environment.getProperty("从application.properties取参数"));
    }

    @Test
    public void test3()throws Exception{
        String token= "NzA5NzUwMDg1NzI2MjQwODQ5.Xxly1g.hNpds706kusPr6gOwTdc8LHk9Ec";
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        CompletableFuture<User> userById = api.getUserById("579329810157928459");
        User user = userById.get();
        System.out.println(user.getStatus());
    }
}
