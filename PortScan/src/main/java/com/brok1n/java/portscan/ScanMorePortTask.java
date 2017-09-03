package com.brok1n.java.portscan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jll on 2017/8/30.
 */
public class ScanMorePortTask implements Runnable {

    ExecutorService fixedThreadPool;

    public ScanMorePortTask( ExecutorService fixedThreadPool ){
        this.fixedThreadPool = fixedThreadPool;
    }

    @Override
    public void run() {
        DataCenter dc = DataCenter.getInstance();
        System.out.println("start time:" + PortScan.simpleDateFormat.format(new Date( System.currentTimeMillis() ) ) );
        Machine machines[] = dc.getOnLineMachineList().toArray( new Machine[]{});
        while ( !dc.getPortList().isEmpty() ) {
            final int port = dc.getPortList().poll();
            for ( int i = 0; i < machines.length; i ++ ) {
                final Machine machine = machines[i];
                fixedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SocketAddress socketAddress = new InetSocketAddress( machine.getIp(), port);
                            Socket socket = new Socket();
                            socket.connect( socketAddress, machine.getTimeout() );
                            machine.getOpenPortList().add( port );
                            System.out.println(""+machine.getIp() + " " + port );
                            socket.close();
                        } catch (IOException e) {
                            //e.printStackTrace();
                        }
                    }
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        fixedThreadPool.shutdown();
    }

}
