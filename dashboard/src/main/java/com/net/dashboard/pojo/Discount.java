package com.net.dashboard.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/29
 **/
@Data
public class Discount implements Serializable {
    private int id;
    private String number;
    private String type;
    //到期时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate;
    private String dcId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date useDate;
}
