package com.brok1n.java.portscan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by brok1n on 2017/9/2.
 */
public class PingTask implements Runnable {

    //获取延迟时间正则
    String delayTimeReg = "[=|<]\\d{1,6}ms";
    private ExecutorService fixedThreadPool;
    Pattern pattern = Pattern.compile(delayTimeReg);
    public static long checkCount = 0;

    //public PingTask( ExecutorService fixedThreadPool ){
    public PingTask(){
        //this.fixedThreadPool = fixedThreadPool;
        checkCount = 0;
        this.fixedThreadPool = Executors.newFixedThreadPool(50);
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
        fixedThreadPool.shutdown();
    }

    class PingRunnable implements Runnable{

        Machine machine;
        public PingRunnable( Machine machine ) {
            this.machine = machine;
        }

        @Override
        public void run() {
            DataCenter dc = DataCenter.getInstance();
            //获取到机器的平均延时  0为机器不在线
            String ip = machine.getIp();
            int delayTime = ping( ip );
            //设置机器在线状态 设置机器连接超时时间
            if ( delayTime > 0 ) {
                //在线
                //设置连接超时时间比平均延时大100
                machine.setTimeout( delayTime + 100 );
                machine.setOnline(true);
                dc.getOnLineMachineList().add(machine);
            }
            checkCount ++;
            if ( checkCount >= dc.getMachineList().size() ) {
                try {
                    Thread.sleep(2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DataCenter.getInstance().setPingOk(true);
            }
        }
    }

    private int ping(String ip ){
        int avg = 0;
        Runtime ping = Runtime.getRuntime();
        try {
            Process process = ping.exec("ping -n 2 -w 600 " + ip );
            BufferedReader br = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            while ( br.readLine() == null ) {Thread.sleep(10);}
            String line = null;
            int sum = 0;
            int count = 0;
            while ( ( line = br.readLine() ) != null  ) {
                if ( line.indexOf(" TTL") <= 0 )
                    continue;
                Matcher matcher = pattern.matcher(line);
                if ( !matcher.find() )
                    continue;
                String matchStr = matcher.group(0);
                matchStr = matchStr.replace("<", "");
                matchStr = matchStr.replace("=", "");
                matchStr = matchStr.replace("ms", "");
                sum += Integer.parseInt( matchStr );
                count ++;
                avg = sum / count;
                Thread.sleep(10 );
            }
            br.close();
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
        return avg;
    }

}
