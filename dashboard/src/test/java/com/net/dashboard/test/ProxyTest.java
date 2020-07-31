package com.net.dashboard.test;

import com.net.dashboard.service.ProxyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@MapperScan("com.net.dashboard.dao")
public class ProxyTest {
    @Autowired
    private ProxyService proxyService;
    @Test
    public void test1(){
        //1.给dc用户分配流量
        //proxyService.buyProxies("530733982942363650",3)
        //2.查看用户剩余流量
        //proxyService.showRemaining("530733982942363650")
        //3.生成混合的代理

        //System.out.println(proxyService.showRemaining("530733982942363650"));
    }
}
