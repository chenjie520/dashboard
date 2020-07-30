package com.net.dashboard.config;


import org.springframework.stereotype.Component;

/**
 *@Author chen_jie
 *@Version 1.0
 *@time 2020/7/21
 **/

public class ContentKey {
    public  static  String CLIENT_ID="709750085726240849";
    public static String TOKEN="NzA5NzUwMDg1NzI2MjQwODQ5.XrqcRg.FC3ioAnggGmi_mHyzhOEHu58gK8";
    public static final String CLIENT_SECRET="zZD6d_EeP4W2U8OA8M9wKqclZS9dh4uL";
    public static final String SCOPE="identify guilds.join";

    public static final String GRANT_TYPE="authorization_code";

    public static final String OAUTH_CLIENT_CALLBACK="https://thinkchen.top/auth/callback";
    public static final String OAUTH_CLIENT_AUTHORIZE="https://discord.com/oauth2/authorize";
    public static final String OAUTH_CLIENT_TOKEN="https://discord.com/api/oauth2/token";

    public static final String GET_USER_INFORMATION="https://discord.com/api/v6/users/@me";


    /**
     * netnut
     * https://l.netnut.io/reseller-api
     * https://drive.google.com/file/d/1leFblBH5bWv2dk74r4TVczXc1Y2njuNZ/view
     */
    public static final String NETNUT_LOGIN_EMAIL="ascdda4@gmail.com";
    public static final String NETNUT_LOGIN_PASSWORD="bMXbMqlR";
    public static final String NETNUT_GET_MY_USEAGE="https://reseller-api.netnut.io/api/aff/usage";
    public static final String NETNUT_GET_ALL_CUSTOMERS="https://reports.netnut.io/api/aff/customers";
    public static final String NETNUT_ADD_CUSTOMER="https://reseller-api.netnut.io/api/aff/customers";
    public static final String NETNUT_ADD_FLOW_RATE="https://reseller-api.netnut.io/api/aff/customer/customer_id/allocate";
    public static final String NETNUT_PACKAGES="https://reports.netnut.io/api/aff/servers/dc/get";
    public static final String NETNUT_SERVER="https://reports.netnut.io/api/aff/servers/dc/getusers";

    /**
     * cheap
     * https://documenter.getpostman.com/view/5861128/SWE6ZxQN?version=latest#cabdcf6d-9f6a-4660-8972-73d7e30520d9
     */
    public static final String CHEAP_TOKEN="b52fd416-cf1c-49e0-9045-60cbc1ca8c96";
    public static final String CHEAP_GETUSERINFON="https://reseller.proxy-cheap.com/reseller/my_info";
    public static final String CHEAP_CREATEUSER="https://reseller.proxy-cheap.com/reseller/sub_users/create";
    public static final String CHEAP_ADDBALANCE="https://reseller.proxy-cheap.com/reseller/sub_users/give_balance";
    public static final String CHEAP_SHOWBALANCE="https://reseller.proxy-cheap.com/reseller/sub_users/view_single";

    /**
     *  smart
     *  https://help.smartproxy.com/reference#get-sub-users
     *  https://dashboard.smartproxy.com/subusers
     */
    public static final String SMART_USERNAME="ascdda3@gmail.com";
    public static final String SMART_PASSWORD="546763132Zwq";
    public static final String SMART_GETAUTH="https://api.smartproxy.com/v1/auth";
}
