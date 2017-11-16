package com.brok1n.java.portscan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brok1n on 2017/9/2.
 */
public class PingTask implements Runnable {

    //获取延迟时间正则
    private ExecutorService fixedThreadPool;
    public static long checkCount = 0;

    public PingTask( ExecutorService fixedThreadPool){
        checkCount = 0;
        this.fixedThreadPool = fixedThreadPool;
    }

    @Override
    public void run() {
        //fixedThreadPool.execute(new PingRunnable( DataCenter.getInstance().getMachineList().get(1)));
        DataCenter dc = DataCenter.getInstance();
        Machine machines[] = dc.getMachineList().toArray( new Machine[]{});
        for ( int i = 0; i < machines.length; i ++ ) {
            fixedThreadPool.execute(new PingRunnable( machines[i] ));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class PingRunnable implements Runnable{

        Machine machine;
        public PingRunnable( Machine machine ) {
            this.machine = machine;
        }

        @Override
        public synchronized void run() {
            DataCenter dc = DataCenter.getInstance();
            //获取到机器的平均延时  0为机器不在线
            String ip = machine.getIp();
            System.out.print("\r");
            System.out.print(ip);
            try {
                boolean status = InetAddress.getByName(ip).isReachable(2000);
                machine.setOnline( status );
                if ( status ){
                    dc.getOnLineMachineList().add( machine );
                    System.out.print("        ");
                    System.out.println("OnLine");
                } else {
                    dc.getOffLineMachineList().add(machine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkCount ++ ;
            if ( checkCount >= dc.getMachineList().size() ) {
                dc.setPingOk( true );
            }

        }
    }

}
