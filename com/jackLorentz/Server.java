package com.jackLorentz;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private int port;
    private ExecutorService threadPool;

    public Server(int port, int num){
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(num);
    }

    public void run(){
        try(ServerSocket s = new ServerSocket(port)){
            System.out.println("Server is listening ...");
            Socket incoming = s.accept();
            System.out.println("Server is connected !");
            InputStream inputStream = incoming.getInputStream();
            Scanner in = new Scanner(inputStream, "UTF-8");
            String req = "";
            while(in.hasNextLine()){
                req = in.nextLine();
            }
            Runnable r = new ThreadHandler(incoming, req);
            threadPool.execute(r);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
