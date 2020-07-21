package com.net.dashboard.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 * 单元测试
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class DemoTest {
    @Test
    public void test1(){
        System.out.println(1);
    }
}
