package com.net.dashboard.util;

import com.alibaba.fastjson.JSONObject;
import com.net.dashboard.config.ContentKey;
import com.net.dashboard.pojo.Cheap;

import java.util.*;

public class CheapUtil {

    public void getUserInfo(){
        String data=HttpClientUtil.doGet(ContentKey.CHEAP_GETUSERINFON,null,ContentKey.CHEAP_TOKEN);
        System.out.println(data);
    }

    /**
     * 创建子用户并将username返回
     * @return
     */
    public String createUser(){
        Map<String,String> params=new HashMap<>();
        String userName=getRandomString(6);
        params.put("username",userName);
        HttpClientUtil util=new HttpClientUtil();
        String data=util.doPost(ContentKey.CHEAP_CREATEUSER,params,ContentKey.CHEAP_TOKEN);
        String status=JSONObject.parseObject(data).get("status").toString();
        if(status.equals("200")){
            return userName;
        }else if(JSONObject.parseObject(data).get("message").toString().equals("that username is already registered")){
            return createUser();
        }
        return null;
    }

    /**
     * 通过username添加cheap子用户的余额，单位是美分
     * @param balance
     * @param userName
     * @return
     */
    public Boolean addBalance(int balance,String userName){
        Map<String,String> params=new HashMap<>();
        params.put("username",userName);
        params.put("amount_usd_cents",balance+"");
        HttpClientUtil util=new HttpClientUtil();
        String data=util.doPost(ContentKey.CHEAP_ADDBALANCE,params,ContentKey.CHEAP_TOKEN);
        String status=JSONObject.parseObject(data).get("status").toString();
        if(status.equals("200")){
            return true;
        }else {
            return  false;
        }
    }

    /**
     * 通过用户名查看这个人的信息
     * @param username
     * @return
     */
    public Cheap getCheapInfo(String username){
        Map<String,String> params=new HashMap<>();
        params.put("username",username);
        HttpClientUtil util=new HttpClientUtil();
        String data=util.doPost(ContentKey.CHEAP_SHOWBALANCE,params,ContentKey.CHEAP_TOKEN);
        String status=JSONObject.parseObject(data).get("status").toString();
        if("200".equals(status)){
            Cheap cheap = JSONObject.parseObject(JSONObject.parseObject(data).get("data").toString(), Cheap.class);
            return cheap;
        }
        return null;
    }

    /**
     * 生成静态代理代理 code:国家缩写,动态代理只有国家
     * us:UnitedStates
     * uk:UnitedKingdom
     * ca:Canada
     * aus:Australia
     * sg:Singapore
     * jp:Japan
     * chn:China
     * ger:Germany
     * sk:SouthKorea
     * @Param number 数量
     * @param username 用户名
     * @param code 区域编号
     * @return
     */
    public List<String> gernateStaticCheapProxies(int number,String username,String code){
        CheapUtil util=new CheapUtil();
        Cheap cheap=util.getCheapInfo(username);
        List<String> proxies=null;
        if(cheap!=null){
            proxies=new ArrayList<>();
            for(int i=0;i<number;i++){
                proxies.add("proxy.21proxies.com:31112:"+username+":"+cheap.getProxy_authkey()+"_country-"+chooseCountry(code)+"-session-"+getRandomString(6));
            }
        }
        return proxies;
    }

    /**
     * 生成动态代理
     * @param number
     * @param username
     * @param code
     * @return
     */
    public List<String> gernateDynamicCheapProxies(int number,String username,String code){
        CheapUtil util=new CheapUtil();
        Cheap cheap=util.getCheapInfo(username);
        List<String> proxies=null;
        if(cheap!=null){
            proxies=new ArrayList<>();
            for(int i=0;i<number;i++){
                proxies.add("proxy.21proxies.com:31112:"+username+":"+cheap.getProxy_authkey()+"_country-"+chooseCountry(code));
            }
        }
        return proxies;
    }

    public static void main(String[] args) {
        //new CheapUtil().getUserInfo();
        //创建子用户
        // String username=new CheapUtil().createUser();
        //添加到子用户余额
        //System.out.println(new CheapUtil().addBalance(10,username));
        //查看子用户剩余
        //静态代理
        /*new CheapUtil().gernateDynamicCheapProxies(10,"21proxiesxzdddd","us").stream().forEach(n->{
            System.out.println(n);
        });*/
        //生成代理
        System.out.println(1*1.00/300);
    }
    private static String chooseCountry(String code){
        switch (code){
            case "us":return "UnitedStates";
            case "uk":return "UnitedKingdom";
            case "ca":return "Canada";
            case "aus":return "Australia";
            case "sg":return "Singapore";
            case "jp":return "Japan";
            case "chn":return "China";
            case "ger":return "Germany";
            case "sk":return "SouthKorea";
        }
        return "UnitedStates";
    }

    /**
     * 随机生成固定长度的字符串
     * @param length
     * @return
     */
    private static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        sb.append("21proxies");
        for(int i=0;i<length;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
