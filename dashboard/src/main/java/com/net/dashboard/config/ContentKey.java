package com.net.dashboard.config;


import org.springframework.stereotype.Component;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/

public class ContentKey {
    public  static  String CLIENT_ID="709750085726240849";
    public static String TOKEN="NzA5NzUwMDg1NzI2MjQwODQ5.Xxly1g.hNpds706kusPr6gOwTdc8LHk9Ec";
    public static final String CLIENT_SECRET="zZD6d_EeP4W2U8OA8M9wKqclZS9dh4uL";
    public static final String SCOPE="identify guilds.join";

    public static final String GRANT_TYPE="authorization_code";

    public static final String OAUTH_CLIENT_CALLBACK="http://3.88.243.220:8081/dashboard/auth/callback";
    public static final String OAUTH_CLIENT_AUTHORIZE="https://discord.com/oauth2/authorize";
    public static final String OAUTH_CLIENT_TOKEN="https://discord.com/api/oauth2/token";

    public static final String GET_USER_INFORMATION="https://discord.com/api/v6/users/@me";


    /**
    * netnut
    */
    public static final String NETNUT_LOGIN_EMAIL="ascdda4@gmail.com";
    public static final String NETNUT_LOGIN_PASSWORD="bMXbMqlR";
    public static final String NETNUT_GET_MY_USEAGE="https://reseller-api.netnut.io/api/aff/usage";

}
