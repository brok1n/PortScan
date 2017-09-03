package com.brok1n.java.portscan;

import jdk.nashorn.internal.ir.CatchNode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by brok1n on 2017/8/31.
 */
public class PortScan {

    //固定数量线程池
    private static ExecutorService fixedThreadPool;

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

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

        printInfo();

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

        DataCenter dc = DataCenter.getInstance();
        //解析参数
        parseParameter( args );

        //预计扫描IP数量
        System.out.println("预计扫描IP数:" + dc.getIpList().size());
        System.out.println("\t\tIP列表:" + dc.getIpParameter() );
        System.out.println("预计扫描端口数:" + dc.getPortList().size());
        System.out.println("\t\t端口列表:" + dc.getPortParameter() );
        System.out.println("连接超时时间:" + dc.getTimeout() );
        System.out.println("线程数:" + dc.getThreadNum() );
        //System.out.println("机器数量:" + dc.getMachineList().size() );
        //System.out.println("机器:" + dc.getMachineList().toString() );

        //ip超过10个就需要优化扫描效率
        if ( dc.getIpList().size() > 5 ) {
            //优化扫描
            analysisOptimize();
        } else {
            //直接扫描
            scan();
        }

        //自动扫描当前系统压力
        //总内存 程序占用内存 剩余内存 内存低于一定阈值 自动将内存数据保存到文件以便重启继续
        //cpu使用量 程序cpu使用量 剩余cpu使用量  系统处于控线状态 提高程序cpu使用量 增加线程 or 增加进程 同时处理
        //cpu占用过高 到一定时间  减少线程、进程  存文件、

