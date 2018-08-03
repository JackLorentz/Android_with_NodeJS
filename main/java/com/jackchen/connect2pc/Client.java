package com.jackchen.connect2pc;

import com.google.android.gms.fit.samples.common.logger.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by jackchen on 2018/7/27.
 */

public class Client implements Runnable{
    private static String TAG = "Connect2PC.Client";
    private String host = "140.117.111.138";
    private int port;
    private Socket s = null;
    private BufferedWriter out = null;

    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void run(){
        try{
            s = new Socket(host, port);
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            String req = "Server gets the message.";
            out.write(req);
            out.flush();
            s.shutdownOutput();
        }
        catch (IOException e){
            e.printStackTrace();
            Log.e(TAG, "Socket連線: " + e.toString());
        }

        try{
            Scanner in = new Scanner(s.getInputStream(), "UTF-8");
            while(in.hasNextLine()){
                Log.i(TAG, in.nextLine());
            }
        }
        catch(IOException e){
            e.printStackTrace();
            Log.e(TAG, "Socket連線: " + e.toString());
        }
    }
}
