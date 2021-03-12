package com.bluemango.bokjipang;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.zip.Inflater;

public class fragment_login extends Fragment {
    private EditText id_text, pw_text;
    private Button btn_login;
    private fragment_signup fragment_signup = new fragment_signup();



    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_login, container, false);


        TextView signup = view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {          //회원가입 페이질 ㅗ이동
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.fm.beginTransaction().replace(R.id.fragment_container,fragment_signup).commit();
            }
        });

        return view;
    }

}

