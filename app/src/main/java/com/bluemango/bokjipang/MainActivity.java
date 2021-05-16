package com.bluemango.bokjipang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    final FragmentManager fm = getSupportFragmentManager();
    private fragment_community fragment_comu = new fragment_community();
    private fragment_home fragment_home = new fragment_home();
    private fragment_notification fragment_noti = new fragment_notification();
    private fragment_setting fragment_set = new fragment_setting();
    private fragment_support fragment_sup = new fragment_support();
    private fragment_login fragment_login = new fragment_login();
    private long backBtnTime = 0;
    SharedPreferences Shared_auto_login;
    SharedPreferences Shared_user_info;
    SharedPreferences Shared_noti_list;

    Fragment active;
    BottomNavigationView bottomNavigationView;
    Fragment firstFragment = null;
    String u_id;
    String u_pwd;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        Shared_noti_list = getSharedPreferences("noti_list", MODE_PRIVATE);
        Shared_auto_login = getSharedPreferences("login",MODE_PRIVATE); //세션유지에 쓸 sharedPreferences
        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("home_interest", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("sup_zzim_list", MODE_PRIVATE);
//        Shared_noti_list.edit().putString("noti_list", null).apply();
//        JSONObject home_interest2 = new JSONObject();
//        try {
//            home_interest2.put("고령자 게시판",false);
//            home_interest2.put("다문화 게시판",false);
//            home_interest2.put("한부모 게시판",false);
//            home_interest2.put("저소득 게시판",false);
//            home_interest2.put("장애인 게시판",false);
//            home_interest2.put("자유 게시판",false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Shared_user_info.edit().putString("home_interest", String.valueOf(home_interest2)).apply();

//        Intent intent = getIntent();
//        if(intent != null){
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Shared_auto_login.edit().putBoolean("login",false).apply();
//            intent = null;
//        }

        setContentView(R.layout.activity_main);        //세션 없으니 바로 로그인으로 가게 해놓아놨음, 만약 필요하면 여기 바꿔서 각자
        bottomNavigationView = findViewById(R.id.bottom_navigation); //탭바 장착
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener); //탭바 리스너

        u_id = Shared_user_info.getString("user_id","error");
        u_pwd = Shared_user_info.getString("user_pwd","error");
        token = Shared_user_info.getString("token","error");
        Log.d("token : " , token);

        if(!u_id.equals("error")){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable(){
                @Override
                public void run(){
                    JSONObject responseJson;
                    try {
                        /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                        URL url = new URL("https://api.bluemango.site/auth/login/");
                        HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                        myconnection.setRequestMethod("POST");  //post, get 나누기
                        myconnection.setRequestProperty("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                        String str = "{\"phone\":"+"\""+u_id+"\""+" ,\"password\":"+"\""+u_pwd+"\""+"}"; //여기에 post인 경우 body json형식으로 채우기
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
                            try {
                                token = responseJson.getString("token");
                                Shared_user_info.edit().putString("token",token).apply();
                                Log.d("main_activity","api 돌아간다. ");
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


        if(Shared_auto_login.getBoolean("login",false)){
            bottomNavigationView.setVisibility(View.VISIBLE);
            firstFragment = new fragment_home();
            fm.beginTransaction().replace(R.id.fragment_container,fragment_home).commit();
        }
        else{
            bottomNavigationView.setVisibility(View.GONE);
            fm.beginTransaction().replace(R.id.fragment_container,fragment_login).commit();
        }

//        bottomNavigationView.setVisibility(View.GONE);
//
//        /**KAKAO hash key 얻기*/
//        try {
//            PackageInfo pkinfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : pkinfo.signatures) {
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                String result = new String(Base64.encode(messageDigest.digest(), 0));
//                Log.d("해시", result);
//            }
//        } catch (Exception e) {
//        }
//        /**KAKAO hash key 얻기*/


        Intent intent = getIntent();
        if(intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("title");
            if(notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }
    }

    /**뒤로가기시 앱 안꺼지게 하기*/
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    int count;
                    count = getSupportFragmentManager().getBackStackEntryCount();
                    Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    switch (menuItem.getItemId()){
                        case R.id.home: //홈화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++) //기존 스택에 남아있던 화면들 다 삭제
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_home).commit(); //홈화면으로 화면변경
                            active = firstFragment;
                            break;

                        case R.id.community: //펫프렌즈 매칭 화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_comu).commit();
                            active = fragment_comu;
                            break;

                        case R.id.support: //펫 파트너 매칭 화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_sup).commit();
                            active = fragment_sup;
                            break;

                        case R.id.notification: //채팅화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_noti).commit();
                            active = fragment_noti;
                            break;

                        case R.id.setting: //마이페이지화면 선택 됐을때
                            if (count>0){
                                for (int i=0; i<count; i++)
                                    getSupportFragmentManager().popBackStack();
                            }
                            fm.beginTransaction().replace(R.id.fragment_container,fragment_set).commit();
                            active = fragment_set;
                            break;
                    }
                    return true;
                }
            };


}
