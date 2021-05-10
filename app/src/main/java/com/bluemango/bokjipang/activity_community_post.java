package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class activity_community_post extends AppCompatActivity {
    SharedPreferences Shared_user_info;
    String user_token;
    JSONObject responseJson;

    TextView nickname;
    TextView createtime;
    TextView title;
    TextView content;

    JSONObject view_post;
    JSONArray view_reply;
    ArrayList<Datareply> list;
    //    private DataComu dataComu = new DataComu();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_community_post);
        nickname = (TextView) findViewById(R.id.nickname);
        createtime = (TextView) findViewById(R.id.post_time);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);

        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("home_interest", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("sup_zzim_list", MODE_PRIVATE);

        user_token = Shared_user_info.getString("token","");

        Intent intent = getIntent();
        String board_idx = intent.getStringExtra("data");
        list = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.community_reply) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;


        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                nickname.setText("닉네임");
                try {
                    title.setText(view_post.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    content.setText(view_post.getString("content"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    createtime.setText(view_post.getString("createdAt"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for(int i = 0 ; i<view_reply.length() ; i++ ){

                }
                Adapterreply adapter = new Adapterreply(list) ;
                recyclerView.setAdapter(adapter) ;
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();


        String first_request = "?post="+board_idx;
        executor.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/board/post"+first_request);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                    if(myconnection.getResponseCode() == 200){
                        /** 리스폰스 데이터 받는 부분*/
                        Log.d("comu post api 접속 성공","33333");
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        try {
                            view_post = responseJson.getJSONObject("post");
                            view_reply = responseJson.getJSONArray("reply");

                            list = make_reply(view_reply);
                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);
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
    }

    public ArrayList<Datareply> make_reply(JSONArray json_array) throws IOException, JSONException {
        ArrayList<Datareply> tmp = new ArrayList<Datareply>();
        for(int i = 0 ; i< json_array.length(); i++){
            JSONObject tt = json_array.getJSONObject(i);
            Datareply datareply = new Datareply();

            datareply.setNickname(tt.getString("uid"));
            datareply.setDate(tt.getString("createdAt"));
            datareply.setContent(tt.getString("content"));
            datareply.setRereply("0");
            tmp.add(datareply);

            JSONArray rereply_arr = tt.getJSONArray("double_reply");
            for(int j =0; j<rereply_arr.length(); j++){
                JSONObject tt2 = rereply_arr.getJSONObject(i);
                Datareply datareply2 = new Datareply();

                datareply2.setNickname(tt2.getString("uid"));
                datareply2.setDate(tt2.getString("createdAt"));
                datareply2.setContent(tt2.getString("content"));
                datareply2.setRereply("1");
                tmp.add(datareply2);
            }
        }
        return tmp;
    }
}