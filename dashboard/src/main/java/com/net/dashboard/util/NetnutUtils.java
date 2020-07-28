package com.net.dashboard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.net.dashboard.config.ContentKey;
import com.net.dashboard.pojo.Netnut;

import java.net.InetAddress;
import java.util.*;

/**
 * 1.创建子用户
 * 2.分配流量
 * 3.查看剩余流量
 * 4.开始创建proxy
 */
public class NetnutUtils {
    private static final String port="33128";
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

    public void getPackage(){
        Map<String,String> params=new HashMap<>();
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        String data=HttpClientUtil.doGet(ContentKey.NETNUT_PACKAGES,params,null);
        System.out.println(data);
    }

    /**
     * 通过id找到这个用户的所有netnut信息
     * @param customerId
     */
    public List<Netnut>  showCustomerFlowRate(int customerId){
        Map<String,String> params=new HashMap<>();
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        params.put("customer_id",customerId+"");
        String data=HttpClientUtil.doGet(ContentKey.NETNUT_GET_ALL_CUSTOMERS,params,null);
        String result =JSONObject.parseObject(JSONObject.parseObject(data).get("result").toString()).get("customers").toString();
        List<Netnut> list=new ArrayList<>();
        List<Object> ts= (List<Object>)JSONArray.parseArray(result);
        ts.stream().forEach(n->{
            JSONObject user=JSONObject.parseObject(n.toString());
            Map<String,Object> userMap=new HashMap<>();
            userMap.putAll(user);
            if(!user.get("name").toString().equals("Trial")){
                Netnut netnut=new Netnut();
                //用户已使用流量
                String used=user.get("used").toString();
                //用户总流量
                String staff=user.get("bandwidth").toString();
                //用户代理的账号
                String proxyName=user.get("customer_login_name").toString();
                //用户代理的密码
                String proxyPwd=user.get("customer_login_pwd").toString();
                netnut.setCustomerId(customerId+"");
                netnut.setUsed(used);
                netnut.setBandwidth(staff);
                netnut.setLoginName(proxyName);
                netnut.setLoginPwd(proxyPwd);
                list.add(netnut);
            }
        });
        if(list.size()!=0){
            return list;
        }
        return null;
    }
    public void showCustomerServer(int customerId){
        Map<String,String> params=new HashMap<>();
        params.put("loginEmail",ContentKey.NETNUT_LOGIN_EMAIL);
        params.put("loginPassword",ContentKey.NETNUT_LOGIN_PASSWORD);
        params.put("customer_id",customerId+"");
        //params.put("orderId")
    }




    /**
     * main
     * @param args
     */
    public static void main(String[] args)throws Exception {
        //System.out.println(HttpClientUtil.doGet("https://reseller-api.netnut.io?loginEmail=ascdda4@gmail.com&loginPassword=ascdda4@gmail.com"));
      /* new NetnutUtils().showCustomerFlowRate(165323).stream().forEach(n->{
           System.out.println(n.getUsed());
       });*/
       // new NetnutUtils().ApportionFlowRate(165323,2);
        //new NetnutUtils().getAllCustomers();
        //new NetnutUtils().getPackage();
        /*new NetnutUtils().findIPs("de").stream().forEach(n->{
            System.out.println(n);
        });*/
        //System.out.println(new NetnutUtils().addCustomer());
        //new NetnutUtils().showCustomerFlowRate(165487);
        //生成动态代理


        //生成静态代理
        new NetnutUtils().getDynamicProxy("165323","us").stream().forEach(n->{
            System.out.println(n);
        });
    }

    /**
     * 生成动态代理 通过用户id，地区code
     * @param customerId
     * @param code
     * @return
     */
    public List<String> getDynamicProxy(String customerId,String code){
        List<String> proxies=new ArrayList<>();
        NetnutUtils utils=new NetnutUtils();
        List<String> list=utils.findIPs(code);
        List<Netnut> netnuts=utils.showCustomerFlowRate(new Integer(customerId));
        if(netnuts!=null||netnuts.size()>0){
            for(int i=1;i<list.size()+1;i++){
                proxies.add("snkrs-us-s"+i+".netnut.io:"+port+":"+netnuts.get(0).getLoginName()+":"+netnuts.get(0).getLoginPwd());
            }
        }
        if(proxies!=null&&proxies.size()>0){
            return proxies;
        }
        return null;

    }

    /**
     * 通过用户id，国家code生成静态代理
     * @param customerId
     * @param code
     * @return
     */
    public List<String> getStaticProxy(String customerId,String code){
        List<String> proxies=new ArrayList<>();
        NetnutUtils utils=new NetnutUtils();
        List<String> list=utils.findIPs(code);
        List<Netnut> netnuts=utils.showCustomerFlowRate(new Integer(customerId));
        if(netnuts!=null||netnuts.size()>0){
            for(int i=0;i<list.size();i++){
                proxies.add(list.get(i)+":"+port+":"+netnuts.get(0).getLoginName()+":"+netnuts.get(0).getLoginPwd());
            }
        }
        if(proxies!=null&&proxies.size()>0){
            return proxies;
        }
        return null;
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

    /**
     * 通过地区code获取ip
     * @param code
     * @return
     * @throws Exception
     */
    public List<String> findIPs(String code){
        String name="snkrs-"+code+".netnut.io";
        try{
            InetAddress[] addresses = InetAddress.getAllByName(name);
            List<String> list=new ArrayList<>();
            for (int i = 0; i < addresses.length; i++) {
                list.add(addresses[i].getHostAddress());
            }
            return list;
        }catch (Exception e){
            return null;
        }

    }
}
