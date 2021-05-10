package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class activity_support_post extends AppCompatActivity {
    SharedPreferences Shared_user_info;
    String user_token;
    Activity activity;
    JSONObject responseJson;
    TextView createtime;
    TextView title;
    TextView url;
    TextView content;
    String sup_idx;
    String j_title,j_content,j_url,j_createdAt,j_updatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_post);
        activity = this;

        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("home_interest", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("sup_zzim_list", MODE_PRIVATE);
        user_token = Shared_user_info.getString("token","");

        createtime = (TextView) findViewById(R.id.post_time);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        url = (TextView) findViewById(R.id.str_url);

        Intent intent = getIntent();
        sup_idx=intent.getStringExtra("data");
//        DataSup dataSup = (DataSup) intent.getSerializableExtra("data");
//        String a = dataSup.getContent();
//        content.setText(fromHtml(dataSup.getContent()));
//        title.setText(dataSup.getTitle());
//        createtime.setText(dataSup.getDate());
//        url.setText(dataSup.getUrl());
//        sup_idx = dataSup.getIdx();


        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                AlertDialog.Builder success_alert = new AlertDialog.Builder(activity);
                try {
                    success_alert.setTitle("add : "+responseJson.getString("success"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                success_alert.create().show();
            }
        };

        @SuppressLint("HandlerLeak") final Handler handler2 = new Handler()
        {
            public void handleMessage(Message msg){
                content.setText(fromHtml(j_content.replace("\n","<br>").replace("\t","")));
                title.setText(j_title);
                createtime.setText(j_createdAt);
                url.setText(j_url);
            }
        };

        String first_request = "?index="+sup_idx;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support/post"+first_request);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        try {
                            JSONObject a = new JSONObject();
                            a =responseJson.getJSONObject("post");
                            j_title = a.getString("title");
                            j_content = a.getString("content");
                            j_url = a.getString("url");
                            j_createdAt = a.getString("createdAt");
                            j_updatedAt = a.getString("updatedAt");

                            Message msg = handler2.obtainMessage();
                            handler2.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.d("api 연결","error : " + Integer.toString(myconnection.getResponseCode()));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.d("api 연결","tru catch 에러뜸");
                }
            }
        });



        FloatingActionButton plus_btn = findViewById(R.id.plus_btn);
        FloatingActionButton sub_btn = findViewById(R.id.sub_btn);
        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sup_add_zzim(handler);
            }
        });


        sub_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sup_sub_zzim(handler);
            }
        });

    }

    public static Spanned fromHtml(String source) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(source);
        }
        return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);

    }


    public void sup_add_zzim(Handler handler){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String idx = "?id="+ sup_idx;
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support/zzim/add"+idx);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
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



    public void sup_sub_zzim(Handler handler){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        String idx = "?id="+ sup_idx;
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/support/zzim/delete"+idx);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
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
}
