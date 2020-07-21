package com.net.dashboard.util;

import java.util.UUID;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/
public class StringUtil {

    /**
    *   判断字符串是否为空，如果为空返回true,不为空返回false
    */
    public boolean isNull(String str){
        return str == null || "".equals(str);
    }

    /**
    * 获取32位的uuid
    */
    public String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }


}
