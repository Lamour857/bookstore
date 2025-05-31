package com.wj.bookstore.common.utils;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;


import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-14-10:55
 **/
@Slf4j
public class IpUtil {
    private static final String UNKNOWN = "unKnown";
    private static final String DEFAULT_IP = "127.0.0.1";
    private static String LOCAL_IP = null;
    public static String getLocalIp4Address() throws SocketException {
        if(LOCAL_IP != null){
            return LOCAL_IP;
        }
        // 从网络接口(网卡)获取ip地址
        final List<Inet4Address> inet4Addresses=getLocalIp4AddressFromNetworkInterface();
        // 若网卡绑定多个地址, 则第一个元素可能不准确
        // 或没有绑定任何地址, 则返回默认值
        if(inet4Addresses.size()!=1){
            final Optional<Inet4Address> ipBySocketOpt=getIpBySocket();
            LOCAL_IP=ipBySocketOpt.map(Inet4Address::getHostAddress).orElseGet(()->inet4Addresses.isEmpty()?DEFAULT_IP:inet4Addresses.get(0).getHostAddress());
            return LOCAL_IP;
        }
        LOCAL_IP=inet4Addresses.get(0).getHostAddress();
        return LOCAL_IP;
    }
    /**
     * 通过Socket 唯一确定一个IP
     */
    private static Optional<Inet4Address> getIpBySocket() throws SocketException{
        try(final DatagramSocket socket=new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if(socket.getLocalAddress() instanceof Inet4Address){
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    // 从网络接口(网卡)获取ip地址
    private static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses=new ArrayList<>(1);
        // 所有网络接口信息
        Enumeration<NetworkInterface> networkInterfaces=NetworkInterface.getNetworkInterfaces();
        if(ObjectUtils.isEmpty(networkInterfaces)){
            return addresses;
        }
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface=networkInterfaces.nextElement();
            if(!isValidInterface(networkInterface)){
                continue;
            }
            // 网络接口的ip地址
            Enumeration<InetAddress> inetAddresses=networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress=inetAddresses.nextElement();
                // 判断是否是ipv4地址且内网地址, 过滤回环地址
                if(isValidAddress(inetAddress)){
                    addresses.add((Inet4Address)inetAddress);
                }
            }
        }
        return addresses;
    }

    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    /** 过滤回环网卡,点对点网卡,非活动网卡和虚拟网卡, 要求网卡名字以eth或ens开头 */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    public static String getClientIp(HttpServletRequest request) {
        try {
            // 反向代理服务器(nginx)设置标识真实ip
            String xIp = request.getHeader("X-Real-IP");
            // 表请求经过的代理服务器链
            String xFor = request.getHeader("X-Forwarded-For");
            if (StringUtils.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
                //多次反向代理后会有多个ip值，第一个ip才是真实ip
                int index = xFor.indexOf(",");
                if (index != -1) {
                    return xFor.substring(0, index);
                } else {
                    return xFor;
                }
            }
            xFor = xIp;
            // X-Forwarded-For不存在或无效
            if (StringUtils.isNotEmpty(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
                return xFor;
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
                xFor = request.getRemoteAddr();
            }

            if ("localhost".equalsIgnoreCase(xFor) || "127.0.0.1".equalsIgnoreCase(xFor) || "0:0:0:0:0:0:0:1".equalsIgnoreCase(xFor)) {
                return getLocalIp4Address();
            }
            return xFor;
        } catch (Exception e) {
            log.error("get remote ip error!", e);
            return "x.0.0.1";
        }
    }
}
