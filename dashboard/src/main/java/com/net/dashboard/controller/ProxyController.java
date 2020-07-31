package com.net.dashboard.controller;

import com.net.dashboard.pojo.Response;
import com.net.dashboard.service.ProxyService;
import org.apache.ibatis.annotations.Param;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("proxy")
public class ProxyController {
    @Autowired
    private ProxyService proxyService;
    @RequestMapping("/getProxies")
    public @ResponseBody Object getProxies(@Param("dcId") String dcId, @Param("code") String code, @Param("type") String type, @Param("count") String count){
        if(dcId==null||("".equals(dcId))){
            return "dcId is not found";
        }
        if(code==null||("".equals(code))){
            return "code is not found";
        }
        if(type==null||("".equals(type))){
            return "type is not found";
        }
        if(count==null||("".equals(count))){
            return "count is not found";
        }
        List<String> list=null;
        if(type.equals("static")){
            list =proxyService.getStaticProxies(code,dcId,new Integer(count));
        }else{
            list=proxyService.getDynamicProxies(code,dcId,new Integer(count));
        }
        if(list==null){
            return new Response(false, "proxy not found");
        }
        return new Response(true,new JSONArray(list).toString());
    }
}
