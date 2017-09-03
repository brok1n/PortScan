package com.brok1n.java.portscan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by brok1n on 2017/9/3.
 */
public class Cmd {

    public static void main(String args[]) {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("cmd");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream inp = process.getInputStream();
                    BufferedReader reader = new BufferedReader( new InputStreamReader( inp ) );
                    while ( true ) {
                        try {
                            String line = reader.readLine();
                            if ( line != null ) {
                                System.out.println( reader.readLine() );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while ( true ) {

                String line = br.readLine();
                if ( line != null ) {
                    process.getOutputStream().write(line.getBytes());
                    process.getOutputStream().write("\n".getBytes());
                    process.getOutputStream().flush();
                }

                Thread.sleep( 100 );
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
