package com.net.dashboard.pojo;

import lombok.Data;

import java.util.Date;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/
@Data
public class User {
    private int id;
    private String dcId;
    private String isInDc;      //是否在dc服务器中：1.在，2.不在
    private Date createDate;
    private String netnutCustomerId;
    private String cheapUserName;
    private String smartUserName;
    private String smartUserPassword;
}
