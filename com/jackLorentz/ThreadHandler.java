package com.jackLorentz;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.stream.Stream;

public class ThreadHandler implements Runnable{
    private Socket incoming;
    private InputStream inputStream;
    private BufferedWriter out;
    private String message;

    public ThreadHandler(Socket incoming, String message){
        this.incoming = incoming;
        this.message = message;
    }

    public void run(){
        try{
            System.out.println(message);
            String res = "Service is finished.";
            out = new BufferedWriter(new OutputStreamWriter(incoming.getOutputStream()));
            out.write(res);
            incoming.shutdownOutput();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                if(inputStream != null) inputStream.close();
                if(out != null) out.close();
                if(incoming != null) incoming.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
