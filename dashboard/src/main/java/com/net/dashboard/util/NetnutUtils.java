package com.net.dashboard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.net.dashboard.config.ContentKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NetnutUtils {

    /**
     * 获取所有的子用户信息
     */
    public void getAllCustomers(){
        Map<String,String> params=new HashMap<>();
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        String data= HttpClientUtil.doGet(ContentKey.NETNUT_GET_ALL_CUSTOMERS,params,null);
        String result =JSONObject.parseObject(JSONObject.parseObject(data).get("result").toString()).get("customers").toString();
        List<Object> ts= (List<Object>)JSONArray.parseArray(result);
        ts.stream().forEach(n->{
            System.out.println(n);
        });
    }

    /**
     * 1.创建子用户
     * 2.分配流量
     * 3.查看剩余流量
     * 4.开始创建proxy
     */


    /**
     * 随机生成netnut账号并将customerid返回
     */
    public String addCustomer(){
        Map<String,String> params=new HashMap<>();
        params.put("customer_name",generaged("1"));
        params.put("customer_dashboard_email",generaged("2"));
        params.put("customer_dashboard_pwd",generaged("3"));
        params.put("customer_login_name",generaged("4"));
        params.put("customer_login_pwd",generaged("5"));
        params.put("customer_country_code","us");
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        String data= HttpClientUtil.doPost(ContentKey.NETNUT_ADD_CUSTOMER,params);
        String customer = JSONObject.parseObject(JSONObject.parseObject(data).get("result").toString()).get("customer").toString();
        JSONObject jsonObject = JSONObject.parseObject(customer);
        Map<String,Object> customerMap=new HashMap<>();
        customerMap.putAll(jsonObject);
        return customerMap.get("customer_id").toString();

    }

    /**
     * 通过customerid和allocation分配流量
     * @param customerId 子用户
     * @param allocation 流量（G）
     * @return
     */
    public Boolean ApportionFlowRate(int customerId,int allocation){
        Map<String,String> params=new HashMap<>();
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        params.put("customer_id",customerId+"");
        params.put("allocation",allocation+"");
        String url=ContentKey.NETNUT_ADD_FLOW_RATE.replaceAll("customer_id",customerId+"");
        String data=HttpClientUtil.doPost(url,params);
        String result=JSONObject.parseObject(JSONObject.parseObject(data).get("result").toString()).get("result").toString();
        if(result.equals("true")){
            return true;
        }
        return false;
    }

    public void showCustomerFlowRate(int customerId){
        Map<String,String> params=new HashMap<>();
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        params.put("customer_id",customerId+"");
        String data=HttpClientUtil.doGet(ContentKey.NETNUT_GET_ALL_CUSTOMERS,params,null);
        System.out.println(data);
    }

    /**
     * 1.customer_name
     * 2.customer_dashboard_email
     * 3.customer_dashboard_pwd
     * 4.customer_login_name
     * 5.customer_login_pwd
     * @param type
     * @return
     */
    private static String generaged(String type){
        switch (type){
            case "1":
            case "4":return getRandomString(10);
            case "2":return getRandomString(8)+"@gmail.com";
            case "3":
            case "5":return getRandomString(8);
        }
        return getRandomString(10);

    }
    private static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }





    public static void main(String[] args) {
        //System.out.println(HttpClientUtil.doGet("https://reseller-api.netnut.io?loginEmail=ascdda4@gmail.com&loginPassword=ascdda4@gmail.com"));
       new NetnutUtils().showCustomerFlowRate(165323);
       // new NetnutUtils().ApportionFlowRate(165323,2);
        //new NetnutUtils().getAllCustomers();
    }

}
