package com.brok1n.java.portscan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by brok1n on 2017/9/2.
 */
public class PingTest {

    public static void main(String args[]) {

        Runtime ping = Runtime.getRuntime();
        try {
            long start = System.currentTimeMillis();
            System.out.println( start );
            Process process = ping.exec("ping -n 2 -l 8 -w 300 www.asdfasdfasdf.com");
            BufferedReader br = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            String line = null;
            int sum = 0;
            while ( ( line = br.readLine() ) != null  ) {
                try {
                    if ( line.indexOf("TTL") > 0 ) {
                        int msIndex = line.indexOf("ms");
                        int eqIndex = line.substring(0, msIndex ).lastIndexOf("=");
                        String ms = line.substring( eqIndex + 1, msIndex );
                        sum += Integer.parseInt( ms );
                    }
                } catch ( Exception e){}
            }

            int avg = sum / 2;
            System.out.println("avg:" + avg );
            long end = System.currentTimeMillis();
            System.out.println( end-start );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
