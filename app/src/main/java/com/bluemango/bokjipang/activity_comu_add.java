package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class activity_comu_add extends AppCompatActivity {


    SharedPreferences Shared_user_info;
    EditText title, content;
    TextView comu_category;
    Button add_comu_btn, back_btn;
    String user_token,user_token2;
    JSONObject responseJson = null;
    Activity activity;
    Context context;
    Intent intent;
    final FragmentManager fm = getSupportFragmentManager();
    fragment_community fragment_community = new fragment_community();

    int category_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comu_add);
        activity = this;

        title = findViewById(R.id.comu_title);
        content = findViewById(R.id.comu_content);
        add_comu_btn = findViewById(R.id.add_comu_btn);
        comu_category = findViewById(R.id.write_category);
        back_btn = findViewById(R.id.comu_back_btn);

        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("home_interest", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("sup_zzim_list", MODE_PRIVATE);
        user_token = Shared_user_info.getString("token","");

        List<String> category_list = Arrays.asList("장애인", "저소득", "다문화", "고령자", "한부모", "자유");


        category_num = intent.getIntExtra("category",0);
        comu_category.setText(category_list.get(category_num).concat(" 게시판"));
        Log.d("comu_Add totken : ",user_token);

        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
//                ((MainActivity)MainActivity.context).
//                fm.beginTransaction().replace(R.id.fragment_container,fragment_community).commit();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("goto","true");
                startActivity(intent);
//                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container3);
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                fragmentTransaction.commit();
            }
        };

        String category = category_list.get(category_num);                          //해당 카테고리에 따라 글 작성하도록 변경
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_comu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.site/board/post/add/");
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
                                if(responseJson.getBoolean("success")) {
                                    //finish();
                                    Message msg = handler.obtainMessage();
                                    handler.sendMessage(msg);
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
        });

    }
}