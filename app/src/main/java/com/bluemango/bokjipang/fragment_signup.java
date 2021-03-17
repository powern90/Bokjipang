package com.bluemango.bokjipang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class fragment_signup extends Fragment {

    EditText first_password, second_password, input_phone, name, age, txt_address;
    Webview_address Webview_address = new Webview_address();
    RadioGroup radio_group;
    TextView back_login;
    RadioButton radio_man, radio_woman;
    String gender = "", interested="";
    Button btn_search;
    CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
    JSONObject js;

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_signup, container, false);
        js = new JSONObject();

        init_varaibles(view);       //변수 초기화
        /**성별 라디오버튼 체크*/
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_man) {
                    gender = "남성";
                } else {
                    gender = "여성";
                }
            }
        });

        /** webview_address에서 addres 정보 받아와서 출력하기 및 js 저장*/
        Bundle bundle = getArguments();
        try {
            bundle_receive(bundle);
            checkbox_ischecked();               //이 부분 회원가입 버튼 누를때로 위치 이동해야됩니다. @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**버튼 클릭시 도로명 주소 api 인 webview_address로 이동*/
        goto_address_api();

        // 회원가입 뒤로가기
        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                Fragment sign_fm = getFragmentManager().findFragmentById(R.id.fragment_container); // 현재 fragment 즉, 현재 fragment 추출
                activity.fm.beginTransaction().remove(sign_fm).commit();
                activity.fm.popBackStack();

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
    }

    public void checkbox_ischecked() throws JSONException {
        if(checkBox1.isChecked())
            interested = interested + checkBox1.getText() + ",";
        if(checkBox2.isChecked())
            interested = interested + checkBox2.getText() + ",";
        if(checkBox3.isChecked())
            interested = interested + checkBox3.getText() + ",";
        if(checkBox4.isChecked())
            interested = interested + checkBox4.getText() + ",";
        if(checkBox5.isChecked())
            interested = interested + checkBox5.getText() + ",";
        if(!interested.equals("")){
            interested = interested.substring(0, interested.length()-1);
        }
        js.put("interested",interested);
    }

    /**webview_address에서 addres 정보 받아와서 출력 및 js 저장*/
    public void bundle_receive(Bundle bundle) throws JSONException {
        if (bundle != null) {
            txt_address.setText(String.format("(%s) %s %s", bundle.getString("arg1"), bundle.getString("arg2"), bundle.getString("arg3")));
            input_phone.setText(bundle.getString("input_phone"));
            first_password.setText(bundle.getString("first_password"));
            second_password.setText(bundle.getString("second_password"));
            name.setText(bundle.getString("name"));
            age.setText(bundle.getString("age"));
            if (bundle.getString("gender").equals("남성")) {
                radio_man.setChecked(true);
                gender = "남성";
            } else {
                radio_woman.setChecked(true);
                gender = "여성";
            }
            /** json 형식으로 저장*/
            js.put("input_phone", bundle.getString("input_phone"));
            js.put("first_password", bundle.getString("first_password"));
            js.put("second_password", bundle.getString("second_password"));
            js.put("name", bundle.getString("name"));
            js.put("age", bundle.getString("age"));
            js.put("gender", gender);
        }
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
                    bundle2.putString("gender", gender);
                    Webview_address.setArguments(bundle2);
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, Webview_address);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
}

