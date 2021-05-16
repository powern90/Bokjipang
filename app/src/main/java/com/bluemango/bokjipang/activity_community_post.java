package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class activity_community_post extends AppCompatActivity {
    SharedPreferences Shared_user_info;
    String user_token;
    String user_name;
    JSONObject responseJson;

    TextView nickname;
    TextView createtime;
    TextView title;
    TextView content;
    EditText add_reply;
    ImageView send_reply, heart;
    JSONObject view_post;
    JSONArray view_reply;
    ArrayList<Datareply> list;
    Adapterreply adapter;
    Context context = ApplicationClass.getContext();
    Activity activity;
    //    private DataComu dataComu = new DataComu();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_community_post);
        activity = this;
        nickname = (TextView) findViewById(R.id.nickname);
        createtime = (TextView) findViewById(R.id.post_time);
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        add_reply = (EditText) findViewById(R.id.enroll_reply);
        send_reply = (ImageView) findViewById(R.id.send);
        heart = (ImageView)findViewById(R.id.heart_post);

        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("home_interest", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("sup_zzim_list", MODE_PRIVATE);

        user_token = Shared_user_info.getString("token","");
        user_name = Shared_user_info.getString("name",null);
        Intent intent = getIntent();
        String board_idx = intent.getStringExtra("data");
        list = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.community_reply);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                nickname.setText(Shared_user_info.getString("name", null));

                try {
                    content.setText(view_post.getString("content"));
                    title.setText(view_post.getString("title"));
                    if(view_post.getString("my_like").equals("1")){
                        heart.setImageResource(R.drawable.full_heart);
                    }
                    else{
                        heart.setImageResource(R.drawable.normal_heart);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    old_format.setTimeZone(TimeZone.getTimeZone("KST"));
                    SimpleDateFormat new_format = new SimpleDateFormat("MM/dd HH:mm");
                    Date old_date = null;
                    old_date = old_format.parse(view_post.getString("createdAt"));
                    String new_date =  new_format.format(old_date);
                    createtime.setText(new_date);
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                for(int i = 0 ; i<view_reply.length() ; i++ ){

                }
                adapter = new Adapterreply(activity, user_name, list, user_token, add_reply);
                recyclerView.setAdapter(adapter);
            }
        };
        @SuppressLint("HandlerLeak") final Handler handler2 = new Handler() {
            public void handleMessage(Message msg) {
                startActivity(intent);
                finish();
            }
        };
        @SuppressLint("HandlerLeak") final Handler handler3 = new Handler() {
            public void handleMessage(Message msg) {
                heart.setImageResource(R.drawable.normal_heart);
            }
        };
        @SuppressLint("HandlerLeak") final Handler handler4 = new Handler() {
            public void handleMessage(Message msg) {
                heart.setImageResource(R.drawable.full_heart);
            }
        };


        ExecutorService executor = Executors.newSingleThreadExecutor();
        String first_request = "?post="+board_idx;
        String like_request = "?id="+board_idx;

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(view_post.getString("my_like").equals("1")){
                        AlertDialog.Builder confirm_reply = new AlertDialog.Builder(activity);
                        confirm_reply.setMessage("게시글 좋아요를 취소하시겠습니까?");
                        confirm_reply.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    delete_heart(handler3,like_request);
                            }
                        });
                        confirm_reply.setNegativeButton("취소", null);
                        confirm_reply.create().show();
                    }
                    else{
                        AlertDialog.Builder confirm_reply = new AlertDialog.Builder(activity);
                        confirm_reply.setMessage("게시글 좋아요를 하시겠습니까?");
                        confirm_reply.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                make_heart(handler4,like_request);
                            }
                        });
                        confirm_reply.setNegativeButton("취소", null);
                        confirm_reply.create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        send_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirm_reply = new AlertDialog.Builder(activity);
                confirm_reply.setMessage("댓글을 작성하시겠습니까?");
                confirm_reply.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        make_reply(board_idx, handler2);
                        add_reply.setHint("댓글을 입력하세요");
                    }
                });
                confirm_reply.setNegativeButton("취소", null);
                confirm_reply.create().show();
            }
        });


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
            datareply.setIdx(tt.getInt("id"));
            datareply.setNickname(tt.getString("uid"));
            datareply.setDate(tt.getString("createdAt"));
            datareply.setContent(tt.getString("content"));
            datareply.setRereply("0");
            tmp.add(datareply);

            JSONArray rereply_arr = tt.getJSONArray("double_reply");
            for(int j =0; j<rereply_arr.length(); j++){
                JSONObject tt2 = rereply_arr.getJSONObject(j);
                Datareply datareply2 = new Datareply();
                datareply2.setIdx(tt2.getInt("id"));
                datareply2.setNickname(tt2.getString("uid"));
                datareply2.setDate(tt2.getString("createdAt"));
                datareply2.setContent(tt2.getString("content"));
                datareply2.setRereply("1");
                tmp.add(datareply2);
            }
        }
        return tmp;
    }


    void make_reply(String board_idx, Handler handler2){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.bluemango.site/board/reply/add/");
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("POST");  //post, get 나누기
                    myconnection.setRequestProperty("Content-Type", "application/json");
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    String str = "{\"m_id\":" +  adapter.getr_num()  + " ,\"post_id\":"  + Integer.parseInt(board_idx)  + " ,\"content\":" + "\"" + add_reply.getText().toString().replaceAll("\n","\\\\n") + "\"" + "}";
                    byte[] outputInBytes = str.getBytes(StandardCharsets.UTF_8);
                    OutputStream os = myconnection.getOutputStream();
                    os.write(outputInBytes);
                    os.close();
                    if (myconnection.getResponseCode() == 200) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(myconnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        responseJson = new JSONObject(sb.toString());
                        if (responseJson.getBoolean("success")) {
                            Message msg = handler2.obtainMessage();
                            handler2.sendMessage(msg);
                        } else {
                            //댓글달기 실패했습니다.
                        }
                    }
                    else {
                        Log.d("api 연결","error : " + Integer.toString(myconnection.getResponseCode()));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    void make_heart(Handler handler4, String like_request){
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        AsyncTask.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/board/post/like/add"+like_request);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setReadTimeout(2000);
                    Log.d("siballalala", String.valueOf(myconnection.getResponseCode()));
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
                            if(responseJson.getBoolean("success")) {
                                Message msg = handler4.obtainMessage();
                                handler4.sendMessage(msg);
                            }
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
    void delete_heart(Handler handler3, String like_request){
        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        AsyncTask.execute(new Runnable(){
            @Override
            public void run(){
                try {
                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                    URL url = new URL("https://api.bluemango.site/board/post/like/delete"+like_request);
                    HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                    myconnection.setRequestMethod("GET");  //post, get 나누기
                    myconnection.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setRequestProperty("x-access-token", user_token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                    myconnection.setReadTimeout(2000);
                    Log.d("siballalala", String.valueOf(myconnection.getResponseCode()));
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
                            if(responseJson.getBoolean("success")) {
                                Message msg = handler3.obtainMessage();
                                handler3.sendMessage(msg);
                            }
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
}