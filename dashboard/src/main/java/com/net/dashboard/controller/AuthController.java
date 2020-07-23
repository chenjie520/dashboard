package com.net.dashboard.controller;

import com.alibaba.fastjson.JSONObject;
import com.net.dashboard.config.ContentKey;
import com.net.dashboard.util.HttpClientUtil;
import com.net.dashboard.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.user.User;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 * discord 授权登录
 **/
@Controller
@RequestMapping("auth")
public class AuthController {
    @GetMapping("/login")
    public String login(){
        return "";
    }

    @RequestMapping("/auth")
    public String loginDisCord(){
        try {
            OAuthClientRequest oauthResponse=OAuthClientRequest.authorizationLocation(ContentKey.OAUTH_CLIENT_AUTHORIZE)
                    .setResponseType(OAuth.OAUTH_CODE)
                    .setClientId(ContentKey.CLIENT_ID)
                    .setRedirectURI(ContentKey.OAUTH_CLIENT_CALLBACK)
                    .setScope(ContentKey.SCOPE)
                    .buildQueryMessage();
            return "redirect:"+oauthResponse.getLocationUri();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/callback")
    public void getToken(HttpServletRequest request, Model model){
        try {
            OAuthAuthzResponse oAuthAuthzResponse=OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            Map<String,String> map=new HashMap<>();
            map.put("code",oAuthAuthzResponse.getCode());
            map.put("grant_type", ContentKey.GRANT_TYPE);
            map.put("client_secret",ContentKey.CLIENT_SECRET);
            map.put("redirect_uri",ContentKey.OAUTH_CLIENT_CALLBACK);
            map.put("client_id",ContentKey.CLIENT_ID);
            map.put("scope",ContentKey.SCOPE);
            String json= HttpClientUtil.doPost(ContentKey.OAUTH_CLIENT_TOKEN,map);
            JSONObject  object= JSONObject.parseObject(json);
            Map<String, Object> valueMap = new HashMap<String, Object>();
            valueMap.putAll(object);
            String token=valueMap.get("access_token").toString();
            String data=HttpClientUtil.doGet(ContentKey.GET_USER_INFORMATION,map,token);
            JSONObject user=JSONObject.parseObject(data);
            Map<String,Object> userMap=new HashMap<>();
            userMap.putAll(user);
            //user dcid
            String dcId=userMap.get("id").toString();
            String dcName=userMap.get("username").toString()+"#"+userMap.get("discriminator").toString();
            System.out.println(data);
            if(judgeUserStatus(dcId)){
                System.out.println("当前用户在dcserver中");
            }else{
                System.out.println("当前用户不在dcserver中");
            }
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过dcId判断用户的状态
     **/
    public boolean judgeUserStatus(String dcId)throws Exception{
        String token= ContentKey.TOKEN;
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        CompletableFuture<User> userById = api.getUserById(dcId);
        User user = userById.get();
        return "ONLINE".equals(user.getStatus().toString().toUpperCase());
    }
}
