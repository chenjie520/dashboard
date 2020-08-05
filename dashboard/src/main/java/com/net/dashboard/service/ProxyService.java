package com.net.dashboard.service;

import com.net.dashboard.dao.IOrderDao;
import com.net.dashboard.dao.ISmartProxyServer;
import com.net.dashboard.dao.IUserDao;
import com.net.dashboard.pojo.*;
import com.net.dashboard.util.CheapUtil;
import com.net.dashboard.util.NetnutUtils;
import com.net.dashboard.util.SmartUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProxyService {
    private  static  Logger logger= LoggerFactory.getLogger(ProxyService.class);
    @Autowired
    private IUserDao userDao;
    @Autowired
    private ISmartProxyServer smartProxyServer;
    @Autowired
    private IOrderDao orderDao;

    /**
     * 生成动态代理
     * @param code 区域
     * @param dcId
     * @param count 数量
     * @return
     */
    public List<String> getDynamicProxies(String code,String dcId,int count){
        User user=userDao.selectUserByDcId(new User(){
            @Override
            public  String getDcId(){
                return dcId;
            }
        });
        if(user!=null){
            List<String> list=new ArrayList<>();
            //1.生成netnut代理
            NetnutUtils netnutUtils=new NetnutUtils();
            if(user.getNetnutCustomerId()!=null&&(!"".equals(user.getNetnutCustomerId()))){
                List<String> netnutList=netnutUtils.getDynamicProxy(user.getNetnutCustomerId(),code,count/3);
                list.addAll(netnutList);
            }
            //2.生成cheap代理
            CheapUtil cheapUtil=new CheapUtil();
            if(user.getCheapUserName()!=null&&(!"".equals(user.getCheapUserName()))){
                List<String> cheapList=cheapUtil.gernateDynamicCheapProxies(count/3,user.getCheapUserName(),code);
                list.addAll(cheapList);
            }
            //3.生成smart代理
            double smartRemain=0d;
            SmartUtil smartUtil=new SmartUtil();
            if(user.getSmartUserName()!=null&&(!user.getSmartUserName().equals(""))) {
                Map<String, String> map = smartUtil.getUserAuthId();
                if (map != null) {
                    List<Smart> smartList = smartUtil.getAllSubUser(map.get("user_id"), map.get("token"));
                    for (int i = 0; i < smartList.size(); i++) {
                        if (smartList.get(i).getUsername().equals(user.getSmartUserName())) {
                            smartRemain = new Double(smartList.get(i).getTraffic_limit()) - smartList.get(i).getTraffic();
                            break;
                        }
                    }
                }
            }
            if(smartRemain>0d){
                List<SmartProxyServer> smartProxyServers = smartProxyServer.selectServer(new SmartProxyServer() {
                    @Override
                    public String getCity() {
                        return code;
                    }
                });
                if(smartProxyServers!=null&&smartProxyServers.size()==1){
                    String port = smartProxyServers.get(0).getPort();
                    String[] dd = port.split("-");
                    int max = new Integer(dd[0]) + count - list.size();
                    List<String> dda=new ArrayList<>();
                    for (int i = new Integer(dd[0]); i < max; i++) {
                        dda.add(smartProxyServers.get(0).getProxyAddress() + ":" + i + ":" + user.getSmartUserName() + ":" + user.getSmartUserPassword());
                    }
                    list.addAll(dda);
                }
            }else{
                //2.通过数据库里面的个人信息去判断是否还有剩余流量，然后生成代理
                //3.打乱代理顺序，并返回
                double netnutRemain=0d;
                List<Netnut>  netnuts=netnutUtils.showCustomerFlowRate(new Integer(user.getNetnutCustomerId()));
                for (int i=0;i<netnuts.size();i++){
                    if(user.getNetnutCustomerId().equals(netnuts.get(i).getCustomerId())){
                        netnutRemain=(new Double(netnuts.get(i).getBandwidth())-new Double(netnuts.get(i).getUsed()));
                    }
                }
                if(netnutRemain>0){
                    if(user.getNetnutCustomerId()!=null&&(!"".equals(user.getNetnutCustomerId()))){
                        List<String> netnutList=netnutUtils.getDynamicProxy(user.getNetnutCustomerId(),code,count-list.size());
                        list.addAll(netnutList);
                    }
                }else{
                    List<String> cheapList=cheapUtil.gernateDynamicCheapProxies(count-list.size(),user.getCheapUserName(),code);
                    list.addAll(cheapList);
                }

            }
            Collections.shuffle(list);
            return list;
        }
        return null;
    }

    /**
     * 生成静态代理
     * @param code 区域
     * @param dcId
     * @param count 数量
     * @return
     */
    public List<String> getStaticProxies(String code,String dcId,int count){
        //1.通过dcId获取数据库里的个人信息
        User user= userDao.selectUserByDcId(new User(){
            @Override
            public String getDcId(){
                return dcId;
            }
        });
        if(user!=null){
            List<String> list=new ArrayList<>();
            NetnutUtils netnutUtils = new NetnutUtils();
            if(user.getNetnutCustomerId()!=null&&(!"".equals(user.getNetnutCustomerId()))) {
                //1.生成netnut代理
                if (user.getNetnutCustomerId() != null && (!"".equals(user.getNetnutCustomerId()))) {
                    List<String> netnutList = netnutUtils.getStaticProxy(user.getNetnutCustomerId(), code, (int) count / 3);
                    list.addAll(netnutList);
                }
            }
            //2.生成cheap代理
            CheapUtil cheapUtil=new CheapUtil();
            if(user.getCheapUserName()!=null&&(!"".equals(user.getCheapUserName()))){
                List<String> cheapList=cheapUtil.gernateStaticCheapProxies((int)count/3,user.getCheapUserName(),code);
                list.addAll(cheapList);
            }
            //3.生成smart代理
            double smartRemain=0d;
            SmartUtil smartUtil=new SmartUtil();
            if(user.getSmartUserName()!=null&&(!user.getSmartUserName().equals(""))) {
                Map<String, String> map = smartUtil.getUserAuthId();
                if (map != null) {
                    List<Smart> smartList = smartUtil.getAllSubUser(map.get("user_id"), map.get("token"));
                    for (int i = 0; i < smartList.size(); i++) {
                        if (smartList.get(i).getUsername().equals(user.getSmartUserName())) {
                            smartRemain = new Double(smartList.get(i).getTraffic_limit()) - smartList.get(i).getTraffic();
                            break;
                        }
                    }
                }
            }
            if(smartRemain>0d) {
                if (user.getSmartUserName() != null && (!"".equals(user.getSmartUserName()))) {
                    List<SmartProxyServer> smartProxyServers = smartProxyServer.selectServer(new SmartProxyServer() {
                        @Override
                        public String getCity() {
                            return code;
                        }
                    });
                    if (smartProxyServers != null && smartProxyServers.size() == 1) {
                        String port = smartProxyServers.get(0).getPort();
                        String[] dd = port.split("-");
                        int max = new Integer(dd[0]) + count - list.size();
                        List<String> dda=new ArrayList<>();
                        for (int i = new Integer(dd[0]); i < max; i++) {
                            dda.add(getAddress(smartProxyServers.get(0).getProxyAddress()) + ":" + i + ":" + user.getSmartUserName() + ":" + user.getSmartUserPassword());
                        }
                        list.addAll(dda);
                    }
                }
            }else {
                //2.通过数据库里面的个人信息去判断是否还有剩余流量，然后生成代理
                //3.打乱代理顺序，并返回
                double netnutRemain=0d;
                List<Netnut>  netnuts=netnutUtils.showCustomerFlowRate(new Integer(user.getNetnutCustomerId()));
                for (int i=0;i<netnuts.size();i++){
                    if(user.getNetnutCustomerId().equals(netnuts.get(i).getCustomerId())){
                        netnutRemain=(new Double(netnuts.get(i).getBandwidth())-new Double(netnuts.get(i).getUsed()));
                    }
                }
                if(netnutRemain>0){
                    if(user.getNetnutCustomerId()!=null&&(!"".equals(user.getNetnutCustomerId()))){
                        List<String> netnutList=netnutUtils.getStaticProxy(user.getNetnutCustomerId(),code,count-list.size());
                        list.addAll(netnutList);
                    }
                }else{
                    List<String> cheapList=cheapUtil.gernateStaticCheapProxies(count-list.size(),user.getCheapUserName(),code);
                    list.addAll(cheapList);
                }
            }
            //4.打乱顺序
            Collections.shuffle(list);
            return list;
        }

        return  null;
    }
    public static String getAddress(String name){
        try{
            Random random=new Random();
            InetAddress[] addresses = InetAddress.getAllByName(name);
            List<String> list=new ArrayList<>();
            for (int i = 0; i < addresses.length; i++) {
                list.add(addresses[i].getHostAddress());
            }
            return list.get(random.nextInt(list.size()-1));
        }catch (Exception e){
            return null;
        }
    }


    public Double showAllData(String dcId){
        Double dd=0d;
        List<Order> list=orderDao.selectOrderByDcId(new Order(){
            @Override
            public String getDcId(){
                return dcId;
            }
        });
        for(int i=0;i<list.size();i++){
            dd+=new Double(list.get(i).getBuyType());
        }
        return dd;

    }
    /**
     * 获取剩余流量
     * @param dcId
     * @return
     */
    public double showRemaining(String dcId){
        User user= userDao.selectUserByDcId(new User(){
            @Override
            public String getDcId(){
                return dcId;
            }
        });
        if(user!=null){
            double remaining=0d;
            try{
                NetnutUtils netnutUtils=new NetnutUtils();
                //1.获取netnut的剩余流量
                if(user.getNetnutCustomerId()!=null&&(!"".equals(user.getNetnutCustomerId()))){
                    List<Netnut>  netnuts=netnutUtils.showCustomerFlowRate(new Integer(user.getNetnutCustomerId()));
                    for (int i=0;i<netnuts.size();i++){
                        if(user.getNetnutCustomerId().equals(netnuts.get(i).getCustomerId())){
                            remaining=remaining+(new Double(netnuts.get(i).getBandwidth())-new Double(netnuts.get(i).getUsed()));
                        }
                    }
                }
            }catch (Exception e){
                logger.error("dcId="+dcId+"获取netNut剩余流量失败");
            }
            //2.获取cheap的剩余流量
            try{
                CheapUtil cheapUtil=new CheapUtil();
                if(user.getCheapUserName()!=null&&(!"".equals(user.getCheapUserName()))) {
                    Cheap cheap=cheapUtil.getCheapInfo(user.getCheapUserName());
                    if(cheap!=null){
                        int balance=cheap.getBalance();
                        remaining=remaining+((double)(balance*1.00d)/300*1.00d);
                    }
                }
            }catch (Exception e){
                logger.error("dcId="+dcId+"获取cheap剩余流量失败");
            }
            //3.获取smart的剩余流量
            try{
                SmartUtil smartUtil=new SmartUtil();
                if(user.getSmartUserName()!=null&&(!"".equals(user.getSmartUserName()))){
                    Map<String,String> map=smartUtil.getUserAuthId();
                    if(map!=null){
                        List<Smart> smartList= smartUtil.getAllSubUser(map.get("user_id"),map.get("token"));
                        for(int i=0;i<smartList.size();i++){
                            if(smartList.get(i).getUsername().equals(user.getSmartUserName())){
                                remaining=remaining+new Double(smartList.get(i).getTraffic_limit())-smartList.get(i).getTraffic();
                                break;
                            }
                        }
                    }
                }
            }catch (Exception e){
                logger.error("dcId="+dcId+"获取smart剩余流量失败");
            }
            return remaining;
        }
        return 0d;
    }
    /**
     * 购买代理
     * @param dcId
     * @param  count (G) must count/3
     * @return
     */
    public boolean buyProxies(String dcId,int count){
        User user=userDao.selectUserByDcId(new User(){
            @Override
            public String getDcId(){
                return dcId;
            }
        });
        if(user!=null){
            //1.分配netnut代理流量
            NetnutUtils netnutUtils=new NetnutUtils();
            if(user.getNetnutCustomerId()!=null&&(!"".equals(user.getNetnutCustomerId()))){
                //分配流量
                try{
                    Boolean flag=netnutUtils.apportionFlowRate(new Integer(user.getNetnutCustomerId()),count/3);
                    if(!flag){
                        logger.error("dcid="+dcId+",netnut分配流量错误");
                    }
                }catch (Exception e){
                    logger.error("dcid="+dcId+",netnut分配流量错误");
                }
            }else{
                try {
                    //创建用户
                    String customerId = netnutUtils.addCustomer();
                    //分配流量
                    Boolean flag = netnutUtils.apportionFlowRate(new Integer(customerId), count / 3);
                    if(flag){
                        user.setNetnutCustomerId(customerId);
                    }else{
                        logger.error("dcid="+dcId+",netnut分配流量错误");
                    }
                }catch (Exception e){
                    logger.error("dcid="+dcId+",netnut创建用户并分配流量错误");
                }


            }
            //2.分配cheap代理流量
            CheapUtil cheapUtil=new CheapUtil();
            if(user.getCheapUserName()!=null&&(!"".equals(user.getCheapUserName()))){
                try{
                    //分配流量
                    Boolean flag=cheapUtil.addBalance(count/3*300,user.getCheapUserName());
                    if(!flag){
                        logger.error("dcid="+dcId+",cheap分配流量错误");
                    }
                }catch (Exception e){
                    logger.error("dcid="+dcId+",cheap分配流量错误");
                }

            }else{
                try{
                    //创建子用户
                    String username=cheapUtil.createUser();
                    Boolean flag=cheapUtil.addBalance(count/3*300,username);
                    if(!flag){
                        logger.error("dcid="+dcId+",cheap分配流量错误");
                    }
                    user.setCheapUserName(username);
                }catch (Exception e){
                    logger.error("dcid="+dcId+",cheap创建用户并分配流量错误");
                }
            }
            //3.分配smart代理流量
            SmartUtil smartUtil=new SmartUtil();
            if(user.getSmartUserName()!=null&&(!"".equals(user.getSmartUserName()))){
                try{
                    //分配流量
                    Map<String,String> map=smartUtil.getUserAuthId();
                    List<Smart> smartList=smartUtil.getAllSubUser(map.get("user_id"),map.get("token"));
                    String smartSubUserId="";
                    String traffic_limit="";//分配流量
                    for(int i=0;i<smartList.size();i++){
                        if(user.getSmartUserName().equals(smartList.get(i).getUsername())){
                            smartSubUserId=smartList.get(i).getId();
                            traffic_limit=smartList.get(i).getTraffic_limit();
                            break;
                        }
                    }
                    Boolean flag=smartUtil.updateUserStaff(smartSubUserId,map.get("user_id"),map.get("token"),new Integer(traffic_limit)+count/3);
                    if(!flag){
                        logger.error("dcid="+dcId+",smart分配流量错误");
                    }
                }catch (Exception e){
                    logger.error("dcid="+dcId+",smart分配流量错误");
                }

            }else{
                try{
                    Map<String,String> map=smartUtil.getUserAuthId();
                    //2.创建子用户
                    Map<String,String>map1=smartUtil.createUser(map.get("user_id"),map.get("token"));
                    user.setSmartUserName(map1.get("sub_username"));
                    user.setSmartUserPassword(map1.get("sub_password"));
                    //3.分配流量
                    List<Smart> smartList=smartUtil.getAllSubUser(map.get("user_id"),map.get("token"));
                    String smartSubUserId="";
                    String traffic_limit="";//分配流量
                    for(int i=0;i<smartList.size();i++){
                        if(user.getSmartUserName().equals(smartList.get(i).getUsername())){
                            smartSubUserId=smartList.get(i).getId();
                            traffic_limit=smartList.get(i).getTraffic_limit();
                            break;
                        }
                    }
                    Boolean flag=smartUtil.updateUserStaff(smartSubUserId,map.get("user_id"),map.get("token"),new Integer(traffic_limit)+count/3);
                    if(!flag){
                        logger.error("dcid="+dcId+",smart分配流量错误");
                    }
                }catch (Exception e){
                    logger.error("dcid="+dcId+",smart创建用户并分配流量错误");
                }

            }
            //update user
            userDao.updateUserProxy(user);
            return true;

        }
        return false;
    }

}
