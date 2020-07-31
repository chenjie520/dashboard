package com.net.dashboard.dao;

import com.net.dashboard.pojo.SmartProxyServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ISmartProxyServer {
    @Select("select id,city,proxy_address as proxyAddress,port from smart_proxy_server where city=#{city}")
    List<SmartProxyServer> selectServer(SmartProxyServer smartProxyServer);
}
