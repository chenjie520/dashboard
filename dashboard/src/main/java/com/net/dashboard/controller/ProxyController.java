package com.net.dashboard.controller;

import com.net.dashboard.pojo.Response;
import com.net.dashboard.service.ProxyService;
import org.apache.ibatis.annotations.Param;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("proxy")
public class ProxyController {
    @Autowired
    private ProxyService proxyService;

    /**
     *
     * @param dcId
     * @param code
     * @param type
     * @param count
     * @return
     */
    @RequestMapping("/getProxies")
    public @ResponseBody Object getProxies(@Param("dcId") String dcId, @Param("code") String code, @Param("type") String type, @Param("count") String count){
        String result="";
        try{
            if(dcId==null||("".equals(dcId))){
                result= "dcId is not found";
            }
            if(code==null||("".equals(code))){
                result= "code is not found";
            }
            if(type==null||("".equals(type))){
                result ="type is not found";
            }
            if(count==null||("".equals(count))){
                result= "count is not found";
            }
            if(!"".equals(result)){
                return new Response(false,result);
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
        }catch (Exception e){
            return new Response(false,"proxy not found");
        }

    }
    @RequestMapping("/getRemaining")
    public @ResponseBody Object getRemaining(@RequestParam("dcId")String dcId){
        try{
            if(dcId==null||("".equals(dcId))){
                return new Response(false,"dcId is not found");
            }
            return new Response(true,proxyService.showRemaining(dcId)+"");
        }catch (Exception e){
            return new Response(false,"have a error");
        }
    }
}
