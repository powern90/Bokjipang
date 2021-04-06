package com.bluemango.bokjipang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.Drawable;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


public class fragment_signup extends Fragment {

    EditText first_password, second_password, input_phone, name, age, txt_address;
    RadioGroup radio_group;
    TextView back_login;
    RadioButton radio_man, radio_woman;
    Webview_address Webview_address;
    String auth_checked="false";
    boolean password_format_check = false;
    boolean password_equal_check = false;
    int gender;
    Button btn_search;
    Button auth_button;
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
    JSONObject js;
    ImageView set_image;
    LinearLayout verify_layout;
    String res = null;
    private String mVerificationID;
    private EditText confirm_code;
    private Activity activity;
    private static final String TAG = "SIGN_UP";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity)
            activity = (Activity) context;
    }

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        Webview_address = new Webview_address();
        View view = layoutInflater.inflate(R.layout.fragment_signup, container, false);
        js = new JSONObject();
        init_varaibles(view);       //변수 초기화
        /**성별 라디오버튼 체크*/
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_man) {
                    gender = 1;
                } else {
                    gender = 2;
                }
            }
        });

        /** webview_address에서 addres 정보 받아와서 출력하기 및 js 저장*/
        Bundle bundle = getArguments();
        res = bundle_receive(bundle);

        /**회원가입 버튼 api*/
        Button btn_signup = (Button)view.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            /** 가입정보 충분/불충분 여부 **/
            public boolean check_signup(String phone, String password, String name, String gender, String age, String address){
                if(phone.equals("") || !auth_checked.equals("true")){
                    Log.d("phone","전화번호를 확인해주세요");  // <- 이 부분들 알림메세지 창 띄워주기 해야함
                    return false;
                }else if(password.equals("") || !password_format_check || !password_equal_check){
                    Log.d("password","비밀번호를 확인해주세요");
                    return false;
                }else if(name.equals("")){
                    Log.d("name","이름을 확인해주세요");
                    return false;
                }else if(gender.equals("0")){
                    Log.d("gender","성별을 확인해주세요");
                    return false;
                }else if(age.equals("")){
                    Log.d("age","나이를 확인해주세요");
                    return false;
                }else if(address.equals("")){
                    Log.d("address","주소를 확인해주세요");
                    return false;
                }else if(!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() && !checkBox5.isChecked()){
                    Log.d("interest","관심분야를 확인해주세요");
                    return false;
                }
                return true;
            }
            @Override
            public void onClick(View view) {
                String user_phone = input_phone.getText().toString();
                String user_password = second_password.getText().toString();
                String user_name = name.getText().toString();
                String user_gender = String.valueOf(gender);
                String user_age = age.getText().toString();
                String user_address = txt_address.getText().toString();
                if(!check_signup(user_phone,user_password,user_name,user_gender,user_age,user_address)){
                    return;
                }
                // 입력값 받아 json 포맷 생성
                res = "{\"phone\" :\"" +
                        user_phone +"\","+
                        "\"password\" :\"" +
                        user_password +"\","+
                        "\"name\" : \"" +
                        user_name +"\","+
                        "\"gender\":" +
                        user_gender +","+
                        "\"age\":" +
                        user_age +","+
                        "\"address\":\"" +
                        user_address +"\",";
                String str = checkbox_ischecked(res);
                AsyncTask.execute(new Runnable(){
                    @Override
                    public void run(){
                        try {
                            /**url에 http 로 하는 경우는 HttpURLConnection 으로 해야하고, url에 https인 경우는 HttpsURLConnection 으로 만들어야함*/
                            URL url = new URL("https://api.bluemango.me/auth/enroll/");
                            HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                            myconnection.setRequestMethod("POST");  //post, get 나누기
                            myconnection.setRequestProperty("Content-Type","application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

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
                                    if(key.equals("success")){
                                        boolean success = jsonReader.nextBoolean();
                                        Log.d("token",Boolean.toString(success));
                                        break;
                                    }
                                    else{
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                myconnection.disconnect();
                                fragment_login fragment_login = new fragment_login();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, fragment_login);
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

        /**버튼 클릭시 도로명 주소 api 인 webview_address로 이동*/
        goto_address_api();

        /** 회원가입 뒤로가기 **/
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_login login = new fragment_login();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, login);
                transaction.commit();
            }
        });
        /** 비밀번호 입력형식 완료여부 **/
        /** 1. 영문(대소문자 구분), 숫자, 특수문자 조합 **/
        /** 2. 9~12자리 사이 문자 **/
        /** 3. 공백문자 사용 불가 **/
        first_password.addTextChangedListener(new TextWatcher() {
            String pwPattern = "^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z]).{9,12}$";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Matcher matcher = Pattern.compile(pwPattern).matcher(first_password.getText().toString());
                if(!matcher.matches()){
                    first_password.setError("비밀번호는 9~12자리 사이의 영문,숫자,특수문자 조합이여야 합니다.");
                    password_format_check = false;
                }
                if(first_password.getText().toString().contains(" ")){
                    first_password.setError("비밀번호는 공백을 포함하지 않습니다.");
                    password_format_check = false;
                }
                if (matcher.matches() && !first_password.getText().toString().contains(" ")) {
                    Drawable icon = getResources().getDrawable(R.drawable.equal);
                    icon.setBounds(0, 0, 80, 80);
                    first_password.setError("사용가능한 비밀번호 입니다.", icon);
                    password_format_check = true;
                }

            }
        });

        /** 비밀번호 일치 여부 **/
        second_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (first_password.getText().toString().equals(second_password.getText().toString())) {
                    set_image.setImageResource(R.drawable.equal);
                    password_equal_check = true;
                } else {
                    set_image.setImageResource(R.drawable.not_equal);
                    password_equal_check = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /** 전화번호 인증 **/

        view.findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /** api 번호 체크 **/
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String phone_number = input_phone.getText().toString().trim();
                            URL url = new URL("https://api.bluemango.me/auth/checkphone/");
                            HttpsURLConnection myconnection = (HttpsURLConnection) url.openConnection();
                            myconnection.setRequestMethod("POST");  //post, get 나누기
                            myconnection.setRequestProperty("Content-Type", "application/json"); // 데이터 json인 경우 세팅 , setrequestProperty 헤더인 경우

                            String phone = "{\"phone\":" + "\"" + phone_number + "\"}";
                            byte[] outputInBytes = phone.getBytes(StandardCharsets.UTF_8);    //post 인 경우 body 채우는 곳
                            OutputStream os = myconnection.getOutputStream();
                            os.write(outputInBytes);
                            os.close();
                            int a = myconnection.getResponseCode();
                            String to = Integer.toString(a);
                            Log.d("api 연결", to);
                            if (myconnection.getResponseCode() == 200 || myconnection.getResponseCode() == 403) {
                                /** 리스폰스 데이터 받는 부분*/
                                InputStream responseBody = myconnection.getInputStream();
                                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
                                JsonReader jsonReader = new JsonReader(responseBodyReader);
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName();
                                    if (key.equals("exist")) {
                                        boolean phone_check = jsonReader.nextBoolean();
                                        if (!phone_check) {
                                            if (phone_number.isEmpty() || phone_number.length() < 10) {
                                                input_phone.setError("잘못된 번호입력입니다.");
                                                input_phone.requestFocus();
                                                return;
                                            }
                                            if (phone_number.startsWith("0")) {
                                                phone_number = phone_number.substring(1);
                                            }
//                                            AlertDialog.Builder auth_alert = new AlertDialog.Builder(activity);
//                                            auth_alert.setTitle("Bokjipang 인증 서비스");
//                                            auth_alert.setMessage("사용가능한 전화번호 입니다.");
//                                            auth_alert.setPositiveButton("예", null);
//                                            auth_alert.create().show();
                                            Log.d("phone_check", "success");
//                                            verify_layout.setVisibility(View.VISIBLE);
//                                            startPhoneNumberVerification(phone_number);
                                        } else {
                                            Log.d("phone_check","failed");
//                                            AlertDialog.Builder auth_alert = new AlertDialog.Builder(activity);
//                                            auth_alert.setTitle("Bokjipang 인증 서비스");
//                                            auth_alert.setMessage("이미 가입된 전화번호 입니다.");
//                                            auth_alert.setPositiveButton("예", null);
//                                            auth_alert.create().show();
                                        }
//                                        Log.d("phone_check", Boolean.toString(phone_check));
                                        break;
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.close();
                                myconnection.disconnect();
                            } else {
                                Log.d("api 연결", to);
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                });

            }
        });
        view.findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = confirm_code.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    confirm_code.setError("잘못된 인증번호입니다.");
                    confirm_code.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });
        return view;
    }


    /**변수 초기화*/
    public void init_varaibles(View view) {
        back_login = view.findViewById(R.id.back);
        checkBox1 = (CheckBox)view.findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)view.findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)view.findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox)view.findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox)view.findViewById(R.id.checkBox5);
        input_phone = (EditText) view.findViewById(R.id.input_phone);
        first_password = (EditText) view.findViewById(R.id.password);
        second_password = (EditText) view.findViewById(R.id.confirm_password);
        name = (EditText) view.findViewById(R.id.name);
        age = (EditText) view.findViewById(R.id.age);
        radio_group = (RadioGroup) view.findViewById(R.id.radio_group);
        radio_man = (RadioButton) view.findViewById(R.id.radio_man);
        radio_woman = (RadioButton) view.findViewById(R.id.radio_woman);
        txt_address = (EditText) view.findViewById(R.id.txt_address);
        btn_search = (Button) view.findViewById(R.id.WebView_btn);
        confirm_code = view.findViewById(R.id.confirm_code);
        set_image = view.findViewById(R.id.setImage);
        auth_button = (Button) view.findViewById(R.id.buttonContinue);
        verify_layout = (LinearLayout) view.findViewById(R.id.verify_layout);
    }

    public String checkbox_ischecked(String str) {
        String result =str;
        boolean t = true;
        boolean f = false;
        if(result!= null)
            result = result + "\"interest\": {\"장애인\":";
        if(checkBox1.isChecked())
            result = result+t+",\"한부모\":";
        else
            result = result+f+",\"한부모\":";
        if(checkBox2.isChecked())
            result = result+t+",\"다문화\":";
        else
            result = result+f+",\"다문화\":";
        if(checkBox3.isChecked())
            result = result+t+",\"고령자\":";
        else
            result = result+f+",\"고령자\":";
        if(checkBox4.isChecked())
            result = result+t+",\"저소득\":";
        else
            result = result+f+",\"저소득\":";
        if(checkBox5.isChecked())
            result = result+t+"}}";
        else
            result = result+f+"}}";
        return result;
    }

    /**webview_address에서 addres 정보 받아와서 출력 및 js 저장*/
    public String bundle_receive(Bundle bundle) {
        if (bundle != null) {
            txt_address.setText(String.format("(%s) %s %s", bundle.getString("arg1"), bundle.getString("arg2"), bundle.getString("arg3")));
            input_phone.setText(bundle.getString("input_phone"));
            first_password.setText(bundle.getString("first_password"));
            second_password.setText(bundle.getString("second_password"));
            name.setText(bundle.getString("name"));
            age.setText(bundle.getString("age"));
            auth_checked = bundle.getString("auth_checked");
            if (bundle.getString("gender").equals("1")) {
                radio_man.setChecked(true);
                gender = 1;
            } else {
                radio_woman.setChecked(true);
                gender = 2;
            }

            if (bundle.getString("auth_checked").equals("true")) {
                auth_button.setEnabled(false);
                input_phone.setClickable(false);
                input_phone.setFocusable(false);
                input_phone.setEnabled(false);
                input_phone.setFocusableInTouchMode(false);
                verify_layout.setVisibility(View.INVISIBLE);
            }
            return "{\"phone\" :\"" +
                    bundle.getString("input_phone") +"\","+
                    "\"password\" :\"" +
                    bundle.getString("second_password") +"\","+
                    "\"name\" : \"" +
                    bundle.getString("name") +"\","+
                    "\"gender\":" +
                    gender +","+
                    "\"age\":" +
                    bundle.getString("age") +","+
                    "\"address\":\"" +
                    String.format("(%s) %s %s", bundle.getString("arg1"), bundle.getString("arg2"), bundle.getString("arg3")) +"\",";
        }
        return null;
    }

    /**버튼 클릭시 도로명 주소 api 인 webview_address로 이동*/
    public void goto_address_api(){
        if (btn_search != null) {
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /** webview address로 데이터 다 가져가기*/
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("input_phone", input_phone.getText().toString());
                    bundle2.putString("first_password", first_password.getText().toString());
                    bundle2.putString("second_password", second_password.getText().toString());
                    bundle2.putString("name", name.getText().toString());
                    bundle2.putString("age", age.getText().toString());
                    bundle2.putString("gender", String.valueOf(gender));
                    bundle2.putString("auth_checked", auth_checked);

                    Webview_address.setArguments(bundle2);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, Webview_address);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }

    private void startPhoneNumberVerification(String phoneNumber) {


        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+82"+phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code = credential.getSmsCode();
            if (code != null) {
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.d(TAG, "Firebase auth credential Error!!");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.d(TAG, "Too many Firebase request!!");
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

            mVerificationID = verificationId;
//            mResendToken = token;
        }
    };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationID, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "전화 인증 성공");
                            auth_checked = "true";
                            FirebaseUser user = task.getResult().getUser();
                            auth_button.setEnabled(false);
                            input_phone.setClickable(false);
                            input_phone.setFocusable(false);
                            input_phone.setEnabled(false);
                            input_phone.setFocusableInTouchMode(false);
                            verify_layout.setVisibility(View.INVISIBLE);

                            AlertDialog.Builder auth_alert = new AlertDialog.Builder(activity);
                            auth_alert.setTitle("Bokjipang 인증 서비스");
                            auth_alert.setMessage("인증 완료");
                            auth_alert.setPositiveButton("예", null);
                            auth_alert.create().show();
                        } else {
                            Log.w(TAG, "전화 인증 실패", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // 있을 수 있는 인증 예외처리들 모음
                            }
                        }
                    }
                });
    }
}

