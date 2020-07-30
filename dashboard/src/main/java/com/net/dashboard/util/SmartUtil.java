package com.net.dashboard.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.net.dashboard.config.ContentKey;
import com.net.dashboard.pojo.Smart;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SmartUtil {
    public static void main(String[] args) throws IOException {
           /* Map<String,String>map=getUserAuthId();
            if(map!=null){
                System.out.println(map.get("user_id"));
            }*/
        SmartUtil util=new SmartUtil();
        //1.获取主用户信息的授权
        Map<String,String> map=util.getUserAuthId();
        if(map!=null){
            //2.创建子用户
            //Map<String,String>map1=util.createUser(map.get("user_id"),map.get("token"));
            //System.out.println(map1.get("sub_username"));
            //System.out.println(map1.get("sub_password"));
            //3.给子用户添加流量
            //util.updateUserStaff(n.getId(),map.get("user_id"),map.get("token"),1);
            //4.查看子用户的剩余流量
            util.getAllSubUser(map.get("user_id"),map.get("token")).stream().forEach(n->{
                System.out.println(n);
            });
            util.dd(map.get("token"));
            //5.生成代理,代理的域名直接从数据库中拿

        }


    }

    public List<String> dd(String token) throws IOException{
        // Token retrieved from Authentication part.
        // Available types: random & rotating
        String type = "random";
        URL url = new URL("https://api.smartproxy.com/v1/endpoints/"+type);

        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;

        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpConn.setRequestProperty ("Authorization", "Token "+token);
        httpConn.setRequestMethod("GET");

        if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            System.out.print(br.lines().collect(Collectors.joining()));
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
            System.out.print(br.lines().collect(Collectors.joining()));
        }

        httpConn.disconnect();
        return null;
    }
    /**
     * 通过主用户授权给子用户添加流量
     * @param sub_user_id
     * @param user_id
     * @param token
     * @param count 修改的流量，并不是添加
     * @return
     */
    public boolean updateUserStaff(String sub_user_id,String user_id,String token,int count){
        try{

            // Details of the subuser you wish to change
            String payload = "{\"status\":\"active\",\"traffic_limit\":"+count+"}";

            URL url = new URL("https://api.smartproxy.com/v1/users/"+user_id+"/sub-users/"+sub_user_id);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;


            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConn.setRequestProperty ("Authorization", "Token "+token);
            httpConn.setRequestMethod("PUT");
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestProperty("Accept", "application/json");

            httpConn.setDoOutput(true);
            OutputStream outStream = httpConn.getOutputStream();
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
            outStreamWriter.write(payload);
            outStreamWriter.flush();
            outStreamWriter.close();
            outStream.close();

            if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
            }

            httpConn.disconnect();
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 获取所有子用户信息
     * @param userId
     * @param token
     * @return
     */
    public List<Smart> getAllSubUser(String userId , String token){

        try {
            URL url = new URL("https://api.smartproxy.com/v1/users/" + userId + "/sub-users");

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;

            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConn.setRequestProperty("Authorization", "Token " + token);
            httpConn.setRequestMethod("GET");

            if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String data = br.lines().collect(Collectors.joining());
                //由于创建完用户之后无法获取用户id，想要获取用户剩余流量信息只能先获取所有子用户信息
                List<Smart> list = JSONArray.parseArray(data, Smart.class);
                return list;
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
            }

            httpConn.disconnect();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 由于已经将所有用户都取出来了就不用这个接口
     * 通过主用户的授权以及子用户的账号id查看子用户的剩余流量
     * @param sub_user_id 子用户的账号id
     * @param user_id
     * @param token
     * @return
     */
    public String getRemainingTraffic(String sub_user_id,String user_id,String token){
        try{
            String type = "24h"; // Date range, available types: 24h, month, 7days, custom. If custom type is selected you must provide String from and String to parameters.
            // to and from format yyyy-mm-dd

            URL url = new URL("https://api.smartproxy.com/v1/users/"+user_id+"/sub-users/"+sub_user_id+"/traffic?type="+type);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;

            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConn.setRequestProperty ("Authorization", "Token "+token);
            httpConn.setRequestMethod("GET");

            if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
            }

            httpConn.disconnect();

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过主账号的授权创建子用户，并将子用户的用户名和密码返回
     * @param user_id
     * @param token
     * @return
     */
    public Map<String,String> createUser(String user_id,String token){
        try{
            String sub_username=createUserName();
            String user_password=createUserPassWord();
            // Details for your new subuser
            String payload = "{\"username\":\""+sub_username+"\",\"password\":\""+user_password+"\",\"traffic_limit\":0, \"service_type\":\"residential_proxies\",\"auto_disable\":true}";

            // Possible service types: residential_proxies, shared_proxies

            URL url = new URL("https://api.smartproxy.com/v1/users/"+user_id+"/sub-users");

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;


            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConn.setRequestProperty ("Authorization", "Token "+token);
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestProperty("Accept", "application/json");

            httpConn.setDoOutput(true);
            OutputStream outStream = httpConn.getOutputStream();
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
            outStreamWriter.write(payload);
            outStreamWriter.flush();
            outStreamWriter.close();
            outStream.close();

            if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
                Map<String,String> map=new HashMap<>();
                map.put("sub_username",sub_username);
                map.put("sub_password",user_password);
                return map;
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
                System.out.print(br.lines().collect(Collectors.joining()));
            }

            httpConn.disconnect();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 通过username，password获取授权用户id
     * @return
     */
    public   Map<String,String> getUserAuthId(){
        try {
            String userCredentials = ContentKey.SMART_USERNAME + ":" + ContentKey.SMART_PASSWORD; // Your Smartproxy credentials
            String basicAuth = Base64.getEncoder().encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8));

            URL url = new URL(ContentKey.SMART_GETAUTH);

            URLConnection connection = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) connection;

            httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpConn.setRequestProperty("Authorization", "Basic " + basicAuth);
            httpConn.setRequestMethod("POST");

            if (200 <= httpConn.getResponseCode() && httpConn.getResponseCode() <= 299) {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                //System.out.print(br.lines().collect(Collectors.joining()));
                String data=br.lines().collect(Collectors.joining());
                Map<String,String>map=new HashMap<>();
                map.put("user_id", JSONObject.parseObject(data).get("user_id").toString());
                map.put("token", JSONObject.parseObject(data).get("token").toString());
                return map;
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
                //System.out.print(br.lines().collect(Collectors.joining()));
            }

            httpConn.disconnect();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 创建用户名
     * @return
     */
    private static String createUserName(){
        String str="abcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(36);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 创建密码,必须有一个大写一个数字
     * @return
     */
    private static String createUserPassWord(){
        String str="abcdefghijklmnopqrstuvwxyz";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<10;i++){
            int number=random.nextInt(26);
            sb.append(str.charAt(number));
        }
        String str1="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        sb.append(str1.charAt(random.nextInt(26)));
        String str2="0123456789";
        sb.append(str2.charAt(random.nextInt(10)));
        return sb.toString();
    }
}
