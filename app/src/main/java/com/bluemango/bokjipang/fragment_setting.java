package com.bluemango.bokjipang;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class fragment_setting extends Fragment {
    private fragment_privacy fragment_privacy = new fragment_privacy();
    private fragment_mypost fragment_mypost = new fragment_mypost();
    private fragment_service fragment_service = new fragment_service();
    private fragment_alarm fragment_alarm = new fragment_alarm();

    private static final String TAG = "MY_POST";
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        TextView my_post = view.findViewById(R.id.my_post);
        my_post.setOnClickListener(new View.OnClickListener() {         // 내 게시물 관리 페이지 이동
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click");
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_mypost);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        TextView privacy = view.findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_privacy);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        TextView service = view.findViewById(R.id.service);
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_service);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        TextView alarm = view.findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment_alarm);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }
}