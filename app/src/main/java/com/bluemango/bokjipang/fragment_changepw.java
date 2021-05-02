package com.bluemango.bokjipang;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class fragment_changepw extends Fragment {
    EditText new_password;
    EditText new_password_confirm;
    TextView back_btn;
    ImageView set_image;
    String user_token;
    boolean password_format_check;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_changepw, container, false);
        activity_mypage activity_mypage = (activity_mypage) getActivity();
        user_token = activity_mypage.Shared_user_info.getString("token", null);

        /** 비밀번호 변경 페이지 뒤로가기 */
        back_btn = view.findViewById(R.id.mypage_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_mypage fragment_mypage = new fragment_mypage();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_mypage);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        /** 비밀번호 입력형식 완료여부 **/
        /** 1. 영문(대소문자 구분), 숫자, 특수문자 조합 **/
        /** 2. 9~12자리 사이 문자 **/
        /** 3. 공백문자 사용 불가 **/
        new_password = view.findViewById(R.id.new_pw_txt);
        new_password.addTextChangedListener(new TextWatcher() {
            String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z]).{9,12}$";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = Pattern.compile(pwPattern).matcher(new_password.getText().toString());
                if (!matcher.matches()) {
                    new_password.setError("비밀번호는 9~12자리 사이의 영문,숫자,특수문자 조합이여야 합니다.");
                    password_format_check = false;
                }
                if (new_password.getText().toString().contains(" ")) {
                    new_password.setError("비밀번호는 공백을 포함하지 않습니다.");
                    password_format_check = false;
                }
                if (matcher.matches() && !new_password.getText().toString().contains(" ")) {
                    Drawable icon = getResources().getDrawable(R.drawable.equal);
                    icon.setBounds(0, 0, 80, 80);
                    new_password.setError("사용가능한 비밀번호 입니다.", icon);
                    password_format_check = true;
                }
            }
        });
        /** 비밀번호 확인 일치 여부 */
        new_password_confirm = view.findViewById(R.id.new_pw_confirm_txt);
        set_image = view.findViewById(R.id.setImage);
        new_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Drawable equal_icon = getResources().getDrawable(R.drawable.equal);
                equal_icon.setBounds(0, 0, 80, 80);
                if (new_password.getText().toString().equals(new_password_confirm.getText().toString())) {
                    new_password_confirm.setError("비밀번호가 일치합니다.", equal_icon);
                } else {
                    new_password_confirm.setError("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                AlertDialog.Builder auth_alert = new AlertDialog.Builder(getActivity());
                auth_alert.setTitle("비밀번호 변경");
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

        EditText new_password = view.findViewById(R.id.new_pw_confirm_txt);

        /**비밀번호 변경 버튼*/
        Button pw_change = (Button) view.findViewById(R.id.pw_change);
        pw_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password_format_check) {
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String pd = new_password.getText().toString();

                                /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                                URL url = new URL("https://api.bluemango.site/auth/password/");
                                HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                                myconnection.setRequestMethod("POST");  //post, get 나누기
                                myconnection.setRequestProperty("Content-Type", "application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우
                                myconnection.setRequestProperty("x-access-token", user_token);

                                String str = "{\"password\":" + "\"" + pd + "\"}";
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
                    auth_alert.setTitle("비밀번호 변경");
                    auth_alert.setMessage("실패하셨습니다.. 양식에 맞지 않습니다");
                    auth_alert.setPositiveButton("예", null);
                    auth_alert.create().show();
                }
            }
        });

        return view;
    }
}