        System.out.println("扫描完成:" + simpleDateFormat.format(new Date( System.currentTimeMillis() )) );
        Machine list[] = dc.getOnLineMachineList().toArray( new Machine[]{});
        for (int m = 0; m < list.length; m ++ ) {
            Machine machine = list[m];
            System.out.println("" + machine.getIp() + ": " + machine.getOpenPortList().toString() );
        }

    }

    //直接扫描 使用优化
    private static void scan() {
        DataCenter dc = DataCenter.getInstance();
        dc.getOnLineMachineList().addAll( dc.getMachineList() );
        fixedThreadPool = Executors.newFixedThreadPool(dc.getThreadNum());
        ScanMorePortTask scanMorePortTask = new ScanMorePortTask( fixedThreadPool );
        Thread thread = new Thread( scanMorePortTask );
        thread.start();

        while ( !fixedThreadPool.isShutdown() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //自动计算预计总时长
    //总时长过大 自动开始优化处理
    //优化第一步 处理无效IP
    //优化第二步 处理离线IP
    //优化第三部 处理超时时间
    private static void analysisOptimize() {
        System.out.println("预计扫描时间过长 开始优化处理:" + simpleDateFormat.format(new Date( System.currentTimeMillis() )));
        System.out.println("正在扫描主机在线状态...");
        //处理无效IP
        DataCenter dc = DataCenter.getInstance();
        Thread pingThread = new Thread( new PingTask( ) );
        pingThread.start();

        //等待ping处理完成
        while ( !dc.isPingOk() ) {
            try {
                Thread.sleep(1000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("主机在线状态扫描完成:" + simpleDateFormat.format(new Date( System.currentTimeMillis() )));
        System.out.println("总主机数量:" + dc.getMachineList().size() );
        System.out.println("在线主机数量:" + dc.getOnLineMachineList().size() );
        System.out.println("离线主机数量:" + (dc.getMachineList().size() - dc.getOnLineMachineList().size() ) );
        System.out.println("优化处理完成！");
        System.out.println("扫描主机数量:" + dc.getOnLineMachineList().size() );
        System.out.println("主机连接超时时间优化成功");

        fixedThreadPool = Executors.newFixedThreadPool(dc.getThreadNum());
        ScanMorePortTask scanMorePortTask = new ScanMorePortTask( fixedThreadPool );
        Thread scaThread = new Thread( scanMorePortTask );
        scaThread.start();

        while ( !fixedThreadPool.isShutdown() ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析命令行参数
     * */
    private static void parseParameter(String args[]) {
        try {
            for ( int p = 0; p < args.length; p ++ ) {
                String key = args[p].trim();
                String val = "";
                if ( key.equals("-i") ) {
                    val = args[p+1].trim();
                    parseIp( val);
                } else if ( key.equals("-p") ) {
                    val = args[p+1].trim();
                    parsePort( val);
                } else if ( key.equals("-t") ) {
                    val = args[p+1].trim();
                    DataCenter.getInstance().setTimeout(Integer.parseInt(val));
                } else if ( key.equals("-th") ) {
                    val = args[p+1].trim();
                    DataCenter.getInstance().setThreadNum(Integer.parseInt(val));
                }

            }
        }catch ( Exception e) {
            printHelp();
        }
    }

    /**
     * 解析端口列表
     * */
    private static void parsePort(String val) {
        if ( val == null )
            return;
        DataCenter dc = DataCenter.getInstance();
        dc.setPortParameter( val );
        //尝试解析端口列表
        String portArr[] = val.split(",");
        if ( portArr.length > 1 ) {
            for ( int i = 0; i < portArr.length; i ++ ) {
                //尝试解析端口区间
                String portSection[] = portArr[i].trim().split(":");
                if ( portSection.length > 1 && portSection.length == 2 ) {
                    try {
                        int start = Integer.parseInt( portSection[0].trim() );
                        int end = Integer.parseInt( portSection[1].trim() );
                        for ( int n = start;n <= end; n++ ) {
                            dc.getPortList().add( n );
                        }
                    } catch (Exception e) {}
                } else if ( portSection.length == 1 ) {
                    try {
                        int port = Integer.parseInt( portArr[i].trim() );
                        dc.getPortList().add(port);
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
                int start = Integer.parseInt( portSection[0].trim() );
                int end = Integer.parseInt( portSection[1] );
                for ( int n = start;n <= end; n++ ) {
                    dc.getPortList().add( n );
                }
            } catch (Exception e) {}
        }

        //尝试解析单个端口
        try {
            int port = Integer.parseInt( val );
            if ( port >= 0 && port <= 65535 ) {
                dc.getPortList().add( port);
            }
        } catch ( Exception e) {}

        return;
    }

    /**
     * 解析ip列表
     * */
    private static void parseIp(String val) {
        if ( val == null )
            return;
        DataCenter dc = DataCenter.getInstance();
        dc.setIpParameter( val );
        //尝试解析IP列表
        String ipArr[] = val.split(",");
        if ( ipArr.length > 1 ) {
            for ( int i = 0; i < ipArr.length; i ++ ) {
                //尝试解析IP区间
                String ipSection[] = ipArr[i].trim().split(":");
                if ( ipSection.length > 1 && ipSection.length == 2 ) {
                    try {
                        String start = ipSection[0].trim();
                        String end = ipSection[1].trim();
                        String startIpBit[] = start.split("\\.");
                        int startOne = Integer.parseInt( startIpBit[0].trim() );
                        int startTwo = Integer.parseInt( startIpBit[1].trim() );
                        int startThree = Integer.parseInt( startIpBit[2].trim() );
                        int startFour = Integer.parseInt( startIpBit[3].trim() );

                        String endIpBit[] = end.split("\\.");
                        int endOne = Integer.parseInt(  endIpBit[0].trim() );
                        int endTwo = Integer.parseInt(  endIpBit[1].trim() );
                        int endThree = Integer.parseInt(endIpBit[2].trim() );
                        int endFour = Integer.parseInt( endIpBit[3].trim() );

                        for ( int one = startOne; one <= endOne; one ++) {
                            for ( int two = startTwo; two <= endTwo; two ++ ) {
                                for ( int three = startThree; three <= endThree; three ++ ) {
                                    for ( int four = startFour; four <= endFour; four ++ ) {
                                        String ip = "" + one + "." + two + "." + three + "." + four;
                                        dc.getIpList().add( ip );
                                        dc.getMachineList().add( new Machine( ip, dc.getTimeout() ) );
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {}
                } else if ( ipSection.length == 1 ) {
                    try {
                        dc.getIpList().add(ipArr[i].trim());
                        dc.getMachineList().add( new Machine( ipArr[i].trim(), dc.getTimeout() ) );
                    } catch (Exception e) {
                    }
                }
            }
            return;
        }

        //尝试解析IP区间
        String ipSection[] = val.split(":");
        if ( ipSection.length > 1 && ipSection.length == 2 ) {
            try {
                String start = ipSection[0].trim();
                String end = ipSection[1].trim();
                String startIpBit[] = start.split("\\.");
                if ( startIpBit.length != 4 )
                {
                    return;
                }
                int startOne = Integer.parseInt( startIpBit[0].trim() );
                int startTwo = Integer.parseInt( startIpBit[1].trim() );
                int startThree = Integer.parseInt( startIpBit[2].trim() );
                int startFour = Integer.parseInt( startIpBit[3].trim() );

                String endIpBit[] = end.split("\\.");
                if ( endIpBit.length != 4 )
                {
                    return;
                }
                int endOne = Integer.parseInt(  endIpBit[0].trim() );
                int endTwo = Integer.parseInt(  endIpBit[1].trim() );
                int endThree = Integer.parseInt(endIpBit[2].trim() );
                int endFour = Integer.parseInt( endIpBit[3].trim() );

                for ( int one = startOne; one <= endOne; one ++) {
                    int twoStart = one == startOne ? startTwo : 0;
                    int twoEnd = one != endOne ? 255 : endTwo;
                    for ( ; twoStart <= twoEnd; twoStart++ ) {
                        int threeStart = twoStart == startTwo && one == startOne ? startThree : 0;
                        int threeEnd = twoStart != endTwo ? 255 : endThree;
                        for ( ; threeStart <= threeEnd; threeStart ++ ) {
                            int fourStart = threeStart == startThree && twoStart == startTwo && one == startOne ? startFour : 0;
                            int fourEnd =  threeStart != endThree ? 255 : endFour;
                            for (; fourStart <= fourEnd; fourStart ++ ) {
                                String ip = "" + one + "." + twoStart + "." + threeStart + "." + fourStart;
                                dc.getIpList().add( ip );
                                dc.getMachineList().add( new Machine( ip, dc.getTimeout() ) );
                            }
                        }
                    }
                }

            } catch (Exception e) {}
        }

        //尝试解析单个IP
        try {
            String ip = val;
            String ipSp[] = ip.split("\\.");
            if ( ipSp.length == 4 ) {
                dc.getIpList().add(ip);
                dc.getMachineList().add( new Machine( ip, dc.getTimeout() ) );
            }
        } catch ( Exception e) {}

        return;
    }

    /**
     * 打印帮助信息
     * */
    public static void printHelp() {
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

    /**
     * 打印软件信息
     * */
    public static void printInfo(){
        System.out.println("Name:Port Scan");
        System.out.println("Author:brok1n");
        System.out.println("Version:1.0.0");
        System.out.println("Email:brok1n@outlook.com");
        System.out.println("time:" + simpleDateFormat.format(new Date( System.currentTimeMillis() )));
    }
}
