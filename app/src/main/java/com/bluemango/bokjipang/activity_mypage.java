package com.bluemango.bokjipang;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class activity_mypage extends AppCompatActivity {

    TextView back_btn;
    TextView btn_logout;
    Activity activity = this;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        /** 내 정보 뒤로가기 */
        back_btn = findViewById(R.id.mypage_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        /** 비밀번호 변경 액티비티 이동 */
        TextView go_changePW = findViewById(R.id.change_password);
        go_changePW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent changePW = new Intent(v.getContext(), activity_changepw.class);
                startActivity(changePW);
            }
        });

        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) MainActivity.context;
                activity.Shared_auto_login.edit().putBoolean("login",false).apply();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}