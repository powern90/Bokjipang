package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class fragment_changeui extends Fragment {
    Webview_address Webview_address;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changeui, container, false);
        activity_mypage activity_mypage = (activity_mypage) getActivity();
        String name = activity_mypage.Shared_user_info.getString("name", null);
        String phone = activity_mypage.Shared_user_info.getString("phone", null);
        String user_token = activity_mypage.Shared_user_info.getString("token", null);

        EditText new_name_txt = (EditText)view.findViewById(R.id.new_name_txt);
        EditText current_phone_txt = (EditText)view.findViewById(R.id.current_phone_txt);
        new_name_txt.setText(name);
        current_phone_txt.setText(phone);


        Webview_address = new Webview_address();

        Button btn_search = (Button)view.findViewById(R.id.WebView_btn);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** webview address로 데이터 다 가져가기*/
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, Webview_address);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                AlertDialog.Builder auth_alert = new AlertDialog.Builder(getActivity());
                auth_alert.setTitle("개인정보 변경");
                auth_alert.setMessage("성공");
                auth_alert.setPositiveButton("예", null);
                auth_alert.create().show();
                fragment_mypage fragment_mypage = new fragment_mypage();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_mypage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };


        /**버튼 클릭시 주소 변경*/
        EditText new_address_txt = (EditText)view.findViewById(R.id.new_address_txt);

        Button btn_changeui = (Button)view.findViewById(R.id.btn_changeui);
        btn_changeui.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String ad = new_address_txt.getText().toString();
                if (!ad.equals("")) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                                URL url = new URL("https://api.bluemango.me/user/update/");
                                HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                                myconnection.setRequestMethod("POST");  //post, get 나누기
                                myconnection.setRequestProperty("Content-Type", "application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                                myconnection.setRequestProperty("x-access-token", user_token);

                                String str = "{\"address\":" + "\"" + ad + "\"}";
                                byte[] outputInBytes = str.getBytes(StandardCharsets.UTF_8);    //post 인 경우 body 채우는 곳
                                OutputStream os = myconnection.getOutputStream();
                                os.write(outputInBytes);
                                os.close();
                                if (myconnection.getResponseCode() == 200) {
                                    /** 리스폰스 데이터 받는 부분*/
                                    InputStream responseBody = myconnection.getInputStream();
                                    InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                                    JsonReader jsonReader = new JsonReader(responseBodyReader);
                                    jsonReader.beginObject();
                                    while (jsonReader.hasNext()) {
                                        String key = jsonReader.nextName();
                                        if (key.equals("success")) {
                                            boolean success = jsonReader.nextBoolean();
                                            if (success) {
                                                Message msg = handler.obtainMessage();
                                                handler.sendMessage(msg);
                                                break;
                                            }
                                        } else {
                                            jsonReader.skipValue();
                                        }
                                    }
                                    jsonReader.close();
                                    myconnection.disconnect();

                                } else {
                                    Log.d("api 연결", "error 200아님");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("api 연결", "tru catch 에러뜸");
                            }
                        }
                    });
                }
                else{
                    AlertDialog.Builder auth_alert = new AlertDialog.Builder(getActivity());
                    auth_alert.setTitle("변경할 주소가 입력되지 않았습니다");
                    auth_alert.setMessage("다시 입력하겠습니까?");
                    auth_alert.setPositiveButton("예", null);
                    auth_alert.create().show();
                }
            }
        });
        return view;
    }
}