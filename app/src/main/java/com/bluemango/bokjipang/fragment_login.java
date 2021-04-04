package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//import com.kakao.auth.Session;
//import com.kakao.usermgmt.LoginButton;
//import com.kakao.usermgmt.UserManagement;
//import com.kakao.usermgmt.callback.LogoutResponseCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.zip.Inflater;

import javax.net.ssl.HttpsURLConnection;

public class fragment_login extends Fragment {
    private EditText id_text, pw_text;
    private TextView text;
    private Activity activity2;
    Button btn_login;
    private fragment_home fragment_home = new fragment_home();
    private fragment_signup fragment_signup = new fragment_signup();
    String token=null;
//    Button kakaoLogin,kakaoLogout;
//    LoginButton loginButton;
//    private KaKaoCallBack kaKaoCallBack;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity2 = (Activity) context;
    }
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_login, container, false);
        MainActivity activity = (MainActivity) getActivity();
        AlertDialog.Builder login_alert = new AlertDialog.Builder(activity2);
        /**백그라운드에서 ui 변경하고 싶어서 handler 호출 하기 위해....*/
        @SuppressLint("HandlerLeak") final Handler handler = new Handler()
        {
            public void handleMessage(Message msg){
                activity.Shared_user_info.edit().clear();
                activity.Shared_user_info.edit().putString("token",token).apply();
                activity.Shared_auto_login.edit().clear();
                activity.Shared_auto_login.edit().putBoolean("login",true).apply();

                activity.fm.beginTransaction().remove(fragment_login.this).commit();
                activity.bottomNavigationView.setSelectedItemId(R.id.bottom_navigation);
                activity.fm.beginTransaction().replace(R.id.fragment_container,fragment_home,"3");
                activity.recreate();

            }
        };
        @SuppressLint("HandlerLeak") final Handler handler2 = new Handler()
        {
            public void handleMessage(Message msg){
                Log.d("login_check","not_exist");
                AlertDialog login_check = login_alert.create();
                login_check.setTitle("Bokjipang 로그인 서비스");
                login_check.setMessage("존재하지 않는 계정입니다.");
//                login_check.setPositiveButton("예", null);
                login_check.show();
                login_check.setCancelable(false);
                login_check.dismiss();
            }
        };

        TextView signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {          //회원가입 페이질 이동
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_signup);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        text = view.findViewById(R.id.text2);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity activity = (MainActivity) getActivity();
//                activity.auto_login.edit().putBoolean("login",true).apply();
//                Fragment fragment_home = new fragment_home();
//                activity.firstFragment = fragment_home;
//                activity.bottomNavigationView.setSelectedItemId(R.id.bottom_navigation);
//                activity.fm.beginTransaction().replace(R.id.fragment_container,fragment_home,"3").commit();
//                activity.active = fragment_home;
//                activity.recreate();
            }
        });
        JSONObject user_info = new JSONObject();
        JSONObject user_interest = new JSONObject();

        /**로그인 버튼 api*/
        id_text = (EditText)view.findViewById(R.id.id_text);
        pw_text = (EditText)view.findViewById(R.id.pw_text);
        btn_login = (Button)view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String id = id_text.getText().toString();
                String pw = pw_text.getText().toString();
                if(id.equals("")){
                    login_alert.setTitle("Bokjipang 로그인 서비스");
                    login_alert.setMessage("아이디를 입력해주세요.");
                    login_alert.setPositiveButton("예", null);
                    login_alert.create().show();
                }else if(pw.equals("")){
                    login_alert.setTitle("Bokjipang 로그인 서비스");
                    login_alert.setMessage("비밀번호를 입력해주세요.");
                    login_alert.setPositiveButton("예", null);
                    login_alert.create().show();
                }
                AsyncTask.execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.me/auth/login/");
                            HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                            myconnection.setRequestMethod("POST");  //post, get 나누기
                            myconnection.setRequestProperty("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                            String str = "{\"phone\":"+"\""+id+"\""+" ,\"password\":"+"\""+pw+"\""+"}"; //여기에 post인 경우 body json형식으로 채우기
                            byte[] outputInBytes = str.getBytes(StandardCharsets.UTF_8);    //post 인 경우 body 채우는 곳
                            OutputStream os = myconnection.getOutputStream();
                            os.write( outputInBytes );
                            os.close();
                            if(myconnection.getResponseCode() == 200){
                                /** 리스폰스 데이터 받는 부분*/
                                InputStream responseBody = myconnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while(jsonReader.hasNext()){
                                    String key = jsonReader.nextName();
                                    if(key.equals("token")){
                                        token = jsonReader.nextString();
                                        Log.d("token",token);
                                        break;
                                    }
                                    else{
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                myconnection.disconnect();

                                /** 로그인 성공하면 check api 이용해서 유저 정보 받아오는 부분 start */
                                try {
                                    /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                                    URL url2 = new URL("https://api.bluemango.me/auth/check/");
                                    HttpsURLConnection myconnection2 = (HttpsURLConnection) url2.openConnection();
                                    myconnection2.setRequestMethod("GET");  //post, get 나누기
                                    myconnection2.setRequestProperty ("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                                    myconnection2.setRequestProperty("x-access-token", token); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                                    Log.d("auth/check", String.valueOf(myconnection2.getResponseCode()));
                                    if(myconnection2.getResponseCode() == 200){
                                        /** 리스폰스 데이터 받는 부분*/
                                        InputStream responseBody2 = myconnection2.getInputStream();
                                        InputStreamReader responseBodyReader2 = new InputStreamReader(responseBody2, StandardCharsets.UTF_8);
                                        JsonReader jsonReader2 = new JsonReader(responseBodyReader2);
                                        jsonReader2.beginObject();
                                        while(jsonReader2.hasNext()){
                                            String key = jsonReader2.nextName();
                                            if(key.equals("info")){
                                                jsonReader2.beginObject();
                                                while(jsonReader2.hasNext()){
                                                    String temp = jsonReader2.nextName();
                                                    if(temp.equals("phone")){
                                                        user_info.put("phone", jsonReader2.nextString());
                                                    }else if(temp.equals("name")){
                                                        user_info.put("name", jsonReader2.nextString());
                                                    }else if(temp.equals("interest")){
                                                        jsonReader2.beginObject();
                                                        while(jsonReader2.hasNext()){
                                                            String tp = jsonReader2.nextName();
                                                            if(tp.equals("고령자")){
                                                                user_interest.put("고령자",jsonReader2.nextBoolean());
                                                            }else if(tp.equals("장애인")){
                                                                user_interest.put("장애인",jsonReader2.nextBoolean());
                                                            }else if(tp.equals("저소득")){
                                                                user_interest.put("저소득",jsonReader2.nextBoolean());
                                                            }else if(tp.equals("다문화")){
                                                                user_interest.put("다문화",jsonReader2.nextBoolean());
                                                            }else if(tp.equals("한부모")){
                                                                user_interest.put("한부모",jsonReader2.nextBoolean());
                                                            }else{
                                                                jsonReader2.skipValue();
                                                            }
                                                        }
                                                        jsonReader2.endObject();
                                                    }else{
                                                        jsonReader2.skipValue();
                                                        Message msg = handler2.obtainMessage();
                                                        handler2.sendMessage(msg);
                                                    }
                                                }
                                                jsonReader2.endObject();
                                            }
                                            else{
                                                jsonReader2.skipValue();
                                            }
                                        }
                                        user_info.put("interest",user_interest);
                                        jsonReader2.close();
                                        myconnection2.disconnect();

                                    }else{
                                        Log.d("api 연결","error 200아님");
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d("api 연결","tru catch 에러뜸");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                /** 로그인 성공하면 check api 이용해서 유저 정보 받아오는 부분 end */



                                int a = 1;
                                /** 토큰 저장 및 자동로그인 shared 저장 위치 start */
                                Message msg = handler.obtainMessage();
                                handler.sendMessage(msg);
                                /** 토큰 저장 및 자동로그인 shared 저장 위치 end */

                                String tmp = user_info.toString();
                                activity.Shared_user_info.edit().putString("user_info", tmp).apply();


                            }else{
                                Log.d("api 연결","error 200아님");
                                //로그인 실패 토스트 띄우기


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("api 연결","tru catch 에러뜸");
                        }
                    }
                });
            }
        });

//        /**KAKAO 로그인 */
//        loginButton = view.findViewById(R.id.loginButton);
//        kakaoLogin = view.findViewById(R.id.kakaoLogin);
//        kakaoLogout = view.findViewById(R.id.kakaoLogout);
//
//        kaKaoCallBack = new KaKaoCallBack();
//        Session.getCurrentSession().addCallback(kaKaoCallBack);
//        Session.getCurrentSession().checkAndImplicitOpen();
//
//        kakaoLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginButton.performClick();
//                MainActivity activity = (MainActivity) getActivity();
//                activity.fm.beginTransaction().replace(R.id.fragment_container,fragment_signup).commit();
//            }
//        });
//
//        kakaoLogout.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
//                    @Override
//                    public void onCompleteLogout() {
//                        Log.d("logout","되었음");
//                    }
//                });
//            }
//        });
//        /**KAKAO 로그인 */
        return view;
    }

//    /**KAKAO 로그인 함수 */
//
//    public void kakaoError(String msg){
//        Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Session.getCurrentSession().removeCallback(kaKaoCallBack);
//    }
//    /**KAKAO 로그인 함수 */
}