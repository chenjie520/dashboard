package com.net.dashboard.pojo;

import lombok.Data;

@Data
public class Netnut {
    private String customerId;  //id
    private String used;        //已用流量
    private String bandwidth;   //总流量
    private String loginName;   //代理的账号
    private String loginPwd;       //代理的密码

}
