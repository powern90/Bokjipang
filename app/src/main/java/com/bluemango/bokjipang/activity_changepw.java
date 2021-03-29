package com.bluemango.bokjipang;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_changepw extends AppCompatActivity {
    EditText new_password;
    EditText new_password_confirm;
    ImageView set_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);
//        boolean password_format_check = false;
        /** 비밀번호 입력형식 완료여부 **/
        /** 1. 영문(대소문자 구분), 숫자, 특수문자 조합 **/
        /** 2. 9~12자리 사이 문자 **/
        /** 3. 공백문자 사용 불가 **/
        new_password = findViewById(R.id.new_pw_txt);
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
                if(!matcher.matches()){
                    new_password.setError("비밀번호는 9~12자리 사이의 영문,숫자,특수문자 조합이여야 합니다.");
//                    password_format_check = false;
                }
                if(new_password.getText().toString().contains(" ")){
                    new_password.setError("비밀번호는 공백을 포함하지 않습니다.");
//                    password_format_check = false;
                }
                if (matcher.matches() && !new_password.getText().toString().contains(" ")) {
                    Drawable icon = getResources().getDrawable(R.drawable.equal);
                    icon.setBounds(0, 0, 80, 80);
                    new_password.setError("사용가능한 비밀번호 입니다.", icon);
//                    password_format_check = true;
                }
            }
        });
        /** 비밀번호 확인 일치 여부 */
        new_password_confirm = findViewById(R.id.new_pw_confirm_txt);
        set_image = findViewById(R.id.setImage);
        new_password_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Drawable equal_icon = getResources().getDrawable(R.drawable.equal);
                equal_icon.setBounds(0, 0, 80, 80);
                if(new_password.getText().toString().equals(new_password_confirm.getText().toString())){
                    new_password_confirm.setError("비밀번호가 일치합니다.",equal_icon);
                }else{
                    new_password_confirm.setError("비밀번호가 일치하지 않습니다.");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}