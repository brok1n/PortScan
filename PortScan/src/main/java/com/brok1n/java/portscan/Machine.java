package com.brok1n.java.portscan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brok1n on 2017/9/2.
 */
public class Machine {

    //机器ip
    private String ip;
    //连接机器的超时时间  ping平均时间+100
    private int timeout;
    //机器开放端口
    private List<Integer> openPortList = new ArrayList<>();
    //是否在线
    private boolean isOnline;

    public Machine(String ip, int timeout) {
        this.ip = ip;
        this.timeout = timeout;
    }

    public String getIp() {
        return ip;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public List<Integer> getOpenPortList() {
        return openPortList;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "ip='" + ip + '\'' +
                ", timeout=" + timeout +
                ", openPortList=" + openPortList +
                ", isOnline=" + isOnline +
                '}';
    }
}
