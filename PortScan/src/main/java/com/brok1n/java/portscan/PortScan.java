package com.brok1n.java.portscan;

import java.util.ArrayList;

/**
 * Created by brok1n on 2017/8/31.
 */
public class PortScan {

    //TCP连接超时时间
    private static int timeout = 300;
    //使用线程数
    private static int threadNum = 300;

    /**
     *  -i
     *      ip  one or more ip address
     *      example: one ip
     *          -i 192.168.1.100
     *      example: more ip split with ','
     *          -i 192.168.1.100,192.168.1.135,192.168.1.188
     *      example: ip section split with ':'
     *          -i 192.168.1.2:192.168.1.254
     *  -p
     *      port one or more port
     *      example: one port
     *          -p 80
     *      example: more port split with','
     *          -p 21,22,25,135,443,445,3306,3389
     *      example: port section split with ':'
     *          -p 20:30000
     *  -t
     *      timeout default 300
     *  -th
     *      processing thread number default 300;
     *
     *
     * */
    public static void main(String args[]) {

        //查看帮助文档
        if ( args.length == 1 && ( args[0].indexOf("?") >= 0 || args[0].indexOf("help") >= 0 || args[0].indexOf("h") >= 0 || args[0].indexOf("man") >= 0 )) {
            printHelp();
            return ;
        }

        //参数错误 提示 并打印帮助文档
        if ( args.length % 2 != 0 ) {
            System.err.println("parameter error ");
            printHelp();
            return ;
        }

        //要扫描的ip列表
        ArrayList<String> ipList = new ArrayList<>();
        //要扫描的端口列表
        ArrayList<Integer> portList = new ArrayList<>();

        //解析参数
        parseParameter( args, ipList, portList );

        System.out.println( ipList.toString() );
        System.out.println( portList.toString() );

        ScanPortTask scanPortTask = new ScanPortTask("www.brok1n.com", 80, 10000, 270, 300);

        Thread thread = new Thread( scanPortTask);
        thread.start();
    }

    private static void parseParameter(String args[], ArrayList<String> ipList, ArrayList<Integer> portList) {
        try {
            for ( int p = 0; p < args.length; p ++ ) {
                String key = args[p];
                String val = "";
                if ( key.startsWith("-i") ) {
                    val = args[p+1];
                    parseIp( val, ipList );
                } else if ( key.startsWith("-p") ) {
                    val = args[p+1];
                    parsePort( val, portList );
                } else if ( key.startsWith("-t") ) {
                    val = args[p+1];
                    timeout = Integer.parseInt(val);
                } else if ( key.startsWith("-th") ) {
                    val = args[p+1];
                    threadNum = Integer.parseInt(val);
                }

            }
        }catch ( Exception e) {
            printHelp();
        }
    }

    /**
     * 解析端口列表
     * */
    private static void parsePort(String val, ArrayList<Integer> portList) {
        if ( val == null || portList == null )
            return;

        //尝试解析端口列表
        String portArr[] = val.split(",");
        if ( portArr.length > 1 ) {
            for ( int i = 0; i < portArr.length; i ++ ) {
                //尝试解析端口区间
                String portSection[] = portArr[i].split(":");
                if ( portSection.length > 1 && portSection.length == 2 ) {
                    try {
                        int start = Integer.parseInt( portSection[0] );
                        int end = Integer.parseInt( portSection[1] );
                        for ( int n = start;n <= end; n++ ) {
                            portList.add( n );
                        }
                    } catch (Exception e) {}
                } else if ( portSection.length == 1 ) {
                    try {
                        int port = Integer.parseInt( portArr[i]);
                        portList.add(port);
                    } catch (Exception e) {
                    }
                }
            }
            return;
        }

        //尝试解析端口区间
        String portSection[] = val.split(":");
        if ( portSection.length > 1 && portSection.length == 2 ) {
            try {
                int start = Integer.parseInt( portSection[0] );
                int end = Integer.parseInt( portSection[1] );
                for ( int n = start;n <= end; n++ ) {
                    portList.add( n );
                }
            } catch (Exception e) {}
        }
        return;
    }

    /**
     * 解析ip列表
     * */
    private static void parseIp(String val, ArrayList<String> ipList) {
        if ( val == null || ipList == null )
            return;

        return;
    }

    public static void printHelp() {
        System.out.println("Name:Port Scan");
        System.out.println("Author:brok1n");
        System.out.println("Version:1.0.0");
        System.out.println("Email:brok1n@outlook.com");
        System.out.println("PortScan.jar -i ip -p port [-t timeout] [-th thread number]");
        System.out.println("Example: PortScan.jar -i 192.168.1.105 -p 80:3389 -t 100 -th 500");
        System.out.println("-i\n" +
                "    ip  one or more ip address\n" +
                "    example: one ip\n" +
                "        -i 192.168.1.100\n" +
                "    example: more ip split with ','\n" +
                "        -i 192.168.1.100,192.168.1.135,192.168.1.188\n" +
                "    example: ip section split with ':'\n" +
                "        -i 192.168.1.2:192.168.1.254\n" +
                "-p\n" +
                "    port one or more port\n" +
                "    example: one port\n" +
                "        -p 80\n" +
                "    example: more port split with','\n" +
                "        -p 21,22,25,135,443,445,3306,3389\n" +
                "    example: port section split with ':'\n" +
                "        -p 20:30000\n" +
                "-t\n" +
                "    timeout default 300\n" +
                "-th\n" +
                "    processing thread number default 300");
    }
}
