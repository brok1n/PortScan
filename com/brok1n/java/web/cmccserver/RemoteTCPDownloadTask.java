package com.brok1n.java.web.cmccserver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by jll on 2017/8/22.
 */

public class RemoteTCPDownloadTask implements Runnable {

    //要连接的服务器IP和端口
    private String serverIp;
    private int port;
    //视频文件播放地址
    private String videoUrl;

    //建立TCP连接之前
    private long beforeConnectTime;
    //建立TCP连接之后
    private long afterConnectTime;

    //发送get请求之前的时间
    private long beforeSendTime;
    //发送get请求之后的时间
    private long afterSendTime;
    //读取响应头数据之前的时间
    private long beforeReadTime;
    //读取响应头数据之后的时间
    private long afterReadTime;
    //读取响应头后的时间和发送请求后的时间差  这是HTTP请求响应时长
    private long beforeOffsetTime;
    private long afterOffsetTime;
    private long respTime;//HTTP响应时长
    //建立TCP连接前后的时间差 这是TCP三次握手时长
    private long offsetConnectTime;

    //响应头
    byte[] respHeader = new byte[1024*1024];

    public RemoteTCPDownloadTask(String ip, int port, String path ) {
        this.serverIp = ip;
        this.port = port;
        this.videoUrl = path;
    }

    @Override
    public void run() {
        System.out.println("before start run before start sleep");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("start connect  start connect");
        try {
            beforeConnectTime = System.currentTimeMillis();
            //建立TCP连接
            Socket socket = new Socket( serverIp, port);
            afterConnectTime = System.currentTimeMillis();

            offsetConnectTime = afterConnectTime - beforeConnectTime;

            //System.out.println("before:" + beforeConnectTime + " after:" + afterConnectTime + " offset:" + ( afterConnectTime - beforeConnectTime ) );
            System.out.println("三次握手时长:" + offsetConnectTime );

            InputStream inp = socket.getInputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() ) );

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //发送get请求
            beforeSendTime = System.currentTimeMillis();//SystemClock.elapsedRealtimeNanos();//

            writer.write("GET "+ videoUrl +" HTTP1.1\r\nHost:" + serverIp + ":" + port + "\r\nUser-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36\r\n\r\n");
            writer.flush();
            afterSendTime = System.currentTimeMillis();//SystemClock.elapsedRealtimeNanos();//

            //等到有响应数据
            while ( inp.available() <= 0 ) {}

            beforeReadTime = System.currentTimeMillis();//SystemClock.elapsedRealtimeNanos();//
            //HTTP/1.1 200 OK
            inp.read( respHeader );
            afterReadTime = System.currentTimeMillis();//SystemClock.elapsedRealtimeNanos();//

            socket.close();

            afterOffsetTime = afterReadTime - afterSendTime;
            beforeOffsetTime = beforeReadTime - beforeSendTime;

            respTime = ( afterOffsetTime + beforeOffsetTime ) / 2;

            System.out.println("task beforeSendTime:" + beforeSendTime + " afterSendTime:" + afterSendTime + " beforeReadTime:" + beforeReadTime + " afterReadTime:" + afterReadTime + " beforeOffsetTime:" + beforeOffsetTime + " afterOffsetTime:" + afterOffsetTime );

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
