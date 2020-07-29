package com.net.dashboard.pojo;

import lombok.Data;

import java.util.Date;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/29
 **/
@Data
public class Discount {
    private int id;
    private String number;
    private String type;
    //到期时间
    private Date dueDate;
    private String dcId;
    private Date createDate;
    private Date useDate;
}
