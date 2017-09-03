package com.brok1n.java.portscan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by brok1n on 2017/9/2.
 */
public class DataCenter {

    private volatile static DataCenter dc;

    //TCP连接超时时间
    private int timeout = 300;
    //使用线程数
    private int threadNum = 300;

    //ping是否处理完成
    private boolean pingOk = false;

    //要扫描的IP  ip参数
    private String ipParameter;
    //要扫描的端口列表 端口参数
    private String portParameter;

    //端口列表
    private ConcurrentLinkedQueue<Integer> portList = new ConcurrentLinkedQueue<>();
    //ip列表
    private ConcurrentLinkedQueue<String> ipList = new ConcurrentLinkedQueue<>();
    //机器列表
    private ConcurrentLinkedQueue<Machine> machineList = new ConcurrentLinkedQueue<>();
    //在线
    private ConcurrentLinkedQueue<Machine> onLineMachineList = new ConcurrentLinkedQueue<>();

    public boolean isPingOk() {
        return pingOk;
    }

    public void setPingOk(boolean pingOk) {
        this.pingOk = pingOk;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public ConcurrentLinkedQueue<Integer> getPortList() {
        return portList;
    }

    public ConcurrentLinkedQueue<String> getIpList() {
        return ipList;
    }

    public ConcurrentLinkedQueue<Machine> getMachineList() {
        return machineList;
    }

    public ConcurrentLinkedQueue<Machine> getOnLineMachineList() {
        return onLineMachineList;
    }

    public String getIpParameter() {
        return ipParameter;
    }

    public void setIpParameter(String ipParameter) {
        this.ipParameter = ipParameter;
    }

    public String getPortParameter() {
        return portParameter;
    }

    public void setPortParameter(String portParameter) {
        this.portParameter = portParameter;
    }

    private DataCenter(){}
    public static DataCenter getInstance(){
        if (dc == null ) {
            synchronized ( DataCenter.class ) {
                if ( dc == null ) {
                    dc = new DataCenter();
                }
            }
        }
        return dc;
    }
}
