package com.brok1n.java.web.cmccserver;

import java.io.*;
import java.net.Socket;

/**
 * Created by jll on 2017/8/28.
 */
public class DownloadTask implements Runnable {

    //服务器IP
    private String serverIp;
    //服务器端口
    private int serverPort;
    //请求地址
    private String url;
    //1M的文件下载临时存储容量
    private byte[] dataArray = new byte[1024*100];

    public DownloadTask(String ip, int port, String url) {
        this.serverIp = ip;
        this.serverPort = port;
        this.url = url;
    }

    @Override
    public void run() {

        try {
            Socket socket = new Socket( serverIp, serverPort);
            InputStream inp = socket.getInputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            //发送HTTP请求
            String req = "GET /2.ts HTTP/1.1\r\nHost: 192.168.1.222:8080\r\nConnection: keep-alive\r\nUpgrade-Insecure-Requests: 1\r\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n\r\n";
            //writer.write("GET /2.ts HTTP1.1\r\nHost:" + serverIp + ":" + serverPort + "\r\nConnection: Keep-Alive\r\nUser-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36\r\n\r\n");
            writer.write(req);
            writer.flush();

            //等待响应
            while ( inp.available()<= 0 ) {}

            //读取请求头
            BufferedReader reader = new BufferedReader(new InputStreamReader( inp ));
            long contentLen = 0;
            do {
                String line = reader.readLine();
                //contentlen
                if ( line.startsWith("Content-Length") ) {
                    contentLen = Long.parseLong(line.split(":")[1].trim());
                }
                System.out.println(line);
                if ( line.equals(""))//空行
                    break;
            }while (true);


            int sumLen = 0;
            //下载开始时间
            long startDownloadTime = System.currentTimeMillis();
            //下载1M开始时间
            long speedStartTime = 0;
            //
            long speedTime = 0;
            int count = 0;
            while ( sumLen < contentLen ) {
                speedStartTime = System.currentTimeMillis();
                int len = 0;
                for ( int i = 0; i < 10; i ++ ) {
                    int lineLen = inp.read( dataArray );
                    sumLen += lineLen;
                    len += lineLen;
                    if ( sumLen >= contentLen )
                        break;
                }
                speedTime = System.currentTimeMillis() - speedStartTime;
                System.out.println("offset:" + speedTime + " len:" + len );
                if ( speedTime == 0 )
                    break;
                float speed = len * ( 1000.0f / speedTime )  / 1024.0f;
                String speedStr = speed > 1000 ? speed / 1024.0f + "MB/s" : speed + "KB/s";
                System.out.println("下载速度:" + speedStr );
                System.out.println("len:" + len);
            }
            //下载结束时间
            long endDownloadTime = System.currentTimeMillis();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
