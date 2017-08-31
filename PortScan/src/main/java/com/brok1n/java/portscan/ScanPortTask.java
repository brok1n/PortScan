package com.brok1n.java.portscan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jll on 2017/8/30.
 */
public class ScanPortTask implements Runnable {

    String ip;
    int startPort;
    int endPort;
    int timeout = 200;
    int threadNum = 100;

    public ScanPortTask( String ip, int startPort, int endPort ){
        this.ip = ip;
        this.startPort = startPort;
        this.endPort = endPort;
    }

    public ScanPortTask( String ip, int startPort, int endPort, int timeout,int threadNum ){
        this.ip = ip;
        this.startPort = startPort;
        this.endPort = endPort;
        this.timeout = timeout;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println("start time:" + simpleDateFormat.format(new Date( System.currentTimeMillis() ) ) );
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadNum);
        for ( int i = startPort; i <= endPort ; i ++ ) {

            int finalI = i;

            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SocketAddress socketAddress = new InetSocketAddress( ip, finalI);
                        Socket socket = new Socket();
                        socket.connect( socketAddress, timeout );
                        System.err.println("ip:" + ip + " 端口:" + finalI + " 开放 " );
                        socket.close();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                    if ( finalI >= endPort ) {
                        System.out.println("end time:" + simpleDateFormat.format( new Date( System.currentTimeMillis() ) ) );
                    }
                }
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fixedThreadPool.shutdown();
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
}
