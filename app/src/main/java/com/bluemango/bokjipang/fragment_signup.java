package com.bluemango.bokjipang;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_signup extends Fragment {

    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_signup, container, false);
        TextView back_login = view.findViewById(R.id.back);
        EditText first_password = view.findViewById(R.id.password);
        EditText second_password = view.findViewById(R.id.confirm_password);


        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           // 회원가입 뒤로가기
                MainActivity activity = (MainActivity) getActivity();
                Fragment sign_fm = getFragmentManager().findFragmentById(R.id.fragment_container); // 현재 fragment 즉, 현재 fragment 추출
                activity.fm.beginTransaction().remove(sign_fm).commit();
                activity.fm.popBackStack();

            }
        });
//        second_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(first_password.getText().toString().equals(second_password.getText().toString())){
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        return view;
    }
}

