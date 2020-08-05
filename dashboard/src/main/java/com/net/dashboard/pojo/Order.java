package com.net.dashboard.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Order implements Serializable {
    private int id;
    private String dcId;
    //'购买时间'
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date buyDate;
    //'到期时间'
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dueDate;
    //'购买价格'
    private String buyPrice;
    //'折扣码'
    private String discount;
    //'购买流量(G)'
    private String buyType;

}
