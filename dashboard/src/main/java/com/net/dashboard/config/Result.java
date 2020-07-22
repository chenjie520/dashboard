package com.net.dashboard.config;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/
@Data
public class Result implements Serializable {
    private static final long serialVersionUID = 2267751680865696851L;

    private Boolean success;

    private String message;

    private Object data;

    private Result(Boolean success,Object data,String message){
        this.success=success;
        this.data=data;
        this.message=message;
    }
    public static String success(Object data,String message){
        Result result= new Result(true,data,message);
        return JSONArray.toJSON(result).toString();
    }
    public static String error(Object data,String message){
        Result result = new Result(false, data, message);
        return JSONArray.toJSON(result).toString();
    }
}
