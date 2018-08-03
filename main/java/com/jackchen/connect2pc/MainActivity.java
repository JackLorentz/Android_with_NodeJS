package com.jackchen.connect2pc;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.gms.fit.samples.common.logger.Log;
import com.google.android.gms.fit.samples.common.logger.LogView;
import com.google.android.gms.fit.samples.common.logger.LogWrapper;
import com.google.android.gms.fit.samples.common.logger.MessageOnlyLogFilter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";
    //HttpClient擴展困難, android建議使用HttpURLConnection
    private BufferedReader br;
    private DataOutputStream out;
    private HttpURLConnection conn;
    private StringBuilder response;
    //
    private EditText Address, Message;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initializeLogging();
    }

    private void findViews(){
        Address = (EditText)findViewById(R.id.editText2);
        Message = (EditText)findViewById(R.id.editText3);
        connect = (Button)findViewById(R.id.button2);
        connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "[Android Client] : Start to connect");
                (new Thread(connection)).start();
            }
        });
    }
    //規定連線類不能放在主執行緒上
    private Runnable connection = new Runnable() {
        @Override
        public void run() {
            try{
                String message = Message.getText().toString();
                String address = Address.getText().toString();
                //設定傳送參數
                HashMap<String, String> params = new HashMap<>();
                params.put("message", "[Android Client] : " + message);
                //socket換成conn的概念
                URL url = new URL("https://73317b93.ngrok.io");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestMethod("POST");
                //超過一定時間就要回覆
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                conn.setDoInput(true);//允許下載
                conn.setDoOutput(true);//允許上傳
                conn.setUseCaches(false);//設定是否用緩存
                //發送訊息
                out = new DataOutputStream(conn.getOutputStream());
                String jsonString = getJSONString(params);
                out.writeBytes(jsonString);
                out.flush();
                out.close();
                //讀取訊息
                if(conn.getResponseCode() == 200){
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while((line = br.readLine())!=null){
                        Log.i(TAG, line);
                        //response.append(line);
                        //response.append('\r');
                    }
                    br.close();
                }
                else{
                    Log.i(TAG, "[Android Client] : Request is failed .");
                }
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "[Android Client] : " + e.toString());
            }
            finally {
                if (br != null){
                    try {
                        br.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "[Android Client] : " + e.toString());
                    }
                }
                if (conn != null){
                    conn.disconnect();
                }
            }
        }
    };

    private void initializeLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);
        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);
        // On screen logging via a customized TextView.
        LogView logView = (LogView) findViewById(R.id.sample_logview);

        // Fixing this lint errors adds logic without benefit.
        //noinspection AndroidLintDeprecation
        logView.setTextAppearance(this, R.style.Log);

        logView.setBackgroundColor(Color.WHITE);
        msgFilter.setNext(logView);
        Log.i(TAG, "Ready");
    }

    public static String getJSONString(Map<String, String> params){
        JSONObject json = new JSONObject();
        for(String key:params.keySet()) {
            try {
                json.put(key, params.get(key));
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "[Android Client] : " + e.toString());
            }
        }
        return json.toString();
    }
}
