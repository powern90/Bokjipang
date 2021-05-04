package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_mypage extends AppCompatActivity {
    final FragmentManager fm = getSupportFragmentManager();
    fragment_mypage fragment_mypage = new fragment_mypage();
    SharedPreferences Shared_user_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        Shared_user_info = getSharedPreferences("token",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_info",MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_id", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("user_pwd", MODE_PRIVATE);
        Shared_user_info = getSharedPreferences("home_interest", MODE_PRIVATE);


        fm.beginTransaction().replace(R.id.fragment_container,fragment_mypage).commit();

    }
}