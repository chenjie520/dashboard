package com.net.dashboard.pojo;

import lombok.Data;

@Data
public class Smart {
    private String id;
    private String username;
    private String status;
    private String created_at;
    private double traffic; //已用流量
    private String traffic_limit;//分配流量
    private String service_type;
    private String traffic_count_from;
    private boolean auto_disable;
}
