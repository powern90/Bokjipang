package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class activity_comu_add extends AppCompatActivity {

    EditText title, content;
    Button add_comu_btn;
    String user_token;
    JSONObject responseJson = null;
    Activity activity;
    Context context;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comu_add);

        title = findViewById(R.id.comu_title);
        content = findViewById(R.id.comu_content);
        add_comu_btn = findViewById(R.id.add_comu_btn);

        user_token = intent.getStringExtra("token");
        Log.d("comu_Add totken : ",user_token);

        String category = "쟝애인";
        add_comu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.me/board/post/add/");
                            HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                            myconnection.setRequestMethod("POST");  //post, get 나누기
                            myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                            myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                            String str = "{\"title\":"+"\""+title.getText().toString()+"\""+" , \"content\":"+"\""+content.getText().toString()+"\""+", \"category\":"+"\""+category+"\""+"}"; //여기에 post인 경우 body json형식으로 채우기
                            byte[] outputInBytes = str.getBytes(StandardCharsets.UTF_8);    //post 인 경우 body 채우는 곳
                            OutputStream os = myconnection.getOutputStream();
                            os.write( outputInBytes );
                            os.close();
                            if(myconnection.getResponseCode() == 200){
                                /** 리스폰스 데이터 받는 부분*/
                                BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line = "";
                                while((line = br.readLine())!=null){
                                    sb.append(line);
                                }
                                responseJson = new JSONObject(sb.toString());
                                Log.d("커뮤니티 글쓰기 성공?",responseJson.getString("success"));
                            }else{
                                Log.d("api 연결","error : " + Integer.toString(myconnection.getResponseCode()));
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            Log.d("api 연결","tru catch 에러뜸");
                        }
                    }
                });
            }
        });

    }
}