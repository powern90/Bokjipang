package com.bluemango.bokjipang;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    Button btn_login;
    private fragment_home fragment_home = new fragment_home();
    private fragment_signup fragment_signup = new fragment_signup();
    String token=null;
//    Button kakaoLogin,kakaoLogout;
//    LoginButton loginButton;
//    private KaKaoCallBack kaKaoCallBack;

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_login, container, false);

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
        /**로그인 버튼 api*/
        id_text = (EditText)view.findViewById(R.id.id_text);
        pw_text = (EditText)view.findViewById(R.id.pw_text);
        btn_login = (Button)view.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String id = id_text.getText().toString();
                String pw = pw_text.getText().toString();
                AsyncTask.execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.me/auth/login/");
                            HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                            myconnection.setRequestMethod("POST");  //post, get 나누기
                            myconnection.setDoOutput(true); // 쓰기모드 지정
                            myconnection.setDoInput(true); // 읽기모드 지정
                            myconnection.setRequestProperty("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                            myconnection.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
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
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment_home);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }else{
                                Log.d("api 연결","error 200아님");
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

